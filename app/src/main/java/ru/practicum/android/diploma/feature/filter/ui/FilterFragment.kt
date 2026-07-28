package ru.practicum.android.diploma.feature.filter.ui

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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
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
            text?.let {
                viewModel.saveSalary(text.toString())
            }
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

    private fun observeState() {
        viewModel.filterState.observe(viewLifecycleOwner) { state ->
            binding.salaryCheckBox.setImageResource(
                if (state.hideWithoutSalary)
                    R.drawable.ic_check_box_on_24
                else
                    R.drawable.ic_check_box_off_24
            )
            val newSalary = state.salary
            val currentInput = binding.salaryEdit.text.toString()

            val targetText = if (newSalary == 0 || newSalary == null) "" else newSalary.toString()

            if (currentInput != targetText) {
                binding.salaryEdit.setText(targetText)
            }

            val industry = state.industry

            if (industry == null) {
                binding.industryHint.apply {
                    text = getString(R.string.industry_hint)
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.gray))
                }
            } else {
                binding.industryHint.apply {
                    text = industry.name
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
