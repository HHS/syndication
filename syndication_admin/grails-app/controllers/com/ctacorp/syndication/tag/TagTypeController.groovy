package com.ctacorp.syndication.tag

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class TagTypeController {

    def tagService

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'])
    def index(){
        if(tagService.status()) {
            renderTagList()
        }
        else {
            flash.error = message(code: 'tag.failure.UNREACHABLE')
        }
    }

    def create(){
        [name:params.name, description:params.description]
    }

    def edit(Long id){
        [tagTypeInstance: tagService.getTagType(id)]
    }

    def update(){
        def tagType = tagService.updateTagType(params.long('id'), params.name, params.description)

        if (!tagType.errors) {
            flash.message = "Tag Type [${params.name}] has been updated!"
        } else {
            flash.errors = tagType.errors
            redirect action: "edit", id:params.long('id')
            return
        }
        redirect action:"index"
    }

    def delete(Long id){
        def r = tagService.deleteTagType(id)
        flash.message = "Tag Type [${params.name}] deleted."
        redirect action:"index"
    }

    def save(){
        def tagType
        try {
            tagType = tagService.saveTagType(params.name, params.description)
        } catch(e){
            log.error e
            flash.errors = [[message:e.getMessage()]]
            renderTagList()
            return
        }

        if (tagType && !tagType.errors) {
            flash.message = "Tag Type [${params.name}] created!"
        } else {
            flash.errors = tagType.errors
            redirect action: "create"
            return
        }
        redirect action:"index"
    }

    private renderTagList(){
        params.includePaginationFields = 1
        try {
            def tagTypeInstanceList = tagService.getTagTypes(params)
            render view:'index', model:[tagTypeInstanceList:tagTypeInstanceList?.tagTypes, tagTypeInstanceCount: tagTypeInstanceList?.total]
        } catch(e){
            flash.errors = [[message:"Could not communicate with tag cloud server!"]]
            render view:'index'
        }
    }
}