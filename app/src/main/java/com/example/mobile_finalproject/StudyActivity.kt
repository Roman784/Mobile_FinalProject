package com.example.mobile_finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class StudyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_activity)

        val deckId = intent.getIntExtra("DECK_ID", -1)


    }
}