<sec:ifAnyGranted roles="ROLE_ADMIN">
    <div class="form-group">
        <label class="col-md-4 control-label" for="subscriberId">Subscriber<span class="required-indicator">*</span></label>
        <div class="col-md-8">
            <g:select from="${subscribers}" name="subscriberId" optionKey="id" optionValue="name" value="${currentSubscriber?.id}" noSelection="['':'-Choose an Owner-']" class="form-control"/>
        </div>
    </div>
</sec:ifAnyGranted>