package com.msf.itunessearch.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toFormatDate(): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(this)
        ?.let { SimpleDateFormat("MMM dd, y", Locale.getDefault()).format(it) } ?: ""

fun String.isExplicit(): Boolean =
    this.equals("explicit", true)
