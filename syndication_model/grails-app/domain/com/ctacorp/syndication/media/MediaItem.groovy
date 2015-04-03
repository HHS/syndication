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
import com.ctacorp.syndication.metric.MediaMetric
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail

@Model(id="MediaItem", properties = [
    @ModelProperty(propertyName = "id",                      attributes = [@PropertyAttribute(type = "integer",  format = "int64",  required = true)]),
    @ModelProperty(propertyName = "name",                    attributes = [@PropertyAttribute(type = "string",                      required = true)]),
    @ModelProperty(propertyName = "mediaType",               attributes = [@PropertyAttribute(type = "string",                      required = true)]),
    @ModelProperty(propertyName = "sourceUrl",               attributes = [@PropertyAttribute(type = "string",                      required = true)]),
    @ModelProperty(propertyName = "targetUrl",               attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "customThumbnailUrl",      attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "customPreviewUrl",        attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "dateSyndicationCaptured", attributes = [@PropertyAttribute(type = "string",    format = "date",  required = true)]),
    @ModelProperty(propertyName = "dateSyndicationUpdated",  attributes = [@PropertyAttribute(type = "string",    format = "date",  required = true)]),
    @ModelProperty(propertyName = "dateSyndicationVisible",  attributes = [@PropertyAttribute(type = "string",    format = "date",  required = true)]),
    @ModelProperty(propertyName = "language",                attributes = [@PropertyAttribute(type = "Language",                    required = true)]),
    @ModelProperty(propertyName = "source",                  attributes = [@PropertyAttribute(type = "Source",                      required = true)]),
    @ModelProperty(propertyName = "description",             attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "dateContentAuthored",     attributes = [@PropertyAttribute(type = "string",    format = "date")]),
    @ModelProperty(propertyName = "dateContentUpdated",      attributes = [@PropertyAttribute(type = "string",    format = "date")]),
    @ModelProperty(propertyName = "dateContentPublished",    attributes = [@PropertyAttribute(type = "string",    format = "date")]),
    @ModelProperty(propertyName = "dateContentReviewed",     attributes = [@PropertyAttribute(type = "string",    format = "date")]),
    @ModelProperty(propertyName = "externalGuid",            attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "hash",                    attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "extendedAttributes",      attributes = [@PropertyAttribute(type = "Map")])
])
class MediaItem {
    String name
    String description
    String sourceUrl
    String targetUrl
    String customThumbnailUrl
    String customPreviewUrl
    Date dateContentAuthored
    Date dateContentUpdated
    Date dateContentPublished
    Date dateContentReviewed
    Date dateSyndicationCaptured    = new Date()
    Date dateSyndicationUpdated     = new Date()
    Date dateSyndicationVisible
    Language language
    boolean active = true
    boolean visibleInStorefront = true
    boolean manuallyManaged = true
    String externalGuid
    String hash
    Source source
    Set metrics

    static hasOne = [
       mediaPreview: MediaPreview,
       mediaThumbnail: MediaThumbnail
    ]

    static hasMany = [
        campaigns: Campaign,
        metrics: MediaMetric,
        alternateImages:AlternateImage,
        extendedAttributes:ExtendedAttribute
    ]
    static belongsTo = Campaign

    static constraints = {
        name                    nullable: false,    blank: false,               maxSize: 255
        description             nullable: true,     blank: false,               maxSize: 2000
        sourceUrl               nullable: false,    blank: false,   url:true,   maxSize: 2000
        targetUrl               nullable: true,     blank:false,    url:true,   maxSize: 2000
        customThumbnailUrl      nullable: true,     blank:false,    url:true,   maxSize: 2000
        customPreviewUrl        nullable: true,     blank:false,    url:true,   maxSize: 2000
        dateContentAuthored     nullable: true
        dateContentUpdated      nullable: true
        dateContentPublished    nullable: true
        dateContentReviewed     nullable: true
        dateSyndicationCaptured nullable: false
        dateSyndicationUpdated  nullable: false
        dateSyndicationVisible  nullable: true
        language                nullable: false
        active()
        visibleInStorefront()
        manuallyManaged()
        externalGuid            nullable: true,                                 maxSize: 255
        hash                    nullable: true,     blank: false,               maxSize: 255
        source                  nullable: false
        mediaPreview            nullable: true
        mediaThumbnail          nullable: true
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

    def beforeUpdate () {
        dateSyndicationUpdated = new Date()
    }

    private static transient mediaTypeMapping = [
        "audio" : "com.ctacorp.syndication.media.Audio",
        "collection" : "com.ctacorp.syndication.media.Collection",
        "html" : "com.ctacorp.syndication.media.Html",
        "image" : "com.ctacorp.syndication.media.Image",
        "infographic" : "com.ctacorp.syndication.media.Infographic",
        "periodical" : "com.ctacorp.syndication.media.Periodical",
        "socialmedia" : "com.ctacorp.syndication.media.SocialMedia",
        "video" : "com.ctacorp.syndication.media.Video",
        "widget" : "com.ctacorp.syndication.media.Widget"
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
                    t.each{ type ->
                        types << mediaTypeMapping[type]
                    }
                    types
                }

                def set = resolveTypes(params.mediaTypes)
                inList 'class', set
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
                    idIs((Long)params.id)
                }
                if (params.collectionId) {
                    mediaForCollection(params.long('collectionId'))
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
                    sourceIdIs((Long) params.long('sourceId'))
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
                if (params.languageId) {
                    languageIdIs((Long) params.long('languageId'))
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
