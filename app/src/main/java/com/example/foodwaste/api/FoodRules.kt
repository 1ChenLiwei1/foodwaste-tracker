package com.example.foodwaste.api

import org.threeten.bp.LocalDate

object FoodRules {

    fun classify(categoryString: String?): String {
        val text = categoryString?.lowercase() ?: return "Unknown"

        return when {
            "milk" in text || "dairy" in text -> "Dairy"
            "egg" in text -> "Eggs"
            "meat" in text -> "Meat"
            "bread" in text || "bakery" in text -> "Bakery"
            "vegetable" in text -> "Vegetable"
            "fruit" in text -> "Fruit"
            "snack" in text -> "Snack"
            "beverage" in text -> "Beverage"
            else -> "Unknown"
        }
    }

    fun defaultExpiry(category: String): LocalDate =
        when (category) {
            "Dairy" -> LocalDate.now().plusDays(7)
            "Eggs" -> LocalDate.now().plusDays(14)
            "Meat" -> LocalDate.now().plusDays(3)
            "Bakery" -> LocalDate.now().plusDays(3)
            "Vegetable" -> LocalDate.now().plusDays(5)
            "Fruit" -> LocalDate.now().plusDays(7)
            "Snack" -> LocalDate.now().plusDays(180)
            "Beverage" -> LocalDate.now().plusDays(90)
            else -> LocalDate.now().plusDays(7)
        }
}
