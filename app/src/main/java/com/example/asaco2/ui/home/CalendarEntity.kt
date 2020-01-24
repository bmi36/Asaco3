package com.example.asaco2.ui.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "CalendarEntity")
data class CalendarEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "entity_food")
    var food: String?,


    @ColumnInfo(name = "entity_absorption")
    var absorption: Int?


):Serializable
