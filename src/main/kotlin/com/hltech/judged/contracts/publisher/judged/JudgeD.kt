package com.hltech.judged.contracts.publisher.judged

import com.hltech.judged.contracts.publisher.PublisherProperties
import com.hltech.judged.contracts.publisher.capabilities.Capabilities
import com.hltech.judged.contracts.publisher.expectations.Expectations
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder

class JudgeD {

    fun publishContracts(properties: PublisherProperties, capabilities: List<Capabilities>, expectations: List<Expectations>) {
        val capabilitiesMap = capabilities
            .map { it.communicationInterface to ContractForm(it.value, it.mimeType) }
            .toMap()

        val expectationsMap = expectations
            .groupBy { it.providerName }
            .mapValues { entry -> entry.value
                .map { it.communicationInterface to ContractForm(it.value, it.mimeType) }
                .toMap()
            }

        val contractsForm = ServiceContractsForm(capabilitiesMap, expectationsMap)

        Feign.builder()
            .encoder(JacksonEncoder())
            .decoder(JacksonDecoder())
            .target(JudgeDClient::class.java, properties.judgeDLocation)
            .publish(properties.projectName, properties.projectVersion, contractsForm)
    }
}