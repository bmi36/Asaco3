package com.example.asaco2

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder

class StepService : Service(), SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var mStepCounterSensor: Sensor? = null
    var step = 0

    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepCounterSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        mSensorManager?.unregisterListener(this, mStepCounterSensor)
    }

    override fun onBind(p0: Intent?): IBinder{
        return StepBindar()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER ) step++
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager?.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
        getSharedPreferences("Step",Context.MODE_PRIVATE).edit().putInt("walk",step).apply()
    }

    inner class StepBindar: Binder(){
        fun getBindar(): StepService = this@StepService
    }
}