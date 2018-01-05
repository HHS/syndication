package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.media.MediaItem
import grails.util.Holders

/**
 * The logic for the syndicate this page widget
 *
 */
class SyndicateThisController {
    //def URLCleanupService
    def config = Holders.config
    def index() {
        String contentURL = params.contentURL
        if (!contentURL){return}
        //contentURL = URLCleanupService.clean(contentURL)
        def existingItem = MediaItem.findBySourceUrl(contentURL)

        if (existingItem){
            redirect controller:"storefront", action: "showContent", params: [id:existingItem.id]
        } else{
            render view: "request", params: [contentURL: params.contentURL]
        }
    }

    def request(){
        [contentURL: params.contentURL]
    }

    def badgeSnippet(){
        def style = params.style ?: "wide"
        [snippet:renderHTML(style), API_SERVER_URL : config?.API_SERVER_URL]
    }

    private String renderHTML(String style){
        def innerCode = """\
<a title="Syndicate This" href="#" onclick="window.location=\
'${Holders.config.STOREFRONT_SERVER_URL}/syndicateThis/index?contentURL=\
'+encodeURIComponent(document.URL); return false;">\
<img src="${config?.STOREFRONT_SERVER_URL}/assets/badge/wide.png" alt="SyndicateThisContentBadge"/>\
</a>"""

        innerCode
    }
}
