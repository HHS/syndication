%{--
Copyright (c) 2014,Â Health and Human Services - Web Communications (ASPA)
â€¨All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard"/>
    <g:set var="entityName" value="${message(code: 'tagType.label', default: 'Tag Type')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Create Tag Type</h1>
        </div>
    </div>

    <div class="col-lg-8">
        <synd:message/>
        <synd:errors/>
    </div>

    <div class="row">
        <div class="col-lg-8">
            <g:form class="form-horizontal">
                <g:render template="form" model="[tagTypeInstance:tagTypeInstance]"/>
                <!-- Button -->
                <div class="form-group">
                    <div class="col-md-4">
                        <g:actionSubmit action="save" value="Save" class="btn btn-success"/>
                        <g:if test="${params.action == 'edit'}">
                            <g:actionSubmit value="Delete" action="delete" class="btn btn-danger" onclick="return confirm('Are you Sure?');"/>
                        </g:if>
                        <g:link class="button" action="index">
                            <button type="button" class="btn">Cancel</button>
                        </g:link>
                    </div>
                </div>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>