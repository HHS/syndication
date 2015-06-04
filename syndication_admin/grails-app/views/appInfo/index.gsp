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
    <meta name="layout" content="dashboard"/>
    <title>System Information: Syndication Admin</title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
    <a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

    <div id="status" role="complementary">
        <div class="col-lg-12">
            <h1 class="page-header">Application Status</h1>
        </div>
        <div>
            <ul>
                <li>&nbsp;</li>
                <li><strong>App version:</strong> <g:meta name="app.version"/></li>
                <li><strong>App git hash:</strong> ${metaData.app.buildHash}</li>
                <li><strong>App build date:</strong> ${metaData.app.buildDate}</li>
                <li><strong>Git commit date:</strong> ${metaData.app.lastGitCommitDate}</li>
                <li><strong>Grails version:</strong> <g:meta name="app.grails.version"/></li>
                <li><strong>Groovy version:</strong> ${GroovySystem.getVersion()}</li>
                <li><strong>JVM version:</strong> ${System.getProperty('java.version')}</li>
                <li><strong>Reloading active:</strong> ${grails.util.Environment.reloadingAgentEnabled}</li>
                <li><strong>Controllers:</strong> ${grailsApplication.controllerClasses.size()}</li>
                <li><strong>Domains:</strong> ${grailsApplication.domainClasses.size()}</li>
                <li><strong>Services:</strong> ${grailsApplication.serviceClasses.size()}</li>
                <li><strong>Tag Libraries:</strong> ${grailsApplication.tagLibClasses.size()}</li>
            </ul>
        </div>
        <h1>Installed Plugins</h1>
        <ul>
            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                <li>${plugin.name} - ${plugin.version}</li>
            </g:each>
        </ul>
    </div>

    <div id="page-body" role="main">

        <div id="controller-list" role="navigation">
            <h2>Available Controllers:</h2>
            <ul>
                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.shortName }}">
                    <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.shortName}</g:link></li>
                </g:each>
            </ul>
        </div>
    </div>
    </div>
</div>
</body>
</html>
