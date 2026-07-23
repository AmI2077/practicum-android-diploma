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

    fun submitList(newItems: List<FilterIndustry>) {
        val diffCallback = IndustryDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
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
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class IndustryViewHolder(
        private val binding: ItemIndustryBinding,
        private val onItemClick: (FilterIndustry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(industry: FilterIndustry) {
            binding.apply {
                binding.industryName.text = industry.name

                // Здесь в будущем будет логика для отображения выбранной отрасли
                // Пока просто показываем radio button off
                industryIconCheck.setImageResource(R.drawable.ic_radio_button_off_24)

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
