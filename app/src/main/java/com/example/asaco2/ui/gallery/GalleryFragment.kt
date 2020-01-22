package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import com.example.asaco2.today
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.walk_statu_layout.*
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment(private val step: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayText.text = today

        dayBtn.setOnClickListener { childFragmentManager.beginTransaction().replace(frame.id, GraphFragment(
            day.toLong(),daylast
        )).commit() }
        monthBtn.setOnClickListener { childFragmentManager.beginTransaction().replace(frame.id, GraphFragment(
            month.toLong(),12
        )).commit() }
        yearBtn.setOnClickListener { childFragmentManager.beginTransaction().replace(frame.id, GraphFragment(
            year.toLong(),0
        )).commit() }
    }

    val day: String = SimpleDateFormat("yyyyMMdd").let { it.format(Date(System.currentTimeMillis())) }
    val month: String = SimpleDateFormat("yyyyMM").run { format(Date(System.currentTimeMillis())) }
    val year: String = SimpleDateFormat("yyyy").run { format(Date(System.currentTimeMillis())) }
    val daylast = Calendar.getInstance().let {
        it.set(Calendar.MONTH,1)
        it.getActualMaximum(Calendar.DATE)
    }
}