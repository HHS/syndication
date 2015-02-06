package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.UserRole
import grails.transaction.Transactional

@Transactional
class ExtendedAttributeService {

    def springSecurityService
    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    def getUpdateInformation(ExtendedAttribute att) {
        def name = att.name
        def mediaItem = att.mediaItem
        def atts = ExtendedAttribute.findAllByMediaItem(mediaItem)
        for (it in atts) {
            if (it.name.trim().equalsIgnoreCase(name.trim())) {
               it.value = att.value
                return it
            }
        }
        return null
    }

    def ifPublisherValid(ExtendedAttribute extendedAttributeInstance){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !publisherItems()?.contains(extendedAttributeInstance.mediaItem?.id)){
            return false
        }
        return true
    }
}
