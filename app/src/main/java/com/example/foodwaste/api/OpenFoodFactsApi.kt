package com.example.foodwaste.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object OpenFoodFactsApi {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun lookup(barcode: String): ProductResult? {
        val url = "https://world.openfoodfacts.org/api/v2/product/$barcode.json"

        return try {
            val response = client.get(url).body<OpenFoodFactsResponse>()
            response.product
        } catch (e: Exception) {
            null
        }
    }
}

@Serializable
data class OpenFoodFactsResponse(
    val product: ProductResult? = null
)

@Serializable
data class ProductResult(
    @SerialName("product_name") val name: String? = null,
    @SerialName("categories") val categories: String? = null
)

