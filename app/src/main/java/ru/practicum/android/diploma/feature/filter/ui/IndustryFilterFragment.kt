package ru.practicum.android.diploma.feature.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.models.filter.FilterIndustry
import ru.practicum.android.diploma.databinding.FragmentIndustryFilterBinding
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryState
import ru.practicum.android.diploma.feature.filter.ui.viewmodel.IndustryViewModel

// Загрузка данных перенесена во ViewModel.
// Fragment отвечает только за отображение состояния UI.
// ViewModel самостоятельно вызывает Interactor при создании.
// Моки закомментированы  после подключения реального источника данных через ViewModel.
// Данные теперь приходят через IndustryState.
// ViewModel получает данные через Domain-слой.

class IndustryFilterFragment : Fragment() {

    private var _binding: FragmentIndustryFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IndustryViewModel by viewModel()

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
//        loadMockData()
        observeState()
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

    // Теперь метод больше не нужен
//    private fun loadMockData() {
//        // Заменить на реальные данные API сейчас моки
//        val mockIndustries = IndustryMocks.getMockIndustries()
//        adapter.submitList(mockIndustries)
//        adapter.setSelectedIndustryId(null)
//    }

    private fun observeState() {
        viewModel.state.observe(
            viewLifecycleOwner
        ) { state ->

            when (state) {

                IndustryState.Loading -> {
                    // TODO:
                }

                is IndustryState.Content -> {
                    adapter.submitList(state.industries)
                }

                IndustryState.Error -> {
                    // TODO:
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
