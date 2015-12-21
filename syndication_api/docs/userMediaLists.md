##User Media Lists

These are personalized lists that anyone can make with their Syndication Storefront account. The registration process
is described <a href='http://ctacdevteam.bitbucket.org/docs/Syndication_Storefront/pages/UserGuide.html#new-user-registration'>here</a>

The User Media List creation documentation is located <a href='http://ctacdevteam.bitbucket.org/docs/Syndication_Storefront/pages/UserGuide.html#manage-lists'>here</a>.

###Get items from your custom Media Lists
When you create your User Media List you need to go to its show page and get its Id for the API call.

You can only look up User Media Lists in the API by their ID's. Here is an example using my own curated list of 
Media Items with the User Media List id being 1022.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/userMediaLists/1022.json
```

The response will only return a list of Media Items that are inside the User Media List.

No other data about the User Media List will be returned.

The format of the response will look like the following.

```html
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 10,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/userMediaLists.json?offset=0&max=1&sort=id&id=1022&format=json",
          "max": 1,
          "nextUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/userMediaLists.json?offset=1&max=1&sort=id&id=1022&format=json",
          "offset": 0,
          "pageNum": 1,
          "previousUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/userMediaLists.json?offset=0&max=1&sort=id&id=1022&format=json",
          "sort": "id",
          "total": 1,
          "totalPages": 1
        },
        "status": 200
      },
      "results": [
        {
          "mediaType": "Html",
          "id": 21,
          "name": "Heart Disease & the Flu",
          "description": "Heart disease can make your body too weak to fight off the flu. The flu can make your heart disease worse.  Learn more at Flu.gov.",
          "sourceUrl": "http://www.flu.gov/at-risk/health-conditions/heart-disease/index.html",
          "targetUrl": null,
          "dateContentAuthored": null,
          "dateContentUpdated": null,
          "dateContentPublished": null,
          "dateContentReviewed": null,
          "dateSyndicationVisible": "2015-04-06T22:03:24Z",
          "dateSyndicationCaptured": "2015-04-06T22:03:24Z",
          "dateSyndicationUpdated": "2015-09-28T16:48:48Z",
          "language": {
            "id": 1,
            "name": "English",
            "isoCode": "eng"
          },
          "externalGuid": null,
          "contentHash": "2f0ffcc14d0ff3119417c514f94fa86e",
          "source": {
            "id": 1,
            "name": "Health and Human Services",
            "acronym": "HHS",
            "websiteUrl": "http://www.hhs.gov",
            "contactEmail": null,
            "largeLogoUrl": null,
            "smallLogoUrl": null
          },
          "campaigns": [],
          "tags": [
            {
              "id": 1,
              "name": "flu",
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
              "id": 160,
              "name": "heart",
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
            }
          ],
          "tinyUrl": "https://tinyurl.digitalmedia.hhs.gov/l",
          "tinyToken": "l",
          "thumbnailUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/21/thumbnail.jpg",
          "previewlUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media/21/preview.jpg",
          "alternateImages": [],
          "attribution": "&lt;div id='hhsAttribution'&gt;Content provided and maintained by &lt;a href='http://www.hhs.gov' target='_blank'&gt;Health and Human Services&lt;/a&gt; (HHS). Please see our system &lt;a href='http:syndication.hhs.gov' target='_blank'&gt;usage guidelines and disclaimer&lt;/a&gt;.&lt;/div&gt;",
          "extendedAttributes": {},
          "customThumbnailUrl": null,
          "customPreviewUrl": null
        }...
      ...]
    }
```