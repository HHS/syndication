<div class="row">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button data-target="#search_filter" data-toggle="collapse" class="navbar-toggle collapsed" type="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="#" class="navbar-brand">Search / Filter By</a>
            </div>
            <div id="search_filter" class="collapse navbar-collapse">
                <form role="search" class="navbar-form navbar-center" action="index.gsp">
                    <div class="form-group">
                        <label class="sr-only" for="search">Name / Username</label>
                        <div class="col-md-4">
                            <input id="search" name="search" type="search" placeholder="Name / Username" class="form-control input-md" value="${params?.search}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="role">Role</label>
                        <div class="col-md-4">
                            <sec:ifAnyGranted roles="ROLE_MANAGER">
                                <g:select name="role" from="${com.ctacorp.syndication.authentication.Role.findAllByAuthorityInList(["ROLE_USER", "ROLE_BASIC", "ROLE_STATS"])}" optionKey="authority" noSelection="['Any':'Any']" class="form-control" value="${currentRole}"/>
                            </sec:ifAnyGranted>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:select name="role" from="${com.ctacorp.syndication.authentication.Role.list()}" noSelection="['Any':'Any Role']" optionKey="authority" class="form-control" value="${currentRole}"/>
                            </sec:ifAnyGranted>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="radio-inline col-md-3" for="searchSelector-0">
                            <input type="radio" name="searchSelector" id="searchSelector-0" value="user.both" ${params.searchSelector ? params?.searchSelector == 'user.both' ? "checked='true'" : "" : "checked='true'"}>
                            Both
                        </label>
                        <label class="radio-inline col-md-3" for="searchSelector-1">
                            <input type="radio" name="searchSelector" id="searchSelector-1" value="user.name" ${params?.searchSelector == 'user.name' ? "checked='true'" : ""}>
                            Name
                        </label>
                        <label class="radio-inline col-md-3" for="searchSelector-2">
                            <input type="radio" name="searchSelector" id="searchSelector-2" value="user.username" ${params?.searchSelector == 'user.username' ? "checked='true'" : ""}>
                            Username
                        </label>
                    </div>
                    <div class="pull-right">
                        <g:submitButton id="apply" name="apply" class="btn btn-primary" value="Apply"/>
                        <a href="index.gsp" class="btn btn-default">Clear Filter</a>
                    </div>

                </form>
            </div>
        </div>
    </nav>
</div>