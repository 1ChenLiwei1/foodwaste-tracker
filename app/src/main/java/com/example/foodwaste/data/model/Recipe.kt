package com.example.foodwaste.data.model

data class Recipe(
    val id: String,
    val title: String,
    val ingredients: List<String>,
    val steps: List<String>
)