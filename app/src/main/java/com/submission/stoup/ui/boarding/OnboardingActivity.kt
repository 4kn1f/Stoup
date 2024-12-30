package com.submission.stoup.ui.boarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submission.stoup.R
import com.submission.stoup.databinding.ActivityOnboardingBinding
import com.submission.stoup.ui.login.LoginActivity
import com.submission.stoup.ui.signup.RegisterActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setNavigation()
    }

    private fun setNavigation() {
        binding.apply {
            btnMasuk.setOnClickListener{
                startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            }
            btnDaftar.setOnClickListener{
                startActivity(Intent(this@OnboardingActivity, RegisterActivity::class.java))
            }
        }
    }
}