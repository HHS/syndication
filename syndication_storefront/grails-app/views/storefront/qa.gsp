<%--
  Created by IntelliJ IDEA.
  User: hmarkman
  Date: 7/29/12
  Time: 1:24 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Syndication</title>
</head>
<body>
<div id="faq">

    <div id="home_left">
        <a id="head"></a><h2><sf:pageContentAnchor/>Questions and Answers</h2>
        <a id="whatIs"></a><h3>What is Content Syndication?</h3>
        <p>Content syndication allows you to place content from HHS websites onto your own site. It allows you to offer high-quality HHS content in the look and feel of your site. The syndicated content is automatically updated in real time, requiring no effort from your staff to keep the pages up to date.
        </p>
        <a id="whyYou"></a><h3>Why should I do this?</h3>
        <p class="italic">There are many reasons to syndicate HHS content:</p>
        <ul>
            <li>You gain direct access to HHS content without having to monitor for or make updates.</li>
            <li>You can pull in HHS content rather than linking to it, so visitors stay on your site.</li>
            <li>You can present HHS Web content in the look and feel of your website.</li>
            <li>You can control which pages and content from HHS to use on your site and where it appears.</li>
            <li>You can integrate HHS content with your own content.</li>
            <li>You can present HHS health content directly to employees through your intranet.</li>
        </ul>
        <a id="whyHHS"></a><h3>Why is HHS doing this?</h3>
        <p>Syndication allows us to disseminate up-to-date, accurate, and timely health information to our partners and the general public. This is an important part of HHS’s mission.
        </p>
    </div>

    <div id="home_right">
        <div class="sidebar_box_blue">
            <h4>On this Page</h4>
            <ul>
                <li><a href="#whatIs"> What is Content Syndication?</a></li>
                <li><a href="#whyYou"> Why should I do this?</a></li>
                <li><a href="#whyHHS"> Why is HHS doing this?</a></li>
                <li><a href="#work"> How does it work?</a></li>
                <li><a href="#beta"> What is Beta Testing?</a></li>
                <li><a href="#api"> Where can I get the API / syndication code to add to my site?</a></li>
                <li><a href="#search"> How do I find a syndicated page I’m interested in?</a></li>
                <li><a href="#cantFind"> What if I don’t see the page I’m interested in syndicating?</a></li>
                <li><a href="#sharepoint"> Is it possible to input syndicated content into a Sharepoint webpage if Sharepoint has the HTML editor disabled?</a></li>
            </ul>
        </div>
        <div class="sidebar_box_blue_bottom"></div>
    </div>

    <a id="work"></a><h3>How does it work?</h3>
    <p>HHS provides an application programming interface (API) that enables you to display HHS.gov content on your website. The syndication system integrates with Content Management Systems (CMS) and can be incorporated into your publishing workflow.
    </p>

    <a id="beta"></a><h3>What is Beta Testing?</h3>
    <p>We have just launched this content syndication service. As an early user, you will be a “beta tester.” Beta testing helps us identify and correct technical, content, user experience, and other issues prior to releasing the product more widely. Beta testers should try out all aspects of the system including: registering; signing in; finding Web pages that you’re interested in syndicating and adding those pages to your list; and getting the syndication code from this site and implementing on your website. Please report any feedback or issues to <a href="mailto:${adminEmail}?subject=Information Request">syndicationAdmin@hhs.gov</a> so we can continue to improve the system.
    </p>
    <a id="api"></a><h3>Where can I get the API / syndication code to add to my site?</h3>
    <p>Getting the syndication code is very easy using HHS’s Content Syndication site. Simply follow these steps:</p>
    <ul>
        <li>Register online to sign up for the service.</li>
        <li>Browse the site to find pages you’re interested in syndicating and add those pages to one of your lists. You can add as many pages as you’d like and create as many lists as you like.</li>
        <li>When you’re finished adding syndicated pages to your list, you can get the RSS or Atom feed link for your content.</li>
        <li>You can then use this feed to automatically pull content into your content management system.</li>
    </ul>

    <a id="search"></a><h3>How do I find a syndicated page I’m interested in?</h3>
    <p>There are several ways you can locate syndicated pages on HHS Content Syndication site:</p>
    <ul>
        <li>If you know the specific URL for an HHS Web page, simply copy and paste the URL into the Web Page Syndication Lookup box and click Find URL.</li>
        <li>If you’re looking for additional search options, click the Advanced Lookup link in the Web Page Syndication Lookup box on the Content Syndication home page.</li>
    </ul>
    <a id="cantFind"></a><h3>What if I don’t see the page I’m interested in syndicating?</h3>
    <p>Due to the large volume of HHS Web content, not all pages are available for syndication. But we’re working hard to add new pages as needed. If you don’t see a page you’re interested in, you can Request a Page to be Syndicated.
    </p>
    <a id="sharepoint"></a><h3>Is it possible to input syndicated content into a Sharepoint webpage if Sharepoint has the HTML editor disabled?</h3>
    <p>Unfortunately not. Currently, there is no way to embed the specific HTML script fragments for this purpose into Sharepoint.
    </p>
</div>
</body>
</html>