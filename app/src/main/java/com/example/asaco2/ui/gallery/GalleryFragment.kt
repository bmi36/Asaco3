package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.StepViewModel
import com.example.asaco2.time
import com.example.asaco2.today
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.walk_statu_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class GalleryFragment(
    private val stepcount:Int
) : Fragment(), CoroutineScope {

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

        viewModel = ViewModelProviders.of(this)[StepViewModel::class.java]

        dayText.text = today

        dayBtn.setOnClickListener { listener(7) }
        monthBtn.setOnClickListener { listener(12) }
    }

    private fun listener(size: Int) {
        val search: Int = when (size) {
            7 -> day.reInt()
            12 -> month.reInt()
            else -> 0
        }

        launch(Default) {
            //        リストの生成（1週間or12か月）
            val list    : Array<Int> = viewModel.getStep(day.toLong())
//            グラフの表示
            childFragmentManager.beginTransaction().replace(frame.id, GraphFragment(list, SimpleDateFormat("yyyy/MM/dd").run {format(
                time)}))
                .commit()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()

}

private val currentTimeMillis = Date(System.currentTimeMillis())
private val day: String = SimpleDateFormat("yyyyMMdd").run { format(currentTimeMillis) }
private val month: String = SimpleDateFormat("yyyy/MM").run { format(currentTimeMillis) }
private fun String.reInt() = this.replace("/", "").toInt()
private val year: String = SimpleDateFormat("yyyy").run { format(currentTimeMillis) }