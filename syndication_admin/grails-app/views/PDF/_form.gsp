<%@ page import="com.ctacorp.syndication.media.PDF" %>


<div class="form-group">
    <label class="col-md-4 control-label" for="name">
        <g:message code="PDF.name.label" default="Name"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:textField name="name" placeholder="pdf name" class="form-control input-md" required="" value="${PDFInstance?.name}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="description">
        <g:message code="PDF.description.label" default="Description"/>

    </label>
    <div class="col-md-8">
        <g:textArea name="description" class="form-control" cols="40" rows="5" maxlength="2000" value="${PDFInstance?.description}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="sourceUrl">
        <g:message code="PDF.sourceUrl.label" default="Source Url"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <input id="sourceUrl" name="sourceUrl"  placeholder="source url" type="url" class="form-control" required="" value="${PDFInstance?.sourceUrl}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="targetUrl">
        <g:message code="PDF.targetUrl.label" default="Target Url"/>

    </label>
    <div class="col-md-8">
        <input id="targetUrl" type="url" class="form-control"  placeholder="target url" name="targetUrl" value="${PDFInstance?.targetUrl}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="customThumbnailUrl">
        <g:message code="PDF.customThumbnailUrl.label" default="Custom Thumbnail Url"/>

    </label>
    <div class="col-md-8">
        <input type="url" id="customThumbnailUrl" name="customThumbnailUrl" placeholder="custom thumbnail url"
                    class="form-control" value="${PDFInstance?.customThumbnailUrl}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="customPreviewUrl">
        <g:message code="PDF.customPreviewUrl.label" default="Custom Preview Url"/>

    </label>
    <div class="col-md-8">
        <input type="url" id="customPreviewUrl" name="customPreviewUrl" placeholder="custom preview url"
               class="form-control" value="${PDFInstance?.customPreviewUrl}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateContentAuthored">
        <g:message code="PDF.dateContentAuthored.label" default="Date Content Authored"/>

    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentAuthored" precision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateContentAuthored}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateContentUpdated">
        <g:message code="PDF.dateContentUpdated.label" default="Date Content Updated"/>

    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentUpdated" precision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateContentUpdated}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateContentPublished">
        <g:message code="PDF.dateContentPublished.label" default="Date Content Published"/>

    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentPublished" precision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateContentPublished}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateContentReviewed">
        <g:message code="PDF.dateContentReviewed.label" default="Date Content Reviewed"/>

    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentReviewed" pprecision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateContentReviewed}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateSyndicationCaptured">
        <g:message code="PDF.dateSyndicationCaptured.label" default="Date Syndication Captured"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationCaptured" precision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateSyndicationCaptured}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateSyndicationUpdated">
        <g:message code="PDF.dateSyndicationUpdated.label" default="Date Syndication Updated"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationUpdated" precision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateSyndicationUpdated}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="dateSyndicationVisible">
        <g:message code="PDF.dateSyndicationVisible.label" default="Date Syndication Visible"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationVisible" precision="minute" relativeYears="[-20..1]"
                      value="${PDFInstance?.dateSyndicationVisible}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="language">
        <g:message code="PDF.language.label" default="Language"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:select id="language" name="language.id" from="${com.ctacorp.syndication.Language.findAllByIsActive(true, [sort: "name"])}"
                  optionKey="id" required="" value="${PDFInstance?.language?.id}" class="form-control"/>
    </div>
</div>

%{--Radio Buttons--}%
<div class="form-group">
    <label class="col-md-4 control-label" for="active">
        <g:message code="PDF.active.label" default="Active"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="active" value="${PDFInstance?.active}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="visibleInStorefront">
        <g:message code="PDF.visibleInStorefront.label" default="Visible In Storefront"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="visibleInStorefront" value="${PDFInstance?.visibleInStorefront}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="manuallyManaged">
        <g:message code="PDF.manuallyManaged.label" default="Manually Managed"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="manuallyManaged" value="${PDFInstance?.manuallyManaged}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="externalGuid">
        <g:message code="PDF.externalGuid.label" default="External Guid"/>

    </label>
    <div class="col-md-8">
        <input name="externalGuid" placeholder="guid" class="form-control input-md" value="${PDFInstance?.externalGuid}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="source">
        <g:message code="PDF.source.label" default="Source"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:select id="source" name="source.id" from="${com.ctacorp.syndication.Source.list([sort: "name"])}" optionKey="id" required=""
                  value="${PDFInstance?.source?.id}" class="form-control"/>
    </div>
</div>

<g:render template="/mediaItem/owner"/>

<script>
    $("form").submit(function(e){
        e.preventDefault();
        var self =this;
        $.ajax({
            data:{sourceUrl:document.getElementById("sourceUrl").value, initController:'${params.controller}'},
            url:'${g.createLink(controller: 'mediaItem', action: 'checkUrlContentType')}',
            success:function(response){
                if(response == "true"){
                    self.submit()
                } else {
                    var dialog = $('<p>Source Url is not of the correct content type. Are you sure you want to continue?</p>').dialog({
                        buttons: {
                            "Yes": function() {self.submit()},
                            "No":  function() {dialog.dialog('close')},
                            "Cancel":  function() {
                                dialog.dialog('close');
                            }
                        }
                    });
                }
            }
        });
    })
</script>