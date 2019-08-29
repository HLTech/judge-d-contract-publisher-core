package com.hltech.judged.contracts.publisher.capabilities

import com.hltech.judged.contracts.publisher.PublisherProperties
import com.hltech.judged.contracts.publisher.vaunt.VauntFileReader
import com.hltech.vaunt.core.VauntSerializer

class VauntCapabilitiesReader : CapabilitiesReader {

    private val fileReader = VauntFileReader()
    private val serializer = VauntSerializer()

    override fun read(properties: PublisherProperties): Capabilities {
        val contracts =  fileReader.read(properties)
            .flatMap { it.capabilities.contracts }
            .toList()

        return Capabilities("jms", serializer.serialize(contracts), "application/json")
    }

}