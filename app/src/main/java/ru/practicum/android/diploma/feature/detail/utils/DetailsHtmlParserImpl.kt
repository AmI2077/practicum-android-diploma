package ru.practicum.android.diploma.feature.detail.utils

import org.jsoup.Jsoup
import ru.practicum.android.diploma.core.utils.HtmlParser

class DetailsHtmlParserImpl: HtmlParser {

    override fun parse(rawHtml: String): String {
        val document = Jsoup.parse(rawHtml)
        val stringBuilder = StringBuilder()

        val title = document.select("h2").text()
        stringBuilder.append("<big><b>").append(title).append("</b></big>").append("<br><br>")

        document.select("p").forEach { element ->
            stringBuilder.append(element.text()).append("<br>")
        }

        document.select("section").forEach { section ->
            val sectionTitle = section.select("h3").text()
            stringBuilder.append("<br>").append("<b>").append(sectionTitle).append("</b>").append(":<br>")

            section.select("li").forEach { item ->
                stringBuilder.append("\t").append("• ").append(item.text()).append("<br>")
            }
        }
        return stringBuilder.toString().trim()
    }
}
