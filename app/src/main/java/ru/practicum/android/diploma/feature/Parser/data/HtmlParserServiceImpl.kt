package ru.practicum.android.diploma.feature.Parser.data

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import ru.practicum.android.diploma.feature.Parser.domain.HtmlParserService
import ru.practicum.android.diploma.feature.Parser.models.FormattedDescription

class HtmlParserServiceImpl : HtmlParserService {

    override fun parseHtmlToStructured(html: String): List<FormattedDescription> {
        if (html.isEmpty()) return emptyList()

        val document = Jsoup.parse(html)
        val body = document.body()
        val result = mutableListOf<FormattedDescription>()

        for (element in body.children()) {
            processElement(element, result)
        }

        return result
    }

    private fun processElement(element: Element, result: MutableList<FormattedDescription>) {
        when (element.tagName()) {
            "h2" -> {
                val text = element.text().trim()
                if (text.isNotEmpty()) {
                    result.add(FormattedDescription.Heading2(text))
                }
            }
            "h3" -> {
                val text = element.text().trim()
                if (text.isNotEmpty()) {
                    result.add(FormattedDescription.Heading3(text))
                }
            }
            "p" -> {
                val text = element.text().trim()
                if (text.isNotEmpty()) {
                    result.add(FormattedDescription.Paragraph(text))
                }
            }
            "ul" -> {
                val items = element.select("li")
                    .map { it.text().trim() }
                    .filter { it.isNotEmpty() }

                if (items.isNotEmpty()) {
                    result.add(FormattedDescription.BulletList(items))
                }
            }
            "li" -> {
                val text = element.text().trim()
                if (text.isNotEmpty()) {
                    result.add(FormattedDescription.SimpleText(text))
                }
            }
            "div", "section", "article", "main" -> {
                for (child in element.children()) {
                    processElement(child, result)
                }
            }
            else -> {
                val text = element.text().trim()
                if (text.isNotEmpty()) {
                    result.add(FormattedDescription.SimpleText(text))
                }
                for (child in element.children()) {
                    processElement(child, result)
                }
            }
        }
    }
}
