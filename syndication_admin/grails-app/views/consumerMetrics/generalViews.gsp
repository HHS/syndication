<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/3/15
  Time: 3:01 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Consumers of Your Content</title>

    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
    <g:javascript>
        $(document).ready(function(){
            $("#mediaItem").tokenInput("${g.createLink(controller: 'mediaItem', action: 'tokenMediaSearch')}.json", {
                prePopulate:${mediaForTokenInput.encodeAsRaw()}
        });
        $("#percentOfTotalViewTokenInput").tokenInput("${g.createLink(controller: 'mediaItem', action: 'tokenMediaSearch')}.json", {
                        prePopulate:${mediaForTokenInput.encodeAsRaw()}
        });
    });
    </g:javascript>
    <style>
    #map {
        height: 100%;
        width: 100%;
        margin: 0;
        padding: 0;
    }
    #search {
        display: block;
        position: absolute;
        z-index: 3;
        /*top: 20px;*/
        left: 75px;
    }
    </style>
</head>
<body>
<g:render template="navPills"/>

<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>
<g:form action="generalViews">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Search for a specific MediaItem
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <g:textField name="mediaItem" id="mediaItem"/>
                    </div>
                    <div class="form-group">
                        <g:submitButton name="submit" value="Find Item Views" class="btn btn-success"/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    Total Views By Month Over The Past Year
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <div class="btn-group btn-group-sm btn-group-justified" role="group" aria-label="...">
                            <a href="#totalLine" id="totalLine" data-totalType="LINE" class="btn totalGraphType btn-default active">Line Graph</a>
                            <a href="#totalTable" id="totalTable" data-totalType="TABLE" class="btn totalGraphType btn-default">Table</a>
                        </div>
                    </div>
                    <div id="totalViews-container"></div>

                    <div>
                        <b>*Month of Year Format  YYYYMM</b>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    Mobile Views By Month
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <div class="btn-group btn-group-sm btn-group-justified" role="group" aria-label="...">
                            <a href="#mobileTotalLine" id="mobileTotalLine" data-totalType="LINE" class="btn mobileTotalGraphType btn-default active">Line Graph</a>
                            <a href="#mobileTotalTable" id="mobileTotalTable" data-totalType="TABLE" class="btn mobileTotalGraphType btn-default">Table</a>
                        </div>
                    </div>

                    <div id="mobileViews-container"></div>

                    <div>
                        <b>*Month of Year Format  YYYYMM</b>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="row">
        <div class="col-lg-4">
            <h4>Google Analytics Overview</h4>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Google Analytics
                    </h1>
                </div>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="start">Since&nbsp;</label>
                                <g:datePicker class="form-control" name="start" precision="day" years="${(new java.util.GregorianCalendar().get(Calendar.YEAR))..2006}" value="${startDate?:new Date()-30}"/>
                            </div>
                            <div class="form-group">
                                <g:submitButton name="lookup" value="Change Date" class="form-control btn btn-xs btn-success"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <g:if test="${googleOverview?.error}">
                                <div class="alert alert-danger" role="alert">
                                    <i class="fa fa-exclamation-circle"></i>
                                    <span class="sr-only">Error:</span>
                                    ${googleOverview.error}
                                </div>
                            </g:if>
                            <g:else>
                                <g:if test="${googleOverview?.stats}">
                                    <ul class="list-group">
                                        <g:each in="${googleOverview.stats}" var="stat">
                                            <li class="searchQuery list-group-item">
                                                <strong>${stat.key}</strong><br/>
                                                <g:if test="${stat.key == 'avgTimeOnPage'}">
                                                    <span>${stat.value} Seconds</span>
                                                </g:if>
                                                <g:else>
                                                    <span>${stat.value}</span>
                                                </g:else>
                                            </li>
                                        </g:each>
                                    </ul>
                                </g:if>
                            </g:else>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</g:form>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-info">
            <div class="panel-heading">
                <label>Google Analytics Data </label> include metrics that are recorded externally by google.
                <br>
                All of your content data will be displayed until you search by specific MediaItems.
            </div>
        </div>
    </div>
</div>



<script>
    (function(w,d,s,g,js,fs){
        g=w.gapi||(w.gapi={});g.analytics={q:[],ready:function(f){this.q.push(f);}};
        js=d.createElement(s);fs=d.getElementsByTagName(s)[0];
        js.src='https://apis.google.com/js/platform.js';
        fs.parentNode.insertBefore(js,fs);js.onload=function(){g.load('analytics');};
    }(window,document,'script'));
</script>
<script>
    function updateTotalViews(currentTag) {
        var type = currentTag.getAttribute('data-totalType');
        currentTag.className = "btn totalGraphType btn-default active";
        var dataChart3 = new gapi.analytics.googleCharts.DataChart({
            query: {
                'ids': 'ga:84359809',
                metrics: 'ga:pageviews',
                dimensions: 'ga:yearMonth',
                'start-date': '365daysAgo',
                'end-date': 'today',
                'filters':'${mediaFilters}'
            },
            chart: {
                container: 'totalViews-container',
                type: type,
                options: {
                    width: '100%'
                }
            }
        });

        dataChart3.execute();
    }
    function updateMobileTotalViews(currentTag) {
        var type = currentTag.getAttribute('data-totalType');
        currentTag.className = "btn mobileTotalGraphType btn-default active";

        var dataChart4 = new gapi.analytics.googleCharts.DataChart({
            query: {
                'ids': 'ga:84359809',
                metrics: 'ga:pageviews',
                dimensions: 'ga:yearMonth',
                segment: 'gaid::-11',
                'start-date': '365daysAgo',
                'end-date': 'today',
                'filters':'${mediaFilters}'
            },
            chart: {
                container: 'mobileViews-container',
                type: type,
                options: {
                    width: '100%'
                }
            }
        });
        dataChart4.execute();
    }

    gapi.analytics.ready(function() {

        /**
         * Authorize the user immediately if the user has already granted access.
         * If no access has been created, render an authorize button inside the
         * element with the ID "embed-api-auth-container".
         */
        gapi.analytics.auth.authorize({
            'serverAuth': {
                'access_token': '${tempAccessToken}'
            }
        });

        var dataChart3 = new gapi.analytics.googleCharts.DataChart({
            query: {
                'ids': 'ga:84359809',
                metrics: 'ga:pageviews',
                dimensions: 'ga:yearMonth',
                'start-date': '365daysAgo',
                'end-date': 'today',
                'filters':'${mediaFilters}'
            },
            chart: {
                container: 'totalViews-container',
                type: 'LINE',
                options: {
                    width: '100%'
                }
            }
        });

        dataChart3.execute();

        var dataChart4 = new gapi.analytics.googleCharts.DataChart({
            query: {
                'ids': 'ga:84359809',
                metrics: 'ga:pageviews',
                dimensions: 'ga:yearMonth',
                segment: 'gaid::-11',
                'start-date': '365daysAgo',
                'end-date': 'today',
                'filters':'${mediaFilters}'
            },
            chart: {
                container: 'mobileViews-container',
                type: 'LINE',
                options: {
                    width: '100%'
                }
            }
        });

        dataChart4.execute();

    });

    $(".totalGraphType").on("click", function(){
        document.getElementById("totalTable").className = "btn totalGraphType btn-default";
        document.getElementById("totalLine").className = "btn totalGraphType btn-default";
        updateTotalViews(this);
    });
    $(".mobileTotalGraphType").on("click", function(){
        document.getElementById("mobileTotalTable").className = "btn mobileTotalGraphType btn-default";
        document.getElementById("mobileTotalLine").className = "btn mobileTotalGraphType btn-default";
        updateMobileTotalViews(this);
    });
</script>
</body>
</html>