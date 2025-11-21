package com.example.mobile_finalproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mobile_finalproject.models.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var addDeckButton = findViewById<Button>(R.id.btnAddDeck)

        // Add new deck.
        addDeckButton.setOnClickListener {
            openEditDeckActivity(-1);
        }

        loadDecks()
    }

    private fun loadDecks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getDecks()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val decks = response.body() ?: emptyList()

                        createDeckItems(decks)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun createDeckItems(decks: List<Deck>) {
        val container = findViewById<LinearLayout>(R.id.decksContainer)

        decks.forEach { deck ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.deck_selection_item, container, false)

            val textView = itemView.findViewById<TextView>(R.id.tvName)
            textView.text = deck.name

            val editButton = itemView.findViewById<ImageButton>(R.id.btnPencil)
            editButton.setOnClickListener {
                openEditDeckActivity(deck.id)
            }

            container.addView(itemView)
        }
    }

    private fun openEditDeckActivity(deckId: Int) {
        val intent = Intent(this, EditDeckActivity::class.java)
        intent.putExtra("DECK_ID", deckId)
        startActivity(intent)
    }
}