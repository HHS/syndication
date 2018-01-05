<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<g:set var="entityName" value="${message(code: controllerName + '.label')}"/>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3><g:message code="default.create.label" args="[entityName]"/></h3>
        </div>
        <div class="panel-body">
            <g:render template="/templates/errors"/>
            <div class="container-fluid">
                <g:form class="form-horizontal" resource="${instance}" url="[resource:instance, action: 'save']" method="POST">
                    <fieldset>
                        <g:render template="/${controllerName}/form" />
                    </fieldset>
                </g:form>
            </div>
        </div>
        <div class="panel-footer">
            <a style="margin-right: .5em" class="btn btn-warning" href="${createLink(action: 'index')}" role="button">Cancel</a>
            <button class="btn btn-success" type="submit" onclick="return document.forms[1].submit();" >Create</button>
        </div>
    </div>
</div>
