package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import com.example.asaco2.ui.home.Step
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.graph_fragment.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GraphFragment(private val list: Array<Int>?) : Fragment(), CoroutineScope {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.graph_fragment, container, false)

    var barChar: ArrayList<BarEntry>? = null

    var listname: ArrayList<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch { action() }

        if (barChar != null) {

            val barDataSet = BarDataSet(barChar, "Cels")

            val barData = BarData(listname, barDataSet)

            chart.run {
                data = barData
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                animateY(1500)
            }
        }
    }

    private suspend fun action() = withContext(Dispatchers.Default) {
        list?.let {
            for (i in list.indices) {
                barChar = arrayListOf(BarEntry(list[i].toFloat(), i))
                listname = arrayListOf(i.toString())
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}
