package com.example.foodwaste.util

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object DateUtil {
    val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    fun parseOrNull(s: String?): LocalDate? = s?.let { runCatching { LocalDate.parse(it, fmt) }.getOrNull() }
}