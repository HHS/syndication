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
<%@ page import="com.ctacorp.syndication.media.Html" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'periodical.label', default: 'Periodical')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-periodical" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-md-10 col-md-offset-2">
                <dl class="dl-horizontal">
                <g:if test="${periodicalInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="periodical.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${periodicalInstance?.id}</dd>
                </g:if>

                <g:if test="${periodicalInstance?.name}">
                    <dt id="name-label" class="word_wrap"><g:message code="periodical.name.label" default="Name"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="name"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.source}">
                    <dt id="source-label" class="word_wrap"><g:message code="periodical.source.label" default="Source"/></dt>
                    <dd class="word_wrap"><g:link controller="source" action="show" id="${periodicalInstance?.source?.id}">${periodicalInstance?.source?.encodeAsHTML()}</g:link></dd>
                </g:if>

                <g:if test="${periodicalInstance?.sourceUrl}">
                    <dt id="sourceUrl-label" class="word_wrap"><g:message code="periodical.sourceUrl.label" default="Source Url"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${periodicalInstance?.sourceUrl}"><g:fieldValue bean="${periodicalInstance}" field="sourceUrl"/></a></dd>
                </g:if>

                <g:if test="${periodicalInstance?.targetUrl}">
                    <dt id="targetUrl-label" class="word_wrap"><g:message code="periodical.targetUrl.label" default="Target Url"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${periodicalInstance?.targetUrl}"><g:fieldValue bean="${periodicalInstance}" field="targetUrl"/></a></dd>
                </g:if>

                <g:if test="${periodicalInstance?.active && periodicalInstance?.visibleInStorefront}">
                    <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${periodicalInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${periodicalInstance?.id}</a></dd>
                </g:if>

                <g:if test="${periodicalInstance?.customThumbnailUrl}">
                    <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="periodical.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="customThumbnailUrl"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.customPreviewUrl}">
                    <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="periodical.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="customPreviewUrl"/></dd>
                </g:if>

                <dt id="period-label" class="word_wrap"><g:message code="periodical.period.label" default="Period"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="period"/></dd>

                <g:if test="${periodicalInstance?.language}">
                    <dt id="language-label" class="word_wrap"><g:message code="periodical.language.label" default="Language"/></dt>
                    <dd class="word_wrap">${periodicalInstance?.language?.encodeAsHTML()}</dd>
                </g:if>

                <g:if test="${periodicalInstance?.description}">
                    <dt id="description-label" class="word_wrap"><g:message code="periodical.description.label" default="Description"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="description"/></dd>
                </g:if>
                <br>
                <g:if test="${periodicalInstance?.dateSyndicationUpdated}">
                    <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="periodical.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                    <dd class="property-value" class="word_wrap"><g:formatDate date="${periodicalInstance?.dateSyndicationUpdated}"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.dateSyndicationCaptured}">
                    <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="periodical.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${periodicalInstance?.dateSyndicationCaptured}"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.dateContentUpdated}">
                    <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="periodical.dateContentUpdated.label" default="Content Updated"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${periodicalInstance?.dateContentUpdated}"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.dateContentAuthored}">
                    <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="periodical.dateContentAuthored.label" default="Content Authored"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${periodicalInstance?.dateContentAuthored}"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.dateContentPublished}">
                    <dt id="dateContentPublished-label" class="property-label"><g:message code="periodical.dateContentPublished.label" default="Content Published"/></dt>
                    <dd class="property-value" aria-labelledby="dateContentPublished-label"><g:formatDate date="${periodicalInstance?.dateContentPublished}"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.dateContentReviewed}">
                    <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="periodical.dateContentReviewed.label" default="Content Reviewed"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${periodicalInstance?.dateContentReviewed}"/></dd>
                </g:if>

                <dt id="active-label" class="word_wrap"><g:message code="periodical.active.label" default="Active"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${periodicalInstance?.active}"/></dd>

                <dt id="active-label" class="word_wrap"><g:message code="periodical.visibleInStorefront.label" default="Visible In Storefront"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${periodicalInstance?.visibleInStorefront}"/></dd>

                <g:if test="${periodicalInstance?.externalGuid}">
                    <dt id="externalGuid-label" class="word_wrap"><g:message code="periodical.externalGuid.label" default="External Guide"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="externalGuid"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.hash}">
                    <dt id="hash-label" class="word_wrap"><g:message code="periodical.hash.label" default="Hash"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${periodicalInstance}" field="hash"/></dd>
                </g:if>

                <g:if test="${periodicalInstance?.alternateImages}">
                    <dt id="alternateImages-label" class="word_wrap"><g:message code="periodical.alternateImages.label" default="Alternate Images"/></dt>
                    <g:each in="${periodicalInstance?.alternateImages?.sort { it.name }}" var="altImage">
                        <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:periodicalInstance.id]" action="show" id="${altImage.id}">${altImage?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collections}">
                    <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                    <g:each in="${collections}" var="collection">
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${periodicalInstance?.campaigns}">
                    <dt id="campaigns-label" class="word_wrap"><g:message code="periodical.campaigns.label" default="Campaigns"/></dt>
                    <g:each in="${periodicalInstance?.campaigns?.sort { it.name }}" var="c">
                        <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                 <g:if test="${periodicalInstance?.extendedAttributes}">
                    <dt id="extendedAttributes-label" class="word_wrap"><g:message code="periodical.extendedAttributes.label" default="Extended Attributes"/></dt>
                    <dd>Attribute : Value</dd>
                    <g:each in="${periodicalInstance.extendedAttributes.sort { it.name }}" var="d">
                        <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:periodicalInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()} : ${d.value}</g:link></dd>
                    </g:each>
                </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form url="[resource:periodicalInstance, action:'delete']">
            <a href="${apiBaseUrl + '/resources/media/'+ periodicalInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" action="delete" onclick="return confirm('Are you sure?');" value="Delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${periodicalInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: periodicalInstance]"/>
</div>
</body>
</html>
