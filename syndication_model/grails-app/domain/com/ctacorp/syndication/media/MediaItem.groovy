/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package com.ctacorp.syndication.media

import com.ctacorp.grails.swagger.annotations.*
import com.ctacorp.syndication.AlternateImage
import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.metric.MediaMetric
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.commons.util.Hash

@Definition
class MediaItem {
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String name
    @DefinitionProperty(name = "mediaType", type=DefinitionPropertyType.STRING)//holder for mediaType for swagger spec
    Set metrics
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String description
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String sourceUrl
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String customAttributionUrl
    @DefinitionProperty(name = "campaigns", type=DefinitionPropertyType.ARRAY, reference = 'Campaign')//holder for campaigns for swagger specs
    String sourceUrlHash
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String targetUrl
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String customThumbnailUrl
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String customPreviewUrl
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateContentAuthored
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateContentUpdated
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateContentPublished
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateContentReviewed
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateSyndicationCaptured    = new Date()
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateSyndicationUpdated     = new Date()
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = 'date')
    Date dateSyndicationVisible
    @DefinitionProperty(type=DefinitionPropertyType.OBJECT, reference = 'Language')
    Language language
    boolean active = true
    boolean visibleInStorefront = true
    boolean manuallyManaged = true
    boolean disableIframe = false
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String externalGuid
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String hash
    @DefinitionProperty(type=DefinitionPropertyType.OBJECT, reference = 'Source')
    Source source
    StructuredContentType structuredContentType
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String createdBy
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String foreignSyndicationAPIUrl

    def cmsService
    def mediaPreviewThumbnailJobService

    static transients = ['cmsService', 'mediaPreviewThumbnailJobService']

    enum StructuredContentType{
        BLOG_POSTING("BlogPosting"),                // https://schema.org/BlogPosting
        NEWS_ARTICLE("NewsArticle"),                // https://schema.org/NewsArticle
        ARTICLE("Article")                          // https://schema.org/Article

        String prettyName

        StructuredContentType(String prettyName){
            this.prettyName = prettyName
        }
    }

    @DefinitionProperty(name = "extendedAttributes", type=DefinitionPropertyType.ARRAY, reference = 'ExtendedAttribute')
    static hasMany = [
        campaigns: Campaign,
        metrics: MediaMetric,
        alternateImages:AlternateImage,
        extendedAttributes:ExtendedAttribute
    ]

    static belongsTo = Campaign

    static constraints = {
        name                        nullable: false,    blank: false,               maxSize: 255
        description                 nullable: true,     blank: false,               maxSize: 2000
        sourceUrl                   nullable: false,    blank: false,   url:true,   maxSize: 2000
        customAttributionUrl        nullable: true,     url:true,       maxSize: 2000
        sourceUrlHash               nullable: false,    blank: false,   unique: true
        targetUrl                   nullable: true,     blank:false,    url:true,   maxSize: 2000
        customThumbnailUrl          nullable: true,     blank:false,    url:true,   maxSize: 2000
        customPreviewUrl            nullable: true,     blank:false,    url:true,   maxSize: 2000
        dateContentAuthored         nullable: true
        dateContentUpdated          nullable: true
        dateContentPublished        nullable: true
        dateContentReviewed         nullable: true
        dateSyndicationCaptured     nullable: false
        dateSyndicationUpdated      nullable: false
        dateSyndicationVisible      nullable: true
        language                    nullable: false
        active()
        visibleInStorefront()
        manuallyManaged()
        disableIframe()
        externalGuid                nullable: true,                                 maxSize: 255
        hash                        nullable: true,     blank: false,               maxSize: 255
        source                      nullable: false
        structuredContentType       nullable: true
        createdBy                   nullable: true,     blank: false,               maxSize: 255
        foreignSyndicationAPIUrl    nullable: true,     url:true,                   maxSize: 255
    }


    static mapping = {
        description type: 'text'
    }

    String toString() {
        "$name"
    }

    String getIdAndName(){
        "${id} - ${name}"
    }

    def beforeValidate() {
        if(sourceUrl){
            sourceUrlHash = Hash.md5(sourceUrl)
        }
    }

    def beforeUpdate () {
        dateSyndicationUpdated = new Date()
    }

    def afterInsert () {

        if(mediaPreviewThumbnailJobService) {
            mediaPreviewThumbnailJobService.delayedPreviewAndThumbnailGeneration(Long.valueOf(id))
        }
    }

    def afterUpdate () {

        if(cmsService) {
            cmsService.flushCacheForMediaItemUpdate(id)
            cmsService.sendDelayedMessage(new Message(messageType:MessageType.UPDATE, mediaId:id))
        }

        if(mediaPreviewThumbnailJobService) {
            mediaPreviewThumbnailJobService.delayedPreviewAndThumbnailGeneration(Long.valueOf(id))
        }
    }

    def beforeDelete() {

        if(cmsService) {
            cmsService.flushCacheForMediaItemUpdate(id)
            cmsService.sendDelayedMessage(new Message(messageType: MessageType.DELETE, mediaId: id))
        }
    }

    private static transient mediaTypeMapping = [
            "collection"        : "com.ctacorp.syndication.media.Collection",
            "faq"               : "com.ctacorp.syndication.media.FAQ",
            "html"              : "com.ctacorp.syndication.media.Html",
            "image"             : "com.ctacorp.syndication.media.Image",
            "infographic"       : "com.ctacorp.syndication.media.Infographic",
            "pdf"               : "com.ctacorp.syndication.media.PDF",
            "questionandanswer" : "com.ctacorp.syndication.media.QuestionAndAnswer",
            "tweet"             : "com.ctacorp.syndication.media.Tweet",
            "video"             : "com.ctacorp.syndication.media.Video",
            "blog_posting"      : "STRUCTURED-BLOG_POSTING-com.ctacorp.syndication.media.Html",
            "news_article"      : "STRUCTURED-NEWS_ARTICLE-com.ctacorp.syndication.media.Html",
            "article"           : "STRUCTURED-ARTICLE-com.ctacorp.syndication.media.Html"
    ]

    static namedQueries = {
        //Authored ---------------------------------------------------------------------
        contentAuthoredIs { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date nextDay = day + 1
            between 'dateContentAuthored', day, nextDay
        }

        contentAuthoredSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateContentAuthored', day, now
        }

        contentAuthoredBeforeDate { String date ->
            Date cutoff = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", date).clearTime()
            lt 'dateContentAuthored', cutoff
        }

        contentAuthoredInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateContentAuthored', start, end
        }
        //Updated ---------------------------------------------------------------------
        contentUpdatedIs { String dateContentUpdated ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateContentUpdated).clearTime()
            Date nextDay = day + 1
            between 'dateContentUpdated', day, nextDay
        }

        contentUpdatedSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateContentUpdated', day, now
        }

        contentUpdatedBeforeDate { String date ->
            Date cutoff = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", date).clearTime()
            lt 'dateContentUpdated', cutoff
        }

        contentUpdatedInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateContentUpdated', start, end
        }
        //Published ---------------------------------------------------------------------
        contentPublishedIs { String dateContentPublished ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateContentPublished).clearTime()
            Date nextDay = day + 1
            between 'dateContentPublished', day, nextDay
        }

        contentPublishedSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateContentPublished', day, now
        }

        contentPublishedBeforeDate { String date ->
            Date cutoff = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", date).clearTime()
            lt 'dateContentPublished', cutoff
        }

        contentPublishedInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateContentPublished', start, end
        }

        //Reviewed ---------------------------------------------------------------------
        contentReviewedIs { String dateContentReviewed ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateContentReviewed).clearTime()
            Date nextDay = day + 1
            between 'dateContentReviewed', day, nextDay
        }

        contentReviewedSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateContentReviewed', day, now
        }

        contentReviewedBeforeDate { String date ->
            Date cutoff = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", date).clearTime()
            lt 'dateContentReviewed', cutoff
        }

        contentReviewedInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateContentReviewed', start, end
        }

        //Captured --------------------------------------------------------------------
        syndicationCapturedIs { String dateSyndicationCaptured ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateSyndicationCaptured).clearTime()
            Date nextDay = day + 1
            between 'dateSyndicationCaptured', day, nextDay
        }

        syndicationCapturedSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateSyndicationCaptured', day, now
        }

        syndicationCapturedBeforeDate { String date ->
            Date cutoff = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", date).clearTime()
            lt 'dateSyndicationCaptured', cutoff
        }

        syndicationCapturedInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateSyndicationCaptured', start, end
        }

        //Updated -------------------------------------------------------------------
        syndicationUpdatedIs { String dateSyndicationUpdated ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateSyndicationUpdated).clearTime()
            Date nextDay = day + 1
            between 'dateSyndicationUpdated', day, nextDay
        }

        syndicationUpdatedSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateSyndicationUpdated', day, now
        }

        syndicationUpdatedBeforeDate { String date ->
            Date cutoff = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", date).clearTime()
            lt 'dateSyndicationUpdated', cutoff
        }

        syndicationUpdatedInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateSyndicationUpdated', start, end
        }

        //Visible ---------------------------------------------------------------------
        syndicationVisibleIs { String dateSyndicationVisible ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateSyndicationVisible).clearTime()
            Date nextDay = day + 1
            between 'dateSyndicationVisible', day, nextDay
        }

        syndicationVisibleSinceDate { String startDate ->
            Date day = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date now = new Date()
            between 'dateSyndicationVisible', day, now
        }

        syndicationVisibleBeforeDate { String date ->
            Date cutoff = Date.parse("E MMM dd H:m:s z yyyy", date)
            or{
                le 'dateSyndicationVisible', cutoff
                isNull('dateSyndicationVisible')
            }
        }

        syndicationVisibleInRange { String startDate, String endDate ->
            Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", startDate).clearTime()
            Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", endDate).clearTime() + 1
            between 'dateSyndicationVisible', start, end
        }
        // Field selectors ---------------------------------------------------------------------
        idIs { Long id ->
            idEq id
        }

        mediaTypes { params ->
            if(params.mediaTypes){
                def resolveTypes = { String theTypes ->
                    
                    def t = theTypes.toLowerCase().replace(" ","").split(",")
                    def types = []
                    
                    t.each { type ->

                        def mapping = mediaTypeMapping[type]
                        if(mapping) {
                            types << mapping
                        }
                    }

                    types
                }

                //resolve a short class name (the simpleName) with it's full classpath mapping defined in this class (mediaTypeMapping)
                def set = resolveTypes(params.mediaTypes)

                //Some of the entries in the set will be STRUCTURED types, which are really just HTML types with an additional field
                //added as a patch-in rather than a whole new syndication type
                //These will have names in the format "STRUCTURED-STRUCTURED_MEDIA_TYPE_HERE-com.ctacorp.syndication.media.Html",
                //Find all items in this format, and collect them
                def structuredTypes = set.findAll{ it.startsWith("STRUCTURED-") }
                if(structuredTypes){
                    //If twe have structured types, the class is the third value delimited by hyphens ( - )
                    //We need both the class and the structuredContentType field to match
                    and {
                        inList 'class', set - structuredTypes + structuredTypes.collect { it.split("-")[2] }.unique()
                        inList 'structuredContentType', structuredTypes.collect{ StructuredContentType."${it.split("-")[1]}" }
                    }
                } else{
                    //Otherwise, we want only items of the class without a structured type
                    and {
                        inList 'class', set
                        isNull 'structuredContentType'
                    }
                }
            }
        }

        restrictToSet { String inList ->
            def ids = inList.split(",")
            def idsl = []
            ids.each {
                idsl << it.toLong()
            }
            'in'('id', idsl)
        }

        nameIs { String name ->
            eq 'name', name, [ignoreCase: true]
        }

        nameContains { String name ->
            ilike 'name', "%${name}%"
        }

        descriptionContains { String description ->
            ilike 'description', "%${description}%"
        }

        sourceUrlContains { String sourceUrl ->
            ilike 'sourceUrl', "%${sourceUrl}%"
        }

        sourceUrlIs { String sourceUrl ->
            eq 'sourceUrl', sourceUrl, [ignoreCase: true]
        }

        targetUrlContains { String targetUrl ->
            ilike 'targetUrl', "%${targetUrl}%"
        }

        targetUrlIs { String targetUrl ->
            eq 'targetUrl', targetUrl, [ignoreCase: true]
        }

        customThumbnailUrlContains { String customThumbnailUrl ->
            ilike 'customThumbnailUrl', "%${customThumbnailUrl}%"
        }

        customThumbnailUrlIs { String customThumbnailUrl ->
            eq 'customThumbnailUrl', customThumbnailUrl, [ignoreCase: true]
        }

        customPreviewUrlContains { String customPreviewUrl ->
            ilike 'customPreviewUrl', "%${customPreviewUrl}%"
        }

        customPreviewUrlIs { String customPreviewUrl ->
            eq 'customPreviewUrl', customPreviewUrl, [ignoreCase: true]
        }

        languageIdIs { Long languageId ->
            language {
                idEq languageId
            }
        }

        languageIdIsContained { ArrayList languageIds ->
            def idsl = []
            languageIds.each {
                idsl << it.toLong()
            }
            language {
                'in'('id', idsl)
            }
        }

        languageNameIs { String languageName ->
            language {
                eq "name", languageName, [ignoreCase: true]
            }
        }

        languageIsoCodeIs { String value ->
            language {
                eq "isoCode", value, [ignoreCase: true]
            }
        }

        hashIs { String hash ->
            eq 'hash', hash, [ignoreCase: true]
        }

        hashContains { String hash ->
            ilike('hash', "%${hash}%")
        }

        sourceIdIs { Long sourceId ->
            source {
                idEq sourceId
            }
        }

        sourceIdIsContained { ArrayList sourceIds ->
            def idsl = []
            sourceIds.each {
                idsl << it.toLong()
            }
            source {
                'in'('id', idsl)
            }
        }

        sourceNameIs { String sourceName ->
            source {
                eq "name", sourceName, [ignoreCase: true]
            }
        }

        sourceNameContains { String sourceName ->
            source {
                ilike "name", "%${sourceName}%"
            }
        }

        sourceAcronymIs { String acronym ->
            source {
                eq "acronym", acronym, [ignoreCase: true]
            }
        }

        sourceAcronymContains { String acronym ->
            source {
                ilike "acronym", "%${acronym}%"
            }
        }

        createdByContains { String createdBy ->
            ilike 'createdBy', "%${createdBy}%"
        }

        activeIs { Boolean active ->
                eq 'active', active
            }

        visibleInStorefrontIs { Boolean visibleInStorefront ->
                eq 'visibleInStorefront', visibleInStorefront
            }


        //Media for Campaign ----------------------------------------------------------
        mediaForCampaign { Long campaignId, String sort = null ->
            campaigns{
                idEq campaignId
            }
            if (sort) {
                multiSort(sort)
            }
        }

        //Media for a collection -------------------------------------------------------
        mediaForCollection { Long collectionId ->
            def collection = Collection.get(collectionId)
            'in'('id', collection.mediaItems*.id)
        }

        //Sort Order -------------------------------------------------------------------
        multiSort{ String sortQuery = "id" ->
            def parts = sortQuery.split(",")
            parts.each{ part ->
                if(part.startsWith("-")){
                    String field = part[1..-1]
                    order(field, 'desc')
                } else{
                    order(part, 'asc')
                }
            }
        }

        facetedSearch { params ->
            and {
                //Date handling ----------------------------------------------------------------

                if (params.dateContentAuthored) {
                    contentAuthoredIs((String) params.dateContentAuthored)
                }
                if (params.contentAuthoredSinceDate) {
                    contentAuthoredSinceDate((String) params.contentAuthoredSinceDate)
                }
                if (params.contentAuthoredBeforeDate) {
                    contentAuthoredBeforeDate((String) params.contentAuthoredBeforeDate)
                }
                if (params.contentAuthoredInRange) {
                    def parts = params.contentAuthoredInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    contentAuthoredInRange(from, to)
                }
                if (params.dateContentUpdated) {
                    contentUpdatedIs((String) params.dateContentUpdated)
                }
                if (params.contentUpdatedSinceDate) {
                    contentUpdatedSinceDate((String) params.contentUpdatedSinceDate)
                }
                if (params.contentUpdatedBeforeDate) {
                    contentUpdatedBeforeDate((String) params.contentUpdatedBeforeDate)
                }
                if (params.contentUpdatedInRange) {
                    def parts = params.contentUpdatedInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    contentUpdatedInRange(from, to)
                }
                if (params.dateContentPublished) {
                    contentPublishedIs((String) params.dateContentPublished)
                }
                if (params.contentPublishedSinceDate) {
                    contentPublishedSinceDate((String) params.contentPublishedSinceDate)
                }
                if (params.contentPublishedBeforeDate) {
                    contentPublishedBeforeDate((String) params.contentPublishedBeforeDate)
                }
                if (params.contentPublishedInRange) {
                    def parts = params.contentPublishedInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    contentPublishedInRange(from, to)
                }
                if (params.dateContentReviewed) {
                    contentReviewedIs((String) params.dateContentReviewed)
                }
                if (params.contentReviewedSinceDate) {
                    contentReviewedSinceDate((String) params.contentReviewedSinceDate)
                }
                if (params.contentReviewedBeforeDate) {
                    contentReviewedBeforeDate((String) params.contentReviewedBeforeDate)
                }
                if (params.contentReviewedInRange) {
                    def parts = params.contentReviewedInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    contentReviewedInRange(from, to)
                }
                if (params.dateSyndicationCaptured) {
                    syndicationCapturedIs((String) params.dateSyndicationCaptured)
                }
                if (params.syndicationCapturedSinceDate) {
                    syndicationCapturedSinceDate((String) params.syndicationCapturedSinceDate)
                }
                if (params.syndicationCapturedBeforeDate) {
                    syndicationCapturedBeforeDate((String) params.syndicationCapturedBeforeDate)
                }
                if (params.syndicationCapturedInRange) {
                    def parts = params.syndicationCapturedInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    syndicationCapturedInRange(from, to)
                }
                if (params.dateSyndicationUpdated) {
                    syndicationUpdatedIs((String) params.dateSyndicationUpdated)
                }
                if (params.syndicationUpdatedSinceDate) {
                    syndicationUpdatedSinceDate((String) params.syndicationUpdatedSinceDate)
                }
                if (params.syndicationUpdatedBeforeDate) {
                    syndicationUpdatedBeforeDate((String) params.syndicationUpdatedBeforeDate)
                }
                if (params.syndicationUpdatedInRange) {
                    def parts = params.syndicationUpdatedInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    syndicationUpdatedInRange(from, to)
                }
                if (params.syndicationVisible) {
                    syndicationVisibleIs((String) params.dateSyndicationVisible)
                }
                if (params.syndicationVisibleSinceDate) {
                    syndicationVisibleSinceDate((String) params.syndicationVisibleSinceDate)
                }
                if (params.syndicationVisibleBeforeDate) {
                    syndicationVisibleBeforeDate((String) params.syndicationVisibleBeforeDate)
                }
                if (params.syndicationVisibleInRange) {
                    def parts = params.syndicationVisibleInRange.split(",")
                    String from = parts[0]
                    String to = parts[1]
                    syndicationVisibleInRange(from, to)
                }
                // ------------------------------------------------------------------
                if (params.id) {
                    idIs(params.id.toLong())
                }
                if (params.collectionId) {
                    mediaForCollection(params.collectionId.toLong())
                }
                if (params.mediaTypes) {
                    mediaTypes((Map) params)
                }
                if (params.restrictToSet) {
                    restrictToSet((String) params.restrictToSet)
                }
                if (params.name) {
                    nameIs((String) params.name)
                }
                if (params.nameContains) {
                    nameContains((String) params.nameContains)
                }
                if (params.descriptionContains) {
                    descriptionContains((String) params.descriptionContains)
                }
                if (params.sourceUrlContains) {
                    sourceUrlContains((String) params.sourceUrlContains)
                }
                if (params.sourceUrl) {
                    sourceUrlIs((String) params.sourceUrl)
                }
                if (params.targetUrlContains) {
                    targetUrlContains((String) params.targetUrlContains)
                }
                if (params.targetUrl) {
                    targetUrlIs((String) params.targetUrl)
                }
                if (params.customThumbnailUrlContains) {
                    customThumbnailUrlContains((String) params.customThumbnailUrlContains)
                }
                if (params.customThumbnailUrl) {
                    customThumbnailUrlIs((String) params.customThumbnailUrl)
                }
                if (params.customPreviewUrlContains) {
                    customPreviewUrlContains((String) params.customPreviewUrlContains)
                }
                if (params.customPreviewUrl) {
                    customPreviewUrlIs((String) params.customPreviewUrl)
                }
                if (params.hash) {
                    hashIs((String) params.hash)
                }
                if (params.hashContains) {
                    hashContains((String) params.hashContains)
                }

                if (params.active) {
                    activeIs((Boolean) params.active)
                }
                if (params.visibleInStorefront) {
                    visibleInStorefrontIs((Boolean) params.visibleInStorefront)
                }

                // Collaborators
                if (params.sourceId) {
                    sourceIdIs(params.sourceId.toLong())
                }
                if (params.sourceIdIsContained) {
                    sourceIdIsContained((ArrayList) params.sourceIdIsContained)
                }
                if (params.sourceName) {
                    sourceNameIs((String) params.sourceName)
                }
                if (params.sourceNameContains) {
                    sourceNameContains((String) params.sourceNameContains)
                }
                if (params.sourceAcronym) {
                    sourceAcronymIs((String) params.sourceAcronym)
                }
                if (params.sourceAcronymContains) {
                    sourceAcronymContains((String) params.sourceAcronymContains)
                }
                if(params.createdByContains) {
                    createdByContains((String) params.createdByContains)
                }
                if (params.languageId) {
                    languageIdIs(params.languageId.toLong())
                }
                if (params.languageIdIsContained) {
                    languageIdIsContained((ArrayList) params.languageIdIsContained)
                }
                if (params.languageName) {
                    languageNameIs((String) params.languageName)
                }
                if (params.languageIsoCode) {
                    languageIsoCodeIs((String) params.languageIsoCode)
                }

                if (params.mediaForCampaign) {
                    mediaForCampaign((Long) params.mediaForCampaign.id, (String) params.mediaForCampaign.sort)
                }
            }

            if (params.sort) {
                multiSort(params.sort)
            }
        }
    }
}
