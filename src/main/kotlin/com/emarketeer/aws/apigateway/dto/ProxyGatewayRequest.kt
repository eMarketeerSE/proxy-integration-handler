package com.emarketeer.aws.apigateway.dto

import com.emarketeer.consent.dto.RequestContext

class ProxyGatewayRequest<Body, Query>(
        val resource: String?,
        val path: String?,
        val httpMethod: String?,
        val headers: Map<String, String>?,
        val pathParameters: Map<String, String>?,
        val stageVariables: Map<String, String>?,
        val requestContext: RequestContext?,
        val isBase64Encoded: Boolean,
        val body: Body?,
        val queryStringParameters: Query?
)