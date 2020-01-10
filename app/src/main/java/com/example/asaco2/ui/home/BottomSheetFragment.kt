package com.example.asaco2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import kotlinx.android.synthetic.main.fragment_included.*
import kotlinx.android.synthetic.main.fragment_included.view.*


class BottomSheetFragment(
    private val list: List<CalendarEntity>,
    private val dayString: String
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_included, container, false).also {
        FoodAdapter(this, list).let { adapter -> it.recyclerView.adapter = adapter }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var total = 0

        for (element in list) {
            total += element.absorption as Int
        }
        nowText.text = dayString
        sumText.text = "合計：${total}cal"
    }
}