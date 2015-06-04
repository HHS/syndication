<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 3/12/15
  Time: 7:22 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
  <asset:javascript src="application.js"/>
  <asset:stylesheet src="sb-admin.css"/>
</head>
<body>
  
  <nav style="box-shadow: none" class="navbar navbar-default" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <span class="navbar-brand" href="#">${microSite?.title}</span>
      </div>
    </div>
  </nav>
  
  <div class="container">
    <div class="row">
      <g:if test="${microSite.mediaArea1}">
        <div class="col-md-8">
          <g:each in="${blogs}" var="blog">
            <div class="panel panel-default">
              <div class="panel-body">
                ${raw(blog?.json?.results?.content[0])}
              </div>
            </div>
          </g:each>
        </div>
      </g:if>
      <div class="col-md-4">
        <g:if test="${microSite.mediaArea2}">
          <div class="panel panel-primary">
            <div class="panel-heading">
              ${microSite.mediaArea2.header}
            </div>
            <div class="panel-body">
              <g:render template="../microsite/displayStyle" model="[mediaArea:microSite.mediaArea2, mediaItems:pane2MediaItems]"/>
            </div>
          </div>
        </g:if>
        <g:if test="${microSite.mediaArea3}">
          <div class="panel panel-warning">
            <div class="panel-heading">
              ${microSite.mediaArea3.header}
            </div>
            <div class="panel-body">
              <g:render template="../microsite/displayStyle" model="[mediaArea:microSite.mediaArea3, mediaItems:pane3MediaItems]"/>
            </div>
          </div>
        </g:if>
      </div>
      
    </div>

    <div class="modal fade" id="preview_popup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">Loading...</h4>
          </div>
          <div class="modal-body" id="modal_popup_body">
            Loading...
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

  </div>

<script type="application/javascript">
  function loadModal(element){
    $("#myModalLabel").html("Loading...")
    $("#modal_popup_body").html("<i class='fa fa-spinner fa-spin fa-lg fa-fw'></i> &nbsp;Loading...")

    var mediaId = element.attr("data-media_id")
    $.getJSON("${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/"+mediaId+"/syndicate.json?callback=?", function(data){
      $("#modal_popup_body").html(data.results[0].content)
      $("#myModalLabel").html(data.results[0].name)
    })
  }

  $(document).ready(function(){
    $(".modal_popup_button").click(function(){
      loadModal($(this))
    })

    $(".list-group-item").click(function(){
      loadModal($(this))
    })
  })
</script>
</body>
</html>