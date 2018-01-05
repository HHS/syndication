package syndication_admin

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class AkamaiTestController {
    def index() { }
}
