package com.example.predvoditel.ui.tournament_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.TableViewQuery
import com.example.predvoditel.databinding.FragmentTeamsStatsItemBinding

class TeamsStatsRecyclerViewAdapter(private val values: List<TournamentTableDataItem>) :
    RecyclerView.Adapter<TeamsStatsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeamsStatsRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            FragmentTeamsStatsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TeamsStatsRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.teamNameView.text = item.name

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentTeamsStatsItemBinding): RecyclerView.ViewHolder(binding.root) {
        val teamNameView: TextView = binding.teamName;
    }
}