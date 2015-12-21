<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 10/28/15
  Time: 9:32 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Consumers of Your Content</title>

    <script src="https://js.arcgis.com/3.14/"></script>
    <link rel="stylesheet" href="https://js.arcgis.com/3.14/esri/css/esri.css">

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


    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Search for a specific MediaItem
                </div>
                <div class="panel-body">
                    <g:form action="viewLocation">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="start">From</label>
                                    <g:datePicker name="start" precision="day" years="${(new java.util.GregorianCalendar().get(Calendar.YEAR))..2006}" value="${startDate}"/>
                                </div>
                                <div class="form-group">
                                    <label for="end">To&nbsp;</label>
                                    <g:datePicker name="end" precision="day" years="${(new java.util.GregorianCalendar().get(Calendar.YEAR))..2006}" value="${endDate}"/>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="panel panel-info">
                                    <div class="panel-heading">
                                        Large date ranges may take several seconds to load
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <g:textField name="mediaItem" id="mediaItem"/>
                        </div>
                        <div class="form-group">
                            <g:submitButton name="submit" value="Apply" class="btn btn-success"/>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">

        <div class="col-md-12">


            <h4>Media Item views by location</h4>


            <div id="search"></div>
            <div id="map"></div>
            <br>
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
        var map;

        require([
            "esri/map",
            "esri/layers/FeatureLayer",
            "esri/dijit/PopupTemplate",
            "esri/request",
            "esri/geometry/Point",
            "esri/graphic",
            "esri/tasks/locator",
            "dojo/on",
            "dojo/_base/array",
            "dojo/domReady!"
        ], function(Map, FeatureLayer, PopupTemplate, esriRequest, Point, Graphic, Locator, on, array) {
            map = new Map("map", {
//                extent: bbox,
                basemap: "topo",
                center:[-102.9833, 38.8833],
                zoom: 4
            });


            var featureCollection = {
                "layerDefinition": null,
                "featureSet": {
                    "features": [],
                    "geometryType": "esriGeometryPoint"
                }
            };
            featureCollection.layerDefinition = {
                "geometryType": "esriGeometryPoint",
                "objectIdField": "ObjectID",
                "drawingInfo": {
                    "renderer": {
                        "type": "simple",
                        "symbol": {
                            "angle":0,"xoffset":0,"yoffset":10,
                            "type": "esriPMS",
                            "url": "http://static.arcgis.com/images/Symbols/Shapes/BluePin1LargeB.png",
                            "contentType": "image/png",
                            "width": 20,
                            "height": 20
                        }
                    }
                },
                "fields": [{
                    "name": "name",
                    "alias": "name",
                    "type": "esriFieldTypeString"
                },{
                    "name": "region",
                    "alias": "region",
                    "type": "esriFieldTypeString"
                }, {
                    "name": "city",
                    "alias": "city",
                    "type": "esriFieldTypeString"
                },{
                    "name": "id",
                    "alias": "id",
                    "type": "esriFieldTypeOID"
                },{
                    "name": "ObjectID",
                    "alias": "ObjectID",
                    "type": "esriFieldTypeOID"
                },{
                    "name": "views",
                    "alias": "views",
                    "type": "esriFieldTypeString"
                }
                ]
            };

            //define a popup template
            var popupTemplate = new PopupTemplate({
                title: "{name}",
                description: "{views} view(s) from {city}, {region}",
                content:"something else"
            });

            //create a feature layer based on the feature collection
            featureLayer = new FeatureLayer(featureCollection, {
                id: 'flickrLayer',
                infoTemplate: popupTemplate
            });

            //associate the features with the popup on click
            featureLayer.on("click", function(evt) {
                map.infoWindow.setFeatures([evt.graphic]);
            });

            map.on("layers-add-result", function(results) {
                addItemsToFeatureLayer();
            });

            //add the feature layer that contains the flickr photos to the map
            map.addLayers([featureLayer]);

            //if there are mulptiple pages for the google analytics get the extra page data
            function addPagination(numPages){
                var startDayEle = document.getElementById("start_day");
                var startDay = startDayEle.options[startDayEle.selectedIndex].value;
                var startMonthEle = document.getElementById("start_month");
                var startMonth = startMonthEle.options[startMonthEle.selectedIndex].value;
                var startYearEle = document.getElementById("start_year");
                var startYear = startYearEle.options[startYearEle.selectedIndex].value;
                var endDayEle = document.getElementById("end_day");
                var endDay = endDayEle.options[endDayEle.selectedIndex].value;
                var endMonthEle = document.getElementById("end_month");
                var endMonth = endMonthEle.options[endMonthEle.selectedIndex].value;
                var endYearEle = document.getElementById("end_year");
                var endYear = endYearEle.options[endYearEle.selectedIndex].value;
                for(var pagination = 1; pagination < numPages; pagination++){
                    $.get('${grailsApplication.config.grails.serverURL}' + "/consumerMetrics/getpaginatedLocations?pagination="+pagination+"&start_day="+startDay+"&start_month="+startMonth+"&start_year="+startYear+"&end_day="+endDay+"&end_month="+endMonth+"&end_year="+endYear+"&start=1&end=1", function(data){
                        var features = [];
                        array.forEach(data, function(item){
                            var geometry = new Point(item);
                            var graphic = new Graphic(geometry);
                            graphic.setAttributes(item);
                            features.push(graphic);
                        });

                        featureLayer.applyEdits(features, null, null);
                    });
                }
            }

            function addItemsToFeatureLayer() {
                <g:applyCodec encodeAs="none">
                var data = ${items};
                </g:applyCodec>
                var features = [];
                array.forEach(data, function(item){
                    var geometry = new Point(item);
                    var graphic = new Graphic(geometry);
                    graphic.setAttributes(item);
                    features.push(graphic);
                });

                featureLayer.applyEdits(features, null, null);
                if(${numPages > 1}) {
                    addPagination(${numPages});
                }
            }

        });

    </script>

</div>
</body>
</html>