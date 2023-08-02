package com.lymors.earnxtra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.lymors.earnxtra.fragments.*
import com.lymors.earnxtra.models.Users
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var ref: DatabaseReference
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_quote -> {
                    replaceFragment(QuotesFragment())
                    true
                }
                R.id.nav_quiz -> {
                    replaceFragment(QuizFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.nav_notification -> {
                    replaceFragment(NotificationFragment())
                    true
                }
                else -> false
            }
        }

        // Set the initial fragment to be loaded (QuotesFragment in this case)
        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

}