package com.example.myapplication7

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication7.ui.playlist.PlaylistDetailFragment
import com.example.myapplication7.ui.playlist.PlaylistFragment
import com.example.myapplication7.ui.quiz.QuizFragment
import com.example.myapplication7.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Show the default fragment on launch
        if (savedInstanceState == null) {
            replaceFragment(QuizFragment())
        }

        // Handle BottomNavigationView item selection
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.music -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.playlist -> {
                    replaceFragment(PlaylistFragment())
                    true
                }
                R.id.quiz -> {
                    replaceFragment(QuizFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Replace the fragment in the container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Navigate to PlaylistDetailFragment with playlist ID
    fun navigateToPlaylistDetail(playlistId: Int) {
        val fragment = PlaylistDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("playlistId", playlistId)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
