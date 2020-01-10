package com.example.asaco2.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
    ): View? {

        return inflater.inflate(R.layout.fragment_included, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var total = 0

        recyclerView.run {
            setHasFixedSize(true)
            adapter = activity?.applicationContext?.let { FoodAdapter(it, list.toTypedArray()) }
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        for (element in list) {
            total += element.absorption as Int
        }
        nowText.text = dayString
        sumText.text = "合計：${total}cal"
        val hosuu = 356 //代替　歩数

        val bmr = activity?.getSharedPreferences("User",Context.MODE_PRIVATE)?.getInt("bmr",0)
            ?: 0

        totalCalText.text = "カロリー差：${total - hosuu-bmr}cal"
    }
}