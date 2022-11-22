package com.example.perfit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfit.adapters.FitnessHistoryAdapter
import com.example.perfit.databinding.FragmentFitnessHistoryBinding
import com.example.perfit.utils.Constants
import com.example.perfit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FitnessHistoryFragment : Fragment(){

    private var _binding: FragmentFitnessHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var historyAdapter: FitnessHistoryAdapter

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

        return binding.root
    }

    private fun setupRecyclerView(){
        historyAdapter = FitnessHistoryAdapter(Constants.FITNESS_RESULTS)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = historyAdapter
    }
}