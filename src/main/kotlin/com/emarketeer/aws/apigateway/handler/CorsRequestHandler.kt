package com.emarketeer.aws.apigateway.handler

import com.emarketeer.aws.apigateway.dto.ApiGatewayResponse

abstract class CorsRequestHandler<Body, Query> : ProxyRequestHandler<Body, Query>() {
    open fun corsHeaders():Map<String, String> = mapOf(
            "access-control-allow-methods" to "GET, POST, PUT, DELETE, OPTIONS",
            "access-control-allow-origin" to "*",
            "access-control-allow-headers" to "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token,X-Amz-User-Agent",
            "access-control-allow-credentials" to "true"
    )

    override fun ok(responseBody: String, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.OK(responseBody, corsHeaders().plus(headers))
    override fun ok(responseBody: Any?, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.OK(gson.toJson(responseBody), corsHeaders().plus(headers))
    override fun error(responseBody: Any?, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.ERROR(gson.toJson(responseBody), corsHeaders().plus(headers))
    override fun badRequest(responseBody: Any?, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.BAD_REQUEST(gson.toJson(responseBody), corsHeaders().plus(headers))
}
