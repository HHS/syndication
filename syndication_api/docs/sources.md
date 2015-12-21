##Sources
Sources are the Government Agency that a Media Item belongs to.


###GET/JSON example
Lets find the most recently added item from the NIH

####Get all agencies

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/sources.json
```

Json response of one agency

```javascript
        {...
          "results":[  
            {
              "id": 4,
              "name": "National Institutes of Health",
              "acronym": "NIH",
              "websiteUrl": "http://www.nih.gov",
              "contactEmail": null,
              "largeLogoUrl": null,
              "smallLogoUrl": null
            }
          ]
        ...}
```

Use the NIH id or name to find the most recently added Media Item published by them

```html
    https://api.digitalmedia.hhs.gov:443/api/vd2/resources/media.json?max=1&sort=dateContentPublished&sourceId=4
```

Json response of the latest media item from NIH

```javascript

    {...
    "results": [
        {
          "mediaType": "Html",
          "id": 3341,
          "name": "NIH Grants Process Overview",
          "description": "The Grants Process Overview below provides an overview of the steps required for an application to proceed from application planning and submission through award and close out.",
          "sourceUrl": "http://grants.nih.gov/grants/grants_process_sync.htm",
          "targetUrl": null,
          "dateContentAuthored": null,
          "dateContentUpdated": null,
          "dateContentPublished": null,
          "dateContentReviewed": null,
          "dateSyndicationVisible": "2015-05-05T14:20:00Z",
          "dateSyndicationCaptured": "2015-05-05T14:20:00Z",
          "dateSyndicationUpdated": "2015-05-05T17:54:02Z",
          "language": {
            "id": 1,
            "name": "English",
            "isoCode": "eng"
          },
          "externalGuid": null,
          "contentHash": "508b327b0fd729f44dd507dbaf359f1c",
          "source": {
            "id": 4,
            "name": "National Institutes of Health",
            "acronym": "NIH",
            "websiteUrl": "http://www.nih.gov",
            "contactEmail": null,
            "largeLogoUrl": null,
            "smallLogoUrl": null
          },
          "campaigns": [
            {
              "id": 3,
              "name": "OER"
            }
          ],
          "tags": [
            {
              "id": 1153,
              "name": "nih",
              "language": {
                "isActive": true,
                "id": 1,
                "name": "English",
                "isoCode": "eng"
              },
              "type": {
                "id": 1,
                "name": "General"
              }
            },
            {
              "id": 1154,
              "name": "grants",
              "language": {
                "isActive": true,
                "id": 1,
                "name": "English",
                "isoCode": "eng"
              },
              "type": {
                "id": 1,
                "name": "General"
              }
            },
            {
              "id": 1742,
              "name": "process",
              "language": {
                "isActive": true,
                "id": 1,
                "name": "English",
                "isoCode": "eng"
              },
              "type": {
                "id": 1,
                "name": "General"
              }
            }
          ],
          "tinyUrl": "NotMapped",
          "tinyToken": "N/A",
          "thumbnailUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3341/thumbnail.jpg",
          "previewlUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3341/preview.jpg",
          "alternateImages": [],
          "attribution": "&lt;div id='hhsAttribution'&gt;Content provided and maintained by &lt;a href='http://www.nih.gov' target='_blank'&gt;Health and Human Services&lt;/a&gt; (HHS). Please see our system &lt;a href='http:syndication.hhs.gov' target='_blank'&gt;usage guidelines and disclaimer&lt;/a&gt;.&lt;/div&gt;",
          "extendedAttributes": {},
          "customThumbnailUrl": null,
          "customPreviewUrl": null
        }
      ]
    }
```

###Jquery JSONP example

In this example lets allow a user to select a source and then display the most recent item that the agency published.

Get all of the sources and populate a select tag.

```html
      <div class="row">
        <div class="col-md-6">
          <select id="sources">

          </select>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <ol id="mediaItems" class="list-group">

          </ol>
        </div>
      </div>
      
      <script>
      $(document).ready(function(){
          $.ajax({
             type: 'GET',
              url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/sources.json",
              async: false,
              jsonpCallback: 'jsonCallback',
              contentType: "application/json",
              dataType: 'jsonp',
              success: function(json) {
                var sourceSelect = document.getElementById("sources");
                var sources = json.results

                for(var index=0;index<sources.length;index++) {
                  sourceSelect.innerHTML += "<option value='" +sources[index].id+"'>"+sources[index].name+"</option>"
                }
                //listMediaItems(sources[0].id);
              },
              error: function(e) {
                 console.log(e.message);
              }
          });
      </script>
```

Now we have a populated select tag of all of all the Sources. Now lets create a jquery Listener to 
display the last Media Item that was published with the selected Source.

```html
    function listMediaItems(id) {
        $.ajax({
           type: 'GET',
            url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=1&sort=dateContentPublished&sourceId="+id,
            async: false,
            jsonpCallback: 'jsonCallback',
            contentType: "application/json",
            dataType: 'jsonp',
            success: function(json) {
              var mediaItemsListTag = document.getElementById("mediaItems")
              var mediaItems = json.results
              mediaItemsListTag.innerHTML = ""
              for(var index=0;index<mediaItems.length;index++) {
                mediaItemsListTag.innerHTML += "<li class='list-group-item'><a href='"+mediaItems[index].sourceUrl+"'><h2>"+mediaItems[index].name+"</h2></a>"+mediaItems[index].description +"</li>"
              }
            },
            error: function(e) {
               console.log(e.message);
            }
        });
      }

    $("#sources").on("change",function(){
      listMediaItems(this.value);
    });
```

Now any time someone changes the selected source the most recent Media Item will be shown with its title, description
and the title being a link the the MediaItems sourceUrl.

####Entire example html file

```html

    <html>
      <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <!-- Latest compiled and minified bootstrap CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==" crossorigin="anonymous">
        <!-- Latest compiled and minified bootstrap JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script>
      </head>
      <body>
          <div class="row">
            <div class="col-md-6">
              <select id="sources">

              </select>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6">
              <ol id="mediaItems" class="list-group">

              </ol>
            </div>
          </div>

          <script>

          function listMediaItems(id) {
            $.ajax({
               type: 'GET',
                url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=1&sort=dateContentPublished&sourceId="+id,
                async: false,
                jsonpCallback: 'jsonCallback',
                contentType: "application/json",
                dataType: 'jsonp',
                success: function(json) {
                  var mediaItemsListTag = document.getElementById("mediaItems")
                  var mediaItems = json.results
                  mediaItemsListTag.innerHTML = ""
                  for(var index=0;index<mediaItems.length;index++) {
                    mediaItemsListTag.innerHTML += "<li class='list-group-item'><a href='"+mediaItems[index].sourceUrl+"'><h2>"+mediaItems[index].name+"</h2></a>"+mediaItems[index].description +"</li>"
                  }
                },
                error: function(e) {
                   console.log(e.message);
                }
            });
          }
          $(document).ready(function(){
            $.ajax({
               type: 'GET',
                url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/sources.json",
                async: false,
                jsonpCallback: 'jsonCallback',
                contentType: "application/json",
                dataType: 'jsonp',
                success: function(json) {
                  var sourceSelect = document.getElementById("sources");
                  var sources = json.results

                  for(var index=0;index<sources.length;index++) {
                    sourceSelect.innerHTML += "<option value='" +sources[index].id+"'>"+sources[index].name+"</option>"
                  }
                  listMediaItems(sources[0].id);
                },
                error: function(e) {
                   console.log(e.message);
                }
            });

            $("#sources").on("change",function(){
              listMediaItems(this.value);
            });

          });
          </script>

      </body>
    </html>
  
```

####Html example in action

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<div class="row">
<div class="col-md-6">
<select id="sources2">

</select>
</div>
</div>
<div class="row">
<div class="col-md-6">
<div id="spinner" hidden style="width:50px" class="center-block"><img src="https://digitalmedia.hhs.gov/assets/spinner.gif"/></div>
<ol id="mediaItems" class="list-group">

</ol>
</div>
</div>

<script>

function listMediaItems(id) {
$("#spinner").show();
$.ajax({
type: 'GET',
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=1&sort=dateContentPublished&sourceId="+id,
async: false,
jsonpCallback: 'jsonCallback',
contentType: "application/json",
dataType: 'jsonp',
success: function(json) {
$("#spinner").hide("fast", function(){
var mediaItemsListTag = document.getElementById("mediaItems")
var mediaItems = json.results
mediaItemsListTag.innerHTML = ""
for(var index=0;index<mediaItems.length;index++) {
mediaItemsListTag.innerHTML += "<li class='list-group-item'><a href='"+mediaItems[index].sourceUrl+"'><h2>"+mediaItems[index].name+"</h2></a>"+mediaItems[index].description +"</li>"
}
});
},
error: function(e) {
console.log(e.message);
}
});
}
$(document).ready(function(){
$.ajax({
type: 'GET',
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/sources.json",
async: false,
jsonpCallback: 'jsonCallback',
contentType: "application/json",
dataType: 'jsonp',
success: function(json) {
var sourceSelect = document.getElementById("sources2");
var sources = json.results
console.log("here")
for(var index=0;index<sources.length;index++) {
console.log("loop")
sourceSelect.innerHTML += "<option value='" +sources[index].id+"'>"+sources[index].name+"</option>"
}
listMediaItems(sources[0].id);
},
error: function(e) {
console.log(e.message);
}
});

$("#sources2").on("change",function(){
listMediaItems(this.value);
});

});
</script>