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
    <meta name="layout" content="dashboard">
    <g:set var="entityName" value="${message(code: 'tagType.label', default: 'Tag Type')}" />
  <title></title>
</head>
<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Edit Tag Type</h1>
        </div>
    </div>

        <synd:message/>
        <synd:errors/>
        <g:form class="form-horizontal" action="update">
            <div class="row">
                <div class="col-md-8">
                 <g:render template="form" model="[tagTypeInstance:tagTypeInstance]"/>
                </div>
            </div>
            <!-- Button -->
            <div class="row">
                <div class="col-xs-12">
                     <div class="form-group">
                        <g:submitButton id="update" name="Update" class="btn btn-success" onclick="return confirm('Are you Sure?');">Update</g:submitButton>
                        <g:if test="${params.action == 'edit'}">
                            <g:actionSubmit value="Delete" action="delete" class="btn btn-danger" onclick="return confirm('Are you Sure?');"/>
                        </g:if>
                        <g:link class="button" action="index">
                            <button type="button" class="btn">Cancel</button>
                        </g:link>
                    </div>
                </div>
            </div>
        </g:form>


</div>

</body>
</html>