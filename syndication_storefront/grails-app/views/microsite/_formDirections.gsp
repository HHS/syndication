<%@ page import="grails.util.Holders" %>
<h3>Form Directions:</h3>

  <i>All fields are optional in this form. The generated website will directly
  reflect what you choose to include. If a media source is not selected for a
  given section, that section will be omitted from the microsite.
  </i>

<h4><b>Header</b></h4>
  <p>You may provide a Title and Url Logo for the Websites Header</p>

<h4><b>Main Content</b></h4>
      <p>This is the primary content area for the template. Selecting media for this section is required
      for the template to display properly.
      </p>
      <p>1. Pick one Media Source. Options include:</p>
    <ul>
      <li>My List: Custom list of Media Items that you can create <a target="_blank" href="${grails.util.Holders.config.API_SERVER_URL}/userMediaList/index">here.</a></li>
      <li>Collection: A grouping of items curated by the source agency.</li>
      <li>Tag: Media Items that to a topic or idea</li>
      <li>Source: All media items published by the specified agency.</li>
      <li>Campaign: Media Items belonging to a specific initiative sponsored by an agency; e.g. “National Stop Smoking Month”</li>
    </ul>
    <p>2. Once you choose a Media Source, you will be asked to choose specifics;
    eg. the particulat tag or list you would like to use as a source.
    <p>3. Next, the sorting and ordering options can be defined for the selected Media Source.</p>
    <p>4. A summary will be displayed detailing settings for the current section.</p>

<g:if test="${params.controller == "carousel"}">
    <h4>Side Content</h4>
    <p>These sections are bottom panels meant for a smaller list of media Items and are chosen
    the same way you choose in section two.</p>

</g:if>
<g:else>
    <h4><b>Side content</b></h4>

    <p>These sections are side panels meant for smaller lists of media and are
    configured in the same way as the Main Content section. Side content is generally optional,
    and the look and feel will be dependent on the content chosen for display along with the display
    method used for each panel.
    </p>

    <p>Additionally, there are three display styles to choose from:</p>
    <ul>
        <li>list of links (display simple media titles)</li>
        <li>grid display of thumbnails</li>
        <li>full content view (slideshow style)</li>
    </ul>
</g:else>


<h4><b>Footer</b></h4>
    <p>The footer allows you to include a mission statement or closing thoughts, as well as provide links back to other
    microsites or your own website. </p>

    <ul>
        <li>Optional: Include a short paragraph.</li>
        <li>Optional: Include up to four links which can be titled an linked however you like.</li>
    </ul>