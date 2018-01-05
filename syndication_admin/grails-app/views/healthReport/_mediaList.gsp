<g:form class="deleteAllForm" controller="mediaItem" action="deleteAll">
    <g:hiddenField name="whereToController" value="healthReport"/>
<table class="table table-header">
    <thead>
        <sec:ifAllGranted roles="ROLE_ADMIN">
            <th></th> <!-- select box -->
        </sec:ifAllGranted>
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
                <sec:ifAllGranted roles="ROLE_ADMIN">
                    <td>
                        <g:checkBox name="deleteChecked_${flaggedMediaItem.mediaItem.id}" class="deleteCheckbox"/>
                    </td>
                </sec:ifAllGranted>
                <td><synd:healthFailureIcon failureType="${flaggedMediaItem.failureType}"/></td>
                <td>${flaggedMediaItem.dateFlagged.format("MMM dd, yyyy hh:mm a")}</td>
                <td>${flaggedMediaItem.mediaItem.id}</td>
                <td><g:link controller="mediaItem" action="show" id="${flaggedMediaItem.mediaItem.id}">${flaggedMediaItem.mediaItem.name}</g:link></td>
                <td>${flaggedMediaItem.message}</td>
                <td>
                    <g:if test="${params.action == 'ignored'}">
                        <g:link action="unignoreFlaggedMedia" id="${flaggedMediaItem.id}"><button type="button" class="btn btn-default btn-circle" title="Ignore"><i class="fa fa-check fa-fw"></i></button></g:link>
                    </g:if>
                    <g:else>
                        <g:link action="ignoreFlaggedMedia" id="${flaggedMediaItem.id}"><button type="button" class="btn btn-default btn-circle" title="Unignore"><i class="fa fa-times fa-fw"></i></button></g:link>
                    </g:else>
                </td>
                <td>
                    <g:link action="checkMediaItem" id="${flaggedMediaItem?.mediaItem?.id}"><button type="button" class="btn btn-success btn-circle"><i class="fa fa-question fa-fw"></i></button></g:link>
                </td>
            </tr>
        </g:each>
    </tbody>
</table>
</g:form>

<sec:ifAllGranted roles="ROLE_ADMIN">
    <div style="margin: 10px;">
        <button id="selectAllButton" class="btn btn-primary">Select All</button>
        <button id="deselectAllButton" class="btn btn-default">Deselect All</button>
        <button id="deleteAllButton" class="btn btn-danger">Delete All Checked</button>
    </div>

    <script>
        $(document).ready(function(){
            $("#selectAllButton").click(function(){
                $('.deleteCheckbox').prop('checked', true)
            });
            $("#deselectAllButton").click(function(){
                $('.deleteCheckbox').prop('checked', false)
            });
            $("#deleteAllButton").click(function(){
                $('.deleteAllForm').submit();
            });
        });
    </script>
</sec:ifAllGranted>