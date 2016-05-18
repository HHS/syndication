%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/17/16
  Time: 12:07 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.Html" %>
<html>
<head>
    <meta name="layout" content="dashboard">
    <title>Admin Tools</title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Duplicate Finder</h1>
        </div><!-- /.col-lg-12 -->
    </div><!-- /.row -->

<synd:message/>
<synd:errors/>
<synd:error/>

    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-lg-6">
                    %{--<g:form action="duplicateFinder">--}%
                        %{--<fieldset class="form-group">--}%
                            %{--<label>Check how many?</label>--}%
                            %{--<g:select name="max" from="[100,200,500,1000,5000,10000,15000]" class="form-control" value="${params.max?:100}"/>--}%
                        %{--</fieldset>--}%
                        %{--<fieldset class="form-group">--}%
                            %{--<g:radioGroup name="order" values="['ASC', 'DESC']" labels="['Search Oldest First', 'Search Newest First']" value="${params.order ?: 'DESC'}" >--}%
                                %{--<p>${it.radio} ${it.label}</p>--}%
                            %{--</g:radioGroup>--}%
                        %{--</fieldset>--}%
                        %{--<fieldset class="form-group">--}%
                            %{--<g:submitButton name="search" value="Search" class="btn btn-primary form-control" />--}%
                        %{--</fieldset>--}%
                    %{--</g:form>--}%
                </div>
            </div>
            total duplicates: ${duplicates.size()}
            <g:if test="${duplicates}">
                <div class="row">
                    <div class="col-lg-12">
                        <ul>
                            <g:each in="${duplicates}" var="dupeSet">
                                <li><g:link controller="mediaItem" action="show" id="${dupeSet.oldest.id}">${dupeSet.oldest.id}</g:link>: ${dupeSet.oldest.sourceUrl}</li>
                                <ul>
                                    <g:each in="${dupeSet.dupes}" var="dupe">
                                        <li><g:link controller="mediaItem" action="show" id="${dupe.id}">${dupe.id}</g:link>: ${dupe.sourceUrl}</li>
                                    </g:each>
                                </ul>
                            </g:each>
                        </ul>
                    </div>
                </div>
            </g:if>
            <g:else>
                <div class="info">No duplicates found in this range</div>
            </g:else>
            %{--<div class="pagination">--}%
                %{--<g:paginate total="${total}" max="${params.max ?: 100}"/>--}%
            %{--</div>--}%
        </div>
    </div>
</div>
</body>
</html>
