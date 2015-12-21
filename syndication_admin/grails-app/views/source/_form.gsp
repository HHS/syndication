
%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.Source" %>


<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-4">
        <input name="name" type="text" placeholder="source name" class="form-control input-md" maxlength="255" required="" value="${sourceInstance?.name}">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="acronym">Acronym<span class="required-indicator">*</span></label>
    <div class="col-md-4">
        <input name="acronym" maxlength="255" required="" value="${sourceInstance?.acronym}" type="text" placeholder="abbreviation" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="websiteUrl">Website Url<span class="required-indicator">*</span></label>
    <div class="col-md-4">
        <input name="websiteUrl" maxlength="2000" required="" value="${sourceInstance?.websiteUrl}" type="url" placeholder="url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="contactEmail">Contact Email</label>
    <div class="col-md-4">
        <input name="contactEmail" maxlength="255"  value="${sourceInstance?.contactEmail}" type="email" placeholder="email" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="largeLogoUrl">Large Logo Url</label>
    <div class="col-md-4">
        <input name="largeLogoUrl" maxlength="2000" value="${sourceInstance?.largeLogoUrl}" type="url" placeholder="large logo" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="smallLogoUrl">Small Logo Url</label>
    <div class="col-md-4">
        <input name="smallLogoUrl" maxlength="2000" value="${sourceInstance?.smallLogoUrl}" type="url" placeholder="small logo" class="form-control input-md">
    </div>
</div>

<!-- Textarea -->
<div class="form-group">
    <label class="col-md-4 control-label" for="description">Description</label>
    <div class="col-md-4">
        <textarea class="form-control" name="description" maxlength="5000">${sourceInstance?.description}</textarea>
    </div>
</div>