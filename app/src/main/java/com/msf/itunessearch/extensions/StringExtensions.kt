package com.msf.itunessearch.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun String.toFormatDate(): String =
    try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(this)?.let { dateParsed ->
            SimpleDateFormat("MMM dd, y", Locale.getDefault()).format(dateParsed)
        } ?: ""
    } catch (e: ParseException) {
        ""
    }

fun String.isExplicit(): Boolean =
    this.equals("explicit", true)
