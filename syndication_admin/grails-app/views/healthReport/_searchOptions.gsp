<div class="row">
    <div class="col-md-8">

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Search Media Items</h3>
            </div>
            <div class="panel-body">

                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="name">Name</label>
                        <div class="col-md-8">
                            <input id="name" name="name" type="text" value="${name}" placeholder="search by title/name" class="form-control input-md">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label" for="id">Id</label>
                        <div class="col-md-8">
                            <input id="id" name="id" type="number" value="${id}" placeholder="search by id" class="form-control input-md">
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="mediaType">Media Type</label>
                        <div class="col-md-8">
                            <g:select from="${mediaTypeList*.name}" class="form-control" keys="${mediaTypeList*.id}" name="mediaType" value="${mediaType}" noSelection="['':'-Any Type-']"/>
                        </div>
                    </div>

                    <g:if test="${subscriberList}">
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="subscriberId">Subscriber</label>
                            <div class="col-md-8">
                                <g:select class="form-control" from="${subscriberList}" name="subscriberId" optionValue="name" optionKey="id" value="${subscriberId}" noSelection="['':'-Any Subscriber-']"/>
                            </div>
                        </div>
                    </g:if>

                    <div class="form-group">
                        <label class="col-md-4 control-label" for="source">Source</label>
                        <div class="col-md-8">
                            <g:select class="form-control" from="${sourceList}" name="sourceId" optionValue="name" optionKey="id" value="${sourceId}" noSelection="['':'-Any Source-']"/>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <g:submitButton name="search" class="btn btn-success pull-right" value="${message(code: 'default.button.search.label', default: 'Search')}" />
                    </div>
                </div>

            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">How to Search Media Items</h3>
            </div>
            <div class="panel-body">
                <ul>
                    <li>Enter in at least one value in order to narrow your search</li>
                    <li>You may choose to use one or multiple values to search on</li>
                </ul>
            </div>
        </div>
    </div>
</div>