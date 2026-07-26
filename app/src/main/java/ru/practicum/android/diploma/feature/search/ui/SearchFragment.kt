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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.NetworkErrors
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.feature.search.ui.viewmodel.SearchState
import ru.practicum.android.diploma.feature.search.ui.viewmodel.SearchViewModel


/**
 * Что перенесено в SearchViewModel:
 * - выполнение поиска вакансий через SearchVacanciesUseCase
 * - debounce (задержка перед отправкой запроса)
 * - формирование параметров поиска VacancySearchParams
 * - обработка результата поиска (успех / пустой результат / ошибки сети)
 * - хранение состояния экрана через SearchState
 *
 * SearchFragment теперь отвечает только за:
 * - отображение UI-состояний
 * - обработку пользовательских действий
 * - подписку на изменения состояния ViewModel
 * - навигацию
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private var adapter: VacancyAdapter? = null

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
        setupScrollListener()
        observeState()
        viewModel.restoreStateIfNeeded()
    }

    private fun setupRecyclerView() {
        adapter = VacancyAdapter { vacancy ->
            openVacancyDetails(vacancy)
        }
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter =
                this@SearchFragment.adapter
        }
    }

    private fun setupScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = adapter?.itemCount ?: 0
                    if (lastVisiblePosition >= totalItemCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
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
        viewModel.state.observe(
            viewLifecycleOwner
        ) { state ->
            when (state) {
                SearchState.Initial -> {
                    showInitialState()
                }
                SearchState.Loading -> {
                    showLoadingState()
                }
                is SearchState.Content -> {
                    showSearchResult(
                        state.vacancies,
                        state.isLoadingNextPage
                    )
                }
                SearchState.EmptyResult -> {
                    showEmptyResultState()
                }
                is SearchState.Error -> {
                    when (state.error) {
                        NetworkErrors.ServerError -> {
                            showServerErrorState()
                        }

                        NetworkErrors.NoInternetConnectionError -> {
                            showNoInternetState()
                        }

                        NetworkErrors.NotFoundError -> {
                            showEmptyResultState()
                        }
                    }
                }
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

    private fun showInitialState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        hideAllImageStates()
        binding.imageStateEmpty.isVisible = true
        adapter?.hideLoadingFooter()
    }

    private fun showLoadingState() {
        hideAllImageStates()
        binding.progressBar.isVisible = true
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        adapter?.hideLoadingFooter()
    }
    private fun showSearchResult(
        vacancies: List<VacancyCard>,
        isLoadingNextPage: Boolean
    ) {
        hideAllImageStates()
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        binding.statusContainer.isVisible = true
        binding.statusVacancies.text =
            getString(
                R.string.vacancies_found,
                vacancies.size
            )
        adapter?.submitList(vacancies)
        if (isLoadingNextPage) {
            adapter?.showLoadingFooter()
        } else {
            adapter?.hideLoadingFooter()
        }
    }

    private fun showEmptyResultState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = true
        adapter?.hideLoadingFooter()
        binding.statusVacancies.text =
            getString(
                R.string.vacancies_not_found
            )
        showImageState(
            ImageState.NO_VACANCIES
        )
    }

    private fun showServerErrorState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        adapter?.hideLoadingFooter()
        showImageState(
            ImageState.SERVER_ERROR
        )
    }

    private fun showNoInternetState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.statusContainer.isVisible = false
        adapter?.hideLoadingFooter()
        showImageState(
            ImageState.NO_INTERNET
        )
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

    private fun showImageState(
        state: ImageState
    ) {
        hideAllImageStates()
        when (state) {
            ImageState.EMPTY ->
                binding.imageStateEmpty.isVisible = true
            ImageState.NO_INTERNET ->
                binding.errorNoInternet.isVisible = true
            ImageState.NO_VACANCIES ->
                binding.errorNoVacancies.isVisible = true
            ImageState.SERVER_ERROR ->
                binding.errorServer.isVisible = true
        }
    }

    private fun hideKeyboard(){
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
        _binding = null
    }
}
