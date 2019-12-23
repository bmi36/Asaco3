package com.example.asaco2.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.sql.SQLException

class CalendarViewModel(application: Application):AndroidViewModel(application) {

    private val repository: Repository
    val allCalendar: LiveData<Array<CalendarEntity>>

    private var repoDao: CalendarDao = CalendarDatabase.getInstance(application).calendarDao()

    init {
        repository = Repository(repoDao)
            allCalendar = repository.allEntity
    }

    fun insert(entity: CalendarEntity) = viewModelScope.launch {
        repository.insert(entity)
    }

    fun update(entity: CalendarEntity) = viewModelScope.launch {
        repository.update(entity)
    }

    fun InsertOrUpdata(entity: CalendarEntity) = viewModelScope.launch {
        try {
            repository.insert(entity)
        }catch (e:SQLException){
            e.printStackTrace()
            repository.update(entity)
        }
    }
}