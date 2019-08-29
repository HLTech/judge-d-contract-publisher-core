package com.hltech.judged.contracts.publisher.judged

data class ServiceContractsForm(
    val capabilities: Map<String, ContractForm>,
    val expectations: Map<String, Map<String, ContractForm>>
)