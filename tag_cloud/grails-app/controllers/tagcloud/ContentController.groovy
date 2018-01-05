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
        respond ContentItem.list(params)
    }

    /**
     * Get content items related to a syndication id
     * @param syndicationId
     * @return
     */
    def getRelatedContentBySyndicationId(Long syndicationId) {
        respond contentItemService.getRelatedContentItemsBySyndicationId(syndicationId, params), view:"index"
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

        respond contentItemService.getRelatedContentItemsByContentItem(ci, params), view:"index"
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
        respond content.unique(), view:"index"
    }

    /**
     * Get content tagged with a tag id
     * @param tagId
     * @return
     */
    def getContentForTagId(Long tagId){
        def items = contentItemService.getContentForTagId(tagId, params) as List<ContentItem> ?: []
        respond items, view:"index"
    }

    /**
     * Get content tagged by all tag Ids
     * @param id  the list of all tag ids
     * @return
     */
    def getContentForTagIds(String id){
        respond contentItemService.getContentForTagIds(id, params), view:"index"

    }
}