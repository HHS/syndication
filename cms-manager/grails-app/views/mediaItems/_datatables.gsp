<%@ page import="grails.util.Holders" %>

<div class="container-fluid">

    <div class="col-sm-3"></div>

    <div class="col-sm-6" style="padding-left: 0; padding-right: 0;">
        <div class="panel panel-info">
            <div class="panel-heading" style="padding-top: 5px; padding-bottom: 5px;">
                <h3 style="font-size: 16px;" class="panel-title">About This Form</h3>
            </div>
            <div class="panel-body">
                <p style="padding-bottom: 10px;">To create a subscription, you will need the source URL of a syndicated media item. You can use the following options to search for syndicated content:</p>
                <ul>
                    <li style="padding-bottom: 10px; list-style-type: none;"><strong class="text-info">Option 1:</strong> Use the built-in search feature clicking the <em>Search</em> button below. Selecting an item field will auto-fill the <em>Source URL</em> field.</li>
                    <li style="list-style-type: none;"><strong class="text-info">Option 2:</strong> Open the <a style="color: #428BCA" class="focus-button" href="${Holders.config.storefront?.serverAddress}" target="_blank">HHS Syndication Storefront</a> in another tab and use the basic or advanced search. Copy the <em>Source URL</em> of the desired media item and paste it in the field below.</li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid" style="padding-bottom: 5px;">
    <div class="col-sm-3"></div>
    <div class="col-sm-6" style="padding-left: 0; padding-right: 0;">
        <a id="mediaItemsBrowse-button" href="#" class="btn btn-default pull-right" data-toggle="modal" data-target="#mediaItems-modal" role="button" onclick="return false;"><i style="margin-right: 5px;" class="fa fa-search" style="padding: 0"></i>Search</a>
    </div>
</div>

<div class="form-group ${hasErrors(bean: instance, field: 'sourceUrl', 'has-error')} ">

    <label class="control-label col-sm-3" for="sourceUrl">
        <g:message code="${controllerName}.sourceUrl.label"/>
        <i class="fa fa-asterisk"></i>
    </label>

    <div class="col-sm-6">

        <g:textField id="sourceUrl-input" class="form-control" name="sourceUrl" value="${instance?.sourceUrl}" aria-describedby="sourceUrlHelp"/>

        <div>
            <div class="modal fade" id="mediaItems-modal" tabindex="-1" role="dialog" aria-labelledby="mediaItemsModal-label" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 class="modal-title" id="mediaItemsModal-title"><g:message code="mediaItems.label"/></h4>
                        </div>
                        <div class="modal-body">
                            <table id="mediaItems-table" class="table table-hover table-striped row-border">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Source</th>
                                    <th>Language</th>
                                    <th></th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                        <div class="modal-footer bg-info"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
