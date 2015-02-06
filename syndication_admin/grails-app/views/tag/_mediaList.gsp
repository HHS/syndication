<div class="row">
    <div class="col-lg-12">
        <!-- /.panel-heading -->
        <div class="panel-body">
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr>
                        <g:sortableColumn action="show" property="id" title="${message(code: 'tag.id.label', default: 'ID')}"/>

                        <g:sortableColumn action="show" params="[tagName: tagName]" property="name" title="${message(code: 'tag.name.label', default: 'Name')}"/>

                        <g:sortableColumn action="show" property="description" title="${message(code: 'tag.type.label', default: 'Description')}"/>

                        <g:sortableColumn action="show" property="language" title="${message(code: 'tag.language.label', default: 'Language')}"/>

                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${mediaItemsList}" status="i" var="mi">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td>${mi.id}</td>

                            <td><g:link controller="tag" action="showMediaItem" id="${mi.id}"><span class="limited-width-md ellipse">${mi.name}</span></g:link></td>

                            <td><span class="limited-width-lg ellipse">${mi.description}</span></td>

                            <td><span class="limited-width-md break-url ellipse">${mi.language}</span></td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
                <g:if test="${total > params.max}">
                    <div class="pagination">
                        <g:paginate action="show" params="[id:tag.id]" total="${total ?: 0}"/>
                    </div>
                </g:if>
            </div>
        </div>
    </div>
</div>