package com.hltech.judged.contracts.publisher.vaunt

import com.hltech.judged.contracts.publisher.PublisherProperties
import com.hltech.vaunt.core.VauntSerializer
import com.hltech.vaunt.core.domain.model.Service
import java.io.File

class VauntFileReader {

    private val serializer = VauntSerializer()

    fun read(properties: PublisherProperties): List<Service> =
        when (val vauntLocation = properties.extra[VAUNT_LOCATION_KEY]) {
            is String -> readFrom(File(vauntLocation))
            else -> throw IllegalArgumentException("Parameter 'vauntLocation' is required for jms support")
        }

    private fun readFrom(vauntFilesLocation: File): List<Service> {
        if (!vauntFilesLocation.exists()) {
            throw IllegalArgumentException("Directory ${vauntFilesLocation.absolutePath} does not exist!")
        }

        val vauntFiles = vauntFilesLocation
            .listFiles { _, name -> name.endsWith(".json") }
            .toList()

        println("Found ${vauntFiles.size} vaunt files")

        return vauntFiles.map { serializer.readServiceDefinition(it) }
    }

    private companion object {
        private const val VAUNT_LOCATION_KEY = "vauntLocation"
    }
}