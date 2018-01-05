package com.ctacorp.syndication.crud

import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.social.TwitterAccount
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.social.TwitterStatusCollector
import grails.plugin.springsecurity.annotation.Secured
import twitter4j.TwitterException

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NOT_FOUND

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
class TwitterAccountController {

    def cmsManagerKeyService
    def springSecurityService
    def mediaItemsService
    def twitterService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def authority = UserRole.findByUser(springSecurityService.currentUser).role.authority
        if(authority in ["ROLE_ADMIN", "ROLE_MANAGER"]) {
            def allAccounts = TwitterAccount.list(params)
            render view:"index", model:[twitterAccountInstanceList:allAccounts, total:allAccounts.totalCount]
        } else {
            def accountsBySubscriber = TwitterAccount.findAllBySubscriberId(springSecurityService.currentUser.subscriberId, params)
            render view:"index", model:[twitterAccountInstanceList:accountsBySubscriber, total:accountsBySubscriber?.size() ?: 0]
        }
    }

    def create() {
        render view:"create", model: [subscribers:cmsManagerKeyService.listSubscribers()]
    }

    def edit(TwitterAccount twitterAccountInstance) {
        render view:"edit", model: [twitterAccountInstance:twitterAccountInstance,subscribers:cmsManagerKeyService.listSubscribers()]
    }

    def save(TwitterAccount twitterAccountInstance) {

        if (twitterAccountInstance == null) {
            return notFound()
        }

        def accountName = twitterAccountInstance.accountName

        if(twitterAccountHasIssue(accountName)) {return}

        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
            twitterAccountInstance.subscriberId = springSecurityService.currentUser.subscriberId
        }

        twitterAccountInstance.validate()
        if (twitterAccountInstance.hasErrors()) {
            flash.errors = twitterAccountInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond twitterAccountInstance, view:'create', model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        twitterAccountInstance.save()

        request.withFormat {
            form multipartForm{
                flash.message = "Twitter account added!"
                redirect twitterAccountInstance
            }
            '*' { respond twitterAccountInstance, [status: CREATED] }
        }
    }

    def show(TwitterAccount twitterAccountInstance) {
        render view:"show", model: [twitterAccountInstance:twitterAccountInstance]
    }
    def update(TwitterAccount twitterAccountInstance) {
        if (twitterAccountInstance == null) {
            notFound()
            return
        }

        if(twitterAccountHasIssue(twitterAccountInstance.accountName)) {return}

        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
            twitterAccountInstance.subscriberId = springSecurityService.currentUser.subscriberId
        }

        twitterAccountInstance.validate()
        if (twitterAccountInstance.hasErrors()) {
            flash.errors = twitterAccountInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond twitterAccountInstance, view:'edit', model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        twitterAccountInstance.save(flush:true)

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.updated.message', args: [message(code: 'twitterAccountInstance.label', default: 'Twitter Account'), [twitterAccountInstance.accountName]])
                redirect twitterAccountInstance
            }
            '*' { respond twitterAccountInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete(TwitterAccount twitterAccount) {
        if (twitterAccount == null) {
            notFound()
            return
        }

        def statusCollectors = TwitterStatusCollector.list()
        statusCollectors.each{collector ->
            if(collector.twitterAccounts.contains(twitterAccount)){
                collector.delete()
            }
        }

        def mediaItems = Tweet.findAllByAccount(twitterAccount)
        mediaItems.each{ item ->
            mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(item, true)
            mediaItemsService.delete(item.id)
        }

        twitterAccount.delete(flush:true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TwitterAccount.label', default: 'Twitter Account'), [twitterAccount.accountName]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }


    }

    protected boolean twitterAccountHasIssue(String accountName) {

        try {

            twitterService.twitter.users().lookupUsers(accountName)
            false

        } catch (TwitterException e) {

            if(e.resourceNotFound()) {
                twitterAccountError("twitter account '${accountName}' does not exist")
            } else {
                twitterAccountError("a problem occurred when creating twitter account '${accountName}'")
            }

            true
        }
    }

    protected void twitterAccountError(msg) {
        request.withFormat {
            form {
                flash.error = [msg]
                redirect action: 'index'
            }
            '*'{ render status: 200 }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'twitterAccountInstance.label', default: 'Twitter Account'), params.id])
                redirect controller: 'twitterAccount', action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
