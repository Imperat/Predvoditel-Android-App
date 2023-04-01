package com.example.predvoditel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.predvoditel.ui.login.LoginFragment
import com.example.predvoditel.ui.players_list.PlayerFragment

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), LoginFragment.Callbacks, MainMenu.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = LoginFragment();
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    override fun onUserLoggedIn() {
        val fragment = MainMenu()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onStartGame() {
        TODO("Not yet implemented")
    }

    override fun onShowAllTournaments() {
        TODO("Not yet implemented")
    }

    override fun onShowPlayers() {
        val fragment = PlayerFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onShowSettings() {
        val fragment = SettingsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onShowCrossTournamentStats() {
        TODO("Not yet implemented")
    }

    override fun onShowAbout() {
        val fragment = AboutFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onShowFootballMessenger() {
        TODO("Not yet implemented")
    }

    override fun onLogout() {
        val fragment = LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}