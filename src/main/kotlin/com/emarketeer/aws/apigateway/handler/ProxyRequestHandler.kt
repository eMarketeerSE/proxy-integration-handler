package com.emarketeer.aws.apigateway.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.emarketeer.aws.apigateway.dto.ApiError
import com.emarketeer.aws.apigateway.dto.ApiGatewayRequest
import com.emarketeer.aws.apigateway.dto.ApiGatewayResponse
import com.emarketeer.aws.apigateway.dto.ProxyGatewayRequest
import com.emarketeer.aws.apigateway.adapter.LocalDateTimeTypeAdapter
import com.emarketeer.aws.apigateway.adapter.LocalDateTypeAdapter
import com.google.gson.GsonBuilder
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

abstract class ProxyRequestHandler<Body, Query> : RequestHandler<ApiGatewayRequest<String, Any>, ApiGatewayResponse> {
    val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .create()

    override fun handleRequest(input: ApiGatewayRequest<String, Any>?, context: Context?): ApiGatewayResponse {
        return try {
            // TODO: support Void type

            log.info("Request body: ${gson.toJson(input?.body)}")

            val body = input?.body?.let { gson.fromJson<Body>(it, bodyType()) }

            val queryStringParameters = gson.fromJson<Query>(gson.toJson(input?.queryStringParameters), queryStringType())

            val parsedRequest: ProxyGatewayRequest<Body, Query> = ProxyGatewayRequest(
                    input!!.resource,
                    input.path,
                    input.httpMethod,
                    input.headers,
                    input.pathParameters,
                    input.stageVariables,
                    input.requestContext,
                    input.isBase64Encoded,
                    body,
                    queryStringParameters
            )

            this.handle(parsedRequest, context)
        } catch (e: Exception) {
            log.error("Internal Server Error", e)
            error(ApiError(e.message, e.message))
        }
    }

    fun ok(responseBody: String): ApiGatewayResponse = ApiGatewayResponse.OK(responseBody)
    fun ok(responseBody: Any?): ApiGatewayResponse = ApiGatewayResponse.OK(gson.toJson(responseBody))
    fun error(responseBody: Any?): ApiGatewayResponse = ApiGatewayResponse.ERROR(gson.toJson(responseBody))
    fun badRequest(responseBody: Any?): ApiGatewayResponse = ApiGatewayResponse.BAD_REQUEST(gson.toJson(responseBody))

    @Throws(Exception::class)
    abstract fun handle(request: ProxyGatewayRequest<Body, Query>, context: Context?): ApiGatewayResponse

    abstract fun bodyType(): Type

    abstract fun queryStringType(): Type

    companion object {
        val log = LogManager.getLogger("GenericRequestHandler")
    }
}