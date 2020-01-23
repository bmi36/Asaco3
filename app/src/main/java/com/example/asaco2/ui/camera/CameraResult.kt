package com.example.asaco2.ui.camera

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.example.asaco2.MainActivity
import com.example.asaco2.R
import com.example.asaco2.ui.home.CalendarViewModel
import kotlinx.android.synthetic.main.activity_camera_result.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.CoroutineContext

class CameraResult : AppCompatActivity(), CoroutineScope {

    //    private lateinit var viewModel: CalendarViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_camera_result)

        val fileDir = intent?.extras?.get("file") as Uri
        val file: File = fileDir.toFile()

        val uri = intent?.extras?.get("uri") as Uri
        frame.visibility = FrameLayout.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(frame.id, LoaderFragment())
            .commitNow()
        job(uri, file).start()
    }

    private fun job(uri: Uri, file: File) = launch(Dispatchers.Main) {
        val baseImage: String = toBase(BitmapFactory.decodeFile(file.absolutePath))

        withContext(Dispatchers.Default) {
            retrofitBuild().create(RetrofitInterface::class.java)
                .sendImage(baseImage).enqueue(object : Callback<Cook> {

                    override fun onFailure(call: Call<Cook>, t: Throwable) {

                        Log.d("test", t.message)
                        supportFragmentManager.beginTransaction()
                            .replace(frame.id, ImageFileFragment())
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
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}
