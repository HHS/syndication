/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce t`he above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/${appName}-${appVersion}.war"

def home = System.getProperty('user.home')
def config = new ConfigSlurper(grailsSettings.grailsEnv).parse(new File("$home/syndicationSharedBuildConfig.groovy").toURI().toURL())
def runtimeConfig = new ConfigSlurper(grailsSettings.grailsEnv).parse(new File("$home/syndicationSharedConfig.groovy").toURI().toURL())

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: false,
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

//grails.project.fork = false

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits("global") {}

        mavenRepo(config.artifactory.repo){
            auth username: config.artifactory.username, password: config.artifactory.password
            updatePolicy "always"
        }

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        runtime 'mysql:mysql-connector-java:5.1.29'
        runtime 'com.ctacorp:syndication-commons:1.3.0'

        compile 'com.rubiconproject.oss:jchronic:0.2.6'

        compile "com.google.guava:guava:18.0"           //in memory cache
        compile 'org.twitter4j:twitter4j-core:4.0.4'    //twitter

        compile 'com.google.api-client:google-api-client:1.19.1'
        compile 'com.google.apis:google-api-services-analytics:v3-rev109-1.19.1'
        compile 'com.google.oauth-client:google-oauth-client-java6:1.19.0'
        compile 'com.google.oauth-client:google-oauth-client-jetty:1.19.0'

        compile 'com.ctacorp.commons:multi-read-servlet-filter:1.0.0'
        compile('com.ctacorp.commons:api-key-utils:1.6.0') {
            excludes 'groovy'
        }
        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
    }

    plugins {
        // plugins for the compile step ----------------------------------------------------
        compile "org.grails.plugins:syndication-model:2.1.0"       //Syndication domain classes
        compile "org.grails.plugins:content-extraction-services:1.5.0"      //syndication content extraction tools
        compile "org.grails.plugins:solr-operations:1.3.0"                    //syndication solr stuff

        //plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ':asset-pipeline:2.5.9'

        compile ":bruteforce-defender:1.1"

        compile ":rest-client-builder:2.1.1"
        compile ":quartz:1.0.2"
        compile ":pretty-time:2.1.3.Final-1.0.1"
        compile ":spring-security-core:2.0-RC5"
        compile ":rabbitmq-native:3.1.2"                       //mq

        // plugins needed at runtime but not for compilation -------------------------------
        runtime ":hibernate4:4.3.8.1" // or ":hibernate4:4.1.11.1"
        runtime ":database-migration:1.4.1"
        runtime ":jquery:1.11.1"

        //Email Support
        compile ":mail:1.0.7"
        compile ":greenmail:1.3.4"

        // plugins for the build system only -----------------------------------------------
        build ":tomcat:8.0.22"
        build (":release:3.1.1"){                                 //artifactory integration
            excludes "rest-client-builder"
        }

        // plugins for the test phase ------------------------------------------------------
        test ":codenarc:0.24.1"
        test ":build-test-data:2.4.0"
        test ":codenarc:0.19"
        test ":code-coverage:2.0.3-3"
        test ":auto-test:1.0.1"
    }
}

codenarc.properties = {
    EmptyMethod.doNotApplyToClassNames = "*Controller,*Spec"
}

//_____________________
// Release War info    \_________________________________________________________________
// |
// | to push a snapshot, run 'grails prod maven-deploy'
// | to release, run 'grails prod maven-deploy --repository=plugin_rel'
// | to install locally, run 'grails maven-install'
//_______________________________________________________________________________________
grails.project.repos.default = "app_snap"
grails.project.repos.app_snap.url = config.artifactory.app_snap.url
grails.project.repos.app_snap.username = config.artifactory.username
grails.project.repos.app_snap.password = config.artifactory.password

grails.project.repos.app_rel.url = config.artifactory.app_rel.url
grails.project.repos.app_rel.username = config.artifactory.username
grails.project.repos.app_rel.password = config.artifactory.password

//codenarc
codenarc.properties = {
    EmptyIfStatement.priority = 1

    EmptyMethod.enabled = false
    GrailsMassAssignment.enabled = false
    GrailsDomainHasEquals.enabled = false
}
