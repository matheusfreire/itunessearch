package com.msf.itunessearch.extensions

import io.kotlintest.shouldBe
import org.junit.Test

internal class StringExtensionsTest {

    @Test
    fun `GIVEN string contains date valid WHEN toFormatDate is called THEN should return new string on new format`() {
        val date = "2018-06-07T12:00:00Z"

        date.toFormatDate() shouldBe "Jun 07, 2018"
    }

    @Test
    fun `GIVEN string couldn't be parse WHEN toFormatDate is called THEN should return new string empty`() {
        val date = "2018-06-07T12:00"

        date.toFormatDate().isEmpty() shouldBe true
    }

    @Test
    fun `GIVEN string contains explicit value WHEN isExplicit is called THEN should return true`() {
        val string = "explicit"

        string.isExplicit() shouldBe true
    }

    @Test
    fun `GIVEN string doesn't contains explicit value WHEN isExplicit is called THEN should return true`() {
        val string = "notExplicit"

        string.isExplicit() shouldBe false
    }

    @Test
    fun `GIVEN string contains explicit uppercase value WHEN isExplicit is called THEN should return true`() {
        val string = "EXPLICIT"

        string.isExplicit() shouldBe true
    }

    @Test
    fun `GIVEN string doesn't contains explicit uppercase value WHEN isExplicit is called THEN should return true`() {
        val string = "NOTEXPLICIT"

        string.isExplicit() shouldBe false
    }
}
