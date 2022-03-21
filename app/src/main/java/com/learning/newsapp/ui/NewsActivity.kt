package com.learning.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.learning.newsapp.R
import com.learning.newsapp.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            supportFragmentManager.findFragmentById(R.id.navHostFragment)?.findNavController()
        binding.apply {
            if (navController != null) {
                bottomNavigationView.setupWithNavController(navController)
            }
        }
    }

}