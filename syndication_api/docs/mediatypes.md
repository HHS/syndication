##MediaTypes

Media Items are broken up into Types depending on the content that they contain. 

Collection - A Media Item that is made up of multiple other MediaItems but not other collections.

Audio - an audio file(Not currently in use)

Html - A basic html page

Image - A basic Image

Infographic - An image with text on it

Periodical - An Html item that automatically checks for updates.

PDF - creates an Iframe to a pdf

Tweet - Twitter posts

Videos - A video

Widget - Not currently in use

###GET/JSON example
The MediaType resource offers one request that lists all of the different mediaTypes

####List all MediaTypes
```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/mediaTypes.json
```

Json Response

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 10,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/mediaTypes.json?offset=0&max=10&sort=name&format=json",
          "max": 10,
          "nextUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/mediaTypes.json?offset=10&max=10&sort=name&format=json",
          "offset": 0,
          "pageNum": 1,
          "previousUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/mediaTypes.json?offset=0&max=10&sort=name&format=json",
          "sort": "name",
          "total": 1,
          "totalPages": 1
        },
        "status": 200
      },
      "results": [
        {
          "name": "Audio",
          "description": "An audio file."
        },
        {
          "name": "Collection",
          "description": "A media item that is comprised of other media items."
        },
        {
          "name": "Html",
          "description": "HTML data with syndication markup."
        },
        {
          "name": "Image",
          "description": "A jpg or png format image."
        },
        {
          "name": "Infographic",
          "description": "An image in jpg or png format that contains textual, graphical, or otherwise structured data in visual form."
        },
        {
          "name": "PDF",
          "description": "syndication.mediaType.PDF.description"
        },
        {
          "name": "Periodical",
          "description": "A piece of HTML content which has ever-changing subject matter that is updated on a regular schedule."
        },
        {
          "name": "Tweet",
          "description": "A social media twitter post (a tweet)."
        },
        {
          "name": "Video",
          "description": "A youtube video."
        },
        {
          "name": "Widget",
          "description": "A combination of html, javascript, and styles which combined form a self contained interactive widget."
        }
      ]
    }
```

You can use the name of the type to make a search for Media Items by their Type

Find the most recently added Infographic Media Item

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=1&order=dateContentPublished&mediaTypes=infographic
```

Media Item json response

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 1,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media.json?offset=0&max=1&sort=id&mediaTypes=infographic&format=json&active=true",
          "max": 1,
          "nextUrl": null,
          "offset": 0,
          "pageNum": 1,
          "previousUrl": null,
          "sort": "id",
          "total": 28,
          "totalPages": 28
        },
        "status": 200
      },
      "results": [
        {
          "mediaType": "Infographic",
          "id": 2859,
          "name": "Get the Facts on Addiction",
          "description": "3 out of 4 teen smokers who think they will stop smoking in 5 years don't.",
          "sourceUrl": "http://therealcost.betobaccofree.hhs.gov/sites/default/files/images/infographic/did-you-know-few-years.jpg",
          "targetUrl": "http://therealcost.betobaccofree.hhs.gov/costs/addiction/index.html",
          "width": 300,
          "height": 330,
          "dateContentAuthored": null,
          "dateContentUpdated": null,
          "dateContentPublished": null,
          "dateContentReviewed": null,
          "dateSyndicationVisible": "2014-12-11T15:39:00Z",
          "dateSyndicationCaptured": "2014-12-11T15:39:00Z",
          "dateSyndicationUpdated": "2015-09-29T19:12:20Z",
          "language": {
            "id": 1,
            "name": "English",
            "isoCode": "eng"
          },
          "externalGuid": null,
          "contentHash": null,
          "tags": [
            {
              "id": 5,
              "name": "facts",
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
              "id": 6,
              "name": "teens",
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
          "source": {
            "id": 3,
            "name": "U S Food and Drug Administration",
            "acronym": "FDA",
            "websiteUrl": "http://www.fda.gov",
            "contactEmail": null,
            "largeLogoUrl": null,
            "smallLogoUrl": null
          },
          "alternateImages": [],
          "campaigns": [],
          "tinyUrl": "NotMapped",
          "tinyToken": "N/A",
          "thumbnailUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/2859/thumbnail.jpg",
          "previewlUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/2859/preview.jpg",
          "attribution": "&lt;div id='hhsAttribution'&gt;Content provided and maintained by &lt;a href='http://www.fda.gov' target='_blank'&gt;Health and Human Services&lt;/a&gt; (HHS). Please see our system &lt;a href='http:syndication.hhs.gov' target='_blank'&gt;usage guidelines and disclaimer&lt;/a&gt;.&lt;/div&gt;",
          "extendedAttributes": {
            "readMoreLink": "http://therealcost.betobaccofree.hhs.gov/costs/addiction/index.html"
          },
          "customThumbnailUrl": null,
          "customPreviewUrl": null
        }
      ]
    }
```

###Jquery JSONP example
In this example we will display the list of Media Types in a select box and display 10 mediaItem name and descriptions.

First we need to get all of the MediaTypes and populate the select tag

```html
    <div class="row">
      <div class="col-md-6">
        <select id="mediaTypes">

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
              url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/mediaTypes.json",
              async: false,
              jsonpCallback: 'jsonCallback',
              contentType: "application/json",
              dataType: 'jsonp',
              success: function(json) {
                var mediaTypeSelect = document.getElementById("mediaTypes");
                var mediaTypes = json.results
  
                for(var index=0;index<mediaTypes.length;index++) {
                  mediaTypeSelect.innerHTML += "<option value='" +mediaTypes[index].name+"'>"+mediaTypes[index].name+"</option>"
                }
                //listMediaItems(mediaTypes[0].name);
              },
              error: function(e) {
                 console.log(e.message);
              }
          });  
    </script>
```

Now we have a populated select tag of all the Media Types. Now lets create a jquery Listener to display five Media Items
of the Type that was selected.

```javascript
    function listMediaItems(name) {
      $.ajax({
         type: 'GET',
          url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=5&order=dateContentPublished&mediaTypes="+name,
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
    
    $("#mediaTypes").on("change",function(){
        listMediaItems(this.value);
      });
```

Now any time someone changes the selected Media Type the list of mediaItems will be updated accordingly.

####Entire example file

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
              <select id="mediaTypes">

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

          function listMediaItems(name) {
            $.ajax({
               type: 'GET',
                url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=5&order=dateContentPublished&mediaTypes="+name,
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
                url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/mediaTypes.json",
                async: false,
                jsonpCallback: 'jsonCallback',
                contentType: "application/json",
                dataType: 'jsonp',
                success: function(json) {
                  var mediaTypeSelect = document.getElementById("mediaTypes");
                  var mediaTypes = json.results

                  for(var index=0;index<mediaTypes.length;index++) {
                    mediaTypeSelect.innerHTML += "<option value='" +mediaTypes[index].name+"'>"+mediaTypes[index].name+"</option>"
                  }
                  listMediaItems(mediaTypes[0].name);
                },
                error: function(e) {
                   console.log(e.message);
                }
            });

            $("#mediaTypes").on("change",function(){
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
<select id="mediaTypes2">

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

function listMediaItems(name) {
$("#spinner").show();   
$.ajax({
type: 'GET',
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=5&order=dateContentPublished&mediaTypes="+name,
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
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/mediaTypes.json",
async: false,
jsonpCallback: 'jsonCallback',
contentType: "application/json",
dataType: 'jsonp',
success: function(json) {
var mediaTypeSelect = document.getElementById("mediaTypes2");
var mediaTypes = json.results

for(var index=0;index<mediaTypes.length;index++) {
mediaTypeSelect.innerHTML += "<option value='" +mediaTypes[index].name+"'>"+mediaTypes[index].name+"</option>"
}
listMediaItems(mediaTypes[0].name);
},
error: function(e) {
console.log(e.message);
}
});

$("#mediaTypes2").on("change",function(){
listMediaItems(this.value);
});

});
</script>