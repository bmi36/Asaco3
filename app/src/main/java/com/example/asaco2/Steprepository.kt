package com.example.asaco2

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import java.lang.Exception
import java.sql.SQLException

class StepRepository (private val dao: StepDao){

    @WorkerThread
    suspend fun insert(entity: StepEntity) = dao.insert(entity)
    suspend fun update(entity: StepEntity) = dao.update(entity)
    fun allSteps(): LiveData<Array<StepEntity>> = dao.allStep()
    fun ponkostu(now: Long,oldDay: Long): LiveData<Array<Int>> = dao.ponkotsu(now,oldDay)
}