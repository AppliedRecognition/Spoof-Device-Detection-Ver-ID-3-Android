package com.appliedrec.verid3.spoofdevicedetection.cloud

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.appliedrec.verid3.common.IImage
import com.appliedrec.verid3.common.serialization.toBitmap
import com.appliedrec.verid3.spoofdevicedetection.core.DetectedSpoof
import com.appliedrec.verid3.spoofdevicedetection.core.SpoofDetectionCore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Constructor
 *
 * @property apiKey API key
 * @property url Server URL
 */
class SpoofDeviceDetection(val apiKey: String, val url: HttpUrl) : SpoofDetectionCore() {

    /**
     * Constructor that uses API key and server URL
     *
     * @param apiKey API key
     * @param url Server URL
     */
    constructor(apiKey: String, url: String) : this(apiKey, url.toHttpUrl())

    /**
     * Constructor that uses API key and server URL from Android manifest
     *
     * @param context Application context
     */
    constructor(context: Context) : this(
        resolveMetaData(context, "com.appliedrec.spoofdevicedetection.apikey"),
        resolveMetaData(context, "com.appliedrec.spoofdevicedetection.serverurl")
    )

    companion object {
        private fun resolveMetaData(context: Context, key: String): String {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val value = appInfo.metaData?.getString(key)
            require(!value.isNullOrEmpty()) {
                "Missing or empty meta-data for key: $key"
            }
            return value
        }
    }

    internal val inputImageSize: Int = 640
    private val httpClient = OkHttpClient()

    override suspend fun detectSpoofDevicesInImage(image: IImage): List<DetectedSpoof> {
        val request = Request.Builder()
            .url(url)
            .header("x-api-key", apiKey)
            .post(prepareRequestBody(image))
            .build()
        val body = httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected HTTP code ${response.code}")
            }
            response.body.string()
        }
        return parseResponseBody(body, image)
    }

    private fun prepareRequestBody(image: IImage): RequestBody {
        val bitmap = scaleImage(image.toBitmap())
        val jpeg = bitmapToJpeg(bitmap)
        val body = RequestBody(jpeg)
        val json = Json.encodeToString(body)
        return json.toRequestBody("image/jpeg".toMediaTypeOrNull())
    }

    private fun parseResponseBody(body: String, image: IImage): List<DetectedSpoof> {
        return Json.decodeFromString<List<DetectedSpoof>>(body)
            .mapNotNull { spoofDevice ->
                if (spoofDevice.confidence < confidenceThreshold) {
                    null
                } else {
                    var topLeft = PointF(spoofDevice.xmin, spoofDevice.ymin)
                    var bottomRight = PointF(spoofDevice.xmax, spoofDevice.ymax)
                    topLeft = mapPointToOriginalImage(topLeft, image.width, image.height)
                    bottomRight = mapPointToOriginalImage(bottomRight, image.width, image.height)
                    DetectedSpoof(spoofDevice.confidence, topLeft.x, topLeft.y, bottomRight.x, bottomRight.y)
                }
            }
    }

    private fun bitmapToJpeg(bitmap: Bitmap): ByteArray {
        return ByteArrayOutputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        }
    }

    internal fun scaleImage(input: Bitmap): Bitmap {
        val inWidth = input.width
        val inHeight = input.height
        val scale = inputImageSize / max(inWidth.toFloat(), inHeight.toFloat())
        val outWidth = (inWidth * scale).roundToInt()
        val outHeight = (inHeight * scale).roundToInt()
        val scaledBitmap = input.scale(outWidth, outHeight)
        val output = createBitmap(inputImageSize, inputImageSize)
        val canvas = Canvas(output)
        canvas.drawColor(Color.BLACK)
        val left = ((inputImageSize - outWidth) / 2f)
        val top = ((inputImageSize - outHeight) / 2f)
        canvas.drawBitmap(scaledBitmap, left, top, null)
        return output
    }

    private fun mapPointToOriginalImage(
        point: PointF,
        originalImageWidth: Int,
        originalImageHeight: Int
    ): PointF {
        val scale = inputImageSize / max(originalImageWidth.toFloat(), originalImageHeight.toFloat())
        val outWidth = (originalImageWidth * scale).roundToInt()
        val outHeight = (originalImageHeight * scale).roundToInt()
        val left = ((inputImageSize - outWidth) / 2f)
        val top = ((inputImageSize - outHeight) / 2f)
        val origX = (point.x - left) / scale
        val origY = (point.y - top) / scale
        return PointF(origX, origY)
    }
}

