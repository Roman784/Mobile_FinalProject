package com.example.mobile_finalproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mobile_finalproject.models.Deck
import com.example.mobile_finalproject.services.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var headerView: View
    protected lateinit var btnHome: ImageButton
    protected lateinit var btnPlus: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkNotificationPermission()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupHeader()
    }

    override fun onDestroy() {
        super.onDestroy()

        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)
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

        headerView.elevation = 8f

        setupHeaderFunctionality()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    protected fun setupHeaderFunctionality() {

        btnHome.setOnClickListener {
            if (this::class.java != MainActivity::class.java) {
                openHomeActivity()
            }
        }

        btnPlus.setOnClickListener {
            if (this::class.java != EditDeckActivity::class.java) {
                createNewDeck()
            }
        }
    }

    protected fun openEditDeckActivity(deckId: Int) {
        val intent = Intent(this, EditDeckActivity::class.java)
        intent.putExtra("DECK_ID", deckId)
        startActivity(intent)
    }

    protected fun openStudyActivity(deckId: Int) {
        val intent = Intent(this, StudyActivity::class.java)
        intent.putExtra("DECK_ID", deckId)
        startActivity(intent)
    }

    protected fun openHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun createNewDeck() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.createDeck(Deck(-1, "New deck"))
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        openEditDeckActivity(response.body()?.id ?: -1)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
}