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
<%@ page import="com.ctacorp.syndication.media.Infographic" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'infographic.label', default: 'Infographic')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-infographic" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row"></div>
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
            <g:if test="${infographicInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="infographic.id.label" default="Id"/></dt>
                <dd class="word_wrap">${infographicInstance?.id}</dd>
            </g:if>

            <g:if test="${infographicInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="infographic.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.source}">
                <dt id="source-label" class="word_wrap"><g:message code="infographic.source.label" default="Source"/></dt>
                <dd class="word_wrap"><g:link controller="source" action="show" id="${infographicInstance?.source?.id}">${infographicInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${infographicInstance?.sourceUrl}">
                <dt id="sourceUrl-label" class="word_wrap"><g:message code="infographic.sourceUrl.label" default="Source Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${infographicInstance?.sourceUrl}"><g:fieldValue bean="${infographicInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${infographicInstance?.targetUrl}">
                <dt id="targetUrl-label" class="word_wrap"><g:message code="infographic.targetUrl.label" default="Target Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${infographicInstance?.targetUrl}"><g:fieldValue bean="${infographicInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${infographicInstance?.active && infographicInstance?.visibleInStorefront}">
                <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${infographicInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${infographicInstance?.id}</a></dd>
            </g:if>

            <g:if test="${infographicInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="infographic.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="infographic.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.language}">
                <dt id="language-label" class="word_wrap"><g:message code="infographic.language.label" default="Language"/></dt>
                <dd class="word_wrap">${infographicInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${infographicInstance?.description}">
                <dt id="description-label" class="word_wrap"><g:message code="infographic.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${infographicInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="infographic.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="infographic.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="infographic.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="infographic.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="infographic.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label" class="word_wrap"><g:message code="infographic.dateContentPublished.label" default="Content Published"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="infographic.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd class="word_wrap"><g:formatDate date="${infographicInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label" class="word_wrap"><g:message code="infographic.active.label" default="Active"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${infographicInstance?.active}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="infographic.visibleInStorefront.label" default="Visible In Storefront"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${infographicInstance?.visibleInStorefront}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="infographic.manuallyManaged.label" default="Manually Managed"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${infographicInstance?.manuallyManaged}"/></dd>

            <g:if test="${infographicInstance?.externalGuid}">
                <dt id="externalGuid-label" class="word_wrap"><g:message code="infographic.externalGuid.label" default="External GUID"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.hash}">
                <dt id="hash-label" class="word_wrap"><g:message code="infographic.hash.label" default="Hash"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="hash"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.height}">
                <dt id="height-label" class="word_wrap"><g:message code="infographic.height.label" default="Height"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="height"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.width}">
                <dt id="width-label" class="word_wrap"><g:message code="infographic.width.label" default="Width"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="width"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.imageFormat}">
                <dt id="imageFormat-label" class="word_wrap"><g:message code="infographic.imageFormat.label" default="Image Format"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="imageFormat"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.altText}">
                <dt id="altText-label" class="word_wrap"><g:message code="infographic.altText.label" default="Alt Text"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${infographicInstance}" field="altText"/></dd>
            </g:if>

            <g:if test="${infographicInstance?.alternateImages}">
                <dt id="alternateImages-label" class="word_wrap"><g:message code="infographic.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${infographicInstance.alternateImages}" var="a">
                    <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:infographicInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${infographicInstance?.extendedAttributes}">
                    <dt id="extendedAttributes-label" class="word_wrap"><g:message code="infographic.extendedAttributes.label" default="Extended Attributes"/><dt>
                    <g:each in="${infographicInstance.extendedAttributes}" var="d">
                        <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:infographicInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                    </g:each>
            </g:if>

            <g:if test="${infographicInstance?.campaigns}">
                <dt id="campaigns-label" class="word_wrap"><g:message code="infographic.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${infographicInstance?.campaigns}" var="c">
                    <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:infographicInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ infographicInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
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
                <g:link controller="featuredMedia" id="${infographicInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: infographicInstance]"/>
</div>
</body>
</html>
