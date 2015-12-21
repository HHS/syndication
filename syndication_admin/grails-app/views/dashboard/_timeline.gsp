<!-- /.panel -->
<div class="panel panel-default">
    <div class="panel-heading">
        <i class="fa fa-clock-o fa-fw"></i> Timeline
    </div>
    <!-- /.panel-heading -->
    <div class="panel-body">
        <ul class="timeline">
            <g:each in="${timelineEvents}" var="timelineEvent" status="i">
                <li class="${i%2==0?'':'timeline-inverted'}">
                    <div class="timeline-badge info">
                        <synd:mediaIcon mediaType="${timelineEvent?.type}"/>
                    </div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title" style="word-wrap: break-word"><a href="${grailsApplication.config.grails.serverURL}/mediaItem/show?id=${timelineEvent.id}">${timelineEvent?.title}</a></h4>
                            <h4 class="timeline-title" style="word-wrap: break-word"><a href="${grailsApplication.config.storefront.serverAddress}/storefront/showContent/${timelineEvent.id}">Storefront Page</a></h4>
                            <p>
                                <small class="text-muted"><i class="fa fa-clock-o"></i> <prettytime:display date="${timelineEvent?.timestamp}" /></small>
                            </p>
                        </div>

                        <div class="timeline-body">
                            <img class="timeline_thumbnail" src="${grailsApplication.config.syndication.contentExtraction.urlBase}/${timelineEvent.id}/preview.jpg"/>
                            <p><synd:shortenString string="${timelineEvent?.message}"/></p>
                        </div>
                    </div>
                </li>
            </g:each>
        </ul>
    </div>
    <!-- /.panel-body -->
</div>
<!-- /.panel -->