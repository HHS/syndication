
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.audit.SystemEvent" %>
<div class="panel-body">
    <div class="list-group">
        <g:if test="${events?.size() > 0}">
            <g:each in="${events}" var="event">
                <a href="#" class="list-group-item">
                    <synd:eventIcon type="${event.type}"/> ${event.name}
                    <span class="pull-right text-muted small"><em><prettytime:display date="${event.dateCreated}" /></em>
                    </span>
                </a>
            </g:each>
        </g:if>
        <g:else>
            <a href="#" class="list-group-item">
            <i class="fa fa-question-circle fa-fw"></i> No recent Activity
            <span class="pull-right text-muted small">
                <em>Just Now</em>
            </span>
            </a>
        </g:else>

        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-twitter fa-fw"></i> 3 New Followers--}%
            %{--<span class="pull-right text-muted small"><em>12 minutes ago</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-envelope fa-fw"></i> Message Sent--}%
            %{--<span class="pull-right text-muted small"><em>27 minutes ago</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-tasks fa-fw"></i> New Task--}%
            %{--<span class="pull-right text-muted small"><em>43 minutes ago</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-upload fa-fw"></i> Server Rebooted--}%
            %{--<span class="pull-right text-muted small"><em>11:32 AM</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-bolt fa-fw"></i> Server Crashed!--}%
            %{--<span class="pull-right text-muted small"><em>11:13 AM</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-warning fa-fw"></i> Server Not Responding--}%
            %{--<span class="pull-right text-muted small"><em>10:57 AM</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-shopping-cart fa-fw"></i> New Order Placed--}%
            %{--<span class="pull-right text-muted small"><em>9:49 AM</em>--}%
            %{--</span>--}%
        %{--</a>--}%
        %{--<a href="#" class="list-group-item">--}%
            %{--<i class="fa fa-money fa-fw"></i> Payment Received--}%
            %{--<span class="pull-right text-muted small"><em>Yesterday</em>--}%
            %{--</span>--}%
        %{--</a>--}%
    </div>
    <!-- /.list-group -->
    <g:link controller="dashboard" action="listEvents" class="btn btn-default btn-block">View All Events</g:link>
</div>