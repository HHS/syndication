<div class="row">
    <div class="col-md-10">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Search Users</h3>
            </div>
            <div class="panel-body">
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="search">Name / Username</label>
                        <div class="col-md-8">
                            <input id="search" name="search" type="search" placeholder="Name / Username" class="form-control input-md" value="${params?.search}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="role">Role</label>
                        <div class="col-md-8">
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:select name="role" from="${com.ctacorp.syndication.authentication.Role.list()}" noSelection="['Any':'Any Role']" optionKey="authority" class="form-control" value="${currentRole}"/>
                            </sec:ifAnyGranted>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
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
                </div>


                <div class="row">
                    <div class="col-md-12">
                        <div class="pull-right">
                            <g:submitButton id="apply" name="apply" class="btn btn-primary" value="Apply"/>
                            <a href="index" class="btn btn-default">Clear Filter</a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>