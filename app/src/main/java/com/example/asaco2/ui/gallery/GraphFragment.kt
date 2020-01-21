package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.ui.home.Step
import com.example.asaco2.ui.home.StepViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.graph_fragment.*

class GraphFragment(private val date: Long, step: Int) : Fragment() {

    private lateinit var viewModel: StepViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.graph_fragment, container, false)

    private lateinit var list: Array<Step>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[StepViewModel::class.java]
        list = viewModel.getstep(date)

        var barChar: ArrayList<BarEntry>? = null
        var listname: ArrayList<String>? = null
        for (element in list) {
            barChar = arrayListOf(BarEntry(element.step.toFloat(), element.id.toInt()))
            listname = arrayListOf(element.id.toString())
        }
        val barDataSet = BarDataSet(barChar, "Cels")

//         = arrayListOf(
//            BarEntry(8f,0),
//            BarEntry(2f,1),
//            BarEntry(5f,2),
//            BarEntry(20f,3),
//            BarEntry(15f,4),
//            BarEntry(19f,5)
//        ).let { BarDataSet(it,"Cels") }

        val barData = BarData(listname,barDataSet)
//            BarData(arrayListOf("2016", "2015", "2014", "2013", "2012", "2011"), barDataSet)

        chart.run {
            data = barData
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
            animateY(2500)
        }
    }
}