/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

userHome = System.getProperty("user.home")
grails.config.locations = ["file:${userHome}/syndicationSharedConfig.groovy", "file:${userHome}/syndicationAdminConfig.groovy"]
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = "com.ctacorp.syndication.admin" // change this to alter the default package name and Maven publishing destination

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

//Security Fix for 2.x apps
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.serverURL = "http://localhost:8086/SyndicationAdmin"
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://localhost:8086/SyndicationAdmin"
    }
}

//log4j configuration
new File("${userHome}/syndicationLogs/admin").mkdirs() //Create logging dir
log4j.main = {
    appenders {
        rollingFile name:'errorFile', maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/admin/errors.log", threshold: org.apache.log4j.Level.ERROR
        rollingFile name:'infoFile',  maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/admin/details.log", threshold: org.apache.log4j.Level.INFO
    }

    root{
        error 'errorFile', 'infoFile', 'stdout'
    }

    info   'grails.app',
           'org.codehaus.groovy.grails.web.servlet'

    fatal  'org.hibernate.tool.hbm2ddl.SchemaExport'
}

//MQ

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
                'x-message-ttl' : 1*60*1000,
                'x-dead-letter-exchange' : 'errorExchange',
                'x-dead-letter-routing-key' : 'emailError'
        ]

        queue name: "restErrorDelayQueue", durable: true, arguments: [
                'x-message-ttl' : 1*60*1000,
                'x-dead-letter-exchange' : 'errorExchange',
                'x-dead-letter-routing-key' : 'restError'
        ]

        queue name: "rhythmyxErrorDelayQueue", durable: true, arguments: [
                'x-message-ttl' : 1*60*1000,
                'x-dead-letter-exchange' : 'errorExchange',
                'x-dead-letter-routing-key' : 'rhythmyxError'
        ]
    }
}

syndication.mq.updateExchangeName = "updateExchange"
//-----------------------------------------------------------------

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.ctacorp.syndication.authentication.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.ctacorp.syndication.authentication.UserRole'
grails.plugin.springsecurity.authority.className = 'com.ctacorp.syndication.authentication.Role'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/dbconsole/**':                  ['permitAll'],
        '/':                              ['permitAll'],
        '/assets/**':                     ['permitAll'],
        '/**/js/**':                      ['permitAll'],
        '/greenmail/**':                  ['permitAll'],
        '/**/css/**':                     ['permitAll'],
        '/**/images/**':                  ['permitAll'],
        '/**/favicon.ico':                ['permitAll']
]

grails.plugin.springsecurity.useSecurityEventListener = true
bruteforcedefender {
    time = 15
    allowedNumberOfAttempts = 5
}

syndication.apiPath = "/api/v2"
grails.assets.minifyJs = false

//___________________________
// Greenmail                 \____________________________________________________________
//_______________________________________________________________________________________

grails.mail.port = 3026
greenmail.ports.smtp = 3026

//Content extraction
syndication.contentExtraction.cssClassName = "syndicate"
syndication.scratch.root = "${System.getProperty('user.home')}/.syndication"