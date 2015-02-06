<table class="table table-header">
    <thead>
        <th></th> <!-- Icon -->
        <g:sortableColumn action="${params.action}" defaultOrder="DESC" title="Date Flagged" property="dateFlagged"/>
        <g:sortableColumn action="${params.action}" title="Media ID" property="mediaItem.id"/>
        <g:sortableColumn action="${params.action}" title="Name" property="mediaItem.name"/>
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
    <g:each in="${flaggedMediaItems}" var="flaggedMediaItem">
        <tr>
            <td><synd:healthFailureIcon failureType="${flaggedMediaItem.failureType}"/></td>
            <td>${flaggedMediaItem.dateFlagged.format("MMM dd, yyyy hh:mm a")}</td>
            <td>${flaggedMediaItem.mediaItem.id}</td>
            <td><g:link controller="mediaItem" action="show" id="${flaggedMediaItem.mediaItem.id}">${flaggedMediaItem.mediaItem.name}</g:link></td>
            <td>${flaggedMediaItem.message}</td>
            <td>
                <g:if test="${params.action == 'ignored'}">
                    <g:link action="unignoreFlaggedMedia" id="${flaggedMediaItem.id}"><button type="button" class="btn btn-default btn-circle" title="Ignore"><i class="fa fa-times fa-fw"></i></button></g:link>
                </g:if>
                <g:else>
                    <g:link action="ignoreFlaggedMedia" id="${flaggedMediaItem.id}"><button type="button" class="btn btn-default btn-circle" title="Unignore"><i class="fa fa-check fa-fw"></i></button></g:link>
                </g:else>
            </td>
            <td>
                <g:link action="checkMediaItem" id="${flaggedMediaItem.mediaItem.id}"><button class="btn btn-success btn-circle"><i class="fa fa-question fa-fw"></i></button></g:link>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>