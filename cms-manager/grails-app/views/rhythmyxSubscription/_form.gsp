<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<%@ page import="com.ctacorp.syndication.manager.cms.RhythmyxSubscriber; com.ctacorp.syndication.manager.cms.RhythmyxSubscription" %>

<g:render template="../mediaItems/datatables"/>
<div class="form-group ${hasErrors(bean: instance, field: 'targetFolder', 'has-error')} ">
    <label class="control-label col-sm-3" for="targetFolder">
        <g:message code="rhythmyxSubscription.targetFolder.label" />
        <i class="fa fa-asterisk"></i>
    </label>
    <div class="col-sm-6">
        <g:textField class="form-control" name="targetFolder" value="${instance?.targetFolder}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'contentType', 'has-error')} ">
    <label class="control-label col-sm-3" for="contentType">
        <g:message code="rhythmyxSubscription.contentType.label" />
        <i class="fa fa-asterisk"></i>
    </label>
    <g:set var="contentTypes" value="${flash.contentTypes}" />

    <div id="" class="controls col-sm-6">
        <g:if test="${contentTypes && !contentTypes.isEmpty()}">
            <g:select class="form-control" onchange="${remoteFunction(action:'getDefaultWorkflow', params:[id:instance.id], update: 'defaultWorkflow')}" id="contentType" name="contentType" from="${contentTypes}" />
        </g:if>
        <g:else>
            <g:textField class="form-control" name="contentType" value="${instance?.contentType}"/>
        </g:else>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'rhythmyxSubscriber', 'error')}">
    <label class="control-label col-sm-3" for="rhythmyxSubscriber">
        <g:message code="rhythmyxSubscription.rhythmyxSubscriber.label" />
        <i class="fa fa-asterisk"></i>
    </label>
    <div class="controls col-sm-6">
        <g:select class="form-control" id="rhythmyxSubscriber" name="rhythmyxSubscriber" from="${flash.rhythmyxSubscribers}" optionKey="id" required="required" optionValue="instanceName" value="${instance?.rhythmyxSubscriber?.id}" />
    </div>
</div>

<div id="defaultWorkflow">
    <g:render template="defaultWorkflow" />
</div>


<div class="form-group">
    <label class="control-label col-sm-3" for="useAsDefaultWorkflow">
        <g:message code="rhythmyxWorkflow.useAsDefaultWorkflow.label" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="useAsDefaultWorkflow" id="useAsDefaultWorkflow" value="${instance?.useAsDefaultWorkflow}" />
    </div>
</div>
