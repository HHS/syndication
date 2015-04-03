%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.Video" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'video.label', default: 'Video')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-video" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
            <g:if test="${videoInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="video.id.label" default="Id"/></dt>
                <dd  class="word_wrap">${videoInstance?.id}</dd>
            </g:if>

            <g:if test="${videoInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="video.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${videoInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${videoInstance?.source}">
                <dt id="source-label"  class="word_wrap"><g:message code="video.source.label" default="Source"/></dt>
                <dd  class="word_wrap"><g:link controller="source" action="show" id="${videoInstance?.source?.id}">${videoInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${videoInstance?.sourceUrl}">
                <dt id="sourceUrl-label"  class="word_wrap"><g:message code="video.sourceUrl.label" default="Source Url"/></dt>
                <dd  class="word_wrap"><a target="_blank" href="${videoInstance?.sourceUrl}"><g:fieldValue bean="${videoInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${videoInstance?.targetUrl}">
                <dt id="targetUrl-label"  class="word_wrap"><g:message code="video.targetUrl.label" default="Target Url"/></dt>
                <dd  class="word_wrap"><a target="_blank" href="${videoInstance?.targetUrl}"><g:fieldValue bean="${videoInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${videoInstance?.active && videoInstance?.visibleInStorefront}">
                <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${videoInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${videoInstance?.id}</a></dd>
            </g:if>

            <g:if test="${videoInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="video.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${videoInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${videoInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="video.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${videoInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${videoInstance?.language}">
                <dt id="language-label"  class="word_wrap"><g:message code="video.language.label" default="Language"/></dt>
                <dd  class="word_wrap">${videoInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${videoInstance?.description}">
                <dt id="description-label"  class="word_wrap"><g:message code="video.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${videoInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${videoInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label"  class="word_wrap"><g:message code="video.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${videoInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label"  class="word_wrap"><g:message code="video.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${videoInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label"  class="word_wrap"><g:message code="video.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${videoInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label"  class="word_wrap"><g:message code="video.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${videoInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label"  class="word_wrap"><g:message code="video.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${videoInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label"  class="word_wrap"><g:message code="video.dateContentPublished.label" default="Content Published"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${videoInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label"  class="word_wrap"><g:message code="video.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd  class="word_wrap"><g:formatDate date="${videoInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label"  class="word_wrap"><g:message code="video.active.label" default="Active"/></dt>
            <dd  class="word_wrap"><g:formatBoolean boolean="${videoInstance?.active}"/></dd>

            <dt id="active-label"  class="word_wrap"><g:message code="video.visibleInStorefront.label" default="Visible In Storefront"/></dt>
            <dd  class="word_wrap"><g:formatBoolean boolean="${videoInstance?.visibleInStorefront}"/></dd>

            <g:if test="${videoInstance?.externalGuid}">
                <dt id="externalGuid-label"  class="word_wrap"><g:message code="video.externalGuid.label" default="External Guide"/></dt>
                <dd  class="word_wrap"><g:fieldValue bean="${videoInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${videoInstance?.hash}">
                <dt id="hash-label"  class="word_wrap"><g:message code="video.hash.label" default="Hash"/></dt>
                <dd  class="word_wrap"><g:fieldValue bean="${videoInstance}" field="hash"/></dd>
            </g:if>

            <g:if test="${videoInstance?.duration}">
                <dt id="duration-label"  class="word_wrap"><g:message code="video.duration.label" default="Duration"/></dt>
                <dd  class="word_wrap"><g:fieldValue bean="${videoInstance}" field="duration"/></dd>
            </g:if>

            <g:if test="${videoInstance?.width}">
                <dt id="width-label"  class="word_wrap"><g:message code="video.width.label" default="Width"/></dt>
                <dd  class="word_wrap"><g:fieldValue bean="${videoInstance}" field="width"/></dd>
            </g:if>

            <g:if test="${videoInstance?.height}">
                <dt id="height-label"  class="word_wrap"><g:message code="video.height.label" default="Height"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${videoInstance}" field="height"/></dd>
            </g:if>

            <g:if test="${videoInstance?.alternateImages}">
                <dt id="alternateImages-label"  class="word_wrap"><g:message code="video.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${videoInstance.alternateImages}" var="a">
                    <dd  class="word_wrap"><g:link controller="alternateImage" params="[mediaId:videoInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${videoInstance?.campaigns}">
                <dt id="campaigns-label"  class="word_wrap"><g:message code="video.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${videoInstance.campaigns}" var="c">
                    <dd  class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${videoInstance?.extendedAttributes}">
                <dt id="extendedAttributes-label"  class="word_wrap"><g:message code="video.extendedAttributes.label" default="Extended Attributes"/></dt>
                <g:each in="${videoInstance.extendedAttributes}" var="d">
                    <dd  class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:videoInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>
            </dl>
        </div>
    </div>
    <fieldset class="buttons">
        <g:form  url="[resource:videoInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ videoInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" onclick="return confirm('Are you sure?');" value="Delete" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${videoInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: videoInstance]"/>
</div>
</body>
</html>
