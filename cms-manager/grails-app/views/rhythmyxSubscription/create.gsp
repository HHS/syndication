<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<html>
<head>
    <g:render template='../mediaItems/datatables_js'/>
    <script>
        $(document).ready(function () {
            updateWorkflow();
            $(document).on('change', '#rhythmyxSubscriber',function(){
                updateWorkflow();
            });
            
            function updateWorkflow(){
                setTimeout(function () {
                    $instance = $('#rhythmyxSubscriber').val();
                    $.ajax({ // create an AJAX call...
                        data: {instance:$instance}, // get the form data
                        type: 'POST', // GET or POST
                        url: '${g.createLink(controller: 'rhythmyxSubscription', action: 'getDefaultWorkflow')}', // the file to call
                        success: function (response) { // on success..
                            $('#defaultWorkflow').html(response); // update the DIV
                        }
                    });
                }, 300);
            }

            activate('subscriptions-nav');

            $(function(){
                $('#rhythmyxWorkflow.autoPublish').attr('checked', 'checked');
            });
        });
    </script>
    <meta name='layout' content='main'/>
</head>
<body>
<g:render template="../templates/header"/>
<g:render template="../templates/create" model="[
        instance: rhythmyxSubscriptionInstance
]"/>
</body>
