package com.appliedrec.verid3.spoofdevicedetection.cloud

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    @Serializable(with = ByteArraySerializer::class)
    val image: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RequestBody) return false

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}
