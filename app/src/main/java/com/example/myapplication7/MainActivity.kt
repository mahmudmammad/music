package com.example.myapplication7

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication7.ui.playlist.PlaylistFragment
import com.example.myapplication7.ui.playlist.QuizFragment
import com.example.myapplication7.ui.playlist.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        replaceFragment(QuizFragment())

        bottomNavigation.setOnItemSelectedListener { item ->
            Log.d("MainActivity", "Item clicked: ${item.itemId}")
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

    private fun replaceFragment(fragment: Fragment): Boolean {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
        return true
    }
}