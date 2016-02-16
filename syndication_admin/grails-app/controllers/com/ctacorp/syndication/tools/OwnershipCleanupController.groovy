package com.ctacorp.syndication.tools

import com.ctacorp.syndication.commons.util.Util
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

    def search(String query, Boolean restrictToUnowned){
        def subscribers = cmsManagerKeyService.listSubscribers()
        def subscribed = MediaItemSubscriber.list()*.mediaItem.id
        def foundMediaItems = MediaItem.findAllBySourceUrlIlike("${query}%")
        def mediaItems = []
        if(params.boolean('restrictToUnowned')){
            foundMediaItems.each{ mi->
                if((!subscribed.contains(mi.id)) && params.boolean('restrictToUnowned')){
                    mediaItems << mi
                }
            }
        } else{
            mediaItems = foundMediaItems
        }
        render view: "index", model:[mediaItems:mediaItems, query:query, subscribers:subscribers, restrictToUnowned:restrictToUnowned]
    }

    def list(boolean restrictToUnowned){
        def subscribers = cmsManagerKeyService.listSubscribers()
        def subscribed = MediaItemSubscriber.list()*.mediaItem.id
        def foundMediaItems = MediaItem.list()
        def mediaItems = []

        if(restrictToUnowned){
            foundMediaItems.each{ mi->
                if((!subscribed.contains(mi.id)) && params.boolean('restrictToUnowned')){
                    mediaItems << mi
                }
            }
        } else{
            mediaItems = foundMediaItems
        }

        render view: "index", model:[mediaItems:mediaItems,subscribers:subscribers, query:params.query, restrictToUnowned: params.boolean('restrictToOwned')]
    }

    def associate(Long subscriber, String query, boolean restrictToUnowned){
        def mediaItems = MediaItem.findAllBySourceUrlIlike("${query}%")

        if(restrictToUnowned){
            mediaItems.each{ mi ->
                if(!MediaItemSubscriber.findByMediaItem(mi)){
                    def mis = new MediaItemSubscriber(mediaItem: mi, subscriberId: subscriber)
                    mis.save()
                }
            }
        } else{
            mediaItems.each{ mi ->
                def mis = MediaItemSubscriber.findByMediaItem(mi)
                if(mis){
                    mis.subscriberId = subscriber
                    mis.save()
                }else{
                    log.error("couldn't find subscriber: ${subscriber}")
                }
            }
        }

        redirect action:"search", params:[restrictToUnowned:false, query:query]
    }
}
