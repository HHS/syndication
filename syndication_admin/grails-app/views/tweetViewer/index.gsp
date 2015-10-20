<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 8/27/15
  Time: 3:28 PM
--%>

<%@ page import="com.mdimension.jchronic.Chronic" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">

    <style>
        *, *:before, *:after {box-sizing:  border-box !important;}

        .tweet_row {
            -moz-column-width: 13em;
            -webkit-column-width: 13em;
            -moz-column-gap: 1em;
            -webkit-column-gap:1em;
        }

        .item {
            display: inline-block;
            padding:  .25rem;
            width:  100%;
        }

        .well {
            position:relative;
            display: block;
        }

        .clickable_thumbnail{
            width: 100%;
            margin-bottom: 10px;
        }

        .tweet_text{
            font-size: 11px;
        }

        .tweet_text_large{
            font-size: 16px;
        }

        .tweet_text_large i{
            font-size: 42px;
            display: inline-block;
            float:left;
            margin-right: 10px;
        }

        body .modal-dialog {
            width: 1000px; /* desired relative width */
            /* place center */
            margin-left:auto;
            margin-right:auto;
        }
    </style>

    <script>
        $(document).ready(function(){
            $(".clickable_thumbnail").click(function(event){
                console.log($(this).data('image_url'));
                $('#modal_body').html("<img src='" + $(this).data('image_url') + "'/>")
            });
        });
    </script>
</head>

<body>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <g:set var="percent" value="${status.remaining / status.limit * 100 as int}"/>
                <g:set var="color" value="${synd.percentColor(percent:percent)}"/>
                <g:link action="rateLimitStatus">API Rate Limiting</g:link> <span data-toggle="tooltip" title="Time until rate reset" class="pull-right"><i class="fa fa-clock-o"></i> ${Math.round(status.getSecondsUntilReset()/60)} minutes</span>
                <div class="progress">
                    <div class="progress-bar progress-bar-${color}" role="progressbar" aria-valuenow="${status.remaining}" aria-valuemin="0" aria-valuemax="${status.limit}" style="width: ${percent}%; min-width: 2em;">
                        ${percent}% (${status.remaining} / ${status.limit})
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <g:form action="index" class="form-inline">
                    <div class="form-group">
                        <label for="accountName">
                            User Account:
                        </label>
                        <g:textField name="accountName" class="form-control" value="${accountName}"/>
                    </div>

                    <div class="form-group pull-right">
                        <div class="checkbox">
                            <label for="restrictToMedia">
                                <g:checkBox name="restrictToMedia" checked="${restrictToMedia}"/> Restrict to Images
                            </label>
                        </div>
                        <g:select name="count" from="${[10, 25, 50, 100]}" value="${count}" class="form-control"/>
                        <g:submitButton name="submit" value="Get Tweets!" class="btn btn-info"/>
                    </div>
                </g:form>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <g:if test="${message}">
                    <div class="alert alert-info" role="alert">${message}</div>
                </g:if>
                <g:if test="${tweets}">
                    <div class="tweet_row">
                        <g:each in="${tweets}" var="tweet">
                            <div class="item">
                                <div class="well">
                                    <g:set var="hasMedia" value="${tweet?.mediaEntities?.mediaURLHttps[0]}"/>
                                    <div>
                                        <strong>
                                            <prettytime:display date="${tweet.createdAt}"/>
                                        </strong>
                                    </div>
                                    <div>
                                        <g:if test="${hasMedia}">
                                            <img class="clickable_thumbnail" data-image_url="${tweet.mediaEntities.mediaURLHttps[0]+":large"}" data-toggle="modal" data-target="#myModal" src="${tweet.mediaEntities.mediaURLHttps[0]+":thumb"}"/>
                                        </g:if>
                                    </div>
                                    <div class="${hasMedia ? 'tweet_text' : 'tweet_text_large'}">
                                        <i class="fa fa-twitter"></i>
                                        ${tweet.text.encodeAsRaw()}
                                    </div>
                                </div>
                            </div>
                        </g:each>
                    </div>
                </g:if>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Modal title</h4>
                </div>
                <div id="modal_body" class="modal-body">
                    ...
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>