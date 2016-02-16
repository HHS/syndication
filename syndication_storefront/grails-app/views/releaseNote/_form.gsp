<%@ page import="com.ctacorp.syndication.storefront.ReleaseNote" %>

<div class="fieldcontain ${hasErrors(bean: releaseNoteInstance, field: 'releaseDate', 'error')} required">
	<label for="releaseDate">
		<g:message code="releaseNote.releaseDate.label" default="Release Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="releaseDate" precision="day"  value="${releaseNoteInstance?.releaseDate}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: releaseNoteInstance, field: 'releaseNoteText', 'error')} required">
	<label for="releaseNoteText">
		<g:message code="releaseNote.releaseNoteText.label" default="Release Note Text" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="releaseNoteText" id="releaseNoteText" cols="120" rows="50" maxlength="2147483647" required="" value="${releaseNoteInstance?.releaseNoteText}"/>
</div>

