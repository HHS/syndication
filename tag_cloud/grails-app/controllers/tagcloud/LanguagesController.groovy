package tagcloud

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.commons.util.Util
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import tagcloud.domain.Tag

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NOT_MODIFIED
import static org.springframework.http.HttpStatus.OK

@Secured(['permitAll'])
class LanguagesController {
    static responseFormats = ["json"]
    static allowedMethods = [index:'GET', activate:'POST', deactivate:'POST']

    def tagService

    @Transactional(readOnly = true)
    def index() {
        if(params?.allTags?.equals("true")){
            respond Language.list(sort:"name", order:"asc"), model: [languageInstanceCount: Language.count()]
            return
        }
        respond Language.findAllIsActive(sort:"name", order:"asc"), model: [languageInstanceCount: Language.count()]
    }

    @Transactional(readOnly = true)
    def getActive() {
        respond Language.findAllIsActive(sort:"name", order:"asc"), model: [languageInstanceCount: Language.count()]
    }

    @Transactional
    def activate() {
        def data = request.JSON
        Long languageId = data.languageId as Long
        if (languageId == null) {
            failedNotFound("activated")
            return
        }

        Language languageInstance = Language.get(languageId)
        if (!languageInstance) {
            failedNotFound("activated")
            return
        }
        languageInstance.isActive = true
        languageInstance.save flush: true

        render text:([success:true, message:"Tag Language: ${languageInstance} has been activated."] as JSON), contentType: "application/json"
    }

    @Transactional
    def deactivate() {
        def data = request.JSON
        Long languageId = data.languageId as Long
        if (languageId == null) {
            failedNotFound("deactivated")
            return
        }

        Language languageInstance = Language.get(languageId)
        if (!languageInstance) {
            failedNotFound("deactivated")
            return
        }
        def tagExists = tagService.tagExists(languageInstance)
        if (tagExists) { //this is currently an error (language is in use)
            failedAssociatedTag()
            return
        }
        languageInstance.isActive = false
        languageInstance.save flush:true

        render text:([success:true, message:"Tag Language: ${languageInstance} has been deactivated."] as JSON), contentType: "application/json"
    }

    def failedNotFound(String action) {
        render text:([success:false, message:"The language could not be ${action}"] as JSON), contentType: "application/json"
    }

    def failedAssociatedTag() {
        render text:([success:false, message:"Sorry! Languages currently associated with a tag cannot be disabled"] as JSON), contentType: "application/json"
    }
}


