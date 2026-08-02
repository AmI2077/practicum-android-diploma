package ru.practicum.android.diploma.feature.detail.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailNavigationCommand
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailState
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.feature.detail.utils.VacancyDetailFragmentFormatters
import ru.practicum.android.diploma.feature.detail.utils.bindVacancyInfo
import ru.practicum.android.diploma.feature.sharing.domain.SharingInteractor

class VacancyDetailFragment : Fragment() {

    private var _binding: FragmentVacancyDetailBinding? = null
    private val binding get() = _binding!!

    private val args: VacancyDetailFragmentArgs by navArgs()
    private val viewModel: VacancyDetailViewModel by viewModel()
    private val sharingInteractor: SharingInteractor by inject()

    private val formatter by lazy {
        VacancyDetailFragmentFormatters(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeState()
        observeNavigation()
        viewModel.loadVacancyDetail(args.vacancyId)
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.sharingButton.setOnClickListener {
            val currentState = viewModel.state.value
            if (currentState is VacancyDetailState.Content) {
                viewModel.onShareClick(currentState.vacancy.url)
            }
        }
        binding.favoritesButton.setOnClickListener {
            val currentState = viewModel.state.value
            if (currentState is VacancyDetailState.Content) {
                viewModel.onFavouritesClick()
            }
        }
        binding.contactEmail.setOnClickListener {
            val currentState = viewModel.state.value

            if (currentState is VacancyDetailState.Content) {
                val email = currentState.vacancy.contacts?.email

                if (!email.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:$email".toUri()
                    }
                    startActivity(intent)
                }
            }
        }

        binding.contactPhone.setOnClickListener {
            callContactPhone()
        }
    }

    private fun callContactPhone() {
        val currentState = viewModel.state.value as? VacancyDetailState.Content ?: return
        val phone = currentState.vacancy.contacts?.phones?.firstOrNull()?.formatted ?: return
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phone".toUri()
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun renderFavouriteButton(isFavourite: Boolean) {
        when (isFavourite) {
            true -> binding.favoritesButton.setImageResource(R.drawable.ic_favorites_on_24)
            false -> binding.favoritesButton.setImageResource(R.drawable.ic_favorites_off_24)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is VacancyDetailState.Loading -> renderStatus(showLoading = true)
                        is VacancyDetailState.Error -> renderStatus(showError = true)
                        is VacancyDetailState.NotFound -> renderStatus(showError = true)
                        is VacancyDetailState.NoInternet -> renderStatus(showError = true)
                        is VacancyDetailState.Content -> {
                            renderStatus(showContent = true)
                            renderFavouriteButton(state.isFavourite)
                            binding.bindVacancyInfo(
                                state.vacancy,
                                formatter
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationCommand.collect { command ->
                    handleNavigationCommand(command)
                }
            }
        }
    }

    private fun handleNavigationCommand(command: VacancyDetailNavigationCommand) {
        when (command) {
            is VacancyDetailNavigationCommand.ShareVacancy -> {
                val shareIntent = sharingInteractor.createShareIntent(command.url)
                startActivity(shareIntent)
            }
        }
    }

    private fun renderStatus(
        showLoading: Boolean = false,
        showContent: Boolean = false,
        showError: Boolean = false
    ) {
        with(binding) {
            progressBar.isVisible = showLoading
            detailScrollView.isVisible = showContent
            errorServer.isVisible = showError
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
