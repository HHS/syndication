package tagcloud

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType
import com.ctacorp.syndication.commons.util.Util

@Secured(['permitAll'])
class TagTypesController {
    def tagService

    static responseFormats = ['json']
    static allowedMethods = [
            listTagTypes:'GET', getTagTypeById: 'GET', getTagTypeByName: 'GET',
            saveTagType:'POST', updateTagType:'POST', index:'GET',
            deleteTagType:'DELETE'
    ]

    def index(){
        params.max = Util.getMax(params)
        def tagTypes = tagService.listTagTypes(params)
        if(Util.isTrue(params.includePaginationFields, false)) {
            respond ([tagTypes:tagTypes, total:tagTypes.totalCount, max:params.max, offset:params.offset?:0])
        } else{
            respond tagTypes
        }
    }

    @Transactional
    def deleteTagType(Long id){
        def tagType = TagType.get(id)
        if(tagType) {
            def tagList = Tag.findAllByType(tagType)
            tagList.each{ Tag tag ->
                tag.contentItems.each{ ContentItem contentItem ->
                    contentItem.removeFromTags(tag)
                }
            }
            tagList*.id.each{ tagId ->
                Tag.load(tagId).delete(flush:true)
            }

            tagType.delete(flush: true)

            render text:([success:true, message:"Tag Type id:${id} deleted."] as JSON), contentType: "application/json"
        } else{
            render text:([success:false, message:"Tag Type id:${id} could not be found."] as JSON), contentType: "application/json", status: 400
        }
    }

    def getTagTypeById(Long id) {
        respond tagService.getTagTypeById(id)
    }

    def getTagTypeByName(String name) {
        respond tagService.getTagTypeByName(name)
    }

    def saveTagType(TagType tagType){
        if(tagType?.hasErrors()){
            respond tagType.errors
            return
        }
        respond tagService.saveTagType(tagType)
    }

    def updateTagType(TagType tagType) {
        if(tagType?.hasErrors()){
            respond tagType.errors
            return
        }
        respond tagService.updateTagType(tagType)
    }
}
