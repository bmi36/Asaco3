package com.example.asaco2.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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
}
