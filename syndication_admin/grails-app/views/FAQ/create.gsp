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
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'faq.label', default: 'FAQ')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
    <asset:javascript src="/jquery.multi-select.js"/>
    <asset:stylesheet src="/multi-select.css"/>
    <style>
        .multi-select-header{
            text-align: center;
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#questionAndAnswers').multiSelect({
                selectableHeader: "<div class='multi-select-header'>Available Questions</div>",
                selectionHeader: "<div class='multi-select-header'>This FAQ</div>"
            });
        });
    </script>
</head>

<body>
<div id="create-faq" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-md-10">
            <g:form class="form-horizontal" url="[resource: faq, action: 'save']" id="updateMediaItem">
                <fieldset class="form">
                    <g:render template="form"/>
                </fieldset>
                <fieldset class="buttons" id="mediaItemSubmitButton">
                    <g:submitButton name="create" class="btn btn-default btn-success"
                                    value="${message(code: 'default.button.create.label', default: 'Create')}"/>
                    <g:link class="btn btn-default" action="index">
                        Cancel
                    </g:link>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
<br/>
<br/>
</body>
</html>
