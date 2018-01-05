package syndication_storefront

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class AkamaiTestController {
    def index() { }
}
