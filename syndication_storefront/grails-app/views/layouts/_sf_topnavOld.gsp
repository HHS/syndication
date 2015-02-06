<div id="top_nav">
    <div class="left_side">
        <ul>
            <li><g:link controller="storefront" action="index">Home</g:link></li>
            %{--<li><g:link controller="storefront" action="browse">Browse</g:link></li>--}%
        </ul>
    </div>

    <div class="right_side">
        <i class="fa fa-user fa-fw"></i> <sf:currentUser/>
        <sec:ifLoggedIn>
            %{--<g:if test="${userMediaLists}">--}%
                %{--<select onchange="${remoteFunction(action: 'selectUserMediaList', controller:'userMediaList', update:"activeList", params:'\'listId=\' + this.value')}">--}%
                    %{--<g:each in="${userMediaLists}" var="userMediaList">--}%
                        %{--<option value="${userMediaList.id}">${userMediaList.name}</option>--}%
                    %{--</g:each>--}%
                %{--</select>--}%
                %{--<g:link controller="userMediaList" action="index"><button>Manage Lists</button></g:link>--}%
            %{--</g:if>--}%
            <g:link controller="userMediaList" action="index"><button>Manage Lists</button></g:link>
            <g:link controller="logout" action="index">
                <button>Logout</button>
            </g:link>
        </sec:ifLoggedIn>
    </div>
</div>