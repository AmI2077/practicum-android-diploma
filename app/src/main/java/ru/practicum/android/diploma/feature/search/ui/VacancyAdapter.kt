package ru.practicum.android.diploma.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.VacancyCard
import ru.practicum.android.diploma.core.models.VacancyCardSalary
import ru.practicum.android.diploma.databinding.ItemVacancyBinding


class VacancyAdapter : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    private var items: List<VacancyCard> = emptyList()

    fun submitList(newItems: List<VacancyCard>) {
        val diffCallback = VacancyDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

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
                salary.text = formatSalary(vacancy.salary)
                loadCompanyLogo(vacancy.logo)
            }
        }

        private fun formatTitle(name: String, city: String?): String {
            return if (!city.isNullOrEmpty()) {
                "$name, $city"
            } else {
                name
            }
        }

        private fun formatSalary(salary: VacancyCardSalary?): String {
            if (salary == null) {
                return getString(R.string.salary_not_specified)
            }

            val from = salary.from
            val to = salary.to
            val currencySymbol = getCurrencySymbol(salary.currency)

            return when {
                from != null && to != null -> {
                    "${getString(R.string.salary_from)} ${formatNumber(from)} " +
                        "${getString(R.string.salary_to)} ${formatNumber(to)} $currencySymbol"
                }
                from != null -> {
                    "${getString(R.string.salary_from)} ${formatNumber(from)} $currencySymbol"
                }
                to != null -> {
                    "${getString(R.string.salary_to)} ${formatNumber(to)} $currencySymbol"
                }
                else -> {
                    getString(R.string.salary_not_specified)
                }
            }
        }

        private fun formatNumber(number: Int): String {
            return String.format("%,d", number).replace(',', ' ')
        }

        private fun getCurrencySymbol(currency: String?): String {
            return when (currency) {
                "RUR", "RUB" -> getString(R.string.currency_rub)
                "USD" -> getString(R.string.currency_usd)
                "EUR" -> getString(R.string.currency_eur)
                "KZT" -> getString(R.string.currency_kzt)
                "UAH" -> getString(R.string.currency_uah)
                "BYR" -> getString(R.string.currency_byr)
                "AZN" -> getString(R.string.currency_azn)
                "UZS" -> getString(R.string.currency_uzs)
                "GEL" -> getString(R.string.currency_gel)
                "KGS" -> getString(R.string.currency_kgs)
                else -> ""
            }
        }

        private fun loadCompanyLogo(logoUrl: String?) {
            val context = binding.root.context
            val cornerRadius = context.resources.getDimension(R.dimen.item_vacancy_logo_corner_radius).toInt()

            if (!logoUrl.isNullOrEmpty()) {
                Glide.with(context)
                    .load(logoUrl)
                    .placeholder(R.drawable.ic_placeholder_32)
                    .error(R.drawable.ic_placeholder_32)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(cornerRadius)
                    )
                    .into(binding.companyLogo)
            } else {
                binding.companyLogo.setImageResource(R.drawable.ic_placeholder_32)
            }
        }

        private fun getString(resId: Int): String {
            return binding.root.context.getString(resId)
        }
    }

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
