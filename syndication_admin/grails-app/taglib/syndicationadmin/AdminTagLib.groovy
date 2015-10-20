
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package syndicationadmin

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.health.FlaggedMedia

class AdminTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [
            serverLink: 'raw',
            mediaIcon:'raw',
            eventIcon:'raw',
            error:'raw',
            errors:'raw',
            message:'raw',
            messages:'raw',
            currentUser:'raw',
            healthFailureIcon:'raw'
    ]

    static namespace = "synd"

    def grailsApplication
    def springSecurityService

    def mediaIcon = { attrs, body ->
        String icon = "fa-check"
        switch(attrs.mediaType){
            case "html" :           icon = "fa-file-text-o"; break;
            case "video" :          icon = "fa-video-camera"; break;
            case "image" :          icon = "fa-picture-o"; break;
            case "infographic" :    icon = "fa-picture-o"; break;
            case "twitter" :        icon = "fa-twitter"; break;
            case "facebook" :       icon = "fa-facebook"; break;
            case "collection" :     icon = "fa-folder-open-o"; break;
        }

        out << "<i class=\"fa ${icon}\"></i>"
    }

    def serverLink = { attrs, body ->
        out << "<a href='${grailsApplication.config.grails.serverURL}' class='bannerLink'>"
        out << body()
        out << "</a>"
    }

    def error = {attrs ->
        if(!flash.error){ return }
        out << "<div class=\"row\"><div class=\"alert alert-danger alert-dismissable break-word\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><ul>"
        out << "<li>${flash.error}</li>"
        out << "</ul></div></div>"
    }

    def errors = { attrs ->
        if(!flash.errors){
            return
        }

        out << "<div class=\"row\"><div class=\"alert alert-danger alert-dismissable break-word\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><ul>"
        flash.errors.each{ error ->
            out << "<li>${error.message}</li>"
        }
        out << "</ul></div></div>"
    }

    def message = { attrs ->
        if(!flash.message){ return }

        out << "<div class='row'><div class=\"alert alert-info alert-dismissable break-word\">"
        out << "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>"
        out << flash.message
        out << "</div></div>"
    }

    def messages = { attrs ->
        if(!flash.messages){ return }

        out << "<div class='row'><div class=\"alert alert-info alert-dismissable break-word\">"
        out << "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><ul>"
        flash.messages.each{ msg ->
            out << "<li>${msg.message}</li>"
        }

        out << "</ul></div></div>"
    }

    def eventIcon = { attrs, body ->
        out << """<i class="fa """

        switch(attrs.type){
            case "crash": out << """fa-bolt"""; break
            case "error": out << """fa-times-circle-o"""; break
            case "system_start" : out << """fa-upload"""; break
            case "system_stop" : out << """fa-download"""; break
        }

        out << """ fa-fw"></i>"""
    }

    def shortenString = { attrs ->
        String text = attrs.string
        if(!text || text.size() < 100){
            return text
        }

        def parts = text.split("\\.")
        switch(parts.size()){
            case 1 : shortenSentence(text); break
            default : out << "${parts[0]}. ${parts[1]} ..."; break
        }
    }

    def currentUser = { attrs, body ->
        def user = springSecurityService.currentUser as User

        def displayName = user?.name?.encodeAsHTML()

        if(!user?.name){
            displayName = user?.username?.encodeAsHTML()
        }

        out <<  "${displayName}"
    }

    def healthFailureIcon = { attrs, body ->
        def prettyName = FlaggedMedia.FailureType."${attrs.failureType}".prettyName
        switch(attrs.failureType){
            case "UNREACHABLE":         out << "<i title='${prettyName}' class='fa fa-chain-broken fa-2x text-danger'></i>"
                break
            case "SERVER_ERROR":        out << "<i title='${prettyName}' class='fa fa-ban fa-2x text-danger'></i>"
                break
            case "UNEXTRACTABLE":       out << "<i title='${prettyName}' class='fa fa-warning fa-2x text-danger'></i>"
                break
            case "NO_CONTENT":          out << "<i title='${prettyName}' class='fa fa-file-o fa-2x text-danger'></i>"
                break
            case "SHORT_CONTENT":       out << "<i title='${prettyName}' class='fa fa-adjust fa-2x text-warning'></i>"
                break
            case "NO_PREVIEW_THUMBNAIL":out << "<i title='${prettyName}' class='fa fa-picture-o fa-2x text-warning'></i>"
                break
        }
    }

    def percentColor = { attrs ->
        switch(attrs.percent){
            case 80..100: out << 'success'; return
            case 60..<80: out << 'info'; return
            case 40..<60: out << 'warning'; return
            case 0..<40: out <<  'danger'; return
        }
    }

    private String shortenSentence(String text){
        def parts = text.split(" ")
        for(i in 0..15){
            if(i > parts.size()-1){ break }
            out << "${parts[i]} "
        }
        out << "..."
    }

}
