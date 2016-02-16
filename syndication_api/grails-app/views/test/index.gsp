
<!--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 -->


<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 3/26/14
  Time: 1:08 PM
--%>

<%@ page import="com.ctacorp.syndication.media.MediaItem" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>

    <g:if test="${flash.message}">
        <div>
            <p style="color:blue;"><strong>${flash.message}</strong></p>
        </div>
    </g:if>
    <g:form action="poster">
        <g:select name="mediaId" from="${MediaItem.list()}" optionKey="id"/>
        <br/>
        <g:submitButton name="submit" value="Send"/>
    </g:form>

    <hr/>
    <g:form action="createStructuredItem">
        <h2>Create a structured item</h2>
        <label>Name: </label>
        <g:textField name="name" required="required"/>
        <br/>
        <label>Structured Content Type:</label>
        <g:select name="structuredType" from="${com.ctacorp.syndication.media.MediaItem.StructuredContentType.enumConstants}" optionValue="prettyName" keys="${com.ctacorp.syndication.media.MediaItem.StructuredContentType.enumConstants}"/>
        <br/>
        <label>SourceUrl: </label>
        <g:textField name="sourceUrl" required="required"/>
        <br/>
        <g:submitButton name="Go!"/>
    </g:form>
</body>
</html>