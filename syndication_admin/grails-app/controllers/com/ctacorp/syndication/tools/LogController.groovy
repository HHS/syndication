package com.ctacorp.syndication.tools

import com.amazonaws.services.logs.model.DescribeLogStreamsRequest
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.services.logs.AWSLogsClient
import com.amazonaws.services.logs.model.GetLogEventsRequest


@Secured(["ROLE_ADMIN"])
class LogController {

	static STREAM_PREFIXES = ['syndication_admin', 'syndication_api', 'cms_manager', 'syndication_storefront', 'tiny_url', 'tag_cloud']
    static LOG_GROUP = 'syndication'
    static SYNDICATION_ENV = 'stg' //['local', 'ctacdev'].contains(System.getenv('SYNDICATION_ENV')) ? 'ctacdev' : System.getenv('SYNDICATION_ENV')

    def index() {}

    def showLogs(String stream) {

        response.contentType = "application/json"

        String nextToken = params.token

        def logEventsResult = getLogEventsResult(stream, nextToken)
        if(!logEventsResult) {
            return [logData: '', pageNextBack: '', firstForwardToken: ''] as JSON
        }

        def logRecords = logEventsResult.getEvents().collectEntries { [it.timestamp, it.message] }

        def sb = new StringBuilder()

        logRecords.sort { -it.key }.eachWithIndex { event, index ->
            sb.append "<p class=\"consoleLine ${index % 2 == 0 ? 'even' : 'odd'}\">${event.value}</p>"
        }

        def firstForwardToken = !params.token ? logEventsResult.getNextForwardToken() : params.firstForwardToken

        def pageResp = ''

        if(logEventsResult.getNextForwardToken() && firstForwardToken!=logEventsResult.getNextForwardToken()){
            pageResp = "<a href=\"#\"  class=\"nextLink\" data-token=\"${logEventsResult.getNextForwardToken()}\">Back</a>"
        }

        if (logEventsResult.getNextBackwardToken() && logEventsResult.getNextForwardToken().substring(2)!=logEventsResult.getNextBackwardToken().substring(2) ) {
            pageResp = "<a href=\"#\"  class=\"nextLink\" data-token=\"${logEventsResult.getNextBackwardToken()}\">Next</a>"
        }

        pageResp += "<input type=\"hidden\" value=\"${nextToken}\" id=\"currentToken\" />"

        [logData: sb.toString(), pageNextBack:pageResp, firstForwardToken:firstForwardToken] as JSON
    }

    def adminErrorLog(){
        render(showLogs('syndication_admin'))
    }

    def apiErrorLog(){
        render(showLogs('syndication_api'))
    }

    def cmsLog(){
        render(showLogs('cms_manager'))
    }

    def storefrontErrorLog(){
        render(showLogs('syndication_storefront'))
    }

    def tinyErrorLog(){
        render(showLogs('tiny_url'))
    }

    def tagErrorLog(){
        render(showLogs('tag_cloud'))
    }

    def logDownload() {

        response.setContentType("application/octet-stream")

        def logEventsResult = getLogEventsResult(params.stream as String, null)
        if(!logEventsResult) {
            return
        }

        def logRecords = logEventsResult.getEvents().collectEntries { [it.timestamp, it.message] }
        def sb = new StringBuffer()

        logRecords.sort { -it.key }.each {
            sb.append "${it.value}\n"
        }


        response.setHeader("Content-disposition", "attachment;filename=\"${logStreams()[params.stream]}\"")
        response.outputStream << sb.toString()
    }


    def getLogEventsResult(String stream, String token) {

        def logStreamName= logStreams()[stream]

        if(!logStreamName) {
            return null
        }

        new AWSLogsClient().getLogEvents(new GetLogEventsRequest(
                logGroupName: LOG_GROUP,
                logStreamName: logStreamName,
                startFromHead: false,
                nextToken: token
        ))
    }

    static logStreams() {

        def client = new AWSLogsClient()

        STREAM_PREFIXES.collectEntries { appName ->

            def request = new DescribeLogStreamsRequest(logGroupName: LOG_GROUP, logStreamNamePrefix: "${appName}_${SYNDICATION_ENV}")
            def logStreams = client.describeLogStreams(request).logStreams.sort { it.creationTime }.reverse()

            if(!logStreams.empty) {

                def logStreamName = logStreams.get(0)?.logStreamName
                [appName, logStreamName]

            } else {
                [appName, null]
            }
        }
    }
}
