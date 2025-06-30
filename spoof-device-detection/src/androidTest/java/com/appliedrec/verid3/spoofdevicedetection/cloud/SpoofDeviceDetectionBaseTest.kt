package com.appliedrec.verid3.spoofdevicedetection.cloud

import android.graphics.BitmapFactory
import android.graphics.RectF
import androidx.test.platform.app.InstrumentationRegistry
import com.appliedrec.verid3.common.Image
import com.appliedrec.verid3.common.serialization.fromBitmap
import com.appliedrec.verid3.common.serialization.toBitmap
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

abstract class SpoofDeviceDetectionBaseTest {

    protected lateinit var spoofDeviceDetection: SpoofDeviceDetection
    private val testImage: Image by lazy {
        Image.fromBitmap(
            InstrumentationRegistry.getInstrumentation().context.assets
                .open("face_on_iPad_001.jpg")
                .use(BitmapFactory::decodeStream))
    }
    private val testImageFaceRect = RectF(1020f, 1420f, 2090f, 2770f)

    @Test
    fun testDetectSpoofDevicesInImage() = runBlocking {
        val spoofDevices = spoofDeviceDetection.detectSpoofDevicesInImage(testImage)
        assertEquals(1, spoofDevices.size)
        val spoofDevice = spoofDevices[0]
        assertTrue(spoofDevice.confidence > spoofDeviceDetection.confidenceThreshold)
    }

    @Test
    fun testDetectSpoofInImage() = runBlocking {
        val score = spoofDeviceDetection.detectSpoofInImage(testImage, testImageFaceRect)
        assertTrue(score > spoofDeviceDetection.confidenceThreshold)
    }

    @Test
    fun scaleBitmap() {
        val output = spoofDeviceDetection.scaleImage(testImage.toBitmap())
        assertEquals(output.width, spoofDeviceDetection.inputImageSize)
        assertEquals(output.height, spoofDeviceDetection.inputImageSize)
    }
}