package com.example.mobile_finalproject

import RetrofitClient
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deck_activity)

        val deckId = intent.getIntExtra("DECK_ID", -1)
        val deckName = intent.getStringExtra("DECK_NAME")

        cards = arrayListOf()
        cardsContainer = findViewById(R.id.cardsContainer)
        val edtDeckName = findViewById<EditText>(R.id.edtDeckName)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnAddCard = findViewById<Button>(R.id.btnAddCard)

        edtDeckName.setText(deckName)

        btnSave.setOnClickListener {
            save()
        }

        btnAddCard.setOnClickListener {
            createEmptyCard()
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

    }

    private fun createEmptyCard() {

    }

    private fun createCard(card: Card) {

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

    override fun onReturnValue(card: Card) {
        for (i in 0..cards.count())
        {
            if (cards[i].id == card.id)
            {
                cards[i] = card;
                break
            }
        }

        createCardViews(cards)
    }

    override fun onDelete(card: Card) {
        cards.removeIf { c -> c.id == card.id }
        createCardViews(cards)
    }

    /////////////////////////////////////////////////////////////////////////////

    private fun createFakeCards() {
        for (i in 1..30) {
            cards.add(Card(i, 1, "fake $i", "fake $i"))
        }
        createCardViews(cards)
    }
}
