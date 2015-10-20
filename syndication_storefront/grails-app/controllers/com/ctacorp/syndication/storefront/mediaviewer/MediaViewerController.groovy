package com.ctacorp.syndication.storefront.mediaviewer

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.storefront.UserMediaList

class MediaViewerController {
    def apiService
    def tagService

    def index() {
        if(!params.mediaSource || !params.id){
            log.error "no media source or id specified"
            response.sendError(400, "no media source or id specified")
            return
        }

        def mediaItems
        long id = params.long("id")

        switch(params.mediaSource.toLowerCase()){
            case "tag":
                mediaItems = tagService.getMediaForTagId(id, [max:20, sort: "dateSyndicationCaptured", order: "desc"])
                break
            case "usermedialist":
                mediaItems = UserMediaList.read(id).mediaItems
                break
            case "collection":
                mediaItems = com.ctacorp.syndication.media.Collection.read(id).mediaItems
                break
            case "campaign":
                mediaItems = Campaign.read(id).mediaItems
                break
            case "source":
                mediaItems = MediaItem.findAllBySource(Source.load(id), [max:20, sort:"id", order:"desc"])
                break
            default:
                log.error "invalid mediaSource"
                response.sendError(400, "Invalid media source: ${params.mediaSource}")
                return
        }

        mediaItems = mediaItems.sort{it.name}

        if(mediaItems?.size() > 20){
            mediaItems = mediaItems.toArray()[0..19]
        }

        def mediaList = []
        mediaItems.each{ mediaItem ->
            mediaList << [content:apiService.getSyndicatedContent(mediaItem.id), meta:mediaItem]
        }

        [mediaList:mediaList]
    }
}
