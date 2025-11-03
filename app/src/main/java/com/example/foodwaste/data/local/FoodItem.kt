package com.example.foodwaste.data.local

import org.threeten.bp.LocalDate

data class FoodItem(
    val id: Int = 0,
    val name: String = "",
    val expiryDate: LocalDate = LocalDate.now()
)



