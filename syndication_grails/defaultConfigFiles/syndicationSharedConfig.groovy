//___________________________________
// Server paths for various apps     \___________________________________________________
//_______________________________________________________________________________________
syndication.serverUrl = "http://localhost:8080/Syndication"
syndication.apiPath = "/api/v2"
syndication.swaggerAddress = "${syndication.serverUrl}"

swagger{
    api.basePath = "http://localhost:8080/Syndication/api/v2"
    apiVersion = "2"
    swaggerVersion = "1.2"
    customView = "/swagger/index"
}

tagCloud.serverAddress = "http://localhost:8084/TagCloud"
storefront.serverAddress = "http://localhost:8084/SyndicationStorefront"

cmsManager.serverUrl = "http://localhost:9090/CmsManager"
cmsManager.selfAuthPath = "/api/v1/debug/status.json"
cmsManager.verifyAuthPath = "/api/v1/authorizations.json"

tinyUrl.serverAddress = "http://localhost:8098/TinyUrl"
tinyUrl.mappingBase = "/api/v1/mediaMappings"

//______________________
// CMS Manager Keys     \________________________________________________________________
//_______________________________________________________________________________________
cmsManager.publicKey = ""
cmsManager.privateKey = ""
cmsManager.secret = ""

//_________________________________________
// Default passwords and Auth settings     \_____________________________________________
//_______________________________________________________________________________________
springsecurity {

    cmsManager {
      adminUsername = "admin@example.com"
      defaultPassword = "password"
    }

    tinyUrl{
      adminUsername = "admin@example.com"
      initialAdminPassword = "password"
    }

    tagCloud{
      adminUsername   = "admin@example.com"
      initialAdminPassword = "password"
    }

    syndicationAdmin {
      adminUsername = "admin@example.com"
      initialAdminPassword = "password"
    }

    syndication {
      adminUsername = "admin@example.com"
      initialAdminPassword = "password"
    }
}

//___________________________
// Global Email Settings     \___________________________________________________________
//_______________________________________________________________________________________
grails{
    mail {
        host = "localhost"
        port = 25
    }
}

grails.mail.default.from = "syndicationAdmin@hhs.gov"
SyndicationAdmin.healthReportEmailAddresses = ["syndication@ctacorp.com"]

//__________________________
// Rabbit MQ Settings       \____________________________________________________________
//_______________________________________________________________________________________
rabbitmq {

    connection = {
        connection host: 'localhost', username: 'mqUsername', password: 'mqPassword', virtualHost: '/syndication', requestedHeartbeat: 10
    }

    queues = {

        exchange name: "updateExchange", type: "fanout", {
            queue name: "emailUpdateQueue", durable: true
            queue name: "restUpdateQueue", durable: true
            queue name: "rhythmyxUpdateQueue", durable: true
        }

        exchange name: "errorExchange", type: "direct", {
            queue name: "emailErrorQueue", durable: true, binding: "emailError"
            queue name: "restErrorQueue", durable: true, binding: "restError"
            queue name: "rhythmyxErrorQueue", durable: true, binding: "rhythmyxError"
        }

        queue name: "emailErrorDelayQueue", durable: true, arguments: [
                'x-message-ttl' : 60*60*1000,
                'x-dead-letter-exchange' : 'errorExchange',
                'x-dead-letter-routing-key' : 'emailError'
        ]

        queue name: "restErrorDelayQueue", durable: true, arguments: [
                'x-message-ttl' : 60*60*1000,
                'x-dead-letter-exchange' : 'errorExchange',
                'x-dead-letter-routing-key' : 'restError'
        ]

        queue name: "rhythmyxErrorDelayQueue", durable: true, arguments: [
                'x-message-ttl' : 60*60*1000,
                'x-dead-letter-exchange' : 'errorExchange',
                'x-dead-letter-routing-key' : 'rhythmyxError'
        ]
    }
}

syndication.mq.updateExchangeName = "updateExchange"

errorQueueRetryPolicy.maxAttempts = 5

//_________________________
// Syndication Importer    \_____________________________________________________________
//_______________________________________________________________________________________
syndication.htmlPublishPath =   "/api/v2/resources/media/htmls"
syndication.videoPublishPath =  "/api/v2/resources/media/videos"
syndication.imagePublishPath =  "/api/v2/resources/media/images"
syndication.sourcesPath =       "/api/v2/resources/sources"
syndication.languagesPath =     "/api/v2/resources/languages"
syndication.dataLocation =      "${System.getProperty('user.home')}/dataDump.json"

//________________________________
// Syndication Grails (The API)   \______________________________________________________
//_______________________________________________________________________________________
syndication.htmlRenderingEngine = "cutycapt"
imageMagick.location = "/usr/local/bin"
cutycapt.location = "/usr/local/bin"
syndication.scratch.root = "${System.getProperty('user.home')}/.syndication"
syndication.contentExtraction.cssClassName = "syndicate"

//_____________________
// Syndication Admin   \_________________________________________________________________
//_______________________________________________________________________________________
google.analytics.profileId = "Your profile id"

//___________________________
// Syndication Solr Search   \___________________________________________________________
//_______________________________________________________________________________________
syndication.solrService.serverAddress = "http://localhost:8983/solr/syndication"
syndication.solrService.useSolr = true

//________________________
// Facebook Keys          \______________________________________________________________
//_______________________________________________________________________________________
// facebook client_id & client_secret
// syndication.facebookAppId = ""
// syndication.facebookSecret = ""

//________________________
// Twitter OAuth Keys     \______________________________________________________________
//_______________________________________________________________________________________
// syndication.twitterConsumerKey = ""
// syndication.twitterConsumerSecret = ""
// syndication.twitterAccessToken = ""
// syndication.twitterAccessTokenSecret = ""

//________________________
// S3 Keys                \______________________________________________________________
//_______________________________________________________________________________________
// amazon.s3.accessKey = ""
// amazon.s3.secretKey = ""
// amazon.s3.syndBucket = "synd3"

//________________________________
// Testing and Debug settings     \______________________________________________________
//
// | Don"t use any of these in production!
//_______________________________________________________________________________________

// tagCloud.generateTestData = true
// tinyUrl.generateTestData = true
// syndication.loadExampleRealData = true
// tagCloud.skipAuthorization = true
// syndication.cms.auth.enabled = true
// cmsManager.createTestDataInProductionMode = true
