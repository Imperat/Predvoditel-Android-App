package com.example.predvoditel

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.predvoditel.ui.login.LoggedInUserView
import com.example.predvoditel.ui.tournaments_list.TournamentsFragment
import com.example.predvoditel.ui.login.LoginFragment
import com.example.predvoditel.ui.players_list.PlayerFragment

private const val TAG = "MainActivity"

private const val JWT_INDEX = "jwt"
private const val REFRESH_TOKEN_INDEX = "refreshToken"

class MainActivity : AppCompatActivity(), LoginFragment.Callbacks, MainMenu.Callbacks {
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mainViewModel.jwtToken = savedInstanceState.getString(JWT_INDEX)
            mainViewModel.refreshToken = savedInstanceState.getString(REFRESH_TOKEN_INDEX)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment != null) {
            return;
        }

        val fragment = if (mainViewModel.jwtToken == null) LoginFragment() else MainMenu()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(JWT_INDEX, mainViewModel.jwtToken)
        outState.putString(REFRESH_TOKEN_INDEX, mainViewModel.refreshToken)
    }

    override fun onUserLoggedIn(loggedInUserView: LoggedInUserView) {
        mainViewModel.jwtToken = loggedInUserView.jwtToken
        mainViewModel.refreshToken = loggedInUserView.refreshToken

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
        moveFragment(LoginFragment())
    }

    private fun moveFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}