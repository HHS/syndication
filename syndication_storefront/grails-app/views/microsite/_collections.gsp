<%@ page import="com.ctacorp.syndication.microsite.MediaSelector" %>
<div class="form-group">
    <label class="col-lg-4  control-label" for="pane${area}LanguageId">Language</label>
    <div class="col-lg-8">
        <g:select name="pane${area}LanguageId" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" value="${selectedLanguage}" from="${languages}" optionKey="id" optionValue="name" class="form-control collection-languages" aria-label="choose collection language"/>
    </div>
</div>

<div class="form-group">
    <label class="col-lg-4 control-label" for="pane${area}ListId">Collection</label>
    <div class="col-lg-8" id="collection-lists">
        <g:if test="${collections}">
            <g:select class="form-control" name="pane${area}ListId" from="${collections}" value="${currentCollection}" optionKey="id" aria-label="choose collection of media items"/>
        </g:if>
        <g:else>
            There are no Collections to choose from.
        </g:else>
    </div>
</div>
<div class="form-group" hidden="hidden">
    <label class="col-sm-2 control-label" for="pane${area}selectorType">Collection</label>
    <input type="hidden" id="pane${area}selectorType" name="pane${area}selectorType" hidden="" value="${selectorType}"/>
</div>
