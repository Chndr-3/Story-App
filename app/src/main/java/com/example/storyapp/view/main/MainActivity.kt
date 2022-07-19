package com.example.storyapp.view.main



import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.datastore.UserPreference
import com.example.storyapp.view.authentication.SignIn
import com.example.storyapp.view.map.MapsActivity
import com.example.storyapp.view.post.PostActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val mainSourceViewModel: MainSourceViewModel by viewModels {
        ViewModelSourceFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupView()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener {
            val intentToPost = Intent(this@MainActivity, PostActivity::class.java)
            startActivity(intentToPost)
        }

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
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(applicationContext))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->

            if (user.token.isNotEmpty()) {
                binding.nameTextView.text = getString(R.string.greeting, user.name)
                getData()
            } else {
                startActivity(Intent(this, SignIn::class.java))
                finish()
            }
        }
    }


    private fun getData() {
        val adapter = StoryListAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainSourceViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutButton -> mainViewModel.logout()
            R.id.mapButotn -> {
                val intentToMap = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intentToMap)
            }
            R.id.languangeButton -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}