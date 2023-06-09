package com.example.predvoditel.ui.players_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import api.Player
import api.PlayersClient
import api.WebClient
import com.example.predvoditel.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class PlayerFragment : Fragment() {
    private var playersRecyclerView: RecyclerView? = null;
    private var adapter: MyPlayerRecyclerViewAdapter? = null;
    private val playersClient = PlayersClient(WebClient.getInstance());


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
            }

            playersRecyclerView = view;
        }

        updateUI(emptyList());

        MainScope().launch {
            // can be launched in a separate asynchronous job
            val result = playersClient.getPlayers();

            updateUI(result.toList() as List<Player>)
        }

        return view
    }

    private fun updateUI(players: List<Player>) {
        adapter = MyPlayerRecyclerViewAdapter(players);
        playersRecyclerView?.adapter = adapter;
    }
}