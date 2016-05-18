
    <%@ page import="com.ctacorp.syndication.commons.util.Util" %>
    <div style="width: 100%; overflow: hidden;">
        <g:if test="${!mediaItemInstance?.foreignSyndicationAPIUrl}">
            <g:render template="/storefront/internalSyndicationForm"/>
        </g:if>
        <g:else>
            <g:render template="/storefront/thirdPartySyndicationForm"/>
        </g:else>

        <div style="margin-left: 420px;">
            <g:textArea name="embedCode" cols="80" rows="10" id="snippetCode"/>
        </div>

    </div>
    <g:if test="${(mediaItemInstance && mediaItemInstance instanceof com.ctacorp.syndication.media.Collection) || (userMediaListInstance) || (renderTagList) || (renderSourceList)}">
        <div style="margin-top: 25px;">
            <h3 style="margin-bottom: 10px;">Collection Display Options</h3>
            <p>
                <g:radioGroup name="displayMethod"
                              id="displayMethod"
                              labels="['Feed', 'List', 'Media Viewer']"
                              values="['feed', 'list', 'mv']"
                              value="feed">
                    ${it.radio} ${it.label}
                </g:radioGroup>
            </p>
        </div>
    </g:if>

