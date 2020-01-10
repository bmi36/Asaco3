package com.example.asaco2.ui.tools

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_tools.*
import kotlinx.android.synthetic.main.fragment_tools.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class ToolsFragment(
    private val content: Context,
    private val navView: NavigationView
) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_tools, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = content.getSharedPreferences("User",Context.MODE_PRIVATE)

        userName.setText(prefs.getString("name", "HUNTER"))
        weight.setText(prefs.getString("weight", "0"))
        high.setText(prefs.getString("height", "0"))
        agetext.setText(prefs.getString("age","0"))
        Btngroup.check(prefs.getInt("sex",0))


        UserButton.setOnClickListener {
            try {
                val editor = prefs.edit()
                val str = userName.text.toString()
                val wint = weight.text.toString().toDouble()
                val hint = high.text.toString().toDouble()
                val temp = (hint * hint) / 10000
                val bmi = (wint / temp).toInt()
                val age = agetext.text.toString()
                val sex = activity?.findViewById<RadioButton>(Btngroup.checkedRadioButtonId)
                val bmr= when(sex?.text.toString()){
                    "男" ->(66+13.7*wint+5.0*hint-6.8*age.toInt()).toInt()
                    "女" ->(665.1+9.6*wint+1.7*hint-7.0*age.toInt()).toInt()
                    else -> 0
                }

                editor.putString("name", str)
                editor.putString("weight", wint.toString())
                editor.putString("height", hint.toString())
                editor.putInt("bmi", bmi)
                editor.putString("age", age)
                sex?.id?.let { it -> editor.putInt("sex", it) }
                editor.putInt("bmr", bmr)

                navView.UserName.text = str
                navView.bmiText.text = "   BMI:$bmi"
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "もう一度やり直してください", Toast.LENGTH_SHORT).show()
            }
        }
    }
}