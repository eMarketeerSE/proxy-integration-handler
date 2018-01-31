package com.emarketeer.aws.apigateway.dto

class Identity() {
    var cognitoIdentityPoolId: String? = null
    var accountId: String? = null
    var cognitoIdentityId: String? = null
    var caller: String? = null
    var apiKey: String? = null
    var sourceIp: String? = null
    var cognitoAuthenticationType: String? = null
    var cognitoAuthenticationProvider: String? = null
    var userArn: String? = null
    var userAgent: String? = null
    var user: String? = null
}