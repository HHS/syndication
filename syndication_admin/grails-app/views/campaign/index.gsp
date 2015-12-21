
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
    <g:set var="entityName" value="${message(code: 'campaign.label', default: 'Campaign')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-campaign" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel panel-info">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'campaign.id.label', default: 'ID')}"/>

                            <g:sortableColumn property="name" title="${message(code: 'campaign.name.label', default: 'Name')}"/>

                            <g:sortableColumn property="description" title="${message(code: 'campaign.description.label', default: 'Description')}"/>

                            <g:sortableColumn property="contactEmail" title="${message(code: 'campaign.contactEmail.label', default: 'Contact Email')}"/>

                            <g:sortableColumn property="startDate" title="${message(code: 'campaign.startDate.label', default: 'Start Date')}"/>

                            <g:sortableColumn property="endDate" title="${message(code: 'campaign.endDate.label', default: 'End Date')}"/>

                            <th><g:message code="campaign.source.label" default="Source"/></th>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${campaignInstanceList}" status="i" var="campaignInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${campaignInstance?.id}</td>

                                <td><span class="limited-width-md ellipse"><g:link action="show" id="${campaignInstance.id}">${fieldValue(bean: campaignInstance, field: "name")}</g:link></span></td>

                                <td><span class="limited-width-lg ellipse">${fieldValue(bean: campaignInstance, field: "description")}</span></td>

                                <td>${fieldValue(bean: campaignInstance, field: "contactEmail")}</td>

                                <td><g:formatDate date="${campaignInstance.startDate}"/></td>

                                <td><g:formatDate date="${campaignInstance.endDate}"/></td>

                                <td><span class="ellipse limited-width-md">${fieldValue(bean: campaignInstance, field: "source")}</span></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>

            <g:if test="${campaignInstanceCount > params.max}">
                <div class="pagination">
                    <g:paginate total="${campaignInstanceCount ?: 0}"/>
                </div>
            </g:if>
            
        </div>
    </div>
</div>
</body>
</html>
