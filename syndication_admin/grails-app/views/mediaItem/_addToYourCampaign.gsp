<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER,ROLE_PUBLISHER">
    <hr>

    <div class="row">
        <div class="col-lg-5">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-flag fa-fw"></i> Add to your Campaign
                </div>
                <div class="panel-body">
                <!-- add to campaign form -->
                    <g:form action="addMediaItem" controller="campaign" params="[mediaItem:mediaItemInstance?.id]" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="addToCampaign">Pick Campaign</label>
                            <div class="col-md-4 col-lg-6">
                                <sec:ifNotGranted roles="ROLE_PUBLISHER">
                                    <g:select from="${com.ctacorp.syndication.Campaign.list()}" optionValue="name" optionKey="id" name="id" class="form-control" id="addToCampaign" noSelection="${['null':'Select A Campaign']}"/>
                                </sec:ifNotGranted>
                                <sec:ifAnyGranted roles="ROLE_PUBLISHER">
                                    <g:set var="subscriberId" value="${com.ctacorp.syndication.authentication.User.get(sec.loggedInUserInfo(field:'id') as long).subscriberId}" />
                                    <g:select from="${com.ctacorp.syndication.CampaignSubscriber.findAllBySubscriberId(464426002).campaign}" optionValue="name" optionKey="id" name="id" class="form-control" id="addToCampaign" noSelection="${['null':'Select A Campaign']}"/>
                                </sec:ifAnyGranted>
                                <br>
                                <g:actionSubmit controller="campaign" action="addMediaItem" class="pull-right btn btn-success" value="Add"/>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
        <div class="col-lg-7">
            <g:render template="/tag/tagSingleItemWidget"/>
            <br>
        </div>
    </div>
</sec:ifAnyGranted>