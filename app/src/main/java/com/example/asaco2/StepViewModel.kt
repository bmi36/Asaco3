package com.example.asaco2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class StepViewModel(application: Application) : AndroidViewModel(application){
    private val repository: StepRepository = StepDataBase.getInstance(application).dao().let {
        StepRepository(it)
    }

    private val _ponkochu: MutableLiveData<Array<Int>> = MutableLiveData()

    val allSteps: LiveData<Array<StepEntity>>
    val ponkochu:LiveData<Array<Int>> = _ponkochu
    val date = Date(System.currentTimeMillis())

    init {
        allSteps = repository.allSteps()
    }

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch{ repository.update(entity) }
}