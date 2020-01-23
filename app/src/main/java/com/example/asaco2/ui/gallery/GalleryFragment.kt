package com.example.asaco2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.asaco2.R
import com.example.asaco2.today
import com.example.asaco2.ui.home.StepViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.walk_statu_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class GalleryFragment(private val step: Int) : Fragment(),CoroutineScope {

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

    private val currentTimeMillis = Date(System.currentTimeMillis())
    val day: String = SimpleDateFormat("yyyyMMdd").run { format(currentTimeMillis) }
    val month: String = SimpleDateFormat("yyyyMM").run { format(Date(System.currentTimeMillis())) }
    val daylast = Calendar.getInstance().let {
        it.set(Calendar.MONTH, 1)
        it.getActualMaximum(Calendar.DATE)
    }

    private fun listener(size: Int) {
        val search = when (size) {
            7 -> day
            12 -> month
            else -> null
        }
        var list: Array<Int>?
        runBlocking(Default) {
                try {
                    list = Array(size) { index ->
                        viewModel.getsumstep((search?.let { it.toInt() - index - 1 } ?: 0).toLong())
                    }
                    arrayOf(step)
                    childFragmentManager.beginTransaction().replace(frame.id, GraphFragment(list)).commit()
                }catch (e: SQLException){
                    e.printStackTrace()
                }

            }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}