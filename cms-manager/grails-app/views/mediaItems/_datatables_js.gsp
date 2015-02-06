<%@ page import="grails.util.Holders" %>
<%--suppress JSJQueryEfficiency --%>
<script type="text/javascript" class="init">

    var thumbnailServiceUrl = '${Holders.config.syndication.serverUrl + Holders.config.syndication.apiPath + Holders.config.syndication.thumbnailPath}';

    function selectMediaItem(sourceUrl) {
        $('#sourceUrl-input').val(sourceUrl);
        $('#mediaItems-modal').modal('hide');
    };

    function format(data) {
        return '<table class="table table-condensed table-responsive" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none; padding-top: 10px;">ID:</td>' +
                '<td style="border-top: none; padding-top: 10px;">' + data.id + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none;">Name:</td>' +
                '<td style="border-top: none;">' + data.name + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none;">Description:</td>' +
                '<td style="border-top: none;">' + data.description + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none;">Source Url:</td>' +
                '<td style="border-top: none;">' + data.sourceUrl + '</td>' +
                '</tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none;">Syndication ID:</td>' +
                '<td style="border-top: none;">' + data.id + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none;"">Language:</td>' +
                '<td style="border-top: none;">' + data.language.name + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none; padding-bottom: 10px;">Source:</td>' +
                '<td style="border-top: none; padding-bottom: 10px;">' + data.source.name + '</td>' +
                '</tr>' +
                '<tr>' +
                '<td width="150px" align="right" style="font-weight: bold; border-top: none; padding-bottom: 10px;">Thumbnail Preview:</td>' +
                '<td style="border-top: none; padding-bottom: 10px;"><img class="img-responsive img-thumbnail" src="' + thumbnailServiceUrl.replace('{id}', data.id) + '"/></td>' +
                '</tr>' +
                '</table>';
    }

    $(document).ready(function () {

        var table = $('#mediaItems-table').DataTable({
            "scrollY": "450px",
            "scrollX": 100,
            "scrollXInner": "100%",
            "displayLength": 20,
            "processing": true,
            "serverSide": true,
            "paging": true,
            "pagingType": "full_numbers",
            "lengthChange": false,
            "order": [[0, "asc"]],
            "ajax": {
                "url": "${createLink(controller: 'mediaItems', action: 'loadMediaItems')}"
            },
            "language": {
                "search": "Filter by name:"
            },
            "columns": [
                {
                    'render': function renderIdCheckboxColumn(data, type, row) {
                        return '<div class="fa fa-plus-circle detail-expando'+data.id+' plus-sign" style="cursor:pointer;" role="button" href="#" id="' + data.id + '"><\/div>';
                    },
                    "className": 'media-details',
                    "orderable": false,
                    "searchable": false,
                    "data": null,
                    "defaultContent": ''
                },
                {
                    "data": 'id',
                    "orderable": false,
                    "searchable": false
                },
                {
                    "data": 'name',
                    "orderable": true,
                    "searchable": true
                },
                {
                    'render': function renderDescriptionColumn(data, type, row) {

                        if (data) {

                            if(data.length > 250) {
                                return '<p style="font-size: 12px">' + data.substring(0, 249) + '</p>';
                            } else {
                                return '<p style="font-size: 12px">' + data + '</p>';
                            }

                        } else {
                            return ''
                        }
                    },
                    "data": 'description',
                    "orderable": false,
                    "searchable": false
                },
                {
                    'render': function renderSourceAcronymColumn(data, type, row) {
                        return '<p style="font-size: small">' + data.acronym + '</p>';
                    },
                    "data": 'source',
                    "orderable": false,
                    "searchable": false
                },
                {
                    'render': function renderSourceAcronymColumn(data, type, row) {
                        return '<p style="font-size: small">' + data.name + '</p>';
                    },
                    "data": 'language',
                    "orderable": false,
                    "searchable": false
                },
                {
                    'render': function renderSourceUrlColumn(data, type, row) {
                        return '<a class="btn btn-success btn-sm" role="button" href="#" id="' + data + '" onclick="selectMediaItem(this.id);">Select<\/a>';
                    },
                    "data": 'sourceUrl',
                    "orderable": false,
                    "searchable": false
                }
            ],
            "createdRow": function (row, data, index) {
                $(row).attr('id', data.id);
            },
            "initComplete": function (settings, data) {
                console.log('dataTables has finished initialization.');
            }
        });

        $('#mediaItems-modal').on('shown.bs.modal', function () {
            $('#mediaItems-table').dataTable().fnDraw();
        });

        $('#mediaItems-table tbody').on('click', 'td.media-details', function () {

            var tr = $(this).closest('tr');
            var row = table.row(tr);
            
            if (row.child.isShown()) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');
                $('.detail-expando' + row.data().id + '').removeClass('fa-minus-circle subtract-sign').addClass('fa-plus-circle plus-sign');
            }
            else {
                // Open this row
                row.child(format(row.data())).show();
                tr.addClass('shown');
                $('.detail-expando' + row.data().id + '').removeClass('fa-plus-circle plus-sign').addClass('fa-minus-circle subtract-sign');
            }
        });
    });
</script>