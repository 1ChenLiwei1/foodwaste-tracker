package com.example.foodwaste.data.local

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

class Converters {
    @TypeConverter fun fromEpochDay(value: Long?): LocalDate? =
        value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter fun localDateToEpoch(value: LocalDate?): Long? =
        value?.toEpochDay()
}