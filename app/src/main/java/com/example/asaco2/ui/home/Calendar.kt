package com.example.asaco2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class Calendar : Fragment(), CoroutineScope {

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var bottomsheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        bottom_sheet_layout.layoutParams.height = 900
        bottomsheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val strMonth = if (month > 9) "${month + 1}" else "0${month + 1}"
            val strDay = if (dayOfMonth > 9) "$dayOfMonth" else "0${dayOfMonth}"
            val dayString = "${year}年${strMonth}月${strDay}日"

            val id =
                "$year$strMonth$strDay".toLong()

            viewModel = activity?.run {
                ViewModelProviders.of(this)[CalendarViewModel::class.java]
            } ?: throw Exception("Invalid Activity")

            launch {
                val element = viewModel.getCalendar(id)
                if (element != null) {
                    if (element.isNotEmpty()) activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(include_frame.id, BottomSheetFragment(element,dayString))?.commit()

                }
                bottomsheetBehavior.state =
                    BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}