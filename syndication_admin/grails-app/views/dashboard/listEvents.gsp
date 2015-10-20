%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Event Log</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="plugins/dataTables/jquery.dataTables.js"/>
    <asset:javascript src="plugins/dataTables/dataTables.bootstrap.js"/>
    <asset:stylesheet src="plugins/dataTables/dataTables.bootstrap.min.css"/>
</head>

<body>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Event Listing</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>

        <div class="row">
            <div class="col-lg-12">
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover" id="eventTable">
                            <thead>
                                <tr>
                                    <g:sortableColumn property="id" title="ID"/>
                                    <g:sortableColumn property="name" title="Name"/>
                                    <g:sortableColumn property="type" title="Type"/>
                                    <g:sortableColumn property="message" title="Message"/>
                                    <g:sortableColumn property="dateCreated" title="Date"/>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${eventInstanceList}" var="event" status="i">
                                    <tr class="${i%2==0 ? 'even' : 'odd'}">
                                        <td>${event.id}</td>
                                        <td>${event.name}</td>
                                        <td>${event.type}</td>
                                        <td>${event.message}</td>
                                        <td><prettytime:display date="${event.dateCreated}"></prettytime:display></td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <div class="pagination">
                            <g:paginate total="${total ?: 0}" />
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
%{--<script>--}%
    %{--$(document).ready(function () {--}%
        %{--$('#eventTable').dataTable();--}%
    %{--});--}%
%{--</script>--}%
</body>
</html>