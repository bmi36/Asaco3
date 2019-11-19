package com.example.asaco2.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asacojoin.Repository
import com.example.myapplication.CalendarDatabase
import com.example.myapplication.CalendarEntity
import kotlinx.coroutines.launch
import java.sql.SQLException

class CalendarViewModel(application: Application):AndroidViewModel(application) {

    private val repository: Repository
    val allCalendar: LiveData<Array<CalendarEntity>>

    init {
        val repoDao = CalendarDatabase.getInstance(application).calendarDao()
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