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
import kotlinx.coroutines.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class GraphFragment(private val list: Array<Float>?, today: String) : Fragment(), CoroutineScope {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.graph_fragment, container, false)

    var barChar: ArrayList<BarEntry>? = null

    private var nameList: ArrayList<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            action()
            if (barChar != null) {

                if (barChar?.size != 0) {
                    val barDataSet = BarDataSet(barChar, "Cels")

                    val barData = BarData(nameList, barDataSet)

                    chart.run {
                        data = barData
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                    }
                }
            }
        }
        chart.animateY(1500)
    }

    //    表示するやつを作るやつ
    private fun action() = launch (Dispatchers.Default) {
        list?.let {
            if (list.isNotEmpty())
                for (i in list.indices) {
                    list[i].let {
                        barChar = arrayListOf(BarEntry(it, i))
                        nameList = arrayListOf(i.toString())
                    }
                }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}