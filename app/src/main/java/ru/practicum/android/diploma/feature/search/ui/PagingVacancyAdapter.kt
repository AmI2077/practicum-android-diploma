package ru.practicum.android.diploma.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.databinding.ItemLoadingFooterBinding
import ru.practicum.android.diploma.databinding.ItemVacancyBinding

class PagingVacancyAdapter(
    private val onItemClick: (VacancyCard) -> Unit
) : PagingDataAdapter<VacancyCard, VacancyAdapter.VacancyViewHolder>(VacancyDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacancyAdapter.VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacancyAdapter.VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VacancyAdapter.VacancyViewHolder,
        position: Int
    ) {
        val vacancy = getItem(position)
        if (vacancy != null) {
            holder.bind(vacancy)
            holder.itemView.setOnClickListener {
                onItemClick(vacancy)
            }
        }
    }

    companion object VacancyDiffCallback : DiffUtil.ItemCallback<VacancyCard>() {
        override fun areItemsTheSame(oldItem: VacancyCard, newItem: VacancyCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VacancyCard, newItem: VacancyCard): Boolean {
            return oldItem == newItem
        }
    }
}

class VacancyLoadStateAdapter : LoadStateAdapter<VacancyLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadingFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: ItemLoadingFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.root.isVisible = loadState is LoadState.Loading
        }
    }
}
