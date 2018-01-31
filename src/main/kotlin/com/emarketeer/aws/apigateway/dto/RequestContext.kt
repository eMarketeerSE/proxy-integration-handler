package com.emarketeer.consent.dto

import com.emarketeer.aws.apigateway.dto.Identity

class RequestContext() {

    var accountId: String? = null
    var resourceId: String? = null
    var stage: String? = null
    var requestId: String? = null
    var identity: Identity? = null
    var resourcePath: String? = null
    var httpMethod: String? = null
    var apiId: String? = null

}