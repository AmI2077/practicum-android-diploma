package ru.practicum.android.diploma.feature.detail.ui

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.Parser.models.FormattedDescription

class DescriptionAdapter : RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder>() {

    private var items: List<FormattedDescription> = emptyList()

    fun submitList(newItems: List<FormattedDescription>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_description, parent, false)
        return DescriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class DescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.description_text)

        fun bind(item: FormattedDescription) {
            when (item) {
                is FormattedDescription.Heading2 -> {
                    textView.text = item.text
                    textView.setTextAppearance(textView.context, R.style.MediumTextStyle_22)
                    textView.setPadding(0, 0, 0, dpToPx(16f))
                }
                is FormattedDescription.Heading3 -> {
                    textView.text = item.text
                    textView.setTextAppearance(textView.context, R.style.MediumTextStyle_16)
                    textView.setPadding(0, 0, 0, dpToPx(4f))
                }
                is FormattedDescription.Paragraph -> {
                    textView.text = item.text
                    textView.setTextAppearance(textView.context, R.style.RegularTextStyle_16)
                    textView.setPadding(0, 0, 0, dpToPx(16f))
                }
                is FormattedDescription.BulletList -> {
                    val spannable = SpannableStringBuilder()
                    val firstLineIndent = dpToPx(4f)
                    val restLinesIndent = dpToPx(19f)
                    for ((index, itemText) in item.items.withIndex()) {
                        if (index > 0) {
                            spannable.append("\n")
                        }
                        val bulletText = "  •  $itemText"
                        val start = spannable.length
                        spannable.append(bulletText)
                        val end = spannable.length
                        spannable.setSpan(
                            LeadingMarginSpan.Standard(
                                firstLineIndent,
                                restLinesIndent
                            ),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    textView.text = spannable
                    textView.setTextAppearance(textView.context, R.style.RegularTextStyle_16)
                    textView.setPadding(0, 0, 0, dpToPx(16f))
                }
                is FormattedDescription.SimpleText -> {
                    textView.text = item.text
                    textView.setTextAppearance(textView.context, R.style.RegularTextStyle_16)
                    textView.setPadding(0, 0, 0, dpToPx(8f))
                }
            }
        }

        private fun dpToPx(dp: Float): Int {
            return Math.round(dp * itemView.context.resources.displayMetrics.density)
        }
    }
}
