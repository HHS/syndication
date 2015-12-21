%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.media.Tweet" %>

<g:render template="/mediaItem/globalForm" model="[mediaItemInstance:tweetInstance]"/>


%{--tweet specific attributes--}%
<div class="form-group ${hasErrors(bean:tweetInstance, field:'tweetId', 'errors')}">
    <label class="col-md-4 control-label" for="tweetId">Tweet Id</label>
    <div class="col-md-8">
        <input id="tweetId" disabled name="tweetId" value="${tweetInstance?.tweetId}" type="text" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean:tweetInstance, field:'account', 'errors')}">
    <label class="col-md-4 control-label" for="account">Account</label>
    <div class="col-md-8">
        <input id="account" disabled name="account" value="${tweetInstance?.account}" type="text" placeholder="Media Item Name" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean:tweetInstance, field:'messageText', 'errors')}">
    <label class="col-md-4 control-label" for="messageText">Message Text</label>
    <div class="col-md-8">
        <input id="messageText" disabled name="messageText" value="${tweetInstance?.messageText}" type="text" placeholder="Media Item Name" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean:tweetInstance, field:'mediaUrl', 'errors')}">
    <label class="col-md-4 control-label" for="mediaUrl">Media Url</label>
    <div class="col-md-8">
        <input id="mediaUrl" name="mediaUrl" value="${tweetInstance?.mediaUrl}" type="text" placeholder="Media Item Name" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean:tweetInstance, field:'tweetDate', 'errors')}">
    <label class="col-md-4 control-label" for="tweetDate">Tweet Date</label>
    <div class="col-md-8">
        <input id="tweetDate" disabled name="tweetDate" value="${tweetInstance?.tweetDate}" type="text" placeholder="Media Item Name" class="form-control input-md">
    </div>
</div>