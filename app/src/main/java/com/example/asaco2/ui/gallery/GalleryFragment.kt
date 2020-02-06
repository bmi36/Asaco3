package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asaco2.R
import com.example.asaco2.StepEntity
import com.example.asaco2.StepViewModel
import com.example.asaco2.ui.gallery.DateRange.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.module_calory.*
import kotlinx.android.synthetic.main.module_distance.*
import kotlinx.android.synthetic.main.module_hosuu.*
import kotlinx.android.synthetic.main.walk_statu_layout.*
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment(
    private val stepCount: Int,
    private val calory: String,
    private val dis: Double
) : Fragment() {

    private lateinit var viewModel: StepViewModel
    private lateinit var arrayStep: Array<StepEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTimeMillis = Calendar.getInstance()
        viewModel = ViewModelProvider(this)[StepViewModel::class.java]

        dayText.text = SimpleDateFormat("yyyy年mm月dd日", Locale.UK).format(currentTimeMillis)
        hosuu_text.text = stepCount.toString()
        calory_text.text = getString(R.string.kcal, calory)
        distance_text.text = getString(R.string.km, String.format("%.1f", dis))

        viewModel.allSteps.observe(viewLifecycleOwner, stepObserver)

        listener(WEEK)
        dayBtn.setOnClickListener { listener(WEEK) }
        monthBtn.setOnClickListener { listener(MONTH) }
    }

    private fun listener(dateRange: DateRange) {

        viewModel.ponkochu.observe(this, Observer {
            it
        })
        //        リストの生成（1週間or12か月）
//        val search: String = when (dateRange) {
//            WEEK -> {
//            }
//            MONTH -> {
//            }
//        }

        //            グラフの表示
        childFragmentManager.beginTransaction()
            .replace(frame.id, GraphFragment(arrayStep, dateRange.size))
            .commit()
    }

    private var stepObserver = Observer<Array<StepEntity>> { arrayStep = it }
}

private fun String.replaceInt() = this.replace("/", "").toInt()