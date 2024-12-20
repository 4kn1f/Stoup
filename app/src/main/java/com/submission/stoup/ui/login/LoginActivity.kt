package com.submission.stoup.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submission.stoup.R
import com.submission.stoup.databinding.ActivityLoginBinding
import com.submission.stoup.ui.main.MainActivity
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

        setupNavigation()
        observeViewModel()
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this){ result ->
            if (result != null) {
                val token = result.loginResult?.token
                if (!token.isNullOrEmpty()) {
                    Toast.makeText(this, "Login successful! Token: $token", Toast.LENGTH_SHORT).show()
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
        binding.btnLogin.setOnClickListener{
            val email = binding.edEmail.text.toString().trim()
            val pw = binding.edPassword.text.toString().trim()

            if (email.isNotEmpty() && pw.isNotEmpty()){
                loginViewModel.login(email, pw)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}