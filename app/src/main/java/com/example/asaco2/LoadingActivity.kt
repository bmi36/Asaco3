package com.example.asaco2

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_loading.*

class LoadingActivity : AppCompatActivity() {

    private fun setupProgressBar() {
        progressBar.visibility = ProgressBar.VISIBLE
        progressBar.max = 200
        progressBar.progressTintList = ColorStateList.valueOf(Color.CYAN)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

    }
}