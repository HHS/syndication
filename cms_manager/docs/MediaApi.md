# MediaApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2ResourcesMediaFeaturedJsonGet**](MediaApi.md#apiV2ResourcesMediaFeaturedJsonGet) | **GET** /api/v2/resources/media/featured.json | Get the list of featured content in the syndication system
[**apiV2ResourcesMediaIdContentGet**](MediaApi.md#apiV2ResourcesMediaIdContentGet) | **GET** /api/v2/resources/media/{id}/content | Get content for MediaItem
[**apiV2ResourcesMediaIdEmbedJsonGet**](MediaApi.md#apiV2ResourcesMediaIdEmbedJsonGet) | **GET** /api/v2/resources/media/{id}/embed.json | Get embed code for MediaItem
[**apiV2ResourcesMediaIdJsonGet**](MediaApi.md#apiV2ResourcesMediaIdJsonGet) | **GET** /api/v2/resources/media/{id}.json | Get MediaItem by ID
[**apiV2ResourcesMediaIdPreviewJpgGet**](MediaApi.md#apiV2ResourcesMediaIdPreviewJpgGet) | **GET** /api/v2/resources/media/{id}/preview.jpg | Get Tag by ID
[**apiV2ResourcesMediaIdRelatedMediaFormatGet**](MediaApi.md#apiV2ResourcesMediaIdRelatedMediaFormatGet) | **GET** /api/v2/resources/media/{id}/relatedMedia.{format} | Get related MediaItems by ID
[**apiV2ResourcesMediaIdSyndicateFormatGet**](MediaApi.md#apiV2ResourcesMediaIdSyndicateFormatGet) | **GET** /api/v2/resources/media/{id}/syndicate.{format} | Get syndicated content for MediaItem
[**apiV2ResourcesMediaIdThumbnailJpgGet**](MediaApi.md#apiV2ResourcesMediaIdThumbnailJpgGet) | **GET** /api/v2/resources/media/{id}/thumbnail.jpg | Get JPG thumbnail for MediaItem
[**apiV2ResourcesMediaIdYoutubeMetaDataJsonGet**](MediaApi.md#apiV2ResourcesMediaIdYoutubeMetaDataJsonGet) | **GET** /api/v2/resources/media/{id}/youtubeMetaData.json | Get Youtube metadata for MediaItem
[**apiV2ResourcesMediaJsonGet**](MediaApi.md#apiV2ResourcesMediaJsonGet) | **GET** /api/v2/resources/media.json | Get MediaItems
[**apiV2ResourcesMediaMostPopularMediaFormatGet**](MediaApi.md#apiV2ResourcesMediaMostPopularMediaFormatGet) | **GET** /api/v2/resources/media/mostPopularMedia.{format} | Get MediaItems by popularity
[**apiV2ResourcesMediaSearchResultsJsonGet**](MediaApi.md#apiV2ResourcesMediaSearchResultsJsonGet) | **GET** /api/v2/resources/media/searchResults.json | Get MediaItems by search query


<a name="apiV2ResourcesMediaFeaturedJsonGet"></a>
# **apiV2ResourcesMediaFeaturedJsonGet**
> List&lt;MediaItem&gt; apiV2ResourcesMediaFeaturedJsonGet(sort, max, offset)

Get the list of featured content in the syndication system

Get the list of featured content in the syndication system

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
try {
    List<MediaItem> result = apiInstance.apiV2ResourcesMediaFeaturedJsonGet(sort, max, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaFeaturedJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| Return records starting at the offset index. | [optional]

### Return type

[**List&lt;MediaItem&gt;**](MediaItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdContentGet"></a>
# **apiV2ResourcesMediaIdContentGet**
> String apiV2ResourcesMediaIdContentGet(id, calledByBuild)

Get content for MediaItem

The actual media content (html, image, etc...)

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the media to show content for.
Boolean calledByBuild = true; // Boolean | The method that called this method
try {
    String result = apiInstance.apiV2ResourcesMediaIdContentGet(id, calledByBuild);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdContentGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the media to show content for. |
 **calledByBuild** | **Boolean**| The method that called this method | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdEmbedJsonGet"></a>
# **apiV2ResourcesMediaIdEmbedJsonGet**
> String apiV2ResourcesMediaIdEmbedJsonGet(id, flavor, width, height, iframeName, excludeJquery, excludeDiv, divId, displayMethod)

Get embed code for MediaItem

Get the javascript or iframe embed code for this item (to embed it on a web page).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the media to get embed code for.
String flavor = "flavor_example"; // String | Currently supports 'iframe', defaults to 'javascript'.
Integer width = 56; // Integer | The width of the generated iframe.
Integer height = 56; // Integer | The height of the generated iframe.
String iframeName = "iframeName_example"; // String | The name of the iframe element
Boolean excludeJquery = false; // Boolean | Should a reference to the JQuery Library be omitted?
Boolean excludeDiv = false; // Boolean | Should the div to insert content into be omitted?
String divId = "divId_example"; // String | Should the div to insert content into have a specific name?
String displayMethod = "displayMethod_example"; // String | Method used to render an html request. Accepts one: [mv, list, feed]
try {
    String result = apiInstance.apiV2ResourcesMediaIdEmbedJsonGet(id, flavor, width, height, iframeName, excludeJquery, excludeDiv, divId, displayMethod);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdEmbedJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the media to get embed code for. |
 **flavor** | **String**| Currently supports &#39;iframe&#39;, defaults to &#39;javascript&#39;. | [optional]
 **width** | **Integer**| The width of the generated iframe. | [optional]
 **height** | **Integer**| The height of the generated iframe. | [optional]
 **iframeName** | **String**| The name of the iframe element | [optional]
 **excludeJquery** | **Boolean**| Should a reference to the JQuery Library be omitted? | [optional] [default to false]
 **excludeDiv** | **Boolean**| Should the div to insert content into be omitted? | [optional] [default to false]
 **divId** | **String**| Should the div to insert content into have a specific name? | [optional]
 **displayMethod** | **String**| Method used to render an html request. Accepts one: [mv, list, feed] | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdJsonGet"></a>
# **apiV2ResourcesMediaIdJsonGet**
> List&lt;MediaItemWrapped&gt; apiV2ResourcesMediaIdJsonGet(id)

Get MediaItem by ID

Information about a specific media item

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the record to look up
try {
    List<MediaItemWrapped> result = apiInstance.apiV2ResourcesMediaIdJsonGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the record to look up |

### Return type

[**List&lt;MediaItemWrapped&gt;**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdPreviewJpgGet"></a>
# **apiV2ResourcesMediaIdPreviewJpgGet**
> Object apiV2ResourcesMediaIdPreviewJpgGet(id)

Get Tag by ID

Get the jpg preview of the content item where applicable.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the media to get a preview for.
try {
    Object result = apiInstance.apiV2ResourcesMediaIdPreviewJpgGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdPreviewJpgGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the media to get a preview for. |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdRelatedMediaFormatGet"></a>
# **apiV2ResourcesMediaIdRelatedMediaFormatGet**
> List&lt;MediaItemWrapped&gt; apiV2ResourcesMediaIdRelatedMediaFormatGet(id, max, offset, sort)

Get related MediaItems by ID

Get the media related to the current media item.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the media item to get related media for
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
try {
    List<MediaItemWrapped> result = apiInstance.apiV2ResourcesMediaIdRelatedMediaFormatGet(id, max, offset, sort);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdRelatedMediaFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the media item to get related media for |
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| Return records starting at the offset index. | [optional]
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]

### Return type

[**List&lt;MediaItemWrapped&gt;**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdSyndicateFormatGet"></a>
# **apiV2ResourcesMediaIdSyndicateFormatGet**
> SyndicateMarshallerWrapped apiV2ResourcesMediaIdSyndicateFormatGet(id, cssClass, stripStyles, stripScripts, stripImages, stripBreaks, stripClasses, fontSize, imageFloat, imageMargin, autoplay, rel)

Get syndicated content for MediaItem

Get syndicated content.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the media to show embed code for.
String cssClass = "syndicate"; // String | The css class to target for extraction.
Boolean stripStyles = false; // Boolean | Remove in-line styles from content.
Boolean stripScripts = false; // Boolean | Remove script tags from content.
Boolean stripImages = false; // Boolean | Remove image tags from content.
Boolean stripBreaks = false; // Boolean | Remove break tags from content.
Boolean stripClasses = false; // Boolean | Remove class attributes from content (except 'syndicate').
Integer fontSize = 56; // Integer | Set font size (in points) of p, div, and span tags.
String imageFloat = "imageFloat_example"; // String | Accepts valid CSS float options, such as 'left' or 'right'. Will inject a style into the content before rendering.
String imageMargin = "imageMargin_example"; // String | Accepts 4 CSV values representing pixel sizes of margin similar to CSS. Default format is 'north,east,south,west' - for example '0,10,10,0' would put a 10 pixel margin on the right and bottom sides of an image. Will inject a style into the content before rendering.
Boolean autoplay = true; // Boolean | If content is a video, the embeded video will auto play when loaded.
Boolean rel = false; // Boolean | If content is a video, related items will be shown at the end of playback.
try {
    SyndicateMarshallerWrapped result = apiInstance.apiV2ResourcesMediaIdSyndicateFormatGet(id, cssClass, stripStyles, stripScripts, stripImages, stripBreaks, stripClasses, fontSize, imageFloat, imageMargin, autoplay, rel);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdSyndicateFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the media to show embed code for. |
 **cssClass** | **String**| The css class to target for extraction. | [optional] [default to syndicate]
 **stripStyles** | **Boolean**| Remove in-line styles from content. | [optional] [default to false]
 **stripScripts** | **Boolean**| Remove script tags from content. | [optional] [default to false]
 **stripImages** | **Boolean**| Remove image tags from content. | [optional] [default to false]
 **stripBreaks** | **Boolean**| Remove break tags from content. | [optional] [default to false]
 **stripClasses** | **Boolean**| Remove class attributes from content (except &#39;syndicate&#39;). | [optional] [default to false]
 **fontSize** | **Integer**| Set font size (in points) of p, div, and span tags. | [optional]
 **imageFloat** | **String**| Accepts valid CSS float options, such as &#39;left&#39; or &#39;right&#39;. Will inject a style into the content before rendering. | [optional]
 **imageMargin** | **String**| Accepts 4 CSV values representing pixel sizes of margin similar to CSS. Default format is &#39;north,east,south,west&#39; - for example &#39;0,10,10,0&#39; would put a 10 pixel margin on the right and bottom sides of an image. Will inject a style into the content before rendering. | [optional]
 **autoplay** | **Boolean**| If content is a video, the embeded video will auto play when loaded. | [optional] [default to true]
 **rel** | **Boolean**| If content is a video, related items will be shown at the end of playback. | [optional] [default to false]

### Return type

[**SyndicateMarshallerWrapped**](SyndicateMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdThumbnailJpgGet"></a>
# **apiV2ResourcesMediaIdThumbnailJpgGet**
> Object apiV2ResourcesMediaIdThumbnailJpgGet(id)

Get JPG thumbnail for MediaItem

Get the jpg thumbnail of the content item where applicable.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the media to get a thumbnail for.
try {
    Object result = apiInstance.apiV2ResourcesMediaIdThumbnailJpgGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdThumbnailJpgGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the media to get a thumbnail for. |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaIdYoutubeMetaDataJsonGet"></a>
# **apiV2ResourcesMediaIdYoutubeMetaDataJsonGet**
> apiV2ResourcesMediaIdYoutubeMetaDataJsonGet(id)

Get Youtube metadata for MediaItem

Youtube meta-data for a video item.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Long id = 789L; // Long | The id of the video to show meta data for.
try {
    apiInstance.apiV2ResourcesMediaIdYoutubeMetaDataJsonGet(id);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaIdYoutubeMetaDataJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the video to show meta data for. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaJsonGet"></a>
# **apiV2ResourcesMediaJsonGet**
> List&lt;MediaItemWrapped&gt; apiV2ResourcesMediaJsonGet(max, offset, sort, order, mediaTypes, name, collectionId, nameContains, descriptionContains, sourceUrl, sourceUrlContains, customThumbnailUrl, customThumbnailUrlContains, dateContentAuthored, dateContentUpdated, dateContentPublished, dateContentReviewed, dateSyndicationCaptured, dateSyndicationUpdated, contentAuthoredSinceDate, contentAuthoredBeforeDate, contentAuthoredInRange, contentUpdatedSinceDate, contentUpdatedBeforeDate, contentUpdatedInRange, contentPublishedSinceDate, contentPublishedBeforeDate, contentPublishedInRange, contentReviewedSinceDate, contentReviewedBeforeDate, contentReviewedInRange, syndicationCapturedSinceDate, syndicationCapturedBeforeDate, syndicationCapturedInRange, syndicationUpdatedSinceDate, syndicationUpdatedBeforeDate, syndicationUpdatedInRange, syndicationVisibleSinceDate, syndicationVisibleBeforeDate, syndicationVisibleInRange, languageId, languageName, languageIsoCode, hash, hashContains, sourceId, sourceName, sourceNameContains, sourceAcronym, sourceAcronymContains, tagIds, restrictToSet, createdBy)

Get MediaItems

Media Items Listings

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | The offset of the records set to return for pagination.
String sort = "sort_example"; // String | * Set of fields to sort the records by.
String order = "order_example"; // String | * The ascending or descending order.
String mediaTypes = "mediaTypes_example"; // String | Find all media items belonging to the specified media type[s].
String name = "name_example"; // String | Find all media items containing the provided name, case insensitive.
Integer collectionId = 56; // Integer | Restrict filtering to media items in a specific collection.
String nameContains = "nameContains_example"; // String | Find all media items containing the partial name, case insensitive.
String descriptionContains = "descriptionContains_example"; // String | Find all media items containing the provided partial description, case insensitive.
String sourceUrl = "sourceUrl_example"; // String | Find all media items which have the provided sourceUrl, case insensitive.
String sourceUrlContains = "sourceUrlContains_example"; // String | Find all media items which contain the provided partial sourceUrl, case insensitive.
String customThumbnailUrl = "customThumbnailUrl_example"; // String | Find all media items which have the provided customThumbnailUrl, case insensitive.
String customThumbnailUrlContains = "customThumbnailUrlContains_example"; // String | Find all media items which contain the provided partial customThumbnailUrl, case insensitive.
LocalDate dateContentAuthored = new LocalDate(); // LocalDate | Find all media items authored on the provided day (RFC 3339, time ignored).
LocalDate dateContentUpdated = new LocalDate(); // LocalDate | Find all media items updated on the provided day (RFC 3339, time ignored).
LocalDate dateContentPublished = new LocalDate(); // LocalDate | Find all media items published on the provided day (RFC 3339, time ignored).
LocalDate dateContentReviewed = new LocalDate(); // LocalDate | Find all media items reviewed on the provided day (RFC 3339, time ignored).
LocalDate dateSyndicationCaptured = new LocalDate(); // LocalDate | Find all media items syndicated on the provided day (RFC 3339, time ignored).
LocalDate dateSyndicationUpdated = new LocalDate(); // LocalDate | Find all media items updated through the syndication system on the provided day, (RFC 3339, time ignored).
LocalDate contentAuthoredSinceDate = new LocalDate(); // LocalDate | Find all media items authored since the provided day (RFC 3339, time ignored).
LocalDate contentAuthoredBeforeDate = new LocalDate(); // LocalDate | Find all media items authored before the provided day (RFC 3339, time ignored).
String contentAuthoredInRange = "contentAuthoredInRange_example"; // String | Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored).
LocalDate contentUpdatedSinceDate = new LocalDate(); // LocalDate | Find all media items updated since the provided day (RFC 3339, time ignored).
LocalDate contentUpdatedBeforeDate = new LocalDate(); // LocalDate | Find all media items updated before the provided day (RFC 3339, time ignored).
String contentUpdatedInRange = "contentUpdatedInRange_example"; // String | Find all media items updated between the provided start and end days (RFC 3339, comma separated, time ignored).
LocalDate contentPublishedSinceDate = new LocalDate(); // LocalDate | Find all media items updated since the provided day (RFC 3339, time ignored).
LocalDate contentPublishedBeforeDate = new LocalDate(); // LocalDate | Find all media items published before the provided day (RFC 3339, time ignored).
String contentPublishedInRange = "contentPublishedInRange_example"; // String | Find all media items published between the provided start and end days (RFC 3339, comma separated, time ignored).
LocalDate contentReviewedSinceDate = new LocalDate(); // LocalDate | Find all media items reviewed since the provided day (RFC 3339, time ignored).
LocalDate contentReviewedBeforeDate = new LocalDate(); // LocalDate | Find all media items reviewed before the provided day (RFC 3339, time ignored).
String contentReviewedInRange = "contentReviewedInRange_example"; // String | Find all media items reviewed between the provided start and end days (RFC 3339, comma separated, time ignored).
LocalDate syndicationCapturedSinceDate = new LocalDate(); // LocalDate | Find all media items authored since the provided day (RFC 3339, time ignored).
LocalDate syndicationCapturedBeforeDate = new LocalDate(); // LocalDate | Find all media items authored before the provided day (RFC 3339, time ignored).
String syndicationCapturedInRange = "syndicationCapturedInRange_example"; // String | Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored).
LocalDate syndicationUpdatedSinceDate = new LocalDate(); // LocalDate | Find all media items updated since the provided day, (RFC 3339, time ignored).
LocalDate syndicationUpdatedBeforeDate = new LocalDate(); // LocalDate | Find all media items updated before the provided day, (RFC 3339, time ignored).
String syndicationUpdatedInRange = "syndicationUpdatedInRange_example"; // String | Find all media items updated between the provided start and end days, (RFC 3339, comma separated, time ignored).
LocalDate syndicationVisibleSinceDate = new LocalDate(); // LocalDate | Find all media items visible since the provided day, (RFC 3339, time ignored).
LocalDate syndicationVisibleBeforeDate = new LocalDate(); // LocalDate | Find all media items visible before the provided day, (RFC 3339, time ignored).
LocalDate syndicationVisibleInRange = new LocalDate(); // LocalDate | Find all media items visible between the provided start and end days, (RFC 3339, comma separated, time ignored).
Long languageId = 789L; // Long | Find all media items written in the language specified by Id.
String languageName = "languageName_example"; // String | Find all media items written in the language specified by name, case insensitive.
String languageIsoCode = "languageIsoCode_example"; // String | Find all media items written in the language specified by 639-2 isoCode , case insensitive.
String hash = "hash_example"; // String | Find all media items which match the provided hash, case insensitive.
String hashContains = "hashContains_example"; // String | Find all media items which match the provided partial hash, case insensitive.
Long sourceId = 789L; // Long | Find all media items that belong to the source specified by Id.
String sourceName = "sourceName_example"; // String | Find all media items that belong to the source specified by name, case insensitive.
String sourceNameContains = "sourceNameContains_example"; // String | Find all media items that belong to the source specified by partial name, case insensitive.
String sourceAcronym = "sourceAcronym_example"; // String | Find all media items that belong to the source specified by acronym, case insensitive.
String sourceAcronymContains = "sourceAcronymContains_example"; // String | Find all media items that belong to the source specified by partial acronym, case insensitive.
String tagIds = "tagIds_example"; // String | Find only media items tagged with the specified tag Ids.
String restrictToSet = "restrictToSet_example"; // String | Find only media from within the supplied list of Ids.
String createdBy = "createdBy_example"; // String | Find all media items containing the createdBy value.
try {
    List<MediaItemWrapped> result = apiInstance.apiV2ResourcesMediaJsonGet(max, offset, sort, order, mediaTypes, name, collectionId, nameContains, descriptionContains, sourceUrl, sourceUrlContains, customThumbnailUrl, customThumbnailUrlContains, dateContentAuthored, dateContentUpdated, dateContentPublished, dateContentReviewed, dateSyndicationCaptured, dateSyndicationUpdated, contentAuthoredSinceDate, contentAuthoredBeforeDate, contentAuthoredInRange, contentUpdatedSinceDate, contentUpdatedBeforeDate, contentUpdatedInRange, contentPublishedSinceDate, contentPublishedBeforeDate, contentPublishedInRange, contentReviewedSinceDate, contentReviewedBeforeDate, contentReviewedInRange, syndicationCapturedSinceDate, syndicationCapturedBeforeDate, syndicationCapturedInRange, syndicationUpdatedSinceDate, syndicationUpdatedBeforeDate, syndicationUpdatedInRange, syndicationVisibleSinceDate, syndicationVisibleBeforeDate, syndicationVisibleInRange, languageId, languageName, languageIsoCode, hash, hashContains, sourceId, sourceName, sourceNameContains, sourceAcronym, sourceAcronymContains, tagIds, restrictToSet, createdBy);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| The offset of the records set to return for pagination. | [optional]
 **sort** | **String**| * Set of fields to sort the records by. | [optional]
 **order** | **String**| * The ascending or descending order. | [optional]
 **mediaTypes** | **String**| Find all media items belonging to the specified media type[s]. | [optional]
 **name** | **String**| Find all media items containing the provided name, case insensitive. | [optional]
 **collectionId** | **Integer**| Restrict filtering to media items in a specific collection. | [optional]
 **nameContains** | **String**| Find all media items containing the partial name, case insensitive. | [optional]
 **descriptionContains** | **String**| Find all media items containing the provided partial description, case insensitive. | [optional]
 **sourceUrl** | **String**| Find all media items which have the provided sourceUrl, case insensitive. | [optional]
 **sourceUrlContains** | **String**| Find all media items which contain the provided partial sourceUrl, case insensitive. | [optional]
 **customThumbnailUrl** | **String**| Find all media items which have the provided customThumbnailUrl, case insensitive. | [optional]
 **customThumbnailUrlContains** | **String**| Find all media items which contain the provided partial customThumbnailUrl, case insensitive. | [optional]
 **dateContentAuthored** | **LocalDate**| Find all media items authored on the provided day (RFC 3339, time ignored). | [optional]
 **dateContentUpdated** | **LocalDate**| Find all media items updated on the provided day (RFC 3339, time ignored). | [optional]
 **dateContentPublished** | **LocalDate**| Find all media items published on the provided day (RFC 3339, time ignored). | [optional]
 **dateContentReviewed** | **LocalDate**| Find all media items reviewed on the provided day (RFC 3339, time ignored). | [optional]
 **dateSyndicationCaptured** | **LocalDate**| Find all media items syndicated on the provided day (RFC 3339, time ignored). | [optional]
 **dateSyndicationUpdated** | **LocalDate**| Find all media items updated through the syndication system on the provided day, (RFC 3339, time ignored). | [optional]
 **contentAuthoredSinceDate** | **LocalDate**| Find all media items authored since the provided day (RFC 3339, time ignored). | [optional]
 **contentAuthoredBeforeDate** | **LocalDate**| Find all media items authored before the provided day (RFC 3339, time ignored). | [optional]
 **contentAuthoredInRange** | **String**| Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored). | [optional]
 **contentUpdatedSinceDate** | **LocalDate**| Find all media items updated since the provided day (RFC 3339, time ignored). | [optional]
 **contentUpdatedBeforeDate** | **LocalDate**| Find all media items updated before the provided day (RFC 3339, time ignored). | [optional]
 **contentUpdatedInRange** | **String**| Find all media items updated between the provided start and end days (RFC 3339, comma separated, time ignored). | [optional]
 **contentPublishedSinceDate** | **LocalDate**| Find all media items updated since the provided day (RFC 3339, time ignored). | [optional]
 **contentPublishedBeforeDate** | **LocalDate**| Find all media items published before the provided day (RFC 3339, time ignored). | [optional]
 **contentPublishedInRange** | **String**| Find all media items published between the provided start and end days (RFC 3339, comma separated, time ignored). | [optional]
 **contentReviewedSinceDate** | **LocalDate**| Find all media items reviewed since the provided day (RFC 3339, time ignored). | [optional]
 **contentReviewedBeforeDate** | **LocalDate**| Find all media items reviewed before the provided day (RFC 3339, time ignored). | [optional]
 **contentReviewedInRange** | **String**| Find all media items reviewed between the provided start and end days (RFC 3339, comma separated, time ignored). | [optional]
 **syndicationCapturedSinceDate** | **LocalDate**| Find all media items authored since the provided day (RFC 3339, time ignored). | [optional]
 **syndicationCapturedBeforeDate** | **LocalDate**| Find all media items authored before the provided day (RFC 3339, time ignored). | [optional]
 **syndicationCapturedInRange** | **String**| Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored). | [optional]
 **syndicationUpdatedSinceDate** | **LocalDate**| Find all media items updated since the provided day, (RFC 3339, time ignored). | [optional]
 **syndicationUpdatedBeforeDate** | **LocalDate**| Find all media items updated before the provided day, (RFC 3339, time ignored). | [optional]
 **syndicationUpdatedInRange** | **String**| Find all media items updated between the provided start and end days, (RFC 3339, comma separated, time ignored). | [optional]
 **syndicationVisibleSinceDate** | **LocalDate**| Find all media items visible since the provided day, (RFC 3339, time ignored). | [optional]
 **syndicationVisibleBeforeDate** | **LocalDate**| Find all media items visible before the provided day, (RFC 3339, time ignored). | [optional]
 **syndicationVisibleInRange** | **LocalDate**| Find all media items visible between the provided start and end days, (RFC 3339, comma separated, time ignored). | [optional]
 **languageId** | **Long**| Find all media items written in the language specified by Id. | [optional]
 **languageName** | **String**| Find all media items written in the language specified by name, case insensitive. | [optional]
 **languageIsoCode** | **String**| Find all media items written in the language specified by 639-2 isoCode , case insensitive. | [optional]
 **hash** | **String**| Find all media items which match the provided hash, case insensitive. | [optional]
 **hashContains** | **String**| Find all media items which match the provided partial hash, case insensitive. | [optional]
 **sourceId** | **Long**| Find all media items that belong to the source specified by Id. | [optional]
 **sourceName** | **String**| Find all media items that belong to the source specified by name, case insensitive. | [optional]
 **sourceNameContains** | **String**| Find all media items that belong to the source specified by partial name, case insensitive. | [optional]
 **sourceAcronym** | **String**| Find all media items that belong to the source specified by acronym, case insensitive. | [optional]
 **sourceAcronymContains** | **String**| Find all media items that belong to the source specified by partial acronym, case insensitive. | [optional]
 **tagIds** | **String**| Find only media items tagged with the specified tag Ids. | [optional]
 **restrictToSet** | **String**| Find only media from within the supplied list of Ids. | [optional]
 **createdBy** | **String**| Find all media items containing the createdBy value. | [optional]

### Return type

[**List&lt;MediaItemWrapped&gt;**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaMostPopularMediaFormatGet"></a>
# **apiV2ResourcesMediaMostPopularMediaFormatGet**
> List&lt;MediaItemWrapped&gt; apiV2ResourcesMediaMostPopularMediaFormatGet(max, offset)

Get MediaItems by popularity

Get the media with the highest ratings.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | The offset of the records set to return for pagination.
try {
    List<MediaItemWrapped> result = apiInstance.apiV2ResourcesMediaMostPopularMediaFormatGet(max, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaMostPopularMediaFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| The offset of the records set to return for pagination. | [optional]

### Return type

[**List&lt;MediaItemWrapped&gt;**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="apiV2ResourcesMediaSearchResultsJsonGet"></a>
# **apiV2ResourcesMediaSearchResultsJsonGet**
> List&lt;MediaItemWrapped&gt; apiV2ResourcesMediaSearchResultsJsonGet(q, max, offset)

Get MediaItems by search query

Full search

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaApi;


MediaApi apiInstance = new MediaApi();
String q = "q_example"; // String | The search query supplied by the user
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | The offset of the records set to return for pagination.
try {
    List<MediaItemWrapped> result = apiInstance.apiV2ResourcesMediaSearchResultsJsonGet(q, max, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaApi#apiV2ResourcesMediaSearchResultsJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **q** | **String**| The search query supplied by the user |
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| The offset of the records set to return for pagination. | [optional]

### Return type

[**List&lt;MediaItemWrapped&gt;**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

