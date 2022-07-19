package com.example.storyapp.view.authentication
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivitySignInBinding
import com.example.storyapp.datastore.UserPreference
import com.example.storyapp.model.*
import com.example.storyapp.view.main.MainActivity


class SignIn : AppCompatActivity() {
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var binding: ActivitySignInBinding
    private lateinit var user: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        binding.loginButton.setOnClickListener {
            signIn()
        }
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
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

    private fun setupViewModel() {
        signInViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(applicationContext))
        )[SignInViewModel::class.java]

        signInViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun signIn() {
        val email = binding.emailEditText2?.text.toString()
        val password = binding.passwordEditText2?.text.toString()
        when {
            email.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.email_signin)
            }
            password.isEmpty() -> {
                binding.passwordEditTextLayout.error = getString(R.string.password_signin)
            }
            else -> {
                signInViewModel.signIn(email, password)
                signInViewModel.message.observe(this){
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
                signInViewModel.isSuccess.observe(this){
                    showLoading(it)

                }
                signInViewModel.loginResult.observe(this){
                    if(it.token.isNotEmpty()) {
                        val intent = Intent(this@SignIn, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        signInViewModel.saveUser(it)
                    }
                }
            }
        }
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.textHeadline, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signUp = ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 1f).setDuration(500)
        val additionalText =
            ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val sectionOne = AnimatorSet().apply {
            playTogether(
                title,
                message,
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
        val sectionFour = AnimatorSet().apply {
            playTogether(
                login,
                additionalText,
                signUp
            )
        }
        AnimatorSet().apply {
            playSequentially(
                sectionOne,
                sectionTwo,
                sectionThree,
                sectionFour
            )
            startDelay = 500
        }.start()

    }
    private fun showLoading(bol: Boolean){
        if(bol){
            binding.progressBarSignIn.visibility = View.GONE

        }else{
            binding.progressBarSignIn.visibility = View.VISIBLE
        }
    }

}

