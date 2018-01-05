package cms.manager

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class AkamaiTestController {
    def index() { }
}
