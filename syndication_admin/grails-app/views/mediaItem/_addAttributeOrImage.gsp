<hr>
<div class="row">

    <div class="col-md-6">

        <div class="clearfix"></div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-subscript fa-fw"></i> Add Extended Attribute
            </div>
            <div class="panel-body">
            <!-- add extended Attribute form -->
                <g:form controller="extendedAttribute" action="addAttribute" params="[mediaItem: mediaItemInstance?.id]" method="POST" class="form-horizontal">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4" for="attrName">Attribute<span class="required-indicator">*</span></label>
                                <div class="col-md-12">
                                    <input id="attrName" name="name" placeholder="attribute" class="form-control input-md">
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4" for="attrValue">Value<span class="required-indicator">*</span></label>
                                <div class="col-md-12">
                                    <input id="attrValue" name="value" placeholder="value" class="form-control input-md">
                                    <br>
                                    <g:actionSubmit action="addAttribute" class="pull-right btn btn-success" value="Add"/>
                                </div>
                            </div>
                        </div>

                    </div>

                </g:form>
            </div>
        </div>
        <h3>Extended Attributes</h3>

        <g:if test="${mediaItemInstance.extendedAttributes}">
            <div class="row">

                <div class="col-xs-5">
                    <strong>Key</strong>
                </div>
                <div class="col-xs-7">
                    <strong>Value</strong>
                </div>
            </div>
            <div style="overflow-x: hidden; height:100px">
                <g:each in="${mediaItemInstance.extendedAttributes.sort { it.name }}" var="attributeInstance">
                    <div class="row">
                        <div class="col-xs-5">
                            <p class="word_wrap"><g:link controller="extendedAttribute" action="show" id="${attributeInstance.id}">${attributeInstance.name}</g:link></p>
                        </div>
                        <div class="col-xs-7">
                            <p class="word_wrap">${attributeInstance.value}</p>
                        </div>
                    </div>
                </g:each>
            </div>
        </g:if>
        <g:else>
            No Attributes Created Yet
        </g:else>
    </div>

    <div class="col-md-6">

        <div class="clearfix"></div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-picture-o fa-fw"></i> Add an Alternate Image
            </div>
            <div class="panel-body">
            <!--AlternateImage Form-->
                <g:form class="form-horizontal" controller="alternateImage" action="addAlternateImage" params="[mediaItem: mediaItemInstance?.id]" method="POST">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4" for="imageWidth">Width</label>
                                <div class="col-md-12">
                                    <input id="imageWidth" name="width" type="number" placeholder="image width" class="form-control input-md">
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4" for="imageHeight">Height</label>
                                <div class="col-md-12">
                                    <input id="imageHeight" name="height" type="number" placeholder="image height" class="form-control input-md">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4" for="imageName">Name<span class="required-indicator">*</span></label>
                                <div class="col-md-12">
                                    <input id="imageName" name="name" placeholder="image name" class="form-control input-md">
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4" for="imageUrl">Url<span class="required-indicator">*</span></label>
                                <div class="col-md-12">
                                    <input id="imageUrl" name="url" type="url" placeholder="location" class="form-control input-md">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-6">
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label class="col-md-4 control-label"></label>
                                <div class="col-md-8">
                                    <g:actionSubmit action="addAlternateImage" class="pull-right btn btn-success" value="Add"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
        <h3>Alternate Images</h3>
        <g:if test="${mediaItemInstance.alternateImages}">
            <div class="row">
                <div class="col-xs-4">
                    <strong>Image Name</strong>
                </div>
                <div class="col-xs-8">
                    <strong>Url</strong>
                </div>
            </div>
            <div style="overflow-x: hidden; height:100px">
                <g:each in="${mediaItemInstance?.alternateImages}" var="alternateInstance">
                    <div class="row">
                        <div class="col-xs-4">
                            <p class="word_wrap"><g:link controller="alternateImage" action="show" id="${alternateInstance.id}">${alternateInstance.name}</g:link></p>
                        </div>
                        <div class="col-xs-8">
                            <p class="word_wrap">${alternateInstance.url}</p>
                        </div>
                    </div>
                </g:each>
            </div>
        </g:if>
        <g:else>
            No Images Created Yet
        </g:else>
    </div>
</div>
    <br>