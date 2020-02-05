package com.example.asaco2

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.asaco2.ui.camera.CameraResult
import com.example.asaco2.ui.gallery.GalleryFragment
import com.example.asaco2.ui.home.Calendar
import com.example.asaco2.ui.tools.ToolsFragment
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


const val CAMERA_REQUEST_CODE = 1
const val HUNTER = "梅田ひろし"


class MainActivity : AppCompatActivity(), CoroutineScope, ToolsFragment.FinishBtn,
    SensorEventListener {

    companion object {
        private const val REQUEST_CODE = 1000
    }

    private lateinit var setFragment: Fragment
    private var mSensorManager: SensorManager? = null
    private var mStepCounterSensor: Sensor? = null
    private lateinit var prefs: SharedPreferences
    private var stepcount = -1
    private lateinit var permissions: Array<String>
    private var sensorcount: Int = -1
    private lateinit var viewModel: StepViewModel
    private var flg = false
    private var hohaba: Double = 0.0
    private var weight = 0.0
    private val day: Long by lazy { prefs.getLong("calendar", time) }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_calendar,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_tools
            ), drawer_layout
        )
    }

    //    ナビゲーションの初期化
    private val navView: NavigationView by lazy {
        nav_view.apply {
            this.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_slideshow -> {
                        if (flg) takePicture()
                        action(null)
                        true
                    }
                    R.id.nav_calendar -> {
                        toolbar.title = getString(R.string.calendar)
                        action(Calendar())
                    }
                    R.id.nav_gallery -> {
                        toolbar.title = getString(R.string.hosuu)
                        action(
                            GalleryFragment(
                                stepcount,
                                (calgary()).toString(),
                                (hohaba * stepcount / 100000)
                            )
                        ).also { viewModel.update(StepEntity(day, stepcount)) }
                    }
                    R.id.nav_tools -> {
                        toolbar.title = getString(R.string.setting)
                        action(ToolsFragment(this@MainActivity, navView))
                    }
                    else -> action(null)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_main)

        permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        checkPermission(permissions, REQUEST_CODE)

        launch {
            mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mStepCounterSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        }
        title = getString(R.string.calendar)

        setSupportActionBar(toolbar)

        prefs = getSharedPreferences("Cock", Context.MODE_PRIVATE)

        fab.setOnClickListener {
            //カメラボタンが押されたときになんかするやつ
            if (flg) takePicture()
            drawer_layout.closeDrawer(GravityCompat.START)

        }
        viewModel = ViewModelProvider(this)[StepViewModel::class.java]

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
        sensorcount = prefs.getInt("sensor", 0)

        stepcount = prefs.getInt("walk", -1)
        getSharedPreferences("User", Context.MODE_PRIVATE).run {
            hohaba = (getString("height", "170")?.toDouble() ?: 0.0 * 0.45)
            weight = getString("weight", "60")?.toDouble() ?: 0.0
        }
        setFragment = Calendar()
        setHeader(navView)
        navView.setCheckedItem(R.id.nav_calendar)
        action(Calendar())
        prefs.run {

            if (day == time){
                viewModel.insert(StepEntity(time,stepcount))
            }
//            day = getLong("calendar", time(currentTimeMillis).toLong())
//                Date(
//                    getLong(
//                        "calendar", System.currentTimeMillis().also {
//                            viewModel.insert(StepEntity(it, 0))
//                        })
//                ).let { time(it).toInt() }
        }
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

    //ぱーにっしょん確認するやつ
    private fun checkPermission(permissions: Array<String>, request_code: Int) {
        ActivityCompat.requestPermissions(this, permissions, request_code)
    }

    //    パーミッションのリクエストとかのそうゆうやつ
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> for (index in permissions.indices) {
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) flg = true
                else {
                    flg = false
                    break
                }
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
    private fun setHeader(navView: NavigationView) {
        getSharedPreferences("User", Context.MODE_PRIVATE).let { data ->

            LayoutInflater.from(this).inflate(R.layout.nav_header_main, navView, false)
                .run {

                    this.UserName.text = data.getString("name", HUNTER)
                    navView.addHeaderView(this)
                }
        }
    }


    //画像をDBに保存するやつ
    private fun registerDatabase(file: File) {
        val contentValues = ContentValues().also {
            it.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            it.put("_data", file.absolutePath)
        }
        this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    //バックボタンを押したときのやつ
    override fun onBackPressed() {
        when (navView.checkedItem?.itemId) {
            R.id.nav_calendar -> super.onBackPressed()
            else -> {
                onClick()

            }
        }
    }

    private fun calgary() = (stepcount.let { 1.05 * (3 * hohaba * it) * weight } / 200000).toInt()

    override val coroutineContext: CoroutineContext
        get() = Job()

    override fun onClick() {
        toolbar.title = getString(R.string.calendar)
        navView.setCheckedItem(R.id.nav_calendar)
        action(Calendar())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onStart() {
        navView.getHeaderView(0).run {
            Cal.text = getString(R.string.calText, prefs.getInt("calory", 0).toString())
            barn.text = getString(R.string.barnText, calgary().toString())
        }
        super.onStart()
    }

    //    歩数をあれするやつ
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                stepcount++
                when (val gap = event.values[0].toInt() - sensorcount) {
                    0 -> sensorcount = event.values[0].toInt()
                    else -> stepcount + gap
                }
            }
        }
        navView.getHeaderView(0).barn.text = getString(R.string.barnText,calgary().toString())
    }

    override fun onResume() {
        super.onResume()
        mSensorManager
            ?.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    //    歩数の保存するやつ
    override fun onStop() {
        super.onStop()
        mSensorManager?.unregisterListener(this, mStepCounterSensor)
        viewModel.updateOrinsert(StepEntity(day.toLong(), stepcount))
        prefs.edit().run {

            if (day.toInt() == time.toInt()) putInt("walk", stepcount) else clear()

            putInt("sensor", sensorcount)
            putLong("calendar",day)
            apply()
        }
    }
}

//キーボードが消えるやつ
fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val manager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

val currentTimeMillis = Date(System.currentTimeMillis())
val time: Long =
    SimpleDateFormat("yyyyMMdd", Locale.US).run { format(currentTimeMillis) }.toLong()
fun today(date: Date): String =
    SimpleDateFormat("yyyy年MM月dd日", Locale.US).run { format(date) }

fun String.toDailyClass() = Gson().fromJson(this, DayilyEventController::class.java)
fun DayilyEventController.toJson() = Gson().toJson(this)