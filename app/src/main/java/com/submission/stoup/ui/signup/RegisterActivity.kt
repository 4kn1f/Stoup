package com.submission.stoup.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.submission.stoup.databinding.ActivityRegisterBinding
import com.submission.stoup.ui.login.LoginActivity
import com.submission.stoup.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        observeViewModel()
    }

    private fun observeViewModel() {
        registerViewModel.registerResult.observe(this) { result ->
            if (result != null) {
                AlertDialog.Builder(this).apply {
                    setTitle("Horray!")
                    setMessage("You've successfully created an account ! Login now")
                    setPositiveButton("Next") { _, _ ->
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSignup.isEnabled = !isLoading
        }
    }

    private fun setupNavigation() {
        binding.apply {
            btnSignup.setOnClickListener{
                val name = binding.edUsername.text.toString().trim()
                val email = binding.edEmail.text.toString().trim()
                val pw = binding.edPassword.text.toString().trim()

                if(email.isNotEmpty() && name.isNotEmpty() && pw.isNotEmpty()){
                    lifecycleScope.launch {
                        registerViewModel.register(name, email, pw)
                    }
                }
            }

            tvHaveAcc.setOnClickListener{
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}