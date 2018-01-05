# SourcesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesSourcesIdJsonGet**](SourcesApi.md#syndicationApiV2ResourcesSourcesIdJsonGet) | **GET** /Syndication/api/v2/resources/sources/{id}.json | Get Source by ID
[**syndicationApiV2ResourcesSourcesIdSyndicateFormatGet**](SourcesApi.md#syndicationApiV2ResourcesSourcesIdSyndicateFormatGet) | **GET** /Syndication/api/v2/resources/sources/{id}/syndicate.{format} | Get MediaItems for Source
[**syndicationApiV2ResourcesSourcesJsonGet**](SourcesApi.md#syndicationApiV2ResourcesSourcesJsonGet) | **GET** /Syndication/api/v2/resources/sources.json | Get Sources


<a name="syndicationApiV2ResourcesSourcesIdJsonGet"></a>
# **syndicationApiV2ResourcesSourcesIdJsonGet**
> List&lt;SourceWrapped&gt; syndicationApiV2ResourcesSourcesIdJsonGet(id)

Get Source by ID

Information about a specific source.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SourcesApi;


SourcesApi apiInstance = new SourcesApi();
Long id = 789L; // Long | The id of the source to look up
try {
    List<SourceWrapped> result = apiInstance.syndicationApiV2ResourcesSourcesIdJsonGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SourcesApi#syndicationApiV2ResourcesSourcesIdJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the source to look up |

### Return type

[**List&lt;SourceWrapped&gt;**](SourceWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesSourcesIdSyndicateFormatGet"></a>
# **syndicationApiV2ResourcesSourcesIdSyndicateFormatGet**
> List&lt;MediaItemWrapped&gt; syndicationApiV2ResourcesSourcesIdSyndicateFormatGet(id, displayMethod)

Get MediaItems for Source

MediaItem

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SourcesApi;


SourcesApi apiInstance = new SourcesApi();
Long id = 789L; // Long | The id of the record to look up
String displayMethod = "displayMethod_example"; // String | Method used to render an html request. Accepts one: [mv, list, feed]
try {
    List<MediaItemWrapped> result = apiInstance.syndicationApiV2ResourcesSourcesIdSyndicateFormatGet(id, displayMethod);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SourcesApi#syndicationApiV2ResourcesSourcesIdSyndicateFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the record to look up |
 **displayMethod** | **String**| Method used to render an html request. Accepts one: [mv, list, feed] | [optional]

### Return type

[**List&lt;MediaItemWrapped&gt;**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesSourcesJsonGet"></a>
# **syndicationApiV2ResourcesSourcesJsonGet**
> List&lt;SourceWrapped&gt; syndicationApiV2ResourcesSourcesJsonGet(max, offset, sort)

Get Sources

Source Listings

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SourcesApi;


SourcesApi apiInstance = new SourcesApi();
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
try {
    List<SourceWrapped> result = apiInstance.syndicationApiV2ResourcesSourcesJsonGet(max, offset, sort);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SourcesApi#syndicationApiV2ResourcesSourcesJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| Return records starting at the offset index. | [optional]
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]

### Return type

[**List&lt;SourceWrapped&gt;**](SourceWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

