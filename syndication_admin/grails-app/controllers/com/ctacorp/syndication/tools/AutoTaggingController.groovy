package com.ctacorp.syndication.tools

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_PUBLISHER"])
class AutoTaggingController {
    def tagService
    def springSecurityService
    static defaultAction = "suggestedTags"

    def index() {
        redirect action:"suggestedTags"
    }

    def suggestedTags(Long languageId, Long lastIndex){
        def language = languageId ? Language.get(languageId) : Language.findByIsoCode("eng")
        def tagLanguage = tagService.getAllActiveTagLanguages().find{ it.isoCode == "eng" }
        def mediaItems
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            def publisherItemIds = MediaItemSubscriber.findAllBySubscriberId(springSecurityService.currentUser.subscriberId).mediaItem.id
            mediaItems = MediaItem.findAllByLanguageAndIdGreaterThanAndIdInList(language, lastIndex ?: 0L, publisherItemIds, [sort:"id", order:"ASC"])
        } else {
            mediaItems = MediaItem.findAllByLanguageAndIdGreaterThan(language, lastIndex ?: 0L, [sort:"id", order:"ASC"])
        }

        def untaggedMedia = []
        def suggestedTags = [:]
        int untaggedCount = 0
        long firstIndex = lastIndex ?: 0
        for(mediaItem in mediaItems){
            def tags = tagService.getTagsForSyndicationId(mediaItem.id)
            if(!tags){
                if(untaggedCount >= 50){
                    lastIndex = mediaItem.id
                    break
                }
                untaggedMedia << mediaItem
                suggestedTags[mediaItem.id] = tagService.suggestTags(mediaItem)
                untaggedCount++
            }
        }
        def activeLanguages = Language.findAllByIsActive(true)
        [
            untaggedMedia:untaggedMedia,
            suggestedTags:suggestedTags,
            firstIndex:firstIndex,
            lastIndex:lastIndex,
            languages:activeLanguages,
            language:language,
            tagLanguage:tagLanguage,
            tagLanguages:tagService.getAllActiveTagLanguages()
        ]
    }

    def tagAll(Long languageId){
        def mediaAndTags = [:]
        params.each{ key, val ->
            def keyName = key as String
            if(keyName.startsWith("media_tag_")){
                long mediaId = keyName[10..-1] as Long
                mediaAndTags[mediaId] = val
            }
        }

        def mediaItems = MediaItem.getAll(mediaAndTags*.key)
        def mediaCount = 0
        def tagCount = 0
        Language mediaLanguage = Language.get(languageId)
        def tagLanguage = tagService.getAllActiveTagLanguages().find{ it.isoCode == mediaLanguage.isoCode }.id
        for(mediaItem in mediaItems){
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
                if(MediaItemSubscriber.findByMediaItem(mediaItem).subscriberId != springSecurityService.currentUser.subscriberId){
                    //if publisher is logged in and they don't own this item then skip to the next mediaItem
                    continue
                }
            }
            mediaCount++
            def tagIds = []
            if(mediaAndTags[mediaItem.id].getClass().isArray()) {
                mediaAndTags[mediaItem.id].each { String tagName ->
                    tagCount++
                    def tagInfo = tagService.createTag(tagName, 1L, tagLanguage)
                    tagIds << tagInfo?.id
                }
            } else{
                    tagCount++
                    def tagInfo = tagService.createTag(mediaAndTags[mediaItem.id], 1L, tagLanguage)
                    tagIds << tagInfo?.id
            }
            log.info "tagging ${mediaItem.id} with ${mediaAndTags[mediaItem.id]}"
            tagService.tag(tagIds.join(","), mediaItem.id)
        }
        flash.message = "$mediaCount media items were tagged with $tagCount tags."
        redirect action:'suggestedTags', params: [lastIndex:params.long("lastIndex")]
    }

    def tagSingle(Long mediaId, Long languageId){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            if(MediaItemSubscriber.findByMediaItem(MediaItem.get(mediaId)).subscriberId != springSecurityService.currentUser.subscriberId){
                response.sendError(404)
                return
            }
        }
        def tags = params.tags
        Language mediaLanguage = Language.get(languageId)
        def tagLanguage = tagService.getAllActiveTagLanguages().find{ it.isoCode == mediaLanguage.isoCode }.id
        def tagIds = []

        if(tags.getClass().isArray()) {
            tags.each { tagName ->
                def tagInfo = tagService.createTag(tagName, 1L, tagLanguage)
                tagIds << tagInfo.id
            }
        } else{
            def tagInfo = tagService.createTag(tags, 1L, tagLanguage)
            tagIds << tagInfo.id
        }

        tagService.tag(tagIds.join(","), mediaId)

        if(!tags){
            flash.message = "If the auto generated tags are not appropriate for MediaItem ID:${mediaId} please manually tag it."
        } else if(!(tags instanceof String)) {
            flash.message = "MediaItem ID:${mediaId} has been tagged with ${tags?.join(", ")}"
        } else{
            flash.message = "MediaItem ID:${mediaId} has been tagged with ${tags}"
        }

        redirect action:'suggestedTags', params: [languageId:params.long('languageId'), lastIndex:params.long('firstIndex')]
    }

    private String mediaWithoutTags(){
        def mediaIds = MediaItem.list()*.id
        def mediaWithoutTags = []
        def mediaWithTags = []
        mediaIds.each{ mediaId ->
            def tags = tagService.getTagsForSyndicationId(mediaId)
            if(!tags){
                mediaWithoutTags << mediaId
            } else{
                mediaWithTags << mediaId
            }
        }
        render mediaWithoutTags
        render "<br/><hr/><br/>"
        render mediaWithTags
    }
}
