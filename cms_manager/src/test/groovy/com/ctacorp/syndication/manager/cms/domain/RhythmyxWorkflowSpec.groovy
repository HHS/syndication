/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.domain

import com.ctacorp.syndication.manager.cms.RhythmyxWorkflow
import grails.buildtestdata.mixin.Build
import spock.lang.Specification

@Build([RhythmyxWorkflow])
class RhythmyxWorkflowSpec extends Specification {

    void "an instance with non-null and non-blank values is valid"() {

        given: "an instance with non-null and non-blank values"

        def rhythmyxWorkflow = RhythmyxWorkflow.build(autoPublish: true,
                importTransitions: null,
                updateTransitions: null,
                updateAutoPublishTransitions: null,
                deleteTransitions: null,
                deleteAutoPublishTransitions: null)

        when: "the instance is validated"

        rhythmyxWorkflow.save(flush: true)

        then: "it should not have errors"

        0 == rhythmyxWorkflow.errors.errorCount
    }

    void "an instance with all null values is valid"() {

        given: "an instance with non-null and non-blank values"

        def rhythmyxWorkflow = RhythmyxWorkflow.build(autoPublish: true,
                importTransitions: null,
                updateTransitions: null,
                updateAutoPublishTransitions: null,
                deleteTransitions: null,
                deleteAutoPublishTransitions: null)

        when: "the instance is validated"

        rhythmyxWorkflow.save(flush: true)

        then: "it should not have errors"

        0 == rhythmyxWorkflow.errors.errorCount
    }

    void "an instance with all blank values is valid"() {

        given: "an instance with non-null and non-blank values"

        def rhythmyxWorkflow = RhythmyxWorkflow.build(autoPublish: true,
                importTransitions: '',
                updateTransitions: '',
                updateAutoPublishTransitions: '',
                deleteTransitions: '',
                deleteAutoPublishTransitions: '')

        when: "the instance is validated"

        rhythmyxWorkflow.save(flush: true)

        then: "it should not have errors"

        0 == rhythmyxWorkflow.errors.errorCount
    }
}
