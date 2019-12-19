package com.example.asaco2.ui.camera

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.example.asaco2.R
import kotlinx.android.synthetic.main.activity_camera_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.CoroutineContext

const val IMAGE_REQUEST_CODE = 3

class CameraResult : AppCompatActivity(), CoroutineScope {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_result)

        val fileDir = intent?.extras?.get("file") as Uri?
        val file: File? = fileDir?.toFile()


        val uri = intent?.extras?.get("uri") as Uri?
        if (uri != null && file != null) {
            frame.visibility = FrameLayout.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(frame.id, LoaderFragment(job(uri, file)))
                .commitNow()
        }
    }

    private fun job(uri: Uri, file: File) {
        val baseImage: String = toBase(BitmapFactory.decodeFile(file.absolutePath))

        retrofitBuild().create(RetrofitInterface::class.java)
            .sendImage(baseImage).enqueue(object : Callback<Cook> {
                override fun onFailure(call: Call<Cook>, t: Throwable) {
                    supportFragmentManager.beginTransaction().replace(frame.id, ImageFileFragment())
                        .commit()
                }

                override fun onResponse(call: Call<Cook>, response: Response<Cook>) {

                    if (response.body() != null) {
                        val cock = response.body() as Cook
                        supportFragmentManager.beginTransaction()
                            .replace(frame.id, ImageSuccessFragment(uri, cock)).commit()
                    }
                }

            })
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}
