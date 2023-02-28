package com.example.perfit.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.perfit.databinding.FitnessHistoriesRowLayoutBinding
import com.example.perfit.models.FitnessResult
import com.example.perfit.utils.FitnessResultsDiffUtil

class FitnessHistoryAdapter(private val videoView: VideoView, private val dialog: AlertDialog) :
    RecyclerView.Adapter<FitnessHistoryAdapter.FitnessHistoryViewHolder>() {

    private var results = listOf<FitnessResult>()

    inner class FitnessHistoryViewHolder(private val binding: FitnessHistoriesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: FitnessResult) {
            binding.textDateContent.text = result.dateTime
            binding.textActionContent.text = result.action
            binding.textScoreContent.text = result.total.toString()
            val subscores = result.scores.toList()
            binding.textSubscoreTitle1.text = subscores[0].first
            binding.textSubscore1.text = subscores[0].second
            binding.textSubscoreTitle2.text = subscores[1].first
            binding.textSubscore2.text = subscores[1].second
            binding.textSubscoreTitle3.text = subscores[2].first
            binding.textSubscore3.text = subscores[2].second
            binding.buttonViewFeedback.setOnClickListener {
                videoView.apply {
                    setVideoURI(result.feedbackVideo.toUri())
                    start()
                }
                dialog.show()
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
        val newResults = mutableListOf<FitnessResult>().apply {
            addAll(results)
            add(newData)
        }
        val recipesDiffUtil = FitnessResultsDiffUtil(results, newResults)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        results = newResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}