package com.ctacorp.syndication.analytics

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.Analytics;
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

    static getDashboardOverviewData(Date start = new Date()-30){
        if(start > new Date()){
            return [error:"Selected date is in the future!"]
        }
        String profileId = Holders.config.google.analytics.profileId
        executeQuery(profileId, start, new Date(), "overview")
    }

    static executeQuery(String profileId, Date start = new Date()-7, Date end = new Date(), String query){
        Analytics analytics = getAnalytics()
        Date today = new Date()
        if(start > today){
            start = today
        }

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
                        .setMaxResults(50)
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
            System.err.println("No Webproperties found");
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

    private static Credential authorize(){
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
}
