<div class="container">
    <div class="panel panel-default">
        <div class="panel-body">
            <form role="search" class="form-horizontal" action="index">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="control-label col-sm-6" for="search">Search / Filter By</label>
                            <div class="col-sm-6">
                                <input id="search" name="search" type="search" placeholder="Name / Email" class="form-control input-md" value="${params?.search}">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="">
                        <div class="form-group container">
                            <label class="radio-inline" for="searchSelector-0">
                                <input type="radio" name="searchSelector" id="searchSelector-0" value="user.both" ${params.searchSelector ? params?.searchSelector == 'user.both' ? "checked='true'" : "" : "checked='true'"}>
                                Both
                            </label>
                            <label class="radio-inline" for="searchSelector-1">
                                <input type="radio" name="searchSelector" id="searchSelector-1" value="user.name" ${params?.searchSelector == 'user.name' ? "checked='true'" : ""}>
                                Name
                            </label>
                            <label class="radio-inline" for="searchSelector-2">
                                <input type="radio" name="searchSelector" id="searchSelector-2" value="user.email" ${params?.searchSelector == 'user.email' ? "checked='true'" : ""}>
                                Email
                            </label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="control-label col-sm-6" for="max">Subscribers Per Page</label>
                            <div class="col-sm-6">
                                <g:select name="max" from="${maxList}" class="form-control" value="${params.max}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="">
                            <g:submitButton id="apply" name="apply" class="btn btn-primary" value="Apply"/>
                            <a href="index" class="btn btn-default">Clear Filter</a>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>