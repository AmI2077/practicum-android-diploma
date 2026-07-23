package ru.practicum.android.diploma.feature.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.databinding.FragmentIndustryFilterBinding
import ru.practicum.android.diploma.feature.filter.ui.utils.IndustryMocks

class IndustryFilterFragment : Fragment() {

    private var _binding: FragmentIndustryFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: IndustryAdapter
    private var selectedIndustryId: Int? = null

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
        setupRecyclerView()
        loadMockData()
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
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
        val newSelectedId = if (selectedIndustryId == industry.id) {
            null
        } else {
            industry.id
        }
        selectedIndustryId = newSelectedId
        adapter.setSelectedIndustryId(selectedIndustryId)
    }

    private fun loadMockData() {
        // Заменить на реальные данные API сейчас моки
        val mockIndustries = IndustryMocks.getMockIndustries()
        adapter.submitList(mockIndustries)
        adapter.setSelectedIndustryId(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
