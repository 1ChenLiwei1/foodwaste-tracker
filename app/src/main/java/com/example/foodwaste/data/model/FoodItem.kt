package com.example.foodwaste.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val quantity: Int,
    val category: String,
    val expiryDate: LocalDate?,
    val barcode: String? = null,
    val addedAt: LocalDate = LocalDate.now()
)