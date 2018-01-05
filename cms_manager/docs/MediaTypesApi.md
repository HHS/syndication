# MediaTypesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesMediaTypesFormatGet**](MediaTypesApi.md#syndicationApiV2ResourcesMediaTypesFormatGet) | **GET** /Syndication/api/v2/resources/mediaTypes.{format} | Get MediaTypes


<a name="syndicationApiV2ResourcesMediaTypesFormatGet"></a>
# **syndicationApiV2ResourcesMediaTypesFormatGet**
> List&lt;MediaTypeHolderWrapped&gt; syndicationApiV2ResourcesMediaTypesFormatGet()

Get MediaTypes

Information about media types

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MediaTypesApi;


MediaTypesApi apiInstance = new MediaTypesApi();
try {
    List<MediaTypeHolderWrapped> result = apiInstance.syndicationApiV2ResourcesMediaTypesFormatGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MediaTypesApi#syndicationApiV2ResourcesMediaTypesFormatGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;MediaTypeHolderWrapped&gt;**](MediaTypeHolderWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

