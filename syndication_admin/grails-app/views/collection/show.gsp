%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.Collection" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'collection.label', default: 'Collection')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-collection" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
                <g:if test="${collectionInstance?.id}">
                    <dt class="word_wrap"><g:message code="collection.id.label" default="Id"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="id"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.name}">
                    <dt class="word_wrap"><g:message code="collection.name.label" default="Name"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="name"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.source}">
                    <dt id="source-label" class="word_wrap"><g:message code="collection.source.label" default="Source"/></dt>
                    <dd class="word_wrap"><g:link controller="source" action="show" id="${collectionInstance?.source?.id}">${collectionInstance?.source?.encodeAsHTML()}</g:link></dd>
                </g:if>

                <g:if test="${collectionInstance?.sourceUrl}">
                    <dt class="word_wrap"><g:message code="collection.sourceUrl.label" default="Source Url"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${collectionInstance.sourceUrl}"><g:fieldValue bean="${collectionInstance}" field="sourceUrl"/></a></dd>
                </g:if>

                <g:if test="${collectionInstance?.targetUrl}">
                    <dt id="targetUrl-label" class="word_wrap"><g:message code="collection.targetUrl.label" default="Target Url"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="targetUrl"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.customThumbnailUrl}">
                    <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="collection.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="customThumbnailUrl"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.language}">
                    <dt id="language-label" class="word_wrap"><g:message code="collection.language.label" default="Language"/></dt>
                    <dd class="word_wrap">${collectionInstance?.language?.encodeAsHTML()}</dd>
                </g:if>

                <g:if test="${collectionInstance?.description}">
                    <dt class="word_wrap"><g:message code="collection.description.label" default="Description"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="description"/></dd>
                </g:if>
                <br>
                <g:if test="${collectionInstance?.dateSyndicationUpdated}">
                    <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="collection.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateSyndicationUpdated}"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.dateSyndicationCaptured}">
                    <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="collection.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateSyndicationCaptured}"/></dd>
                </g:if>

                %{--<g:if test="${collectionInstance?.dateSyndicationVisible}">--}%
                    %{--<dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="collection.dateSyndicationVisible.label" default="Syndication Visible"/></dt>--}%
                    %{--<dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateSyndicationVisible}"/></dd>--}%
                %{--</g:if>--}%

                <g:if test="${collectionInstance?.dateContentUpdated}">
                    <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="collection.dateContentUpdated.label" default="Content Updated"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateContentUpdated}"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.dateContentAuthored}">
                    <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="collection.dateContentAuthored.label" default="Content Authored"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateContentAuthored}"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.dateContentPublished}">
                    <dt id="dateContentPublished-label" class="word_wrap"><g:message code="collection.dateContentPublished.label" default="Content Published"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateContentPublished}"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.dateContentReviewed}">
                    <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="collection.dateContentReviewed.label" default="Content Reviewed"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${collectionInstance?.dateContentReviewed}"/></dd>
                </g:if>

                <dt id="active-label" class="word_wrap"><g:message code="collection.active.label" default="Active"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${collectionInstance?.active}"/></dd>

                <dt id="active-label" class="word_wrap"><g:message code="collection.visibleInStorefront.label" default="Visible In Storefront"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${collectionInstance?.visibleInStorefront}"/></dd>

                <g:if test="${collectionInstance?.externalGuid}">
                    <dt id="externalGuid-label" class="word_wrap"><g:message code="collection.externalGuid.label" default="External Guide"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="externalGuid"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.hash}">
                    <dt id="hash-label" class="word_wrap"><g:message code="collection.hash.label" default="Hash"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${collectionInstance}" field="hash"/></dd>
                </g:if>

                <g:if test="${collectionInstance?.campaigns}">
                    <dt id="campaigns-label" class="word_wrap"><g:message code="collection.campaigns.label" default="Campaigns"/></dt>
                    <g:each in="${collectionInstance.campaigns}" var="c">
                        <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collectionInstance?.alternateImages}">
                    <dt id="alternateImages-label" class="word_wrap"><g:message code="collection.alternateImages.label" default="Alternate Images"/></dt>
                    <g:each in="${collectionInstance.alternateImages}" var="a">
                        <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:collectionInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collectionInstance?.extendedAttributes}">
                    <dt id="extendedAttributes-label" class="word_wrap"><g:message code="collection.extendedAttributes.label" default="Extended Attributes"/></dt>
                    <g:each in="${collectionInstance.extendedAttributes}" var="d">
                        <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:collectionInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collectionInstance?.metrics}">
                    <dt id="metrics-label" class="word_wrap"><g:message code="collection.metrics.label" default="Metrics"/></dt>
                    <g:each in="${collectionInstance.metrics}" var="m">
                        <dd class="word_wrap"><g:link controller="mediaMetric" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collectionInstance?.mediaItems}">
                    <dt id="mediaItems-label" class="word_wrap"><g:message code="collection.mediaItems.label" default="Media Items"/></dt>
                    <g:each in="${collectionInstance.mediaItems}" var="m">
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:collectionInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ collectionInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Alternate/Extended Attributes" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" value="Delete" onclick="return confirm('Are you sure?');" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${collectionInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>

    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: collectionInstance]"/>
</div>
</body>
</html>
