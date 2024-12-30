package com.submission.stoup.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.submission.stoup.databinding.ActivityLoginBinding
import com.submission.stoup.ui.main.HomeActivity
import com.submission.stoup.ui.viewmodelfactory.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupNavigation()
        observeViewModel()
        playAnimation()
    }

    private fun playAnimation() {
        val animator = listOf(
            ObjectAnimator.ofFloat(binding.tvCaption, View.ALPHA, 0f, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.tvCaption2, View.ALPHA, 0f, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 0f, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 0f,1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f,1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(500),
        )

        AnimatorSet().apply {
            playSequentially(animator)
            startDelay = 50
            start()
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this) { result ->
            if (result != null) {
                val token = result.loginResult?.token
                if (!token.isNullOrEmpty()) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    Log.d("LoginActivity", "Login successful! Token: $token")

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Token is missing", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        loginViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigation() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString().trim()
            val pw = binding.edPassword.text.toString().trim()

            if (email.isNotEmpty() && pw.isNotEmpty()) {
                loginViewModel.login(email, pw)
            } else {
                Toast.makeText(this, "Please fill in first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
