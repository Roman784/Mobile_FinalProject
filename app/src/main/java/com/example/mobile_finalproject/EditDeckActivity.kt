package com.example.mobile_finalproject

import RetrofitClient
import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deck_activity)

        val deckId = intent.getIntExtra("DECK_ID", -1)
        val deckName = intent.getStringExtra("DECK_NAME")

        cards = arrayListOf()
        cardsContainer = findViewById(R.id.cardsContainer)
        scrollView = findViewById(R.id.scrollView)

        val edtDeckName = findViewById<EditText>(R.id.edtDeckName)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnAddCard = findViewById<Button>(R.id.btnAddCard)

        edtDeckName.setText(deckName)

        btnSave.setOnClickListener {
            save()
        }

        btnAddCard.setOnClickListener {
            createNewCard(deckId)
        }

        //loadCards(deckId)
        createFakeCards()
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

    private fun save() {
        // TODO: Save on the server.
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun createNewCard(deckId: Int) {
        val newId = cards.count() + 1 // TODO: From server.
        val card = Card(id = newId, deckId = deckId, term = "", definition = "")
        cards.add(card)

        val manager = supportFragmentManager
        val myDialogFragment = EditCardDialog(card)
        myDialogFragment.show(manager, "editCardDialog")
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

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun editCard(card: Card) {
        val manager = supportFragmentManager
        val myDialogFragment = EditCardDialog(card)
        myDialogFragment.show(manager, "editCardDialog")
    }

    override fun onReturnValue(card: Card) {
        if (card.term.isEmpty() and card.definition.isEmpty()) {
            onDelete(card)
            return
        }

        val idx = getCardIndexById(card.id)
        if (idx >= 0) {
            cards[idx] = card
            createCardViews(cards)
        }
    }

    override fun onDelete(card: Card) {
        // TODO: Delete from server.
        cards.removeIf { c -> c.id == card.id }
        createCardViews(cards)
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

    /////////////////////////////////////////////////////////////////////////////

    private fun createFakeCards() {
        for (i in 1..30) {
            cards.add(Card(i, 1, "fake $i", "fake $i"))
        }
        createCardViews(cards)
    }
}
