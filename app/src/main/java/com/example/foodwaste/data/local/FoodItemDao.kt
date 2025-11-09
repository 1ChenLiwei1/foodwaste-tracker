package com.example.foodwaste.data.local

import androidx.room.*
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
    fun observeAll(): Flow<List<FoodItem>>

    @Query("""
        SELECT * FROM food_items 
        WHERE expiryDate IS NOT NULL AND expiryDate <= :until
        ORDER BY expiryDate ASC
    """)
    fun observeExpiring(until: LocalDate): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: FoodItem)

    @Delete
    suspend fun delete(item: FoodItem)

    @Query("DELETE FROM food_items")
    suspend fun clearAll()
}
