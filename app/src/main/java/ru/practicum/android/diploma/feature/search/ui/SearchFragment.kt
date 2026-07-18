package ru.practicum.android.diploma.feature.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.VacancyCard
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.feature.search.ui.utils.MockData
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var adapter: VacancyAdapter? = null
    private var isSearchPerformed = false

    private var searchJob: Job? = null // потом перенести логику в ViewModel

    private companion object {
        const val SEARCH_DELAY = 2000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchEditText()
        setupClearButton()
        setupFilterButton()

        showInitialState()
    }

    private fun setupRecyclerView() {
        adapter = VacancyAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupSearchEditText() {
        binding.searchEditText.apply {
            doOnTextChanged { text, _, _, _ ->
                handleTextChanged(text.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cancelDelayedSearch()
                    performSearch(text.toString())
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun handleTextChanged(query: String) {
        binding.clearButton.isVisible = query.isNotEmpty()
        binding.searchButton.isVisible = query.isEmpty()

        if (query.isEmpty()) {
            cancelDelayedSearch()
            isSearchPerformed = false
            showInitialState()
            return
        }

        scheduleSearch(query)
    }

    private fun scheduleSearch(query: String) {
        cancelDelayedSearch()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DELAY)
            performSearch(query)
        }
    }

    private fun cancelDelayedSearch() {
        searchJob?.cancel()
        searchJob = null
    }

    private fun setupClearButton() {
        binding.clearButton.setOnClickListener {
            cancelDelayedSearch()
            isSearchPerformed = false
            binding.searchEditText.text?.clear()
            showInitialState()
            binding.searchEditText.requestFocus()
            hideKeyboard()
        }
    }

    private fun setupFilterButton() {
        binding.filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_search_screen_tab_to_filterFragment)
        }
    }

    // в будущем выполнение запросов в ViewModel
    private fun performSearch(query: String) {
        if (query.isBlank()) {
            showInitialState()
            return
        }

        isSearchPerformed = true

        showLoadingState()
        // что бы проверить разные экраны состояний
        binding.root.postDelayed({
            when {
                query.contains("пусто", ignoreCase = true) -> {
                    showEmptyResultState()
                }
                query.contains("ошибка", ignoreCase = true) -> {
                    showServerErrorState()
                }
                query.contains("интернет", ignoreCase = true) -> {
                    showNoInternetState()
                }
                else -> {
                    showSearchResult(MockData.getMockVacancies(), query)
                }
            }
        }, SEARCH_DELAY)
    }

    private fun showInitialState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        hideAllImageStates()
        if (!isSearchPerformed) {
            showImageState(ImageState.EMPTY)
        }
    }

    private fun showLoadingState() {
        hideAllImageStates()
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showSearchResult(vacancies: List<VacancyCard>, query: String) {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.statusContainer.isVisible = true
        binding.statusVacancies.text = getString(
            R.string.vacancies_found,
            vacancies.size
        )
        binding.recyclerView.isVisible = true
        adapter?.submitList(vacancies)
    }

    private fun showEmptyResultState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = true
        binding.statusVacancies.text = getString(R.string.vacancies_not_found)
        showImageState(ImageState.NO_VACANCIES)
    }

    private fun showNoInternetState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        hideAllImageStates()
        showImageState(ImageState.NO_INTERNET)
    }

    private fun showServerErrorState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        hideAllImageStates()
        showImageState(ImageState.SERVER_ERROR)
    }

    private fun hideAllImageStates() {
        binding.imageStateEmpty.isVisible = false
        binding.errorNoInternet.isVisible = false
        binding.errorNoVacancies.isVisible = false
        binding.errorServer.isVisible = false
    }

    private enum class ImageState {
        EMPTY,
        NO_INTERNET,
        NO_VACANCIES,
        SERVER_ERROR
    }

    private fun showImageState(state: ImageState) {
        hideAllImageStates()
        when (state) {
            ImageState.EMPTY -> {
                binding.imageStateEmpty.isVisible = true
            }
            ImageState.NO_INTERNET -> {
                binding.errorNoInternet.isVisible = true
            }
            ImageState.NO_VACANCIES -> {
                binding.errorNoVacancies.isVisible = true
            }
            ImageState.SERVER_ERROR -> {
                binding.errorServer.isVisible = true
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(
            InputMethodManager::class.java
        )
        imm?.hideSoftInputFromWindow(
            binding.searchEditText.windowToken,
            0
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelDelayedSearch()
        _binding = null
    }
}
