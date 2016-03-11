package com.ctacorp.syndication.storefront.tools

import com.ctacorp.syndication.storefront.ReleaseNote
import grails.plugin.springsecurity.annotation.Secured

import static com.ctacorp.syndication.storefront.ReleaseNote.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.pegdown.*

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class ReleaseNoteController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond list(params), model:[releaseNoteInstanceCount: count()]
    }

    @Secured(['permitAll'])
    def show(ReleaseNote releaseNoteInstance) {
        respond releaseNoteInstance
    }

    def create() {
        respond new ReleaseNote(params)
    }

    def previewMarkdown(){
        PegDownProcessor peg = new PegDownProcessor()
        def htmlMarkdown = peg.markdownToHtml(params.markdownContentForPreview)
        render htmlMarkdown.encodeAsRaw()
    }

    @Transactional
    def save(ReleaseNote releaseNoteInstance) {
        if (releaseNoteInstance == null) {
            notFound()
            return
        }

        if (releaseNoteInstance.hasErrors()) {
            respond releaseNoteInstance.errors, view:'create'
            return
        }

        releaseNoteInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'releaseNote.label', default: 'ReleaseNote'), releaseNoteInstance.id])
                redirect releaseNoteInstance
            }
            '*' { respond releaseNoteInstance, [status: CREATED] }
        }
    }

    def edit(ReleaseNote releaseNoteInstance) {
        respond releaseNoteInstance
    }

    @Transactional
    def update(ReleaseNote releaseNoteInstance) {
        if (releaseNoteInstance == null) {
            notFound()
            return
        }

        if (releaseNoteInstance.hasErrors()) {
            respond releaseNoteInstance.errors, view:'edit'
            return
        }

        releaseNoteInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ReleaseNote.label', default: 'ReleaseNote'), releaseNoteInstance.id])
                redirect releaseNoteInstance
            }
            '*'{ respond releaseNoteInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(ReleaseNote releaseNoteInstance) {

        if (releaseNoteInstance == null) {
            notFound()
            return
        }

        releaseNoteInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ReleaseNote.label', default: 'ReleaseNote'), releaseNoteInstance.id])
                redirect controller:"storefront", action:"releaseInfo", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'releaseNote.label', default: 'ReleaseNote'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
