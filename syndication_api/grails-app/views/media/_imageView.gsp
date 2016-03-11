<g:if test="${thumbnailGeneration || previewGeneration}">
    <!DOCTYPE html>
    <html>
        <head>
            <style>
                body{
                    padding:0px;
                    margin:0px;
                }
                .preview-container{
                    width:${width}px;
                    height:${height}px;
                }
                img{
                    position: absolute;
                }
            </style>
        </head>
        <body>
            <div class="preview-container"><img id="previewImage" src='${img.sourceUrl}' alt='${img.altText}'/></div>
            <script type="text/javascript">
                var img = document.getElementById('previewImage');
                img.onload = function() {
                    if(this.width > this.height){
                        this.style.height = '${height}px';
                        var margin = (this.width - ${width}) / 2;
                        this.style['margin-left'] = '-' + margin + 'px'
                    } else{
                        this.style.width = '${width}px';
                        var margin = (this.height - ${height}) / 4;
                        this.style['margin-top'] = '-' + margin + 'px'
                    }
                }
            </script>
        </body>
    </html>
</g:if>
<g:else>
    <img style="max-width: 100%" src='${img.sourceUrl}' alt='${img.altText}'/>
</g:else>