package tagcloud

import grails.converters.JSON
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import tagcloud.domain.ContentItem

@Transactional(readOnly = true)
@Secured(['permitAll'])
class ContentController {
    static responseFormats = ['json']
    def contentItemService
    def tagService

    /**
     * Get a list of all the content items in the database
     * @return
     */
    def index() {
        JSON.use("contentList") {
            respond ContentItem.list(params)
        }
    }

    /**
     * Get content items related to a syndication id
     * @param syndicationId
     * @return
     */
    def getRelatedContentBySyndicationId(Long syndicationId) {
        JSON.use("contentList") {
            respond contentItemService.getRelatedContentItemsBySyndicationId(syndicationId, params)
        }
    }

    /**
     * Get content items related to a Url
     * @param url
     * @return
     */
    def getContentRelatedToUrl(String url) {
        ContentItem ci = ContentItem.findByUrl(url)
        if(!ci){
            def empty = []
            respond empty
            return
        }

        JSON.use("contentList") {
            respond contentItemService.getRelatedContentItemsByContentItem(ci, params)
        }
    }

    /**
     * Get content items related to a tag id
     * @param tagId
     * @return
     */
    def getContentRelatedByTagId(Long tagId) {
        def tags = tagService.getRelatedTagsByTagId(tagId)
        def content = []
        tags*.contentItems.each{ contentItems ->
            content.addAll(contentItems)
        }
        JSON.use("contentList") {
            respond content.unique()
        }
    }

    /**
     * Get content tagged with a tag id
     * @param tagId
     * @return
     */
    def getContentForTagId(Long tagId){
        JSON.use("contentList"){
            respond contentItemService.getContentForTagId(tagId, params)
        }
    }

    /**
     * Get content tagged by all tag Ids
     * @param id  the list of all tag ids
     * @return
     */
    def getContentForTagIds(String id){
        JSON.use("contentList"){
            respond contentItemService.getContentForTagIds(id, params)
        }
    }
}