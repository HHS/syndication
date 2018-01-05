<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 5/4/15
  Time: 4:42 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
    <style type="text/css" media="screen">
    #status {
        background-color: #eee;
        border: .2em solid #fff;
        margin: 2em 2em 1em;
        padding: 1em;
        width: 12em;
        float: left;
        -moz-box-shadow: 0px 0px 1.25em #ccc;
        -webkit-box-shadow: 0px 0px 1.25em #ccc;
        box-shadow: 0px 0px 1.25em #ccc;
        -moz-border-radius: 0.6em;
        -webkit-border-radius: 0.6em;
        border-radius: 0.6em;
    }

    .ie6 #status {
        display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
    }

    #status ul {
        font-size: 0.9em;
        list-style-type: none;
        margin-bottom: 0.6em;
        padding: 0;
    }

    #status li {
        line-height: 1.3;
    }

    #status h1 {
        text-transform: uppercase;
        font-size: 1.1em;
        margin: 0 0 0.3em;
    }

    #page-body {
        margin: 2em 1em 1.25em 18em;
    }

    h2 {
        margin-top: 1em;
        margin-bottom: 0.3em;
        font-size: 1em;
    }

    p {
        line-height: 1.5;
        margin: 0.25em 0;
    }

    #controller-list ul {
        list-style-position: inside;
    }

    #controller-list li {
        line-height: 1.3;
        list-style-position: inside;
        margin: 0.25em 0;
    }

    @media screen and (max-width: 480px) {
        #status {
            display: none;
        }

        #page-body {
            margin: 0 1em 1em;
        }

        #page-body h1 {
            margin-top: 0;
        }
    }
    </style>
</head>
<body>
<g:render template="/templates/header"/>
<div id="status" role="complementary">
    <h1>Installed Plugins</h1>
    <ul>
        <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
            <li>${plugin.name} - ${plugin.version}</li>
        </g:each>
    </ul>
</div>
<div id="page-body" role="main" style="height:1000px;">
    <h1>Application Status</h1>
    <ul>
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

    <div id="controller-list" role="navigation">
        <h2>Available Controllers:</h2>
        <ul>
            <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.shortName } }">
                <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.shortName}</g:link></li>
            </g:each>
        </ul>
    </div>
</div>
</body>
</html>
