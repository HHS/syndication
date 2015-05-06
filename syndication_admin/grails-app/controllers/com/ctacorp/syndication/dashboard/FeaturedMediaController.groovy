package com.ctacorp.syndication.dashboard

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER", "ROLE_BASIC"])
class FeaturedMediaController {
    def featuredMediaService

    def index() {
        def featuredMedia = featuredMediaService.listFeatured(params) ?: []
        String featuredMediaForTokenInput = featuredMedia.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON
        [featuredMedia:featuredMedia, featuredMediaForTokenInput:featuredMediaForTokenInput]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'])
    def featureMediaItems(String featuredMedia){
        if(!featuredMedia){
            featuredMediaService.clear()
            redirect action:'index'
            return
        }

        def toFeature = featuredMedia.split(",").collect{ it as Long }.unique()
        def alreadyFeatured = featuredMediaService.listFeatured()*.id

        def toUnFeature = alreadyFeatured - toFeature
        toFeature = toFeature - alreadyFeatured.intersect(toFeature)

        toUnFeature.each { id ->
            featuredMediaService.unfeatureMedia(id)
        }

        toFeature.each{ id ->
            featuredMediaService.featureMedia(id)
        }
        redirect action:'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'])
    def featureItem(Long id){
        flash.message = "The media item has been featured. <a href='${createLink(controller: 'featuredMedia', action: 'index')}'>Click here for feature page</a>"

        if(featuredMediaService.listFeatured().id.contains(id)){
            flash.message = "The media item is already featured. <a href='${createLink(controller: 'featuredMedia', action: 'index')}'>Click here for feature page</a>"
        } else {
            featuredMediaService.featureMedia(id)
        }
        redirect controller:'mediaItem', action:'show', id:id
    }
}
