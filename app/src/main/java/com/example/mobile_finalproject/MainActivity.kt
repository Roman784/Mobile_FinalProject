package com.example.mobile_finalproject

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.example.mobile_finalproject.models.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {
    private  lateinit var decks: ArrayList<Deck>
    private lateinit var decksContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        decks = arrayListOf()
        decksContainer = findViewById(R.id.decksContainer)
        val btnAddDeck = findViewById<Button>(R.id.btnAddDeck)

        // Add new deck.
        btnAddDeck.setOnClickListener {
            openEditDeckActivity(-1, "");
        }

        createFakeDecks()
        //loadDecks()
    }

    private fun loadDecks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getDecks()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        decks.addAll(response.body() ?: emptyList())
                        createDeckViews(decks)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun createDeckViews(decks: List<Deck>) {
        decks.forEach { deck ->
            createDeckView(deck)
        }
    }

    private fun createDeckView(deck: Deck) {
        val itemView = LayoutInflater.from(this)
            .inflate(R.layout.deck_selection_view, decksContainer, false)

        val textView = itemView.findViewById<TextView>(R.id.tvName)
        textView.text = deck.name

        val editButton = itemView.findViewById<ImageButton>(R.id.btnPencil)
        editButton.setOnClickListener {
            openEditDeckActivity(deck.id, deck.name)
        }

        decksContainer.addView(itemView)
    }

    private fun openEditDeckActivity(deckId: Int, deckName: String) {
        val intent = Intent(this, EditDeckActivity::class.java)
        intent.putExtra("DECK_ID", deckId)
        intent.putExtra("DECK_NAME", deckName)
        startActivity(intent)
    }

    /////////////////////////////////////////////////////////////////////////////

    private fun createFakeDecks() {
        for (i in 1..15) {
            decks.add(Deck(i, "fake $i"))
        }
        createDeckViews(decks)
    }
}