package com.appliedrec.verid3.spoofdevicedetection.cloud

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpoofDeviceDetectionOnlineTest : SpoofDeviceDetectionBaseTest() {

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val string = context.assets.open("config.json").readAllBytes()
            .toString(Charsets.UTF_8)
        val config = Json.decodeFromString<Config>(string)
        spoofDeviceDetection = SpoofDeviceDetection(
            config.apiKey,
            config.url.toHttpUrl()
        )
    }
}

@Serializable
private data class Config(val apiKey: String, val url: String)