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


class Calendar(private val viewModel: CalendarViewModel) : Fragment() {


    private lateinit var list: Array<CalendarEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            var intent = Intent(activity, MemoNullActivity::class.java)
            val id = year.toLong() + month.toLong() + dayOfMonth.toLong()
            for (element in list) {
                if (element.id == id) {
                    intent = Intent(activity, Memo::class.java).apply {
                        this.putExtra("calendar", element)
                        this.putExtra("year", year)
                        this.putExtra("month", month)
                        this.putExtra("day", dayOfMonth)
                    }
                    break
                }
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.allCalendar.observe(this, Observer {
            if (it != null) list = it
        })
    }
}

