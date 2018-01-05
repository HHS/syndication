package com.ctacorp.syndication

import grails.util.Holders

/**
 * Created by nburk on 11/26/14.
 */
class CmsManagerKeyService {

    def authorizationService
    def config = Holders.config

    def getSubscriberById(subscriberId){
        try{
            return authorizationService.getRest("${config?.CMSMANAGER_SERVER_URL}/api/v1/subscriber.json?id=${subscriberId}")
        } catch (e) {
            log.error("Can't connect to CMS Manager!", e)
            return []
        }
    }

    def listSubscribers(){

        def resp = null

        try{

            resp = authorizationService.getRest("${config?.CMSMANAGER_SERVER_URL}/api/v1/subscribers.json?sort=name")
            return resp.findAll { resp?.id != null && resp.id != 'null'}

        } catch (e) {

            log.error("CMS Manager is either unavailable, or the keys are invalid", e)
            e.printStackTrace(new PrintWriter(new StringWriter()))
            log.error new StringWriter().toString()

            return []
        }
    }
}