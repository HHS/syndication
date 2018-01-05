<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>

<!--[if lt IE 7]>
<html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>
<html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>
<html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"><!--<![endif]-->
<head>
    <title>HHS Media Services Workgroup</title>
    <meta name="layout" content="storefront"/>
    <asset:javascript src="jquery"/>
    <asset:javascript src="workgroup/main.js"/>
</head>

<body>
    <div id="faq">

        <h2><strong>Digital Media Syndication</strong><br/></h2>
        <strong>Sharing Your Health Content for Websites, Apps, and Social Media</strong></p>

        <p>
            We have created an easy way for public health partners to access your federally produced digital resources
            --  including web content, images, video, data, infographics, and more – for use on their website, apps, and social media.
            Syndication allows our science-based resources to be combined with information at state and local levels by sharing health messaging for maximum impact.</p>


        <div style="float:left">
            <p>
                <strong>Syndication Sites</strong>
            </p>
            <ul>
                <li>
                    <a href="https://digitalmedia.hhs.gov">Health and Human Services</a>
                </li>
                <li>
                    <a href="https://tools.cdc.gov/medialibrary/">Centers for Disease Control and Prevention – Public Health Media Library</a>
                </li>
                <li>
                    <a href="https://digitalmedia.hhs.gov/tobacco">Food and Drug Administration - Center for Tobacco Products - Exchange Lab</a>
                </li>
            </ul>
            <p><strong>Open Source Code</strong></p>
            <p><strong>Below are links to the related code</strong></p>
            <ul>
                <li>
                    <a href="http://sourceforge.net/projects/contentservices/?source=directory">HHS Digital Media API Platform<br/></a>
                </li>
                <li>
                    <a href="https://github.com/HHSDigitalMediaAPIPlatform">WCMS Plugins</a>
                </li>
                <li>
                    <a href="https://github.com/HHSDigitalMediaAPIPlatform">HHS Content Models via GitHub</a>
                </li>
            </ul>

            <p>
                The HHS Content Services Workgroup is a collaboration of agencies under HHS and other federal partners who are working to realize the vision of digital media syndication
                where public health information is credibly sourced, instantly updated, and delivered when and where consumers want it. 
                Members actively share knowledge and experiences to set strategy, align digital infrastructures, expand product development,
                and coordinate outreach to public health partners. For more information regarding the workgroup please email: <a href="mailto:syndication@hhs.gov">Syndication@hhs.gov</a>
            </p>

        </div>

        <br clear="all"/>


        <p>
            <strong>Promotional Materials</strong><br/>
            These factsheets and posters are used at conferences and presentations to raise awareness about the HHS digital media syndication project,
            and are geared toward external partners including state and local health departments, non-profit organizations, and commercial organizations.
            Please download and use at your events.
        </p>
        <ul>
            <li>
                <a href="${assetPath(src:'DMS_collaboration_factsheet_85x11.pdf')}">Partnering for Public Health</a>
            </li>
            <li>
                <a href="${assetPath(src:'HealthContent.pdf')}">Health Content for Websites, Apps, and Social Media</a>
            </li>
            <li>
                <a href="${assetPath(src:'EnergizingData.pdf')}">Energizing Data</a>
            </li>
            <li>
                <a href="${assetPath(src:'Digital_Content_Syndication_Infographic.pdf')}">Digital Media Syndication Infographic</a>
            </li>
        </ul>
    </div>

    <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
    <script>
        (function (b, o, i, l, e, r) {
            b.GoogleAnalyticsObject = l;
            b[l] || (b[l] =
                    function () {
                        (b[l].q = b[l].q || []).push(arguments)
                    });
            b[l].l = +new Date;
            e = o.createElement(i);
            r = o.getElementsByTagName(i)[0];
            e.src = '//www.google-analytics.com/analytics.js';
            r.parentNode.insertBefore(e, r)
        }(window, document, 'script', 'ga'));
        ga('create', 'UA-XXXXX-X');
        ga('send', 'pageview');
    </script>
</body>
</html>