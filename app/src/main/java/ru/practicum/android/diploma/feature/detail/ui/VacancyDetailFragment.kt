package ru.practicum.android.diploma.feature.detail.ui

import android.R.attr.data
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import ru.practicum.android.diploma.core.extensions.loadCompanyLogo
import ru.practicum.android.diploma.core.models.details.Contacts
import ru.practicum.android.diploma.core.models.details.Phone
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailNavigationCommand
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailState
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.feature.detail.utils.VacancyDetailFragmentFormatters
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                        data = Uri.parse("mailto:$email")
                    }

                    startActivity(intent)
                }
            }
        }


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
                        is VacancyDetailState.Loading -> showLoading()
                        is VacancyDetailState.Content -> {
                            showVacancyDetail(state.vacancy)
                            renderFavouriteButton(state.isFavourite)
                        }

                        is VacancyDetailState.Error -> showError()
                        is VacancyDetailState.NotFound -> showNotFound()
                        is VacancyDetailState.NoInternet -> showNoInternet()
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

    private fun showLoading() {
        with(binding) {
            detailScrollView.isVisible = false
            progressBar.isVisible = true
            errorServer.isVisible = false
        }
    }

    private fun showVacancyDetail(vacancy: VacancyDetails) {
        with(binding) {
            detailScrollView.isVisible = true
            progressBar.isVisible = false
            errorServer.isVisible = false

            jobTitle.text = vacancy.name

            salary.text = formatter.formatSalary(vacancy.salary)

            company.text = vacancy.employer.name

            companyLogo.loadCompanyLogo(
                logoUrl = vacancy.employer.logo,
                cornerRadius = resources.getDimension(R.dimen.corner_radius).toInt()
            )

            val locationText = vacancy.address?.raw ?: vacancy.area.name
            city.text = locationText

            experience.text = vacancy.experience?.name ?: getString(R.string.not_specified)

            val scheduleName = vacancy.schedule?.name ?: ""
            val employmentName = vacancy.employment?.name ?: ""
            employment.text = when {
                scheduleName.isNotEmpty() && employmentName.isNotEmpty() -> "$scheduleName, $employmentName"
                scheduleName.isNotEmpty() -> scheduleName
                employmentName.isNotEmpty() -> employmentName
                else -> getString(R.string.not_specified)
            }

            description.text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT)
            description.isVisible = true

            if (vacancy.skills.isNotEmpty()) {
                val skillsText = vacancy.skills.joinToString("\n") { "• $it" }
                skill.text = skillsText
                skill.isVisible = true
                skillTitle.isVisible = true
            } else {
                skill.isVisible = false
                skillTitle.isVisible = false
            }
            displayContacts(vacancy.contacts)
        }
    }

    private fun showError() {
        with(binding) {
            detailScrollView.isVisible = false
            progressBar.isVisible = false
            errorServer.isVisible = true
            errorServerImage.setImageResource(R.drawable.ic_vacancy_server_error)
        }
    }

    private fun showNotFound() {
        with(binding) {
            detailScrollView.isVisible = false
            progressBar.isVisible = false
            errorServer.isVisible = true
            errorServerImage.setImageResource(R.drawable.ic_industry_filter)
        }
    }

    private fun showNoInternet() {
        with(binding) {
            detailScrollView.isVisible = false
            progressBar.isVisible = false
            errorServer.isVisible = true
            errorServerImage.setImageResource(R.drawable.ic_no_internet)
        }
    }

    private fun displayContacts(contacts: Contacts?) {
        with(binding) {
            if (contacts == null) {
                contactsCont.isVisible = false
                return
            }

            contactsCont.isVisible = true

            val hasName = setContactField(contactName, contactNameLabel, contacts.name)
            val hasEmail = setContactField(contactEmail, contactEmailLabel,     contacts.email.orEmpty())
            val hasPhone = setupPhoneFields(contacts.phones)

            contactsCont.isVisible = hasName || hasEmail || hasPhone
        }
    }

    private fun setContactField(textView: TextView, labelView: View, text: String): Boolean {
        val isValid = text.isNotEmpty()
        textView.isVisible = isValid
        labelView.isVisible = isValid
        if (isValid) {
            textView.text = text
        }
        return isValid
    }

    private fun setupPhoneFields(phones: List<Phone>): Boolean {
        val phone = phones.firstOrNull()
        val hasPhone = phone != null

        binding.contactPhoneLabel.isVisible = hasPhone
        binding.contactPhone.isVisible = hasPhone
        if (phone != null) {
            binding.contactPhone.setOnClickListener {
                val currentState = viewModel.state.value as? VacancyDetailState.Content ?: return@setOnClickListener

                val phone = currentState.vacancy.contacts
                    ?.phones
                    ?.firstOrNull()
                    ?.formatted ?: return@setOnClickListener

                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }

                startActivity(Intent.createChooser(intent, null))
            }
        }
        val hasComment = phone != null && !phone.comment.isNullOrEmpty()
        binding.contactPhoneCommentLabel.isVisible = hasComment
        binding.contactPhoneComment.isVisible = hasComment
        if (hasComment) {
            binding.contactPhoneComment.text = phone.comment
        }

        return hasPhone
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
