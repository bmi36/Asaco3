package com.example.asaco2

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.asaco2.ui.gallery.GalleryFragment
import com.example.asaco2.ui.home.Calendar
import com.example.asaco2.ui.tools.ToolsFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


const val CAMERA_REQUEST_CODE = 1
const val CAMERA_PERMISSION_REQUEST_CODE = 2
const val FILE_PERMISSION_REQUEST_CODE = 3
const val HUNTER = "HUNTER"

class MainActivity : AppCompatActivity() {

    private lateinit var setFragment: Fragment

    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_calendar, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_tools
            ), drawer_layout
        )
    }

    //    private val fileFolder by lazy {
//        getExternalFilesDir(
//            Environment.DIRECTORY_DCIM
//        )
//    }
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("User", Context.MODE_PRIVATE)
    }

    private val file: File by lazy {
        SimpleDateFormat("YYMMddhhmmss", Locale.US).format(Date()).run {
            String.format("CameraIntent_%s.jpg", this)
            File(getExternalFilesDir(Environment.DIRECTORY_DCIM), this)
        }
    }

//    private val uri: Uri by lazy {
//        FileProvider.getUriForFile(
//            this, applicationContext.packageName + ".fileprovider",
//            file
//        )
//    }

    private val navView: NavigationView by lazy {
        nav_view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            //カメラボタンが押されたときになんかするやつ
//            openIntent()
            checkPermission()
        }

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

        supportFragmentManager.beginTransaction().let {
            it.add(R.id.nav_host_fragment, Calendar())
            it.commit()
        }

        //スライドメニューの各項目を押したとき
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_slideshow -> {
                    checkPermission()
//                    openIntent()
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
        setFragment = Calendar()
        setHeader(navView)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()


    private fun takePicture() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            //            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            addCategory(Intent.CATEGORY_DEFAULT)
        }.run {
            startActivityForResult(this, CAMERA_REQUEST_CODE)
        }
    }

    //右上の…を設定するやつ
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.activity_main_drawer, menu)
//        return true
//    }

    private fun checkCameraPermission() =
        PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.CAMERA
                )

    private fun grantCameraPermission() =
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        } else {
            Toast.makeText(this, "うんこ💩💩💩", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {

            registerDatabase(file)
            BitmapFactory.Options().inSampleSize = 10

            data?.extras?.get("data").let {
                Intent(this, CameraActivity::class.java).apply {
                    putExtra("data", it as Bitmap)
                    startActivity(this)
                }
            }
        }
    }


    private fun openIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager)?.let {

            if (checkCameraPermission()) takePicture() else grantCameraPermission()

        } ?: Toast.makeText(this, "カメラを扱うアプリがありません", Toast.LENGTH_LONG).show()

    }

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
        ContentValues().let {
            it.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            it.put("data", file.absolutePath)
            this.contentResolver.run {
                this.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, it)
            }
        }
    }

    private fun requestFilePermission() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) Toast.makeText(
            this, "許可されないとアプリが実行できません",
            Toast.LENGTH_SHORT
        ).show()

        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).let {
            ActivityCompat.requestPermissions(this, it, FILE_PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        )
            openIntent() else requestFilePermission()
    }


    override fun onBackPressed() {
        when (setFragment) {
            is Calendar -> super.onBackPressed()

            else -> {
                action(Calendar())

            }
        }
    }
}