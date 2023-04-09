package com.example.predvoditel.ui.tournament_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import api.*
import com.example.predvoditel.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TOURNAMENT_ID_PARAM = "tournamentId"

class CompletedTournamentFragment : Fragment() {
    private var tournamentId: String? = null
    private val tournamentsClient: TournamentsClient = TournamentsClient(WebClient.getInstance())


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

        MainScope().launch {
            val stats = tournamentsClient.getStats(tournamentId!!)
            updateUI(stats)
        }
        return view
    }

    fun updateUI(stats: TournamentStats) {
        val demoCollectionPagerAdapter = DemoCollectionPagerAdapter(childFragmentManager, stats)
        val viewPager = view?.findViewById<ViewPager>(R.id.completed_stats_view_pager)
        viewPager?.adapter = demoCollectionPagerAdapter
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

private const val TOURNAMENT_STATS = "TOURNAMENT_STATS";

class GoalMakersFragment: Fragment() {
    private lateinit var tournamentStats: TournamentStats
    private var bombardiersRecyclerView: RecyclerView? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tournamentStats = it.getParcelable(TOURNAMENT_STATS)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed_tournament_goal_makers, container, false)
        bombardiersRecyclerView = view.findViewById(R.id.bombardiersStats);

        if (bombardiersRecyclerView != null) {
            bombardiersRecyclerView!!.layoutManager = LinearLayoutManager(context)
            bombardiersRecyclerView?.adapter = BombardiersRecyclerViewAdapter(tournamentStats.goalMakers)
        }

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(tournamentStats: TournamentStats) =
            GoalMakersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TOURNAMENT_STATS, tournamentStats)
                }
            }
    }
}

class DemoCollectionPagerAdapter(fm: FragmentManager, val stats: TournamentStats) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int  = 3

    override fun getItem(i: Int): Fragment {
        val fragment = GoalMakersFragment.newInstance(stats)
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "OBJECT ${(position + 1)}"
    }
}
