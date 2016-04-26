%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tweet.label', default: 'Tweet')}" />
		<title>Import Tweets</title>
		<style>
			.tweetHolder{
				max-width: 950px;
			}
			.clickable_thumbnail{
				margin-bottom: 10px;
			}
            .clickable_thumbnail:hover{
                cursor: pointer;
            }
            .left_margin{
                margin-left:5px;
            }
			.tweet_text_large{
				font-size: 16px;
			}
			.tweet_text_large i{
				font-size: 42px;
				display: inline-block;
				float:left;
				margin-right: 10px;
			}
			.modal-image{
				max-width:100%;
			}
			.foot{
				height:50px;
			}
		</style>
		<script>
			$(document).ready(function(){
				$(".clickable_thumbnail").click(function(event){
					console.log($(this).data('image_url'));
					$('#modal_body').html("<img class='modal-image' src='" + $(this).data('image_url') + "'/>")
				});
			});
		</script>
	</head>
	<body>
		<div id="import-tweet" class="content tweetHolder" role="main">
			<h1>Import Tweet</h1>
			<synd:message/>
			<synd:errors/>
			<synd:error/>

			<div class="row">
				<div class="col-sm-12">
					<g:form action="importTweets" class="form-inline">
                        <div class="form-group pull-right">
                            <div class="checkbox">
                                <label for="restrictToMedia">
                                    <g:checkBox name="restrictToMedia" checked="${restrictToMedia}"/> Restrict to Images
                                </label>
                            </div>
                            <g:select name="count" from="${[10, 25, 50, 100]}" value="${count}" class="form-control"/>
                            <g:submitButton name="submit" value="Find Tweets!" class="btn btn-info"/>
                        </div>
						<div class="form-group">
							<label for="accountName">
								Twitter Account:
							</label>
							<g:select from="${twitterAccountList}" name="accountName" optionKey="accountName" optionValue="accountName" value="${accountName}" noSelection="['':'Choose an Account']" class="form-control"/>
						</div>
					</g:form>
				</div>
			</div>
			<br/>
			<g:if test="${message}">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-info" role="alert">${message}</div>
					</div>
				</div>
			</g:if>

			<g:if test="${tweets}">
				<g:form action="saveTweets">
				<g:hiddenField name="accountName" value="${accountName}"/>
				<g:each in="${tweets}" var="tweet">
					<div class="well">
						<g:set var="hasMedia" value="${tweet?.mediaEntities?.mediaURLHttps[0]}"/>

						<div class="row">
							<div class="col-md-2 checkbox form-group">
								<div class="checkbox">
									<label>
										<g:set var="tweetId" value="tweetId_${tweet.id}"/>
										<g:if test="${params[tweetId]}">
											<input type="checkbox" checked name="tweetId_${tweet.id}"> Import Tweet
										</g:if>
										<g:else>
											<input type="checkbox" name="tweetId_${tweet.id}"> Import Tweet
										</g:else>
									</label>
								</div>
							</div>

							<div class="col-md-10">
								<g:if test="${hasMedia}">
									<div class="row">
										<div class="col-md-3" style="max-width: 160px;">
											<div>
												<img class="clickable_thumbnail" data-image_url="${tweet.mediaEntities.mediaURLHttps[0]}" data-toggle="modal" data-target="#myModal" src="${tweet.mediaEntities.mediaURLHttps[0]+":thumb"}"/>
											</div>
										</div>
										<div class="col-md-9">
											<div class="left_margin">
												<strong>
													<prettytime:display date="${tweet.createdAt}"/>
												</strong>
											</div>
											<div class="tweet_text_large left_margin">
												${tweet.text.encodeAsRaw()}
											</div>
                                            <br/>
                                            <div class="left_margin">
                                                <strong>Tweet ID: </strong>${tweet.id}
                                            </div>
										</div>
									</div>
								</g:if>
								<g:else>
									<div class="row">
										<div class="col-md-12">
											<div>
												<strong>
													<prettytime:display date="${tweet.createdAt}"/>
												</strong>
											</div>
											<div class="tweet_text_large">
												<i class="fa fa-twitter"></i>
												${tweet.text.encodeAsRaw()}
											</div>
                                            <br/>
                                            <div>
                                                <strong>Tweet ID: </strong>${tweet.id}
                                            </div>
										</div>
									</div>
								</g:else>
							</div>
						</div>
					</div>
				</g:each>
				<div>

				<div class="row">
					<div class="col-sm-12">
                        <div class="form-group">
                            <label for="sourceId">
                                Source<span class="required-indicator">*</span>
                            </label>
                            <g:select from="${sourceList}" name="sourceId" optionKey="id" optionValue="name" value="${name}" noSelection="['':'Choose a Source']" class="form-control"/>
                        </div>
						<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER">
							<div class="form-group">
								<label for="subscriberId">Subscriber<span class="required-indicator">*</span></label>
								<g:select from="${subscribers}" name="subscriberId" optionKey="id" optionValue="name" value="${currentSubscriber?.id}" noSelection="['':'Choose an Owner']" class="form-control"/>
							</div>
						</sec:ifAnyGranted>
						<div class="form-group">
							<g:submitButton name="import" value="Import Tweets" class="btn btn-lg btn-primary"/>
						</div>
					</div>
				</div>
					<div class="pull-right">
					<input type="button" id="select-all" class="btn btn-large btn-success " value="Select All"/>
					<input type="button" id="unselect-all" class="btn btn-large btn-success " value="Deselect All"/>
				</div>

				</div>
				</g:form>
			</g:if>
		</div>

		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">Modal title</h4>
					</div>
					<div id="modal_body" class="modal-body">Loading...</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
		<div class="foot"></div>

	<script>
		$(document).ready(function(){
			$("#select-all").on("click", function(){
				var cbs = document.getElementsByTagName('input');
				for(var i=0; i < cbs.length; i++) {
					if(cbs[i].type == 'checkbox') {
						cbs[i].checked = "checked";
					}
				}
			});

			$("#unselect-all").on("click", function(){
				var cbs = document.getElementsByTagName('input');
				for(var i=0; i < cbs.length; i++) {
					if(cbs[i].type == 'checkbox') {
						cbs[i].checked = "";
					}
				}
			})
		})
	</script>
	</body>
</html>
