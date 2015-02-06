
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.service


import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import syndication.rest.CampaignsService

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(CampaignsService)
class CampaignsServiceSpec extends Specification {
    def items = []
    def defaultParams = []
    def defaultResponse = []
    def response = [format: "json"]

    def setup() {
        1..20.each {
            items << ["id": it,
                    "description": "Campaign:${it}",
                    "endDate": new Date(),
                    "mediaItems": [],
                    "name": "Campaign:${it}",
                    "source": "CTAC",
                    "startDate": new Date() + it]
        }
        defaultParams = [
                controller: "campaigns",
                url: "http://localhost:8080/Syndication/api/v2/resources/campaigns/",
                max: 10,
                count: 10,
                offset: 0,
                limit: 10,
                sort: "name",
                order: "ASC",
                next_url: "http://localhost:8080/Syndication/api/v2/resources/campaigns?format=json&offset=10&max=10&sort=name&order=ASC"
        ]
        defaultResponse = [status:200, developer_message:"Everything is fine.", error_message:"No Error.", error_type:"N/A", user_message:"Search Results"]

    }

    void "get meta data template"(){
        given: "params and response objects"
            def params = defaultParams
            def response = defaultResponse
            def service = mockCampaignsService

        when: "valid params and response object are passed from controller"
            def metaMap = service.getMetaDataTemplate(params, response)

        then: "result should be success"
            metaMap != null
    }

    private def getMockCampaignsService(){
        def errorHandlingService = [
            send200: {
                return ["status": 200]
            },
            send400SortField: {
                return [ "status": 400]

            },
            send500:{
                return ["status":500]
            }
        ]

        def apiResponseBuilderService = [
                getMetaData:{defaultParams, defaultResponse ->
                    return [controller: "campaigns",
                    url: "http://localhost:8080/Syndication/api/v2/resources/campaigns/",
                    max: 10,
                    count: 10,
                    offset: 0,
                    limit: 10,
                    sort: "name",
                    order: "ASC",
                    next_url: "http://localhost:8080/Syndication/api/v2/resources/campaigns?format=json&offset=10&max=10&sort=name&order=ASC"]

                },
                getResultSet:{
                    return [max:10, offset:0, sort:"name", order:"ASC", count:1, nextOffset:10,
                            serverURL:"http://localhost:8080/Syndication",
                            url:"http://localhost:8080/Syndication/api/v2/resources/campaigns?format=json&offset=0&max=10&sort=name&order=ASC",
                            next_url:"http://localhost:8080/Syndication/api/v2/resources/campaigns?format=json&offset=10&max=10&sort=name&order=ASC",
                            format:"json",
                            items:items,
                            response:defaultResponse
                    ]
                }
        ]

        def campaignsService = new CampaignsService()
        campaignsService.apiResponseBuilderService = apiResponseBuilderService
        campaignsService.errorHandlingService = errorHandlingService

        return campaignsService

    }
}
