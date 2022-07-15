package com.example.perfit.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.util.Log
import android.view.MenuItem
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perfit.R
import com.example.perfit.databinding.ActivityFitnessResultBinding
import com.example.perfit.models.FitnessResult
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FitnessResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFitnessResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFitnessResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back arrow
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // display fitness result
        val outputFile = handleResultIntent()

        // video playback
        val mediaController = MediaController(this).apply {
            setAnchorView(binding.videoFeedback)
        }
        binding.videoFeedback.apply {
            setVideoURI(Uri.parse(outputFile.toString()))
            setMediaController(mediaController)
            start()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun handleResultIntent(): FileOutputStream{
        val fitnessResult: FitnessResult? = intent.getParcelableExtra("FitnessResults")
        if (fitnessResult == null){
            Toast.makeText(this, "Server data cannot be processed", Toast.LENGTH_SHORT).show()
            finish()
        }
        Log.d("Network", "score: " + fitnessResult!!.score)
        Log.d("Network", "video: " + fitnessResult!!.video)
        binding.textScore.text = fitnessResult!!.score.toString()

        val videoBytes = decode(fitnessResult.video, DEFAULT)
        val outputFile = FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + R.string.app_name
                + SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".mp4").apply {
            write(videoBytes)
            close()
        }

        return outputFile
    }
}