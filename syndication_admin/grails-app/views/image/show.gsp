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
<%@ page import="com.ctacorp.syndication.media.Image" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'image.label', default: 'Image')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-image" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
            <g:if test="${imageInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="image.id.label" default="Id"/></dt>
                <dd class="word_wrap">${imageInstance?.id}</dd>
            </g:if>

            <g:if test="${imageInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="image.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${imageInstance?.source}">
                <dt id="source-label" class="word_wrap"><g:message code="image.source.label" default="Source"/></dt>
                <dd class="word_wrap"><g:link controller="source" action="show" id="${imageInstance?.source?.id}">${imageInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${imageInstance?.sourceUrl}">
                <dt id="sourceUrl-label" class="word_wrap"><g:message code="image.sourceUrl.label" default="Source Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${imageInstance?.sourceUrl}"><g:fieldValue bean="${imageInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${imageInstance?.targetUrl}">
                <dt id="targetUrl-label" class="word_wrap"><g:message code="image.targetUrl.label" default="Target Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${imageInstance?.targetUrl}"><g:fieldValue bean="${imageInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${imageInstance?.active && imageInstance?.visibleInStorefront}">
                <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${imageInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${imageInstance?.id}</a></dd>
            </g:if>

            <g:if test="${imageInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="image.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${imageInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="image.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${imageInstance?.language}">
                <dt id="language-label" class="word_wrap"><g:message code="image.language.label" default="Language"/></dt>
                <dd class="word_wrap">${imageInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${imageInstance?.description}">
                <dt id="description-label" class="word_wrap"><g:message code="image.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${imageInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="image.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${imageInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="image.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${imageInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="image.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${imageInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="image.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${imageInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="image.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${imageInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label"class="word_wrap"><g:message code="image.dateContentPublished.label" default="Content Published"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${imageInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="image.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd class="word_wrap"><g:formatDate date="${imageInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label" class="word_wrap"><g:message code="image.active.label" default="Active"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${imageInstance?.active}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="image.visibleInStorefront.label" default="Visible In Storefront"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${imageInstance?.visibleInStorefront}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="image.manuallyManaged.label" default="Manually Managed"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${imageInstance?.manuallyManaged}"/></dd>

            <g:if test="${imageInstance?.externalGuid}">
                <dt id="externalGuid-label" class="word_wrap"><g:message code="image.externalGuid.label" default="External Guide"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${imageInstance?.hash}">
                <dt id="hash-label" class="word_wrap"><g:message code="image.hash.label" default="Hash"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="hash"/></dd>
            </g:if>

            <g:if test="${imageInstance?.width}">
                <dt id="width-label" class="word_wrap"><g:message code="image.width.label" default="Width"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="width"/></dd>
            </g:if>

            <g:if test="${imageInstance?.height}">
                <dt id="height-label" class="word_wrap"><g:message code="image.height.label" default="Height"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="height"/></dd>
            </g:if>

            <g:if test="${imageInstance?.imageFormat}">
                <dt id="imageFormat-label" class="property-label"><g:message code="image.imageFormat.label" default="Image Format"/></dt>
                <dd class="property-value" aria-labelledby="imageFormat-label"><g:fieldValue bean="${imageInstance}" field="imageFormat"/></dd>
            </g:if>

            <g:if test="${imageInstance?.altText}">
                <dt id="altText-label" class="word_wrap"><g:message code="image.altText.label" default="Alt Text"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${imageInstance}" field="altText"/></dd>
            </g:if>

            <g:if test="${imageInstance?.alternateImages}">
                <dt id="alternateImages-label" class="word_wrap"><g:message code="image.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${imageInstance.alternateImages}" var="a">
                    <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:imageInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${imageInstance?.campaigns}">
                <dt id="campaigns-label" class="word_wrap"><g:message code="image.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${imageInstance.campaigns}" var="c">
                    <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${imageInstance?.extendedAttributes}">
                <dt id="extendedAttributes-label" class="word_wrap"><g:message code="image.extendedAttributes.label" default="Extended Attributes"/></dt>
                <g:each in="${imageInstance.extendedAttributes}" var="d">
                    <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:imageInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>
            </dl>
        </div>
    </div>
    <fieldset class="buttons">
        <g:form  url="[resource:imageInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ imageInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" value="Delete" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${imageInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: imageInstance]"/>
</div>
</body>
</html>
