package com.example.asaco2.ui.tools

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    ): View? =
        inflater.inflate(R.layout.fragment_tools, container, false).also {
            it.userName.setText(prefs.getString("name", "HUNTER"), TextView.BufferType.NORMAL)
            it.weight.setText(prefs.getFloat("weight", 0f).toString(), TextView.BufferType.NORMAL)
            it.high.setText(prefs.getFloat("height", 0f).toString(), TextView.BufferType.NORMAL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserButton.setOnClickListener {
            try {
                val editor = prefs.edit()
                val str = userName.text.toString()
                val wint = weight.text.toString().toDouble()
                val hint = high.text.toString().toDouble()
                val temp = (hint * hint) / 10000

                Log.d("test", wint.toString())
                Log.d("test", hint.toString())

                val bmi = (wint / temp).toInt()

                Log.d("test", bmi.toString())
                editor.putString("name", str)
                editor.putFloat("weight", wint.toFloat())
                editor.putFloat("height", hint.toFloat())
                editor.putInt("bmi", bmi)
                navView.UserName.text = str
                navView.bmiText.text = "   BMI:$bmi"
                editor.apply()
            } catch (e: Exception) {
                Toast.makeText(activity, "もう一度やり直してください", Toast.LENGTH_SHORT)
            }
        }
    }
}