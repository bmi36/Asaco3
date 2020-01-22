package com.example.asaco2.ui.home

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.selects.select

@Dao
interface CalendarDao {

    //💩
    @Query("select * from CalendarEntity")
    fun getEntity(): LiveData<Array<CalendarEntity>>

    @Query("select * from CalendarEntity where id like :id || '%'")
    fun getEntity(id: Long): List<CalendarEntity>?

    //リストに追加
    @Insert
    suspend fun insert(entity: CalendarEntity)

    //リストを更新
    @Update
    suspend fun update(entity: CalendarEntity)


}

@Dao
interface StepDao{

    @Query("select * from StepEntity where id like :date || '%'")
    fun getEntity(date: Long): Array<Step>

    @Insert
    suspend fun insert(entity: Step)

    @Update
    suspend fun update(entity: Step)

    @Query("select sum(entity_step) from StepEntity where id like :date||'%'")
    fun getstep(date: Long): Int
}