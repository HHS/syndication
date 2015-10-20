<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="width">Width<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="width" name="width" type="number" min="0" max="2147483646" value="${mediaItemInstance.width}" required="" placeholder="Image Height" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="height">Height<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="height" name="height" type="number" min="0" max="2147483646" value="${mediaItemInstance.height}" required="" placeholder="Image Height" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="altText">Alt Text<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="altText" name="altText" required="" value="${mediaItemInstance?.altText}" type="text" placeholder="Alt Text" class="form-control input-md">
    </div>
</div>

<!-- Dropdown Input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'imageFormat', 'errors')}">
    <label class="col-md-4 control-label" for="imageFormat">Image Format<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${formats}" id="imageFormat" name="imageFormat" class="form-control" required="" value="${mediaItemInstance?.imageFormat}"/>
    </div>
</div>