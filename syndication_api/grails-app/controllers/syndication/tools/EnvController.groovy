package syndication.tools

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class EnvController {

    def envService

    def index() {
        render text: envService.dumpEnv()
    }
}
