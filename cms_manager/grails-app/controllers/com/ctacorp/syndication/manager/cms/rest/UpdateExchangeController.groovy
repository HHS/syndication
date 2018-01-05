package com.ctacorp.syndication.manager.cms.rest

import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import grails.converters.JSON

class UpdateExchangeController {

    def emailSubscriptionConsumerService
    def restSubscriptionConsumerService
    def rhythmyxSubscriptionConsumerService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def emailUpdate() {
        def message = [messageType:request.JSON?.messageType?.name,mediaId:request.JSON.mediaId,subscriptionId:request.JSON.subscriptionId, meta:[attempts:request.JSON?.meta?.attempts]]
        MqUtils.handleMessage(message, "emailUpdateQueue", emailSubscriptionConsumerService)
        def resp = [message:"emailUpdated"]
        respond resp

    }

    def emailError() {
        def message = [messageType:request.JSON?.messageType?.name,mediaId:request.JSON.mediaId,subscriptionId:request.JSON.subscriptionId, meta:[attempts:request.JSON?.meta?.attempts]]
        MqUtils.handleMessage(message, "emailUpdateQueue", emailSubscriptionConsumerService)
        def resp = [message:"emailError Captured"]
        respond resp
    }

    def rhythmyxUpdate() {
        def message = [messageType:request.JSON?.messageType?.name,mediaId:request.JSON.mediaId,subscriptionId:request.JSON.subscriptionId, meta:[attempts:request.JSON?.meta?.attempts]]
        MqUtils.handleMessage(message, "rhythmyxUpdateQueue", rhythmyxSubscriptionConsumerService)
        def resp = [message:"rhythmyxUpdated"]
        respond resp
    }

    def rhythmyxError() {
        def message = [messageType:request.JSON?.messageType?.name,mediaId:request?.JSON?.mediaId,subscriptionId:request?.JSON?.subscriptionId, meta:[attempts:request?.JSON?.meta?.attempts]]
        MqUtils.handleMessage(message, "rhythmyxErrorQueue", rhythmyxSubscriptionConsumerService)
        def resp = [message:"rhythmyxError Captured"]
        respond resp
    }

    def restUpdate() {
        def message = [messageType:request.JSON?.messageType?.name,mediaId:request?.JSON?.mediaId,subscriptionId:request.JSON.subscriptionId, meta:[attempts:request.JSON?.meta?.attempts]]
        MqUtils.handleMessage(message, "restUpdateQueue", restSubscriptionConsumerService)
        def resp = [message:"restUpdated"]
        respond resp
    }

    def restError() {
        def message = [messageType:request.JSON?.messageType?.name,mediaId:request.JSON.mediaId,subscriptionId:request.JSON.subscriptionId, meta:[attempts:request.JSON?.meta?.attempts]]
        MqUtils.handleMessage(message, "restErrorQueue", restSubscriptionConsumerService)
        def resp = [message:"restError Captured"]
        respond resp
    }
}
