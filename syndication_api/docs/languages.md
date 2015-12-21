##Languages
Media Items may be in a different language. The language resource can be used to find all of the available
languages and then you can use that info to look up media items by a particular language.

###GET/JSON example

####Get the list of all possible languages.
```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/languages.json?max=100
```

The response json will look like the following with the languages listed inside the "results" key

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 5,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/languages.json?offset=0&max=100&sort=id&format=json",
          "max": 100,
          "nextUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/languages.json?offset=100&max=100&sort=id&format=json",
          "offset": 0,
          "pageNum": 1,
          "previousUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/languages.json?offset=0&max=100&sort=id&format=json",
          "sort": "id",
          "total": 5,
          "totalPages": 1
        },
        "status": 200
      },
      "results": [
        {
          "id": 1,
          "name": "English",
          "isoCode": "eng"
        },
        {
          "id": 2,
          "name": "Spanish",
          "isoCode": "spa"
        },
        {
          "id": 3,
          "name": "French",
          "isoCode": "fre"
        },
        {
          "id": 82,
          "name": "Chinese",
          "isoCode": "chi"
        },
        {
          "id": 458,
          "name": "Vietnamese",
          "isoCode": "vie"
        }
      ]
    }
```

From here you can pick out the id or name of the language that you want to use to look up Media Items by.

Lets pick Spanish which has an id of value 2 and look up two Media Items in alphabetical order that are Spanish.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?max=2&sort=name&languageId=2
```
The response json will look like the following with the two Media Items being contained in the "results" key

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 2,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media.json?offset=0&max=2&sort=name&languageId=2&format=json&active=true",
          "max": 2,
          "nextUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media.json?offset=2&max=2&sort=name&languageId=2&format=json&active=true",
          "offset": 0,
          "pageNum": 1,
          "previousUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media.json?offset=0&max=2&sort=name&languageId=2&format=json&active=true",
          "sort": "name",
          "total": 397,
          "totalPages": 199
        },
        "status": 200
      },
      "results": [
        {
          "mediaType": "Html",
          "id": 3545,
          "name": "¿Qué es el acné? (Esenciales: hojas informativas de fácil lectura)",
          "description": "El acné es una enfermedad que afecta las glándulas sebáceas. El acné es la enfermedad de la piel más común. Las personas de todas las razas y de todas las edades pueden tener acné.",
          "sourceUrl": "http://www.niams.nih.gov/Portal_en_espanol/Informacion_de_salud/Acne/default.asp",
          "targetUrl": null,
          "dateContentAuthored": null,
          "dateContentUpdated": null,
          "dateContentPublished": null,
          "dateContentReviewed": null,
          "dateSyndicationVisible": "2015-08-12T19:04:00Z",
          "dateSyndicationCaptured": "2015-08-12T19:04:00Z",
          "dateSyndicationUpdated": "2015-08-12T19:04:00Z",
          "language": {
            "id": 2,
            "name": "Spanish",
            "isoCode": "spa"
          },
          "externalGuid": null,
          "contentHash": "1321a2e0e5a10ddf97ac7b01cdeedd85",
          "source": {
            "id": 14,
            "name": "National Institute of Arthritis and Musculoskeletal and Skin Diseases",
            "acronym": "NIAMS",
            "websiteUrl": "http://www.niams.nih.gov",
            "contactEmail": "bettends@mail.nih.gov",
            "largeLogoUrl": null,
            "smallLogoUrl": null
          },
          "campaigns": [],
          "tags": [
            {
              "id": 2452,
              "name": "piel",
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
          "thumbnailUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3545/thumbnail.jpg",
          "previewlUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3545/preview.jpg",
          "alternateImages": [],
          "attribution": "&lt;div id='hhsAttribution'&gt;Content provided and maintained by &lt;a href='http://www.niams.nih.gov' target='_blank'&gt;Health and Human Services&lt;/a&gt; (HHS). Please see our system &lt;a href='http:syndication.hhs.gov' target='_blank'&gt;usage guidelines and disclaimer&lt;/a&gt;.&lt;/div&gt;",
          "extendedAttributes": {},
          "customThumbnailUrl": null,
          "customPreviewUrl": null
        },
        {
          "mediaType": "Html",
          "id": 3546,
          "name": "¿Qué es la fibromialgia? (Esenciales: hojas informativas de fácil lectura)",
          "description": "La fibromialgia es un trastorno que causa dolores musculares y fatiga (cansancio). Las personas con fibromialgia tienen “puntos hipersensibles” en el cuerpo. Las personas que padecen de fibromialgia pueden también tener otros síntomas.",
          "sourceUrl": "http://www.niams.nih.gov/Portal_en_espanol/Informacion_de_salud/Fibromialgia/default.asp",
          "targetUrl": null,
          "dateContentAuthored": null,
          "dateContentUpdated": null,
          "dateContentPublished": null,
          "dateContentReviewed": null,
          "dateSyndicationVisible": "2015-08-12T19:20:00Z",
          "dateSyndicationCaptured": "2015-08-12T19:20:00Z",
          "dateSyndicationUpdated": "2015-08-12T19:20:00Z",
          "language": {
            "id": 2,
            "name": "Spanish",
            "isoCode": "spa"
          },
          "externalGuid": null,
          "contentHash": "637a6ed29cc4550130901b9f1e27999f",
          "source": {
            "id": 14,
            "name": "National Institute of Arthritis and Musculoskeletal and Skin Diseases",
            "acronym": "NIAMS",
            "websiteUrl": "http://www.niams.nih.gov",
            "contactEmail": "bettends@mail.nih.gov",
            "largeLogoUrl": null,
            "smallLogoUrl": null
          },
          "campaigns": [],
          "tags": [
            {
              "id": 2453,
              "name": "dolores",
              "language": {
                "isActive": true,
                "id": 2,
                "name": "Spanish",
                "isoCode": "spa"
              },
              "type": {
                "id": 1,
                "name": "General"
              }
            },
            {
              "id": 2454,
              "name": "fatiga",
              "language": {
                "isActive": true,
                "id": 2,
                "name": "Spanish",
                "isoCode": "spa"
              },
              "type": {
                "id": 1,
                "name": "General"
              }
            },
            {
              "id": 2455,
              "name": "fibromialgia",
              "language": {
                "isActive": true,
                "id": 2,
                "name": "Spanish",
                "isoCode": "spa"
              },
              "type": {
                "id": 1,
                "name": "General"
              }
            }
          ],
          "tinyUrl": "NotMapped",
          "tinyToken": "N/A",
          "thumbnailUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3546/thumbnail.jpg",
          "previewlUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3546/preview.jpg",
          "alternateImages": [],
          "attribution": "&lt;div id='hhsAttribution'&gt;Content provided and maintained by &lt;a href='http://www.niams.nih.gov' target='_blank'&gt;Health and Human Services&lt;/a&gt; (HHS). Please see our system &lt;a href='http:syndication.hhs.gov' target='_blank'&gt;usage guidelines and disclaimer&lt;/a&gt;.&lt;/div&gt;",
          "extendedAttributes": {},
          "customThumbnailUrl": null,
          "customPreviewUrl": null
        }
      ]
    }
```

###Jquery JSONP example
We can use this Resource if we want to create a search that allows a user to sort Media Items based on their
language. Lets find a couple of Media Items with a specific language and display their thumbnail image.

####First get all of the possible languages to populate an html select tag. 
```html
      <body>
          <div class="row">
            <div class="col-md-6">
              <select id="languages">
  
              </select>
            </div>
          </div>
  
          <script>
          $(document).ready(function(){
            $.ajax({
                 type: 'GET',
                  url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/languages.json?max=100",
                  async: false,
                  jsonpCallback: 'jsonCallback',
                  contentType: "application/json",
                  dataType: 'jsonp',
                  success: function(json) {
                    var languageSelect = document.getElementById("languages");
                    var languages = json.results
  
                    for(var index=0;index<languages.length;index++) {
                      languageSelect.innerHTML += "<option value='" +languages[index].id+"'>"+languages[index].name+"</option>"
                    }
                    listMediaItems(languages[0].id);
                  },
                  error: function(e) {
                     console.log(e.message);
                  }
              });
  
          });
          </script>
  
      </body>
```
Now we have a populated select tag of all of the languags in alphabetical order. Now lets create a jquery Listener to 
display 5 Media Item names and their thumbnails.

Display MediaItem thumbnail and name

we need to add a list tag

```html
          <div class="row">
            <div class="col-md-6">
              <ol id="mediaItems" class="list-group">
  
              </ol>
            </div>
          </div>
```

Then add the listener that gets the mediaItems based on the languages selected.

```javascript  
            function listMediaItems(id) {
              $.ajax({
                     type: 'GET',
                      url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?sort=name&max=5&languageId="+id,
                      async: false,
                      jsonpCallback: 'jsonCallback',
                      contentType: "application/json",
                      dataType: 'jsonp',
                      success: function(json) {
                        var mediaItemsListTag = document.getElementById("mediaItems")
                        var mediaItems = json.results
                        mediaItemsListTag.innerHTML = ""
                        for(var index=0;index<mediaItems.length;index++) {
                          mediaItemsListTag.innerHTML += "<li class='list-group-item'><img src='"+mediaItems[index].thumbnailUrl+"' />"+mediaItems[index].name +"</li>"
                        }
                      },
                      error: function(e) {
                         console.log(e.message);
                      }
                  });
            }
                
            $("#languages").on("change",function(){
              listMediaItems(this.value);
            });
  
```

Now any time the language is changed 5 Media Items from that language will be displayed with its name and thumbnail.

####Full html file
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
                <select id="languages">
  
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
                  url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?sort=name&max=5&languageId="+id,
                  async: false,
                  jsonpCallback: 'jsonCallback',
                  contentType: "application/json",
                  dataType: 'jsonp',
                  success: function(json) {
                    var mediaItemsListTag = document.getElementById("mediaItems")
                    var mediaItems = json.results
                    mediaItemsListTag.innerHTML = ""
                    for(var index=0;index<mediaItems.length;index++) {
                      mediaItemsListTag.innerHTML += "<li class='list-group-item'><img src='"+mediaItems[index].thumbnailUrl+"' />"+mediaItems[index].name +"</li>"
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
                  url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/languages.json?max=100",
                  async: false,
                  jsonpCallback: 'jsonCallback',
                  contentType: "application/json",
                  dataType: 'jsonp',
                  success: function(json) {
                    var languageSelect = document.getElementById("languages");
                    var languages = json.results
  
                    for(var index=0;index<languages.length;index++) {
                      languageSelect.innerHTML += "<option value='" +languages[index].id+"'>"+languages[index].name+"</option>"
                    }
                    listMediaItems(languages[0].id);
                  },
                  error: function(e) {
                     console.log(e.message);
                  }
              });
  
              $("#languages").on("change",function(){
                listMediaItems(this.value);
              });
  
            });
            </script>
  
        </body>
    </html>

```

####HTML example in action
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<div class="row">
<div class="col-md-6">
<select id="languages2">

</select>
</div>
</div>
<div class="row">
<div class="col-md-6">
<div id="spinner" hidden style="width:50px" class="center-block"><img src="https://digitalmedia.hhs.gov/assets/spinner.gif"/></div>
<ol id="mediaItems2" class="list-group">

</ol>
</div>
</div>

<script>

function listMediaItems(id) {
$("#spinner").show();
$.ajax({
type: 'GET',
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/media.json?sort=name&max=5&languageId="+id,
async: false,
jsonpCallback: 'jsonCallback',
contentType: "application/json",
dataType: 'jsonp',
success: function(json) {
$("#spinner").hide("fast", function(){
var mediaItemsListTag = document.getElementById("mediaItems2")
var mediaItems = json.results
mediaItemsListTag.innerHTML = ""
for(var index=0;index<mediaItems.length;index++) {
mediaItemsListTag.innerHTML += "<li class='list-group-item'><img src='"+mediaItems[index].thumbnailUrl+"' />"+mediaItems[index].name +"</li>"
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
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/languages.json?max=100",
async: false,
jsonpCallback: 'jsonCallback',
contentType: "application/json",
dataType: 'jsonp',
success: function(json) {
var languageSelect = document.getElementById("languages2");
var languages = json.results

for(var index=0;index<languages.length;index++) {
languageSelect.innerHTML += "<option value='" +languages[index].id+"'>"+languages[index].name+"</option>"
}
listMediaItems(languages[0].id);
},
error: function(e) {
console.log(e.message);
}
});

$("#languages2").on("change",function(){
listMediaItems(this.value);
});

});
</script>
