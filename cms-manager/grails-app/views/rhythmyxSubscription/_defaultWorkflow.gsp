<div class="page-header">
    <h4>Rhythmyx Workflow</h4>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="rhythmyxWorkflow.autoPublish">
        <g:message code="rhythmyxWorkflow.autoPublish.label" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="rhythmyxWorkflow.autoPublish" id="rhythmyxWorkflow.autoPublish" value="${instance?.rhythmyxWorkflow?.autoPublish}" onchange="useAutoPublish(this.checked);"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'rhythmyxWorkflow.importTransitions', 'has-error')} ">
    <label class="control-label col-sm-3" for="rhythmyxWorkflow.importTransitions">
        <g:message code="rhythmyxWorkflow.importTransitions.label" />
    </label>
    <div class="col-sm-6">
        <g:textField class="form-control" name="rhythmyxWorkflow.importTransitions" value="${instance?.rhythmyxWorkflow?.importTransitions}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'rhythmyxWorkflow.updateTransitions', 'has-error')} ">
    <label class="control-label col-sm-3" for="rhythmyxWorkflow.updateTransitions">
        <g:message code="rhythmyxWorkflow.updateTransitions.label" />
    </label>
    <div class="col-sm-6">
        <g:if test="${instance?.rhythmyxWorkflow?.autoPublish}">
            <g:textField class="form-control" name="rhythmyxWorkflow.updateTransitions" value="${instance?.rhythmyxWorkflow?.updateTransitions}" disabled="disabled"/>
        </g:if>
        <g:else>
            <g:textField class="form-control" name="rhythmyxWorkflow.updateTransitions" value="${instance?.rhythmyxWorkflow?.updateTransitions}"/>
        </g:else>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'rhythmyxWorkflow.updateAutoPublishTransitions', 'has-error')} ">
    <label class="control-label col-sm-3" for="rhythmyxWorkflow.updateAutoPublishTransitions">
        <g:message code="rhythmyxWorkflow.updateAutoPublishTransitions.label" />
    </label>
    <div class="col-sm-6">
        <g:if test="${instance?.rhythmyxWorkflow?.autoPublish}">
            <g:textField class="form-control" name="rhythmyxWorkflow.updateAutoPublishTransitions" value="${instance?.rhythmyxWorkflow?.updateAutoPublishTransitions}"/>
        </g:if>
        <g:else>
            <g:textField class="form-control" name="rhythmyxWorkflow.updateAutoPublishTransitions" value="${instance?.rhythmyxWorkflow?.updateAutoPublishTransitions}" disabled="disabled"/>
        </g:else>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'rhythmyxWorkflow.deleteTransitions', 'has-error')} ">
    <label class="control-label col-sm-3" for="rhythmyxWorkflow.deleteTransitions">
        <g:message code="rhythmyxWorkflow.deleteTransitions.label" />
    </label>
    <div class="col-sm-6">
        <g:if test="${instance?.rhythmyxWorkflow?.autoPublish}">
            <g:textField class="form-control" name="rhythmyxWorkflow.deleteTransitions" value="${instance?.rhythmyxWorkflow?.deleteTransitions}" disabled="disabled"/>
        </g:if>
        <g:else>
            <g:textField class="form-control" name="rhythmyxWorkflow.deleteTransitions" value="${instance?.rhythmyxWorkflow?.deleteTransitions}"/>
        </g:else>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'rhythmyxWorkflow.deleteAutoPublishTransitions', 'has-error')} ">
    <label class="control-label col-sm-3" for="rhythmyxWorkflow.deleteAutoPublishTransitions">
        <g:message code="rhythmyxWorkflow.deleteAutoPublishTransitions.label" />
    </label>
    <div class="col-sm-6">
        <g:if test="${instance?.rhythmyxWorkflow?.autoPublish}">
            <g:textField class="form-control" name="rhythmyxWorkflow.deleteAutoPublishTransitions" value="${instance?.rhythmyxWorkflow?.deleteAutoPublishTransitions}"/>
        </g:if>
        <g:else>
            <g:textField class="form-control" name="rhythmyxWorkflow.deleteAutoPublishTransitions" value="${instance?.rhythmyxWorkflow?.deleteAutoPublishTransitions}" disabled="disabled"/>
        </g:else>
    </div>
</div>

