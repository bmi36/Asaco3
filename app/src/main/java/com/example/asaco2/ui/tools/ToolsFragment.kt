package com.example.asaco2.ui.tools

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_tools.*
import kotlinx.android.synthetic.main.fragment_tools.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class ToolsFragment(
    private val navView: NavigationView,
    private val prefs: SharedPreferences
) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_tools, container, false)
        val str = prefs.getString("name", "HUNTER")
        val wight = prefs.getInt("wight", 0)
        val high = prefs.getInt("height", 0)
        view.userName.setText(str, TextView.BufferType.NORMAL)
        view.wight.setText(wight.toString(), TextView.BufferType.NORMAL)
        view.high.setText(high.toString(), TextView.BufferType.NORMAL)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserButton.setOnClickListener {
            val editor = prefs.edit()
            val str = userName.text.toString()
            val wint = high.text.toString().toInt()
            val hint = wight.text.toString().toInt()
            val bmi = if (wint == 0 || hint == 0){
                 0
            }else{
                 val tmp = (hint*hint)/10
                wint*1000/tmp
            }

            editor.putString("name", str)
            editor.putInt("wight", hint)
            editor.putInt("height", wint)
            editor.putInt("bmi", bmi)
            navView.UserName.text = str
            navView.bmiText.text = "   BMI:$bmi"
            editor.apply()
        }
    }
}