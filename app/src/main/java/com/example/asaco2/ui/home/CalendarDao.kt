package com.example.asaco2.ui.home

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CalendarDao {

    //💩
    @Query("select * from Entity")
    fun getEntity(): LiveData<Array<CalendarEntity>>

    @Query("select * from Entity where id = :id")
    fun getEntity(id: Long): LiveData<CalendarEntity>

    //リストに追加
    @Insert
    suspend fun insert(entity: CalendarEntity)

    //リストを更新
    @Update
    suspend fun update(entity: CalendarEntity)




}