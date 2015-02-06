// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

grails.config.locations = [ "file:${userHome}/syndicationSharedConfig.groovy", "file:${userHome}/syndicationStorefrontConfig.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = "com.ctacorp.syndication.storefront" // change this to alter the default package name and Maven publishing destination

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

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
    }
}

//log4j configuration
new File("${userHome}/syndicationLogs/storefront").mkdirs() //Create logging dir
log4j.main = {
    appenders {
        rollingFile name:'errorFile', maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/storefront/errors.log", threshold: org.apache.log4j.Level.ERROR
        rollingFile name:'infoFile',  maxFileSize:"10MB", file:"${System.getProperty("user.home")}/syndicationLogs/storefront/details.log", threshold: org.apache.log4j.Level.INFO
    }

    root{
        error 'errorFile', 'infoFile'
    }

    info   'grails.app',
            'org.codehaus.groovy.grails.web.servlet'

    fatal  'org.hibernate.tool.hbm2ddl.SchemaExport'
}

//plugin
grails.plugin.springsecurity.useSecurityEventListener = true

bruteforcedefender {
    time = 15
    allowedNumberOfAttempts = 5
}

grails.plugin.springsecurity.securityConfigType = "Annotation"
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.ctacorp.syndication.authentication.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.ctacorp.syndication.authentication.UserRole'
grails.plugin.springsecurity.authority.className = 'com.ctacorp.syndication.authentication.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/dbconsole/**':                  ['permitAll'],
        '/**':                            ['permitAll'],
        '/assets/**':                     ['permitAll'],
        '/**/js/**':                      ['permitAll'],
        '/**/css/**':                     ['permitAll'],
        '/**/images/**':                  ['permitAll'],
        '/greenmail/**':                  ['permitAll'],
        '/**/favicon.ico':                ['permitAll']
]

syndication.swaggerAddress = "http://localhost:8080/Syndication"

//TagCloud
tagCloud.serverAddress = "http://localhost:8090/TagCloud"

// Mail plugin defaults:
grails {
    mail {
        host = "localhost"
        port = 25
    }
}

SyndicationStorefront.mail.syndicate.to = "syndicationAdmin@hhs.gov"
grails.mail.default.from="server@yourhost.com"

grails.assets.minifyCss = false
grails.assets.minifyJs = false
//___________________________
// Greenmail                 \____________________________________________________________
//_______________________________________________________________________________________
grails.mail.port = 3027
greenmail.ports.smtp = 3027