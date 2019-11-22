package com.example.asaco2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera_activty.*

const val HTTP = "http://192.168.3.7:8080/8180/python_server/.images/image"

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_activty)

        val bmp = intent?.extras?.get("data") as Bitmap
        cameraImag.setImageBitmap(bmp)

        PostBmpAsyncHttpRequest(setIntent(bmp)).execute(Param(HTTP, bmp))
    }

    private fun setIntent(bmp: Bitmap) {
        startActivity(
            Intent(this, CameraResultActivity::class.java).apply {
                intent.putExtra("data", bmp)
            })

    }
}
