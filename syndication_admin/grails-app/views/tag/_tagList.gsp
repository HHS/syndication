<g:if test="${tags} ">
    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel-body">
            <div class="row">
                <div class="col-md-6 col-lg-6">
                    <div class="form-group">
                        <label class="col-md-4" for="languageLabel">Language</label>
                        <div class="col-md-12">
                            <g:select name="languageId" value="${selectedLanguage}" from="${languages}" optionKey="id" optionValue="name" class="form-control tagSpecifics"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-6">
                    <div class="form-group">
                        <label class="col-md-4" for="typeLabel">Type</label>
                        <div class="col-md-12">
                            <g:select name="typeId" value="${selectedTagType}" from="${tagTypes}" optionKey="id" optionValue="name" class="form-control tagSpecifics"/>
                        </div>
                    </div>
                </div>
            </div>
            <br>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <g:sortableColumn action="index" params="[tagName: tagName]" property="id" title="${message(code: 'tag.id.label', default: 'ID')}"/>

                            <g:sortableColumn action="index" params="[tagName: tagName]" property="name" title="${message(code: 'tag.name.label', default: 'Name')}"/>

                            <g:sortableColumn action="index" params="[tagName: tagName]" property="type" title="${message(code: 'tag.type.label', default: 'Type')}"/>

                            <g:sortableColumn action="index" params="[tagName: tagName]" property="language" title="${message(code: 'tag.language.label', default: 'Language')}"/>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${tags}" status="i" var="tag">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${tag.id}</td>

                                <td><g:link action="show" id="${tag.id}"><span class="limited-width-md ellipse">${tag.name}</span></g:link></td>

                                <td><span class="limited-width-lg ellipse">${tag.type.name}</span></td>

                                <td><span class="limited-width-md ellipse">${tag.language.name}</span></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:if test="${total > params?.int('max')}">
                        <div class="pagination">
                            <g:paginate action="index" params="[tagName: tagName]" total="${total ?: 0}"/>
                        </div>
                    </g:if>
                </div>
            </div>
        </div>
    </div>
</g:if>
<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
    <g:form action="create">
        <div class="pull-right">
            <span>Can't find the tag you're looking for?&nbsp;</span>
            <g:hiddenField name="tagName" id="passThroughTagName" value="noValue"/>
            <button type="submit" class="btn btn-success" onclick='appendTagName()'><i class="fa fa-plus-circle"></i>&nbsp;Create Tag</button>
        </div>
    </g:form>
</sec:ifAnyGranted>


<script>
    function appendTagName(){
        $("#passThroughTagName").attr('value', $('#tagName').val());
    }
</script>