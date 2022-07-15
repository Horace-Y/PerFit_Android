package com.example.perfit.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfit.adapters.FitnessActionAdapter
import com.example.perfit.databinding.ActivityActionSelectionBinding
import com.example.perfit.utils.Constants.Companion.FITNESS_ACTIONS

class FitnessActionSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActionSelectionBinding
    private lateinit var actionAdapter: FitnessActionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back arrow
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set up recycler view and its adapter
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(){
        actionAdapter = FitnessActionAdapter(FITNESS_ACTIONS, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = actionAdapter
    }
}