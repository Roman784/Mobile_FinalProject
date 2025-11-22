package com.example.mobile_finalproject.api
import com.example.mobile_finalproject.models.Card
import com.example.mobile_finalproject.models.Deck
import retrofit2.http.GET
import retrofit2.Response;
import retrofit2.http.Path

interface ApiService {
    @GET("api/decks")
    suspend fun getDecks(): Response<List<Deck>>
    @GET("api/decks/{deckId}")
    suspend fun getDeck(@Path("deckId") deckId: Int): Response<Deck>
    @GET("api/cards/deck/{deckId}")
    suspend fun getCards(@Path("deckId") deckId: Int): Response<List<Card>>
}