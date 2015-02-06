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
import com.ctacorp.syndication.manager.cms.RhythmyxSubscription
import com.ctacorp.syndication.manager.cms.RhythmyxSubscriptionTransitionService
import com.ctacorp.syndication.manager.cms.utils.exception.RhythmyxIngestionException
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(RhythmyxSubscriptionTransitionService)
@Build([RhythmyxSubscription])
class RhythmyxSubscriptionTransitionServiceSpec extends Specification {

    def rhythmyxIngestionService = Mock(RhythmyxIngestionService)

    RhythmyxSubscription rhythmyxSubscription1
    RhythmyxSubscription rhythmyxSubscription2
    def rhythmyxSubscriptons = []

    def setup() {

        rhythmyxSubscription1 = RhythmyxSubscription.build()
        rhythmyxSubscription2 = RhythmyxSubscription.build()

        rhythmyxSubscriptons.add(rhythmyxSubscription1)
        rhythmyxSubscriptons.add(rhythmyxSubscription2)

        service.rhythmyxIngestionService = rhythmyxIngestionService
    }

    void "do import transitions correctly handles a null rhythmyx subscription"() {

        when: "transitioning the rhythmyx subscriptions"

        service.doImportTransitions(null)

        then: "nothing is transitioned and no exceptions are thrown"

        noExceptionThrown()
    }

    void "do import transitions correctly handles a failed rhythmyx subscription"() {

        when: "doing import transitions"

        service.doImportTransitions(rhythmyxSubscription1)

        then: "transition the first rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription1, RhythmyxIngestionService.WORKFLOW_TRANSITION_IMPORT) >> {
            throw new RhythmyxIngestionException('The Wolfman Attacks!')
        }

        and: "no exceptions are thrown"

        notThrown(RhythmyxIngestionException)
    }

    void "do update transitions correctly handles a null rhythmyx subscription"() {

        when: "transitioning the rhythmyx subscriptions"

        service.doUpdateTransitions(null)

        then: "nothing is transitioned and no exceptions are thrown"

        noExceptionThrown()
    }

    void "do update transitions correctly handles multiple rhythmyx subscriptions"() {

        when: "transitioning the rhythmyx subscriptions"

        service.doUpdateTransitions(rhythmyxSubscriptons)

        then: "transition the first rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription1, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)

        and: "transition the second rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription2, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)
    }

    void "do update transitions correctly handles a failed rhythmyx subscription"() {

        when: "transitioning the rhythmyx subscriptions"

        service.doUpdateTransitions(rhythmyxSubscriptons)

        then: "transition the first rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription1, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE) >> {
            throw new RhythmyxIngestionException('The Wolfman Attacks!')
        }

        and: "transition the second rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription2, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)

        and: "no exceptions are thrown"

        notThrown(RhythmyxIngestionException)
    }

    void "do delete transitions correctly handles a null rhythmyx subscription"() {

        when: "transitioning the rhythmyx subscriptions"

        service.doDeleteTransitions(null)

        then: "nothing is transitioned and no exceptions are thrown"

        noExceptionThrown()
    }

    void "do delete transitions correctly handles multiple rhythmyx subscriptions"() {

        when: "doing import transitions"

        service.doDeleteTransitions(rhythmyxSubscriptons)

        then: "transition the first rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription1, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)

        and: "transition the second rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription2, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)
    }

    void "do delete transitions correctly handles a failed rhythmyx subscription"() {

        when: "transitioning the rhythmyx subscriptions"

        service.doDeleteTransitions(rhythmyxSubscriptons)

        then: "transition the first rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription1, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE) >> {
            throw new RhythmyxIngestionException('The Wolfman Attacks!')
        }

        and: "transition the second rhythmyx subscription"

        1 * rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription2, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)

        and: "no exceptions are thrown"

        notThrown(RhythmyxIngestionException)
    }
}
