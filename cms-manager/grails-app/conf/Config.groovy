/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
def userHome = System.getProperty('user.home')
externalConfig="${userHome}/cmsManagerConfig.groovy"
sharedConfig="${userHome}/syndicationSharedConfig.groovy"

grails.config.locations = ["file:${externalConfig}", "file:${sharedConfig}"]
grails.project.groupId = "com.ctacorp.syndication"

grails.war.resources = { stagingDir, args ->
    copy(file: "MetaData.groovy", tofile: "${stagingDir}/MetaData.groovy")
}

//________________________________________
// MIME TYPES                             \______________________________________________
//_______________________________________________________________________________________

grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']

grails.mime.types = [
        all: '*/*',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        form: 'application/x-www-form-urlencoded',
        html: ['text/html', 'application/xhtml+xml'],
        js: 'text/javascript',
        json: ['application/json', 'text/json'],
        multipartForm: 'multipart/form-data',
        rss: 'application/rss+xml',
        text: 'text/plain',
        hal: ['application/hal+json', 'application/hal+xml'],
        xml: ['text/xml', 'application/xml']
]

//________________________________________
// GSP                                    \______________________________________________
//_______________________________________________________________________________________

grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml'
            codecs {
                expression = 'html'
                scriptlet = 'html'
                taglib = 'none'
                staticparts = 'none'
            }
        }
    }
}

//________________________________________
// GSP                                    \______________________________________________
//_______________________________________________________________________________________

grails.converters.encoding = 'UTF-8'
grails.scaffolding.templates.domainSuffix = 'Instance'
grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart = false
grails.exceptionresolver.params.exclude = ['password']
grails.hibernate.cache.queries = false
grails.urlmapping.cache.maxsize = 5000
grails.controllers.defaultScope = 'singleton'
grails.views.default.codec = 'html'
grails.assets.less.compile = 'less4j'
grails.assets.plugin."twitter-bootstrap".excludes = ["**/*.less"]
grails.assets.plugin."twitter-bootstrap".includes = ["bootstrap.less"]

grails {
    filteringCodecForContentType {
    }
}

grails.serverURL = "http://localhost:8080/${appName}"

//________________________________________
// SPRING SECURITY                        \______________________________________________
//_______________________________________________________________________________________

grails.plugin.springsecurity.useSecurityEventListener = true
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.ctacorp.syndication.manager.cms.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.ctacorp.syndication.manager.cms.UserRole'
grails.plugin.springsecurity.authority.className = 'com.ctacorp.syndication.manager.cms.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/**':              ['permitAll'],
        '/api/**':          ['permitAll'],
        '/index':           ['permitAll'],
        '/index.gsp':       ['permitAll'],
        '/assets/**':       ['permitAll'],
        '/greenmail/**':    ['permitAll'],
        '/**/js/**':        ['permitAll'],
        '/**/css/**':       ['permitAll'],
        '/**/images/**':    ['permitAll'],
        '/**/favicon.ico':  ['permitAll'],
        '/login/**':        ['permitAll'],
        '/logout/**':       ['permitAll']
]

bruteforcedefender {
    time = 15
    allowedNumberOfAttempts = 5
}

springsecurity {
    cmsManager {
        account.help.email = "syndicationadmin@hhs.gov"
    }
}

//________________________________________
// TEST PROPERTIES                        \______________________________________________
//_______________________________________________________________________________________

test {
    subscriber {
        email.address = 'syndication@ctacorp.com'
        keyAgreement {
            entity1 = 'cmsmanager.syndication.hhs.gov'
            entity2 = 'internal.syndication.hhs.gov'
            entity1PublicKey = 'mmpJ+xLlpSjjACMOT7BPy3aQdVOtX31iyBKk0ZoNdv2GTzLx5in74TEYu1UuNn/hRtwNbTtzceEiWE391n+3jg=='
            entity1PrivateKey = 'bRvubHYL0nO4VZ2dLV+5y9uL0ZVTRXdsalMCB6vmxQlAEdovUYc7IOciN04ITT00SjKhz2FfIloxErY15XccOQ=='
            entity2PublicKey = '9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bg=='
            entity2PrivateKey = 'UxMt4OpdAZhJFMOF/kmv/lZAYXjE4hV8EI9UdmQP71J9VbbIvmR0DEhX2D3He7AKTq1IQz4tAqDX+Jy1Svxlqw=='
            secret = 'xjY3i4AnsZ9wWuDKboD1XbAdtX1hgOh2tYMnwCWnXhweO94IKrbVJuPZIQsyO5Sa40CjAMF9tG5ciI+cXITjVw=='
            prime = '9sXU4jNSYQ954vTLuA+dD3rinMXQjBTgHhyCyfqhWVBfUEcJOc8vJGXNVmWH9JRvc2SpfDC0yXRL8DxZ5KiHIw=='
            generator = 'Ag=='
        }
    }
    rhythmyxSubscriber {
        instanceName = 'tobacco'
        rhythmyxHost = '127.0.0.1'
        rhythmyxPort = '3000'
        rhythmyxUser = 'rsrw_man'
        rhythmyxPassword = 'rsrw_pass'
        rhythmyxCommunity = 'rsrw_community'
        subscriber = test.subscriber.keyAgreement.entity2
    }
    emailSubscriber {
        email = 'syndication@ctacorp.com'
    }
    restSubscribers = [
        [
            deliveryEndpoint: 'http://127.0.0.1:3000/delivery/endpoint/410',
            subscriber: test.subscriber.keyAgreement.entity2
        ],
        [
            deliveryEndpoint: 'http://127.0.0.1:3000/delivery/endpoint/400',
            subscriber: test.subscriber.keyAgreement.entity2
        ],
        [
            deliveryEndpoint: 'http://127.0.0.1:3000/delivery/endpoint/200',
            subscriber: test.subscriber.keyAgreement.entity2
        ]
    ]
}

cmsManager.createTestDataInProductionMode = false

//________________________________________
// LOGGING                                \______________________________________________
//_______________________________________________________________________________________

import org.apache.log4j.Level

new File("${userHome}/syndicationLogs/api").mkdirs()

log4j.main = {
    appenders {
        rollingFile name:'errorFile', maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/cms/errors.log", threshold: Level.WARN
        rollingFile name:'infoFile',  maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/cms/details.log", threshold: Level.INFO
    }

    root{
        error 'errorFile', 'infoFile', 'stdout'
    }

    info  'grails.app',
          'org.codehaus.groovy.grails.web.servlet'

    fatal  'org.hibernate.tool.hbm2ddl.SchemaExport'

    error stdout: 'com.sun.jersey'
}

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
    }
}

//___________________________
// Rabbit MQ Settings        \____________________________________________________________
//________________________________________________________________________________________

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

errorQueueRetryPolicy.maxAttempts = 6

//___________________________
// API Key Settings          \____________________________________________________________
//_______________________________________________________________________________________

apiKey {
    filteredUri = '/api/v1/**'
    headerName = 'Authorization'
    keyName = 'syndication_api_key'
    entities {
        cmsManager = 'cmsmanager.syndication.hhs.gov'
        syndication = 'syndication.hhs.gov'
    }
    includeAuthorizationFailureDetails = false
}

//___________________________
// Greenmail                 \____________________________________________________________
//_______________________________________________________________________________________

import com.icegreen.greenmail.util.ServerSetupTest

grails.mail.port = ServerSetupTest.SMTP.port

//___________________________
// Syndication               \____________________________________________________________
//_______________________________________________________________________________________

syndication.thumbnailPath = "/resources/media/{id}/thumbnail.jpg"
syndication.mediaPath = "/resources/media/{id}.json"