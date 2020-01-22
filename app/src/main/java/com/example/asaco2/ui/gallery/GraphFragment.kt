package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.ui.home.Step
import com.example.asaco2.ui.home.StepViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.graph_fragment.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GraphFragment(private val date: Long, private val type: Int) : Fragment(), CoroutineScope {

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

        launch {

            list = viewModel.getstep(date)

            var barChar: ArrayList<BarEntry>? = null

            var listname: ArrayList<String>? = null

            for (element in list) {
                barChar = arrayListOf(BarEntry(element.step.toFloat(), element.id.toInt()))
                listname = arrayListOf(element.id.toString())
            }

            if (list.size < type){
                val differnce = type - list.size
                for (index in 0..differnce){
                    barChar = arrayListOf(BarEntry(0f,12))
                    listname = arrayListOf("次のやつ")
                }
            }

            if (barChar != null) {
                val barDataSet = BarDataSet(barChar, "Cels")

                val barData = BarData(listname, barDataSet)

                chart.run {
                    data = barData
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                }
            }
        }
        chart.animateY(2500)
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}