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
<%@ page import="com.ctacorp.syndication.Source" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'source.label', default: 'Source')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-source" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10">
            <div class="panel panel-default">
                <div class="panel-body">
                    <g:form action="index" class="form-horizontal">
                        <div class="row">
                            <div class="col-sm-8">
                                <div class="form-group">
                                    <label class="control-label col-sm-6" for="search">Search By Name</label>
                                    <div class="col-sm-6">
                                        <input id="name" name="name" type="search" placeholder="Name" class="form-control input-md" value="${params?.name}">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-8">
                                <div class="form-group">
                                    <label class="control-label col-sm-6" for="max">Sources Per Page</label>
                                    <div class="col-sm-6">
                                        <g:select name="max" from="[5, 10, 15, 20, 50]" class="form-control" value="${params.max}"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="">
                                    <g:submitButton id="search" name="search" class="btn btn-primary" value="Search"/>
                                </div>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel panel-info">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'source.id.label', default: 'ID')}" params="[name:params.name,max:params.max]"/>

                            <g:sortableColumn property="name" title="${message(code: 'source.name.label', default: 'Name')}" params="[name:params.name,max:params.max]"/>

                            <g:sortableColumn property="acronym" title="${message(code: 'source.acronym.label', default: 'Acronym')}" params="[name:params.name,max:params.max]"/>

                            <g:sortableColumn property="websiteUrl" title="${message(code: 'source.websiteUrl.label', default: 'Website Url')}" params="[name:params.name,max:params.max]"/>

                            <g:sortableColumn property="contactEmail" title="${message(code: 'source.contactEmail.label', default: 'Contact Email')}" params="[name:params.name,max:params.max]"/>

                            <g:sortableColumn property="description" title="${message(code: 'source.description.label', default: 'Description')}" params="[name:params.name,max:params.max]"/>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${sourceInstanceList}" status="i" var="sourceInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${sourceInstance?.id}</td>

                                <td><g:link action="show" id="${sourceInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: sourceInstance, field: "name")}</span></g:link></td>

                                <td><span class="limited-width-md ellipse">${fieldValue(bean: sourceInstance, field: "acronym")}</span></td>

                                <td><a target="_blank" href="${sourceInstance?.websiteUrl}"><span class="limited-width-md break-url ellipse">${fieldValue(bean: sourceInstance, field: "websiteUrl")}</span></td>

                                <td><span class="limited-width-md ellipse">${fieldValue(bean: sourceInstance, field: "contactEmail")}</span></td>

                                <td><span class="limited-width-md ellipse">${fieldValue(bean: sourceInstance, field: "description")}</span></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="pagination">
                <g:paginate total="${sourceInstanceCount ?: 0}" params="[max:params.max, name:params.name]"/>
            </div>
            
        </div>
    </div>
</div>
</body>
</html>
