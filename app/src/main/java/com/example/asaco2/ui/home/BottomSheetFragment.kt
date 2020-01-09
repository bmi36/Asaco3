package com.example.asaco2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import kotlinx.android.synthetic.main.fragment_included.*


class BottomSheetFragment(
    private val element: List<CalendarEntity>,
    private val dayString: String
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_included,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nowText.text = dayString
    }
}