package com.wutsi.ecommerce.shipping.service

import com.github.mustachejava.DefaultMustacheFactory
import com.wutsi.platform.mail.dto.Message
import java.io.InputStreamReader
import java.io.StringWriter

class EmailMessageBuilder(
    private val template: String,
    private val language: String,
    private val variables: Map<String, Any>
) {
    fun build(): Message {
        val msg = generate()
        val i = msg.indexOf("\n")
        return Message(
            subject = msg.substring(0, i),
            body = msg.substring(i + 1),
            language = language,
            mimeType = "text/html"
        )
    }

    private fun generate(): String {
        val reader =
            InputStreamReader(EmailMessageBuilder::class.java.getResourceAsStream("/template/${template}_$language.html"))
        reader.use {
            val writer = StringWriter()
            writer.use {
                val mustache = DefaultMustacheFactory().compile(reader, "text")
                mustache.execute(
                    writer,
                    mapOf(
                        "scope" to variables
                    )
                )
                writer.flush()
                return writer.toString()
            }
        }
    }
}
