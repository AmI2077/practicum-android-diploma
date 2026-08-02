package ru.practicum.android.diploma.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.extensions.loadCompanyLogo
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.core.models.card.toSalary
import ru.practicum.android.diploma.databinding.ItemLoadingFooterBinding
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.feature.detail.utils.VacancyDetailFragmentFormatters

class VacancyAdapter(
    private val onItemClick: (VacancyCard) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_VACANCY = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var items: List<VacancyCard> = emptyList()
    private var showLoadingFooter = false

    fun submitList(newItems: List<VacancyCard>) {
        val diffCallback = VacancyDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && showLoadingFooter) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_VACANCY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_VACANCY -> {
                val binding = ItemVacancyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VacancyViewHolder(binding)
            }

            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Неизвестный тип view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VacancyViewHolder) {
            val vacancy = items[position]
            holder.bind(vacancy)
            holder.itemView.setOnClickListener {
                onItemClick(vacancy)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size + if (showLoadingFooter) 1 else 0
    }

    class VacancyViewHolder(
        private val binding: ItemVacancyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: VacancyCard) {
            binding.apply {
                vacancyTitle.text = formatTitle(
                    name = vacancy.name,
                    city = vacancy.city
                )

                companyName.text = vacancy.company
                salary.text = VacancyDetailFragmentFormatters(binding.root.context)
                    .formatSalary(vacancy.salary?.toSalary())
                companyLogo.loadCompanyLogo(
                    logoUrl = vacancy.logo,
                    cornerRadius = root.context.resources.getDimension(R.dimen.corner_radius).toInt()
                )
            }
        }

        private fun formatTitle(name: String, city: String?): String {
            return if (!city.isNullOrEmpty()) {
                "$name, $city"
            } else {
                name
            }
        }
    }

    class LoadingViewHolder(
        binding: ItemLoadingFooterBinding
    ) : RecyclerView.ViewHolder(binding.root)

    class VacancyDiffCallback(
        private val oldList: List<VacancyCard>,
        private val newList: List<VacancyCard>
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
