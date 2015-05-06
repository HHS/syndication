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
<%@ page import="com.ctacorp.syndication.media.SocialMedia" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'socialMedia.label', default: 'Social Media')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-socialMedia" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
            <g:if test="${socialMediaInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="socialMedia.id.label" default="Id"/></dt>
                <dd class="word_wrap">${socialMediaInstance?.id}</dd>
            </g:if>

            <g:if test="${socialMediaInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="socialMedia.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.source}">
                <dt id="source-label" class="property-label"><g:message code="socialMedia.source.label" default="Source"/></dt>
                <dd class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${socialMediaInstance?.source?.id}">${socialMediaInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.sourceUrl}">
                <dt id="sourceUrl-label" class="word_wrap"><g:message code="socialMedia.sourceUrl.label" default="Source Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${socialMediaInstance?.sourceUrl}"><g:fieldValue bean="${socialMediaInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.targetUrl}">
                <dt id="targetUrl-label" class="word_wrap"><g:message code="socialMedia.targetUrl.label" default="Target Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${socialMediaInstance?.targetUrl}"><g:fieldValue bean="${socialMediaInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.active && socialMediaInstance?.visibleInStorefront}">
                <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${socialMediaInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${socialMediaInstance?.id}</a></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="socialMedia.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="socialMedia.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.language}">
                <dt id="language-label" class="word_wrap"><g:message code="socialMedia.language.label" default="Language"/></dt>
                <dd class="word_wrap">${socialMediaInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${socialMediaInstance?.description}">
                <dt id="description-label" class="word_wrap"><g:message code="socialMedia.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${socialMediaInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="socialMedia.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="socialMedia.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="socialMedia.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="socialMedia.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="socialMedia.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label" class="word_wrap"><g:message code="socialMedia.dateContentPublished.label" default="Content Published"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="socialMedia.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd class="word_wrap"><g:formatDate date="${socialMediaInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label" class="word_wrap"><g:message code="socialMedia.active.label" default="Active"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${socialMediaInstance?.active}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="socialMedia.visibleInStorefront.label" default="visible In Storefront"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${socialMediaInstance?.visibleInStorefront}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="socialMedia.manuallyManaged.label" default="Manually Managed"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${socialMediaInstance?.manuallyManaged}"/></dd>

            <g:if test="${socialMediaInstance?.externalGuid}">
                <dt id="externalGuid-label" class="word_wrap"><g:message code="socialMedia.externalGuid.label" default="External Guide"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.hash}">
                <dt id="hash-label" class="word_wrap"><g:message code="socialMedia.hash.label" default="Hash"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="hash"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.socialMediaType}">
                <dt id="socialMediaType-label" class="word_wrap"><g:message code="socialMedia.socialMediaType.label" default="Social Media Type"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${socialMediaInstance}" field="socialMediaType"/></dd>
            </g:if>

            <g:if test="${socialMediaInstance?.alternateImages}">
                <dt id="alternateImages-label" class="word_wrap"><g:message code="socialMedia.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${socialMediaInstance.alternateImages}" var="a">
                    <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:socialMediaInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${socialMediaInstance?.extendedAttributes}">
                <dt id="extendedAttributes-label" class="word_wrap"><g:message code="socialMedia.extendedAttributes.label" default="Extended Attributes"/></dt>
                <g:each in="${socialMediaInstance.extendedAttributes}" var="d">
                    <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:socialMediaInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${socialMediaInstance?.campaigns}">
                <dt id="campaigns-label" class="word_wrap"><g:message code="socialMedia.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${socialMediaInstance.campaigns}" var="c">
                    <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:socialMediaInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ socialInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${socialMediaInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: socialMediaInstance]"/>
</div>
</body>
</html>
