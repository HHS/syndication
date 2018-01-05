# CampaignsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**syndicationApiV2ResourcesCampaignsIdJsonGet**](CampaignsApi.md#syndicationApiV2ResourcesCampaignsIdJsonGet) | **GET** /Syndication/api/v2/resources/campaigns/{id}.json | Get Campaign by ID
[**syndicationApiV2ResourcesCampaignsIdMediaJsonGet**](CampaignsApi.md#syndicationApiV2ResourcesCampaignsIdMediaJsonGet) | **GET** /Syndication/api/v2/resources/campaigns/{id}/media.json | Get MediaItems by Campaign ID
[**syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet**](CampaignsApi.md#syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet) | **GET** /Syndication/api/v2/resources/campaigns/{id}/syndicate.{format} | Get MediaItems for Campaign
[**syndicationApiV2ResourcesCampaignsJsonGet**](CampaignsApi.md#syndicationApiV2ResourcesCampaignsJsonGet) | **GET** /Syndication/api/v2/resources/campaigns.json | Get Campaigns


<a name="syndicationApiV2ResourcesCampaignsIdJsonGet"></a>
# **syndicationApiV2ResourcesCampaignsIdJsonGet**
> CampaignWrapped syndicationApiV2ResourcesCampaignsIdJsonGet(id)

Get Campaign by ID

Information about a specific campaign

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CampaignsApi;


CampaignsApi apiInstance = new CampaignsApi();
Long id = 789L; // Long | The id of the record to look up
try {
    CampaignWrapped result = apiInstance.syndicationApiV2ResourcesCampaignsIdJsonGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignsApi#syndicationApiV2ResourcesCampaignsIdJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the record to look up |

### Return type

[**CampaignWrapped**](CampaignWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesCampaignsIdMediaJsonGet"></a>
# **syndicationApiV2ResourcesCampaignsIdMediaJsonGet**
> MediaItemWrapped syndicationApiV2ResourcesCampaignsIdMediaJsonGet(id, sort, max, offset)

Get MediaItems by Campaign ID

Campaign Listings

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CampaignsApi;


CampaignsApi apiInstance = new CampaignsApi();
Long id = 789L; // Long | The id of the campaign to find media items for
String sort = "sort_example"; // String | The name of the property to which sorting will be applied
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | The offset of the records set to return for pagination
try {
    MediaItemWrapped result = apiInstance.syndicationApiV2ResourcesCampaignsIdMediaJsonGet(id, sort, max, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignsApi#syndicationApiV2ResourcesCampaignsIdMediaJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the campaign to find media items for |
 **sort** | **String**| The name of the property to which sorting will be applied | [optional]
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| The offset of the records set to return for pagination | [optional]

### Return type

[**MediaItemWrapped**](MediaItemWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet"></a>
# **syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet**
> SyndicateMarshallerWrapped syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet(id, displayMethod)

Get MediaItems for Campaign

MediaItem

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CampaignsApi;


CampaignsApi apiInstance = new CampaignsApi();
Long id = 789L; // Long | The id of the record to look up
String displayMethod = "displayMethod_example"; // String | Method used to render an html request. Accepts one: [mv, list, feed]
try {
    SyndicateMarshallerWrapped result = apiInstance.syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet(id, displayMethod);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignsApi#syndicationApiV2ResourcesCampaignsIdSyndicateFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The id of the record to look up |
 **displayMethod** | **String**| Method used to render an html request. Accepts one: [mv, list, feed] | [optional]

### Return type

[**SyndicateMarshallerWrapped**](SyndicateMarshallerWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="syndicationApiV2ResourcesCampaignsJsonGet"></a>
# **syndicationApiV2ResourcesCampaignsJsonGet**
> CampaignWrapped syndicationApiV2ResourcesCampaignsJsonGet(max, offset, sort)

Get Campaigns

Media Listings for a specific campaign

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CampaignsApi;


CampaignsApi apiInstance = new CampaignsApi();
Integer max = 56; // Integer | The maximum number of records to return
Integer offset = 56; // Integer | The offset of the records set to return for pagination
String sort = "sort_example"; // String | * Set of fields to sort the records by.
try {
    CampaignWrapped result = apiInstance.syndicationApiV2ResourcesCampaignsJsonGet(max, offset, sort);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignsApi#syndicationApiV2ResourcesCampaignsJsonGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **max** | **Integer**| The maximum number of records to return | [optional]
 **offset** | **Integer**| The offset of the records set to return for pagination | [optional]
 **sort** | **String**| * Set of fields to sort the records by. | [optional]

### Return type

[**CampaignWrapped**](CampaignWrapped.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

