package com.example.foodwaste.data.model

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val category: String,
    val steps: List<String>
)

