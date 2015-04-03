package com.ctacorp.syndication

/**
 * Created by nburk on 11/26/14.
 */
class CmsManagerKeyService {

    def grailsApplication
    def authorizationService

    def getSubscriberById(subscriberId){
        try{
            authorizationService.getRest(grailsApplication.config.cmsManager.serverUrl + "/api/v1/subscriber.json?id=${subscriberId}")
        } catch (e) {
            log.error("Can't connect to CMS Manager!")
            return []
        }
    }

    def listSubscribers(){
        try{
            def resp = authorizationService.getRest(grailsApplication.config.cmsManager.serverUrl + "/api/v1/subscribers.json")
            def validSubscribers = resp.findAll{ subscriber ->
                subscriber.id as String != "null"
            }
            return validSubscribers
        } catch (e) {
            log.error("CMS Manager is either unavailable, or the keys are invalid")
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log.error sw.toString();

            return []
        }
    }
}