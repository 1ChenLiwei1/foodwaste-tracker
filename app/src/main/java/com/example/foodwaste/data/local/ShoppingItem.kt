package com.example.foodwaste.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val name: String,

    @field:JvmField
    @ColumnInfo(name = "checked")
    val checked: Boolean = false
)

