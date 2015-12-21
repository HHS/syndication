##tags

Tags are key words that Media Items are attached to. They allow you to search on Media Items by keywords that were
carefully created for the Media Items.

Tags are a big resource that have different end points that allow you to get all of their possible languages and types, 
can be searched by a relation to other tags, listed out by different search parameters and even syndicated on to pages
with an embed code just like Media Items.


###GET/JSON example
Lets search for tags containing the name "ebola" and then find other tag names that are related to that tag.

Search for ebola 

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/tags.json?name=ebola
```

The Json response 

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 1,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json?offset=0&max=20&sort=id&name=ebola&format=json&includePaginationFields=",
          "max": 20,
          "nextUrl": null,
          "offset": 0,
          "pageNum": 1,
          "previousUrl": null,
          "sort": "id",
          "total": 1,
          "totalPages": 1
        },
        "status": 200
      },
      "results": [
        {
          "id": 99,
          "name": "ebola",
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
      ]
    }
```

Ebola has an ID of 99 so lets look up related tags with the ID of 99

```html
  https://api.digitalmedia.hhs.gov:443/api/v2/resources/tags/99/related.json
```

The json response will return tags that are similar in meaning. Which currently for ebola are 

```javascript
  {
    "callback": null,
    "meta": {
      "messages": [],
      "pagination": {
        "count": 11,
        "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json?offset=0&max=1&sort=id&id=99&format=json&tagId=99&includePaginationFields=true",
        "max": 1,
        "nextUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json?offset=1&max=1&sort=id&id=99&format=json&tagId=99&includePaginationFields=true",
        "offset": 0,
        "pageNum": 1,
        "previousUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json?offset=0&max=1&sort=id&id=99&format=json&tagId=99&includePaginationFields=true",
        "sort": "id",
        "total": 1,
        "totalPages": 1
      },
      "status": 200
    },
    "results": [
      {
        "id": 100,
        "name": "ebolavirus",
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
        "id": 101,
        "name": "disaster",
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
        "id": 102,
        "name": "hemorrhagic",
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
        "id": 103,
        "name": "west africa",
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
        "id": 104,
        "name": "infectious",
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
        "id": 105,
        "name": "viral",
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
        "id": 106,
        "name": "fever",
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
        "id": 161,
        "name": "disease",
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
        "id": 689,
        "name": "outbreak",
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
        "id": 1473,
        "name": "west",
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
        "id": 1709,
        "name": "africa",
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
    ]
  }
```

###Jquery JSONP example
We can search on tags and then view the media items that are tagged with that name.

####First get the tags and populate a select tag
```html
  <div class="row">
      <div class="col-md-6">
        <select id="tags">

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
              url: "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json",
              async: false,
              jsonpCallback: 'jsonCallback',
              contentType: "application/json",
              dataType: 'jsonp',
              success: function(json) {
                 var tagSelect = document.getElementById("tags");
                 var tags = json.results
                 for(var index=0;index<tags.length;index++) {
                   tagSelect.innerHTML += "<option value='" +tags[index].id+"'>"+tags[index].name+"</option>"
                 }
                 listMediaItems(tags[0].id)
              },
              error: function(e) {
                 console.log(e.message);
              }
          });
  </script>
```
         
Now we have a populated select tag of some of the available tags. Now lets create a jquery Listener to 
display all of the MediaItems tagged with selected tag name.

Display Media Items from the campaign that was selected

```javascript
    function listMediaItems(id) {
          $.ajax({
             type: 'GET',
              url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/tags/"+id+"/media.json",
              async: false,
              jsonpCallback: 'jsonCallback',
              contentType: "application/json",
              dataType: 'jsonp',
              success: function(json) {
                var mediaItemsListTag = document.getElementById("mediaItems")
                var mediaItems = json.results
                mediaItemsListTag.innerHTML = ""
                for(var index=0;index<mediaItems.length;index++) {
                  mediaItemsListTag.innerHTML += "<li class='list-group-item'><img src='"+mediaItems[index].thumbnailUrl+"'/><a href='"+mediaItems[index].sourceUrl+"'>"+mediaItems[index].name +"</a></li>"
                }
              },
              error: function(e) {
                 console.log(e.message);
              }
          });
    }
            
    $("#tags").on("change",function(){
        listMediaItems(this.value)
      });
```
      
Now any time someone changes the selected tag the list of mediaItems will get updated to the ones in that 
tag.

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
                    <select id="tags">
    
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
                      url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/tags/"+id+"/media.json",
                      async: false,
                      jsonpCallback: 'jsonCallback',
                      contentType: "application/json",
                      dataType: 'jsonp',
                      success: function(json) {
                        var mediaItemsListTag = document.getElementById("mediaItems")
                        var mediaItems = json.results
                        mediaItemsListTag.innerHTML = ""
                        for(var index=0;index<mediaItems.length;index++) {
                          mediaItemsListTag.innerHTML += "<li class='list-group-item'><img src='"+mediaItems[index].thumbnailUrl+"'/><a href='"+mediaItems[index].sourceUrl+"'>"+mediaItems[index].name +"</a></li>"
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
                      url: "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json",
                      async: false,
                      jsonpCallback: 'jsonCallback',
                      contentType: "application/json",
                      dataType: 'jsonp',
                      success: function(json) {
                         var tagSelect = document.getElementById("tags");
                         var tags = json.results
                         for(var index=0;index<tags.length;index++) {
                           tagSelect.innerHTML += "<option value='" +tags[index].id+"'>"+tags[index].name+"</option>"
                         }
                         listMediaItems(tags[0].id)
                      },
                      error: function(e) {
                         console.log(e.message);
                      }
                  });
    
                  $("#tags").on("change",function(){
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
<select id="tags2">

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
url: "https://api.digitalmedia.hhs.gov:443/api/v2/resources/tags/"+id+"/media.json?max=5",
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
mediaItemsListTag.innerHTML += "<li class='list-group-item'><img src='"+mediaItems[index].thumbnailUrl+"'/><a href='"+mediaItems[index].sourceUrl+"'>"+mediaItems[index].name +"</a></li>"
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
url: "https://api.digitalmedia.hhs.gov/api/v2/resources/tags.json",
async: false,
jsonpCallback: 'jsonCallback',
contentType: "application/json",
dataType: 'jsonp',
success: function(json) {
var tagSelect = document.getElementById("tags2");
var tags = json.results
for(var index=0;index<tags.length;index++) {
tagSelect.innerHTML += "<option value='" +tags[index].id+"'>"+tags[index].name+"</option>"
}
listMediaItems(tags[0].id)
},
error: function(e) {
console.log(e.message);
}
});

$("#tags2").on("change",function(){
listMediaItems(this.value)
});

});
</script>