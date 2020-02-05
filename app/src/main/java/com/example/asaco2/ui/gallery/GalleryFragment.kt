package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.asaco2.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.module_calory.*
import kotlinx.android.synthetic.main.module_distance.*
import kotlinx.android.synthetic.main.module_hosuu.*
import kotlinx.android.synthetic.main.walk_statu_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class GalleryFragment(private val stepCount: Int, private val calory: String, private val dis: Double) : Fragment(),

    CoroutineScope {

    private lateinit var viewModel: StepViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[StepViewModel::class.java]

        dayText.text = time.toString()
        hosuu_text.text = stepCount.toString()
        calory_text.text = getString(R.string.kcal,calory)
        distance_text.text = getString(R.string.km,String.format("%.1f", dis))

        listener(7)
        dayBtn.setOnClickListener { listener(7) }
        monthBtn.setOnClickListener { listener(12) }
    }

    private fun listener(size: Int) {
        val currentTimeMillis = Date(System.currentTimeMillis())
        val search: String = when (size) {
            7 -> SimpleDateFormat("yyyy/MM/dd", Locale.US).run { format(currentTimeMillis) }
            else -> SimpleDateFormat("yyyy/MM", Locale.US).run { format(currentTimeMillis) }
        }

        launch(Default) {
            //        リストの生成（1週間or12か月）
            val list: List<Float>? = when (size) {
                7 -> viewModel.getStep(search.replaceInt().toLong())?.map { it.toFloat() }
                12 -> viewModel.getmonth(search.replaceInt().toLong()).map { it.toFloat() }
                else -> null
            }

            //            グラフの表示
            childFragmentManager.beginTransaction()
                .replace(frame.id, GraphFragment(list?.toTypedArray(),size,currentTimeMillis)).commit()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()

}
private fun String.replaceInt() = this.replace("/", "").toInt()