package ru.practicum.android.diploma.feature.Parser.domain

import ru.practicum.android.diploma.feature.Parser.models.FormattedDescription

interface HtmlParserService {
    fun parseHtmlToStructured(html: String): List<FormattedDescription>
}
