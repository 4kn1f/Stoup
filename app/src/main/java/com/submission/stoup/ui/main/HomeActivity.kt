package com.submission.stoup.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.stoup.data.remote.pref.UserModel
import com.submission.stoup.data.remote.response.Story
import com.submission.stoup.databinding.ActivityHomeBinding
import com.submission.stoup.ui.adapter.StoryAdapter
import com.submission.stoup.ui.viewmodelfactory.ViewModelFactory

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
