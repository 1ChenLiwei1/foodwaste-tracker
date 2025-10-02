package com.example.foodwaste.util

import com.example.foodwaste.data.model.FoodItem
import org.threeten.bp.LocalDate

object ExpiryChecker {
    fun isExpiringSoon(item: FoodItem, days: Long = 3): Boolean {
        val d = item.expiryDate ?: return false
        return !d.isBefore(LocalDate.now()) && d.isBefore(LocalDate.now().plusDays(days + 1))
    }
}