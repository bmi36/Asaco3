package com.example.asaco2.ui.camera

import com.google.gson.Gson

data class Cook(
    val foodname: String,
    val calorie: Int
)

fun Cook.toJson(): String = Gson().toJson(this)
fun String.toDataClass(): Cook = Gson().fromJson(this, Cook::class.java)