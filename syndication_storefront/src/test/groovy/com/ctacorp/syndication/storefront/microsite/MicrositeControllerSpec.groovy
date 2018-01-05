package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.MicrositeService
import com.ctacorp.syndication.storefront.TagService
import com.ctacorp.syndication.storefront.UserMediaList
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.IgnoreRest
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MicrositeController)
@Build([User, MediaItem, Html, MicroSite, FeaturedMedia, UserMediaList, MediaSelector, com.ctacorp.syndication.media.Collection, Source, Language])
class MicrositeControllerSpec extends Specification {

    def tagService = Mock(TagService)
    def micrositeService = Mock(MicrositeService)

    def setup() {
        MediaItem mi = MediaItem.build()
        User user = User.build()
        MicroSite microSite = MicroSite.build(user:user)
        UserMediaList uml = UserMediaList.build(user:user, mediaItems: [mi])
        controller.mediaService = [getFeaturedMedia:{params -> [mi]}]
        controller.springSecurityService = [currentUser:user]
        controller.tagService = tagService
        controller.micrositeService = micrositeService

        views['/microsite/_collections.gsp'] = 'mock template contents'
    }

    def "index should return a model with featuredMedia and UserMicrosites"() {
        given:
            FeaturedMedia featuredMedia = FeaturedMedia.build(mediaItem:MediaItem.first())
        when:
            def model = controller.index()
        then:
            model.featuredMedia
            model.userMicrosites
    }

    def "show should direct to the correct microsite type"() {
        given: "four different microsite templates"
            def blog = MicroSite.build(templateType: MicroSite.TemplateType.BLOG)
            def grid = MicroSite.build(templateType: MicroSite.TemplateType.GRID)
            def carousel = MicroSite.build(templateType: MicroSite.TemplateType.CAROUSEL)
            def classic = MicroSite.build(templateType: MicroSite.TemplateType.CLASSIC)

        when: "blog is used for a show() call"
            controller.show(blog)
        then: "the blog view should be loaded"
            response.redirectUrl == "/blog/show/${blog.id}?showAdminControls=0"

        when: "grid is used for a show() call"
            response.reset()
            controller.show(grid)
        then: "the grid view should be loaded"
            response.redirectUrl == "/grid/show/${grid.id}?showAdminControls=0"

        when: "carousel is used for a show() call"
            response.reset()
            controller.show(carousel)
        then: "the carousel view should be loaded"
            response.redirectUrl == "/carousel/show/${carousel.id}?showAdminControls=0"

        when: "classic is used for a show() call"
            response.reset()
            controller.show(classic)
        then: "the classic view should be loaded"
            response.redirectUrl == "/classic/show/${classic.id}?showAdminControls=0"
    }

    def "edit should direct to the correct microsite type"() {
        given: "four different microsite templates"
            def blog = MicroSite.build(templateType: MicroSite.TemplateType.BLOG)
            def grid = MicroSite.build(templateType: MicroSite.TemplateType.GRID)
            def carousel = MicroSite.build(templateType: MicroSite.TemplateType.CAROUSEL)
            def classic = MicroSite.build(templateType: MicroSite.TemplateType.CLASSIC)

        when: "blog is used for a edit() call"
            controller.edit(blog)
        then: "the blog view should be loaded"
            response.redirectUrl == "/blog/edit/${blog.id}?showAdminControls=0"

        when: "grid is used for a edit() call"
            response.reset()
            controller.edit(grid)
        then: "the grid view should be loaded"
            response.redirectUrl == "/grid/edit/${grid.id}?showAdminControls=0"

        when: "carousel is used for a edit() call"
            response.reset()
            controller.edit(carousel)
        then: "the carousel view should be loaded"
            response.redirectUrl == "/carousel/edit/${carousel.id}?showAdminControls=0"

        when: "classic is used for a edit() call"
            response.reset()
            controller.edit(classic)
        then: "the classic view should be loaded"
            response.redirectUrl == "/classic/edit/${classic.id}?showAdminControls=0"
    }

    def "delete should direct to the correct microsite type"() {
        given: "four different microsite templates"
            def blog = MicroSite.build(templateType: MicroSite.TemplateType.BLOG)
            def grid = MicroSite.build(templateType: MicroSite.TemplateType.GRID)
            def carousel = MicroSite.build(templateType: MicroSite.TemplateType.CAROUSEL)
            def classic = MicroSite.build(templateType: MicroSite.TemplateType.CLASSIC)

        when: "blog is used for a edit() call"
            controller.delete(blog)
        then: "the blog view should be loaded"
            response.redirectUrl == "/blog/delete/${blog.id}?showAdminControls=0"

        when: "grid is used for a edit() call"
            response.reset()
            controller.delete(grid)
        then: "the grid view should be loaded"
            response.redirectUrl == "/grid/delete/${grid.id}?showAdminControls=0"

        when: "carousel is used for a edit() call"
            response.reset()
            controller.delete(carousel)
        then: "the carousel view should be loaded"
            response.redirectUrl == "/carousel/delete/${carousel.id}?showAdminControls=0"

        when: "classic is used for a edit() call"
            response.reset()
            controller.delete(classic)
        then: "the classic view should be loaded"
            response.redirectUrl == "/classic/delete/${classic.id}?showAdminControls=0"
    }

    def "redirectToSpecificMicrosite should show admincontrols if specified"() {
        given: "four different microsite templates"
            def blog = MicroSite.build(templateType: MicroSite.TemplateType.BLOG)
            def grid = MicroSite.build(templateType: MicroSite.TemplateType.GRID)
            def carousel = MicroSite.build(templateType: MicroSite.TemplateType.CAROUSEL)
            def classic = MicroSite.build(templateType: MicroSite.TemplateType.CLASSIC)
            params.showAdminControls = 1

        when: "blog is used for a show() call"
            controller.show(blog)
        then: "the blog view should be loaded"
            response.redirectUrl == "/blog/show/${blog.id}?showAdminControls=1"

        when: "grid is used for a show() call"
            response.reset()
            controller.show(grid)
        then: "the grid view should be loaded"
            response.redirectUrl == "/grid/show/${grid.id}?showAdminControls=1"

        when: "carousel is used for a show() call"
            response.reset()
            controller.show(carousel)
        then: "the carousel view should be loaded"
            response.redirectUrl == "/carousel/show/${carousel.id}?showAdminControls=1"

        when: "classic is used for a show() call"
            response.reset()
            controller.show(classic)
        then: "the classic view should be loaded"
            response.redirectUrl == "/classic/show/${classic.id}?showAdminControls=1"
    }

//    ------Current List --------
    def "currentList should redirect to index if an unsupported type is specified"() {
        given: "an unsupported media selector type"
            params.listType = "banana"
        when: "currentList is called"
            controller.currentList()
        then: "we should get a string containing no source and list"
            response.contentAsString == "No Media Source and list specified"
    }

    def "currentList should find userMediaLists for the current user"() {
        given: "a media selector and the user media list type"
            MediaSelector ms = MediaSelector.build()
            params.listType = "USER_MEDIA_LIST"
            params.mediaAreaId = ms.id
        when: "currentList is called"
            controller.currentList()
        then: "we shouldn't see an error about media lists not found"
            !response.text.contains("No personal Media Lists can be found")
    }

    def "currentList should show error if no userMediaLists are found"() {
        given: "a new user with no media lists"
            MediaSelector ms = MediaSelector.build()
            params.listType = "USER_MEDIA_LIST"
            params.mediaAreaId = ms.id
            controller.springSecurityService = [currentUser:User.build()]
        when: "currentList is called"
            controller.currentList()
        then: "we should see an error about media lists not being found"
            response.text.contains("No personal Media Lists can be found")
    }

    def "currentList should find collections"() {
        given: "a media selector and the collection type"
            com.ctacorp.syndication.media.Collection collection = com.ctacorp.syndication.media.Collection.build(language: Language.build(), name:"myCollection")
            MediaSelector ms = MediaSelector.build(selectionId:collection.id)
            params.listType = "COLLECTION"
            params.mediaAreaId = ms.id
        when: "currentList is called"
            controller.currentList()
        then: "response should render the collections template"
            response.text == "mock template contents"
    }

    def "currentList should find tags"() {
        given: "a mocked tagService"
            controller.tagService = [
                    getTagTypes:{[]},
                    getAllActiveTagLanguages:{[]},
                    getTag:{tagId -> [type:[id:1], language:[id:1]]},
                    listTags:{attr-> [tags:["some tag"]]}
            ]
            params.listType = "TAG"
        when: "currentList is called"
            controller.currentList()
        then: "there should be a template rendered"
            response.text
    }

    def "currentList should find sources"() {
        given: "a media selector and the source type"
            Source source = Source.build(name:"SomeSource")
            MediaSelector ms = MediaSelector.build(selectionId:source.id)
            params.mediaAreaId = 1
            params.listType = "SOURCE"
        when: "currentList is called"
            controller.currentList()
        then: "our source should exist in the response text"
            response.text.contains("SomeSource")
    }

//    ---------------- Specific List -------------

    def "specificList should find userMediaLists for the current user"() {
        given: "a media selector and the user media list type"
            MediaSelector ms = MediaSelector.build()
            params.listType = "USER_MEDIA_LIST"
            params.mediaAreaId = ms.id
        when: "currentList is called"
            controller.specificList()
        then: "we shouldn't see an error about media lists not found"
            !response.text.contains("No personal Media Lists can be found")
    }

    def "specificList should show error if no userMediaLists are found"() {
        given: "a new user with no media lists"
            MediaSelector ms = MediaSelector.build()
            params.listType = "USER_MEDIA_LIST"
            params.mediaAreaId = ms.id
            controller.springSecurityService = [currentUser:User.build()]
        when: "currentList is called"
            controller.specificList()
        then: "we should see an error about media lists not being found"
            response.text.contains("No personal Media Lists can be found")
    }

    def "specificList should find collections"() {
        given: "a media selector and the collection type"
            def collection = com.ctacorp.syndication.media.Collection.build(language: Language.build(), name:"myCollection")
            MediaSelector ms = MediaSelector.build(selectionId: collection.id)
            params.listType = "COLLECTION"
            params.mediaAreaId = ms.id

        when: "specificList is called"
            controller.specificList()

        then: "response should contain the name of the collection"
            response.text == 'mock template contents'
    }

    def "specificList should find tags"() {
        given: "a mocked tagService"
            controller.tagService = [
                    getTagTypes:{[]},
                    getAllActiveTagLanguages:{[]},
                    getTag:{tagId -> [type:[id:1], language:[id:1]]},
                    listTags:{attr-> [tags:["some tag"]]}
            ]
            params.listType = "TAG"
        when: "specificList is called"
            controller.specificList()
        then: "there should be a template rendered"
            response.text
    }

    def "specificList should find sources"() {
        given: "a media selector and the source type"
            Source source = Source.build(name:"SomeSource")
            MediaSelector ms = MediaSelector.build(selectionId:source.id)
            params.mediaAreaId = 1
            params.listType = "SOURCE"
        when: "specificList is called"
            controller.specificList()
        then: "our source should exist in the response text"
            response.text.contains("SomeSource")
    }

    def "searchTags should render tags as JSON"() {
        setup:
            controller.tagService = [getTagTypes:{[]},
                                     getAllActiveTagLanguages:{[]},
                                    listTags: {Map map -> return [tags:[name:"test"]]}]
        when:"searchTags is called"
            controller.searchTags()
        then:"tags should be returned as JSON"
            response.text == '{"name":"test"}'
    }

    def "summary should render the summary template"() {
        when:"summary is called"
        views['/microsite/_summary.gsp'] = 'mock template contents'
        controller.summary()
        then:"the summary tempate should be rendered"
        response.text == 'mock template contents'
    }

    def "displayStyle should render the fullPagePagination template"() {
        setup:"param values"
            controller.micrositeService = [getMediaContents: {def items -> return [1]}]
            params.offset = "0"
            params.mediaItems = "1"
        when:"summary is called"
            views['/microsite/_fullContentPagination.gsp'] = 'mock template contents'
            controller.displayStyle()
        then:"the summary tempate should be rendered"
            response.text == 'mock template contents'
    }

    // Static view loading actions --------------------------------------------------

    def "selectNewMicrosite should rely on default grails view behavior"() {
        when: "selectNewMicrosite is called"
            controller.selectNewMicrosite()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "blogGallery should rely on default grails view behavior"() {
        when: "blogGallery is called"
            controller.blogGallery()
        then: "we should be relying on grails default behavior for view rendering"
        view == null
        model == [:]
    }

    def "classicGallery should rely on default grails view behavior"() {
        when: "classicGallery is called"
        controller.classicGallery()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "carouselGallery should rely on default grails view behavior"() {
        when: "carouselGallery is called"
            controller.carouselGallery()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "gridGallery should rely on default grails view behavior"() {
        when: "gridGallery is called"
            controller.gridGallery()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "microsite should rely on default grails view behavior"() {
        when: "microsite is called"
            controller.microsite()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "templateBlog should rely on default grails view behavior"() {
        when: "templateBlog is called"
            controller.templateBlog()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "templateCarousel should rely on default grails view behavior"() {
        when: "templateCarousel is called"
            controller.templateCarousel()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "templateClassic should rely on default grails view behavior"() {
        when: "templateClassic is called"
            controller.templateClassic()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "templateGrid should rely on default grails view behavior"() {
        when: "templateGrid is called"
            controller.templateGrid()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }

    def "newTemplates should rely on default grails view behavior"() {
        when: "newTemplates is called"
            controller.newTemplates()
        then: "we should be relying on grails default behavior for view rendering"
            view == null
            model == [:]
    }
}