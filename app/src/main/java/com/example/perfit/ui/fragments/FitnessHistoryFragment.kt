package com.example.perfit.ui.fragments

import android.app.AlertDialog
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
import com.example.perfit.databinding.FragmentVideoDialogBinding
import com.example.perfit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FitnessHistoryFragment : Fragment(){

    private var _binding: FragmentFitnessHistoryBinding? = null
    private val binding get() = _binding!!
    private var _videoDialogBinding: FragmentVideoDialogBinding? = null
    private val videoDialogBinding get() = _videoDialogBinding!!
    private lateinit var videoDialog: AlertDialog
    private lateinit var mainViewModel: MainViewModel
    private lateinit var historyAdapter : FitnessHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFitnessHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        _videoDialogBinding = FragmentVideoDialogBinding.inflate(inflater, container, false)

        // load local fitness results
        lifecycleScope.launchWhenStarted {
            readDatabase()
        }

        // video dialog
        videoDialog = AlertDialog.Builder(context).apply {
            setView(videoDialogBinding.root)
        }.create()

        // set up recycler view and its adapter
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView(){
        historyAdapter = FitnessHistoryAdapter(videoDialogBinding.videoDemo, videoDialog)
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