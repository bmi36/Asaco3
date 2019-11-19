package com.example.asaco2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import kotlinx.android.synthetic.main.fragment_home.*


class Calendar : Fragment() {

    private lateinit var viewModel: CalendarViewModel
    private lateinit var list: Array<CalendarEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allCalendar.observe(this, Observer {
            if (it != null) list = it
        })

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(activity, Memo::class.java)
            val id = year.toLong() + month.toLong() + dayOfMonth.toLong()
            for (element in list) {
                if (element.id == id) {
                    intent.putExtra("calendar", element)
                    intent.putExtra("year", year)
                    intent.putExtra("month", month)
                    intent.putExtra("day", dayOfMonth)
                    startActivity(intent)
                    return@setOnDateChangeListener
                }
            }

            val nullIntent = Intent(activity, MemoNullActivity::class.java)
            startActivity(nullIntent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CalendarViewModel::class.java)
    }
}