package com.example.mobile_finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.mobile_finalproject.models.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyActivity : BaseActivity() {
    private  lateinit var cards: ArrayList<Card>
    private var currentCardIdx: Int = 0

    private lateinit var tvTerm: TextView
    private lateinit var tvDefinition: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_activity)

        val deckId = intent.getIntExtra("DECK_ID", -1)

        cards = arrayListOf()
        tvTerm = findViewById(R.id.tvTerm)
        tvDefinition = findViewById(R.id.tvDefinition)

        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnShowDefinition = findViewById<View>(R.id.btnShowDefinition)
        val btnPencil = findViewById<ImageButton>(R.id.btnPencil)

        btnNext.setOnClickListener {
            switchCard()
        }

        btnShowDefinition.setOnClickListener {
            setActiveDefinitionView()
        }

        btnPencil.setOnClickListener {
            openEditDeckActivity(deckId)
        }

        loadCards(deckId)
    }

    private fun loadCards(deckId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getCards(deckId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        cards.addAll(response.body() ?: emptyList())
                        cards.shuffle()
                        showFirstCard()
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun switchCard() {
        disableDefinitionView()

        if (cards.count() == 0) return
        currentCardIdx = ++currentCardIdx % cards.count()

        showCard(cards[currentCardIdx])
    }

    private fun setActiveDefinitionView() {
        if (tvDefinition.alpha > 0f)
            tvDefinition.alpha = 0f
        else
            tvDefinition.alpha = 1f
    }

    private fun disableDefinitionView() {
        tvDefinition.alpha = 0f
    }

    private fun showFirstCard() {
        disableDefinitionView()

        if (cards.count() == 0) {
            tvTerm.text = R.string.cards_list_empty.toString()
            return
        }

        showCard(cards[0])
    }

    private fun showCard(card: Card) {
        tvTerm.text = card.term
        tvDefinition.text = card.definition
    }

    private fun openEditDeckActivity(deckId: Int) {
        val intent = Intent(this, EditDeckActivity::class.java)
        intent.putExtra("DECK_ID", deckId)
        startActivity(intent)
    }

    /////////////////////////////////////////////////////////////////////////////

    private fun createFakeCards() {
        for (i in 1..30) {
            cards.add(Card(i, 1, "fake $i", "fake $i"))
        }
        cards.shuffle()
        showFirstCard()
    }
}