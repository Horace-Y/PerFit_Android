package com.example.perfit.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfit.adapters.FitnessActionAdapter
import com.example.perfit.databinding.ActivityActionSelectionBinding
import com.example.perfit.databinding.FragmentVideoDialogBinding
import com.example.perfit.utils.Constants.Companion.FITNESS_ACTIONS

class FitnessActionSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActionSelectionBinding
    private lateinit var videoDialogBinding: FragmentVideoDialogBinding
    private lateinit var actionAdapter: FitnessActionAdapter
    private lateinit var videoDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionSelectionBinding.inflate(layoutInflater)
        videoDialogBinding = FragmentVideoDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back arrow
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // video dialog
        videoDialog = AlertDialog.Builder(this).apply {
            setView(videoDialogBinding.root)
        }.create()

        // set up recycler view and its adapter
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(){
        actionAdapter = FitnessActionAdapter(FITNESS_ACTIONS, videoDialogBinding.videoDemo, videoDialog, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = actionAdapter
    }
}