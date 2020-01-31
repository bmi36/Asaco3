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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class GraphFragment(
    private val list: Array<Float>?, private val size: Int, val search: Date
) : Fragment(), CoroutineScope {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.graph_fragment, container, false)

    private val barChar: ArrayList<BarEntry> = ArrayList(size)

    private val nameList: ArrayList<String> = ArrayList(size)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            action()
            barChar.let {
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

//    private var word = search.split("/").last().toInt()

    private val calendar = Calendar.getInstance()

    //    表示するやつを作るやつ
    private fun action() {

        list?.let {
            if (it.isNotEmpty()) {
                for (i in 0..it.lastIndex) {
                    addlist(i, it[i])
                }
            }
        }
    }

    fun addlist(index: Int, element: Float) {
        calendar.time = search
        var strDate: String
        val unit = when (size) {
            7 -> getString(R.string.Day).also {
                strDate = SimpleDateFormat("yyyy/MM/dd", Locale.US).run { format(calendar) }
                calendar.add(Calendar.DATE,-1)
            }
            else -> getString(R.string.Month).also {
                strDate = SimpleDateFormat("yyyy/MM", Locale.US).run { format(calendar) }
                calendar.add(Calendar.MONTH,-1)
            }
        }
        barChar.add(BarEntry(element,index))
        nameList.add(index,"$strDate$unit")
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}