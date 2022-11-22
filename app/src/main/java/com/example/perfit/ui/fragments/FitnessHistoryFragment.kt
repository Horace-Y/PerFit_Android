package com.example.perfit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfit.adapters.FitnessHistoryAdapter
import com.example.perfit.databinding.FragmentFitnessHistoryBinding
import com.example.perfit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FitnessHistoryFragment : Fragment(){

    private var _binding: FragmentFitnessHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val historyAdapter by lazy { FitnessHistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFitnessHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        // set up recycler view and its adapter
        setupRecyclerView()

        // load local fitness results
        lifecycleScope.launchWhenStarted {
            readDatabase()
        }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.recyclerView.adapter = historyAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun readDatabase(){
        lifecycleScope.launch {
            mainViewModel.readResults.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    historyAdapter.setData(database[0].fitnessResult)
                }
            }
        }
    }
}