%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.Html" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'html.label', default: 'Html')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
    <asset:stylesheet src="popup-styles.css"/>
</head>

<body>
<div id="show-html" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>
    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
                <g:render template="/mediaItem/commonShowView" model="[mediaItemInstance: html, mediaType:'html']"/>

                <g:if test="${html?.structuredContentType}">
                    <dt id="name-label" class="word_wrap"><g:message code="html.structuredContentType.label" default="Structured Type"/></dt>
                    <dd class="word_wrap">${html.structuredContentType.prettyName}</dd>
                </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form url="[resource:html, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ html?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" action="delete" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete"/>
            </sec:ifAnyGranted>
            <g:link class="btn btn-default" action="index">
                Cancel
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" style="margin-right: 3px;" class="btn btn-success pull-right" id="${html?.id}" action="featureItem">
                    Feature this Item
                </g:link>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                <g:link controller="mediaPreviewThumbnail" style="margin-right: 3px;"  class="btn btn-warning pull-right" id="${html?.id}" action="flush">
                    Regenerate Thumbnail & Preview
                </g:link>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                <g:link controller="mediaItem" class="btn btn-primary pull-right" style="margin-right: 3px;" id="${html?.id}" action="resetHash">
                    Reset Hash
                </g:link>
                <g:link controller="mediaItem" class="btn btn-warning pull-right" style="margin-right: 3px;" id="${html?.id}" action="resetDBCache">
                    Flush DB Cache
                </g:link>
            </sec:ifAnyGranted>

        </g:form>

    </fieldset>

    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: html]"/>

</div>
</body>
</html>
