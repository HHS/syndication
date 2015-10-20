<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 8/28/15
  Time: 1:22 PM
--%>
<html>
<head>
    <title></title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">

    <style>

    </style>

    <script>
        $(document).ready(function(){
            $(".clickable_thumbnail").click(function(event){
                console.log($(this).data('image_url'));
                $('#modal_body').html("<img src='" + $(this).data('image_url') + "'/>")
            });

            $('[data-toggle="tooltip"]').tooltip();
        });
    </script>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6">
            <g:each in="${0..<Math.ceil(statuses.size()/2)}" var="i">
                <g:set var="status" value="${statuses[i]}"/>
                <g:set var="percent" value="${status.value.remaining / status.value.limit * 100 as int}"/>
                <g:set var="color" value="${synd.percentColor(percent:percent)}"/>
                ${statuses[i].key} <span data-toggle="tooltip" title="Time until rate reset" class="pull-right"><i class="fa fa-clock-o"></i> ${Math.round(statuses[i].value.getSecondsUntilReset()/60)} minutes</span>
                <div class="progress">
                    <div class="progress-bar progress-bar-${color}" role="progressbar" aria-valuenow="${status.value.remaining}" aria-valuemin="0" aria-valuemax="${status.value.limit}" style="width: ${percent}%; min-width: 2em;">
                        ${percent}% (${status.value.remaining} / ${status.value.limit})
                    </div>
                </div>
            </g:each>
        </div>
        <div class="col-sm-6">
            <g:each in="${((int)Math.ceil(statuses.size()/2))..<statuses.size()}" var="i">
                <g:set var="status" value="${statuses[i]}"/>
                <g:set var="percent" value="${status.value.remaining / status.value.limit * 100 as int}"/>
                <g:set var="color" value="${synd.percentColor(percent:percent)}"/>
                ${statuses[i].key} <span data-toggle="tooltip" title="Time until rate reset" class="pull-right"><i class="fa fa-clock-o"></i> ${Math.round(statuses[i].value.getSecondsUntilReset()/60)} minutes</span>
                <div class="progress">
                    <div class="progress-bar progress-bar-${color}" role="progressbar" aria-valuenow="${status.value.remaining}" aria-valuemin="0" aria-valuemax="${status.value.limit}" style="width: ${percent}%; min-width: 2em;">
                        ${percent}% (${status.value.remaining} / ${status.value.limit})
                    </div>
                </div>
            </g:each>
        </div>
    </div>
</div>
</body>
</html>