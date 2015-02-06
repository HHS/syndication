
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import static org.springframework.http.HttpStatus.*

import com.ctacorp.syndication.MediaMetric
import com.ctacorp.syndication.MediaItem
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
@Transactional(readOnly = true)
class MediaMetricController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: "media"
        respond MediaMetric.list(params), model:[mediaMetricInstanceCount: MediaMetric.count()]
    }

    def show(MediaMetric mediaMetricInstance) {
        respond mediaMetricInstance
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new MediaMetric(params)
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def save(MediaMetric mediaMetricInstance) {
        if (mediaMetricInstance == null) {
            notFound()
            return
        }

        if (mediaMetricInstance.hasErrors()) {
            flash.errors = mediaMetricInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond mediaMetricInstance.errors, view:'create'
            return
        }

        if(!MediaItem.get(mediaMetricInstance.media.id)){
            flash.error = "That media Item does not exist."
            respond mediaMetricInstance.errors, view:'create'
            return
        }

        mediaMetricInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'mediaMetricInstance.label', default: 'Media Metric'), mediaMetricInstance.id])
                redirect mediaMetricInstance
            }
            '*' { respond mediaMetricInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(MediaMetric mediaMetricInstance) {
        respond mediaMetricInstance
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def update(MediaMetric mediaMetricInstance) {
        if (mediaMetricInstance == null) {
            notFound()
            return
        }

        if (mediaMetricInstance.hasErrors()) {
            flash.errors = mediaMetricInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond mediaMetricInstance.errors, view:'edit'
            return
        }

        mediaMetricInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'MediaMetric.label', default: 'Media Metric'), mediaMetricInstance.id])
                redirect mediaMetricInstance
            }
            '*'{ respond mediaMetricInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(MediaMetric mediaMetricInstance) {

        if (mediaMetricInstance == null) {
            notFound()
            return
        }

        mediaMetricInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'MediaMetric.label', default: 'Media Metric'), mediaMetricInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'mediaMetricInstance.label', default: 'Media Metric'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
