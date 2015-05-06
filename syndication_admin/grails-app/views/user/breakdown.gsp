<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 4/22/15
  Time: 4:24 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard">
    <title>User Breakdown</title>
    <asset:javascript src="/plugins/morris/raphael-2.1.0.min.js"/>
    <asset:javascript src="/plugins/morris/morris.js"/>
    <asset:stylesheet src="/plugins/morris/morris-0.4.3.min.css"/>
</head>

<body>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">User Account Overview</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h1 class="panel-title">Totals</h1>
                    </div>
                    <div class="panel-body">
                        <p><strong>Total Accounts:</strong> ${totalEmails}</p>
                        <p><strong>Total .govs:</strong> ${gov.size()}</p>
                        <p><strong>Total .orgs:</strong> ${org.size()}</p>
                        <p><strong>Total .nets:</strong> ${net.size()}</p>
                        <p><strong>Total .edus:</strong> ${edu.size()}</p>
                        <p><strong>Total .coms:</strong> ${com.size()}</p>
                        <p><strong>Total .other:</strong> ${evElse.size()}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h1 class="panel-title">Unique Domains</h1>
                    </div>
                    <div class="panel-body">
                        <p><strong>Unique Domains:</strong> ${totalUniqueEmailes}</p>
                        <p><strong>Unique .govs:</strong> ${ugov.size()}</p>
                        <p><strong>Unique .orgs:</strong> ${uorg.size()}</p>
                        <p><strong>Unique .nets:</strong> ${unet.size()}</p>
                        <p><strong>Unique .edus:</strong> ${uedu.size()}</p>
                        <p><strong>Unique .coms:</strong> ${ucom.size()}</p>
                        <p><strong>Unique .other:</strong> ${uevElse.size()}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div id="domainDistribution"></div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <h2>Unique .gov's</h2>
                <ul>
                    <g:each in="${ugov}" var="dom">
                        <li>${dom}</li>
                    </g:each>
                </ul>
            </div>
            <div class="col-md-4">
                <h2>Unique .edu's</h2>
                <ul>
                    <g:each in="${uedu}" var="dom">
                        <li>${dom}</li>
                    </g:each>
                </ul>
            </div>
            <div class="col-md-4">
                <h2>Unique .org's</h2>
                <ul>
                    <g:each in="${uorg}" var="dom">
                        <li>${dom}</li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>

    <script type="application/javascript">
        function initDistributionDonut(){
            $.getJSON('${grailsApplication.config.grails.serverURL}/user/domainDistribution.json', function (data) {
                donut(data, "domainDistribution");
            });
        }

        function donut(data, element) {
            return Morris.Donut({
                element: ""+element,
                data: data,
                resize: true
            });
        }

        $(document).ready(function(){
            initDistributionDonut();
        })
    </script>
</body>
</html>