package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.media.MediaItem
/**
 * The logic for the syndicate this page widget
 *
 */
class SyndicateThisController {
    //def URLCleanupService

    def index() {
        String contentURL = params.contentURL
        if (!contentURL){return}
        //contentURL = URLCleanupService.clean(contentURL)

        def existingItem = MediaItem.findBySourceUrl(contentURL)

        if (existingItem){
            redirect controller:"storefront", action: "showContent", params: [id:existingItem.id]
        } else{
            redirect action: "request", params: [contentURL: params.contentURL]
        }
    }

    def request(){
        [contentURL: params.contentURL]
    }

    def badgeSnippet(){
        def style = params.style ?: "wide"
        [snippet:renderHTML(style)]
    }

    private String renderHTML(String style){
        def imgLink = asset.image(src: "badge/${style}.png", absolute:true, alt:"SyndicateThisContentBadge" )
        def innerCode = """\
<a title="Syndicate This" href="#" onclick="window.location=\
'${grailsApplication.config.grails.serverURL}/syndicateThis/index?contentURL=\
'+encodeURIComponent(document.URL); return false;">${imgLink}</a>"""

        innerCode
    }
}
