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
<%@ page import="com.ctacorp.syndication.media.Widget" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'widget.label', default: 'Widget')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-widget" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-lg-6  col-lg-offset-2">
            <dl class="dl-horizontal">

            <g:if test="${widgetInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="widget.id.label" default="Id"/></dt>
                <dd class="word_wrap">${widgetInstance?.id}</dd>
            </g:if>

            <g:if test="${widgetInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="widget.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.source}">
                <dt id="source-label" class="word_wrap"><g:message code="widget.source.label" default="Source"/></dt>
                <dd class="word_wrap"><g:link controller="source" action="show" id="${widgetInstance?.source?.id}">${widgetInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${widgetInstance?.sourceUrl}">
                <dt id="sourceUrl-label" class="word_wrap"><g:message code="widget.sourceUrl.label" default="Source Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${widgetInstance?.sourceUrl}"><g:fieldValue bean="${widgetInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${widgetInstance?.targetUrl}">
                <dt id="targetUrl-label" class="word_wrap"><g:message code="widget.targetUrl.label" default="Target Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${widgetInstance?.targetUrl}"><g:fieldValue bean="${widgetInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${widgetInstance?.active && widgetInstance?.visibleInStorefront}">
                <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${widgetInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${widgetInstance?.id}</a></dd>
            </g:if>

            <g:if test="${widgetInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="widget.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="widget.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.language}">
                <dt id="language-label" class="word_wrap"><g:message code="widget.language.label" default="Language"/></dt>
                <dd class="word_wrap">${widgetInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${widgetInstance?.description}">
                <dt id="description-label" class="word_wrap"><g:message code="widget.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${widgetInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="widget.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="widget.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="widget.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="widget.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="widget.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label" class="word_wrap"><g:message code="widget.dateContentPublished.label" default="Content Published"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="widget.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd class="word_wrap"><g:formatDate date="${widgetInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label" class="word_wrap"><g:message code="widget.active.label" default="Active"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${widgetInstance?.active}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="widget.visibleInStorefront.label" default="Visible In Storefront"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${widgetInstance?.visibleInStorefront}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="widget.manuallyManaged.label" default="Manually Managed"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${widgetInstance?.manuallyManaged}"/></dd>

            <g:if test="${widgetInstance?.externalGuid}">
                <dt id="externalGuid-label" class="word_wrap"><g:message code="widget.externalGuid.label" default="External Guide"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.hash}">
                <dt id="hash-label" class="word_wrap"><g:message code="widget.hash.label" default="Hash"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="hash"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.width}">
                <dt id="width-label" class="word_wrap"><g:message code="widget.width.label" default="Width"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="width"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.height}">
                <dt id="height-label" class="word_wrap"><g:message code="widget.height.label" default="Height"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="height"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.code}">
                <dt id="code-label" class="word_wrap"><g:message code="widget.code.label" default="Code"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${widgetInstance}" field="code"/></dd>
            </g:if>

            <g:if test="${widgetInstance?.alternateImages}">
                <dt id="alternateImages-label" class="word_wrap"><g:message code="widget.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${widgetInstance.alternateImages}" var="a">
                    <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:widgetInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${widgetInstance?.campaigns}">
                <dt id="campaigns-label" class="word_wrap"><g:message code="widget.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${widgetInstance.campaigns}" var="c">
                    <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${widgetInstance?.extendedAttributes}">
                <dt id="extendedAttributes-label" class="word_wrap"><g:message code="widget.extendedAttributes.label" default="Extended Attributes"/></dt>
                <g:each in="${widgetInstance.extendedAttributes}" var="d">
                    <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:widgetInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${widgetInstance?.metrics}">
                <dt id="metrics-label" class="word_wrap"><g:message code="widget.metrics.label" default="Metrics"/></dt>
                <g:each in="${widgetInstance.metrics}" var="m">
                    <dd class="word_wrap"><g:link controller="mediaMetric" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>
            </dl>
        </div>
    </div>
    <fieldset class="buttons">
        <g:form  url="[resource:widgetInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ widgetInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
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
                <g:link controller="featuredMedia" id="${widgetInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: widgetInstance]"/>
</div>
</body>
</html>
