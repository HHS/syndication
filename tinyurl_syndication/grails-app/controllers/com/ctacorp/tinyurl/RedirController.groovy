package com.ctacorp.tinyurl

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class RedirController {

    def resolveTinyToken(String token) {
        try {
            Long id = token?.decodeBase32()
            MediaMapping mm = MediaMapping.get(id)
            if(mm) {
                log.info "Redirecting to ${mm?.targetUrl}"
                redirect(url: mm.targetUrl)
                return
            }
        } catch(e){
            log.error("Resolving tiny token ${token} failed", e)
        }
        response.sendError(404)
    }
}
