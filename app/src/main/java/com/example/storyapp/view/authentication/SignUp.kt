package com.example.storyapp.view.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivitySignUpBinding



class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        signUpViewModel = SignUpViewModel()
        binding.signupButton.setOnClickListener {
            signUp()
        }
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun signUp() {

        val name = binding.nameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        when {
            name.isEmpty() -> {
                binding.nameEditTextLayout.error = getString(R.string.name_command)
            }
            email.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.email_command)
            }
            password.isEmpty() -> {
                binding.passwordEditTextLayout.error = getString(R.string.password_command)
            }
            else -> {
                signUpViewModel.signUp(name, password, email)
                signUpViewModel.message.observe(this){
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
                signUpViewModel.isSuccess.observe(this){
                    showLoading(it)
                }

            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.textHeadline, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f)
                .setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val signup =
            ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val sectionOne = AnimatorSet().apply {
            playTogether(
                nameTextView,
                nameEditTextLayout,
            )
        }
        val sectionTwo = AnimatorSet().apply {
            playTogether(
                emailTextView,
                emailEditTextLayout,
            )
        }
        val sectionThree = AnimatorSet().apply {
            playTogether(
                passwordTextView,
                passwordEditTextLayout,
            )
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                sectionOne,
                sectionTwo,
                sectionThree,
                signup
            )
            startDelay = 500
        }.start()
    }
    private fun showLoading(bol: Boolean){
        if(bol){
            binding.progressBarSignUp.visibility = View.GONE

        }else{
            binding.progressBarSignUp.visibility = View.VISIBLE
        }
    }

}
