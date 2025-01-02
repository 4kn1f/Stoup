package com.submission.stoup.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.submission.stoup.R
import com.submission.stoup.databinding.ActivityDetailStoryBinding
import com.submission.stoup.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idStory = intent.getStringExtra(EXTRA_ID_STORY)
        if (idStory != null) {
            lifecycleScope.launch {
                viewModel.getStoryDetails(idStory)
            }
        } else {
            Toast.makeText(this, "ID cerita tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.detailStory.observe(this) { detailStory ->
            detailStory?.let {
                binding.tvTitleStory.text = it.story?.name
                binding.tvDescStory.text = it.story?.description

                Glide.with(this)
                    .load(it.story?.photoUrl)
                    .apply(RequestOptions().transform(RoundedCorners(14)))
                    .into(binding.imgStory)
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                showErrorMessage(it)
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID_STORY = "EXTRA_ID_STORY"

        fun start(context: Context, storyId: String) {
            val intent = Intent(context, DetailStoryActivity::class.java).apply {
                putExtra(EXTRA_ID_STORY, storyId)
            }
            context.startActivity(intent)
        }
    }
}