package com.emarketeer.aws.apigateway.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.xray.AWSXRayRecorder
import com.amazonaws.xray.AWSXRayRecorderBuilder
import com.amazonaws.xray.contexts.SegmentContextResolverChain
import com.amazonaws.xray.contexts.ThreadLocalSegmentContextResolver
import com.amazonaws.xray.entities.Entity
import com.amazonaws.xray.entities.Segment
import com.emarketeer.aws.apigateway.adapter.LocalDateTimeTypeAdapter
import com.emarketeer.aws.apigateway.adapter.LocalDateTypeAdapter
import com.emarketeer.aws.apigateway.dto.ApiError
import com.emarketeer.aws.apigateway.dto.ApiGatewayRequest
import com.emarketeer.aws.apigateway.dto.ApiGatewayResponse
import com.emarketeer.aws.apigateway.dto.ProxyGatewayRequest
import com.google.gson.GsonBuilder
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime


abstract class ProxyRequestHandler<Body, Query>() : RequestHandler<ApiGatewayRequest<String, Any>, ApiGatewayResponse> {
    val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .create()

    override fun handleRequest(input: ApiGatewayRequest<String, Any>?, context: Context?): ApiGatewayResponse {
        val xrayRecorder = AWSXRayRecorderBuilder.defaultRecorder()

        val segment = xrayRecorder.beginSegment(this.javaClass.simpleName)

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

            this.handle(parsedRequest, context, segment)
        } catch (e: Exception) {
            segment.addException(e)
            segment.isError = true
            log.error("Internal Server Error", e)
            error(ApiError(e.message, e.message))
        } finally {
            xrayRecorder.endSegment()
        }
    }

    open fun ok(): ApiGatewayResponse = ok("")
    open fun ok(responseBody: String): ApiGatewayResponse = ok(responseBody, emptyMap())
    open fun ok(responseBody: String, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.OK(responseBody, headers)
    open fun ok(responseBody: Any?): ApiGatewayResponse = ok(responseBody, emptyMap())
    open fun ok(responseBody: Any?, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.OK(gson.toJson(responseBody), headers)
    open fun error(responseBody: Any?): ApiGatewayResponse = error(responseBody, emptyMap())
    open fun error(responseBody: Any?, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.ERROR(gson.toJson(responseBody), headers)
    open fun badRequest(responseBody: Any?): ApiGatewayResponse = badRequest(responseBody, emptyMap())
    open fun badRequest(responseBody: Any?, headers: Map<String, String>): ApiGatewayResponse = ApiGatewayResponse.BAD_REQUEST(gson.toJson(responseBody), headers)

    open fun threadLocalRecorder(traceEntity: Entity): AWSXRayRecorder {
        val resolver = SegmentContextResolverChain()
        resolver.addResolver(ThreadLocalSegmentContextResolver())

        val xrayRecorder = AWSXRayRecorderBuilder.standard().withSegmentContextResolverChain(resolver).build()

        xrayRecorder.traceEntity = traceEntity

        return xrayRecorder
    }

    @Throws(Exception::class)
    abstract fun handle(request: ProxyGatewayRequest<Body, Query>, context: Context?, segment: Segment): ApiGatewayResponse

    abstract fun bodyType(): Type

    abstract fun queryStringType(): Type

    companion object {
        val log = LogManager.getLogger("GenericRequestHandler")
    }
}
