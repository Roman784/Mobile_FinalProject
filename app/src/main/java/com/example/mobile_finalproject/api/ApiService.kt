package com.example.mobile_finalproject.api
import com.example.mobile_finalproject.models.Deck
import retrofit2.http.GET
import retrofit2.Response;

interface ApiService {
    @GET("api/Decks")
    suspend fun getDecks(): Response<List<Deck>>
}