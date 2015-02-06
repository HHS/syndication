<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="dashboard">
    <title>Tag Type</title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Tag Types</h1>
        </div>
    </div>
    <div class="row">
    <div class="col-lg-8">
        <synd:message/>
        <synd:error/>
        <synd:errors/>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Tag Type List
                    </h1>
                </div>


                <div class="panel-body">
                    <div class="col-lg-12">
                        <!-- /.panel-heading -->
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                <tr>
                                    <g:sortableColumn property="id"
                                                      title="${message(code: 'tagType.id.label', default: 'ID')}"/>
                                    <g:sortableColumn property="name"
                                                      title="${message(code: 'tagType.name.label', default: 'Name')}"/>
                                    <g:sortableColumn property="description"
                                                      title="${message(code: 'tagType.description.label', default: 'Description')}"/>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each in="${tagTypeInstanceList}" status="i" var="tagTypeInstance">
                                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                            <td>${tagTypeInstance.id}</td>
                                            <td><span class="limited-width-md ellipse"><g:if test="${grails.plugin.springsecurity.SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGER')}"><g:link action="edit" id="${tagTypeInstance.id}">${tagTypeInstance.name}</g:link></g:if><g:else>${tagTypeInstance.name}</g:else></span></td>
                                            <td><span class="limited-width-md ellipse">${tagTypeInstance.description ?: "-None-"}</span></td>

                                    </tr>
                                </g:each>
                                </tbody>
                            </table>

                            <g:if test="${tagTypeInstanceCount}">
                                <div class="pagination">
                                    <g:paginate total="${tagTypeInstanceCount}"/>
                                </div>
                            </g:if>

                            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER">
                                <g:form action="create">
                                    <div class="pull-right">
                                        <g:submitButton class="btn btn-default btn-md btn-success" name="Create Tag Type"/>
                                    </div>
                                </g:form>
                            </sec:ifAnyGranted>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h1 class="panel-title">Tag Type Info</h1>
                </div>
                <div class="panel-body">
                    <p>
                        Tag Types are used to define a general category to which a given tag belongs. These categories should be high level, e.g., Audience.
                    </p>
                    <p>
                        Click on a Tag Type name to edit the record.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
