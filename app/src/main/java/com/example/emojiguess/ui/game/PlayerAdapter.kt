package com.example.emojiguess.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojiguess.R
import com.example.emojiguess.databinding.ItemPlayerBinding

class PlayerAdapter : ListAdapter<PlayerUiModel, PlayerAdapter.PlayerViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayerBinding.inflate(inflater, parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlayerViewHolder(private val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: PlayerUiModel) {
            binding.textPlayerName.text = player.displayName
            binding.textPlayerEmoji.text = player.visibleEmoji
            binding.textPlayerStatus.text = when {
                player.isEliminated -> binding.root.context.getString(R.string.status_eliminated)
                player.isCurrentTurn -> binding.root.context.getString(R.string.status_playing)
                else -> binding.root.context.getString(R.string.status_waiting)
            }

            val context = binding.root.context
            val strokeColor = when {
                player.isEliminated -> ContextCompat.getColor(context, R.color.playerEliminated)
                player.isCurrentTurn -> ContextCompat.getColor(context, R.color.playerActive)
                else -> ContextCompat.getColor(context, R.color.playerIdle)
            }
            binding.cardPlayer.strokeColor = strokeColor
            binding.cardPlayer.alpha = if (player.isEliminated) 0.6f else 1f
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<PlayerUiModel>() {
        override fun areItemsTheSame(oldItem: PlayerUiModel, newItem: PlayerUiModel): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PlayerUiModel, newItem: PlayerUiModel): Boolean = oldItem == newItem
    }
}