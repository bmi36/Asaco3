package com.example.asaco2

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.asaco2.ui.gallery.GalleryFragment
import com.example.asaco2.ui.home.Calendar
import com.example.asaco2.ui.tools.ToolsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST_CODE = 1
        const val CAMERA_PERMISSION_REQUEST_CODE = 2
        const val FILE_PERMISSION_REQUEST_CODE = 3
        const val HUNTER = "HUNTER"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    //    private val fileFolder by lazy {
//        getExternalFilesDir(
//            Environment.DIRECTORY_DCIM
//        )
//    }
    private val prefs by lazy {
        getSharedPreferences("User", Context.MODE_PRIVATE)
    }
    private lateinit var file: File
    private lateinit var uri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            //カメラボタンが押されたときになんかするやつ
//            openIntent()
            checkPermission()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val hostFragment = Calendar()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.nav_host_fragment, hostFragment)
        transaction.commit()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_calendar, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_tools
            ), drawerLayout
        )

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
        prefs
        setHeader(navView)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun takePicture() {
        val folder = getExternalFilesDir(Environment.DIRECTORY_DCIM)

        val fileName =
            SimpleDateFormat("YYMMddhhmmss", Locale.US).format(Date()).let {
                String.format("ComeraIntent_%s.jpg", it)
            }


        file = File(folder, fileName)

        uri = FileProvider.getUriForFile(
            this, applicationContext.packageName + ".fileprovider",
            file
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            addCategory(Intent.CATEGORY_DEFAULT)
        }

        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    //右上の…を設定するやつ
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.activity_main_drawer, menu)
//        return true
//    }

    private fun checkCameraPermission() =
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        )

    private fun grantCameraPermission() =
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
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
            //            val stream = FileInputStream(file)
            registerDatabase(file)
            val option = BitmapFactory.Options()
            option.inSampleSize = 10
            data?.extras?.get("data").let {
                //                val bitmap = BitmapFactory.decodeStream(
//                    stream, null, option
//                )
                val bmp = it as Bitmap
                val intent = Intent(this, CameraActivity::class.java)
                intent.putExtra("data", bmp)
                startActivity(intent)
            }
        } else {
            Log.d("debug", "うんこ💩💩💩")
        }
    }


    private fun openIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager)?.let {
            if (checkCameraPermission()) {
                takePicture()
            } else {
                grantCameraPermission()
            }
        } ?: Toast.makeText(this, "カメラを扱うアプリがありません", Toast.LENGTH_LONG).show()

    }

    private fun action(fragment: Fragment?): Boolean {
        if (fragment != null) {
            val fragmentTransition = supportFragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.nav_host_fragment, fragment)
            fragmentTransition.commit()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setHeader(navView: NavigationView) {
        val data = getSharedPreferences("User", Context.MODE_PRIVATE)
        val view = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navView, false)
        navView.addHeaderView(view)
        view.UserName.text = data.getString("name", HUNTER)
        view.Cal.text =
            "摂取⇒${data?.getInt("calory", 0)}" +
                    "\n燃焼⇒${data?.getInt("barn", 0)}"

        view.bmiText.text = "   BMI:${data?.getInt("bmi", 36)}"

    }


    private fun registerDatabase(file: File) {
        val contentValues: ContentValues = ContentValues().also {
            it.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            it.put("data", file.absolutePath)
        }
        this.contentResolver.also {
            it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
    }

    private fun requestFilePermission() {
        val compat = {
            val strarry: Array<String> =
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, strarry, FILE_PERMISSION_REQUEST_CODE)
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) compat else {
            Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT)
                .show()
            compat
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        )
            openIntent() else requestFilePermission()
    }


}