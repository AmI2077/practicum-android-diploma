package ru.practicum.android.diploma.feature.search.ui

import android.os.Bundle
import android.util.TypedValue
import ru.practicum.android.diploma.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private var hideWithoutSalary = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.salaryEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.salaryLabel.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.blue)
                )
            } else {
                binding.salaryLabel.setTextColorAttr(android.R.attr.textColorHint)
            }
        }

        binding.hideWithoutSalaryContainer.setOnClickListener {
            hideWithoutSalary = !hideWithoutSalary
            updateSalaryCheckBox()
        }

        binding.mainContainer.setOnClickListener {
            clearSalaryFocus()
        }
    }

    fun TextView.setTextColorAttr(@AttrRes attrRes: Int) {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        setTextColor(typedValue.data)
    }

    private fun clearSalaryFocus() {
        binding.salaryEdit.clearFocus()
        hideKeyboard()
    }

    private fun hideKeyboard(){
        val imm =
            requireContext()
                .getSystemService(
                    InputMethodManager::class.java
                )
        imm?.hideSoftInputFromWindow(
            binding.salaryEdit.windowToken,
            0
        )
    }

    private fun updateSalaryCheckBox() {
        binding.salaryCheckBox.setImageResource(
            if (hideWithoutSalary)
                R.drawable.ic_check_box_on_24
            else
                R.drawable.ic_check_box_off_24
        )
    }

    //заделка на будущее, поведение иконки и цвета текста
    private fun updateIndustryTextIcon(workPlace: String?) {

        if (workPlace == null) {
            binding.industryHint.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.gray)
            )
            binding.industryHint.text = getString(R.string.industry_hint)
            binding.IndustryIcon.setImageResource(R.drawable.ic_arrow_forward_24)
        } else {
            binding.industryHint.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            binding.industryHint.text = workPlace
            binding.IndustryIcon.setImageResource(R.drawable.ic_search_clean_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
