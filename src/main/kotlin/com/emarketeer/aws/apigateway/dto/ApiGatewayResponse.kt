package com.emarketeer.aws.apigateway.dto


class ApiGatewayResponse(val statusCode: Int, val headers: Map<String, String>?, val body: String?) {
    constructor(statusCode: Int, body: String?) : this(statusCode, null, body)

    companion object {
        fun OK(body: String?): ApiGatewayResponse = ApiGatewayResponse(200, body)
        fun OK(body: String?, headers: Map<String, String>?): ApiGatewayResponse = ApiGatewayResponse(200, headers, body)
        fun ERROR(body: String?): ApiGatewayResponse = ApiGatewayResponse(500, body)
        fun ERROR(body: String?, headers: Map<String, String>?): ApiGatewayResponse = ApiGatewayResponse(500, headers, body)
        fun BAD_REQUEST(body: String?): ApiGatewayResponse = ApiGatewayResponse(400, body)
        fun BAD_REQUEST(body: String?, headers: Map<String, String>?): ApiGatewayResponse = ApiGatewayResponse(400, headers, body)
    }
}
