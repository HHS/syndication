<%@ page import="com.ctacorp.tinyurl.MediaMapping" %>



<div class="fieldcontain ${hasErrors(bean: mediaMappingInstance, field: 'targetUrl', 'error')} ">
	<label for="targetUrl">
		<g:message code="mediaMapping.targetUrl.label" default="Target Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="targetUrl" cols="40" rows="5" maxlength="2000" value="${mediaMappingInstance?.targetUrl}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: mediaMappingInstance, field: 'syndicationId', 'error')} ">
	<label for="syndicationId">
		<g:message code="mediaMapping.syndicationId.label" default="Syndication Id" />
		
	</label>
	<g:field name="syndicationId" type="number" min="0" value="${mediaMappingInstance.syndicationId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: mediaMappingInstance, field: 'guid', 'error')} ">
	<label for="guid">
		<g:message code="mediaMapping.guid.label" default="Guid" />
		
	</label>
	<g:textField name="guid" value="${mediaMappingInstance?.guid}"/>

</div>

