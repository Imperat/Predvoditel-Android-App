package com.example.predvoditel.ui.tournaments_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.Tournament
import com.example.predvoditel.R
import com.example.predvoditel.databinding.TournamentItemBinding

class TournamentsRecyclerViewAdapter(
    private val values: List<Tournament>,
    private val callbacks: Callbacks
) : RecyclerView.Adapter<TournamentsRecyclerViewAdapter.ViewHolder>() {
    interface Callbacks {
        fun openTournament(tournamentId: String, tournamentStatus: String): Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TournamentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.name;
        val icon = when (item.status) {
            "completed" -> R.drawable.completed
            "started" -> R.drawable.live
            else -> R.drawable.planned
        }

        holder.iconView.setImageResource(icon)
        holder.root.setOnClickListener {
            callbacks.openTournament(item._id, item.status)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(binding: TournamentItemBinding): RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
        val iconView: ImageView = binding.imageButton
        val root = binding.root;

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}