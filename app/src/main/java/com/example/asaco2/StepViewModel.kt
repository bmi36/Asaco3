package com.example.asaco2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class StepViewModel(application: Application) : AndroidViewModel(application){
    private val repository: StepRepository = StepDataBase.getInstance(application).dao().let {
        StepRepository(it)
    }

    fun getStep(id: Long) = runBlocking { getsum(id) }
    fun getMonth(year: Long) = runBlocking { getmonth(year) }
    fun getDayEntity(id: Long) = runBlocking { getdayentity(id) }

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch{ repository.update(entity) }
    suspend fun getsum(id: Long): Array<Int>? = withContext(Dispatchers.Default) { repository.getsum(id) }
    suspend fun getmonth(year: Long): Array<Int> = withContext(Dispatchers.Default){ repository.getMonth(year) }
    suspend fun getdayentity(id: Long): StepEntity = withContext(Dispatchers.Default){ repository.getDayEntity(id) }
}