package ru.practicum.android.diploma.feature.detail.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailNavigationCommand
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailState
import ru.practicum.android.diploma.feature.detail.ui.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.feature.sharing.domain.SharingInteractor
import java.util.Locale

class VacancyDetailFragment : Fragment() {

    private var _binding: FragmentVacancyDetailBinding? = null
    private val binding get() = _binding!!

    private val args: VacancyDetailFragmentArgs by navArgs()

    private val viewModel: VacancyDetailViewModel by viewModel()

    private val sharingInteractor: SharingInteractor by inject()

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

        setupBackButton()
        setupShareButton()
        observeState()
        observeNavigation()

        viewModel.loadVacancyDetail(args.vacancyId)
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupShareButton() {
        binding.sharingButton.setOnClickListener {
            val currentState = viewModel.state.value
            if (currentState is VacancyDetailState.Content) {
                viewModel.onShareClick(currentState.vacancy.url)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is VacancyDetailState.Loading -> showLoading()
                        is VacancyDetailState.Content -> showVacancyDetail(state.vacancy)
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

            salary.text = formatSalary(vacancy.salary)

            company.text = vacancy.employer.name
            loadCompanyLogo(vacancy.employer.logo)

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

            vacancy.description?.let {
                description.text = Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                description.isVisible = true
            } ?: run {
                description.isVisible = false
            }

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

    private fun formatSalary(salary: ru.practicum.android.diploma.core.models.details.Salary?): String {
        if (salary == null) {
            return getString(R.string.salary_not_specified)
        }

        val from = salary.from
        val to = salary.to
        val currencySymbol = getCurrencySymbol(salary.currency)

        return when {
            from != null && to != null -> {
                "${getString(R.string.salary_from)} ${formatNumber(from)} " +
                    "${getString(R.string.salary_to)} ${formatNumber(to)} $currencySymbol"
            }

            from != null -> {
                "${getString(R.string.salary_from)} ${formatNumber(from)} $currencySymbol"
            }

            to != null -> {
                "${getString(R.string.salary_to)} ${formatNumber(to)} $currencySymbol"
            }

            else -> {
                getString(R.string.salary_not_specified)
            }
        }
    }

    private fun formatNumber(number: Int): String {
        return String.format(Locale.getDefault(), "%,d", number).replace(',', ' ')
    }

    private fun getCurrencySymbol(currency: String?): String {
        return when (currency) {
            "RUR", "RUB" -> getString(R.string.currency_rub)
            "USD" -> getString(R.string.currency_usd)
            "EUR" -> getString(R.string.currency_eur)
            "KZT" -> getString(R.string.currency_kzt)
            "UAH" -> getString(R.string.currency_uah)
            "BYR" -> getString(R.string.currency_byr)
            "AZN" -> getString(R.string.currency_azn)
            "UZS" -> getString(R.string.currency_uzs)
            "GEL" -> getString(R.string.currency_gel)
            "KGS" -> getString(R.string.currency_kgs)
            else -> ""
        }
    }

    private fun loadCompanyLogo(logoUrl: String?) {
        val context = binding.root.context
        val cornerRadius = context.resources.getDimension(R.dimen.corner_radius).toInt()

        if (!logoUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(logoUrl)
                .placeholder(R.drawable.ic_placeholder_32)
                .error(R.drawable.ic_placeholder_32)
                .transform(
                    CenterCrop(),
                    RoundedCorners(cornerRadius)
                )
                .into(binding.companyLogo)
        } else {
            binding.companyLogo.setImageResource(R.drawable.ic_placeholder_32)
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

    private fun displayContacts(contacts: ru.practicum.android.diploma.core.models.details.Contacts?) {
        with(binding) {
            if (contacts == null) {
                contactsCont.isVisible = false
                return
            }

            contactsCont.isVisible = true

            val hasName = !contacts.name.isNullOrEmpty()
            contactNameLabel.isVisible = hasName
            contactName.text = contacts.name ?: ""
            contactName.isVisible = hasName

            val hasEmail = !contacts.email.isNullOrEmpty()
            contactEmailLabel.isVisible = hasEmail
            contactEmail.text = contacts.email ?: ""
            contactEmail.isVisible = hasEmail

            val phones = contacts.phones
            val hasPhone = !phones.isNullOrEmpty()

            if (hasPhone) {
                val phone = phones.first()

                contactPhoneLabel.isVisible = true
                contactPhone.text = phone.formatted ?: ""
                contactPhone.isVisible = true

                val hasComment = !phone.comment.isNullOrEmpty()
                contactPhoneCommentLabel.isVisible = hasComment
                contactPhoneComment.text = phone.comment ?: ""
                contactPhoneComment.isVisible = hasComment
            } else {
                contactPhoneLabel.isVisible = false
                contactPhone.isVisible = false
                contactPhoneCommentLabel.isVisible = false
                contactPhoneComment.isVisible = false
            }

            val hasAnyContactInfo = hasName || hasEmail || hasPhone

            if (!hasAnyContactInfo) {
                contactsCont.isVisible = false
            }
        }
    }

    private fun showNotFound() {
        showError()
    }

    private fun showNoInternet() {
        showError()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
