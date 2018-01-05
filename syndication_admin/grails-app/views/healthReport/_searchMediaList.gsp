
    <g:hiddenField name="whereToController" value="healthReport"/>
    <table class="table table-header">
        <thead>
        <th></th> <!-- Icon -->
        <g:sortableColumn action="${params.action}" defaultOrder="DESC" title="Date Flagged" property="dateFlagged"/>
        <g:sortableColumn action="${params.action}" title="Media ID" property="mediaItem.id"/>
        <g:sortableColumn action="${params.action}" title="Name" property="mediaItem.name"/>
        <g:sortableColumn action="${params.action}" title="Url" property="mediaItem.sourceUrl"/>
        <g:sortableColumn action="${params.action}" title="Problem" property="message"/>

        </thead>
        <tbody>

        <g:each in="${flaggedMediaItems}" var="flaggedMediaItem">
            <tr>
                <td><synd:healthFailureIcon failureType="${flaggedMediaItem.failureType}"/></td>
                <td>${flaggedMediaItem.dateFlagged.format("MMM dd, yyyy hh:mm a")}</td>
                <td>${flaggedMediaItem.mediaItem.id}</td>
                <td><g:link controller="mediaItem" action="show" id="${flaggedMediaItem.mediaItem.id}">${flaggedMediaItem.mediaItem.name}</g:link></td>
                <td style="word-break:break-all"><a href="${flaggedMediaItem.mediaItem.sourceUrl}">${flaggedMediaItem.mediaItem.sourceUrl}</a></td>
                <td>${flaggedMediaItem.message}</td>
            </tr>
        </g:each>
        </tbody>
    </table>