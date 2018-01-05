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
<%@ page import="com.ctacorp.syndication.media.QuestionAndAnswer" %>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'questionAndAnswer.label', default: 'Question and Answer')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="edit-questionAndAnswer" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<synd:message/>
			<synd:errors/>
			<synd:hasError/>
			<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
				<div class="row">
					<div class="col-md-8">
						<g:form class="form-horizontal" url="[resource:questionAndAnswer, action:'update']" method="PUT" id="updateMediaItem">
							<g:hiddenField name="version" value="${questionAndAnswer?.version}" />
							<fieldset class="form">
								<g:render template="form"/>
								<!-- Text input-->
								<div class="form-group">
									<label class="col-md-4 control-label" for="campaigns">Campaigns</label>
									<div class="col-md-4">
										<ul class="one-to-many">
											<g:each in="${questionAndAnswer?.campaigns?}" var="campaignInstance">
												<li><g:link controller="campaign" action="show" id="${campaign.id}">${campaign?.encodeAsHTML()}</g:link></li>
											</g:each>
										</ul>
									</div>
								</div>
							</fieldset>
							<fieldset class="buttons" id="mediaItemSubmitButton">
								<g:actionSubmit class="btn btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
								<g:link class="button" id="${questionAndAnswer.id}" resource="${questionAndAnswer}" action="show">
									<button type="button" class="btn">Cancel</button>
								</g:link>
							</fieldset>
						</g:form>
					</div>
				</div>
			</sec:ifAnyGranted>
            <g:render template="/mediaItem/addAttributeOrImage" model="[mediaItemInstance: questionAndAnswer]"/>
		</div>
	</body>
</html>
