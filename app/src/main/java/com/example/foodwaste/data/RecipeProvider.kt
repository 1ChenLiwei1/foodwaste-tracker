package com.example.foodwaste.data

import com.example.foodwaste.data.model.Recipe

object RecipeProvider {

    val recipes = listOf(
        Recipe(
            name = "Scrambled Eggs",
            ingredients = listOf("egg", "salt", "butter"),
            category = "Breakfast",
            steps = listOf(
                "Crack eggs into a bowl",
                "Add salt and beat well",
                "Melt butter in pan",
                "Cook eggs on medium heat while stirring"
            )
        ),
        Recipe(
            name = "Milk Tea",
            ingredients = listOf("milk", "tea", "sugar"),
            category = "Drink",
            steps = listOf(
                "Boil water and brew tea",
                "Add hot milk",
                "Sweeten with sugar"
            )
        ),
        Recipe(
            name = "Fried Rice",
            ingredients = listOf("rice", "egg", "oil", "salt"),
            category = "Lunch",
            steps = listOf(
                "Heat oil in pan",
                "Add egg and scramble",
                "Add rice and stir fry",
                "Season with salt"
            )
        )
    )
}



