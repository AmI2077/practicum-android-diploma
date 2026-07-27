package ru.practicum.android.diploma.feature.filter.ui

import android.os.Build
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
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.FilterState
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.FilterViewModel

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilterViewModel by koinNavGraphViewModel(R.id.filter_screen)
    private var hideWithoutSalary = false

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

        getIndustryResult()

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

    private fun getIndustryResult() {
        parentFragmentManager.setFragmentResultListener(
            IndustryFilterFragment.INDUSTRY_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val industry = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(IndustryFilterFragment.INDUSTRY_KEY, FilterIndustry::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(IndustryFilterFragment.INDUSTRY_KEY)
            }
            viewModel.saveIndustry(industry)
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

    private fun updateSalaryCheckBox() {
        binding.salaryCheckBox.setImageResource(
            if (hideWithoutSalary)
                R.drawable.ic_check_box_on_24
            else
                R.drawable.ic_check_box_off_24
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
        viewModel.state.observe(
            viewLifecycleOwner
        ) { state ->
            hideWithoutSalary =
                state.hideWithoutSalary
            updateSalaryCheckBox()

            state.industry?.let {
                binding.industryHint.apply {
                    text = it.name
                    setTextColor(resources.getColor(R.color.black))
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
