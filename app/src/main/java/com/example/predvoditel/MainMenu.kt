package com.example.predvoditel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenu : Fragment() {
    interface Callbacks {
        fun onStartGame()
        fun onShowAllTournaments()
        fun onShowPlayers()
        fun onShowSettings()
        fun onShowCrossTournamentStats()
        fun onShowAbout()
        fun onShowFootballMessenger()
        fun onLogout()
    }

    private var callbacks: Callbacks? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var startGameButton: Button
    private lateinit var showTournamentsButton: Button
    private lateinit var showPlayersButton: Button
    private lateinit var showSettingsButton: Button
    private lateinit var crossTournamentStatsButton: Button
    private lateinit var aboutButton: Button
    private lateinit var footballMessangerButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)

        startGameButton = view.findViewById(R.id.startGameButton)
        showTournamentsButton = view.findViewById(R.id.allTournamentsButton)
        showPlayersButton = view.findViewById(R.id.playersButton)
        showSettingsButton = view.findViewById(R.id.settingsButton)
        crossTournamentStatsButton = view.findViewById(R.id.crossTournamentStatsButton)
        aboutButton = view.findViewById(R.id.aboutButton)
        footballMessangerButton = view.findViewById(R.id.footballMessengerButton)
        logoutButton = view.findViewById(R.id.logOutButton)

        startGameButton.setOnClickListener {
            callbacks?.onStartGame()
        }

        showTournamentsButton.setOnClickListener {
            callbacks?.onShowAllTournaments()
        }

        showPlayersButton.setOnClickListener {
            callbacks?.onShowPlayers()
        }

        showSettingsButton.setOnClickListener {
            callbacks?.onShowSettings()
        }

        crossTournamentStatsButton.setOnClickListener {
            callbacks?.onShowCrossTournamentStats()
        }

        aboutButton.setOnClickListener {
            callbacks?.onShowAbout()
        }

        footballMessangerButton.setOnClickListener {
            callbacks?.onShowFootballMessenger()
        }

        logoutButton.setOnClickListener {
            callbacks?.onLogout()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainMenu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainMenu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}