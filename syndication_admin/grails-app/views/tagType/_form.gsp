<fieldset>
    <g:if test="${tagTypeInstance}">
        <g:hiddenField name="id" value="${tagTypeInstance.id}"/>
    </g:if>

    <!-- Text input-->
    <div class="form-group">
        <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>

        <div class="col-md-4">
            <input id="name" name="name" type="text" required="" placeholder="name" class="form-control input-md" value="${tagTypeInstance?.name}">
        </div>
    </div>

    <!-- Textarea -->
    <div class="form-group">
        <label class="col-md-4 control-label" for="description">Description</label>

        <div class="col-md-4">
            <textarea class="form-control" id="description" name="description">${tagTypeInstance?.description}</textarea>
        </div>
    </div>
</fieldset>