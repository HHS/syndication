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
<%@ page import="com.ctacorp.syndication.media.Tweet" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'tweet.label', default: 'Tweet')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
</head>

<body>
<div id="show-tweet" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
                <g:render template="/mediaItem/commonShowView" model="[mediaItemInstance: tweet, mediaType:'tweet']"/>

                <g:if test="${tweet?.tweetId}">
                    <dt id="name-label" class="word_wrap"><g:message code="tweet.tweetId.label" default="Tweet Id"/></dt>
                    <dd class="word_wrap">${tweet.tweetId}</dd>
                </g:if>

                <g:if test="${tweet?.account}">
                    <dt id="name-label" class="word_wrap"><g:message code="tweet.account.label" default="Account"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${tweet}" field="account"/></dd>
                </g:if>

                <g:if test="${tweet?.messageText}">
                    <dt id="name-label" class="word_wrap"><g:message code="tweet.messageText.label" default="Message Text"/></dt>
                    <dd class="word_wrap"><synd:linkify><g:fieldValue bean="${tweet}" field="messageText"/></synd:linkify></dd>
                </g:if>

                <g:if test="${tweet?.mediaUrl}">
                    <dt id="name-label" class="word_wrap"><g:message code="tweet.mediaUrl.label" default="Media Url"/></dt>
                    <dd class="word_wrap"><a href="${tweet?.mediaUrl}"><g:fieldValue bean="${tweet}" field="mediaUrl"/></a></dd>
                </g:if>

                <g:if test="${tweet?.videoVariantUrl}">
                    <dt id="name-label" class="word_wrap"><g:message code="tweet.videoVariantUrl.label" default="Video Url"/></dt>
                    <dd class="word_wrap"><a href="${tweet?.videoVariantUrl}"><g:fieldValue bean="${tweet}" field="videoVariantUrl"/></a></dd>
                </g:if>

                <g:if test="${tweet?.tweetDate}">
                    <dt id="name-label" class="word_wrap"><g:message code="tweet.tweetDate.label" default="Tweet Date"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${tweet}" field="tweetDate"/></dd>
                </g:if>

            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:tweet, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ tweet?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="btn btn-default" action="index">
                Cancel
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" class="btn btn-success pull-right" id="${tweet?.id}" action="featureItem">
                    Feature this Item
                </g:link>
                <g:link controller="tweet" class="btn btn-primary pull-right" style="margin-right: 5px;" id="${tweet?.id}" action="refreshTwitterMeta">
                    Refresh
                </g:link>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                <g:link controller="mediaPreviewThumbnail" style="margin-right: 3px;" class="btn btn-warning pull-right" id="${tweet?.id}" action="flush">
                    Regenerate Thumbnail & Preview
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: tweet]"/>
</div>
</body>
</html>
