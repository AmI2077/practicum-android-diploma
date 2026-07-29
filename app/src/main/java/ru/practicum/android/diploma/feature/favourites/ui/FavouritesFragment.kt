package ru.practicum.android.diploma.feature.favourites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.models.card.VacancyCard
import ru.practicum.android.diploma.databinding.FragmentFavouritesBinding
import ru.practicum.android.diploma.feature.favourites.ui.viewmodel.FavouritesState
import ru.practicum.android.diploma.feature.favourites.ui.viewmodel.FavouritesViewModel
import ru.practicum.android.diploma.feature.search.ui.VacancyAdapter

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null

    private val binding get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModel()
    private var vacancyAdapter: VacancyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVacancyAdapter()
        observeUiState()
    }

    private fun observeUiState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavouritesState) {
        when (state) {
            is FavouritesState.Content -> {
                showContent(state.vacancies)
            }

            FavouritesState.Empty -> {
                showEmptyView()
            }

            FavouritesState.Error -> {
                showErrorView()
            }

            FavouritesState.Loading -> {
                showLoadingView()
            }
        }
    }

    private fun showEmptyView() {
        hideViews()
        binding.stateEmpty.isVisible = true
    }

    private fun showErrorView() {
        hideViews()
        binding.errorState.isVisible = true
    }

    private fun showLoadingView() {
        hideViews()
        binding.progressBar.isVisible = true
    }

    private fun showContent(vacancies: List<VacancyCard>) {
        hideViews()
        binding.recyclerView.isVisible = true
        vacancyAdapter?.submitList(vacancies)
    }

    private fun setupVacancyAdapter() {
        vacancyAdapter = VacancyAdapter {
            openVacancyDetails(it)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacancyAdapter
        }
    }

    private fun openVacancyDetails(
        vacancy: VacancyCard
    ) {
        val action =
            FavouritesFragmentDirections.actionFavouritesScreenTabToVacancyDetailFragment(vacancy.id)

        findNavController().navigate(action)
    }

    private fun hideViews() {
        binding.recyclerView.isVisible = false
        binding.errorState.isVisible = false
        binding.stateEmpty.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
