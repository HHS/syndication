<%@ page import="com.ctacorp.syndication.microsite.MediaSelector" %>

<div class="form-group">
    <g:if test="${userMediaLists}">
        <label class="col-lg-4 control-label" for="pane${area}ListId">My List</label>

        <div class="col-lg-8">
            <g:select class="form-control" name="pane${area}ListId" from="${userMediaLists}" value="${currentUserMediaList}" optionKey="id" optionValue="name" aria-label="choose your media item list"/>
        </div>
    </g:if>
    <g:else>
        <div class="col-lg-12" aria-label="No personal media lists can be found">
            <i tabindex="0" role="note">No personal Media Lists can be found. Please Select a different Media Source.</i>
        </div>
    </g:else>
</div>


<div class="form-group" hidden="hidden">
    <label class="col-sm-2 control-label" for="pane${area}selectorType">Collection</label>
    <input type="hidden" id="pane${area}selectorType" name="pane${area}selectorType" hidden="" value="${selectorType}"/>
</div>