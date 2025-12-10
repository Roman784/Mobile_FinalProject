package com.example.mobile_finalproject

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.example.mobile_finalproject.dialogs.EditCardDialog
import com.example.mobile_finalproject.dialogs.EditCardDialog.EditCardDialogListener
import com.example.mobile_finalproject.models.Card
import com.example.mobile_finalproject.models.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditDeckActivity : BaseActivity(), EditCardDialogListener {
    private  lateinit var cards: ArrayList<Card>
    private lateinit var cardsContainer: LinearLayout
    private lateinit var scrollView: ScrollView
    private lateinit var edtDeckName: EditText
    private lateinit var deckName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deck_activity)

        val deckId = intent.getIntExtra("DECK_ID", -1)

        cards = arrayListOf()
        cardsContainer = findViewById(R.id.cardsContainer)
        scrollView = findViewById(R.id.scrollView)
        edtDeckName = findViewById(R.id.edtDeckName)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnAddCard = findViewById<Button>(R.id.btnAddCard)
        val btnTrash = findViewById<ImageButton>(R.id.btnTrash)

        btnSave.setOnClickListener {
            saveDeckName(deckId)
        }

        btnAddCard.setOnClickListener {
            createNewCard(deckId)
        }

        btnTrash.setOnClickListener {
            deleteDeck(deckId)
        }

        loadDeckName(deckId)
        loadCards(deckId)
    }

    private fun loadDeckName(deckId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getDeck(deckId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        deckName = response.body()?.name ?: "New deck"
                        edtDeckName.setText(deckName)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun loadCards(deckId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getCards(deckId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        cards.addAll(response.body() ?: emptyList())
                        createCardViews(cards)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun deleteDeck(deckId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.deleteDeck(deckId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        openMainActivity()
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun saveDeckName(deckId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newDeckName = edtDeckName.text.toString()
                if (!newDeckName.isEmpty()) deckName = newDeckName

                val response = RetrofitClient.apiService.updateDeck(deckId, Deck(deckId, deckName))
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        openStudyActivity(deckId)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun createNewCard(deckId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.createCard(Card(-1, deckId, "", ""))
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val newId = response.body()?.id ?: -1
                        val card = Card(id = newId, deckId = deckId, term = "", definition = "")
                        cards.add(card)

                        val manager = supportFragmentManager
                        val myDialogFragment = EditCardDialog(card)
                        myDialogFragment.show(manager, "editCardDialog")
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun createCardViews(cards: List<Card>) {
        cardsContainer.removeAllViews()
        cards.forEach { card ->
            createCardView(card)
        }
    }

    private fun createCardView(card: Card) {
        val itemView = LayoutInflater.from(this)
            .inflate(R.layout.card_selection_view, cardsContainer, false)

        val tvTerm = itemView.findViewById<TextView>(R.id.tvTerm)
        val tvDefinition = itemView.findViewById<TextView>(R.id.tvDefinition)

        tvTerm.text = card.term
        tvDefinition.text = card.definition

        val editButton = itemView.findViewById<ImageButton>(R.id.btnPencil)
        editButton.setOnClickListener {
            editCard(card)
        }

        cardsContainer.addView(itemView)
    }

    private fun editCard(card: Card) {
        val manager = supportFragmentManager
        val myDialogFragment = EditCardDialog(card)
        myDialogFragment.show(manager, "editCardDialog")
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onReturnValue(card: Card) {
        if (card.term.isEmpty() and card.definition.isEmpty()) {
            onDelete(card)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newDeckName = edtDeckName.text.toString()
                if (!newDeckName.isEmpty()) deckName = newDeckName

                val response = RetrofitClient.apiService.updateCard(card.id, card)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val idx = getCardIndexById(card.id)
                        if (idx >= 0) {
                            cards[idx] = card
                            createCardViews(cards)
                        }

                        scrollView.post {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    override fun onDelete(card: Card) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.deleteCard(card.id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        cards.removeIf { c -> c.id == card.id }
                        createCardViews(cards)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun getCardIndexById(id: Int): Int {
        for (i in 0..cards.count())
        {
            if (cards[i].id == id)
            {
                return i
            }
        }
        return -1
    }
}
