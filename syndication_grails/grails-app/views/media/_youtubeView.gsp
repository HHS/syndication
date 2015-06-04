<g:if test="${thumbnailGeneration == true || previewGeneration == true}">
    <g:set var="width" value="${thumbnailGeneration ? 250 : 1024}"/>
    <g:set var="height" value="${thumbnailGeneration ? 188 : 768}"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Youtube Thumbnail</title>
        <meta http-equiv="content-type" content="text/html;charset=utf-8" />
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
        <div class="preview-container"><img id="previewImage" src='${thumbnail}' alt='${thumbnail}'/></div>
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
    ${player.encodeAsRaw()}
</g:else>

