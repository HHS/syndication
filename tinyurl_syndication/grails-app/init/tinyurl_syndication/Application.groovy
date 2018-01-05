package tinyurl_syndication

/**
 * Created by utank on 3/3/17.
 */
import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

/**
 * Configuration can happen here instead of Application.yml if desired
 */
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
