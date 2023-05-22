package com.example.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.remote.response.ListStory
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.viewmodel.HomeViewModel
import com.example.storyapp.viewmodel.LoginPreferences
import com.example.storyapp.viewmodel.LoginViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.all_story)

        binding.rvStory.layoutManager = LinearLayoutManager(this@HomeActivity)

        val pref = LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this@HomeActivity, ViewModelFactory(pref))[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this) {
            homeViewModel.getAllStory(it)
        }

        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        homeViewModel.story.observe(this) {
            setStory(it)
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@HomeActivity, UploadActivity::class.java))
        }
    }

    private fun setStory(story: List<ListStory>) {
        val list = ArrayList<ListStory>()
        for (i in story) {
            list.addAll(listOf(i))
        }
        binding.rvStory.adapter = StoryAdapter(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val pref = LoginPreferences.getInstance(dataStore)
                val loginViewModel = ViewModelProvider(this@HomeActivity, ViewModelFactory(pref))[LoginViewModel::class.java]
                loginViewModel.setLoggedIn(false)
                loginViewModel.setToken("")
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                finish()
            }

            R.id.action_refresh -> {
                val pref = LoginPreferences.getInstance(dataStore)
                val loginViewModel = ViewModelProvider(this@HomeActivity, ViewModelFactory(pref))[LoginViewModel::class.java]
                loginViewModel.getToken().observe(this) {
                    homeViewModel.getAllStory(it)
                }
            }

            R.id.action_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}