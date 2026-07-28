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

        showInitialState()
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

            val hasFilters = viewModel.filterState.value?.let {
                it.salary != null && it.salary != 0 || it.hideWithoutSalary || it.industry != null
            } ?: false

            val isSearching = !isQueryBlank || hasFilters

            val isInitialState = !isSearching

            val isLoading = refreshState is LoadState.Loading && isSearching

            val isSuccess = refreshState is LoadState.NotLoading && itemCount > 0 && isSearching

            val isEmptyResult = refreshState is LoadState.NotLoading && itemCount == 0 && isSearching

            binding.statusContainer.isVisible = (isSuccess || isEmptyResult) && true

            when {
                isInitialState -> showInitialState()
                isLoading -> showLoadingState()
                isSuccess -> showSearchResultState()
                isEmptyResult -> showEmptyResultState()
                refreshState is LoadState.Error -> {
                    val error = refreshState.error
                    if (error.message?.contains("ServerError") == true) {
                        showServerErrorState()
                    } else {
                        showNoInternetState()
                    }
                }
            }
        }
    }

    private fun showSearchResultState() {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
    }

    private fun showInitialState() {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        binding.imageStateEmpty.isVisible = true
    }

    private fun showLoadingState() {
        hideAllImageStates()
        binding.progressBar.isVisible = true
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
    }

    private fun showEmptyResultState() {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusVacancies.text = getString(R.string.vacancies_not_found)
        binding.errorNoVacancies.isVisible = true
    }

    private fun showServerErrorState() {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        binding.errorServer.isVisible = true
    }

    private fun showNoInternetState() {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        binding.errorNoInternet.isVisible = true
    }

    private fun hideAllImageStates() {
        binding.imageStateEmpty.isVisible = false
        binding.errorNoInternet.isVisible = false
        binding.errorNoVacancies.isVisible = false
        binding.errorServer.isVisible = false
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
