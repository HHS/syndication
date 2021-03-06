<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/17/14
  Time: 8:23 AM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="dashboard">
  <g:set var="entityName" value="${message(code: 'mediaItem.label', default: 'MediaItem')}"/>
  <title>Media Item Search</title>
</head>
<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
          <h1 class="page-header">Media Item Search</h1>
        </div>
    </div>

    <div class="row">
      <form class="form-horizontal">
        <div class="col-md-8">

          <div class="panel panel-default">
              <div class="panel-heading">
                  <h3 class="panel-title">Search Media Items</h3>
              </div>
            <div class="panel-body">

              <div class="row">
                <div class="col-md-12">

                  <div class="form-group">
                    <label class="col-md-2 control-label" for="fullText">Any Text</label>
                    <div class="col-md-10">
                      <input id="fullText" name="fullText" type="text" value="${fullText}" placeholder="search any text" class="form-control input-md">
                    </div>
                  </div>

                </div>
              </div>

              <hr style="padding: 0 10px; margin-top: 0; margin-bottom: 10px;">
              <h5 style="margin-bottom: 15px; color: #555;">Filter results by:</h5>

              <div class="row">
                <div class="col-md-6">

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="name">Name</label>
                    <div class="col-md-8">
                      <input id="name" name="title" type="text" value="${title}" placeholder="search by title/name" class="form-control input-md">
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="url">Url</label>
                    <div class="col-md-8">
                      <input id="url" name="url" type="text" value="${url}" placeholder="search by url" class="form-control input-md">
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="id">Id</label>
                    <div class="col-md-8">
                      <input id="id" name="id" type="number" value="${id}" placeholder="search by id" class="form-control input-md">
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="CreatedBy">Created By</label>
                    <div class="col-md-8">
                      <input id="createdBy" name="createdBy" value="${createdBy}" placeholder="search created by" class="form-control input-md">
                    </div>
                  </div>
                </div>

                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="mediaType">Media Type</label>
                    <div class="col-md-8">
                      <g:select from="${mediaTypeList*.name}" class="form-control" keys="${mediaTypeList*.id}" name="mediaType" value="${mediaType}" noSelection="['':'-Any Type-']"/>
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="language">Language</label>
                    <div class="col-md-8">
                      <g:select class="form-control" from="${languageList}" name="language" optionKey="id" value="${language}" noSelection="['':'-Any Language-']"/>
                    </div>
                  </div>

                  <g:if test="${subscriberList}">
                    <div class="form-group">
                      <label class="col-md-4 control-label" for="subscriberId">Subscriber</label>
                      <div class="col-md-8">
                        <g:select class="form-control" from="${subscriberList}" name="subscriberId" optionValue="name" optionKey="id" value="${subscriberId}" noSelection="['':'-Any Subscriber-']"/>
                      </div>
                    </div>
                  </g:if>

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="source">Source</label>
                    <div class="col-md-8">
                      <g:select class="form-control" from="${sourceList}" name="sourceId" optionValue="name" optionKey="id" value="${sourceId}" noSelection="['':'-Any Source-']"/>
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-md-4 control-label" for="source">Items Per Page</label>
                    <div class="col-md-8">
                      <g:select class="form-control" from="[5, 10, 15, 20, 50]" name="max" value="${max}" noSelection="['':'-Items Per Page-']"/>
                    </div>
                  </div>

                </div>
              </div>

              <div class="row">
                <div class="col-md-12">
                  <g:submitButton name="search" class="btn btn-success pull-right" value="${message(code: 'default.button.search.label', default: 'Search')}" />
                </div>
              </div>

            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title">How to Search Media Items</h3>
            </div>
            <div class="panel-body">
              <ul>
                <li>Enter in at least one value in order to narrow your search</li>
                <li>You may choose to use one or multiple values to search on</li>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-12">
          <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover">
              <thead>
              <tr>
                <g:sortableColumn class="idTables" property="id" title="${message(code: 'html.id.label', default: 'Id')}" params="[fullText: params.fullText, url:params.url, title:params.title, id:params.id, language:params.language, mediaType:params.mediaType,subscriberId:params.subscriberId,sourceId:params.sourceId,createdBy:params.createdBy,max: params.max]"/>

                <g:sortableColumn property="name" title="${message(code: 'html.name.label', default: 'Name')}" params="[fullText: params.fullText, url:params.url, title:params.title, id:params.id, language:params.language, mediaType:params.mediaType,subscriberId:params.subscriberId,sourceId:params.sourceId,createdBy:params.createdBy,max: params.max]"/>

                <g:sortableColumn property="description" title="${message(code: 'html.description.label', default: 'Description')}" params="[fullText: params.fullText, url:params.url, title:params.title, id:params.id, language:params.language, mediaType:params.mediaType,subscriberId:params.subscriberId,sourceId:params.sourceId,createdBy:params.createdBy,max: params.max]"/>

                <g:sortableColumn property="sourceUrl" title="${message(code: 'html.sourceUrl.label', default: 'Source Url')}" params="[fullText: params.fullText, url:params.url, title:params.title, id:params.id, language:params.language, mediaType:params.mediaType,subscriberId:params.subscriberId,sourceId:params.sourceId,createdBy:params.createdBy,max: params.max]"/>

                <g:sortableColumn class="col-md-2" property="dateSyndicationCaptured" title="${message(code: 'html.dateSyndicationCaptured.label', default: 'Date Syndication Captured')}" params="[fullText: params.fullText, url:params.url, title:params.title, id:params.id, language:params.language, mediaType:params.mediaType,subscriberId:params.subscriberId,sourceId:params.sourceId,createdBy:params.createdBy,max: params.max]"/>

                <g:sortableColumn property="dateSyndicationUpdated" title="${message(code: 'html.dateSyndicationUpdated.label', default: 'Date Syndication Updated')}" params="[fullText: params.fullText, url:params.url, title:params.title, id:params.id, language:params.language, mediaType:params.mediaType,subscriberId:params.subscriberId,sourceId:params.sourceId,createdBy:params.createdBy,max: params.max]"/>

              </tr>
              </thead>
              <tbody>
              <g:each in="${mediaItemInstanceList}" status="i" var="mediaItemInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                  <td>${mediaItemInstance?.id}</td>

                  <td><g:link action="show" id="${mediaItemInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: mediaItemInstance, field: "name")}</span></g:link></td>

                  <td><span class="limited-width-lg ellipse">${fieldValue(bean: mediaItemInstance, field: "description")}</span></td>

                  <td><span class="wrappedText ellipse break-url">${fieldValue(bean: mediaItemInstance, field: "sourceUrl")}</span></td>

                  <td><g:formatDate date="${mediaItemInstance.dateSyndicationCaptured}"/></td>

                  <td><g:formatDate date="${mediaItemInstance.dateSyndicationUpdated}"/></td>

                </tr>
              </g:each>
              </tbody>
            </table>

            <div class="pagination">
              <g:paginate total="${mediaItemInstanceCount ?: 0}" params="[fullText: params.fullText,
                                                                          url:params.url,
                                                                          title:params.title,
                                                                          id:params.id,
                                                                          language:params.language,
                                                                          mediaType:params.mediaType,
                                                                          subscriberId:params.subscriberId,
                                                                          sourceId:params.sourceId,
                                                                          createdBy:params.createdBy,
                                                                          max: params.max]"/>
            </div>
          </div>
        </div>
      </form>
    </div>

</div>
</body>
</html>