/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import grails.transaction.Transactional

@Transactional
class RhythmyxSubscriptionTransitionService {

    def rhythmyxIngestionService

    void doImportTransitions(RhythmyxSubscription rhythmyxSubscription) {

        try {
            rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_IMPORT)
        } catch (e) {
            log.error("the import transition failed for the content item associated with rhythmyxSubscription '${rhythmyxSubscription.id}'", e)
        }
    }

    void doUpdateTransitions(List<RhythmyxSubscription> rhythmyxSubscriptions) {

        rhythmyxSubscriptions.each { RhythmyxSubscription rhythmyxSubscription ->
            try {
                rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_UPDATE)
            } catch (e) {
                log.error("the update transition failed for the content item associated with rhythmyxSubscription '${rhythmyxSubscription.id}'", e)
            }
        }
    }

    void doDeleteTransitions(List<RhythmyxSubscription> rhythmyxSubscriptions) {

        rhythmyxSubscriptions.each { RhythmyxSubscription rhythmyxSubscription ->
            try {
                rhythmyxIngestionService.transitionContentItem(rhythmyxSubscription, RhythmyxIngestionService.WORKFLOW_TRANSITION_DELETE)
            } catch (e) {
                log.error("the delete transition failed for the content item associated with rhythmyxSubscription '${rhythmyxSubscription.id}'", e)
            }
        }
    }
}
