package ru.practicum.android.diploma.feature.detail.utils

import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.extensions.loadCompanyLogo
import ru.practicum.android.diploma.core.models.details.Contacts
import ru.practicum.android.diploma.core.models.details.Phone
import ru.practicum.android.diploma.core.models.details.VacancyDetails
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailBinding
import kotlin.collections.firstOrNull

fun FragmentVacancyDetailBinding.bindVacancyInfo(
    vacancy: VacancyDetails,
    formatter: VacancyDetailFragmentFormatters
) {
    jobTitle.text = vacancy.name
    salary.text = formatter.formatSalary(vacancy.salary)
    company.text = vacancy.employer.name

    companyLogo.loadCompanyLogo(
        logoUrl = vacancy.employer.logo,
        cornerRadius = root.resources.getDimension(R.dimen.corner_radius).toInt()
    )

    val locationText = vacancy.address?.raw ?: vacancy.area.name
    city.text = locationText
    experience.text = vacancy.experience?.name ?: root.context.getString(R.string.not_specified)

    val scheduleName = vacancy.schedule?.name ?: ""
    val employmentName = vacancy.employment?.name ?: ""
    employment.text = when {
        scheduleName.isNotEmpty() && employmentName.isNotEmpty() -> "$scheduleName, $employmentName"
        scheduleName.isNotEmpty() -> scheduleName
        employmentName.isNotEmpty() -> employmentName
        else -> root.context.getString(R.string.not_specified)
    }

    description.text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT)
    description.isVisible = true

    if (vacancy.skills.isNotEmpty()) {
        skill.text = vacancy.skills.joinToString("\n") { "• $it" }
        skill.isVisible = true
        skillTitle.isVisible = true
    } else {
        skill.isVisible = false
        skillTitle.isVisible = false
    }

    bindContacts(vacancy.contacts)
}

private fun FragmentVacancyDetailBinding.bindContacts(contacts: Contacts?) {
    if (contacts == null) {
        contactsCont.isVisible = false
        return
    }

    contactsCont.isVisible = true

    val hasName = setContactField(contactName, contactNameLabel, contacts.name)
    val hasEmail = setContactField(contactEmail, contactEmailLabel, contacts.email.orEmpty())
    val hasPhone = setupPhoneFields(contacts.phones)

    contactsCont.isVisible = hasName || hasEmail || hasPhone
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

private fun FragmentVacancyDetailBinding.setupPhoneFields(phones: List<Phone>): Boolean {
    val phone = phones.firstOrNull()
    val hasPhone = phone != null

    contactPhoneLabel.isVisible = hasPhone
    contactPhone.isVisible = hasPhone

    phone?.let {
        contactPhone.text = it.formatted
    }

    val hasComment = phone?.comment?.isNotEmpty() == true

    contactPhoneCommentLabel.isVisible = hasComment
    contactPhoneComment.isVisible = hasComment

    if (hasComment) {
        contactPhoneComment.text = phone.comment
    }

    return hasPhone
}
