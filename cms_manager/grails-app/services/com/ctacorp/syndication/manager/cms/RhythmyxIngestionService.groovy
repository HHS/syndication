/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms
import com.ctacorp.syndication.rsrw.ContentTypesResource
import com.ctacorp.syndication.rsrw.PercussionContentServicesFactory
import com.ctacorp.syndication.rsrw.SyndicatedContentResource
import com.ctacorp.syndication.rsrw.model.Guid
import com.ctacorp.syndication.rsrw.model.SyndicatedContent
import grails.transaction.Transactional

import javax.ws.rs.core.Response

@Transactional
class RhythmyxIngestionService {

    static final WORKFLOW_TRANSITION_UPDATE = 0
    static final WORKFLOW_TRANSITION_IMPORT = 1
    static final WORKFLOW_TRANSITION_DELETE = 2

    def rhythmyxResourceFactory = new RhythmyxResourceFactory()

    String importMediaItem(RhythmyxSubscription subscription, String mediaContent, String systemTitle) {

        SyndicatedContentResource resource = rhythmyxResourceFactory.syndicatedContentResource(subscription)
        SyndicatedContent syndicatedContent = rhythmyxResourceFactory.syndicatedContent(subscription, mediaContent, systemTitle)

        Response response

        try {
            response = resource.importContent(syndicatedContent, subscription.targetFolder as Long, subscription.contentType as String)
        } catch (e) {
            log.error("Exception occurred when trying to import content for rhythmyxSubscription '${subscription.id}'", e)
            return null
        }

        if(response.status != 200) {
            log.error("Received a ${response.status} status code when trying to import content for rhythmyxSubscription.id '${subscription.id}'")
            return null
        } else {
            return (response.entity as Guid).id
        }
    }

    def getContentTypes(rhythmyxSubscriber) {

        rhythmyxResourceFactory.contentTypesResource(rhythmyxSubscriber).contentTypes
    }

    boolean updateContentItem(RhythmyxSubscription subscription, syndicatedMediaItem) {

        SyndicatedContentResource resource = rhythmyxResourceFactory.syndicatedContentResource(subscription)
        SyndicatedContent syndicatedContent = rhythmyxResourceFactory.syndicatedContent(subscription, syndicatedMediaItem.content, syndicatedMediaItem.name)

        Response response

        try {
            response = resource.updateContent(syndicatedContent, subscription.contentId as Long)
        } catch (e) {
            log.error("Exception occurred when trying to update content for rhythmyxSubscription '${subscription.id}'", e)
            return false
        }

        if(response.status != 200) {
            log.error("Received a ${response.status} status code when trying to update content for rhythmyxSubscription.id '${subscription.id}'")
            return false
        }

        return true
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void transitionContentItem(RhythmyxSubscription rhythmyxSubscription, int transitionMode) {

        def rhythmyxWorkflow = rhythmyxSubscription.rhythmyxWorkflow

        def updateTransitions = {
            if(rhythmyxWorkflow.autoPublish) {
                return rhythmyxWorkflow.updateAutoPublishTransitions?.split(',') ?: new String[0]
            } else {
                return rhythmyxWorkflow.updateTransitions?.split(',') ?: new String[0]
            }
        }

        def deleteTransitions = {
            if(rhythmyxWorkflow.autoPublish) {
                return rhythmyxWorkflow.deleteAutoPublishTransitions?.split(',') ?: new String[0]
            } else {
                return rhythmyxWorkflow.deleteTransitions?.split(',') ?: new String[0]
            }
        }

        if(transitionMode == WORKFLOW_TRANSITION_IMPORT) {
            transitionContentItem(rhythmyxSubscription, rhythmyxWorkflow.importTransitions?.split(',') ?: new String[0])
        } else if(transitionMode == WORKFLOW_TRANSITION_UPDATE) {
            transitionContentItem(rhythmyxSubscription, updateTransitions())
        } else if(transitionMode == WORKFLOW_TRANSITION_DELETE) {
            transitionContentItem(rhythmyxSubscription, deleteTransitions())
        }
    }

    void transitionContentItem(RhythmyxSubscription rhythmyxSubscription, String [] transitions) {

        SyndicatedContentResource resource = rhythmyxResourceFactory.syndicatedContentResource(rhythmyxSubscription)

        def contentId = rhythmyxSubscription.contentId as Long

        transitions.each { String transition ->

            def response

            try {
                response = resource.transitionItem(contentId, transition.trim())
            } catch (e) {
                log.error("Exception occurred when trying to transition the content item '${contentId}' for rhythmyxSubscription '${rhythmyxSubscription.id} using the transition '${transition}'", e)
                return
            }

            if(response.status != 200) {
                log.error("Received a ${response.status} status code when trying to transition the content item '${contentId}' for rhythmyxSubscription '${rhythmyxSubscription.id} using the transition '${transition}'")
                return
            }

            log.info("Successfully transitioned rhythmyxSubscription '${rhythmyxSubscription.id}' using the '${transition}' transition")
        }
    }

    static class RhythmyxResourceFactory {

        def syndicatedContentResource(rhythmyxSubscription) {

            def servicesFactory = contentServiceFactory(rhythmyxSubscription.rhythmyxSubscriber)
            def resource = new SyndicatedContentResource()
            resource.servicesFactory = servicesFactory
            return resource
        }

        def contentTypesResource(rhythmyxSubscriber) {

            def servicesFactory = contentServiceFactory(rhythmyxSubscriber)
            def resource = new ContentTypesResource()
            resource.servicesFactory = servicesFactory
            return resource
        }

        private PercussionContentServicesFactory contentServiceFactory(rhythmyxSubscriber) {
            def servicesFactory = new PercussionContentServicesFactory()
            servicesFactory.host = rhythmyxSubscriber.rhythmyxHost
            servicesFactory.port = rhythmyxSubscriber.rhythmyxPort as Integer
            servicesFactory.community = rhythmyxSubscriber.rhythmyxCommunity
            servicesFactory.username = rhythmyxSubscriber.rhythmyxUser
            servicesFactory.password = rhythmyxSubscriber.rhythmyxPassword
            servicesFactory
        }

        @SuppressWarnings("GrMethodMayBeStatic")
        def syndicatedContent(subscription, mediaContent, systemTitle) {
            def content = new SyndicatedContent()
            content.content = mediaContent
            content.systemTitle = systemTitle
            content.targetUrl = subscription.sourceUrl
            return content
        }
    }
}
