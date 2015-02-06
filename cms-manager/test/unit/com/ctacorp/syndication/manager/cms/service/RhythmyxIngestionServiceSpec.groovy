/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.manager.cms.RhythmyxIngestionService
import com.ctacorp.syndication.manager.cms.RhythmyxSubscriber
import com.ctacorp.syndication.manager.cms.RhythmyxSubscription
import com.ctacorp.syndication.manager.cms.RhythmyxWorkflow
import com.ctacorp.syndication.manager.cms.utils.exception.RhythmyxIngestionException
import com.ctacorp.syndication.rsrw.ContentTypesResource
import com.ctacorp.syndication.rsrw.SyndicatedContentResource
import com.ctacorp.syndication.rsrw.model.Guid
import com.ctacorp.syndication.rsrw.model.SyndicatedContent
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
@TestFor(RhythmyxIngestionService)
@Build([RhythmyxSubscription, RhythmyxSubscriber, RhythmyxWorkflow])
class RhythmyxIngestionServiceSpec extends Specification {

    def rhythmyxResourceFactory = Mock(RhythmyxIngestionService.RhythmyxResourceFactory)
    def syndicatedContentResource = Mock(SyndicatedContentResource)
    def contentTypesResource = Mock(ContentTypesResource)
    def syndicatedContent = new SyndicatedContent()

    def setup() {
        service.rhythmyxResourceFactory = rhythmyxResourceFactory
    }

    void "rhythmyx import returns a 200 status code"() {

        given: "the RhythmyxSubscription"

        def rhythmyxSubscription = RhythmyxSubscription.build(contentType: 'Synd_ImportedContent', targetFolder: 123456, sourceUrl: 'http://sourceUrl.gov')

        and: "the content to import"

        def mediaContent = '<html><body><div class="syndicate">Donuts</div></body></html>'

        and: "the system title of the content item"

        def systemTitle = 'The System Title'

        when: "importing a media item"

        def contentId = service.importMediaItem(rhythmyxSubscription, mediaContent, systemTitle)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(rhythmyxSubscription) >> syndicatedContentResource

        and: "create a syndicated content instance"

        rhythmyxResourceFactory.syndicatedContent(rhythmyxSubscription, mediaContent, systemTitle) >> syndicatedContent

        and: "import the syndicated content and return a successful response"

        syndicatedContentResource.importContent(syndicatedContent, 123456l, rhythmyxSubscription.contentType) >> new FakeResponse(200, new Guid(1234))

        and: "the content id should match the guid"

        contentId == "1234"
    }

    void "rhythmyx import is returns a 400 status code"() {

        given: "the RhythmyxSubscription"

        def rhythmyxSubscription = RhythmyxSubscription.build(contentType: 'Synd_ImportedContent', targetFolder: 123456, systemTitle: 'The System Title', sourceUrl: 'http://sourceUrl.gov')

        and: "the content to import"

        def mediaContent = '<html><body><div class="syndicate">Donuts</div></body></html>'

        and: "the system title of the content item"

        def systemTitle = 'The System Title'

        when: "importing a media item"

        def contentId = service.importMediaItem(rhythmyxSubscription, mediaContent, systemTitle)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(rhythmyxSubscription) >> syndicatedContentResource

        and: "create a syndicated content instance"

        rhythmyxResourceFactory.syndicatedContent(rhythmyxSubscription, mediaContent, systemTitle) >> syndicatedContent

        and: "import the syndicated content and return a bad request response"

        syndicatedContentResource.importContent(syndicatedContent, 123456l, rhythmyxSubscription.contentType) >> new FakeResponse(400, null)

        and: "expect a null contentId"

        !contentId
    }

    void "get allowed content types returns a list of content types"() {

        given: "the rhythmyx subscriber"

        def rhythmyxSubscriber = RhythmyxSubscriber.build()

        when: "fetching the content types"

        def contentTypes = service.getContentTypes(rhythmyxSubscriber)

        then: "create a ContentTypesResource instance"

        rhythmyxResourceFactory.contentTypesResource(rhythmyxSubscriber) >> contentTypesResource

        then: "get the content types from the resource"

        contentTypesResource.contentTypes >> ['hamburgerType','beefaroniType']

        and: "the returned content types should match"

        contentTypes == ['hamburgerType','beefaroniType']
    }

    void "rhythmyx update returns a 200 status code"() {

        given: "the RhythmyxSubscription"

        def rhythmyxSubscription = RhythmyxSubscription.build(contentId: '98765', contentType: 'Synd_ImportedContent', targetFolder: 123456, systemTitle: 'The System Title', sourceUrl: 'http://sourceUrl.gov')

        and: "the syndicated media item to import"

        SyndicatedMediaItem syndicatedMediaItem = new SyndicatedMediaItem()
        syndicatedMediaItem.content = '<html><body><div class="syndicate">Donuts</div></body></html>'
        syndicatedMediaItem.name = 'The System Title!'

        when: "updating a media item"

        service.updateContentItem(rhythmyxSubscription, syndicatedMediaItem)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(rhythmyxSubscription) >> syndicatedContentResource
        and: "create a syndicated content instance"

        rhythmyxResourceFactory.syndicatedContent(rhythmyxSubscription, syndicatedMediaItem.content, syndicatedMediaItem.name) >> syndicatedContent

        and: "import the syndicated content and return a successful response"

        syndicatedContentResource.updateContent(syndicatedContent, 98765l) >> new FakeResponse(200)
    }

    void "update media item when rhythmyx returns a non 200 status code"() {

        given: "the RhythmyxSubscription"

        def rhythmyxSubscription = RhythmyxSubscription.build(contentId: '98765', contentType: 'Synd_ImportedContent', targetFolder: 123456, systemTitle: 'The System Title', sourceUrl: 'http://sourceUrl.gov')

        and: "the syndicated media item to import"

        SyndicatedMediaItem syndicatedMediaItem = new SyndicatedMediaItem()
        syndicatedMediaItem.content = '<html><body><div class="syndicate">Donuts</div></body></html>'
        syndicatedMediaItem.name = 'The System Title!'

        when: "updating a media item"

        def success = service.updateContentItem(rhythmyxSubscription, syndicatedMediaItem)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(rhythmyxSubscription) >> syndicatedContentResource

        and: "create a syndicated content instance"

        rhythmyxResourceFactory.syndicatedContent(rhythmyxSubscription, syndicatedMediaItem.content, syndicatedMediaItem.name) >> syndicatedContent

        and: "import the syndicated content and return a bad request response"

        syndicatedContentResource.updateContent(syndicatedContent, 98765l) >> new FakeResponse(400)

        and: "expect success to be false"

        !success
    }

    void "import transition returns a 200 status code for one transition"() {

        given: "A rhythmyx subscription with a rhythmyx workflow"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(importTransitions: ' Punch'))

        when: "transitioning an item using import transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_IMPORT)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the import transitions"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "import transition correctly handles empty transitions"() {

        given: "A rhythmyx subscription with a rhythmyx workflow"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(importTransitions: null))

        when: "transitioning an item using import transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_IMPORT)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "import transition returns a 200 status code for two transitions"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(importTransitions: ' Punch, Kick '))

        when: "transitioning an item using import transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_IMPORT)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the first import transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(200)

        and: "transition the item using the second import transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Kick') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "import transition returns a non 200 status code for the first transition"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(importTransitions: ' Punch, Kick '))

        when: "transitioning an item using import transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_IMPORT)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the first import transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(400)

        and: "transition the item using the second import transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Kick') >> new FakeResponse(200)

        and: "expect no rhythmyx ingestion exception to be thrown"

        notThrown(RhythmyxIngestionException)
    }

    void "update transition correctly handles empty transitions"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(updateTransitions: ' Punch'))

        when: "transitioning an item using update transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the update transitions"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "update transition returns a 200 status code for one transition"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(updateTransitions: ' Punch'))

        when: "transitioning an item using update transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the update transitions"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "update transition returns a 200 status code for one transition when auto publish is true"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(updateAutoPublishTransitions: ' Punch', autoPublish: true))

        when: "transitioning an item using update transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the update transitions"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "update transition returns a non 200 status code for the first transition"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(updateTransitions: ' Punch, Kick '))

        when: "transitioning an item using import transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the first update transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(400)

        and: "transition the item using the second update transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Kick') >> new FakeResponse(400)

        and: "expect no rhythmyx ingestion exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "delete transition returns a 200 status code for one transition"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(deleteTransitions: ' Fart'))

        when: "transitioning an item using delete transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the delete transitions"

        syndicatedContentResource.transitionItem(12345 as Long, 'Fart') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "delete transition returns a non 200 status code for the first transition"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(deleteTransitions: ' Punch, Kick '))

        when: "transitioning an item using delete transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the first delete transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Punch') >> new FakeResponse(400)

        and: "transition the item using the second delete transition"

        syndicatedContentResource.transitionItem(12345 as Long, 'Kick') >> new FakeResponse(200)

        and: "expect no rhythmyx ingestion exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    void "delete transition returns a 200 status code for one transition when auto publish is true"() {

        given: "A rhythmyx subscription"

        def subscription = RhythmyxSubscription.build(contentId: '12345', rhythmyxWorkflow: RhythmyxWorkflow.build(deleteAutoPublishTransitions: ' Fart', autoPublish: true))

        when: "transitioning an item using delete transitions"

        service.transitionContentItem(subscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)

        then: "create a syndicated content resource instance"

        rhythmyxResourceFactory.syndicatedContentResource(subscription) >> syndicatedContentResource

        and: "transition the item using the delete transitions"

        syndicatedContentResource.transitionItem(12345 as Long, 'Fart') >> new FakeResponse(200)

        and: "expect no exception to be thrown"

        notThrown RhythmyxIngestionException
    }

    @SuppressWarnings(["GroovyUnusedDeclaration"])
    class FakeResponse extends Response {

        private Object entity
        private int status

        FakeResponse(int status, entity) {
            this.status = status
            this.entity = entity
        }

        FakeResponse(status) {
            this.status = status
        }

        @Override
        Object getEntity() {
            return entity
        }

        @Override
        int getStatus() {
            return status
        }

        @Override
        MultivaluedMap<String, Object> getMetadata() {
            return null
        }
    }
}
