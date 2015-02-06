
<!--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-->
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main"/>
<title>Tag Cloud Administration</title>
<style type="text/css" media="screen">
#status {
    background-color: #eee;
    border: .2em solid #fff;
    margin: 2em 2em 1em;
    padding: 1em;
    width: 12em;
    float: left;
    -moz-box-shadow: 0px 0px 1.25em #ccc;
    -webkit-box-shadow: 0px 0px 1.25em #ccc;
    box-shadow: 0px 0px 1.25em #ccc;
    -moz-border-radius: 0.6em;
    -webkit-border-radius: 0.6em;
    border-radius: 0.6em;
}

.ie6 #status {
    display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
}

#status ul {
    font-size: 0.9em;
    list-style-type: none;
    margin-bottom: 0.6em;
    padding: 0;
}

#status li {
    line-height: 1.3;
}

#status h1 {
    text-transform: uppercase;
    font-size: 1.1em;
    margin: 0 0 0.3em;
}

#page-body {
    margin: 2em 1em 1.25em 18em;
}

h2 {
    margin-top: 1em;
    margin-bottom: 0.3em;
    font-size: 1em;
}

p {
    line-height: 1.5;
    margin: 0.25em 0;
}

#controller-list ul {
    list-style-position: inside;
}

#controller-list li {
    line-height: 1.3;
    list-style-position: inside;
    margin: 0.25em 0;
}
/*css for tabs */
div.tabbed-menu {
    font-family: "Helvetica";
    font-size: 15px;
    width: 265px;
}

ul.tabs {
    text-align: center;
    list-style: none;
    position: relative;
    margin: 0;
    padding: 0;
    line-height: 26px;
    color: #3b4143;
    /*border-bottom: 1px solid #DDDDDD;*/
}

ul.tabs li {
    margin-bottom: -1px;
    padding: 5px 30px 5px 30px;
/*
    border: 1px solid #c6dcdd;
*/
    color: #b3c4cc;
    display: inline-block;
    /*border-top-left-radius: 6px;*/
/*
    border-top-right-radius: 6px;
*/
}

ul.tabs li.selected {
    background: #255b17; /* #b3c4cc; */
    border-bottom-color: #255b17;
}

ul.tabs li:hover {
    color: #255b17;
    cursor: pointer;
    background: transparent;
}

@media screen and (max-width: 480px) {
    #status {
        display: none;
    }

    #page-body {
        margin: 0 1em 1em;
    }

    #page-body h1 {
        margin-top: 0;
    }
}
</style>
<g:javascript library="application"/>
<g:javascript library="jquery"/>
<g:javascript library="tokenInput"/>
<r:require module="jqvalidator"/>
<g:javascript>
            jQuery(document).ready(function () {
                jQuery("#tag-token-input").tokenInput("${request.contextPath + '/tag/tokenListForMerge'}", {
                    theme: "facebook",
                    preventDuplicates: true,
                    tokenLimit:5
                });
                jQuery("#new-tag").tokenInput("${request.contextPath + '/tag/tokenList'}", {
                    theme: "facebook",
                    preventDuplicates: true,
                    tokenLimit:1
                });
                jQuery("#splittingTag").tokenInput("${request.contextPath + '/tag/tokenListForMerge'}", {
                    theme: "facebook",
                    preventDuplicates: true,
                    tokenLimit:1
                });
                jQuery("#newTagList").tokenInput("${request.contextPath + '/tag/tokenList'}", {
                    theme: "facebook",
                    preventDuplicates: true,
                    tokenLimit:5
                });

               jQuery("#split").submit(function(e){
                    //e.preventDefault()
                    var error=0;
                    var name = $('#splittingTag').val();
                    var ntgl = $('#newTagList').val();
                    if(name == '' ){
                        error = 1;
                    }
                    if(ntgl == '' ){
                        error = 1;
                    }
                    if(error==1){
                        alert("All fields are required");
                        return false;
                    }else{
                        return true;
                    }
                });
                jQuery("#mmerge").click(function(e){
                    //e.preventDefault()
                    var error=0;
                    var ti = $('#tag-token-input').val();

                    var ntg = $('#new-tag').val();
                    if(ti == '' ){
                        error = 1;
                    }
                    if(ntg == '' ){
                        error = 1;
                    }
                    if(error==1){
                        alert("All fields are required");
                        return false;
                    }else{
                        return true;
                    }
                });

//    for tabs
            $('.tabs li').click(function() {

                if(!$(this).hasClass('selected')) {
                $('.tabs li').removeClass('selected');
                $(this).addClass('selected');
                }

                var selectionId = $(this).attr('id');
                $('.page').css('display', 'none');
                $('.page#'+selectionId).css('display', 'block');

                $('.content').fadeOut('slow', function() {
                $('.content').fadeIn('slow');
                });
            });

            %{--jQuery("#splitForm").validate(--}%
            %{--{--}%
            %{--ignore: "",--}%
            %{--rules: {--}%
            %{--splittingTag: {--}%
            %{--required: true--}%
            %{--}--}%
            %{--}--}%
            %{--});--}%

});
</g:javascript>

</head>

<body>
<a href="#list-tag" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                          default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <ul class="tabs">
            <li id="page1" class="selected"><h4>Merge Tags</h4></li>
            <li id="page2"><h4>Split Tag</h4></li>
        </ul>

    </ul>
</div>

<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>

<div class="tabbed-menu">
    <div class="content">
        <div class="page" id="page1" style="display:block">
            <div id="merge">
                <g:render template="mergeform"/>
            </div>
        </div>

        <div class="page" id="page2" style="display:none">
            <div id="split">
                <g:render template="splitForm"/>
            </div>
        </div>
       </div>
</div>
</div>
<script>

</script>

</body>
</html>
