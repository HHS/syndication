<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 5/28/14
  Time: 3:09 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Featured Media</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
    <g:javascript>
        $(document).ready(function(){
            $("#featuredMedia").tokenInput("${g.createLink(controller: 'mediaItem', action: 'tokenMediaSearch')}.json?active=true&visibleInStorefront=true", {
                prePopulate:${featuredMediaForTokenInput.encodeAsRaw()}
            });
        });
    </g:javascript>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <h1>Featured Media</h1>
    </div>
    <br/>
    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER">
        <div class="row">
            <div class="col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Feature Media</h3>
                    </div>
                    <div class="panel-body">
                        <g:form action="featureMediaItems">
                            <g:textField name="featuredMedia" id="featuredMedia"/>
                            <br/>
                            <g:submitButton name="submit" value="Save Featured Items" class="btn btn-success"/>
                        </g:form>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h1 class="panel-title">Featured Media</h1>
                    </div>
                    <ul class="list-group">
                        <g:if test="${featuredMedia}">
                            <g:each in= "${featuredMedia}" status = "i" var="activeMediaInstance">
                                <g:link controller="mediaItem" class="list-group-item" action="show" id="${activeMediaInstance.id}">${activeMediaInstance.name}</g:link>
                            </g:each>
                        </g:if>
                        <g:else>
                            <div class="list-group-item">No featured media at this time.</div>
                        </g:else>
                    </ul>
                </div>
            </div>
        </div>
    </sec:ifAnyGranted>

    <sec:ifAnyGranted roles="ROLE_BASIC">
        <div class="row">
            <div class="col-lg-8">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h1 class="panel-title">Featured Media</h1>
                    </div>
                    <ul class="list-group">
                        <g:if test="${featuredMedia}">
                            <g:each in= "${featuredMedia}" status = "i" var="activeMediaInstance">
                                <div class="list-group-item" id="${activeMediaInstance.id}">${activeMediaInstance.name}</div>
                            </g:each>
                        </g:if>
                        <g:else>
                            <div class="list-group-item">No featured media at this time.</div>
                        </g:else>
                    </ul>
                </div>
            </div>
        </div>
    </sec:ifAnyGranted>

</div>
</body>
</html>