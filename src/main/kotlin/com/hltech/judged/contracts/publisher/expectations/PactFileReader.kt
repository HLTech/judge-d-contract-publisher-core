package com.hltech.judged.contracts.publisher.expectations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

class PactFileReader {

    private val mapper = jacksonObjectMapper()

    fun read(file: File): PactFile? {
        val content = mapper.readTree(file)
        if (!content.has("provider") || !content["provider"].has("name")) {
            println("Looks like not pact file: ${file.absolutePath}, skipping")
            return null
        }

        return PactFile(content["provider"]["name"].textValue(), mapper.writeValueAsString(content))
    }

    data class PactFile(val providerName: String, val content: String)
}