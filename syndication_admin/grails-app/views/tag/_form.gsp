<fieldset>
    <!-- Select Basic -->
    <div class="form-group">
        <label class="col-md-4 control-label" for="language">Language</label>
        <div class="col-md-5">
            <select name="language" id="language" class="form-control">
                <g:each in="${languages}" var="language">
                    <g:if test="${language.id == tag?.language?.id}">
                        <option value="${language.id}" selected>${language.name}</option>
                    </g:if>
                    <g:else>
                        <option value="${language.id}">${language.name}</option>
                    </g:else>

                </g:each>
            </select>
        </div>
    </div>

    <!-- Select Basic -->
    <div class="form-group">
        <label class="col-md-4 control-label" for="tagType">Tag Type</label>
        <div class="col-md-5">
            <select id="tagType" name="tagType" class="form-control">
                <g:each in="${tagTypes}" var="tagType">
                    <g:if test="${tagType.id == tag?.type?.id}">
                        <option value="${tagType.id}" selected>${tagType.name}</option>
                    </g:if>
                    <g:else>
                        <option value="${tagType.id}">${tagType.name}</option>
                    </g:else>

                </g:each>
            </select>

        </div>
    </div>

    <!-- Text input-->
    <div class="form-group">
        <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
        <div class="col-md-5">
            <input id="name" name="name" required="" type="text" placeholder="tag name" class="form-control input-md" value="${tag?.name}">
        </div>
    </div>

    <g:if test="${tag}">
        <g:hiddenField name="id" value="${tag.id}"/>
    </g:if>

    <!-- Button (Double) -->
    <div class="form-group">
        <label class="col-md-4 control-label" for="create"></label>
        <div class="col-md-8">
            <g:submitButton name="save" class="btn btn-success" value="Save"/>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
        </div>
    </div>

</fieldset>