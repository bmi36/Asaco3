package com.example.asaco2.ui.camera

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.ui.home.CalendarViewModel
import kotlinx.android.synthetic.main.image_success_fragment.*

class ImageSuccessFragment(
    private val uri: Uri,
    private val cook: Cook
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

        cooknametext.setText(cook.foodname,TextView.BufferType.EDITABLE)
        caltext.setText(cook.calorie.toString(),TextView.BufferType.EDITABLE)
        okbutton.setOnClickListener {
            Toast.makeText(activity, "完了しました", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }
}