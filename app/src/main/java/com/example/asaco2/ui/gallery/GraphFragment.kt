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

class GraphFragment(
    private val list: Array<Float>?, private val size: Int, private val search: String
) : Fragment(), CoroutineScope {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.graph_fragment, container, false)

    private var barChar: ArrayList<BarEntry>? = ArrayList(size)

    private var nameList: ArrayList<String>? = ArrayList(size)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            action()
            barChar?.let {
                if (it.size != 0) {
                    val barDataSet = BarDataSet(it, "Cels")
                    val barData = BarData(nameList, barDataSet)
                    chart.run {
                        data = barData
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                    }
                }
            }
        }
        chart.animateY(1000)
    }

    private var word = search.split("/").last().toInt()
    //    表示するやつを作るやつ
    private fun action() {

        list?.let {

            if (list.isNotEmpty()) {
                for (i in list.indices) {
                    addlist(i, list[i])
                    if (word < 0) break
                }
                when (val lost: Int = size - list.size) {
                    0 -> Unit
                    else -> for (i in lost..0) {
                        addlist(list.lastIndex + 1, 0f)
                        if (word < 0) break
                    }
                }
            }
        }
    }

    fun addlist(index: Int, element: Float) {
        val unit = when (size) {
            7 -> "日"
            else -> "月"
        }
        barChar?.add(BarEntry(element, index))
        nameList?.add("$word$unit")
        word--
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}