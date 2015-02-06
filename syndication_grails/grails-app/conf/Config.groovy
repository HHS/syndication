/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import org.springframework.core.io.ClassPathResource

def userHome = userHome ?: System.getProperty("user.home")
grails.config.locations = ["file:${userHome}/syndicationConfig.groovy", "file:${userHome}/syndicationSharedConfig.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = "com.ctacorp.syndication.api" // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}

grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.serverURL = "http://localhost:8080/Syndication"
    }
    test {
        grails.serverURL = "http://localhost:8080/Syndication"
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://syndication.hhs.gov"
    }
}

//log4j configuration
new File("${userHome}/syndicationLogs/api").mkdirs() //Create logging dir
log4j.main = {
    appenders {
        rollingFile name:'errorFile', maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/api/errors.log", threshold: org.apache.log4j.Level.ERROR
        rollingFile name:'infoFile',  maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/api/details.log", threshold: org.apache.log4j.Level.INFO
    }

    root{
        error 'errorFile', 'infoFile', 'stdout'
    }

    info   'grails.app',
            'org.codehaus.groovy.grails.web.servlet'

    fatal  'org.hibernate.tool.hbm2ddl.SchemaExport'
}

//swaggerDefaults
swagger.apiVersion = "v2"
swagger.swaggerVersion = "1.2"
swagger.basePath = "http://localhost:8080/Syndication"
swagger.api.basePath = "http://localhost:8080/Syndication/api/v2"
swagger.info.title = "HHS Media Services API"
swagger.info.description = new ClassPathResource("syndication/api/api_info_description.html").inputStream.text
swagger.info.contact = "syndication@hhs.gov"
swagger.info.license = "GNU GENERAL PUBLIC LICENSE"
swagger.info.licenseUrl = "http://www.gnu.org/licenses/gpl.html"
swagger.info.termsOfServiceUrl = "http://www.hhs.gov/web/socialmedia/policies/tos.html#ready"

//scratch directory defaults
syndication.scratch.root = "${userHome}/.syndication"

//TTL for image data (in seconds)
syndication.preview.imagettl = 2592000    //30 days
syndication.preview.pagettl  = 2592000    //30 days
syndication.html.pagettl     = 2592000    //30 days

//preview defaults
syndication.preview.thumbnail.width     = 250
syndication.preview.thumbnail.height    = 188
syndication.preview.small.width         = 640
syndication.preview.small.height        = 480
syndication.preview.medium.width        = 1024
syndication.preview.medium.height       = 768
syndication.preview.large.width         = 2048
syndication.preview.large.height        = 1536
syndication.preview.browserWindowSize   = 640

//External image tools default locations & settings
imageMagick.location = "/usr/bin"
webkit2png.location = "/usr/bin"
xvfb.location = "/usr/bin"
cutycapt.location="/usr/bin"
//syndication.htmlRenderingEngine = "webkit2png"
//syndication.htmlRenderingEngine = "cutycapt"
syndication.htmlRenderingEngine = "cutycaptMac"

//Test data
syndication.generateTestData = false
syndication.loadExampleRealData = false

//TinyURL
tinyUrl.serverAddress = "http://localhost:8082/TinyUrl"
tinyUrl.mappingBase = "/api/v1/mediaMappings"

//TagCloud
tagCloud.serverAddress = "http://localhost:8084/TagCloud"

//CMS Manager
cmsManager.serverUrl = "http://ctacdev.com:8090/CmsManager"
cmsManager.selfAuthPath = "/api/v1/debug/status.json"
cmsManager.verifyAuthPath = "/api/v1/authorizations.json"

//Supported extraction commands
syndication.contentExtraction.supportedParams = [
    "imageFloat",
    "imageMargin",
    "crop",
    "previewSize",
    "width",
    "height",
    "browserWindowSize",
    "callback"
]

//supported image media types
syndication.contentExtraction.supportedImageTypes = [
    "Image",
    "Infographic"
]

//__________________________
// Rabbit MQ Settings       \____________________________________________________________
//_______________________________________________________________________________________
rabbitmq {

    connection = {
        connection host: 'localhost', username: 'syndication', password: 'syndication', virtualHost: '/syndication', requestedHeartbeat: 10
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
//-----------------------------------------------------------------

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName =       'com.ctacorp.syndication.authentication.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName =    'com.ctacorp.syndication.authentication.UserRole'
grails.plugin.springsecurity.authority.className =                  'com.ctacorp.syndication.authentication.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
    '/**':                            ['permitAll'],
    '/index':                         ['permitAll'],
    '/index.gsp':                     ['permitAll'],
    '/assets/**':                     ['permitAll'],
    '/**/js/**':                      ['permitAll'],
    '/**/css/**':                     ['permitAll'],
    '/**/images/**':                  ['permitAll'],
    '/**/favicon.ico':                ['permitAll']
]

syndication.contentExtraction.urlBase = "http://localhost:8080/Syndication/api/v2/resources/media"

swagger{
    api.basePath = "http://localhost:8080/Syndication/api/v2"
    apiVersion = "2"
    swaggerVersion = "1.2"
    customView = "/swagger/index"
}

grails.plugin.springsecurity.useSecurityEventListener = true

bruteforcedefender {
    time = 15
    allowedNumberOfAttempts = 5
}