package ru.practicum.android.diploma.feature.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.databinding.ItemIndustryBinding

class IndustryAdapter(
    private val onItemClick: (FilterIndustry) -> Unit
) : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {

    private var items: List<FilterIndustry> = emptyList()
    private var selectedIndustryId: Int? = null

    fun submitList(newItems: List<FilterIndustry>) {
        val diffCallback = IndustryDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun setSelectedIndustryId(industryId: Int?) {
        val oldPosition = findPositionById(selectedIndustryId)
        selectedIndustryId = industryId
        val newPosition = findPositionById(industryId)
        notifyPositionsChanged(oldPosition, newPosition)
    }

    private fun findPositionById(id: Int?): Int {
        return if (id != null) {
            items.indexOfFirst { it.id == id }
        } else {
            -1
        }
    }

    private fun notifyPositionsChanged(oldPosition: Int, newPosition: Int) {
        when {
            oldPosition == -1 && newPosition != -1 -> notifyItemChanged(newPosition)
            oldPosition != -1 && newPosition == -1 -> notifyItemChanged(oldPosition)
            oldPosition != -1 && newPosition != -1 -> {
                if (oldPosition != newPosition) {
                    notifyItemChanged(oldPosition)
                    notifyItemChanged(newPosition)
                } else {
                    notifyItemChanged(oldPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(items[position], selectedIndustryId)
    }

    override fun getItemCount(): Int = items.size

    class IndustryViewHolder(
        private val binding: ItemIndustryBinding,
        private val onItemClick: (FilterIndustry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(industry: FilterIndustry, selectedIndustryId: Int?) {
            binding.apply {
                industryName.text = industry.name

                val isSelected = industry.id == selectedIndustryId
                industryIconCheck.setImageResource(
                    if (isSelected) {
                        R.drawable.ic_radio_button_on_24
                    } else {
                        R.drawable.ic_radio_button_off_24
                    }
                )

                itemView.setOnClickListener {
                    onItemClick(industry)
                }
            }
        }
    }

    class IndustryDiffCallback(
        private val oldList: List<FilterIndustry>,
        private val newList: List<FilterIndustry>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos].id == newList[newPos].id
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos] == newList[newPos]
        }
    }
}
