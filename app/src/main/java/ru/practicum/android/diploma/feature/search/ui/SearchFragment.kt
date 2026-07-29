package ru.practicum.android.diploma.feature.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.feature.search.ui.viewmodel.SearchViewModelWithPaging

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModelWithPaging by koinNavGraphViewModel(R.id.search_screen_tab)
    private var adapter: PagingVacancyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentSearchBinding.inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchEditText()
        setupClearButton()
        setupFilterButton()
        observeState()
        setupLoadStateListener()

        updateUiState(PagingUiState.Initial)
    }

    private fun setupRecyclerView() {
        adapter = PagingVacancyAdapter { vacancy ->
            openVacancyDetails(vacancy)
        }
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter =
                this@SearchFragment.adapter?.withLoadStateFooter(
                    footer = VacancyLoadStateAdapter()
                )
        }
    }

    private fun setupSearchEditText() {
        binding.searchEditText.apply {
            doOnTextChanged { text, _, _, _ ->
                val query = text.toString()
                binding.clearButton.isVisible =
                    query.isNotEmpty()
                binding.searchButton.isVisible =
                    query.isEmpty()
                viewModel.search(query)
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupClearButton() {
        binding.clearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
            binding.searchEditText.requestFocus()
        }
    }

    private fun setupFilterButton() {
        binding.filterButton.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_search_screen_tab_to_filterFragment
                )
        }
    }

    private fun observeState() {
        viewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            adapter?.submitData(lifecycle, pagingData)
        }

        viewModel.totalFound.observe(viewLifecycleOwner) { found ->
            if (found > 0) {
                binding.statusContainer.isVisible = true
                binding.statusVacancies.text =
                    getString(
                        R.string.vacancies_found,
                        found
                    )
            }

        }
    }

    private fun openVacancyDetails(
        vacancy: VacancyCard
    ) {
        val action =
            SearchFragmentDirections
                .actionSearchScreenTabToVacancyDetailFragment(
                    vacancy.id
                )
        findNavController()
            .navigate(action)
    }

    private fun setupLoadStateListener() {
        adapter?.addLoadStateListener { loadStates ->
            val refreshState = loadStates.refresh
            val itemCount = adapter?.itemCount ?: 0
            val isQueryBlank = binding.searchEditText.text.isNullOrBlank()

            val hasFilters = hasActiveFilters()

            val isSearching = !isQueryBlank || hasFilters

            binding.statusContainer.isVisible = refreshState is LoadState.NotLoading && isSearching

            val uiState = when {
                !isSearching -> PagingUiState.Initial
                refreshState is LoadState.Loading -> PagingUiState.Loading
                refreshState is LoadState.NotLoading && itemCount > 0 -> PagingUiState.Success
                refreshState is LoadState.NotLoading && itemCount == 0 -> PagingUiState.Empty
                refreshState is LoadState.Error -> PagingUiState.Error(refreshState.error)
                else -> PagingUiState.Initial
            }

            updateUiState(uiState)
        }
    }

    private fun hasActiveFilters(): Boolean {
        return viewModel.filterState.value?.let {
            it.salary != null && it.salary != 0 || it.hideWithoutSalary || it.industry != null
        } ?: false
    }

    private fun updateUiState(state: PagingUiState) {
        binding.imageStateEmpty.isVisible = state is PagingUiState.Initial
        binding.progressBar.isVisible = state is PagingUiState.Loading
        binding.recyclerView.isVisible = state is PagingUiState.Success

        binding.errorNoVacancies.isVisible = state is PagingUiState.Empty

        val isServerError = state is PagingUiState.Error && state.error.message?.contains("ServerError") == true
        val isInternetError = state is PagingUiState.Error && !isServerError

        binding.errorServer.isVisible = isServerError
        binding.errorNoInternet.isVisible = isInternetError

        if (state is PagingUiState.Initial || state is PagingUiState.Loading || state is PagingUiState.Error) {
            binding.statusContainer.isVisible = false
        }
        if (state is PagingUiState.Empty) {
            binding.statusVacancies.text = getString(R.string.vacancies_not_found)
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext()
                .getSystemService(
                    InputMethodManager::class.java
                )
        imm?.hideSoftInputFromWindow(
            binding.searchEditText.windowToken,
            0
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}
