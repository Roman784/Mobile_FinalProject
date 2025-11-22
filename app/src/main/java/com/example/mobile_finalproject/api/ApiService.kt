package com.example.mobile_finalproject.api
import com.example.mobile_finalproject.models.Card
import com.example.mobile_finalproject.models.Deck
import retrofit2.http.GET
import retrofit2.Response;
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/decks")
    suspend fun getDecks(): Response<List<Deck>>

    @GET("api/decks/{id}")
    suspend fun getDeck(@Path("id") id: Int): Response<Deck>

    @POST("api/decks")
    suspend fun createDeck(@Body deck: Deck): Response<Deck>

    @PUT("api/decks/{id}")
    suspend fun updateDeck(@Path("id") id: Int, @Body deck: Deck): Response<Unit>

    @DELETE("api/decks/{id}")
    suspend fun deleteDeck(@Path("id") id: Int): Response<Unit>

    @GET("api/cards/deck/{id}")
    suspend fun getCards(@Path("id") id: Int): Response<List<Card>>

    @POST("api/cards")
    suspend fun createCard(@Body card: Card): Response<Card>

    @PUT("api/cards/{id}")
    suspend fun updateCard(@Path("id") id: Int, @Body card: Card): Response<Unit>

    @DELETE("api/cards/{id}")
    suspend fun deleteCard(@Path("id") id: Int): Response<Unit>
}