/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.metric.CampaignMetric
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'])
@Transactional(readOnly = true)
class CampaignMetricController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CampaignMetric.list(params), model: [campaignMetricInstanceCount: CampaignMetric.count()]
    }

    def show(CampaignMetric campaignMetricInstance) {
        respond campaignMetricInstance
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new CampaignMetric(params)
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def save(CampaignMetric campaignMetricInstance) {
        if (campaignMetricInstance == null) {
            notFound()
            return
        }

        if (campaignMetricInstance.hasErrors()) {
            flash.errors = campaignMetricInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond campaignMetricInstance.errors, view: 'create'
            return
        }

        campaignMetricInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'campaignMetricInstance.label', default: 'Campaign Metric'), [campaignMetricInstance.id]])
                redirect campaignMetricInstance
            }
            '*' { respond campaignMetricInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(CampaignMetric campaignMetricInstance) {
        respond campaignMetricInstance
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def update(CampaignMetric campaignMetricInstance) {
        if (campaignMetricInstance == null) {
            notFound()
            return
        }

        if (campaignMetricInstance.hasErrors()) {
            flash.errors = campaignMetricInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond campaignMetricInstance.errors, view: 'edit'
            return
        }

        campaignMetricInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'CampaignMetric.label', default: 'Campaign Metric'), [campaignMetricInstance.id]])
                redirect campaignMetricInstance
            }
            '*' { respond campaignMetricInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(CampaignMetric campaignMetricInstance) {

        if (campaignMetricInstance == null) {
            notFound()
            return
        }

        campaignMetricInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'CampaignMetric.label', default: 'Campaign Metric'), [campaignMetricInstance.id]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'campaignMetricInstance.label', default: 'Campaign Metric'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
