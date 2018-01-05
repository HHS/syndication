<%@ page import="com.ctacorp.syndication.media.PDF" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'PDF.label', default: 'PDF')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-PDF" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel panel-info">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                            <tr>

                                <g:sortableColumn property="id" class="idTables"
                                                  title="${message(code: 'PDF.id.label', default: 'Id')}"/>

                                <g:sortableColumn property="name" title="${message(code: 'PDF.name.label', default: 'Name')}"/>

                                <g:sortableColumn property="description"
                                                  title="${message(code: 'PDF.description.label', default: 'Description')}"/>

                                <g:sortableColumn property="sourceUrl"
                                                  title="${message(code: 'PDF.sourceUrl.label', default: 'Source Url')}"/>

                                <g:sortableColumn property="dateSyndicationCaptured"
                                                  title="${message(code: 'PDF.customThumbnailUrl.label', default: 'Date Syndication Captured')}"/>

                                <g:sortableColumn property="dateSyndicationUpdated"
                                                  title="${message(code: 'PDF.customPreviewUrl.label', default: 'Date Syndication Updated')}"/>

                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${PDFList}" status="i" var="PDFInstance">
                                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                    <td>${PDFInstance?.id}</td>

                                    <td><g:link action="show" id="${PDFInstance.id}">
                                        <span class="limited-width-md ellipse">${fieldValue(bean: PDFInstance, field: "name")}</span>
                                    </g:link></td>

                                    <td><span class="limited-width-lg ellipse abv60">${fieldValue(bean: PDFInstance, field: "description")}</span></td>

                                    <td>
                                        <span class="wrappedText ellipse break-url"><a target="_blank" href="${PDFInstance.sourceUrl}">

                                            ${fieldValue(bean: PDFInstance, field: "sourceUrl")}

                                        </a></span>
                                    </td>

                                    <td>${fieldValue(bean: PDFInstance, field: "dateSyndicationCaptured")}</td>

                                    <td>${fieldValue(bean: PDFInstance, field: "dateSyndicationUpdated")}</td>

                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            <div class="pagination">
                <g:paginate total="${PDFInstanceCount ?: 0}"/>
            </div>
        </div>
    </div>
</div>
</body>
</html>
