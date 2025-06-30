package com.appliedrec.verid3.spoofdevicedetection.core

import kotlinx.serialization.Serializable

@Serializable
data class DetectedSpoof(
    val confidence: Float,
    val xmin: Float,
    val ymin: Float,
    val xmax: Float,
    val ymax: Float
)