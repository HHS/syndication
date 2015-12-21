
%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.metric.MediaMetric" %>



<div class="fieldcontain ${hasErrors(bean: mediaMetricInstance, field: 'day', 'error')} required">
	<label for="day">
		<g:message code="mediaMetric.day.label" default="Day" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="day" precision="minute" relativeYears="[-20..1]"  value="${mediaMetricInstance?.day}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: mediaMetricInstance, field: 'storefrontViewCount', 'error')} required">
	<label for="storefrontViewCount">
		<g:message code="mediaMetric.storefrontViewCount.label" default="Storefront View Count" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="storefrontViewCount" type="number" min="0" max="9223372036854775806" value="${mediaMetricInstance.storefrontViewCount}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: mediaMetricInstance, field: 'apiViewCount', 'error')} required">
	<label for="apiViewCount">
		<g:message code="mediaMetric.apiViewCount.label" default="Api View Count" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="apiViewCount" type="number" min="0" max="9223372036854775806" value="${mediaMetricInstance.apiViewCount}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: mediaMetricInstance, field: 'media', 'error')} required">
	<label for="media">
		<g:message code="mediaMetric.media.label" default="Media ID" />
		<span class="required-indicator">*</span>
	</label>
    <g:field name="media.id" type="number" min="1" max="9223372036854775806" value="${mediaMetricInstance?.media?.id}" required="true"/>
</div>

