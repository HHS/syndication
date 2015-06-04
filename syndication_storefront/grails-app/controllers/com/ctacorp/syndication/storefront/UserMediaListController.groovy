package com.ctacorp.syndication.storefront

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.authentication.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_STOREFRONT_USER', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', "ROLE_PUBLISHER"])
class UserMediaListController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def springSecurityService
    def mediaService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def userMediaLists = UserMediaList.where{
            user == springSecurityService.currentUser as User
        }.list(params)
        respond userMediaLists, view:'index', model: [userMediaListInstanceCount: userMediaLists.totalCount,
                                                      featuredMedia: mediaService.getFeaturedMedia(max:20)]
    }

    def show(UserMediaList userMediaListInstance) {
        respond userMediaListInstance, model: [featuredMedia: mediaService.getFeaturedMedia(max:20)]
    }

    def create() {
        respond new UserMediaList(params), model: [featuredMedia: mediaService.getFeaturedMedia(max:20)]
    }

    def mediaSearch(String q){
        response.contentType = "application/json"
        render MediaItem.facetedSearch([nameContains:q, active:true, visibleInStorefront:true,syndicationVisibleBeforeDate:new Date()]).list([max:15]).collect{ [name:"${it.id} - ${it.name}", id:it.id] } as JSON
    }

    def selectUserMediaList(){
        render params
    }

    @Transactional
    def save(UserMediaList userMediaListInstance) {
        if (userMediaListInstance == null) {
            notFound()
            return
        }

        userMediaListInstance.user = springSecurityService.getCurrentUser() as User
        addMediaItems(userMediaListInstance)

        userMediaListInstance.validate()
        if (userMediaListInstance.hasErrors()) {
            String mediaItemList = userMediaListInstance.mediaItems?.collect{ [id:it.id, name:"${it.id} - ${it.name}"] } as JSON
            respond userMediaListInstance.errors, view: 'create', model:[mediaItemList:mediaItemList]
            return
        }

        userMediaListInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userMediaListInstance.label', default: 'List'), userMediaListInstance.name])
                redirect userMediaListInstance
            }
            '*' { respond userMediaListInstance, [status: CREATED] }
        }
    }

    def edit(UserMediaList userMediaListInstance) {
        if(!userMediaListInstance){
            notFound()
            return
        }

        String mediaItemList = userMediaListInstance.mediaItems?.collect{ [id:it.id, name:"${it.id} - ${it.name}"] } as JSON
        respond userMediaListInstance, model:[mediaItemList:mediaItemList, featuredMedia: mediaService.getFeaturedMedia(max:20)]
    }

    @Transactional
    def update(UserMediaList userMediaListInstance) {
        if (!userMediaListInstance) {
            notFound()
            return
        }

        if(userMediaListInstance.user != springSecurityService.getCurrentUser() as User){
            userMediaListInstance.errors.reject("invalid.ownership", "You tried to update a list you don't own, has your login expired?")
            respond userMediaListInstance.errors, view: 'edit'
            return
        }
        addMediaItems(userMediaListInstance)

        userMediaListInstance.validate()
        if (userMediaListInstance.hasErrors()) {
            respond userMediaListInstance.errors, view: 'edit'
            return
        }

        userMediaListInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'UserMediaList.label', default: 'List'), userMediaListInstance.name])
                redirect userMediaListInstance
            }
            '*' { respond userMediaListInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(UserMediaList userMediaListInstance) {
        if (userMediaListInstance == null) {
            notFound()
            return
        }

        if(UserMediaList.findAllByUser(springSecurityService.currentUser).size() <= 1){
            flash.error = "You cannot delete your only media list. Please create another one first."
            respond userMediaListInstance, view: 'show'
            return
        }

        userMediaListInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'UserMediaList.label', default: 'List'), userMediaListInstance.name])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userMediaListInstance.label', default: 'List'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    protected addMediaItems(UserMediaList userMediaListInstance){
        userMediaListInstance?.mediaItems?.clear()
        if(params.mediaItemIds){
            params.mediaItemIds.split(",").collect{ it as Long }.each{
                userMediaListInstance.addToMediaItems(MediaItem.load(it))
            }
        }
    }
}