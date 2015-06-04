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
    <style>
        .scroll{
            height: 500px;
            overflow:scroll;
        }
    </style>
</head>

<body>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">User Breakdown</h1>
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
                        <p><strong>Total .gov:</strong> ${gov.size()}</p>
                        <p><strong>Total .org:</strong> ${org.size()}</p>
                        <p><strong>Total .net:</strong> ${net.size()}</p>
                        <p><strong>Total .edu:</strong> ${edu.size()}</p>
                        <p><strong>Total .com:</strong> ${com.size()}</p>
                        <p><strong>Total .us:</strong> ${us.size()}</p>
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
                        <p><strong>Unique .gov:</strong> ${ugov.size()}</p>
                        <p><strong>Unique .org:</strong> ${uorg.size()}</p>
                        <p><strong>Unique .net:</strong> ${unet.size()}</p>
                        <p><strong>Unique .edu:</strong> ${uedu.size()}</p>
                        <p><strong>Unique .com:</strong> ${ucom.size()}</p>
                        <p><strong>Unique .us:</strong> ${uus.size()}</p>
                        <p><strong>Unique .other:</strong> ${uevElse.size()}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div id="domainDistribution"></div>
            </div>
        </div>
        <br/>
        <div class="row">
            <g:render template="emailPanel" model="[name:'.gov', list:ugov]"/>
            <g:render template="emailPanel" model="[name:'.edu', list:uedu]"/>
            <g:render template="emailPanel" model="[name:'.org', list:uorg]"/>
        </div>
        <div class="row">
            <g:render template="emailPanel" model="[name:'.com', list:ucom]"/>
            <g:render template="emailPanel" model="[name:'.net', list:unet]"/>
            <g:render template="emailPanel" model="[name:'.us', list:uus]"/>
        </div>
        <div class="row">
            <g:render template="emailPanel" model="[name:'.other', list:uevElse]"/>
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