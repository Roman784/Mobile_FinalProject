package com.example.mobile_finalproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.mobile_finalproject.models.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DeckApp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d(TAG, "Старт")
        loadDecks()
    }

    private fun loadDecks() {
        Log.d(TAG, "Начало загрузки данных...")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getDecks()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val decks = response.body() ?: emptyList()

                        displayDecksInConsole(decks)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun displayDecksInConsole(decks: List<Deck>) {
        if (decks.isEmpty()) {
            return
        }

        decks.forEach { deck ->
            Log.d(TAG, "Deck: id=${deck.id}, name='${deck.name}'")
        }
    }
}