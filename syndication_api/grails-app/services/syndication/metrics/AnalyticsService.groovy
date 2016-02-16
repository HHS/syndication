
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.metrics

import com.ctacorp.syndication.media.MediaItem

class AnalyticsService {
    static transactional = false

    def grailsApplication

    /*
        Following are currently unused, but can be added in the future:
        - userId: add an ID for the user generating a snippet
        - campaignId: add the campaign this item belongs to
        - campaignName: add the name of the campaign this item belongs to
     */

    String getGoogleAnalyticsString(MediaItem mi, params = null){
        def mediaId = mi.id
        def mediaType = mi.getClass().simpleName.toLowerCase()
        def sourceUrl = URLEncoder.encode(mi.sourceUrl, "UTF-8")
        def sourceId = mi.source.id
        def sourceAcronym = mi.source.acronym
        def languageId = mi.language.id
        def isoCode = mi.language.isoCode
        def userId = params?.userId ?: -1

        String iframe = """\
<iframe src="//www.googletagmanager.com/ns.html?id=GTM-KT9TM9&\
mediaId=${mediaId}&\
mediaType=${mediaType}&\
sourceUrl=${sourceUrl}&\
userId=${userId}&\
sourceId=${sourceId}&\
sourceAcronym=${sourceAcronym}&\
campaignId=-1&\
campaignName=null&\
languageId=${languageId}\
&isoCode=${isoCode}" \
height="0" width="0" style="display:none;visibility:hidden"></iframe>"""

        String analyticsBlock = """\
${iframe}<noscript>${iframe}</noscript>"""

        analyticsBlock
    }

    String encode(String toEncode){
        URLEncoder.encode(toEncode, "UTF-8")
    }

    String uuid(){
        encode("${UUID.randomUUID()}")
    }
}
