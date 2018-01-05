package tinyurl_syndication

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class AkamaiTestController {
    def index() { }
}
