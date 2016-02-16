<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/13/15
  Time: 3:10 PM
--%>

<%@ page import="com.ctacorp.syndication.MediaItemSubscriber; com.ctacorp.syndication.media.MediaItem" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard"/>
    <title>Ownership Cleanup</title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Media Ownership Cleanup Tool</h1>
            <h2 style="color:red;">☠ DANGER ☠</h2>
        </div><!-- /.col-lg-12 -->
    </div><!-- /.row -->

    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <g:form>
        <div class="form-group">
            <label for="query">Search Query:</label>
            <g:textField class="form-control" name="query" value="${query}"/>
        </div>
        <div class="form-group">
            <label for="subscriber" style="color: red;">Change to this subscriber:</label>
            <g:select class="form-control" name="subscriber" from="${subscribers}" optionValue="name" optionKey="id"/>
        </div>
        <div class="checkbox">
            <label>
                <g:checkBox name="restrictToUnowned" checked="${restrictToUnowned?:false}"/> Restrict to Unowned
            </label>
        </div>

        <g:actionSubmit class="btn btn-success" value="search" action="search"/>
        <g:actionSubmit class="btn btn-success" value="listAll" action="list"/>
        <g:actionSubmit class="btn btn-danger pull-right" value="Change Owner" action="associate" onclick="return confirm('Are you sure? This can alter data!!!!')"/>
    </g:form>
    <hr/>

    <p>Items found: ${mediaItems?.size()} of ${com.ctacorp.syndication.media.MediaItem.count()}</p>

    %{--${subscribers}--}%
    <g:each in="${mediaItems}" var="mi">
        <div>
            <g:set var="currentKey" value="${ subscribers.find{ it.id == "${com.ctacorp.syndication.MediaItemSubscriber.findByMediaItem(mi)?.subscriberId}" }?.name }"/>
            <span>${mi.id} - ${mi.sourceUrl}</span>
            <g:if test="${currentKey}">
                <span style="color: green; margin-left: 20px;">${currentKey}</span>
            </g:if>
            <g:else>
                <span style="color: red; margin-left: 20px;">-- NONE --</span>
            </g:else>
        </div>
    </g:each>
    <g:if test="${!mediaItems}">
        No media items found matching provided criteria.
    </g:if>
</div>
</body>
</html>