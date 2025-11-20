package com.example.foodwaste

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate

/**
 * Example local unit test, runs on the development machine.
 */
class ExampleUnitTest {

    // 1. Simple Math Test
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    // 2. String Test: Verify Formatted Name
    @Test
    fun formatName_isCorrect() {
        val rawName = "   spring onion   "
        val formatted = rawName.trim().replaceFirstChar { it.uppercase() }
        assertEquals("Spring onion", formatted)
    }

    // 3. FoodWaste: Determining Whether Food Has Expired
    @Test
    fun item_isExpired() {
        val today = LocalDate.of(2025, 1, 1)
        val expiry = LocalDate.of(2024, 12, 30)

        val isExpired = expiry.isBefore(today)

        assertTrue(isExpired)
    }

    // 4. FoodWaste: Determining if food is nearing expiration (expiration date within 3 days)
    @Test
    fun item_isExpiringSoon() {
        val today = LocalDate.of(2025, 1, 1)
        val expiry = LocalDate.of(2025, 1, 3)

        val daysLeft = java.time.Period.between(today, expiry).days
        val isSoon = daysLeft in 0..3

        assertTrue(isSoon)
    }

    // 5. FoodWaste: Automatic Shopping List Addition Logic
    @Test
    fun shoppingList_autoAdd() {
        val inventoryQuantity = 0
        val shouldAutoAdd = inventoryQuantity <= 0

        assertTrue(shouldAutoAdd)
    }
}
