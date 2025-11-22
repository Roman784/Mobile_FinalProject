package com.example.mobile_finalproject

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var headerView: View
    protected lateinit var btnHome: ImageButton
    protected lateinit var btnPlus: ImageButton

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupHeader()
    }

    private fun setupHeader() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_view, null)

        btnHome = headerView.findViewById(R.id.btnHome)
        btnPlus = headerView.findViewById(R.id.btnPlus)

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.header_height)
        )
        params.gravity = Gravity.TOP

        val rootView = findViewById<ViewGroup>(android.R.id.content)
        val contentView = rootView.getChildAt(0) as? ViewGroup

        contentView?.addView(headerView, 0, params)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            headerView.elevation = 8f
        }

        setupHeaderFunctionality()
    }

    protected fun setupHeaderFunctionality() {

        btnHome.setOnClickListener {
            openHomeActivity()
        }

        btnPlus.setOnClickListener {
            addNewDeck()
        }
    }

    private fun openHomeActivity() {
        if (this::class.java != MainActivity::class.java) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addNewDeck() {
        if (this::class.java != EditDeckActivity::class.java) {
            val intent = Intent(this, EditDeckActivity::class.java)
            intent.putExtra("DECK_ID", -1)
            startActivity(intent)
        }
    }
}