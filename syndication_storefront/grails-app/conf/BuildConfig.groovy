grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

if(config.grails.project.fork != null && config.grails.project.fork == false){
    grails.project.fork = false
} else {
    grails.project.fork = [
            test   : false,
            run    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
            war    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
            console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
    ]
}

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    inherits("global") {}
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits("global") {}

        mavenRepo("http://54.234.21.193:8080/artifactory/central"){
            updatePolicy "always"
        }

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        compile 'com.google.guava:guava:18.0'
        compile 'org.pegdown:pegdown:1.6.0'

        runtime 'mysql:mysql-connector-java:5.1.38'
        runtime 'com.ctacorp:syndication-commons:1.3.1'

        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
    }

    plugins {
        compile ":rest-client-builder:2.1.1"
        compile "org.grails.plugins:syndication-model:2.8.0"
        compile "org.grails.plugins:content-extraction-services:1.13.0"

        compile ":bruteforce-defender:1.0.1-spring-security-core-2.0-RC4"
        compile ":recaptcha:0.6.9"

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ':asset-pipeline:2.1.5'
        compile ":quartz:1.0.2"

        compile ":spring-security-core:2.0.0"
        compile ":rest-client-builder:2.1.1"

        //Email Support
        compile ":mail:1.0.7"
        compile ":greenmail:1.3.4"

        //pagination
        compile ":remote-pagination:0.4.8"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.8.1"
        runtime ":database-migration:1.4.1"
        runtime ":jquery:1.11.1"

        // plugins for the build system only ------------------------------------
        build (":release:3.0.1"){
            excludes "rest-client-builder"
        }

        build ":tomcat:7.0.55.2"

        // Solr --------------------------------------------------------------------------------------------------------
        // |  Leave this at the bottom, moving it causes dependency problems at the moment                             |
        // -------------------------------------------------------------------------------------------------------------
        compile ":solr-operations:1.4.0"      //syndication solr stuff

        //Testing ---------------------------------------------------------------
        test ":build-test-data:2.4.0"
        test ":codenarc:0.24.1"
        test ":code-coverage:2.0.3-3"
        test ":auto-test:1.0.1"
    }
}

//codenarc
codenarc.properties = {
    EmptyIfStatement.priority = 1

    EmptyMethod.enabled = false
    GrailsMassAssignment.enabled = false
    GrailsDomainHasEquals.enabled = false
}

coverage {
    exclusions = ["**/ApplicationResources*", "**/RecaptchaConfig*"]
}
