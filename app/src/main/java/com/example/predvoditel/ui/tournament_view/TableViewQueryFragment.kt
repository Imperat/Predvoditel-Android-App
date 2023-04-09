package com.example.predvoditel.ui.tournament_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.PlayerInfo
import api.TournamentStats
import com.example.predvoditel.R

private const val TOURNAMENT_STATS = "TOURNAMENT_STATS";

class TableViewQueryFragment: Fragment() {
    private lateinit var tournamentStats: TournamentStats
    private var teamsRecyclerView: RecyclerView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tournamentStats = it.getParcelable(TOURNAMENT_STATS)!!
        }

        initTournamentsTable()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_completed_tournament_table_view, container, false)
        teamsRecyclerView = view.findViewById(R.id.teamsStats);

        if (teamsRecyclerView != null) {
            teamsRecyclerView?.layoutManager = LinearLayoutManager(context)
            teamsRecyclerView?.adapter = TeamsStatsRecyclerViewAdapter(initTournamentsTable())
        }

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(tournamentStats: TournamentStats) =
            TableViewQueryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TOURNAMENT_STATS, tournamentStats)
                }
            }
    }

    private fun initTournamentsTable(): List<TournamentTableDataItem> {
        val teamsMap = mutableMapOf<String, TournamentTableDataItem>()
        tournamentStats.teamsResult.forEach {
            teamsMap[it.teamId] = TournamentTableDataItem(
                it.teamName,
                it.teamColor,
                it.players,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
            );
        }


            tournamentStats.tableViewQuery.forEach { game ->
                if (game.firstTeam.goals.size == game.secondTeam.goals.size) {
                    teamsMap[game.firstTeam._id]!!.points += 1;
                    teamsMap[game.firstTeam._id]!!.drawns += 1;
                    teamsMap[game.secondTeam._id]!!.points += 1;
                    teamsMap[game.secondTeam._id]!!.drawns += 1;
                } else if (game.firstTeam.goals.size > game.secondTeam.goals.size) {
                    teamsMap[game.firstTeam._id]!!.points += 3;
                    teamsMap[game.firstTeam._id]!!.wins += 1;
                    teamsMap[game.secondTeam._id]!!.loses += 1;
                } else {
                    teamsMap[game.secondTeam._id]!!.points += 3;
                    teamsMap[game.secondTeam._id]!!.wins += 1;
                    teamsMap[game.firstTeam._id]!!.loses += 1;
                }

                teamsMap[game.firstTeam._id]!!.games += 1;
                teamsMap[game.secondTeam._id]!!.games += 1;

                teamsMap[game.firstTeam._id]!!.scored += game.firstTeam.goals.size;
                teamsMap[game.firstTeam._id]!!.failed += game.secondTeam.goals.size;

                teamsMap[game.secondTeam._id]!!.scored += game.secondTeam.goals.size;
                teamsMap[game.secondTeam._id]!!.failed += game.firstTeam.goals.size;
            }

        return teamsMap.values.sorted()
    }
}

class TournamentTableDataItem(
    val name: String,
    val color: String,
    val players: List<PlayerInfo>,
    var games: Int,
    var wins: Int,
    var drawns: Int,
    var loses: Int,
    var scored: Int,
    var failed: Int,
    var points: Int,
): Comparable<TournamentTableDataItem> {
    override fun compareTo(other: TournamentTableDataItem): Int = when {
        this.points != other.points -> this.points compareTo other.points
        this.scored - this.failed != other.scored - other.failed -> this.scored - this.failed compareTo other.scored - other.failed
        this.games != other.games -> this.games compareTo other.games;
        else -> 0
    }
}
