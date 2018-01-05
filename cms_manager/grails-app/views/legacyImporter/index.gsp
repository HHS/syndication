<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 7/22/14
  Time: 11:49 AM
--%>

<%@ page import="com.ctacorp.syndication.manager.cms.RhythmyxSubscriber" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
<g:form action="importLegacyRecords">
    <table>
        <tr><td>
            <label for="jsonData">Json Data:</label>
        </td><td>
            <g:textArea rows="20" cols="128" name="jsonData"/><br/>
        </td></tr>
        <tr><td>
            <label for="importTransitions">importTransitions:</label>
        </td><td>
            <g:textField name="importTransitions"/><br/>
        </td></tr>
        <tr><td>
            <label for="updateTransitions">updateTransitions:</label>
        </td><td>
            <g:textField name="updateTransitions"/><br/>
        </td></tr>
        <tr><td>
            <label for="updateAutoPublishTransitions">updateAutoPublishTransitions:</label>
        </td><td>
            <g:textField name="updateAutoPublishTransitions"/><br/>
        </td></tr>
        <tr><td>
            <label for="deleteTransitions">deleteTransitions:</label>
        </td><td>
            <g:textField name="deleteTransitions"/><br/>
        </td></tr>
        <tr><td>
            <label for="deleteAutoPublishTransitions">deleteAutoPublishTransitions:</label>
        </td><td>
            <g:textField name="deleteAutoPublishTransitions"/><br/>
        </td></tr>
        <tr><td>
            <label for="autoPublish">autoPublish:</label>
        </td><td>
            <g:checkBox name="autoPublish" value="true"/><br/>
        </td></tr>
        <tr>
            <td>
                <label for="rhythmyxSubscriber">rhythmyxSubscriber:</label>
            </td>
            <td>
                <g:select name="rhythmyxSubscriber"
                          from="${com.ctacorp.syndication.manager.cms.RhythmyxSubscriber.list()}"
                          optionValue="instanceName"
                          optionKey="id"
                />
            </td>
        </tr>
        <tr><td>
            <br/>
        <tr><td>
            <g:submitButton name="submit"/>
        </td></tr>
        <tr><td>
    </table>
</g:form>
</body>
</html>