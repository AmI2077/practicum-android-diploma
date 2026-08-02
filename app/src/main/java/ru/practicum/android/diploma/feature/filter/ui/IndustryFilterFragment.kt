package ru.practicum.android.diploma.feature.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.databinding.FragmentIndustryFilterBinding
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryState
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryViewModel
import ru.practicum.android.diploma.feature.search.ui.viewmodel.SearchViewModelWithPaging

class IndustryFilterFragment : Fragment() {

    private var _binding: FragmentIndustryFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IndustryViewModel by viewModel()
    private val filterViewModel: SearchViewModelWithPaging by koinNavGraphViewModel(R.id.search_screen_tab)

    private var adapter: IndustryAdapter? = null
    private var selectedIndustry: FilterIndustry? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupSelectButton()
        setupRecyclerView()
        observeState()
        setupSearch()

        selectedIndustry = filterViewModel.filterState.value?.industry
        adapter?.setSelectedIndustryId(selectedIndustry?.id)
        updateSelectButtonVisibility()
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSelectButton() {
        binding.selectButton.setOnClickListener {
            closeFragment()
        }
    }

    private fun closeFragment() {
        if (selectedIndustry != null) {
            filterViewModel.saveIndustry(selectedIndustry)
        }
        findNavController().navigateUp()
    }

    private fun setupRecyclerView() {
        adapter = IndustryAdapter { industry ->
            handleIndustryClick(industry)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@IndustryFilterFragment.adapter
        }
    }

    private fun handleIndustryClick(industry: FilterIndustry) {
        selectedIndustry =
            if (selectedIndustry == industry) {
                null
            } else {
                industry
            }
        adapter?.setSelectedIndustryId(selectedIndustry?.id)
        updateSelectButtonVisibility()
    }

    private fun observeState() {
        viewModel.state.observe(
            viewLifecycleOwner
        ) { state ->

            when (state) {
                IndustryState.Loading -> {
                    showLoading()
                }

                is IndustryState.Content -> {
                    showIndustries()

                    adapter?.submitList(state.industries)
                    adapter?.setSelectedIndustryId(selectedIndustry?.id)
                }

                IndustryState.Error -> {
                    showError()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearch() {
        binding.searchEditText.doAfterTextChanged { editable ->
            val text = editable?.toString().orEmpty()

            viewModel.searchIndustry(text)

            binding.clearButton.isVisible = text.isNotBlank()
            binding.searchButton.isVisible = text.isBlank()
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
        }
    }

    private fun updateSelectButtonVisibility() {
        binding.buttonsContainer.isVisible = selectedIndustry != null
    }

    private fun showLoading() {
        binding.recyclerView.isVisible = false
        binding.errorNoIndustries.isVisible = false
    }

    private fun showIndustries() {
        binding.recyclerView.isVisible = true
        binding.errorNoIndustries.isVisible = false
    }

    private fun showError() {
        binding.recyclerView.isVisible = false
        binding.errorNoIndustries.isVisible = true
        binding.buttonsContainer.isVisible = false
    }
}
