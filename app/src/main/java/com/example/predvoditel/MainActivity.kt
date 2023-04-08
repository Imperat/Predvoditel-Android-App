package com.example.predvoditel

import android.content.Context
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import api.WebClient
import api.newWebClient
import com.example.predvoditel.ui.login.LoggedInUserView
import com.example.predvoditel.ui.tournaments_list.TournamentsFragment
import com.example.predvoditel.ui.login.LoginFragment
import com.example.predvoditel.ui.players_list.PlayerFragment
import com.example.predvoditel.ui.tournament_view.CompletedTournamentFragment
import com.example.predvoditel.ui.tournaments_list.TournamentsRecyclerViewAdapter

private const val TAG = "MainActivity"

private const val JWT_INDEX = "jwt"
private const val REFRESH_TOKEN_INDEX = "refreshToken"

class MainActivity : AppCompatActivity(), LoginFragment.Callbacks, MainMenu.Callbacks,
    TournamentsRecyclerViewAdapter.Callbacks {
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mainViewModel.jwtToken = savedInstanceState.getString(JWT_INDEX)
            mainViewModel.refreshToken = savedInstanceState.getString(REFRESH_TOKEN_INDEX)
        }

        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        if (sharedPref != null) {
            mainViewModel.jwtToken = sharedPref.getString(JWT_INDEX, "")
            mainViewModel.refreshToken = sharedPref.getString(REFRESH_TOKEN_INDEX, "")
            if (mainViewModel.jwtToken != null && mainViewModel.refreshToken != null) {
                WebClient.getInstance().setTokens(
                    mainViewModel.jwtToken!!,
                    mainViewModel.refreshToken!!
                );
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment != null) {
            return;
        }

        Log.i("TAAAAAAAA", mainViewModel.jwtToken + "-")

        val fragment = if (mainViewModel.jwtToken == "") LoginFragment() else MainMenu()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
    }

    override fun onUserLoggedIn(loggedInUserView: LoggedInUserView) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(JWT_INDEX, loggedInUserView.jwtToken)
            putString(REFRESH_TOKEN_INDEX, loggedInUserView.refreshToken)
            apply()
        }

        moveFragment(MainMenu())
    }

    override fun onStartGame() {
        TODO("Not yet implemented")
    }

    override fun onShowAllTournaments() {
        moveFragment(TournamentsFragment())
    }

    override fun onShowPlayers() {
        moveFragment(PlayerFragment())
    }

    override fun onShowSettings() {
        moveFragment(SettingsFragment())
    }

    override fun onShowCrossTournamentStats() {
        TODO("Not yet implemented")
    }

    override fun onShowAbout() {
        moveFragment(AboutFragment())
    }

    override fun onShowFootballMessenger() {
        TODO("Not yet implemented")
    }

    override fun onLogout() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            remove(JWT_INDEX)
            remove(REFRESH_TOKEN_INDEX)
            apply()
        }
        moveFragment(LoginFragment())
    }

    private fun moveFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    override fun openTournament(tournamentId: String, tournamentStatus: String) {
        moveFragment(CompletedTournamentFragment.newInstance(tournamentId));
    }
}