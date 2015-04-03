package com.ctacorp.syndication.tools

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class OwnershipCleanupController {
    def cmsManagerKeyService

    def index(String query) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        [query:query, subscribers:subscribers]
    }

    def search(String query){
        def subscribers = cmsManagerKeyService.listSubscribers()
        def subscribed = MediaItemSubscriber.list()*.mediaItem.id
        def foundMediaItems = MediaItem.findAllBySourceUrlIlike("${query}%")
        def unownedItems = []
        foundMediaItems.each{ mi->
            if(!subscribed.contains(mi.id)){
                unownedItems << mi
            }
        }
        render view: "index", model:[mediaItems:unownedItems, query:query, subscribers:subscribers]
    }

    def list(){
        def subscribers = cmsManagerKeyService.listSubscribers()
        def subscribed = MediaItemSubscriber.list()*.mediaItem.id
        def foundMediaItems = MediaItem.list()
        def unownedItems = []
        foundMediaItems.each{ mi->
            if(!subscribed.contains(mi.id)){
                unownedItems << mi
            }
        }
        render view: "index", model:[mediaItems:unownedItems,subscribers:subscribers]
    }

    def associate(Long subscriber, String query){
        def mediaItems = MediaItem.findAllBySourceUrlIlike("${query}%")
        mediaItems.each{ mi ->
            if(!MediaItemSubscriber.findByMediaItem(mi)){
                def mis = new MediaItemSubscriber(mediaItem: mi, subscriberId: subscriber)
                mis.save()
            }
        }
        redirect action:"list"
    }
}
