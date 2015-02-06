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
            <g:if test="${alternateLabel}">
                <h3><g:message code="${alternateLabel}" /></h3>
            </g:if>
            <g:else>
                <h3><g:message code="default.list.label" args="[entityName]"/></h3>
            </g:else>
        </div>
        <div class="panel-body">
            <g:render template="../templates/flash_message"/>
            <div class="table-responsive">
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <g:render template="list_headers"/>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${instanceList}" status="i" var="instance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <g:render template="list_data" model="[instance: instance]"/>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <g:render template="../templates/pagination" model="[
                    instanceCount: instanceCount,
                    instanceList: instanceList
            ]"/>
        </div>
        <div class="panel-footer">
            <g:if test="${allowCreate}">
                <a class="btn btn-success" href="${createLink(action: 'create')}" role="button">Create</a>
            </g:if>
        </div>
    </div>
</div>
