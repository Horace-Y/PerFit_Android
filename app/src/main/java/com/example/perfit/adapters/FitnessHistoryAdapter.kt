package com.example.perfit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.perfit.databinding.FitnessHistoriesRowLayoutBinding
import com.example.perfit.models.FitnessResult
import com.example.perfit.utils.FitnessResultsDiffUtil

class FitnessHistoryAdapter : RecyclerView.Adapter<FitnessHistoryAdapter.FitnessHistoryViewHolder>() {

    private var results = emptyList<FitnessResult>()

    inner class FitnessHistoryViewHolder(private val binding: FitnessHistoriesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: FitnessResult) {
            binding.textDateContent.text = result.dateTime
            binding.textActionContent.text = result.action.name
            binding.textScoreContent.text = result.score.toString()
            binding.buttonViewFeedback.setOnClickListener {
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FitnessHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FitnessHistoriesRowLayoutBinding.inflate(layoutInflater, parent, false)
        return FitnessHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FitnessHistoryViewHolder, position: Int) {
        val currentAction = results[position]
        holder.bind(currentAction)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun setData(newData: FitnessResult){
        val recipesDiffUtil = FitnessResultsDiffUtil(results, listOf(newData))
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        results = listOf(newData)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}