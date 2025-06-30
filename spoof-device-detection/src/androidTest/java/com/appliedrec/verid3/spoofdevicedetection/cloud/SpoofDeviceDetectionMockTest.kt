package com.appliedrec.verid3.spoofdevicedetection.cloud

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpoofDeviceDetectionMockTest : SpoofDeviceDetectionBaseTest() {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        val body = InstrumentationRegistry.getInstrumentation().context
            .assets
            .open("response.json")
            .readAllBytes()
            .toString(Charsets.UTF_8)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.enqueue(
            MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json")
        )
        spoofDeviceDetection = SpoofDeviceDetection(
            "",
            mockWebServer.url("/detect_spoof_devices")
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}