<%@ page import="grails.util.Holders" %>
<!-- Text input-->
<div class="form-group">
    <label class="col-md-5 control-label" for="hashTags">Hashtag(s)<span class="required-indicator">*</span></label>
    <div class="col-md-5">
        <input id="hashTags" name="hashTags" required="" value="${twitterStatusCollectorInstance?.hashTags}" type="text" placeholder="hash tags to follow" class="form-control input-md"><i>Example Format: #hashtag1</i>
    </div>
    <div class="2">
        <input type="button" class="btn btn-info"  data-toggle="modal" data-target="#formatModal" value="Formats"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: twitterStatusCollector, field: 'startDate', 'error')} required">
    <label class="col-md-5 control-label" for="startDate">
        Start Date
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-7">
        <g:datePicker name="startDate" precision="day" relativeYears="[-1..5]" value="${twitterStatusCollector?.startDate}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: twitterStatusCollector, field: 'endDate', 'error')} required">
    <label class="col-md-5 control-label" for="startDate">
        End Date
    </label>
    <div class="col-md-7">
        <g:datePicker name="endDate" precision="day" relativeYears="[0..5]" value="${twitterStatusCollector?.endDate}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-5 control-label" for="twitterAccounts">Twitter Account<span class="required-indicator">*</span></label>
    <div class="col-md-7">
        <g:select from="${twitterAccounts}" name="twitterAccounts" optionKey="id" optionValue="accountName" value="${twitterAccount}" noSelection="['':'-Choose Account to follow-']" class="form-control"/>
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-5 control-label" for="source">Source<span class="required-indicator">*</span></label>
    <div class="col-md-7">
        <g:select from="${com.ctacorp.syndication.Source.list([sort: "name"])}" name="source.id" id="source" class="form-control" optionValue="name" optionKey="id" value="${sourceId}" noSelection="${['null':'-Choose a Source-']}"/>
    </div>
</div>