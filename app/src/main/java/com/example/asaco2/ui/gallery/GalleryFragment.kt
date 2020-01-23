package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.time
import com.example.asaco2.today
import com.example.asaco2.ui.home.StepViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.walk_statu_layout.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment : Fragment() {

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

        runBlocking(Default) {
            val list: Array<Int> = Array(size) { index ->
                (search - index).toLong().let { viewModel.getsumstep(it) }
            }

            childFragmentManager.beginTransaction().replace(frame.id, GraphFragment(list, time)).commit()
        }
    }

}
private val currentTimeMillis = Date(System.currentTimeMillis())
private val day: String = SimpleDateFormat("yyyyMMdd").run { format(currentTimeMillis) }
private val month: String = SimpleDateFormat("yyyy/MM").run { format(currentTimeMillis) }
private fun String.reInt() = this.replace("/", "").toInt()