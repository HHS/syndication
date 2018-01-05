<%@ page import="grails.util.Holders" %>

<script>

    //determines the active tab
    $(function() {
        //for bootstrap 3 use 'shown.bs.tab' instead of 'shown' in the next line
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            //save the latest tab; use cookies if you like 'em better:
            localStorage.setItem('lastTab', $(e.target).attr('id'));
        });

        //go to the latest tab, if it exists:
        var lastTab = localStorage.getItem('lastTab');
        if (lastTab) {
            $('#'+lastTab).tab('show');
        }
    });


    %{--Single MediaItems--}%
    $(document).ready(function(){
        if(localStorage.getItem('lastTab') == "singleMediaItem"){
            initLineChart();
            initDayDonut();
            initWeekDonut();
        }

        $("#singleMediaApi").click(function(){
            var labelText = $(this).text();
            $("#lineChartLabel").html("'" + labelText + "' Views Per Month");
            updateLineChart($(this).attr("class"));
        });
        $("#singleMediaStore").click(function(){
            var labelText = $(this).text();
            $("#lineChartLabel").html("'" + labelText + "' Views Per Month");
            updateLineChart($(this).attr("class"));
        });
        $("#singleMediaItem").click(function(){
            initLineChart();
            initDayDonut();
            initWeekDonut();
        });
    });

    function updateLineChart(whichData) {
        $.getJSON('${grails.util.Holders.config.ADMIN_SERVER_URL}/metricReport/mediaContent.json?mediaToGraph=${mediaToGraph.id}&whichData=' + whichData, function (data) {
            $("div#viewGraph").html('');
            lineTopTen(data,"viewGraph");
        });
    }

    function initLineChart(){
        $("div#viewGraph").text("");
        $.getJSON('${Holders.config.ADMIN_SERVER_URL}/metricReport/mediaContent.json?mediaToGraph=${mediaToGraph.id}&whichData=', function (data) {
            line(data, "viewGraph");
        });
    }

    function line(data, element) {
        lineGraph = Morris.Line({
            // ID of the element in which to draw the chart.
            element: ""+element,
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.
//    data: data.data,
            data: data.data,
            // The name of the data record attribute that contains x-values.
            xkey: data.xkey,
            xLabels: 'month',
            // A list of names of data record attributes that contain y-values.
            ykeys: data.ykeys,
            // Labels for the ykeys -- will be displayed when you hover over the
            // chart.
            labels: data.labels,
            hideHover: 'auto',
            resize: true
        });
        return lineGraph;
    }

    var lineGraph;
//    initLineChart();
//    initDayDonut();
//    initWeekDonut();

    //  Single mediaItem Donut graphs

    $(document).ready(function(){
        $(".mediaItemDateSelector").click(function(){
            var labelText = $(this).text();
            $("#dayLabel").html("Views '" + labelText + "'");
            updatePercentDonutGraph($(this).attr("id"));
        });
    });

    function updatePercentDonutGraph(range) {
        console.log('getting data for ' + range);

        $.getJSON('${Holders.config.ADMIN_SERVER_URL}/metricReport/updatePercentOfTotalViews.json?mediaToGraph=${mediaToGraph.id}&whichData=apiViewCount&range=' + range, function (data) {
            dayDonutGraph.setData(data);
        });
        $.getJSON('${Holders.config.ADMIN_SERVER_URL}/metricReport/updatePercentOfTotalViews.json?mediaToGraph=${mediaToGraph.id}&whichData=storefrontViewCount&range=' + range, function (data) {
            weekDonutGraph.setData(data);
        });
    }

    function initDayDonut(){
        $("div#day").text("");
        $.getJSON('${Holders.config.ADMIN_SERVER_URL}/metricReport/updatePercentOfTotalViews.json?mediaToGraph=${mediaToGraph.id}&range=1&whichData=', function (data) {
            dayPercentOfTotal(data, "day");
        });
    }

    function initWeekDonut(){
        $("div#week").text("");
        $.getJSON('${Holders.config.ADMIN_SERVER_URL}/metricReport/updatePercentOfTotalViews.json?mediaToGraph=${mediaToGraph.id}&range=1&whichData=', function (data) {
            weekPercentOfTotal(data, "week");
        });
    }

    function dayPercentOfTotal(data, element) {
        dayDonutGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return dayDonutGraph;
    }

    function weekPercentOfTotal(data, element) {
        weekDonutGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return weekDonutGraph;
    }

</script>

%{--Top Ten--}%
<script>

    $(document).ready(function(){
        %{--if(${secondTabActive == null}){--}%
            initTopTenLineChart();
            initStorefrontDonut();
            initApiDonut();
//        }
        $(".topTenDateSelector").click(function(){
            var labelText = $(this).text();
            $("#topTenDonutLabel").html("Views '" + labelText + "'");
            updateTopTenDonut($(this).attr("id"));
        });
        $(".topTenViewSelector").click(function(){
            var labelText = $(this).text();
            $("#topTenLineLabel").html("'" + labelText + "' Views Per Month");
            $.getJSON('${Holders.config.ADMIN_SERVER_URL}/metricReport/getTopTen.json?extra=555&typeGraph=line&range=365&whichData=' + $(this).attr("id"), function (data) {
                $("div#topTenGraph").html('');
                lineTopTen(data,"topTenGraph");
            });
        });
        $("#topTenTab").click(function(){
            initTopTenLineChart();
            initStorefrontDonut();
            initApiDonut();
        });
    });

    function updateTopTenDonut(range) {
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getTopTen.json?whichData=storefrontViewCount&range=' + range, function (data) {
            storefrontTopTenGraph.setData(data);
        });
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getTopTen.json?whichData=apiViewCount&range=' + range, function (data) {
            apiTopTenGraph.setData(data);
        });
    }

    function initStorefrontDonut(){
        $("div#storefrontTopTen").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getTopTen.json?range=1&whichData=storefrontViewCount', function (data) {
            storefrontTopTen(data, "storefrontTopTen");
        });
    }

    function initApiDonut(){
        $("div#apiTopTen").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getTopTen.json?range=1&whichData=apiViewCount', function (data) {
            apiTopTen(data, "apiTopTen");
        });
    }

    function storefrontTopTen(data, element) {
        storefrontTopTenGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return storefrontTopTenGraph;
    }

    function apiTopTen(data, element) {
        apiTopTenGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return apiTopTenGraph;
    }

    function initTopTenLineChart(){
        $("div#topTenGraph").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getTopTen.json?range=365&whichData=apiViewCount&typeGraph=line', function (data) {
            lineTopTen(data, "topTenGraph");
        });
    }

    function lineTopTen(data, element) {
        topTenLineGraph = Morris.Line({
            // ID of the element in which to draw the chart.
            element: ""+element,
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.
//    data: data.data,
            data: data.data,
            // The name of the data record attribute that contains x-values.
            xkey: data.xkey,
            xLabels: 'month',
            // A list of names of data record attributes that contain y-values.
            ykeys: data.ykeys,
            // Labels for the ykeys -- will be displayed when you hover over the
            // chart.
            labels: data.labels,
            hideHover: 'auto',
            resize: true
        });
        return topTenLineGraph;
    }

</script>

%{--SingleAgency--}%
<script>


    $(document).ready(function(){
       $(".agencyTopTenLineSelector").click(function(){
           var labelText = $(this).text();
           $("#agencyTopTenLabel").html("Top Ten '" + labelText + "' Views");
           if(labelText == "Storefront"){
               initAgencyTopTenLineChart("storefrontViewCount");
           } else {
               initAgencyTopTenLineChart("apiViewCount");
           }
       });
        $(".agencyTopTenDonutSelector").click(function(){
            var labelText = $(this).text();
            $("#agencyTopTenDonutLabel").html("Views '" + labelText + "'");
            initAgencyApiTopTenDonut($(this).attr("id"));
            initAgencyStorefrontTopTenDonut($(this).attr("id"));
        });
        if(localStorage.getItem('lastTab') == "singleAgencyTab"){
            initAgencyTopTenLineChart("apiViewCount");
            initAgencyStorefrontTopTenDonut(1);
            initAgencyApiTopTenDonut(1);
        }
    });

    function getAgencyTopTen(){
        initAgencyTopTenLineChart("apiViewCount");
        initAgencyStorefrontTopTenDonut(1);
        initAgencyApiTopTenDonut(1);
    }
    $("#singleAgencyTab").click(function(){
        initAgencyTopTenLineChart("apiViewCount");
        initAgencyStorefrontTopTenDonut(1);
        initAgencyApiTopTenDonut(1);
    });
    function initAgencyTopTenLineChart(whichData){
        var agency = document.getElementById("agency").value;
        $("div#singleAgencyLineGraph").html('<div id="spinnerDiv" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyTopTen.json?range=365&whichData=' + whichData + '&graphType=line&agency=' + agency, function (data) {
            $("#spinnerDiv").fadeOut("fast",function(){
                if(data.data){
                    agencyTopTenLine(data, "singleAgencyLineGraph");
                } else {
                    $("div#singleAgencyLineGraph").html('This agency currently has no media items with views.');
                }
            });
        });
    }

    function initAgencyStorefrontTopTenDonut(range){
        var agency = document.getElementById("agency").value;
        $("div#storefrontAgencyTopTenDonut").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyTopTen.json?range=' + range + '&whichData=storefrontViewCount&agency=' + agency, function (data) {
            storefrontAgencyTopTenDonut(data, "storefrontAgencyTopTenDonut");
        });
    }

    function initAgencyApiTopTenDonut(range){
        var agency = document.getElementById("agency").value;
        $("div#apiAgencyTopTenDonut").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyTopTen.json?range=' + range + '&whichData=apiViewCount&agency=' + agency,function (data) {
            apiAgencyTopTenDonut(data, "apiAgencyTopTenDonut");
        });
    }

    //  Donut Graphs
    function storefrontAgencyTopTenDonut(data, element) {
        storefrontAgencyTopTenGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return storefrontAgencyGraph;
    }

    function apiAgencyTopTenDonut(data, element) {
        apiAgencyTopTenGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return apiAgencyGraph;
    }

    //line Graph
    function agencyTopTenLine(data, element) {
        agencyTopTenLineGraph = Morris.Line({
            // ID of the element in which to draw the chart.
            element: ""+element,
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.
//    data: data.data,
            data: data.data,
            // The name of the data record attributfe that contains x-values.
            xkey: data.xkey,
            xLabels: 'month',
            // A list of names of data record attributes that contain y-values.
            ykeys: data.ykeys,
            // Labels for the ykeys -- will be displayed when you hover over the
            // chart.
            labels: data.labels,
            hideHover: 'auto',
            resize: true
        });
        return agencyLineGraph;
    }

    var agencyTopTenLineGraph;
    var storefrontAgencyTopTenGraph;
    var apiAgencyTopTenGraph;
</script>

%{--Agencies--}%
<script>

    $(document).ready(function(){
        $("#agencyStoreSelector").click(function(){
            var labelText = $(this).text();
            $("#agencyLabel").html("'" + labelText + "' Views Per Month");
            updateAgencyLineGraph($(this).attr("class"));
        });

        $("#agencyApiSelector").click(function(){
            var labelText = $(this).text();
            $("#agencyLabel").html("'" + labelText + "' Views Per Month");
            updateAgencyLineGraph($(this).attr("class"));
        });

        $(".agencyDateSelector").click(function(){
            var labelText = $(this).text();
            $("#agencyDonutLabel").html("Views '" + labelText + "'");
            updateAgencyDonut($(this).attr("id"));
        });

        function updateAgencyLineGraph(whichData){
            $("div#agencyGraph").html('<div id="spinnerDiv" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
            $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyViews.json?graphType=line&range=365&whichData=' + whichData, function (data) {
                $("#spinnerDiv").fadeOut("fast",function(){
                    $("div#agencyGraph").html('');
                    agencyLine(data, "agencyGraph");
                });

            });
        }

        if(localStorage.getItem('lastTab') == "agencyTab"){
            initAgencyLineChart();
            initAgencyStorefrontDonut();
            initAgencyApiDonut();
        }

        $("#agencyTab").click(function(){
            initAgencyLineChart();
            initAgencyStorefrontDonut();
            initAgencyApiDonut();
        });
    });

    function updateAgencyDonut(range) {
        console.log('getting data for the past' + range + ' day(s).');

        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyViews.json?whichData=storefrontViewCount&range=' + range, function (data) {
            storefrontAgencyGraph.setData(data);
        });
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyViews.json?whichData=apiViewCount&range=' + range, function (data) {
            apiAgencyGraph.setData(data);
        });
    }

    function initAgencyStorefrontDonut(){
        $("div#storefrontAgency").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyViews.json?range=1&whichData=storefrontViewCount', function (data) {
            storefrontAgency(data, "storefrontAgency");
        });
    }

    function initAgencyApiDonut(){
        $("div#apiAgency").text("");
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyViews.json?range=1&whichData=apiViewCount', function (data) {
            apiAgency(data, "apiAgency");
        });
    }

    function storefrontAgency(data, element) {
        storefrontAgencyGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return storefrontAgencyGraph;
    }

    function apiAgency(data, element) {
        apiAgencyGraph = Morris.Donut({
            // ID of the element in which to draw the chart.
            element: ""+element,
            data: data,
            resize: true
        });
        return apiAgencyGraph;
    }

    function initAgencyLineChart(){

        $("div#agencyGraph").html('<div id="spinnerDiv" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getAgencyViews.json?range=365&whichData=apiViewCount&graphType=line', function (data) {
            $("#spinnerDiv").fadeOut("fast",function(){
                $("div#agencyGraph").html('');
                agencyLine(data, "agencyGraph");
            });
        });
    }

    function agencyLine(data, element) {
        agencyLineGraph = Morris.Line({
            // ID of the element in which to draw the chart.
            element: ""+element,
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.
//    data: data.data,
            data: data.data,
            // The name of the data record attribute that contains x-values.
            xkey: data.xkey,
            xLabels: 'month',
            // A list of names of data record attributes that contain y-values.
            ykeys: data.ykeys,
            // Labels for the ykeys -- will be displayed when you hover over the
            // chart.
            labels: data.labels,
            hideHover: 'auto',
            resize: true
        });
        return agencyLineGraph;
    }

    var agencyLineGraph;
    var storefrontAgencyGraph;
    var apiAgencyGraph;
</script>

 %{--General--}%
<script>
    $(document).ready(function() {
        if (localStorage.getItem('lastTab') == "generalTab") {
            initGeneralLineChart();
        }
    });

    $("#generalTab").click(function(){
        initGeneralLineChart();
    });

    function initGeneralLineChart(){
        $("div#generalTotalLineGraph").html('<div id="spinnerDivGeneral" style="width:50px;" class="center-block"><asset:image src="spinner.gif"/></div>');
        $.getJSON('${grailsApplication.config.grails.serverURL}/metricReport/getTotalLineGraph', function (data) {
            console.log("before clear")
            $("#spinnerDivGeneral").fadeOut("fast",function(){
                $("div#generalTotalLineGraph").html('');
                console.log("here");
                generalTotalLine(data, "generalTotalLineGraph");
            });
        });
    }

    function generalTotalLine(data, element) {
        agencyLineGraph = Morris.Line({
            // ID of the element in which to draw the chart.
            element: ""+element,
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.
//    data: data.data,
            data: data.data,
            // The name of the data record attribute that contains x-values.
            xkey: data.xkey,
            xLabels: 'month',
            // A list of names of data record attributes that contain y-values.
            ykeys: data.ykeys,
            // Labels for the ykeys -- will be displayed when you hover over the
            // chart.
            labels: data.labels,
            hideHover: 'auto',
            resize: true
        });
        return agencyLineGraph;
    }

    var generalTotalLineGraph
</script>