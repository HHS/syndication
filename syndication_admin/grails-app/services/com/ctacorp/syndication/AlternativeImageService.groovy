package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.UserRole
import grails.transaction.Transactional

@Transactional
class AlternativeImageService {

    def springSecurityService
    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    def addAlternativeImage(AlternateImage ai) {
        if (ai == null) {
            return "Image not found"
        }

        if (ai.hasErrors()) {
            return "You must include a valid Name and Url"
        }

        //TODO check for duplicate images for the same mediaItem

        ai.save flush:true
        return null


    }

    def ifPublisherValid(AlternateImage alternateImageInstance){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !publisherItems()?.contains(alternateImageInstance.mediaItem?.id)){
            return false
        }
        return true
    }
}
