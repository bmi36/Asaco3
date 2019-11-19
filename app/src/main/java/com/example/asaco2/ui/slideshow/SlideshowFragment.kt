package com.example.asaco2.ui.slideshow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asaco2.MainActivity

open class SlideshowFragment : Fragment() {
    interface onCameraClicked {
        fun onClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (context as onCameraClicked).onClick()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            activity?.setResult(resultCode)
        }
    }
}
