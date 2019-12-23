package com.example.asaco2.ui.camera

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.asaco2.MainActivity
import com.example.asaco2.R
import com.example.asaco2.ui.home.CalendarEntity
import com.example.asaco2.ui.home.CalendarViewModel
import kotlinx.android.synthetic.main.image_success_fragment.*
import java.io.DataInputStream
import java.lang.String.format
import java.util.*

class ImageSuccessFragment(
    private val uri: Uri,
    private val cook: Cook
//    private val viewModel: CalendarViewModel
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.image_success_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraImage.run {
            setImageURI(uri)
            adjustViewBounds = true
        }

        cooknametext.setText(cook.foodname, TextView.BufferType.EDITABLE)
        caltext.setText(cook.calorie.toString(), TextView.BufferType.EDITABLE)
        okbutton.setOnClickListener {
            CalendarEntity(
                timeStamp, cook.foodname, 0, cook.calorie, 0
            ).let {
                MainActivity().insert(it)
            }
            Toast.makeText(activity, "完了しました", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }

    private val timeStamp =
        SimpleDateFormat("yyyyMMddhhmmss").let {
            Date(System.currentTimeMillis()).let { data ->
                it.format(data)
            }
        }.toLong()
}