package ru.practicum.android.diploma.feature.Parser.models

sealed class FormattedDescription {
    data class Heading2(val text: String) : FormattedDescription()
    data class Heading3(val text: String) : FormattedDescription()
    data class Paragraph(val text: String) : FormattedDescription()
    data class BulletList(val items: List<String>) : FormattedDescription()
    data class SimpleText(val text: String) : FormattedDescription()
}
