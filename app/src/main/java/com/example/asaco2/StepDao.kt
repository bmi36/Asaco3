package com.example.asaco2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StepDao{
//    @Query("select sum(step) from entity where data between :data - 6 and :data group by data order by data ASC")
//    fun getsumSteps(data: Long): LiveData<Array<Int>>

    @Insert
    suspend fun insert(entity: StepEntity)

    @Update
    suspend fun update(entity: StepEntity)

//    @Query("select sum(step) from entity where data between :year || 01 || '%' and :year || 12 || '%' group by data ")
//    fun getMonth(year: Long): LiveData<Array<Int>>

    @Query("select sum(step) from entity where data between :oldDay and :now")
    fun ponkotsu(now: Long, oldDay: Long): LiveData<Array<Int>>

    @Query("select * from entity")
    fun allStep(): LiveData<Array<StepEntity>>
}