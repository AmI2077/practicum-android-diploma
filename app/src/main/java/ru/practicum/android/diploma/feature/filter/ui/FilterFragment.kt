package ru.practicum.android.diploma.feature.filter.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import androidx.core.view.isVisible
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.FilterState
import ru.practicum.android.diploma.feature.search.ui.viewmodel.SearchViewModelWithPaging

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModelWithPaging by koinNavGraphViewModel(R.id.search_screen_tab)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWorkPlaceButton()
        setupIndustryButton()
        setupBackButton()
        observeState()

        setupEditText()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.hideWithoutSalaryContainer.setOnClickListener {
            val checked = viewModel.filterState.value?.hideWithoutSalary ?: false

            viewModel.saveHideWithoutSalary(!checked)
        }

        binding.mainContainer.setOnClickListener {
            clearSalaryFocus()
        }

        binding.resetButton.setOnClickListener {
            viewModel.clearFilter()
        }

        binding.applyButton.setOnClickListener {
            viewModel.applyFilters()
            findNavController().navigateUp()
        }
    }

    private fun setupEditText() {
        binding.salaryEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.salaryLabel.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.blue)
                )
            } else {
                binding.salaryLabel.setTextColorAttr(android.R.attr.textColorHint)
            }
        }

        binding.salaryEdit.doOnTextChanged { text, _, _, _ ->
            binding.clearButton.isVisible =
                !text.isNullOrBlank()

            if (binding.salaryEdit.hasFocus()) {
                viewModel.saveSalary(text?.toString())
            }
        }

        binding.clearButton.setOnClickListener {
            binding.salaryEdit.text?.clear()
            viewModel.saveSalary(null)
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

    private fun hideKeyboard() {
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

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupWorkPlaceButton() {
        binding.workPlaceContainer.setOnClickListener {
            findNavController().navigate(
                FilterFragmentDirections.actionFilterScreenToWorkPlaceFilter()
            )
        }
    }

    private fun setupIndustryButton() {
        binding.industryContainer.setOnClickListener {
            findNavController().navigate(
                FilterFragmentDirections.actionFilterScreenToIndustryFilter()
            )
        }
    }

    private fun updateButtonsVisibility(state: FilterState) {
        val hasFilters =
            state.salary != null ||
                state.hideWithoutSalary ||
                state.industry != null

        binding.buttonsContainer.isVisible = hasFilters
    }

    private fun observeState() {
        viewModel.filterState.observe(viewLifecycleOwner) { state ->
            updateButtonsVisibility(state)
            binding.salaryCheckBox.setImageResource(
                if (state.hideWithoutSalary) {
                    R.drawable.ic_check_box_on_24
                } else {
                    R.drawable.ic_check_box_off_24
                }
            )
            val newSalary = state.salary
            val currentInput = binding.salaryEdit.text.toString()

            val targetText = newSalary?.toString().orEmpty()

            if (currentInput != targetText) {
                binding.salaryEdit.setText(targetText)
            }

            val industry = state.industry

            if (industry == null) {
                binding.industryHint.apply {
                    text = getString(R.string.industry_hint)
                    binding.IndustryIcon.setImageResource(R.drawable.ic_arrow_forward_24)
                    binding.IndustryIcon.setOnClickListener(null)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                }
            } else {
                binding.industryHint.apply {
                    text = industry.name
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    binding.IndustryIcon.setImageResource(R.drawable.ic_search_clean_24)
                    binding.IndustryIcon.setOnClickListener {
                        viewModel.saveIndustry(null)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
