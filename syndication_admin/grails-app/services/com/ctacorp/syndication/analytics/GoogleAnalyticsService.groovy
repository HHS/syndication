package com.ctacorp.syndication.analytics

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analytics.Analytics
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.Webproperties
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import grails.util.Holders;

class GoogleAnalyticsService {
    static transactional = false

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private static HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".syndication/analytics/analytics_data_store");
    private static FileDataStoreFactory dataStoreFactory;

    static executeQuery(String profileId, def start = new Date()-7, def end = new Date(), String query, params = [:]){
        Analytics analytics = getAnalytics()

        switch(query){
            case "pageViews":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:pageviews") // Metrics.
                        .setMaxResults(25)
                        .execute();
            case "overview":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:sessions,ga:pageviews,ga:avgTimeOnPage") // Metrics.
                        .setFilters("${params.mediaFilters}")
                        .setMaxResults(50)
                        .execute();
            case "sources":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:sessions,ga:pageviews") //Metrics
                        .setDimensions("ga:source,ga:medium")
                        .setSort("-ga:sessions")
                        .setMaxResults(50)
                        .execute();
            case "endUserViews":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:pageviews") //Metrics
                        .setDimensions("ga:source,ga:medium")
                        .setFilters("ga:source!=(direct)")
                        .setMaxResults(50)
                        .execute();
            case "mobileViews":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:sessions,ga:pageviews") //Metrics
                        .setSegment("gaid::-11")
                        .setMaxResults(50)
                        .execute();
            case "medaitemsOnEndUserSites":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:sessions,ga:pageviews") //Metrics
                        .setDimensions("ga:source,ga:dimension2")
                        .setFilters("ga:source!=(direct)")
                        .setMaxResults(50)
                        .execute();
            case "viewsByLocation":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start.format("yyyy-MM-dd"), // Start date.
                        end.format("yyyy-MM-dd"), // End date.
                        "ga:pageviews") //Metrics
                        .setDimensions("ga:region,ga:city,ga:dimension2,ga:longitude,ga:latitude")
                        .setFilters("ga:city!=(not set);ga:region!=(not set)${params.mediaFilters}")
                        .setMaxResults(10000)
                        .setStartIndex(params.startIndex)
                        .execute();
            case "whosEmbedding":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        start, // Start date.
                        end, // End date.
                        "ga:pageviews") //Metrics
                        .setDimensions("ga:hostname")
                        .setFilters("${params.mediaFilters}")
                        .setSort("-ga:pageviews")
                        .setMaxResults(params.size ?: 5)
                        .execute();
            case "generalTotalViews":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        "365daysAgo", // Start date.
                        "today", // End date.
                        "ga:pageviews") // Metrics.
                        .setDimensions("ga:yearMonth")
                        .setFilters("${params.mediaFilters}")
                        .execute();
            case "generalTotalMobileViews":
                return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                        "365daysAgo", // Start date.
                        "today", // End date.
                        "ga:pageviews") // Metrics.
                        .setDimensions("ga:yearMonth")
                        .setSegment("gaid::-11")
                        .setFilters("${params.mediaFilters}")
                        .execute();
        }
    }

    static getAccounts() throws IOException {
        Analytics analytics = getAnalytics()
        String profileId = null;

        // Query accounts collection.
        Accounts accounts = analytics.management().accounts().list().execute();

        accounts
    }

    static getWebProperties(String accountId){
        Webproperties webproperties = analytics.management().webproperties().list(accountId).execute();

        if (webproperties.getItems().isEmpty()) {
            println("No Webproperties found")
        } else {
            return webproperties.getItems()
        }
    }

    static getProfiles(String accountId, String webPropertyId){
        analytics.management().profiles().list(accountId, webPropertyId).execute();
    }

    private static Analytics getAnalytics(){
        Credential credential = authorize();

        return new Analytics.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("SyndicationApi").build();
    }

    private static Credential authorize() {
        String emailAddress = Holders.config.google.analytics.emailAddress

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(emailAddress)
                .setServiceAccountPrivateKeyFromP12File(new File("${System.getProperty('user.home')}/.syndication/analytics/SyndicationIntegration-f5abe9084219.p12"))
                .setServiceAccountScopes(["https://www.googleapis.com/auth/analytics.readonly"])
                .build();
        credential
    }

    private static Credential authorizeForAccessToken() {
        if(!dataStoreFactory){
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new File("${System.getProperty('user.home')}/.syndication/analytics/analytics_data_store/client_secrets.json").newDataInputStream()));

        if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=analytics "
                            + "into analytics-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY)).setDataStoreFactory(dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }


    def restrictItemsToSize(def items, int size) {
        def tempItems = []
        def topItems = []
        if(items.size() > size){
            tempItems.add(items[0])
            //sorting list
            for(int i = 1;i<items.size();i++) {
                def count = 0
                def index = 0
                tempItems.each{
                    if((items[i][1] as int) > (it[1] as int)){
                        index = count
                        count -- //keeps count the same until the end
                    } else if(count == (tempItems.size() - 1)) {
                        index = -1
                    }
                    count ++
                }
                if(index == -1 && tempItems.size() <= i) {
                    tempItems.add(items[i])
                } else if(index <= size) {
                    tempItems.add(index,items[i])
                }
            }
            //trimming list to size
            for(int i = 0;i<size;i++){
                topItems[i] = tempItems[i]
            }
        }
        return topItems
    }

    def mergeResults(def items, def newItems) {
        boolean addToList = true
        newItems.each{ newItem ->
            items.each{oldItem ->
                if(oldItem[0] == newItem[0]){
                    oldItem[1] = (oldItem[1] as int) + (newItem[1] as int)
                    addToList = false
                }
            }
            if(addToList){
                items.add(newItem)
            }
            addToList = true
        }

        return items
    }

    def formatResponseDate(def items) {
        items.each{item ->
            def year = item[0].toString().substring(0,4)
            def month = item[0].toString().substring(4,6)
            switch(month) {
                case "01":month = "Jan"
                    break;
                case "02":month = "Feb"
                    break;
                case "03":month = "Mar"
                    break;
                case "04":month = "Apr"
                    break;
                case "05":month = "May"
                    break;
                case "06":month = "Jun"
                    break;
                case "07":month = "Jul"
                    break;
                case "08":month = "Aug"
                    break;
                case "09":month = "Sep"
                    break;
                case "10":month = "Oct"
                    break;
                case "11":month = "Nov"
                    break;
                case "12":month = "Dec"
                    break;
            }
            item[0] = month + ", " + year
        }
        items
    }

}
