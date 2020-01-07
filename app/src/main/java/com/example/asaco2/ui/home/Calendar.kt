package com.example.asaco2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.ui.camera.toDataClass
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.coroutines.CoroutineContext


class Calendar : Fragment(), CoroutineScope {

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val strMonth = if (month > 9)"${month+1}" else "0${month+1}"
            val strDay = if(dayOfMonth > 9) "$dayOfMonth" else "0${dayOfMonth}"
            val id =
                "$year$strMonth$strDay".toLong()

            viewModel = activity?.run {
                ViewModelProviders.of(this)[CalendarViewModel::class.java]
            } ?: throw Exception("Invalid Activity")

            launch {
                var intent = Intent(activity, MemoNullActivity::class.java)
                val element = viewModel.getCalendar(id)
                if (element != null) {
                    if (element.isNotEmpty())
                        intent = Intent(activity, Memo::class.java)
                    intent.putExtra("list", element.toTypedArray())
                }
                startActivity(intent)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}