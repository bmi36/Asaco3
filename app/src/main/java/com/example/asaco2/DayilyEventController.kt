package com.example.asaco2

import android.util.Log
import java.util.*

class DayilyEventController(private val thresholdHour: Int, private val thresholdMinute: Int) {
    var isDone: Boolean = false
    lateinit var executeTime: Calendar

    // デイリー範囲内で実行済みかどうかを返す
    fun isDoneDaily(): Boolean {

        // 以前に実行されたことがないのであれば false
        if (!isDone) return false

        // 直近のしきい日時を計算する
        val thresholdTime =
            calculateThresholdDate(thresholdHour, thresholdMinute, Calendar.getInstance())

        // 前回実行日時が、しきい日時よりも未来だったら true
        // |----+[thresholdTime]+----+[executeTime]+----+[currentTime]+---|
        // 逆だったら false
        // |----+[executeTime]+----+[thresholdTime]+----+[currentTime]+---|
        return executeTime.after(thresholdTime)

    }

    // イベントを実行し、実行時間を保持する
    fun execute() {
        val executeTime = Calendar.getInstance()
        Log.d("Daily", "date: ${executeTime.get(Calendar.MINUTE)}")
        this.executeTime = executeTime
        isDone = true
    }

    // 現在時刻の直前のリセット時刻を作る
    fun calculateThresholdDate(
        thresholdHour: Int,
        thresholdMinute: Int,
        currentTime: Calendar
    ): Calendar {
        var thresholdTime = currentTime.clone() as Calendar

        thresholdTime.set(Calendar.HOUR_OF_DAY, thresholdHour)
        thresholdTime.set(Calendar.MINUTE, thresholdMinute)

        // 現在時刻がしきい日時より過去になってしまったら、しきい日時を１日前にする
        if (currentTime.before(thresholdTime)) {
            thresholdTime.add(Calendar.DAY_OF_MONTH, -1)
        }

        Log.d(
            "Daily",
            "threshold time is: ${thresholdTime.get(Calendar.MONTH) + 1}/${thresholdTime.get(
                Calendar.DAY_OF_MONTH
            )} ${thresholdTime.get(Calendar.HOUR_OF_DAY)}:${thresholdTime.get(Calendar.MINUTE)}"
        )

        return thresholdTime
    }
}