package com.example.asaco2.ui.tools

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.asaco2.R
import com.example.asaco2.hideKeyboard
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_tools.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class ToolsFragment(
    private val content: Context,
    private val navView: NavigationView
) : Fragment() {


    interface FinishBtn {
        fun onClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_tools, container, false)

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = content.getSharedPreferences("User", Context.MODE_PRIVATE)
        val checkId: Int = prefs.getInt("sex", 0)

        userName.setText(prefs.getString("name", "HUNTER"))
        weight.setText(prefs.getString("weight", "0"))
        high.setText(prefs.getString("height", "0"))
        agetext.setText(prefs.getString("age", "0"))
        Btngroup.check(checkId)


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
                val bmr = when (sex?.text.toString()) {
                    "男" -> (66 + 13.7 * wint + 5.0 * hint - 6.8 * age.toInt()).toInt()
                    "女" -> (665.1 + 9.6 * wint + 1.7 * hint - 7.0 * age.toInt()).toInt()
                    else -> 0
                }

                editor.putString("name", str)
                editor.putString("weight", wint.toString())
                editor.putString("height", hint.toString())
                editor.putInt("bmi", bmi)
                editor.putString("age", age)
                sex?.let { editor.putInt("sex", it.id) }
                editor.putInt("bmr", bmr)
                activity?.let {
                    navView.UserName.text = str
                    navView.bmiText.text = "BMI:$bmi"
                }

                (content as FinishBtn).onClick().run {
                    editor.apply()
                    Toast.makeText(activity, "更新しました", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "もう一度やり直してください", Toast.LENGTH_SHORT).show()
            }
            activity?.let { hideKeyboard(it) }
        }
    }
}