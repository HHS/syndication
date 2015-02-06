/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.rest

class SubscriptionManagementController {

    def restSubscriptionManagementService

    static allowedMethods = [subscribe: "PUT", deleteSubscription: "DELETE"]

    def deleteSubscription(Long id) {

        String senderPublicKey = request.getAttribute('senderPublicKey')

        def result = restSubscriptionManagementService.deleteSubscription(id, senderPublicKey)

        renderResponse(result, null)
    }

    def getSubscription(Long id) {

        String senderPublicKey = request.getAttribute('senderPublicKey')
        def callback = params.callback

        def result = restSubscriptionManagementService.getSubscription(id, senderPublicKey)

        renderResponse(result, callback)
    }

    def getAllSubscriptions() {

        String senderPublicKey = request.getAttribute('senderPublicKey')
        def callback = params.callback

        def result = restSubscriptionManagementService.getAllSubscriptions(senderPublicKey)

        renderResponse(result, callback)
    }

    def subscribe() {

        String mediaId = params.media_id
        String sourceUrl = params.source_url
        boolean notificationOnly = Boolean.parseBoolean(params.notification_only as String)
        String senderPublicKey = request.getAttribute('senderPublicKey')
        def callback = params.callback

        def result = restSubscriptionManagementService.createSubscription(mediaId, sourceUrl, notificationOnly, senderPublicKey)

        renderResponse(result, callback)
    }

    private void renderResponse(result, callback) {

        def renderParams = {

            if (result.status == 204) {
                return [status: 204]
            }

            def params = [status: result.status, text: result.message, encoding: "UTF-8", contentType: "application/json"]
            if (callback) {
                params.text = "${callback}(${params.text});"
            }

            return params
        }()

        render renderParams
    }
}
