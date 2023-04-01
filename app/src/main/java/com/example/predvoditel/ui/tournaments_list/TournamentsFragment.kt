package com.example.predvoditel.ui.tournaments_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Tournament
import api.WebClient
import com.example.predvoditel.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TournamentsFragment : Fragment() {
    private var tournamentsRecyclerView: RecyclerView? = null
    private var adapter: TournamentsRecyclerViewAdapter? = null;
    private var webClient = WebClient.getInstance();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tournaments, container, false);

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
            tournamentsRecyclerView = view
        }

        updateUI(emptyList())

        MainScope().launch {
            val result = webClient.getTournaments()
            updateUI(result.toList())
        }

        return view;
    }

    private fun updateUI(tournaments: List<Tournament>) {
        adapter = TournamentsRecyclerViewAdapter(tournaments)
        tournamentsRecyclerView?.adapter = adapter
    }
}
