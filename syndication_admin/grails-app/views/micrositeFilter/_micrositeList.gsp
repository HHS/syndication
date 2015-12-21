<table class="table table-header">
    <thead>
    %{--<th></th> <!-- Icon -->--}%
    <g:sortableColumn action="${params.action}" defaultOrder="DESC" title="Date Flagged" property="dateFlagged"/>
    <g:sortableColumn action="${params.action}" title="Microsite ID" property="microsite.id"/>
    <g:sortableColumn action="${params.action}" title="Problem" property="message"/>
    <g:if test="${actionName=="ignored"}">
        <th>Un-Ignore</th><!-- Button -->
    </g:if>
    <g:else>
        <th>Ignore</th>
    </g:else>
    <th>Validate</th>

    </thead>
    <tbody>
    <g:each in="${flaggedMicrosites}" var="flaggedMicrosite">
        <tr>
            <td>${flaggedMicrosite.dateFlagged.format("MMM dd, yyyy hh:mm a")}</td>
            <td><g:link controller="microsite" action="show" id="${flaggedMicrosite.microsite.id}">${flaggedMicrosite.microsite.id}</g:link></td>
            <td>${flaggedMicrosite.message}</td>
            <td>
                <g:if test="${params.action == 'ignored'}">
                    <g:link action="unignoreFlaggedMicrosite" id="${flaggedMicrosite.id}"><button type="button" class="btn btn-default btn-circle" title="Ignore"><i class="fa fa-times fa-fw"></i></button></g:link>
                </g:if>
                <g:else>
                    <g:link action="ignoreFlaggedMicrosite" id="${flaggedMicrosite.id}"><button type="button" class="btn btn-default btn-circle" title="Unignore"><i class="fa fa-check fa-fw"></i></button></g:link>
                </g:else>
            </td>
            <td>
                <g:link action="checkMicrosite" id="${flaggedMicrosite.microsite.id}"><button class="btn btn-success btn-circle"><i class="fa fa-question fa-fw"></i></button></g:link>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>