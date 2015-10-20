<%@ page import="com.ctacorp.syndication.microsite.MediaSelector" %>
<div class="form-group">
    <label class="col-lg-4 control-label" for="pane${area}ListId">Source</label>
    <div class="col-lg-8">
        <g:select class="form-control" name="pane${area}ListId" from="${sources}" value="${currentSource}" optionKey="id" aria-label="choose agencies list of media items"/>
    </div>
</div>

<div class="form-group" hidden="hidden">
    <label class="col-sm-2 control-label" for="pane${area}selectorType">Collection</label>
    <input type="hidden" id="pane${area}selectorType" name="pane${area}selectorType" hidden="" value="${selectorType}"/>
</div>