package com.example.asaco2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StepDao{
    @Query("select sum(step) from entity where data between :data - 6 and :data group by data")
    suspend fun getsumSteps(data: Long): Array<Int>

    @Insert
    suspend fun insert(entity: StepEntity)

    @Update
    suspend fun update(entity: StepEntity)

    @Query("select sum(step) from entity where data between :year || 01 || '%' and :year || 12 || '%' group by data")
    suspend fun getMonth(year: Long): Array<Int>

    @Query("select * from entity where data like :data || '%'")
    fun getDayEntity(data: Long): StepEntity
}