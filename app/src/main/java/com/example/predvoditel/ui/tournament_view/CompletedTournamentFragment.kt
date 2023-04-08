package com.example.predvoditel.ui.tournament_view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.*
import com.example.predvoditel.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TOURNAMENT_ID_PARAM = "tournamentId"

class CompletedTournamentFragment : Fragment() {
    private var tournamentId: String? = null
    private val tournamentsClient: TournamentsClient = TournamentsClient(WebClient.getInstance())

    private var bombardiersRecyclerView: RecyclerView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tournamentId = it.getString(TOURNAMENT_ID_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed_tournament, container, false)

        bombardiersRecyclerView = view.findViewById(R.id.bombardiersStats);

        if (bombardiersRecyclerView != null) {
            bombardiersRecyclerView!!.layoutManager = LinearLayoutManager(context)
        }

        MainScope().launch {
            val stats = tournamentsClient.getStats(tournamentId!!)
            updateUI(stats)
        }
        return view
    }

    fun updateUI(stats: TournamentStats) {
        bombardiersRecyclerView?.adapter = BombardiersRecyclerViewAdapter(stats.goalMakers)
    }

    companion object {
        @JvmStatic
        fun newInstance(tournamentId: String) =
            CompletedTournamentFragment().apply {
                arguments = Bundle().apply {
                    putString(TOURNAMENT_ID_PARAM, tournamentId)
                }
            }
    }
}
