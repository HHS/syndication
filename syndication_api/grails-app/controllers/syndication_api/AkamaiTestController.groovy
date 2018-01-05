package syndication_api

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class AkamaiTestController {
    def index() { }
}
