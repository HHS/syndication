
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
<div id="list-consumerMetrics" class="content scaffold-list" role="main">

    <g:render template="navPills"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

<script>
    (function(w,d,s,g,js,fs){
        g=w.gapi||(w.gapi={});g.analytics={q:[],ready:function(f){this.q.push(f);}};
        js=d.createElement(s);fs=d.getElementsByTagName(s)[0];
        js.src='https://apis.google.com/js/platform.js';
        fs.parentNode.insertBefore(js,fs);js.onload=function(){g.load('analytics');};
    }(window,document,'script'));
</script>

    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Search for specific MediaItems
                </div>
                <div class="panel-body">
                    <g:form action="consumers">
                        <div class="form-group">
                            <g:textField name="mediaItem" id="mediaItem"/>
                        </div>
                        <div class="form-group">
                            <g:submitButton name="submit" value="Find Item Views" class="btn btn-success"/>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
    </div>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary">
            <div class="panel-heading">
                Views by Domains that are embedding Syndicated Content
            </div>
            <div class="panel-body">
                <div class="row">

                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Start Date:</label>
                            <g:datePicker name="startSyndicators" precision="day" years="${(new java.util.GregorianCalendar().get(Calendar.YEAR))..2006}" value="${new Date()-30}"/>
                        </div>
                        <div class="form-group">
                            <label>End Date:</label>
                            <g:datePicker id="endSyndicators" name="endSyndicators" precision="day" years="${(new java.util.GregorianCalendar().get(Calendar.YEAR))..2006}" value="${new Date()}"/>
                        </div>
                        <div class="form-group">
                            <input type="button" class="btn btn-info" onclick="updateSyndicatingDomainsDate(this)" value="Apply"/>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="btn-group btn-group-sm btn-group-justified" role="group" aria-label="...">
                                <a href="#domainsLine" id="domainsLine" data-graphType="LINE" class="btn syndicatingDomainsGraph btn-default active">Bar Graph</a>
                                <a href="#domainsTable" id="domainsTable" data-graphType="TABLE" class="btn syndicatingDomainsGraph btn-default">Table</a>
                                <a href="#domainsPie" id="domainsPie" data-graphType="PIE" class="btn syndicatingDomainsGraph btn-default">Pie</a>
                            </div>
                        </div>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="width:100%" type="button" data-toggle="dropdown">Select Top Syndicators
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu col-md-12">
                                <li><a href="#" class="syndicatingDomains btn-default active" id="domainsTopFive" data-size="5">Top 5</a></li>
                                <li><a href="#" class="syndicatingDomains" id="domainsTopTen" data-size="10">Top 10</a></li>
                                <li><a href="#" class="syndicatingDomains" id="domainsTopTwenty" data-size="20">Top 20</a></li>
                                <li><a href="#" class="syndicatingDomains" id="domainsTopFifty" data-size="50">Top 50</a></li>
                                <li><a href="#" class="syndicatingDomains" id="domainsAll" data-size="100">ALL</a></li>
                            </ul>
                        </div>
                    </div>

                </div>

                <div id="endUser-container"></div>
            </div>
        </div>
    </div>
</div>
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-info">
                <div class="panel-heading">
                    All of your content data is displayed until you search by specific MediaItems
                </div>
            </div>
        </div>
    </div>

<script>


    %{--functions for graph type toggling--}%

    function getSyndicatorsStartDay(){
        var startDay = document.getElementById("startSyndicators_day").value;
        var startMonth = document.getElementById("startSyndicators_month").value;
        var startYear = document.getElementById("startSyndicators_year").value;
        if(startDay < 10){
            startDay = "0" + startDay
        }
        if(startMonth < 10) {
            startMonth = "0" + startMonth
        }
        return startYear+"-"+startMonth+"-"+startDay
    }

    function getSyndicatorsEndDay(){
        var endDay = document.getElementById("endSyndicators_day").value;
        var endMonth = document.getElementById("endSyndicators_month").value;
        var endYear = document.getElementById("endSyndicators_year").value;
        if(endDay < 10){
            endDay = "0" + endDay
        }
        if(endMonth < 10) {
            endMonth = "0" + endMonth
        }
        return endYear+"-"+endMonth+"-"+endDay
    }

    function updateSyndicatingDomainsDate() {
        //get size
        var sizeTags = document.getElementsByClassName("syndicatingDomains");
        var size;
        for(var index = 0; index < sizeTags.length; index++){
            if(sizeTags[index].className.indexOf("active") > -1) {
                size = sizeTags[index].getAttribute("data-size")
            }
        }
        //  get graphType
        var graphTypeTags = document.getElementsByClassName("syndicatingDomainsGraph");
        var graphType;
        for(var index = 0; index < graphTypeTags.length; index++){
            if(graphTypeTags[index].className.indexOf("active") > -1) {
                graphType = graphTypeTags[index].getAttribute("data-graphType")
            }
        }
        domainsBySyndicationGraph(getSyndicatorsEndDay(),getSyndicatorsStartDay(), size, graphType);
    }

    function updateSyndicatingDomainsGraphType(currentTag) {
        var graphType = currentTag.getAttribute('data-graphType');
        currentTag.className = "btn syndicatingDomainsGraph btn-default active";

        var sizeTags = document.getElementsByClassName("syndicatingDomains");
        var size;
        for(var index = 0; index < sizeTags.length; index++){
            if(sizeTags[index].className.indexOf("active") > -1) {
                size = sizeTags[index].getAttribute("data-size")
            }
        }
        domainsBySyndicationGraph(getSyndicatorsEndDay(),getSyndicatorsStartDay(), size, graphType);
    }

    function updateSyndicatingDomainsSize(currentTag) {
        var size = currentTag.getAttribute('data-size');
        var graphTypeTags = document.getElementsByClassName("syndicatingDomainsGraph");
        var graphType;
        for(var index = 0; index < graphTypeTags.length; index++){
            if(graphTypeTags[index].className.indexOf("active") > -1) {
                graphType = graphTypeTags[index].getAttribute("data-graphType")
            }
        }
        currentTag.className = "syndicatingDomains btn-default active";
        domainsBySyndicationGraph(getSyndicatorsEndDay(),getSyndicatorsStartDay(), size, graphType);
    }

    function domainsBySyndicationGraph(endDate,startDate,size,graphType) {
        $("#endUser-container").html('<div id="spinnerDiv" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
        $.get('${grails.util.Holders.config.ADMIN_SERVER_URL}' + "/consumerMetrics/getWhosEmbedding?startDate="+startDate+"&endDate="+endDate+"&size="+size+"&mediaToGraph="+"${mediaToGraph}", function(data){
            renderData(data, graphType);
        });
    }

    function renderData(rows, graphType){
        switch(graphType) {
            case "TABLE":
                    var contents = '<table class="table table-striped"><thead><tr><th>Hostname</th><th>Pageviews</th></tr></thead><tbody>';

                    for(var row in rows){
                        contents += '<tr><td>'+ rows[row][0] +'</td><td>'+ rows[row][1] +'</td></tr>'
                    }
                    contents += '</tbody></table>';
                $("#endUser-container").html(contents);
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
                $("#endUser-container").text("");
                Morris.Bar({
                    // ID of the element in which to draw the chart.
                    element: "endUser-container",
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
                    labels: ["pageviews"],
                    hideHover: 'auto',
                    resize: true
                });
                break;
            case "PIE":
                    var data = [];
                for(var row in rows){
                    data.push({label:rows[row][0],value:rows[row][1]})
                }
                $("#endUser-container").text("");
                Morris.Donut({
                    // ID of the element in which to draw the chart.
                    element: "endUser-container",
                    data: data,
                    resize: true
                });
                break;
        }
    }

//   generate graphs on page load

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

        domainsBySyndicationGraph(getSyndicatorsEndDay(),getSyndicatorsStartDay(), 5, "LINE");

    });

    $(".syndicatingDomainsGraph").on("click", function(){
        document.getElementById("domainsLine").className = "btn syndicatingDomainsGraph btn-default";
        document.getElementById("domainsTable").className = "btn syndicatingDomainsGraph btn-default";
        document.getElementById("domainsPie").className = "btn syndicatingDomainsGraph btn-default";
        updateSyndicatingDomainsGraphType(this);
    });
    $(".syndicatingDomains").on("click", function(){
        document.getElementById("domainsTopFive").className = "syndicatingDomains";
        document.getElementById("domainsTopTen").className = "syndicatingDomains";
        document.getElementById("domainsTopTwenty").className = "syndicatingDomains";
        document.getElementById("domainsTopFifty").className = "syndicatingDomains";
        document.getElementById("domainsAll").className = "syndicatingDomains";
        updateSyndicatingDomainsSize(this);
    });
</script>

</div>
</body>
</html>