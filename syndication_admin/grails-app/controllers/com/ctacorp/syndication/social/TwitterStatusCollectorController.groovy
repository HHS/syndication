package com.ctacorp.syndication.social

import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.Collection
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
class TwitterStatusCollectorController {

    def springSecurityService
    def mediaItemsService
    def twitterService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def authority = UserRole.findByUser(springSecurityService.currentUser).role.authority
        if(authority in ["ROLE_ADMIN", "ROLE_MANAGER"]) {
            def allStatusCollectors = TwitterStatusCollector.list(params)
            render view:"index", model:[twitterStatusCollectorInstanceList:allStatusCollectors, total:allStatusCollectors.totalCount]
        } else {
            def statusCollectorBySubscriber = TwitterStatusCollector.findAllByCollectionInList(mediaItemsService.getIndexResponse(params, Collection).mediaItemList, params)
            render view:"index", model:[twitterStatusCollectorInstanceList:statusCollectorBySubscriber, total:statusCollectorBySubscriber?.size() ?: 0]
        }
    }

    def create() {
        def twitterStatusCollector = new TwitterStatusCollector(params)
        def twitterAccounts
        def authority = UserRole.findByUser(springSecurityService.currentUser).role.authority
        if(authority in ["ROLE_ADMIN", "ROLE_MANAGER"]){
            twitterAccounts = TwitterAccount.list()
        } else {
            twitterAccounts = TwitterAccount.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)
        }
        respond twitterStatusCollector, model:[twitterAccounts:twitterAccounts]
    }

    def save(TwitterStatusCollector twitterStatusCollector) {
        def errors = twitterService.twitterStatusCollectorErrors(twitterStatusCollector, params)
        if(errors){
            def twitterAccounts
            def authority = UserRole.findByUser(springSecurityService.currentUser).role.authority
            if(authority in ["ROLE_ADMIN", "ROLE_MANAGER"]){
                twitterAccounts = TwitterAccount.list()
            } else {
                twitterAccounts = TwitterAccount.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)
            }
            flash.errors = errors
            respond twitterStatusCollector, view:"create", model:[twitterAccounts:twitterAccounts,twitterAccount:params.twitterAccounts, sourceId:params.source.id]
            return
        }

        twitterStatusCollector.collection = new Collection(source: Source.get(params.source.id))
        twitterService.createStatusCollection(twitterStatusCollector)

        if(!twitterStatusCollector.validate()){
            def twitterAccounts
            def authority = UserRole.findByUser(springSecurityService.currentUser).role.authority
            if(authority in ["ROLE_ADMIN", "ROLE_MANAGER"]){
                twitterAccounts = TwitterAccount.list()
            } else {
                twitterAccounts = TwitterAccount.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)
            }
            flash.errors = twitterStatusCollector.errors.allErrors.collect{[message:g.message([error : it])]}
            respond twitterStatusCollector, view:"create", model:[twitterAccounts:twitterAccounts]
            return
        }

        twitterStatusCollector.save(flush:true)

        redirect action:"show", id:twitterStatusCollector.id
    }

    def show(TwitterStatusCollector twitterStatusCollector) {
        render view:"show", model:[twitterStatusCollector:twitterStatusCollector]
    }

    def delete(TwitterStatusCollector twitterStatusCollector) {
        if (twitterStatusCollector == null) {
            notFound()
            return
        }

        twitterStatusCollector.delete(flush:true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TwitterStatusCollector.label', default: 'Tweet Auto Importer'), [twitterStatusCollector?.hashTags]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'twitterStatusCollectorInstance.label', default: 'Tweet Auto Importer'), params.id])
                redirect controller: 'twitterStatusCollector', action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
