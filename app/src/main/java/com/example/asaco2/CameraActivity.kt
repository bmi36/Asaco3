package com.example.asaco2

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera_activty.*
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_activty)

        try {
            val bmp = intent?.extras?.get("data") as Bitmap
            cameraImag.setImageBitmap(bmp)
            PostBmpAsyncHttpRequest(this).execute(Param("なんかのURL", bmp))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
