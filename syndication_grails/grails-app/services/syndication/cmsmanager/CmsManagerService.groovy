package syndication.cmsmanager

/**
 * Created by nburk on 12/23/14.
 */
class CmsManagerService {

    def grailsApplication
    def authorizationService

    def getSubscriber(def publicKey){
        try{
            authorizationService.getRest(grailsApplication.config.cmsManager.serverUrl + "/api/v1/subscriber.json?publicKey=${publicKey}")
        } catch (e) {
            log.error("Can't connect to CMS Manager!")
            log.error(e)
            return []
        }
    }

    def listSubscribers(){
        try{
            authorizationService.getRest(grailsApplication.config.cmsManager.serverUrl + "/api/v1/subscribers.json")

        } catch (e) {
            log.error("Can't connect to CMS Manager!")
            log.error(e)
            return []
        }
    }
}
