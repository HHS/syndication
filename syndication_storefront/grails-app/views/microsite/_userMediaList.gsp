<%@ page import="com.ctacorp.syndication.microsite.MediaSelector" %>
<div class="form-group">
    <label class="col-lg-4 control-label" for="pane${area}ListId">UserMediaList</label>
    <div class="col-lg-8">
        <g:select class="form-control" name="pane${area}ListId" from="${userMediaLists}" value="${currentUserMediaList}" optionKey="id" optionValue="name"/>
    </div>
</div>

<div class="form-group" hidden="hidden">
    <label class="col-sm-2 control-label" for="pane${area}selectorType">Collection</label>
    <input type="hidden" id="pane${area}selectorType" name="pane${area}selectorType" hidden="" value="${selectorType}"/>
</div>