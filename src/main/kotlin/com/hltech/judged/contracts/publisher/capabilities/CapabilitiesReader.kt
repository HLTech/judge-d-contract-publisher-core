package com.hltech.judged.contracts.publisher.capabilities

import com.hltech.judged.contracts.publisher.PublisherProperties

interface CapabilitiesReader {

    fun read(properties: PublisherProperties): Capabilities
}