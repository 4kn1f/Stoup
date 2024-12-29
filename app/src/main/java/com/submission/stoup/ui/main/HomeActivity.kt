package com.submission.stoup.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.stoup.R
import com.submission.stoup.data.remote.pref.UserModel
import com.submission.stoup.data.remote.pref.UserPreferences
import com.submission.stoup.data.remote.pref.dataStore
import com.submission.stoup.data.remote.response.Story
import com.submission.stoup.databinding.ActivityHomeBinding
import com.submission.stoup.ui.adapter.StoryAdapter
import com.submission.stoup.ui.boarding.OnboardingActivity
import com.submission.stoup.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var storyAdapter: StoryAdapter
    private var userSession: UserModel? = null

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = storyAdapter

        observeViewModel()
        homeViewModel.getAllStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                lifecycleScope.launch {
                    homeViewModel.logout()
                    UserPreferences.getInstance(this@HomeActivity.dataStore).logout()
                    Toast.makeText(this@HomeActivity, "Berhasil log out", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomeActivity, OnboardingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeViewModel() {

        homeViewModel.getSession().observe(this){ user ->
            userSession = user
        }

        homeViewModel.isLoading.observe(this){ isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.stories.observe(this){ result ->
            result.onSuccess { storiesResponse ->
                val storyList = storiesResponse?.listStory?.map { listStoryItem ->
                    Story(
                        id = listStoryItem?.id ?: "",
                        name = listStoryItem?.name ?: "",
                        description = listStoryItem?.description ?: "",
                        photoUrl = listStoryItem?.photoUrl ?: ""
                    )
                }
                storyAdapter.submitList(storyList)
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Failed to load stories: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
