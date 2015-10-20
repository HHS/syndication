<!-- Text input-->
<div class="form-group">
    <label class="col-md-5 control-label" for="accountName">Account Name</label>
    <div class="col-md-7">
        <input id="accountName" name="accountName" required="" value="${twitterAccountInstance?.accountName}" type="text" placeholder="Account Name" class="form-control input-md">
    </div>
</div>

<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
    <div class="form-group">
        <label class="col-md-5 control-label" for="subscriberId">Subscriber</label>
        <div class="col-md-7">
            <g:select from="${subscribers}" name="subscriberId" optionKey="id" optionValue="name" value="${twitterAccountInstance?.subscriberId}" noSelection="['':'-Choose an Owner-']" class="form-control"/>
        </div>
    </div>
</sec:ifAnyGranted>