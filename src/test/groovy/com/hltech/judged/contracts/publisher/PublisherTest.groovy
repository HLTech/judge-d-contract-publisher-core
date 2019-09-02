package com.hltech.judged.contracts.publisher

import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import org.junit.ClassRule
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

class PublisherTest extends Specification {

    @ClassRule
    static WireMockClassRule wireMockRule = new WireMockClassRule(0)

    @Rule
    WireMockClassRule instanceRule = wireMockRule

    def publisher = new Publisher()

    def "should do nothing when no expectations nor capabilities provided"() {
        given:
            def properties = new PublisherProperties(
                    'test-project',
                    '1.0',
                    "http://localhost:${wireMockRule.port()}",
                    [],
                    [],
                    [:]
            )
        expect:
            publisher.publish(properties)
    }

    def "should publish swagger contract when rest capabilities are provided"() {
        given:
            stubFor(
                    post('/contracts/services/test-project/versions/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities.rest.value',
                                    equalToJson(file('src/test/resources/swagger/swagger.json'))))
                            .withRequestBody(matchingJsonPath('$.capabilities.rest.mimeType',
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath('$.expectations',
                                    equalToJson('{}')))
                            .willReturn(aResponse().withStatus(200))
            )

        and:
            def properties = new PublisherProperties(
                    'test-project',
                    '1.0',
                    "http://localhost:${wireMockRule.port()}",
                    ['rest'],
                    [],
                    [swaggerLocation:'src/test/resources/swagger']
            )

        expect:
            publisher.publish(properties)
    }

    def "should publish pacts when rest expectations are provided"() {
        given:
            stubFor(
                    post('/contracts/services/test-project/versions/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities',
                                    equalToJson('{}')))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Animal Service'].rest.value",
                                    equalToJson(file('src/test/resources/pact/sample-pact.json'))))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Animal Service'].rest.mimeType",
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Events Service'].rest.value",
                                    equalToJson(file('src/test/resources/pact/another-pact.json'))))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Events Service'].rest.mimeType",
                                    equalTo('application/json')))
                            .willReturn(aResponse().withStatus(200))
            )

        and:
            def properties = new PublisherProperties(
                    'test-project',
                    '1.0',
                    "http://localhost:${wireMockRule.port()}",
                    [],
                    ['rest'],
                    [pactsLocation:'src/test/resources/pact']
            )

        expect:
            publisher.publish(properties)
    }

    def "should publish vaunt capabilities when jms capability is declared"() {
        given:
            stubFor(
                    post('/contracts/services/test-project/versions/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities.jms.value',
                                    equalToJson("""
                                        [
                                          {
                                            "destinationType": "QUEUE",
                                            "destinationName": "request_for_information_queue",
                                            "message": {
                                              "type": "object",
                                              "id": "RequestMessage",
                                              "properties": {
                                                "name": {
                                                  "type": "string"
                                                }
                                              }
                                            }
                                          },
                                          {
                                            "destinationType": "TOPIC",
                                            "destinationName": "something_changed_topic",
                                            "message": {
                                              "type": "object",
                                              "id": "ChangedEvent",
                                              "properties": {
                                                "timestamp": {
                                                  "type": "integer"
                                                }
                                              }
                                            }
                                          }
                                        ]
                                        """)))
                            .withRequestBody(matchingJsonPath('$.capabilities.jms.mimeType',
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath('$.expectations',
                                    equalToJson('{}')))
                            .willReturn(aResponse().withStatus(200))
            )

        and:
            def properties = new PublisherProperties(
                    'test-project',
                    '1.0',
                    "http://localhost:${wireMockRule.port()}",
                    ['jms'],
                    [],
                    [vauntLocation:'src/test/resources/vaunt']
            )

        expect:
            publisher.publish(properties)
    }

    def "should publish vaunt expectations when jms expectation is declared"() {
        given:
            stubFor(
                    post('/contracts/services/test-project/versions/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities',
                                    equalToJson('{}')))
                            .withRequestBody(matchingJsonPath('$.expectations.audit-service.jms.value',
                                    equalToJson("""
                                        [
                                          {
                                            "destinationType": "QUEUE",
                                            "destinationName": "audit_queue",
                                            "message": {
                                              "type": "object",
                                              "id": "AuditMessage",
                                              "properties": {
                                                "payload": {
                                                  "type": "string"
                                                }
                                              }
                                            }
                                          }
                                        ]
                                        """)))
                            .withRequestBody(matchingJsonPath('$.expectations.audit-service.jms.mimeType',
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath('$.expectations.remote-client.jms.value',
                                    equalToJson("""
                                        [
                                          {
                                            "destinationType": "QUEUE",
                                            "destinationName": "reject_information_queue",
                                            "message": {
                                              "type": "object",
                                              "id": "RejectMessage",
                                              "properties": {
                                                "reason": {
                                                  "type": "string"
                                                },
                                                "code": {
                                                  "type": "integer"
                                                }
                                              }
                                            }
                                          },
                                          {
                                            "destinationType": "QUEUE",
                                            "destinationName": "accept_information_queue",
                                            "message": {
                                              "type": "object",
                                              "id": "AcceptMessage",
                                              "properties": {
                                                "id": {
                                                  "type": "integer"
                                                }
                                              }
                                            }
                                          }
                                        ]
                                        """)))
                            .withRequestBody(matchingJsonPath('$.expectations.remote-client.jms.mimeType',
                                    equalTo('application/json')))
                            .willReturn(aResponse().withStatus(200))
            )

        and:
            def properties = new PublisherProperties(
                    'test-project',
                    '1.0',
                    "http://localhost:${wireMockRule.port()}",
                    [],
                    ['jms'],
                    [vauntLocation:'src/test/resources/vaunt']
            )

        expect:
            publisher.publish(properties)
    }


    private static String file(String path) {
        return new File(path).text
    }
}
