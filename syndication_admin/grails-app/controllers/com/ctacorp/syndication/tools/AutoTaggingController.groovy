package com.ctacorp.syndication.tools

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_PUBLISHER"])
class AutoTaggingController {
    def tagService
    def springSecurityService
    static defaultAction = "suggestedTags"

    def index() {
        redirect action:"suggestedTags"
    }

    def suggestedTags(Long languageId, Integer max, Integer offset){
        max = Math.min(max ?: 50, 50)
        offset = offset ?: 0
        Language language = languageId ? Language.read(languageId): Language.findByIsoCode("eng")
        languageId = language.id
        def untaggedMedia
        def tagLanguage = tagService.getAllActiveTagLanguages().find{ it.isoCode == "eng" }

        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            untaggedMedia = tagService.listMediaItemsWithoutTags(languageId, springSecurityService.currentUser.subscriberId, offset, max)
        } else {
            untaggedMedia = tagService.listMediaItemsWithoutTags(languageId, null, offset, max)
        }

        def suggestedTags = [:]
        untaggedMedia.each{ MediaItem mediaItem ->
            suggestedTags[mediaItem.id] = tagService.suggestTags(mediaItem)
        }

        def activeLanguages = Language.findAllByIsActive(true)
        [
                untaggedMedia:untaggedMedia,
                suggestedTags:suggestedTags,
                languages:activeLanguages,
                language:language,
                tagLanguage:tagLanguage,
                tagLanguages:tagService.getAllActiveTagLanguages(),
                total:untaggedMedia ? untaggedMedia.totalCount : 0
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
        Language mediaLanguage = Language.get(languageId)
        def tagLanguage = tagService.getAllActiveTagLanguages().find{ it.isoCode == mediaLanguage.isoCode }?.id
        if(!tagLanguage) {
            flash.message = "Tag Cloud does not currently support the language '${mediaLanguage}'"
            redirect action:'suggestedTags', params: [lastIndex:params.long("lastIndex"), languageId:params.long('languageId')]
            return
        }

        def response = tagService.bulkTag(mediaItems, mediaAndTags, languageId)

        def allIds = []
        response.collect()*.tags*.id.collect{ allIds += it }
        def tagCount = allIds.unique().size()
        def mediaCount = response.size()

        flash.message = "$mediaCount media items were tagged with $tagCount tags."
        redirect action:'suggestedTags', params: [lastIndex:params.long("lastIndex"), languageId:params.long('languageId')]
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
        def tagLanguage = tagService.getAllActiveTagLanguages().find{ it.isoCode == mediaLanguage.isoCode }?.id
        if(!tagLanguage) {
            flash.message = "Tag Cloud does not currently support this language '${mediaLanguage}'"
            redirect action:'suggestedTags', params: [languageId:params.long('languageId'), lastIndex:params.long('firstIndex')]
            return
        }
        
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
