package com.example.asaco2.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.asaco2.R


class FoodAdapter(
    context: Fragment,
    private val list: List<CalendarEntity>
) : RecyclerView.Adapter<FoodListViewHolder>() {

    private val inflater = LayoutInflater.from(context.context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val view = inflater.inflate(R.layout.food_list_row,parent,false)
        return FoodListViewHolder(view)


    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        list[position].let {
            holder.foodText.text = it.food
            holder.calText.text = it.absorption.toString()
        }
    }

}

class FoodListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val foodText: TextView = view.findViewById(R.id.foodText)
    val calText: TextView = view.findViewById(R.id.foodCalText)

}