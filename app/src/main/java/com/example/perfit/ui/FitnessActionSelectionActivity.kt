package com.example.perfit.ui

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfit.R
import com.example.perfit.adapters.FitnessActionAdapter
import com.example.perfit.databinding.ActivityActionSelectionBinding
import com.example.perfit.models.FitnessActions
import com.example.perfit.utils.Constants.Companion.ACTION1
import com.example.perfit.utils.Constants.Companion.ACTION2
import com.example.perfit.utils.Constants.Companion.ACTION3
import com.example.perfit.utils.Constants.Companion.ACTION4
import com.example.perfit.utils.Constants.Companion.ACTION5
import com.example.perfit.utils.Constants.Companion.ACTION6
import com.example.perfit.utils.Constants.Companion.DESC1
import com.example.perfit.utils.Constants.Companion.EASY
import com.example.perfit.utils.Constants.Companion.HARD
import com.example.perfit.utils.Constants.Companion.MEDIUM

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
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.basketball)
        val actions = listOf(FitnessActions(ACTION1, EASY, DESC1, uri),
            FitnessActions(ACTION2, MEDIUM, DESC1, uri),
            FitnessActions(ACTION3, HARD, DESC1, uri),
            FitnessActions(ACTION4, EASY, DESC1, uri),
            FitnessActions(ACTION5, MEDIUM, DESC1, uri),
            FitnessActions(ACTION6, HARD, DESC1, uri))
        actionAdapter = FitnessActionAdapter(actions, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = actionAdapter
    }
}