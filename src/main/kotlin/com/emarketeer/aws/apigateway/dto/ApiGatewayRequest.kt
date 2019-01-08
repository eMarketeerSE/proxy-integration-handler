package com.emarketeer.aws.apigateway.dto

import com.emarketeer.consent.dto.RequestContext

class ApiGatewayRequest<Body, Query>() {
    var resource: String? = null
    var path: String? = null
    var httpMethod: String? = null
    var headers: Map<String, String>? = null
    var queryStringParameters: Query? = null
    var pathParameters: Map<String, String>? = null
    var stageVariables: Map<String, String>? = null
    var requestContext: RequestContext? = null
    var body: Body? = null
    var isBase64Encoded: Boolean = false
    var source: String? = null

    companion object {
        const val SOURCE_HEATER = "heater"
    }

    fun isHeatingRequest(): Boolean {
        return SOURCE_HEATER.equals(source)
    }
}
