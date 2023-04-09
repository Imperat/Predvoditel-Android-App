package com.example.predvoditel.ui.tournament_view

import GoalMakersFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import api.*
import com.example.predvoditel.R
import com.google.android.material.tabs.TabLayout
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
        val demoCollectionPagerAdapter = CompletedTournamentPagerAdapter(
            childFragmentManager, stats, listOf(
                getString(R.string.tournamentTable),
                getString(R.string.bombardiers),
                getString(R.string.completedGames),
            )
        )

        val viewPager = view?.findViewById<ViewPager>(R.id.completed_stats_view_pager)
        viewPager?.adapter = demoCollectionPagerAdapter
        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout?.setupWithViewPager(viewPager)
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

class CompletedTournamentPagerAdapter(
    fm: FragmentManager,
    val stats: TournamentStats,
    val tabNames: List<CharSequence>
) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(i: Int): Fragment {
        return when (i) {
            0 -> TableViewQueryFragment.newInstance(stats)
            1 -> GoalMakersFragment.newInstance(stats)
            else -> GoalMakersFragment.newInstance(stats)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabNames[position]
    }
}
