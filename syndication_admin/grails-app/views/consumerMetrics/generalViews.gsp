<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/3/15
  Time: 3:01 PM
--%>

<%@ page import="grails.util.Holders" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Consumers of Your Content</title>

    <asset:javascript src="plugins/morris/raphael-2.1.0.min.js"/>
    <asset:javascript src="plugins/morris/morris.js"/>
    <asset:stylesheet src="plugins/morris/morris-0.4.3.min.css"/>
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
        $("#totalViews-container").html('<div id="spinnerDiv" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
        $.get('${grails.util.Holders.config.ADMIN_SERVER_URL}' + "/consumerMetrics/getTotalViews?&mediaToGraph="+"${mediaToGraph}", function(data){
            renderData(data, type, "totalViews-container");
        });
    }
    function updateMobileTotalViews(currentTag) {
        var type = currentTag.getAttribute('data-totalType');
        currentTag.className = "btn mobileTotalGraphType btn-default active";
        $("#mobileViews-container").html('<div id="spinnerDiv" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
        $.get('${Holders.config.ADMIN_SERVER_URL}' + "/consumerMetrics/getTotalMobileViews?&mediaToGraph="+"${mediaToGraph}", function(data){
            renderData(data, type, "mobileViews-container");
        });
    }

    function renderData(rows, graphType, container) {
        switch (graphType) {
            case "TABLE":
                var contents = '<table class="table table-striped"><thead><tr><th>Month</th><th>Pageviews</th></tr></thead><tbody>';

                for(var row in rows){
                    contents += '<tr><td>'+ rows[row][0] +'</td><td>'+ rows[row][1] +'</td></tr>'
                }
                contents += '</tbody></table>';
                $("#"+container).html(contents);
                break;
            case "LINE":
                var data = [];
                var labels = [];
                var values = [];
                for(var row in rows){
                    data.push({label:rows[row][0],value:Math.round(rows[row][1])});
                    labels.push(rows[row][0]);
                    values.push(rows[row][1]);
                }
                $("#" + container).text("");
                Morris.Line({
                    // ID of the element in which to draw the chart.
                    element: container,
                    // Chart data records -- each entry in this array corresponds to a point on
                    // the chart.
                    data: data,
                    // The name of the data record attribute that contains x-values.
                    xkey: "label",
//                    xLabels: 'month',
                    // A list of names of data record attributes that contain y-values.
                    ykeys: ["value"],
                    // Labels for the ykeys -- will be displayed when you hover over the
                    // chart.
                    smooth:false,
                    parseTime:false,
                    labels: ["pageviews"],
                    hideHover: 'auto',
                    resize: true
                });
                break;
        }
    }

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

    $( document ).ready(function() {
        updateMobileTotalViews(document.getElementById("mobileTotalLine"));
        updateTotalViews(document.getElementById("totalLine"));
    });

</script>
</body>
</html>