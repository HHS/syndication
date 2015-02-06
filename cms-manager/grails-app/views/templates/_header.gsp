<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>

<g:form id="logout-form" url="${createLink(controller: 'logout', action: 'index')}" method="POST" hidden="hidden"/>

<nav style="box-shadow: none" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <span class="navbar-brand" href="#">CMS Manager</span>
        </div>
        <sec:ifLoggedIn>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <sec:ifAllGranted roles="ROLE_ADMIN">
                    <ul class="nav navbar-nav">
                        <li id="subscribers-nav" class="dropdown">
                            <a id="subscribers-link" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-expanded="false">Subscribers <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="${createLink(controller: 'subscriber', action: 'index')}">Subscribers</a></li>
                                <li class="divider"></li>
                                <li><a href="${createLink(controller: 'emailSubscriber', action: 'index')}">Email Subscribers</a></li>
                                <li><a href="${createLink(controller: 'restSubscriber', action: 'index')}">Rest Subscribers</a></li>
                                <li><a href="${createLink(controller: 'rhythmyxSubscriber', action: 'index')}">Rhythmyx Subscribers</a></li>
                                <li class="divider"></li>
                                <li><a href="${createLink(controller: 'keyAgreement', action: 'index')}">Key Agreements</a></li>
                            </ul>
                            <span class="sr-only">(current)</span>
                        </li>
                    </ul>
                    <ul class="nav navbar-nav">
                        <li id="subscriptions-nav" class="dropdown">
                            <a id="subscriptions-link" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-expanded="false">Subscriptions <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="${createLink(controller: 'subscription', action: 'index')}">Subscriptions</a></li>
                                <li class="divider"></li>
                                <li><a href="${createLink(controller: 'emailSubscription', action: 'index')}">Email Subscriptions</a></li>
                                <li><a href="${createLink(controller: 'restSubscription', action: 'index')}">Rest Subscriptions</a></li>
                                <li><a href="${createLink(controller: 'rhythmyxSubscription', action: 'index')}">Rhythmyx Subscriptions</a></li>
                            </ul>
                        </li>
                    </ul>
                    <ul class="nav navbar-nav">
                        <li id="users-nav" class=""><a id="users-link" href="${createLink(controller: 'user', action: 'index')}">Users</a></li>
                    </ul>
                </sec:ifAllGranted>
                <sec:ifAllGranted roles="ROLE_RHYTHMYX_USER">
                    <ul class="nav navbar-nav">
                        <li id="subscriptions-nav" class="dropdown">
                            <a id="subscriptions-link" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-expanded="false">Subscriptions <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="${createLink(controller: 'rhythmyxSubscription', action: 'index')}">Rhythmyx Subscriptions</a></li>
                            </ul>
                        </li>
                    </ul>
                </sec:ifAllGranted>
                <ul style="margin-right: 0" class="nav navbar-nav navbar-right">
                    <li><button type="button" class="btn btn-default btn-sm navbar-btn" onclick="return $('#logout-form').submit()"><i class="fa fa-power-off" style="padding: 0"></i> Logout</button></li>
                </ul>
                <ul style="margin-right: 5px" class="nav navbar-nav navbar-right">
                    <li><p class="navbar-text">User: <sec:username/></p></li>
                </ul>
            </div>
        </sec:ifLoggedIn>
    </div>
</nav>
