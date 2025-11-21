package com.example.mobile_finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditDeckActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deck_activity)

        val deck_id = intent.getIntExtra("DECK_ID", -1)

        findViewById<TextView>(R.id.deck_id).text = deck_id.toString()
    }
}