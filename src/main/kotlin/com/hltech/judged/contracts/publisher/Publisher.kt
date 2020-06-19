package com.hltech.judged.contracts.publisher

import com.hltech.judged.contracts.publisher.capabilities.Capabilities
import com.hltech.judged.contracts.publisher.capabilities.CapabilitiesReaderFactory
import com.hltech.judged.contracts.publisher.expectations.Expectations
import com.hltech.judged.contracts.publisher.expectations.ExpectationsReaderFactory
import com.hltech.judged.contracts.publisher.judged.JudgeD

class Publisher {

    private val capabilitiesReaderFactory = CapabilitiesReaderFactory()
    private val expectationsReaderFactory = ExpectationsReaderFactory()
    private val judgeD = JudgeD()

    fun publish(properties: PublisherProperties) {
        if (properties.capabilities.isEmpty() && properties.expectations.isEmpty()) {
            println("Did not found any contracts for publish.")
            return
        }

        val capabilities = getCapabilitiesFrom(properties)
        val expectations = getExpectationsFrom(properties)
        judgeD.publishContracts(properties, capabilities, expectations)
        println("Contracts successfully published.")
    }

    private fun getCapabilitiesFrom(properties: PublisherProperties): List<Capabilities> =
        properties.capabilities
            .map { capabilitiesReaderFactory.create(it).read(properties) }
            .toList()

    private fun getExpectationsFrom(properties: PublisherProperties): List<Expectations> =
        properties.expectations
            .map { expectationsReaderFactory.create(it).read(properties) }
            .flatten()

}