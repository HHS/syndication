
%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<g:javascript>
    $(document).ready(function(){
        $(".areaDateSelector").click(function(){
            var labelText = $(this).text();
            $("#areaChartLabel").html("Showing content by 'Date " + labelText + "'");
            updateAreaChart($(this).attr("id"));
        });
        $(".areaPublisherDateSelector").click(function(){
            var labelText = $(this).text();
            console.log(labelText)
            $("#areaPublisherChartLabel").html("Showing content by 'Date " + labelText + "'");
            updatePublisherAreaChart($(this).attr("id"));
        });
    });

    function initPublisherContentArea(){
        $.getJSON('${grailsApplication.config.grails.serverURL}/dashboard/contentByPublisherAreaChart.json?whichDate=areaDateSelectorSyndicationCaptured', function (data) {
            area(data, "publisherContentArea");
        });
    }

    function initContentByAgencyArea(){
        $.getJSON('${grailsApplication.config.grails.serverURL}/dashboard/contentByAgencyAreaChart.json?whichDate=areaDateSelectorSyndicationCaptured', function (data) {
            area(data, "contentByAgencyArea");
        });
    }

    function updatePublisherAreaChart(whichData){
        $.getJSON('${grailsApplication.config.grails.serverURL}/dashboard/contentByPublisherAreaChart.json?whichDate=' + whichData, function (data) {
            areaChart.setData(data.data);
        });
    }

    function updateAreaChart(whichData){
        $.getJSON('${grailsApplication.config.grails.serverURL}/dashboard/contentByAgencyAreaChart.json?whichDate=' + whichData, function (data) {
            areaChart.setData(data.data);
        });
    }

    function initContentByAgencyDonut(){
        $.getJSON('${grailsApplication.config.grails.serverURL}/dashboard/contentByAgencyDonut.json', function (data) {
            donut(data, "contentByAgencyDonut");
        });
    }

    function initContentDistributionDonut(){
        $.getJSON('${grailsApplication.config.grails.serverURL}/dashboard/contentTypeDistributionDonut.json', function (data) {
            donut(data, "contentDistributionDonut");
        });
    }

    function area(data, element) {
        areaChart = Morris.Area({
            element: ""+element,
            data: data.data,
            xkey: data.xkey,
            ykeys: data.ykeys,
            labels: data.labels,
            pointSize: 2,
            hideHover: 'auto',
            resize: true
        });
        return areaChart;
    }

    function donut(data, element) {
        return Morris.Donut({
            element: ""+element,
            data: data,
            resize: true
        });
    }

    var areaChart;
    initContentByAgencyArea();
    initPublisherContentArea();
    initContentByAgencyDonut();
    initContentDistributionDonut();
</g:javascript>