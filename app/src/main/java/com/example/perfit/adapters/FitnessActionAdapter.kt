package com.example.perfit.adapters

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.perfit.databinding.FitnessActionsRowLayoutBinding
import com.example.perfit.models.FitnessActions

class FitnessActionAdapter(private val actions: List<FitnessActions>, private val videoView: VideoView,
                           private val dialog: AlertDialog, private val activity: Activity)
    : RecyclerView.Adapter<FitnessActionAdapter.FitnessActionViewHolder>() {

    inner class FitnessActionViewHolder(private val binding: FitnessActionsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(action: FitnessActions) {
            binding.textActionContent.text = action.name
            binding.textDifficultyContent.text = action.difficulty
            binding.textDescriptionContent.text = action.description
            binding.buttonViewDemo.setOnClickListener {
                videoView.apply {
                    setVideoURI(action.video.toUri())
                    start()
                }
                dialog.show()
            }
            binding.buttonSelect.setOnClickListener {
                val intent = Intent().apply {
                    putExtra("Mode", action)
                }
                activity.setResult(RESULT_OK, intent)
                activity.finish()
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FitnessActionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FitnessActionsRowLayoutBinding.inflate(layoutInflater, parent, false)
        return FitnessActionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FitnessActionViewHolder, position: Int) {
        val currentAction = actions[position]
        holder.bind(currentAction)
    }

    override fun getItemCount(): Int {
        return actions.size
    }
}