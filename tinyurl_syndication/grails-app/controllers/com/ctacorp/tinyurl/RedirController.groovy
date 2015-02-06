package com.ctacorp.tinyurl

import com.ctacorp.tinyurl.MediaMapping
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class RedirController {

    def resolveTinyToken(String token) {
        Long id = token.decodeBase32()
        MediaMapping mm = MediaMapping.get(id)
        log.info "Redirecting to ${mm?.targetUrl}"
        if(mm) {
            redirect(url: mm.targetUrl)
        } else{
            response.sendError(404)
        }
    }
}
