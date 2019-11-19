package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Entity")
data class CalendarEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "entity_food")
    var food: String,

    @ColumnInfo(name= "entity_burned")
    var burned: Int,

    @ColumnInfo(name = "entity_absorption")
    var absorption: Int,

    @ColumnInfo(name = "entity_step")
    var step: Int


):Serializable