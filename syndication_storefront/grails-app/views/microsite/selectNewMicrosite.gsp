<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 3/11/15
  Time: 10:38 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="microsite"/>
    <title>Pick a Microsite Template</title>
    <style type="text/css">
        .select-template-button{
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>

<body>
    %{--HEADER--}%
    <nav style="box-shadow: none" class="navbar navbar-default" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <a id="pageContent"></a>
                <span class="navbar-brand" href="#">Pick a Microsite Template</span>
            </div>
        </div>
    </nav>

    %{--Body container--}%
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h2>Grid Template</h2>
                <div class="well">
                    <asset:image src="microsite/grid.png" class="img-responsive center-block"/>
                    <div class="select-template-button"><g:link controller="grid" action="create" class="btn btn-md btn-success">Select</g:link></div>
                </div>
            </div>
            <div class="col-md-6">
                <h2>Carousel Template</h2>
                <div class="well">
                    <asset:image src="microsite/carousel.png" class="img-responsive center-block"/>
                    <div class="select-template-button"><g:link controller="carousel" action="create" class="btn btn-md btn-success">Select</g:link></div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <h2>Blog Template</h2>
                <div class="well">
                    <asset:image src="microsite/blog.png" class="img-responsive center-block"/>
                    <div class="select-template-button"><g:link controller="blog" action="create" class="btn btn-md btn-success">Select</g:link></div>
                </div>
            </div>
            <div class="col-md-6">
                <h2>Classic Template</h2>
                <div class="well">
                    <asset:image src="microsite/classic.png" class="img-responsive center-block"/>
                    <div class="select-template-button"><g:link controller="classic" action="create" class="btn btn-md btn-success">Select</g:link></div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>