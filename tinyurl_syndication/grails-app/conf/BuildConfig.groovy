grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

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
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        mavenRepo(config.artifactory.repo){
            auth([
                    username: config.artifactory.username,
                    password: config.artifactory.password
            ])

            updatePolicy "always"
        }
        mavenLocal()
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
        runtime 'mysql:mysql-connector-java:5.1.27'
        //CMS Manager security libs
        runtime 'com.ctacorp.commons:api-key-utils:1.4'
        compile "com.google.guava:guava:12.0"
        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.55"
        build (":release:3.0.1"){
            excludes "rest-client-builder"
        }

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'

        compile ":rest-client-builder:2.0.1"

        compile ":bruteforce-defender:1.0.1-spring-security-core-2.0-RC4"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.6.1" // or ":hibernate:3.6.10.18"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"
//        runtime ":resources:1.2.7"
        compile ":asset-pipeline:1.9.9"

        compile ":spring-security-core:2.0-RC4"

        test ":auto-test:1.0.1"

        //Artifactory integration
        build (":release:3.0.1"){
            excludes "rest-client-builder"
        }

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0.1"
        //runtime ":cached-resources:1.1"
        //runtime ":yui-minify-resources:0.1.5"
    }
}

//Localize the grails work folder
grails.work.dir = "target/work"

//_____________________
// Release plugin info \_________________________________________________________________
//
// | to release, just run 'grails maven-deploy'
// | to install locally, run 'grails maven-install'
//_______________________________________________________________________________________
grails.project.repos.default = "myRepo"
grails.project.repos.myRepo.url = config.artifactory.deploymentAddress
grails.project.repos.myRepo.username = config.artifactory.username
grails.project.repos.myRepo.password = config.artifactory.password