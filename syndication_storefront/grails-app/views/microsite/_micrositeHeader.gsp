
<div class="builder-form-header col-xs-12">

	<div class="form-group row">
        <label class="control-label" for="title">Website Title</label>
        <input type="text" name="title" value="${microSite?.title}" placeholder="My Microsite" class="form-control" id="title">
    </div><!-- end form-group row -->

    <div class="form-group row">
        <label class="control-label" aria-label="URL should be fully qualified and link to an image approximately 100px tall" for="logo">Provide Logo URL</label>
        <p>URL should be fully qualified and link to an image approximately 100px tall</p>
        <input type="text" name="logo" value="${microSite?.logoUrl}" class="form-control" id="logo">
    </div><!-- end form-group row -->

</div><!-- end builder-form-header col-xs-12 -->
