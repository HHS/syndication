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
            return authorizationService.getRest(config.cmsManager.serverUrl + "/api/v1/subscriber.json?id=${subscriberId}")
        } catch (e) {
            log.error("Can't connect to CMS Manager!")
            return []
        }
    }

    def listSubscribers(){
        def resp
        try{
            resp = authorizationService.getRest(config.cmsManager.serverUrl + "/api/v1/subscribers.json?sort=name")
            def validSubscribers = resp.findAll{ subscriber ->
                subscriber.id as String != "null"
            }
            return validSubscribers
        } catch (e) {
            log.error("CMS Manager is either unavailable, or the keys are invalid. Response was: ${resp}")
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log.error sw.toString();

            return []
        }
    }
}