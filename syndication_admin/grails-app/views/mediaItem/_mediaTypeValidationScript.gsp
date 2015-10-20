<script>
    $("form").submit(function(e){
        e.preventDefault();
        var self = this;
        var sourceUrl = document.getElementById("sourceUrl").value
        var data = {sourceUrl:sourceUrl, initController:'${params.controller}'}

        if(document.getElementById("imageFormat")){
            data['imageFormat'] = document.getElementById("imageFormat").value
        }

        $("#creationSpinner").toggle(true);

        $.ajax({
            data:data,
            url:'${g.createLink(controller: 'mediaItem', action: 'checkUrlContentType')}',
            success:function(response){
                if(response === "true"){
                    self.submit()
                } else {
                    var dialog = $("<p>Specified SourceURL doesn't appear to contain the type of data you specified. Verify the link and image format (if applicable). <br/><br/><strong>Continue Anyway?</strong></p>").dialog({
                        buttons: {
                            "No":  {
                                click:function() {dialog.dialog('close')},
                                id:"noConfirm",
                                text:"No"
                            },
                            "Yes": {
                                click:function() {self.submit();},
                                id:"yesConfirm",
                                text:"Yes"
                            }
                        }
                    });
                }
                $("#creationSpinner").toggle(false)
            }
        });
    })
</script>