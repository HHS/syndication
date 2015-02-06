
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.MediaItem; com.ctacorp.syndication.Campaign" %>


<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-4">
        <input id="name" name="name" required="" value="${campaignInstance?.name}" type="text" placeholder="campaign name" class="form-control input-md">
    </div>
</div>

<!-- Textarea -->
<div class="form-group">
    <label class="col-md-4 control-label" for="description">description</label>
    <div class="col-md-4">
        <textarea class="form-control" id="description" name="description" maxlength="2000" >${campaignInstance?.description}</textarea>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="contactEmail">Contact Email</label>
    <div class="col-md-4">
        <input id="contactEmail" name="contactEmail"  value="${campaignInstance?.contactEmail}" type="email" placeholder="email" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean: campaignInstance, field: 'startDate', 'error')} required">
    <label class="col-md-4 control-label" for="startDate">
        <g:message code="campaign.startDate.label" default="Start Date" />
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-4">
        <g:datePicker name="startDate" precision="minute" relativeYears="[-20..1]"  value="${campaignInstance?.startDate}"  />
    </div>

</div>

<div class="form-group ${hasErrors(bean: campaignInstance, field: 'endDate', 'error')} ">
    <label class="col-md-4 control-label" for="endDate">
        <g:message code="campaign.endDate.label" default="End Date" />
    </label>
    <div class="col-md-4">
        <g:datePicker name="endDate" precision="minute" relativeYears="[-20..1]"  value="${campaignInstance?.endDate}" default="none" noSelection="['': '']" />
    </div>
</div>

<!-- Select Basic -->
<div class="form-group required">
    <label class="col-md-4 control-label" for="selectbasic">
        Source
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-4">
        <select id="selectbasic" name="source.id" class="form-control">
            <g:each in="${com.ctacorp.syndication.Source.list()}" var="sourceInstance">
                <g:if test="${sourceInstance.id == campaignInstance?.source?.id}">
                    <option value="${sourceInstance.id}" selected>${sourceInstance}</option>
                </g:if>
                <g:else>
                    <option value="${sourceInstance.id}">${sourceInstance}</option>
                </g:else>
            </g:each>
        </select>
    </div>
</div>

<div class="form-group ${hasErrors(bean: campaignInstance, field: 'campaignMetrics', 'error')} ">
    <label class="col-md-4 control-label" for="campaignMetrics">
        <g:message code="campaign.campaignMetrics.label" default="Campaign Metrics" />
    </label>
    <div class="col-md-4">
        <ul class="one-to-many col-md-6">
            <g:each in="${campaignInstance?.campaignMetrics?}" var="metricInstance">
                <li><g:link controller="campaignMetric" action="show" id="${metricInstance.id}">${metricInstance?.encodeAsHTML()}</g:link></li>
            </g:each>
            <li class="add">
                <g:link controller="campaignMetric" action="create" params="['campaign.id': campaignInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'campaignMetric.label', default: 'CampaignMetric')])}</g:link>
            </li>
        </ul>
    </div>
</div>

<!-- Select Multiple -->
<div class="form-group">
    <label class="col-md-4 control-label" for="selectmultiple">Media Items</label>
    <div class="col-md-4">
        <g:textField name="mediaItems" id="mediaItems"/>
    </div>
</div>

<sec:ifAnyGranted roles="ROLE_ADMIN">
    <div class="form-group">
        <label class="col-md-4 control-label" for="subscriberId">Subscriber</label>
        <div class="col-md-4">
            <g:select from="${subscribers}" name="subscriberId" optionKey="id" optionValue="name" value="${currentSubscriber?.id}" noSelection="['':'-Choose an Owner-']" class="form-control"/>
        </div>
    </div>
</sec:ifAnyGranted>