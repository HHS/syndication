<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 4/10/15
  Time: 9:29 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
    <div><strong>Total Jobs: ${jobs.size()}</strong></div>

    <ul>
    <g:each in="${jobs}" var="job">
        <li style="color:${job.status == 'running' ? 'red' : 'darkgray'}">${job.name} - Next Run: ${job.trigger?.nextFireTime?.time ? new Date(job.trigger?.nextFireTime?.time): ""}</li>
    </g:each>
    </ul>
</body>
</html>