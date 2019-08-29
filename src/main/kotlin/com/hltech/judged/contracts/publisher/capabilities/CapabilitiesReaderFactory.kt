package com.hltech.judged.contracts.publisher.capabilities

class CapabilitiesReaderFactory {

    fun create(communicationInterface: String): CapabilitiesReader =
        when (communicationInterface) {
            "rest" -> SwaggerCapabilitiesReader()
            "jms" -> VauntCapabilitiesReader()
            else -> throw IllegalArgumentException("Capabilities reader for requested " +
                    "communication interface ($communicationInterface) not found")
        }
}