package ru.practicum.android.diploma.feature.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.databinding.FragmentIndustryFilterBinding
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.FilterViewModel
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryState
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryViewModel
import kotlin.getValue

class IndustryFilterFragment : Fragment() {

    private var _binding: FragmentIndustryFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IndustryViewModel by viewModel()
    private val filterViewModel: FilterViewModel by koinNavGraphViewModel(R.id.filter_screen)

    private lateinit var adapter: IndustryAdapter
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
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            closeFragment()
        }
    }

    private fun setupSelectButton() {
        binding.selectButton.setOnClickListener {
            closeFragment()
        }
    }

    private fun closeFragment() {
        filterViewModel.saveIndustry(selectedIndustry)
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
        val newSelected = if (selectedIndustry == industry) {
            null
        } else {
            industry
        }
        selectedIndustry = newSelected
        adapter.setSelectedIndustryId(selectedIndustry?.id)
    }

    private fun observeState() {
        viewModel.state.observe(
            viewLifecycleOwner
        ) { state ->

            when (state) {

                IndustryState.Loading -> {

                }

                is IndustryState.Content -> {
                    adapter.submitList(state.industries)
                }

                IndustryState.Error -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val INDUSTRY_KEY = "INDUSTRY"
    }
}
