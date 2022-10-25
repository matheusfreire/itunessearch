package com.msf.itunessearch.extensions

import io.kotlintest.shouldBe
import org.junit.Test

internal class LongExtensionsTest {

    @Test
    fun `GIVEN milliseconds represents duration WHEN convertDurationToHumanReadable is called THEN will return a string readable by human`() {
        val duration: Long = 173233

        duration.convertDurationToHumanReadable() shouldBe "02:53"
    }
}
