package com.hltech.judged.contracts.publisher

data class PublisherProperties(
    val projectName: String,
    val projectVersion: String,
    val judgeDLocation: String,
    val capabilities: List<String> = emptyList(),
    val expectations: List<String> = emptyList(),
    val extra: Map<String, String> = emptyMap()
)