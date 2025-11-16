package com.example.foodwaste.data.ai

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GeminiService {

    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generate(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

// ---------- Data Classes ----------
data class GeminiRequest(
    val contents: List<GeminiContent>
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiResponse(
    @SerializedName("candidates") val candidates: List<GeminiCandidate>
)

data class GeminiCandidate(
    val content: GeminiContentResponse?
)

data class GeminiContentResponse(
    val parts: List<GeminiPartResponse>?
)

data class GeminiPartResponse(
    val text: String?
)
