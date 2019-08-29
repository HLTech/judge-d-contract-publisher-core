package com.hltech.judged.contracts.publisher.expectations

import kotlin.String

class ExpectationsReaderFactory {

    fun create(communicationInterface: String): ExpectationsReader =
        when (communicationInterface) {
            "rest" -> PactExpectationsReader()
            "jms" -> VauntExpectationsReader()
            else -> throw IllegalArgumentException("String reader for requested " +
                    "communication interface ($communicationInterface) not found")
        }
}