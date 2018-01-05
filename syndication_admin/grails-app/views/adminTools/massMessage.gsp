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
  Date: 3/23/16
  Time: 4:49 PM
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
            <h1 class="page-header">Mass Message</h1>
            <h2 class="page-header">Send messages to all users at once</h2>
        </div><!-- /.col-lg-12 -->
    </div><!-- /.row -->

<synd:message/>
<synd:errors/>
<synd:hasError/>
    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-lg-6">
                    <g:form action="sendMessages">
                        <div class="form-group">
                            <label for="whichUsers">Who to message:</label>
                            <g:select class="form-control" name="whichUsers" from="['All Users', 'All Storefront Users', 'All Publishers', 'All Managers', 'All Admins']" keys="['all', 'store', 'pub', 'man', 'admin']" value="${whichUsers ?: 'all'}"/>
                        </div>
                        <div class="form-group">
                            <label for="subject">Email Subject:</label>
                            <g:textField class="form-control" name="subject" value="${subject ?: 'Important message from the Digital Media Content Syndication System at HHS'}"/>
                        </div>
                        <div class="form-group">
                            <label for="body">Message Body:</label>
                            <g:textArea class="form-control" name="body" value="${body ?: ''}"/>
                        </div>
                        <div class="checkbox">
                            <label>
                                <g:if test="${htmlMessage}">
                                    <g:checkBox name="htmlMessage" checked="true"/> This is an HTML email
                                </g:if>
                                <g:else>
                                    <g:checkBox name="htmlMessage"/> This is an HTML email
                                </g:else>
                            </label>
                        </div>

                        <g:submitButton class="form-control btn btn-warning" name="send" onclick="return confirm('Are you sure? This could send thousands of emails...')"/>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
