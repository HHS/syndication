grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

def home = System.getProperty('user.home')
def config = new ConfigSlurper(grailsSettings.grailsEnv).parse(new File("$home/syndicationSharedBuildConfig.groovy").toURI().toURL())

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        mavenRepo(config.artifactory.repo){
            auth username: config.artifactory.username, password: config.artifactory.password
        }

        grailsCentral()
        mavenLocal()
        mavenCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        runtime 'org.jsoup:jsoup:1.8.1'                 //html extraction/manipulation
        runtime 'com.ctacorp:syndication-commons:1.1.7',
                'com.google.guava:guava:18.0',
                'joda-time:joda-time:2.7'
        //Misc http
        compile('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1') {
            excludes 'xalan'
            excludes 'xml-apis'
            excludes 'groovy'
        }
    }

    plugins {
        runtime ("org.grails.plugins:syndication-model:2.0.3"){
            export = false
        }

        build(":release:3.1.1") {
            excludes "rest-client-builder"
            export = false
        }

        build(":rest-client-builder:2.1.1") {
            export = false
        }

        compile ":codenarc:0.23"

        test(":hibernate4:4.3.5.4"){
            export = false
        }
    }
}

//_____________________
// Release plugin info \_________________________________________________________________
//
// | to release, just run 'grails publish-plugin'
// | to install locally, run 'grails maven-install'
//_______________________________________________________________________________________
grails.project.repos.default = "myRepo"
grails.project.repos.myRepo.url = config.artifactory.deploymentAddress
grails.project.repos.myRepo.username = config.artifactory.username
grails.project.repos.myRepo.password = config.artifactory.password