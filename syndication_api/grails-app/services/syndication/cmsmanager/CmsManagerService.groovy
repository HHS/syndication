package syndication.cmsmanager

import grails.util.Holders

/**
 * Created by nburk on 12/23/14.
 */
class CmsManagerService {

    def grailsApplication
    def authorizationService

    def getSubscriber(def publicKey){
        try{
            return authorizationService.getRest(Holders.config.CMSMANAGER_SERVER_URL + "/api/v1/subscriber.json?publicKey=${publicKey}")
        } catch (e) {
            log.error("Can't connect to CMS Manager!", e)
            return []
        }
    }

    def listSubscribers(){
        try{
            authorizationService.getRest(Holders.config.CMSMANAGER_SERVER_URL + "/api/v1/subscribers.json")

        } catch (e) {
            log.error("Can't connect to CMS Manager!", e)
            return []
        }
    }
}
