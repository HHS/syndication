package syndicationstorefront

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole

class StorefrontTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [currentUser: 'raw', mediaListApiLink:'raw', pageContentAnchor:'raw', hasErrors: 'raw', errors:'raw']
    def grailsApplication

    static namespace = "sf"

    def springSecurityService

    def currentUser = { attrs, body ->
        def user = springSecurityService.currentUser as User
        out << "<ul>"
        if(!user){
            out <<  "<li> ${g.link(controller:'login', action:'register'){'<i class=\'fa fa-plus\'></i> Register'}}</li>" +
                    "<li> ${g.link(controller:'login', action:'auth'){'<i class=\'fa fa-sign-in\'></i> Login'}}</li>"
            return
        }
        def displayName = user.name.encodeAsHTML()
        if(!user.name){
            displayName = user.username.encodeAsHTML()
        }

        out <<  "<li><span style='text-transform: lowercase;'>${g.link(controller:'login', action:'userAccount'){'<i class="fa fa-user fa-fw"></i> ' + displayName}}</span></li>" +
                "<li>${g.link(controller:'userMediaList', action:'index'){'<i class="fa fa-list-ul fa-fw"></i> Lists'}}</li>"
        if(UserRole.findByUser(user).role.authority in ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER']){
            out << "<li>${g.link(controller:'microsite', action:'index'){'<i class="fa fa-sitemap fa-fw"></i> Microsites'}}</li>"
        }
        out << "<li>${g.link(controller:'logout', action:'index'){'<i class="fa fa-sign-out"></i> Logout'}}</li>"
        out << "</ul>"
    }

    /**
     * If errors exist in the flash.errors, this tag cleanly renders them
     */
    def hasErrors = {
        if (flash.errors) {
            out << '<div class="errors">'
            out << '<ul>'
            flash.errors.each { er ->
                out << "<li>${er.encodeAsHTML()}</li>"
            }
            out << '</ul></div>'
            return out
        }
        return ''
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

    def mediaListApiLink = { attrs, body ->
        if(attrs.id){
            String linkText = "${grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath}/resources/userMediaLists/${attrs.id}"
            out << "<a href='${linkText}'>${linkText.encodeAsHTML()}</a>"
        } else{
            out << ""
        }
    }

    /**
     * Render the content anchor (used for 'skip to content' links)
     */
    def pageContentAnchor = {
        out << "<a style='float:left' id='pageContent'>&nbsp;</a>"
    }
}