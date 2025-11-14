package com.example.foodwaste.data

import com.example.foodwaste.data.model.Recipe

object RecipeProvider {

    val recipes = listOf(
        Recipe(
            name = "Milk Omelette",
            ingredients = listOf("Milk", "Eggs"),
            steps = listOf(
                "Beat eggs with milk",
                "Heat pan and add oil",
                "Cook egg mixture until firm"
            )
        ),
        Recipe(
            name = "Fried Rice",
            ingredients = listOf("Rice", "Eggs"),
            steps = listOf(
                "Cook rice beforehand",
                "Stir-fry eggs",
                "Add rice and seasoning"
            )
        ),
        Recipe(
            name = "Cheese Toast",
            ingredients = listOf("Cheese"),
            steps = listOf(
                "Place cheese on bread",
                "Heat in toaster until melted"
            )
        )
    )
}


