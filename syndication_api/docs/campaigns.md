##Campaigns
Publishers of syndicated content will place mediaItems in a campaign to group them for a specific goal. 

###GET/JSON example
We can use the campaigns resource to get the list of all campaigns then look up all of the media items associated 
with that campaign.

####First get all the campaigns
```html
 https://stage-api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns.json?sort=name
```

This API request will return a json response with a list of campaigns with the following format

```javascript
       {
           "callback": null,
           "meta": {
           "messages": [ ],
           "pagination": {
           "count": 4,
           "currentUrl": "https://stage-api.digitalmedia.hhs.gov/api/v2/resources/campaigns.json?offset=0&max=20&sort=id&format=json",
           "max": 20,
           "nextUrl": "https://stage-api.digitalmedia.hhs.gov/api/v2/resources/campaigns.json?offset=20&max=20&sort=id&format=json",
           "offset": 0,
           "pageNum": 1,
           "previousUrl": "https://stage-api.digitalmedia.hhs.gov/api/v2/resources/campaigns.json?offset=0&max=20&sort=id&format=json",
           "sort": "id",
           "total": 4,
           "totalPages": 1
           },
           "status": 200
           },
           "results": [
               {
                 "id": 3,
                 "name": "OER",
                 "description": null,
                 "contactEmail": "david.rosen@nih.gov",
                 "startDate": "2015-05-05T12:54:00Z",
                 "endDate": null,
                 "source": {
                   "id": 4,
                   "name": "National Institutes of Health",
                   "acronym": "NIH",
                   "websiteUrl": "http://www.nih.gov",
                   "contactEmail": null,
                   "largeLogoUrl": null,
                   "smallLogoUrl": null
                 }
               }
           ]
       }
```

From here we can pick the OER campaign and find all of the Media Items associated within it.

We will use the OER id of 3 and pick a max of 1 so that we only get back one of the campaigns Media Items.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns/3/media.json?max=1
```

The API json response will return the campaign along with all of the mediaItem associated with it.

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 1,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/campaigns.json?offset=0&max=1&sort=id&id=3&format=json&maxOverride=true",
          "max": 1,
          "nextUrl": null,
          "offset": 0,
          "pageNum": 1,
          "previousUrl": null,
          "sort": "id",
          "total": 3,
          "totalPages": 3
        },
        "status": 200
      },
      "results": [
        {
          "mediaType": "Html",
          "id": 3316,
          "name": "Standard Due Dates for Competing Applications",
          "description": "NIH Grants Application Due Dates.",
          "sourceUrl": "http://grants.nih.gov/grants/funding/submissionschedule.htm",
          "targetUrl": null,
          "dateContentAuthored": null,
          "dateContentUpdated": null,
          "dateContentPublished": null,
          "dateContentReviewed": null,
          "dateSyndicationVisible": "2015-03-26T20:06:00Z",
          "dateSyndicationCaptured": "2015-03-26T20:06:00Z",
          "dateSyndicationUpdated": "2015-05-05T12:56:47Z",
          "language": {
            "id": 1,
            "name": "English",
            "isoCode": "eng"
          },
          "externalGuid": null,
          "contentHash": "0ed3379c7fba00b205a178f7da9472d5",
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
              "id": 46,
              "name": "funding",
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
            }
          ],
          "tinyUrl": "NotMapped",
          "tinyToken": "N/A",
          "thumbnailUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3316/thumbnail.jpg",
          "previewlUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/3316/preview.jpg",
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
One way to use campaigns may be to search for mediaItems that are contained in a specified campaign that was selected 
from a drop down menu. Lets take a look at this example.

####First get all the campaigns and populate a select tag
```html
  <div class="row">
     <div class="col-md-6">
       <select id="campaigns">
   
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
                  url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns.json?sort=-name",
                  async: false,
                  jsonpCallback: 'jsonCallback',
                  contentType: "application/json",
                  dataType: 'jsonp',
                  success: function(json) {
                     var campaignSelect = document.getElementById("campaigns");
                     var campaigns = json.results
                     console.log(campaigns)
                     console.log(campaigns.length)
                     for(var index=0;index<campaigns.length;index++) {
                       campaignSelect.innerHTML += "<option value='" +campaigns[index].id+"'>"+campaigns[index].name+"</option>"
                     }
                     listMediaItems(campaigns[0].id)
                  },
                  error: function(e) {
                     console.log(e.message);
                  }
              });
      });
  </script>
```
         
Now we have a populated select tag of all of the campaigns in alphabetical order. Now lets create a jquery Listener to 
display all of the MediaItems that a user selects.

Display Media Items from the campaign that was selected

```javascript
    function listMediaItems(id) {
          $.ajax({
             type: 'GET',
              url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns/"+id+"/media.json",
              async: false,
              jsonpCallback: 'jsonCallback',
              contentType: "application/json",
              dataType: 'jsonp',
              success: function(json) {
                var mediaItemsListTag = document.getElementById("mediaItems")
                var mediaItems = json.results
                mediaItemsListTag.innerHTML = ""
                for(var index=0;index<mediaItems.length;index++) {
                 mediaItemsListTag.innerHTML += "<li class='list-group-item'>"+mediaItems[index].name +"</li>"
                }
              },
              error: function(e) {
                 console.log(e.message);
              }
          });
    }
            
    $("#campaigns").on("change",function(){
        listMediaItems(this.value)
      });
```
      
Now any time someone changes the selected campaign the list of mediaItems will get updated to the ones in that 
campaign.

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
                <select id="campaigns">
    
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
                  url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns/"+id+"/media.json",
                  async: false,
                  jsonpCallback: 'jsonCallback',
                  contentType: "application/json",
                  dataType: 'jsonp',
                  success: function(json) {
                    var mediaItemsListTag = document.getElementById("mediaItems")
                    var mediaItems = json.results
                    mediaItemsListTag.innerHTML = ""
                    for(var index=0;index<mediaItems.length;index++) {
                      mediaItemsListTag.innerHTML += "<li class='list-group-item'>"+mediaItems[index].name +"</li>"
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
                  url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns.json?sort=-name",
                  async: false,
                  jsonpCallback: 'jsonCallback',
                  contentType: "application/json",
                  dataType: 'jsonp',
                  success: function(json) {
                     var campaignSelect = document.getElementById("campaigns");
                     var campaigns = json.results;
                     for(var index=0;index<campaigns.length;index++) {
                       campaignSelect.innerHTML += "<option value='" +campaigns[index].id+"'>"+campaigns[index].name+"</option>"
                     }
                     listMediaItems(campaigns[0].id)
                  },
                  error: function(e) {
                     console.log(e.message);
                  }
              });
    
    
              $("#campaigns").on("change",function(){
                listMediaItems(this.value)
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
<select class="col-md-6" id="campaigns2">

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
              url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns/"+id+"/media.json",
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
                    mediaItemsListTag.innerHTML += "<li class='list-group-item'>"+mediaItems[index].name +"</li>"
                  }
                });
              },
              error: function(e) {
                 console.log(e.message);
              }
          });
}

$(document).ready(function(){
$.ajaxSetup({
    xhrFields: {
        withCredentials: true
    }
});
$.ajax({
   type: 'GET',
    url: 'https://api.digitalmedia.hhs.gov:443/api/v2/resources/campaigns.json?sort=-name',
    async: false,
    jsonpCallback: 'jsonCallback',
    contentType: "application/json",
    dataType: 'jsonp',
    success: function(json) {
       var campaignSelect = document.getElementById("campaigns2");
       var campaigns = json.results;
       for(var index=0;index<campaigns.length;index++) {
         campaignSelect.innerHTML += "<option value='" +campaigns[index].id+"'>"+campaigns[index].name+"</option>"
       }
       listMediaItems(campaigns[0].id)
    },
    error: function(e) {
       console.log(e.message);
    }
});

$("#campaigns2").on("change",function(){
listMediaItems(this.value)
});

});
</script>