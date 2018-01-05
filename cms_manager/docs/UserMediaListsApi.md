# UserMediaListsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesUserMediaListsIdJsonGet**](UserMediaListsApi.md#syndicationApiV2ResourcesUserMediaListsIdJsonGet) | **GET** /Syndication/api/v2/resources/userMediaLists/{id}.json | Get UserMediaList by ID


<a name="syndicationApiV2ResourcesUserMediaListsIdJsonGet"></a>
# **syndicationApiV2ResourcesUserMediaListsIdJsonGet**
> List&lt;MediaItemWrapped&gt; syndicationApiV2ResourcesUserMediaListsIdJsonGet(id, displayMethod)

Get UserMediaList by ID

Get a specific user media list.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.UserMediaListsApi;


UserMediaListsApi apiInstance = new UserMediaListsApi();
Long id = 789L; // Long | The id of the record to look up
String displayMethod = "displayMethod_example"; // String | Method used to render an html request. Accepts one: [mv, list, feed]
try {
    List<MediaItemWrapped> result = apiInstance.syndicationApiV2ResourcesUserMediaListsIdJsonGet(id, displayMethod);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserMediaListsApi#syndicationApiV2ResourcesUserMediaListsIdJsonGet");
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

