package com.example.asaco2.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.asaco2.R
import kotlinx.android.synthetic.main.activity_memo.*

class Memo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        var string =
            "${intent.extras.getInt("year")}年${intent.extras.getInt("month") + 1}月${intent.extras.getInt(
                "day"
            )}日だよ"
        val calendar: CalendarEntity = intent.extras.get("calendar") as CalendarEntity
        string = if (calendar.id != null) {
            "$string\n$calendar"
        } else {
            "$string\nなにもないよ"
        }
        dayText.text = string

        val action = supportActionBar
        action?.title = "詳細画面"
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
