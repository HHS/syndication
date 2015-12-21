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
<%@ page import="com.ctacorp.syndication.ExtendedAttribute" %>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'extendedAttribute.label', default:'Extended Attribute')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-extendedAttribute" class="container-fluid" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <synd:message/>
            <synd:errors/>
            <synd:error/>
            <div class="row">
                <div class="col-sm-9 col-sm-offset-1">
                    <dl class="dl-horizontal">

                    <g:if test="${extendedAttributeInstance?.id}">
                        <dt id="id-label" class="word_wrap"><g:message code="extendedAttribute.id.label" default="Id" /></dt>
                        <dd class="word_wrap">${extendedAttributeInstance?.id}</dd>
                    </g:if>

                    <g:if test="${extendedAttributeInstance?.name}">
                        <dt id="name-label" class="word_wrap"><g:message code="extendedAttribute.name.label" default="Name" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${extendedAttributeInstance}" field="name"/></dd>
                    </g:if>

                    <g:if test="${extendedAttributeInstance?.value}">
                        <dt id="value-label" class="word_wrap"><g:message code="extendedAttribute.value.label" default="Value" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${extendedAttributeInstance}" field="value"/></dd>
                    </g:if>

                    <g:if test="${extendedAttributeInstance?.mediaItem}">
                        <dt id="mediaItem-label" class="word_wrap"><g:message code="extendedAttribute.mediaItem.label" default="Media Item" /></dt>
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${extendedAttributeInstance?.mediaItem?.id}">${extendedAttributeInstance?.mediaItem?.encodeAsHTML()}</g:link></dd>
                    </g:if>
			        </dl>
                </div>
            </div>
			<div class="col-sm-8">
                <g:form url="[resource:extendedAttributeInstance, action:'delete']" method="DELETE">
                    <g:hiddenField name="mediaId" value="${params.mediaId}"/>
                    <fieldset class="buttons">
                        <g:link class="btn btn-success" action="edit" params="[mediaId:params.mediaId]" resource="${extendedAttributeInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER">
                            <g:link class="button" action="index">
                                <button type="button" class="btn">Cancel</button>
                            </g:link>
                        </sec:ifAnyGranted>
                    </fieldset>
                </g:form>
            </div>
		</div>
	</body>
</html>
