/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

def userHome = userHome ?: System.getProperty("user.home")
grails.config.locations = ["file:${userHome}/syndicationConfig.groovy", "file:${userHome}/syndicationSharedConfig.groovy"]

grails.war.resources = { stagingDir, args ->
    copy(file: "MetaData.groovy", tofile: "${stagingDir}/MetaData.groovy")
}

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
    }
    production {
        grails.logging.jul.usebridge = false
    }
}

grails.serverURL = System.getenv("API_PUBLIC_URL")

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

//Swagger Description
def swaggerDescription = """
<div class="swagger-ui-wrap extraFooter"><h3>Common Features / Behaviors</h3>
    <div class="features">
        <ul>
            <li><strong>* "sort" param:</strong> supports multi column sorting through the use of commas as delimiters, and a hyphen to denote descending order.
                <br/>
                <strong><span>Examples:</span></strong>
                <ul>
                    <li><span class="example">name</span><span class="description">sort results by name ascending</span></li>
                    <li><span class="example">-name</span><span class="description">sort results by name descending</span></li>
                    <li><span class="example">-name,id</span><span class="description">sort results by name descending and then by id ascending</span></li>
                    <li><span class="example">id,-dateContentAuthored</span><span class="description">sort results by id ascending and then date descending</span></li>
                </ul>
            </li>
            <li><strong>Date formats:</strong> Date input format is expected to be based on <a href="http://www.ietf.org/rfc/rfc3339.txt">RFC 3339</a>. <br/>
                <span><strong>Example:</strong></span>
                <ul><li>2013-11-18T18:43:01Z</li></ul>
            </li>
        </ul>
    </div>
</div>
"""

//scratch directory defaults
syndication.scratch.root = "${userHome}/.syndication"

//Test data
syndication.generateTestData = false
if(System.getenv("SYNDICATION_LOAD_EXAMPLE_DATA") == "true"){
    syndication.loadExampleRealData = true
} else{
    syndication.loadExampleRealData = false
}

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

//swaggerDefaults
swagger{
    basePath = System.getenv("SWAGGER_BASE_PATH")
    api.basePath = swagger.basePath + "/api/v2"
    apiVersion = "2"
    swaggerVersion = "1.2"
    customView = "/swagger/index"
    info{
        title = "HHS Media Services API"
        description = swaggerDescription
        contact = "syndicationadmin@hhs.gov"
        license = "GNU GENERAL PUBLIC LICENSE"
        licenseUrl = "http://www.gnu.org/licenses/gpl.html"
        termsOfServiceUrl = "http://www.hhs.gov/web/socialmedia/policies/tos.html#ready"
    }
}

grails.plugin.springsecurity.useSecurityEventListener = true

syndication.contentExtraction.cssClassName = "syndicate"

bruteforcedefender {
    time = 15
    allowedNumberOfAttempts = 5
}

//AWS
grails {
    plugin {
        aws {
            s3 {
                bucket = "bucketName"
                acl = "private"
            }
            credentials {
                accessKey = "your-access-key"
                secretKey = "your-secret-key"
            }
        }
    }
}

//__________________________
// Rabbit MQ Settings       \____________________________________________________________
//_______________________________________________________________________________________
rabbitmq {

    connection = {
        connection  host: "${System.getenv('RABBIT_PORT_5672_TCP_ADDR')}",
                username: "${System.getenv('RABBIT_ENV_RABBITMQ_DEFAULT_USER')}",
                password: "${System.getenv('RABBIT_ENV_RABBITMQ_DEFAULT_PASS')}",
                virtualHost: "${System.getenv('RABBITMQ_VIRTUAL_HOST') ?: '/'}",
                requestedHeartbeat: 10
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

//_________________________________________
// Default passwords and Auth settings     \_____________________________________________
//_______________________________________________________________________________________
springsecurity {
    syndication{
        adminUsername = System.getenv('ADMIN_USERNAME')
        initialAdminPassword = System.getenv('ADMIN_PASSWORD')
    }
}

//_____________________
// Youtube API Keys    \_________________________________________________________________
//_______________________________________________________________________________________
google.youtube.apiKey = System.getenv("YOUTUBE_API_KEY")

if(System.getenv("USING_DOCKER") == "true") {
    syndication.solrService.serverAddress = "http" + (System.getenv("SOLR_PORT_8983_TCP") - "tcp") + "/solr/syndication"
}

manet{
    server.url = System.getenv("MANET_PORT_8891_TCP")?.replace("tcp://", "http://")
}

//CMS Manager
cmsManager{
    serverUrl = System.getenv("CMS_PUBLIC_URL")
    privateKey = System.getenv("CMS_PRIVATE_KEY")
    publicKey = System.getenv("CMS_PUBLIC_KEY")
    secret = System.getenv("CMS_SECRET")
    cmsManager.selfAuthPath = "/api/v1/debug/status.json"
    cmsManager.verifyAuthPath = "/api/v1/authorizations.json"
}

disableGuavaCache = System.getenv("DISABLE_GUAVA_CACHE")


//TinyURL
tinyUrl.serverAddress = System.getenv("TINY_PUBLIC_URL")
tinyUrl.mappingBase = "/api/v1/mediaMappings"

//TagCloud
tagCloud.serverAddress = System.getenv("TAG_PUBLIC_URL")

syndication{
    internalAuthHeader = System.getenv("AUTHORIZATION_HEADER")
}