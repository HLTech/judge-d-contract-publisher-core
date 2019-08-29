package com.hltech.judged.contracts.publisher.expectations

import com.hltech.judged.contracts.publisher.PublisherProperties

interface ExpectationsReader {

    fun read(properties: PublisherProperties): List<Expectations>
}