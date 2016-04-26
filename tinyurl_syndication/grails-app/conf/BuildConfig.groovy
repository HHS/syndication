grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.fork = [
    test: false,
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

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
        mavenLocal()
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        runtime 'mysql:mysql-connector-java:5.1.38'
        //CMS Manager security libs
        compile('com.ctacorp.commons:api-key-utils:1.6.1'){
            excludes 'groovy'
        }
        compile "com.google.guava:guava:18.0"
        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.55.2"
        build (":release:3.1.1"){
            excludes "rest-client-builder"
        }

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'

        compile ":rest-client-builder:2.1.1"

        compile ":bruteforce-defender:1.1"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.8.1" // or ":hibernate:3.6.10.18"
        runtime ":database-migration:1.4.1"
        runtime ":jquery:1.11.1"
        compile ":asset-pipeline:2.6.5"

        compile ":spring-security-core:2.0.0"

        test ":auto-test:1.0.1"

        //Artifactory integration
        build (":release:3.1.1"){
            excludes "rest-client-builder"
        }
    }
}

//Localize the grails work folder
grails.work.dir = "target/work"