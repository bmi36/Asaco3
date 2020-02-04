package com.example.asaco2

import androidx.annotation.WorkerThread
import java.lang.Exception
import java.sql.SQLException

class StepRepository (private val dao: StepDao){

    @WorkerThread
    suspend fun insert(entity: StepEntity) = dao.insert(entity)
    suspend fun update(entity: StepEntity) = dao.update(entity)
    suspend fun getsum(date: Long): Array<Int> = dao.getsumSteps(date)
    suspend fun getMonth(year: Long): Array<Int> = dao.getMonth(year)
    suspend fun getDayEntity(id: Long): StepEntity = dao.getDayEntity(id)
    suspend fun updateOrinsert(entity: StepEntity) = try { insert(entity) }catch (e:Exception){update(entity)}
}