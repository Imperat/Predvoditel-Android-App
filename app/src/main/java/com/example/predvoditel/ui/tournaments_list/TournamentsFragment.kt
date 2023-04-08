package com.example.predvoditel.ui.tournaments_list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Tournament
import api.TournamentsClient
import api.WebClient
import com.example.predvoditel.MainMenu
import com.example.predvoditel.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TournamentsFragment : Fragment() {
    private var tournamentsRecyclerView: RecyclerView? = null
    private var adapter: TournamentsRecyclerViewAdapter? = null;
    private var tournamentsClient = TournamentsClient(WebClient.getInstance());
    private lateinit var callbacks: TournamentsRecyclerViewAdapter.Callbacks;

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
            val result = tournamentsClient.getTournaments()
            updateUI(result.toList())
        }

        return view;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as TournamentsRecyclerViewAdapter.Callbacks
    }

    private fun updateUI(tournaments: List<Tournament>) {
        adapter = TournamentsRecyclerViewAdapter(tournaments, callbacks)
        tournamentsRecyclerView?.adapter = adapter
    }
}
