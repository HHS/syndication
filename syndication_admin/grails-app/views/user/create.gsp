%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
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
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
        <asset:javascript src="passwordValidator.js"/>
	</head>
	<body>
		<a href="#create-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-user" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <synd:message/>
            <synd:errors/>
            <synd:error/>
            <div class="row">
                <div class="col-md-6 col-sm-10">
                    <g:form url="[resource:userInstance, action:'save']" class="form-horizontal" role="form">
                        <fieldset class="form">
                            <g:render template="form"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="btn btn-success" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        </fieldset>
                    </g:form>
                </div>
                <div class="hidden-lg hidden-md col-sm-12"><br></div>
                <div class="col-md-6 col-sm-12">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title">Password Guidelines:</h3>
                        </div>

                        <div class="panel-body">
                            <ul>
                                <li>Be at least 8 characters</li>
                                <li>Contain at least one uppercase letter</li>
                                <li>Contain at least one lowercase letter</li>
                                <li>Contain at least one number</li>
                            </ul>
                        </div>
                    </div>

                    <!-- Button trigger modal -->
                    <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
                        Launch Role Abilities
                    </button>
                    
                    <!-- Modal -->
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header" style="background-color: #F5F5F5">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title default-color3" id="myModalLabel">Roles</h4>
                                </div>
                                <div class="modal-body">

                                    <div class="table-responsive" style="">
                                        <table class="table table-striped table-bordered table-hover">
                                            <thead>
                                            <tr><th></th><th>Stats</th><th>Basic</th><th>Publisher</th><th>User</th><th>Manager</th></tr>
                                            </thead>
                                            <tr><td>Restricted to own media</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                            <tr><td>View Dashboard</td><td>X</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Search for Media</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>View Media</td><td>X</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Create Media</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Preview Media</td><td>X</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Edit Media</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Delete Media</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                            <tr><td>Add Media to Campaigns</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Tag Media</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Feature/Unfeature Media</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Add Extended Attributes to Media</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Add Alternate Images</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>View Metrics Report</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>View raw Metrics database</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>View raw Alternate Image database</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Create Alternate Images</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Edit Alternate Images</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Delete Alternate Images</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>View Campaigns</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Create Campaigns</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Edit Campaigns</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Delete Campaigns</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Enable/Disable System Languages</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>View Extended Attributes (RAW DB)</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Create Extended Attributes</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Edit Extended Attributes</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Delete Extended Attributes</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>List Sources</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Show Sources</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Create Sources</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Edit Sources</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Delete Sources</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Show Tags</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>List Tags</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Create Tags</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td><td>X</td></tr>
                                            <tr><td>Edit Tags</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Delete Tags</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Bulk tag items</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Show Tag Types</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Create Tag Types</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Edit Tag Types</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Delete Tag Types</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Enable/Disable Tag Languages</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>See all featured media / make changes</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>X</td></tr>
                                            <tr><td>Read Health Reports</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Resolve Health Report Issues</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Run adhoc Health Report Scans</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Reindex media adhoc</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>View Server Logs</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                            <tr><td>View Users</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                            <tr><td>Edit Users < Manager</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Edit Any Users</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                            <tr><td>Creat Users < Manager</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                            <tr><td>Create Any Users</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                            <tr><td>Delete Users</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>X</td></tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="modal-footer" style="background-color: #F5F5F5">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
		</div>
	</body>
</html>
