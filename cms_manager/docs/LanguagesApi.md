# LanguagesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesLanguagesIdJsonGet**](LanguagesApi.md#syndicationApiV2ResourcesLanguagesIdJsonGet) | **GET** /Syndication/api/v2/resources/languages/{id}.json | Get Language by ID
[**syndicationApiV2ResourcesLanguagesJsonGet**](LanguagesApi.md#syndicationApiV2ResourcesLanguagesJsonGet) | **GET** /Syndication/api/v2/resources/languages.json | Get Languages


<a name="syndicationApiV2ResourcesLanguagesIdJsonGet"></a>
# **syndicationApiV2ResourcesLanguagesIdJsonGet**
> List&lt;LanguageWrapped&gt; syndicationApiV2ResourcesLanguagesIdJsonGet(id)

Get Language by ID

Information about a specific language

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.LanguagesApi;


LanguagesApi apiInstance = new LanguagesApi();
Long id = 789L; // Long | The id of the language to look up
try {
    List<LanguageWrapped> result = apiInstance.syndicationApiV2ResourcesLanguagesIdJsonGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling LanguagesApi#syndicationApiV2ResourcesLanguagesIdJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the language to look up |

### Return type

[**List&lt;LanguageWrapped&gt;**](LanguageWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesLanguagesJsonGet"></a>
# **syndicationApiV2ResourcesLanguagesJsonGet**
> List&lt;LanguageWrapped&gt; syndicationApiV2ResourcesLanguagesJsonGet(max, offset, sort)

Get Languages

Language Listings

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.LanguagesApi;


LanguagesApi apiInstance = new LanguagesApi();
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
try {
    List<LanguageWrapped> result = apiInstance.syndicationApiV2ResourcesLanguagesJsonGet(max, offset, sort);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling LanguagesApi#syndicationApiV2ResourcesLanguagesJsonGet");
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

[**List&lt;LanguageWrapped&gt;**](LanguageWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

