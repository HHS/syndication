package com.ctacorp.syndication.tag

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class TagLanguagesController {
    def tagService

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'])
    def index() {
        def tagLanguageList
        def activeTagLanguageList
        if(tagService.status()) {
            tagLanguageList = tagService.getTagLanguages(true)
            activeTagLanguageList = tagService.getTagLanguages()
        }
        else {
            flash.error = message(code: 'tag.failure.UNREACHABLE')
        }
        [tagLanguageInstanceList:tagLanguageList, activeTagLanguageInstanceList:activeTagLanguageList]
    }


    def activateLanguage(Long id){
        def resp = tagService.activateTagLanguage(id)
        if(resp?.success){
            flash.message = resp.message
        } else{
            flash.errors = [[message:"${resp?.message?:'The Tag Language could not be activated!'}"]]
        }
        redirect action:'index'
    }

    def deactivateLanguage(Long id){
        def resp = tagService.deactivateTagLanguage(id)

        if(resp?.success){
            flash.message = resp.message
        } else{
            flash.errors = [[message:"${resp?.message?:'The TagLanguage could not be deactivated!'}"]]
        }
        redirect action:'index'
    }

}
