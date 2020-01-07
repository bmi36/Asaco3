package com.example.asaco2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import kotlinx.android.synthetic.main.fragment_home.*


class Calendar : Fragment() {

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val id = "$year$month$dayOfMonth".toLong()

            viewModel = activity?.run {
                ViewModelProviders.of(this)[CalendarViewModel::class.java]
            } ?: throw Exception("Invalid Activity")

            Thread {
                var intent = Intent(activity, MemoNullActivity::class.java)
                val element = viewModel.getCalendar(id)
                if (element != null) {
                    if (element.isEmpty())
                        intent = Intent(activity,Memo::class.java)
                        intent.putExtra("list", element.toTypedArray())
                }
                startActivity(intent)
            }
        }
    }
}

