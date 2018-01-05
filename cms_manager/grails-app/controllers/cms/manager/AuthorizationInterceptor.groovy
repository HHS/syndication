package cms.manager

import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.rest.security.ApiKeyUtils
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest


class AuthorizationInterceptor {

    AuthorizationInterceptor(){
        match(controller: 'authorization')
    }
    boolean before() {
        setSubscriberRequestAttributeAuth(request)
        String senderPublicKey = request.getAttribute('senderPublicKey')
        def keyAgreement = KeyAgreement.findByEntity2PublicKey(senderPublicKey)

        if (keyAgreement && Subscriber.findByKeyAgreement(keyAgreement)?.isPrivileged) {
            return true
        } else {
            render status: 401, text: ([message: "Unauthorized: the sender's public key is not valid"] as JSON).toString(), encoding: "UTF-8", contentType: 'application/json'
            return false
        }
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }

    private static void setSubscriberRequestAttributeAuth(HttpServletRequest request) {
        def rawAuthHeader = request.getHeader("Authorization")
        def authHeader = ApiKeyUtils.getAuthHeader(rawAuthHeader, System.currentTimeMillis())
        request.setAttribute("senderPublicKey", authHeader?.senderPublicKey)
    }
}
