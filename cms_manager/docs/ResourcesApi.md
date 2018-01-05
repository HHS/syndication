# ResourcesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesJsonGet**](ResourcesApi.md#syndicationApiV2ResourcesJsonGet) | **GET** /Syndication/api/v2/resources.json | Get Resources by search query


<a name="syndicationApiV2ResourcesJsonGet"></a>
# **syndicationApiV2ResourcesJsonGet**
> List&lt;Resource&gt; syndicationApiV2ResourcesJsonGet(q)

Get Resources by search query

Global search

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ResourcesApi;


ResourcesApi apiInstance = new ResourcesApi();
String q = "q_example"; // String | The search query supplied by the user
try {
    List<Resource> result = apiInstance.syndicationApiV2ResourcesJsonGet(q);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ResourcesApi#syndicationApiV2ResourcesJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **q** | **String**| The search query supplied by the user |

### Return type

[**List&lt;Resource&gt;**](Resource.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

