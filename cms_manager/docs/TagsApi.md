# TagsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesTagsFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsFormatGet) | **GET** /Syndication/api/v2/resources/tags.{format} | Get Tags
[**syndicationApiV2ResourcesTagsIdFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsIdFormatGet) | **GET** /Syndication/api/v2/resources/tags/{id}.{format} | Get Tag by ID
[**syndicationApiV2ResourcesTagsIdMediaFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsIdMediaFormatGet) | **GET** /Syndication/api/v2/resources/tags/{id}/media.{format} | Get MediaItems for Tag
[**syndicationApiV2ResourcesTagsIdRelatedFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsIdRelatedFormatGet) | **GET** /Syndication/api/v2/resources/tags/{id}/related.{format} | Get related Tags by ID
[**syndicationApiV2ResourcesTagsIdSyndicateFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsIdSyndicateFormatGet) | **GET** /Syndication/api/v2/resources/tags/{id}/syndicate.{format} | Get MediaItems for Tag
[**syndicationApiV2ResourcesTagsTagLanguagesFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsTagLanguagesFormatGet) | **GET** /Syndication/api/v2/resources/tags/tagLanguages.{format} | Get TagLanguages
[**syndicationApiV2ResourcesTagsTagTypesFormatGet**](TagsApi.md#syndicationApiV2ResourcesTagsTagTypesFormatGet) | **GET** /Syndication/api/v2/resources/tags/tagTypes.{format} | Get MediaItems for Tag


<a name="syndicationApiV2ResourcesTagsFormatGet"></a>
# **syndicationApiV2ResourcesTagsFormatGet**
> List&lt;TagMarshallerWrapped&gt; syndicationApiV2ResourcesTagsFormatGet(sort, max, offset, name, nameContains, mediaId, typeId, typeName)

Get Tags

List of Tags

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
String name = "name_example"; // String | Return tags[s] matching the supplied name
String nameContains = "nameContains_example"; // String | Return tags which contain the supplied partial name.
Long mediaId = 789L; // Long | Return tags associated with the supplied media id.
Long typeId = 789L; // Long | Return tags belonging to the supplied tag type id.
String typeName = "typeName_example"; // String | Return tags belonging to the supplied tag type name.
try {
    List<TagMarshallerWrapped> result = apiInstance.syndicationApiV2ResourcesTagsFormatGet(sort, max, offset, name, nameContains, mediaId, typeId, typeName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| Return records starting at the offset index. | [optional]
 **name** | **String**| Return tags[s] matching the supplied name | [optional]
 **nameContains** | **String**| Return tags which contain the supplied partial name. | [optional]
 **mediaId** | **Long**| Return tags associated with the supplied media id. | [optional]
 **typeId** | **Long**| Return tags belonging to the supplied tag type id. | [optional]
 **typeName** | **String**| Return tags belonging to the supplied tag type name. | [optional]

### Return type

[**List&lt;TagMarshallerWrapped&gt;**](TagMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesTagsIdFormatGet"></a>
# **syndicationApiV2ResourcesTagsIdFormatGet**
> List&lt;TagMarshallerWrapped&gt; syndicationApiV2ResourcesTagsIdFormatGet(id)

Get Tag by ID

Information about a specific tag

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
Long id = 789L; // Long | The id of the record to look up
try {
    List<TagMarshallerWrapped> result = apiInstance.syndicationApiV2ResourcesTagsIdFormatGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsIdFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the record to look up |

### Return type

[**List&lt;TagMarshallerWrapped&gt;**](TagMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesTagsIdMediaFormatGet"></a>
# **syndicationApiV2ResourcesTagsIdMediaFormatGet**
> List&lt;MediaItemMarshallerWrapped&gt; syndicationApiV2ResourcesTagsIdMediaFormatGet(id, sort, max, offset)

Get MediaItems for Tag

MediaItem

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
Long id = 789L; // Long | The id of the tag to look up
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
try {
    List<MediaItemMarshallerWrapped> result = apiInstance.syndicationApiV2ResourcesTagsIdMediaFormatGet(id, sort, max, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsIdMediaFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the tag to look up |
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| Return records starting at the offset index. | [optional]

### Return type

[**List&lt;MediaItemMarshallerWrapped&gt;**](MediaItemMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesTagsIdRelatedFormatGet"></a>
# **syndicationApiV2ResourcesTagsIdRelatedFormatGet**
> List&lt;TagMarshallerWrapped&gt; syndicationApiV2ResourcesTagsIdRelatedFormatGet(id, sort, max, offset)

Get related Tags by ID

Information about related tags to a specific tag

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
Long id = 789L; // Long | The id of the tag to look up
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | Return records starting at the offset index.
try {
    List<TagMarshallerWrapped> result = apiInstance.syndicationApiV2ResourcesTagsIdRelatedFormatGet(id, sort, max, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsIdRelatedFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the tag to look up |
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| Return records starting at the offset index. | [optional]

### Return type

[**List&lt;TagMarshallerWrapped&gt;**](TagMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesTagsIdSyndicateFormatGet"></a>
# **syndicationApiV2ResourcesTagsIdSyndicateFormatGet**
> String syndicationApiV2ResourcesTagsIdSyndicateFormatGet(id, displayMethod)

Get MediaItems for Tag

MediaItem

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
Long id = 789L; // Long | The id of the record to look up
String displayMethod = "displayMethod_example"; // String | Method used to render an html request. Accepts one: [mv, list, feed]
try {
    String result = apiInstance.syndicationApiV2ResourcesTagsIdSyndicateFormatGet(id, displayMethod);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsIdSyndicateFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the record to look up |
 **displayMethod** | **String**| Method used to render an html request. Accepts one: [mv, list, feed] | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesTagsTagLanguagesFormatGet"></a>
# **syndicationApiV2ResourcesTagsTagLanguagesFormatGet**
> List&lt;TagLanguageMarshallerWrapped&gt; syndicationApiV2ResourcesTagsTagLanguagesFormatGet()

Get TagLanguages

List of Tag Languages

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
try {
    List<TagLanguageMarshallerWrapped> result = apiInstance.syndicationApiV2ResourcesTagsTagLanguagesFormatGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsTagLanguagesFormatGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;TagLanguageMarshallerWrapped&gt;**](TagLanguageMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesTagsTagTypesFormatGet"></a>
# **syndicationApiV2ResourcesTagsTagTypesFormatGet**
> List&lt;TagTypeMarshallerWrapped&gt; syndicationApiV2ResourcesTagsTagTypesFormatGet()

Get MediaItems for Tag

List of Types

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TagsApi;


TagsApi apiInstance = new TagsApi();
try {
    List<TagTypeMarshallerWrapped> result = apiInstance.syndicationApiV2ResourcesTagsTagTypesFormatGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagsApi#syndicationApiV2ResourcesTagsTagTypesFormatGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;TagTypeMarshallerWrapped&gt;**](TagTypeMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

