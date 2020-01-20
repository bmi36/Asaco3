package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.graph_fragment.*

class GraphFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.graph_fragment,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChar: BarDataSet = arrayListOf(
            BarEntry(8f,0),
            BarEntry(2f,1),
            BarEntry(5f,2),
            BarEntry(20f,3),
            BarEntry(15f,4),
            BarEntry(19f,5)
        ).let { BarDataSet(it,"Cels") }

        val barData = BarData(arrayListOf("2016","2015","2014","2013","2012","2011"),barChar)

        chart.run {
            data = barData
            barChar.setColors(ColorTemplate.COLORFUL_COLORS)
            animateY(2500)
        }
    }
}