package com.example.perfit.ui

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.perfit.databinding.ActivityFitnessResultBinding

class FitnessResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFitnessResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFitnessResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back arrow
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // display feedback score and video
        binding.textScore.text = intent.getStringExtra("score")
        val mediaController = MediaController(this).apply {
            setAnchorView(binding.videoFeedback)
        }
        binding.videoFeedback.apply {
            setVideoURI(Uri.parse(intent.getStringExtra("video")))
            setMediaController(mediaController)
            start()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }
}