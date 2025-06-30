package com.appliedrec.verid3.spoofdevicedetection.core

import android.graphics.RectF
import com.appliedrec.verid3.common.IImage
import com.appliedrec.verid3.common.SpoofDetection

abstract class SpoofDetectionCore : SpoofDetection {

    var confidenceThreshold: Float = 0.5f

    override suspend fun detectSpoofInImage(image: IImage, regionOfInterest: RectF?): Float {
        val spoofDevices = detectSpoofDevicesInImage(image)
        return regionOfInterest?.let { roi ->
            spoofDevices.maxOfOrNull { spoofDevice ->
                val rect = RectF(
                    spoofDevice.xmin,
                    spoofDevice.ymin,
                    spoofDevice.xmax,
                    spoofDevice.ymax
                )
                if (rect.contains(roi)) {
                    spoofDevice.confidence
                } else {
                    0f
                }
            }
        } ?: spoofDevices.maxOfOrNull { it.confidence } ?: 0f
    }

    abstract suspend fun detectSpoofDevicesInImage(image: IImage): List<DetectedSpoof>
}