package com.example.predvoditel.ui.tournament_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.GoalMakes
import com.example.predvoditel.databinding.BombardiersItemBinding

class BombardiersRecyclerViewAdapter(private val values: List<GoalMakes>) :
    RecyclerView.Adapter<BombardiersRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BombardiersRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            BombardiersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BombardiersRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.bombardierNameView.text = item.playerName;
        holder.scoreView.text = item.goalsCount.toString();
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: BombardiersItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val bombardierNameView: TextView = binding.playerName
        val teamNameView: TextView = binding.teamName
        val scoreView: TextView = binding.score

        override fun toString(): String {
            return super.toString() + " '" + bombardierNameView.text + "'"
        }
    }
}
