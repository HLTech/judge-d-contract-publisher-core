package com.hltech.judged.contracts.publisher.expectations

data class Expectations(
    val providerName: String,
    val communicationInterface: String,
    val value: String,
    val mimeType: String
)