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
            name = "Chinese Fried Rice",
            category = "Main",
            ingredients = listOf("Rice", "Egg", "Carrot", "Soy Sauce", "Spring Onion"), // ★ 添加 Spring Onion
            steps = listOf(
                "Heat oil in pan",
                "Add egg and scramble",
                "Add rice and vegetables",
                "Season with soy sauce"
            )
        ),

        Recipe(
            name = "Tomato Egg Stir-Fry",
            category = "Main",
            ingredients = listOf("Tomato", "Egg", "Salt", "Sugar", "Oil"),
            steps = listOf(
                "Cut tomatoes into wedges",
                "Scramble eggs and set aside",
                "Stir fry tomatoes until soft",
                "Add eggs back in",
                "Season with salt and sugar"
            )
        ),

        Recipe(
            name = "Butter Garlic Shrimp",
            category = "Main",
            ingredients = listOf("Shrimp", "Butter", "Garlic", "Salt", "Pepper"),
            steps = listOf(
                "Melt butter in pan",
                "Add garlic and sauté",
                "Add shrimp and cook until pink",
                "Season and serve"
            )
        ),

        Recipe(
            name = "Mushroom Cream Pasta",
            category = "Main",
            ingredients = listOf("Pasta", "Mushroom", "Cream", "Garlic", "Salt"),
            steps = listOf(
                "Cook pasta",
                "Sauté mushrooms and garlic",
                "Add cream and simmer",
                "Mix pasta and sauce"
            )
        ),

        Recipe(
            name = "Chicken Fried Noodles",
            category = "Main",
            ingredients = listOf("Noodles", "Chicken", "Carrot", "Soy Sauce", "Cabbage"),
            steps = listOf(
                "Boil noodles",
                "Cook chicken",
                "Stir fry vegetables",
                "Add noodles and soy sauce"
            )
        ),

        Recipe(
            name = "Pancakes",
            category = "Dessert",
            ingredients = listOf("Flour", "Egg", "Milk", "Sugar", "Butter"),
            steps = listOf(
                "Mix flour, egg, milk, sugar",
                "Pour batter onto pan",
                "Cook both sides until golden brown",
                "Serve with butter or syrup"
            )
        ),
    )
}



