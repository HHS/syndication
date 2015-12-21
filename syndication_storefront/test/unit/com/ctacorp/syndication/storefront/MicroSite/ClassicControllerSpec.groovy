package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.MicrositeService
import com.ctacorp.syndication.storefront.TagService
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.microsite.FlaggedMicrosite
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ClassicController)
@Mock([MicroSite, MediaSelector,UserMediaList, Collection, Source, Campaign, User, FlaggedMicrosite])
class ClassicControllerSpec extends Specification {

    def tagService = Mock(TagService)
    def micrositeService = Mock(MicrositeService)
    def sort = [[name:"Alphabetically",value:"name"], [name:"Authored Date",value:"dateContentAuthored"], [name:"Published Date", value:"dateContentPublished"]]
    def order = [[name:"Ascending", value:"asc"],[name:"Descending", value:"desc"]]

    def setup() {
        controller.tagService = tagService
        controller.micrositeService = micrositeService
        controller.micrositeService.metaClass.saveBuild = {Map params, String micrositeType -> return new MicroSite()}
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["templateType"] = MicroSite.TemplateType.CLASSIC
        params["user"] = new User()
        params["title"] = "microsite temp"
        return params
    }



    void "index should return a model with featuredMedia and UserMicrosites"() {
        when:
            controller.index()
        then:
            view == null
            response.redirectedUrl == null
            status == 200
    }

    void "create should provide the proper model and direct you to the create view"() {
        when: "create action is called"
            def model = controller.create()
        then: "the create view should be called with the proper model"
            view == null
            model.userMediaLists == []
            model.collections == []
            model.sources == []
            model.tags == null
            model.campaigns == []
            model.sort == sort
            model.order == order
            model.displayStyle == MediaSelector.DisplayStyle.values()
    }

    void "Save should save the classic microsite and redirect to the show page"() {
        when:"the save action is called"
            controller.save()

        then: "the classic microsite should get saved and be redirected to its show page"
            response.redirectedUrl == "/classic/show?showAdminControls=true"

    }

    void "when a classic microsite has an error you should be redirected to the create page"() {
        setup:
            controller.micrositeService.metaClass.saveBuild = {Map params, String micrositeType -> [hasErrors:{return true}, errors:"error"]}
        when:"the save action is called"
            controller.save()
        then: "the microsite should get saved and be redirected to its show page"
            view == "/classic/create"
    }

    void "Calling edit should return the right model and render the edit page"() {
        setup:
            populateValidParams(params)
            def micro = new MicroSite(params).save()
            params.id = 1
        when:"the save action is called"
            def model = controller.edit()
        then: "the microsite should get saved and be redirected to its show page"
            view == null
            response.redirectedUrl == null
            model.userMediaLists == []
            model.collections == []
            model.sources == []
            model.tags == null
            model.campaigns == []
            model.sort == sort
            model.order == order
            model.displayStyle == MediaSelector.DisplayStyle.values()
            model.microSite == micro

    }

    void "calling show should render the show view"() {
        setup:
            populateValidParams(params)
            def micro = new MicroSite(params).save()
            controller.micrositeService.metaClass.getMediaItems = {MediaSelector mediaArea -> return "item"}
            controller.micrositeService.metaClass.getMediaItems = {MediaSelector mediaArea, Integer num -> return "item"}
            params.id = 1
            params.collection = "collection"
        when:"the save action is called"
            controller.show()
        then: "the microsite should get saved and be redirected to its show page"
            view == "/classic/show"
            model.microSite == micro
            model.pane1MediaItems == "item"
            model.pane2MediaItems == "item"
            model.pane3MediaItems == "item"
            model.collection == "collection"
            model.apiBaseUrl == grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
    }


    void "when updating a microsite you should be redirected to its show page"() {
        setup:
            populateValidParams(params)
            def micro = new MicroSite(params).save()
            controller.micrositeService.metaClass.updateBuild = {MicroSite microsite, Map params -> return []}
        when:"the update action is called with a valid instance"
            controller.update(micro)
        then: "the microsite should be redirected to its show page"
            response.redirectedUrl == "/classic/show?showAdminControls=true"
    }

    void "when updating a microsite with an error you should be returned to its edit page"() {
        setup:
            populateValidParams(params)
            def micro = new MicroSite(params).save()
            def micro2 = new MicroSite()
            micro2.validate()
            controller.micrositeService.metaClass.updateBuild = {MicroSite microsite, Map params -> return micro2}
        when:"the update action is called"
            controller.update(micro)
        then: "the microsite should redirected to its edit page with the correct model"
            view == "/classic/edit"
            flash.errors != null
            model.userMediaLists == UserMediaList.list()
            model.collections == Collection.list()
            model.sources == Source.list()
            model.tags == tagService.getTagsByType("General")
            model.campaigns == Campaign.list()
            model.sort == sort
            model.order == order
            model.displayStyle == MediaSelector.DisplayStyle.values()
            model.microSite == micro2
    }

    void "when updating a microsite with an invalid id"() {
        setup:
            populateValidParams(params)
            def micro = new MicroSite(params)
        when:"the update action is called with an invalid instance"
            controller.update(micro)
        then: "the microsite should be redirected to its edit page"
            response.redirectedUrl == "/microsite/index"
            flash.error != null
    }

    void "when deleting a microsite with a valid instance"() {
        setup:"create microsite to be delted"
            populateValidParams(params)
            def micro = new MicroSite(params).save()
            MicroSite.count() == 1
        when:"calling delete with a valid instance"
            controller.delete(micro)
        then:"the item should be deleted and redirected to the index"
            response.redirectedUrl == "/microsite/index"
            MicroSite.count() == 0
            flash.message != null
            flash.error == null
    }

    void "when deleting a microsite with an invalid instance"() {
        setup:"create microsite to be delted"
            def micro = new MicroSite().save()
        when:"calling delete with a valid instance"
            controller.delete(micro)
        then:"the item should be deleted and redirected to the index"
            response.redirectedUrl == "/microsite/index"
            flash.error != null
            flash.message == null
    }
}
