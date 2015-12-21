##Resources

Returns the list of Resources matching the search query 'q'.<p>The search query 'q' is a Lucene query. The syntax for
 a Lucene query can be found <a href="http://lucene.apache.org/core/2_9_4/queryparsersyntax.html">here</a>.

###GET/JSON example
If we use the work "health" to search on we will get back multiple types of content that are related to health.

```html
  https://api.digitalmedia.hhs.gov:443/api/v2/resources.json?q=health
```

This json response contains tags, campaigns, and sources that relate to the work health. 

```javascript

  {...
    "results": [
      {
        "tags": {
          "items": [
            {
              "id": "7",
              "name": "health effects"
            }
          ]
        },
        "sources": {
          "items": [
            {
              "id": "27",
              "name": "National Institute of Child Health and Human Development"
            },
            {
              "id": "24",
              "name": "NIH Senior Health"
            },
            {
              "id": "15",
              "name": "National Institute of Dental and Craniofacial Research"
            },
            {
              "id": "23",
              "name": "National Institute of Environmental Health Sciences"
            },
            {
              "id": "25",
              "name": "National Institute of Mental Health"
            },
            {
              "id": "26",
              "name": "The National Institute of Nursing Research"
            },
            {
              "id": "9",
              "name": "NIH Heart, Lung and Blood Institute"
            }
          ]
        },
        "campaigns": {
          "items": [
            {
              "id": "5",
              "name": "Pain Promo"
            }
          ]
        },
        "total": 9
      }
    ]
  }
  
```

Each content item will only contain its name and Id. With that you can make subsequent calls to get more meta data 
on those items.