package syndication.cmsmanager

/**
 * Created by nburk on 12/23/14.
 */
class CmsManagerService {

    def grailsApplication
    def authorizationService

    def getSubscriber(def publicKey){
        authorizationService.getRest(grailsApplication.config.cmsManager.serverUrl + "/api/v1/subscriber.json?publicKey=${publicKey}")
    }

    def listSubscribers(){
        try{
            authorizationService.getRest(grailsApplication.config.cmsManager.serverUrl + "/api/v1/subscribers.json")

        } catch (e) {
            log.error("Can't connect to CMS Manager!")
            return []
        }
    }
}
