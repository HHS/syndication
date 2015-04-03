<%@ page import="com.ctacorp.syndication.manager.cms.EmailSubscriber" %>
<%@ page import="com.ctacorp.syndication.manager.cms.RhythmyxSubscriber" %>

<div class="page-header">
    <h4>Email Subscription</h4>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="addEmailSubscription">
        <g:message code="emailSubscription.addSubscription" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="addEmailSubscription" id="addEmailSubscription" onchange="addEmail(this.checked);"/>
    </div>
</div>

<div id="emailAttributes" hidden="hidden">
    <div class="form-group">
        <label class="control-label col-sm-3" for="attachContent">
            <g:message code="emailSubscription.attachContent.label" />
        </label>
        <div class="col-sm-6" style="padding-top: .5em;">
            <g:checkBox name="attachContent" id="attachContent" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-3" for="createNewEmailSubscriber">
            <g:message code="emailSubscription.createNewEmailSubscriber.label" />
        </label>
        <div class="col-sm-6" style="padding-top: .5em;">
            <g:checkBox name="createNewEmailSubscriber" id="createNewEmailSubscriber" onchange="newEmail(this.checked)" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-3" for="emailSubscribers">
            <g:message code="emailSubscriber.label" />
        </label>
        <div class="controls col-sm-6" id="emailSubscribersSelect">
            %{--select gets genereated with javascript on ready getEmailSubscribers();--}%
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-3" for="email">
            <g:message code="emailSubscriber.email.label" />
        </label>
        <div class="col-sm-6" style="padding-top: .5em;">
            <g:textField disabled="disabled" class="form-control" name="email" id="email" />
        </div>
    </div>
    
</div>

%{--value="${instance?.emailSubscriber?.email}"--}%