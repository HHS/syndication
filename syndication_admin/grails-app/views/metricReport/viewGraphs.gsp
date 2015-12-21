<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/10/14
  Time: 7:53 AM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Graphs</title>
  <meta name="layout" content="main">
  <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
  <asset:stylesheet src="tokenInput/token-input.css"/>
  <asset:javascript src="plugins/morris/raphael-2.1.0.min.js"/>
  <asset:javascript src="plugins/morris/morris.js"/>
  %{--<asset:javascript src="viewMetrics.js"/>--}%
  <asset:stylesheet src="plugins/morris/morris-0.4.3.min.css"/>
  <g:set var="entityName" value="Metric Graphs"/>
  <g:javascript>
        $(document).ready(function(){
            $("#featuredMedia").tokenInput("${g.createLink(controller: 'mediaItem', action: 'tokenMediaSearch')}.json", {
                prePopulate:${mediaForTokenInput.encodeAsRaw()}
            });
            $("#percentOfTotalViewTokenInput").tokenInput("${g.createLink(controller: 'mediaItem', action: 'tokenMediaSearch')}.json", {
                        prePopulate:${mediaForTokenInput.encodeAsRaw()}
            });
        });
  </g:javascript>
</head>

<body>
  <div id="list-mediaMetric" class="content scaffold-list" role="main">
    <g:render template="header"/>
  </div>

  <input type="button" class="btn btn-info pull-right" value="Field Keys" data-toggle="modal" data-target="#myModal"/>

  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" id="tab1" class="active"><a id="topTenTab" role="tab" data-toggle="tab" href="#topTen">Top Ten</a></li>
    <li role="presentation" id="tab2"><a id="singleMediaItem" role="tab" data-toggle="tab" href="#singleMediaItems">Single Media Items</a></li>
    <li role="presentation" id="tab3"><a id="singleAgencyTab" role="tab" data-toggle="tab" href="#singleAgency">Single Agency</a></li>
    <li role="presentation" id="tab4"><a id="agencyTab" role="tab" data-toggle="tab" href="#agencies">Agencies</a></li>
    <li role="presentation" id="tab5"><a id="generalTab" role="tab" data-toggle="tab" href="#general">General</a></li>
  </ul>
  <br>

<div class="tab-content">
  
  %{--Top Ten--}%
  <div class="row tab-pane active" id="topTen">
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="topTenLineLabel">'API' Views Per Month</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Metric
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="apiViewCount" class="topTenViewSelector">API</a></li>
                  <li><a href="#" id="storefrontViewCount" class="topTenViewSelector">Storefront</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div id="topTenGraph"></div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="topTenDonutLabel">Views 'Today'</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Time Range
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="1" class="topTenDateSelector">Today</a></li>
                  <li><a href="#" id="7" class="topTenDateSelector">This Week</a></li>
                  <li><a href="#" id="30" class="topTenDateSelector">This Month</a></li>
                  <li><a href="#" id="365" class="topTenDateSelector">This Year</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  %{--<i class="fa fa-bar-chart-o fa-fw"></i> <span id="apiTopTenLabel">API Views 'Today'</span>--}%
                  <i class="fa fa-bar-chart-o fa-fw"></i> <span>API Views</span>
                </div>
                <div class="panel-body">
                  <div id="apiTopTen"></div>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <i class="fa fa-bar-chart-o fa-fw"></i> <span>Storefront Views</span>
                </div>
                <div class="panel-body">
                  <div id="storefrontTopTen"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  %{--Single MediaItems--}%
  <div class="row tab-pane" id="singleMediaItems">
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Search Media Items</h3>
          </div>
          <div class="panel-body">
            <g:form action="viewGraphs" params="[fromSecondTab:true]">
              <g:textField name="mediaToGraph" id="featuredMedia"/>
              <br/>
              <g:submitButton name="submit" value="Graph Items" class="btn btn-success"/>
            </g:form>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="lineChartLabel">'API' Views Per Month</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Metric
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="singleMediaApi" class="apiViewCount">API</a></li>
                  <li><a href="#" id="singleMediaStore" class="storefrontViewCount">Storefront</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div id="viewGraph"></div>
          </div>
        </div>
      </div>
    </div>
    %{------Single MediaItems Donuts-------}%
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="dayLabel">Views Today</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Time Range
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="1" class="mediaItemDateSelector">Today</a></li>
                  <li><a href="#" id="7" class="mediaItemDateSelector">This Week</a></li>
                  <li><a href="#" id="30" class="mediaItemDateSelector">This Month</a></li>
                  <li><a href="#" id="365" class="mediaItemDateSelector">This Year</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  %{--<i class="fa fa-bar-chart-o fa-fw"></i> <span id="dayLabel">'API' Views Today</span>--}%
                  <i class="fa fa-bar-chart-o fa-fw"></i> <span>API Views</span>
                </div>
                <div class="panel-body">
                  <div id="day"></div>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <i class="fa fa-bar-chart-o fa-fw"></i><span>Storefront Views</span>
                </div>
                <div class="panel-body">
                  <div id="week"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  

  %{--Agencies--}%
  <div class="row tab-pane" id="agencies">
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="agencyLabel">'API' Views Per Month</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Metric
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="agencyApiSelector" class="apiViewCount">API</a></li>
                  <li><a href="#" id="agencyStoreSelector" class="storefrontViewCount">Storefront</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div id="agencyGraph"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="agencyDonutLabel">Views 'Today'</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Time Range
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="1" class="agencyDateSelector">Today</a></li>
                  <li><a href="#" id="7" class="agencyDateSelector">This Week</a></li>
                  <li><a href="#" id="30" class="agencyDateSelector">This Month</a></li>
                  <li><a href="#" id="365" class="agencyDateSelector">This Year</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <i class="fa fa-bar-chart-o fa-fw"></i> <span>API Views</span>
                </div>
                <div class="panel-body">
                  <div id="apiAgency"></div>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <i class="fa fa-bar-chart-o fa-fw"></i> <span>Storefront Views</span>
                </div>
                <div class="panel-body">
                  <div id="storefrontAgency"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  %{--Single Agencies--}%
  <div class="row tab-pane" id="singleAgency">
    <div class="row">
      <div class="col-md-8">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Pick an Agency</h3>
          </div>
          <div class="panel-body">
            <g:form class="form-horizontal" action="">
              <div class="form-group">
                <label class="col-md-4 control-label" for="agency">Agency</label>
                <div class="col-md-8">
                  <g:select from="${agencyList}" name="agency" optionKey="id" value="${agency}" class="form-control"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label"></label>
                <div class="col-md-8">
                  <input type="button" class="btn btn-success pull-right" name="submitAgency" Value="Apply Agency" onclick="getAgencyTopTen()"/>
                </div>
              </div>
            </g:form>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="agencyTopTenLabel">Top Ten 'API' Views</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Metric
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="agencyTopTenApiSelector" class="agencyTopTenLineSelector">API</a></li>
                  <li><a href="#" id="agencyTopTenStoreSelector" class="agencyTopTenLineSelector">Storefront</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div id="singleAgencyLineGraph"></div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i><span id="agencyTopTenDonutLabel">Views 'Today'</span>
            <div class="pull-right">
              <div class="btn-group">
                <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                  Select Time Range
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right" role="menu">
                  <li><a href="#" id="1" class="agencyTopTenDonutSelector">Today</a></li>
                  <li><a href="#" id="7" class="agencyTopTenDonutSelector">This Week</a></li>
                  <li><a href="#" id="30" class="agencyTopTenDonutSelector">This Month</a></li>
                  <li><a href="#" id="365" class="agencyTopTenDonutSelector">This Year</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <i class="fa fa-bar-chart-o fa-fw"></i><span id="agencyApiTopTenDonutLabel">API Views</span>
                </div>
                <div class="panel-body">
                  <div id="apiAgencyTopTenDonut"></div>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <i class="fa fa-bar-chart-o fa-fw"></i><span id="agencyStorefrontTopTenDonutLabel">Storefront Views</span>
                </div>
                <div class="panel-body">
                  <div id="storefrontAgencyTopTenDonut"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  %{--General Tab--}%
  <div class="row tab-pane" id="general">
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> <span id="generalTotalLabel">Total Media Item Hits</span>
          </div>
          <div class="panel-body">
            <div id="generalTotalLineGraph"></div>
          </div>
        </div>
      </div>
    </div>
  </div>

</div>

<!-- Modal for partner domains -->
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Partner Domains</h4>
      </div>
      <div class="modal-body">
        <ul class="list-group">
          <li class="list-group-item">
            <label>Storefront Views:&nbsp;</label>Generated when someone looks at the given Media Item on the HHS Syndication Storefront site.
          </li>
          <li class="list-group-item">
            <label>API Views:&nbsp;</label>Generated when the Media Item's content is pulled from our API. (Any time the items Embed Code is triggered)
          </li>
        </ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<g:render template="scripts"/>

</body>
</html>