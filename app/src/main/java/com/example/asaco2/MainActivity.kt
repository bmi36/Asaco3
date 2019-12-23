package com.example.asaco2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.asaco2.ui.camera.CameraResult
import com.example.asaco2.ui.gallery.GalleryFragment
import com.example.asaco2.ui.home.Calendar
import com.example.asaco2.ui.home.CalendarEntity
import com.example.asaco2.ui.home.CalendarViewModel
import com.example.asaco2.ui.tools.ToolsFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


const val CAMERA_REQUEST_CODE = 1
const val CAMERA_PERMISSION_REQUEST_CODE = 2
const val FILE_PERMISSION_REQUEST_CODE = 3
const val HUNTER = "HUNTER"

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var setFragment: Fragment

//    private val viewModel: CalendarViewModel by lazy {
//        ViewModelProviders.of(this).get(CalendarViewModel::class.java)
//    }

    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_calendar, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_tools
            ), drawer_layout
        )
    }

    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("User", Context.MODE_PRIVATE)
    }

    private val navView: NavigationView by lazy {
        nav_view.apply {
            this.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_slideshow -> {
                        action(null)
                        checkPermission()

                        true
                    }

                    R.id.nav_calendar -> {
                        toolbar.title = "カレンダー画面"
                        action(Calendar())
                    }

                    R.id.nav_gallery -> {
                        toolbar.title = "徒歩"
                        action(GalleryFragment())
                    }

                    R.id.nav_tools -> {
                        toolbar.title = "設定"
                        action(ToolsFragment(navView, prefs))
                    }
                    else -> action(null)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "カレンダー画面"

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            //カメラボタンが押されたときになんかするやつ
            drawer_layout.closeDrawer(GravityCompat.START)
            checkPermission()
        }

        //どろわーの設定
        ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ).also {
            drawer_layout.addDrawerListener(it)
            it.syncState()
        }

        setFragment = Calendar()
        setHeader(navView)
        navView.setCheckedItem(R.id.nav_calendar)
        action(Calendar())
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    private lateinit var file: File

    private lateinit var uri: Uri

    //写真を取るときのやつ
    private fun takePicture() {
        val folder = getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val name = SimpleDateFormat("ddHHmmss", Locale.US).format(Date()).let {
            String.format("CameraIntent_%s.jpg", it)
        }
        file = File(folder, name)
        uri = FileProvider.getUriForFile(
            applicationContext, "$packageName.fileprovider",
            file
        )

        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, uri).run {
                startActivityForResult(this, CAMERA_REQUEST_CODE)
            }
    }

    //ぱーにっしょんをリクエストするやつ
    private fun requestPermission() {
        val str: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(this, str, CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    //ぱーにっしょん確認するやつ
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) takePicture() else requestPermission()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        }
    }

    //写真を撮った後のやつ
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                registerDatabase(file)
                val intent = Intent(this, CameraResult::class.java)
                    .putExtra("file", file.toUri())
                    .putExtra("uri", uri)
                startActivity(intent)
            }
        }
    }


    //フラグメントの切り替えのやつ
    private fun action(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().also {
                setFragment = fragment
                it.replace(R.id.nav_host_fragment, fragment).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //スライドメニューのへっだーのやつ
    @SuppressLint("SetTextI18n")
    private fun setHeader(navView: NavigationView) {
        getSharedPreferences("User", Context.MODE_PRIVATE).let { data ->

            LayoutInflater.from(this).inflate(R.layout.nav_header_main, navView, false).run {

                this.UserName.text = data.getString("name", HUNTER)

                this.Cal.text =
                    "摂取⇒${data?.getInt("calory", 0)}" +
                            "\n燃焼⇒${data?.getInt("barn", 0)}"

                this.bmiText.text = "   BMI:${data?.getInt("bmi", 36)}"

                navView.addHeaderView(this)
            }
        }
    }



    private fun registerDatabase(file: File) {
        val contentValues = ContentValues().also {
            it.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            it.put("_data", file.absolutePath)
        }
        this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }


    override fun onBackPressed() {
        when (navView.checkedItem?.itemId) {
            R.id.nav_calendar -> super.onBackPressed()
            else -> {
                title = "カレンダー画面"
                navView.setCheckedItem(R.id.nav_calendar)
                action(Calendar())
            }
        }
    }

//    fun insert(enttity: CalendarEntity){
//        viewModel.insert(enttity)
//    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}

//fun setTime() =
//    SimpleDateFormat("YYMMddhhmmss", Locale.US).format(Date()).run {
//        String.format("CameraIntent_%s.jpg", this)
//    }