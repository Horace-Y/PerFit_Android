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

        // total score and breakdowns
        binding.textScore.text = intent.getStringExtra("total")
        val subscores = (intent.getSerializableExtra("scores") as HashMap<String, String>).toList()
        binding.textSubscoreTitle1.text = subscores[0].first
        binding.textSubscore1.text = subscores[0].second
        binding.textSubscoreTitle2.text = subscores[1].first
        binding.textSubscore2.text = subscores[1].second
        binding.textSubscoreTitle3.text = subscores[2].first
        binding.textSubscore3.text = subscores[2].second

        // feedback video
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