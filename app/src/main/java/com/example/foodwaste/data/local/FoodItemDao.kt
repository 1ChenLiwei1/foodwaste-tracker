package com.example.foodwaste.data.local

import androidx.room.*
import com.example.foodwaste.data.model.FoodItem
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

@Dao
interface FoodItemDao {

    @Query("""
        SELECT * FROM food_items
        ORDER BY 
          CASE WHEN expiryDate IS NULL THEN 1 ELSE 0 END,
          expiryDate ASC
    """)
    fun observeAll(): kotlinx.coroutines.flow.Flow<List<com.example.foodwaste.data.model.FoodItem>>

    @Query("""
        SELECT * FROM food_items
        WHERE expiryDate IS NOT NULL AND expiryDate <= :until
        ORDER BY expiryDate ASC
    """)
    fun observeExpiring(until: LocalDate): kotlinx.coroutines.flow.Flow<List<com.example.foodwaste.data.model.FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: com.example.foodwaste.data.model.FoodItem)

    @Delete
    suspend fun delete(item: com.example.foodwaste.data.model.FoodItem)
}