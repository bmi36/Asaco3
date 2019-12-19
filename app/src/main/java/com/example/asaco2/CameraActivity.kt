package com.example.asaco2

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera_activty.*

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_activty)

        val bmp = intent?.extras?.get("data") as Bitmap
        cameraImag.setImageBitmap(bmp)

    }
}
