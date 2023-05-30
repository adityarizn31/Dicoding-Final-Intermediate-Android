package com.dicoding.sub1_appsstory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sub1_appsstory.RemoteData.Result
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sub1_appsstory.Adapter.AdapterStory
import com.dicoding.sub1_appsstory.Adapter.LoadingStateAdapter
import com.dicoding.sub1_appsstory.Data.ListStoryItem
import com.dicoding.sub1_appsstory.Login.LoginActivity
import com.dicoding.sub1_appsstory.Map.MapsActivity
import com.dicoding.sub1_appsstory.Story.AddStoryActivity
import com.dicoding.sub1_appsstory.ViewModel.MainViewModel
import com.dicoding.sub1_appsstory.ViewModel.ViewModelFactory
import com.dicoding.sub1_appsstory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager


        // Tombol Floating untuk menambah story baru
        binding.addNewStory.setOnClickListener {
            val addStory = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(addStory)
        }

        // Tombol Floating untuk menuju ke Maps
        binding.addMaps.setOnClickListener {
            val addMaps = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(addMaps)
        }

        getStory()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)

        val logout = menu.findItem(R.id.logout)

        logout.setOnMenuItemClickListener(this)
        return true

    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mainViewModel.setPreference("", this)
                val loginActivity = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(loginActivity)
                finish()
                true
            }
//            R.id. -> {
//                val addStoryActivity = Intent(this@MainActivity, AddStoryActivity::class.java)
//                startActivity(addStoryActivity)
//                showStory(mainViewModel)
//                finish()
//                true
//            }
            else -> false
        }
    }

    private fun getStory() {
        val token = getToken()
        val adapterr = AdapterStory()
        if (token != null) {
            binding.rvStory.adapter = adapterr.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapterr.retry()
                }
            )
            mainViewModel.story("Bearer $token").observe(this) {
                adapterr.submitData(lifecycle, it)
            }
        }
    }

    private fun getToken(): String? {
        return mainViewModel.getPreference(this).value
    }
}