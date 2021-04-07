package com.bootcamp.watch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shared.Meal
import kotlinx.android.synthetic.main.adapter_meal.view.*

class MealListAdapter(
        private val meals: MutableList<Meal>,
        private val callback: Callback?
) : RecyclerView.Adapter<MealListAdapter.MealViewHolder>() {

  override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
    val meal = meals[position]
    holder.itemView.title.text = meal.title
    holder.itemView.ingredients.text = meal.ingredients.joinToString(separator = ", ")
    holder.itemView.calories.text = meal.calories.toString()
    holder.itemView.star.visibility = if (meal.favorited) View.VISIBLE else View.INVISIBLE
    holder.itemView.setOnClickListener {
      callback?.mealClicked(meal)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_meal, parent, false)
    return MealViewHolder(view)
  }

  override fun getItemCount() = meals.size

  fun updateMeal(meal: Meal) {
    for ((index, value) in meals.withIndex()) {
      if (value.title == meal.title) {
        meals[index] = meal
      }
    }

    notifyDataSetChanged()
  }

  inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view)

  interface Callback {
    fun mealClicked(meal: Meal)
  }
}