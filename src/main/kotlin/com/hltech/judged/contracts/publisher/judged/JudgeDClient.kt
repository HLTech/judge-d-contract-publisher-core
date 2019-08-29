package com.hltech.judged.contracts.publisher.judged

import feign.Headers
import feign.Param
import feign.RequestLine

interface JudgeDClient {

    @RequestLine("POST contracts/services/{serviceName}/versions/{serviceVersion}")
    @Headers("Content-Type: application/json")
    fun publish(
        @Param("serviceName") serviceName: String,
        @Param("serviceVersion") serviceVersion: String,
        contracts: ServiceContractsForm
    )
}