package com.example.foodwaste.data.ai

import com.example.foodwaste.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeminiRepository {

    private val apiKey: String = BuildConfig.GEMINI_API_KEY

    private val api = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeminiService::class.java)

    suspend fun generateRecipes(ingredients: List<String>): String {

        val prompt = """
            I have these ingredients: ${ingredients.joinToString(", ")}
            Generate 5 creative recipes I can cook.

            For each recipe include:
            - Recipe name
            - Ingredients
            - Instructions
            - Difficulty
            - Cooking time
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(prompt))
                )
            )
        )

        val res = api.generate(apiKey, request)

        return res.candidates
            .firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: "No recipe generated."
    }
}