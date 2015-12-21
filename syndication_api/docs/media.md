##Media

The Media Items are carefully syndicated content items that are completely free to the public.
 
The whole syndication system is centered around the grouping and searching of different Media Items and different ways 
to display them and all of their meta data.

Here are some ways to directly access the Media Items Metadata.

1 Get the raw content (html, image, etc...) for the MediaItem identified by their 'id'.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media/{id}/content
```

2 Get the JPG preview, where applicable, for the MediaItem identified by the 'id'.

```html
  https://api.digitalmedia.hhs.gov:443/api/v2/resources/media/{id}/preview.jpg
```

3 Get the JPG thumbnail, where applicable, for the MediaItem identified by the 'id'.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media/{id}/thumbnail.jpg
```

4 Get all the fields that make up a Media Item by its id.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media/{id}.json
```

###GET/JSON example

Lets get the Javascript embed code that will make it easy for someone to syndicate an item on their own web page.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media/1/embed.json?excludeJquery=false&excludeDiv=false
```

This Get request will return embed code inside the result.snippet key

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 1,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media.json?offset=0&max=1&sort=id&excludeJquery=false&excludeDiv=false&id=1&format=json",
          "max": 1,
          "nextUrl": null,
          "offset": 0,
          "pageNum": 1,
          "previousUrl": null,
          "sort": "id",
          "total": 1,
          "totalPages": 1
        },
        "status": 200
      },
      "results": [
        {
          "snippet": "&lt;script src=&quot;https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js&quot;&gt;&lt;/script&gt;&lt;script&gt;$(document).ready(function(){$.getJSON(&#39;https://api.digitalmedia.hhs.gov/api/v2/resources/media/1/syndicate.json?stripStyles=false&amp;stripScripts=false&amp;stripBreaks=false&amp;stripImages=false&amp;stripClasses=false&amp;displayMethod=null&amp;autoplay=false&amp;userId=null&amp;callback=?&#39;, function(data){$(&#39;#syndicatedContent_1_6008675044967602&#39;).html(data.results[0].content);});});&lt;/script&gt;&lt;div id=&quot;syndicatedContent_1_6008675044967602&quot;&gt;&lt;/div&gt;"
        }
      ]
    }
    
```

This embed code actually makes a call to the Media Items syndicate end point which returns the content of the given
media Item.

```html
    https://api.digitalmedia.hhs.gov:443/api/v2/resources/media/1/syndicate.json?cssClass=syndicate&stripStyles=false&stripScripts=false&stripImages=false&stripBreaks=false&stripClasses=false&autoplay=true&rel=false
```

This request will return the content which is the data that the publisher of the content intended to get out to his 
audience. Along with some extra meta data about the Item.

```javascript
    {
      "callback": null,
      "meta": {
        "messages": [],
        "pagination": {
          "count": 1,
          "currentUrl": "https://api.digitalmedia.hhs.gov/api/v2/resources/media.json?offset=0&max=1&sort=id&stripBreaks=false&stripClasses=false&stripStyles=false&stripImages=false&stripScripts=false&cssClass=syndicate&rel=false&autoplay=true&id=1&format=json&newUrlBase=http://www.flu.gov/pandemic/about",
          "max": 1,
          "nextUrl": null,
          "offset": 0,
          "pageNum": 1,
          "previousUrl": null,
          "sort": "id",
          "total": 1,
          "totalPages": 1
        },
        "status": 200
      },
      "results": [
        {
          "content": "<div class=\"syndicate\"> \n <h1 autofocus=\"true\">About Pandemics</h1> \n <a name=\"top\" href=\"http://www.flu.gov\"></a> \n <p>A pandemic is a global disease outbreak. It is determined by how the disease spreads, not how many deaths it causes.<br> <br> When a new <a href=\"http://www.cdc.gov/flu/avianflu/influenza-a-virus-subtypes.htm\">influenza A virus</a> emerges, a flu pandemic can occur. Because the virus is new, the human population has little to no immunity against it. The virus spreads quickly from person-to-person worldwide.</p> \n <p>The United States is not currently experiencing a flu pandemic. If a pandemic occurs, the federal government will work to identify the cause and create a vaccine. Flu.gov will provide updates on the steps the federal government is taking to address the pandemic.</p> \n <h2>Characteristics and Challenges of a Flu Pandemic</h2> \n <ol start=\"1\" type=\"1\"> \n  <li>Rapid Worldwide Spread<br> \n   <ul type=\"circle\"> \n    <li>When a pandemic flu virus emerges, expect it to spread around the world.</li> \n    <li>You should <a href=\"http://www.flu.gov/planning-preparedness/index.html\">prepare</a>&nbsp;for a pandemic flu as if the entire world population is susceptible.</li> \n    <li>Countries may try to delay the pandemic flu’s arrival through border closings and travel restrictions, but they cannot stop it.<br> </li> \n   </ul> </li> \n  <li>Overloaded Health Care Systems<br> \n   <ul type=\"circle\"> \n    <li>Most people have little or no immunity to a pandemic virus. Infection and illness rates soar. A substantial percentage of the world’s population will require some form of medical care.</li> \n    <li>Nations are unlikely to have the staff, facilities, equipment, and hospital beds needed to cope with the number of people who get the pandemic flu.</li> \n    <li>Death rates may be high. Four factors largely determine the death toll:</li> \n   </ul> </li> \n </ol> \n <div start=\"1\" type=\"1\" style=\" margin-left: 4em;\"> \n  <ul type=\"disc\"> \n   <li style=\" list-style: none;\"> \n    <ul type=\"disc\"> \n     <li style=\" list-style: none;\"> \n      <ul type=\"disc\"> \n       <li>The number of people who become infected</li> \n       <li>The strength of the virus</li> \n       <li>The underlying characteristics and vulnerability of affected populations</li> \n       <li>The effectiveness of <a href=\"http://www.flu.gov/planning-preparedness/index.html\">preventive measures</a>&nbsp;</li> \n      </ul> </li> \n    </ul> </li> \n  </ul> \n </div> \n <ol start=\"2\" type=\"1\"> \n  <li style=\" list-style: none;\"> \n   <ul type=\"circle\"> \n    <li><a href=\"http://www.flu.gov/pandemic/history/index.html\">Past pandemics</a> spread globally in two or sometimes three waves.<br> </li> \n   </ul> </li> \n  <li>Inadequate Medical Supplies<br> \n   <ul type=\"circle\"> \n    <li>The need for vaccines is likely to be larger than the supply. Those at <a href=\"http://www.flu.gov/at-risk/index.html\">highest risk</a>&nbsp;will likely get the <a href=\"http://www.cdc.gov/flu/consumer/vaccinations.htm\">vaccine</a> first.</li> \n    <li>Early in a pandemic, the need for antiviral medications is likely to be larger than the supply. Those at highest risk will likely get antiviral medications first.<br> </li> \n   </ul> </li> \n  <li>A pandemic can create a shortage of hospital beds, ventilators, and other supplies. Alternative sites, such as schools, may serve as medical facilities.<br> <br> </li> \n  <li>Disrupted Economy and Society<br> \n   <ul type=\"circle\"> \n    <li>Travel bans, event cancellations, and school and business closings could have a major impact on communities and citizens.</li> \n    <li>Caring for sick family members and fear of exposure could result in significant employee absenteeism.</li> \n   </ul> </li> \n </ol> \n <h2>Seasonal Flu versus Pandemic Flu&nbsp;<a id=\"comparison\" name=\"comparison\" href=\"http://www.flu.gov\">&nbsp;</a></h2> \n <table border=\"1\" bordercolor=\"#000000\" cellpadding=\"2\" cellspacing=\"2\"> \n  <tbody> \n   <tr> \n    <th scope=\"col\" valign=\"top\" width=\"319\" style=\" background-color: #C0C0C0;\"> <p align=\"center\"><strong>Pandemic Flu</strong></p> </th> \n    <th scope=\"col\" valign=\"top\" width=\"319\" style=\" background-color: #C0C0C0;\"> <p align=\"center\"><strong>Seasonal Flu</strong></p> </th> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Rarely happens (three times in 20th century)</td> \n    <td valign=\"top\" width=\"319\">Happens annually and usually peaks in January or February</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">People have little or no immunity because they have no previous exposure to the virus</td> \n    <td valign=\"top\" width=\"319\">Usually some immunity built up from previous exposure</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Healthy people may be at increased risk for serious complications</td> \n    <td valign=\"top\" width=\"319\">Usually only <a href=\"http://www.flu.gov/at-risk/index.html\">people at high risk</a>, not healthy adults, are at risk of serious complications</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Health care providers and hospitals may be overwhelmed</td> \n    <td valign=\"top\" width=\"319\">Health care providers and hospitals can usually meet public and patient needs</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Vaccine probably would not be available in the early stages of a pandemic</td> \n    <td valign=\"top\" width=\"319\">Vaccine available for annual flu season</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Effective <a href=\"http://www.cdc.gov/flu/consumer/treatment.htm\">antivirals</a> may be in limited supply</td> \n    <td valign=\"top\" width=\"319\">Adequate supplies of antivirals are usually available</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Number of deaths could be high (The U.S. death toll during the <a href=\"http://flu.gov/pandemic/history/index.html#1918\">1918 pandemic</a> was approximately 675,000)</td> \n    <td valign=\"top\" width=\"319\"><a href=\"http://www.cdc.gov/flu/about/disease/us_flu-related_deaths.htm\">Seasonal flu-associated deaths</a> in the United States over 30 years ending in 2007 have ranged from about 3,000 per season to about 49,000 per season.</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Symptoms may be more severe</td> \n    <td valign=\"top\" width=\"319\"><a href=\"http://www.flu.gov/symptoms-treatment/index.html\">Symptoms</a> include fever, cough, runny nose, and muscle pain</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">May cause major impact on the general public, such as widespread travel restrictions and school or business closings</td> \n    <td valign=\"top\" width=\"319\">Usually causes minor impact on the general public, some schools may close and sick people are encouraged to stay home</td> \n   </tr> \n   <tr> \n    <td valign=\"top\" width=\"319\">Potential for severe impact on domestic and world economy</td> \n    <td valign=\"top\" width=\"319\">Manageable impact on domestic and world economy</td> \n   </tr> \n  </tbody> \n </table> \n</div><div class='syndicate'><span><Strong>Syndicated Content Details:</strong></span><br/><span>Source URL: <a href='http://www.flu.gov/pandemic/about/index.html'>http://www.flu.gov/pandemic/about/index.html</a></span><br/><span>Source Agency: <a href='http://www.hhs.gov'>Health and Human Services (HHS)</a></span><br/><span>Captured Date: 2015-04-07 00:03:09.0</span><br/></div><iframe src=\"//www.googletagmanager.com/ns.html?id=GTM-KT9TM9&mediaId=1&mediaType=html&sourceUrl=http%3A%2F%2Fwww.flu.gov%2Fpandemic%2Fabout%2Findex.html&userId=-1&sourceId=1&sourceAcronym=HHS&campaignId=-1&campaignName=null&languageId=1&isoCode=eng\" height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe><noscript><iframe src=\"//www.googletagmanager.com/ns.html?id=GTM-KT9TM9&mediaId=1&mediaType=html&sourceUrl=http%3A%2F%2Fwww.flu.gov%2Fpandemic%2Fabout%2Findex.html&userId=-1&sourceId=1&sourceAcronym=HHS&campaignId=-1&campaignName=null&languageId=1&isoCode=eng\" height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe></noscript>",
          "description": "Definition, characteristics, and impact of a flu pandemic.",
          "id": 1,
          "mediaType": "Html",
          "name": "About Pandemics",
          "sourceUrl": "http://www.flu.gov/pandemic/about/index.html"
        }
      ]
    }
```

Inject the the 'content' field into your html to display the latest content in real time.

###Jquery JSONP example
On our <a href="https://digitalmedia.hhs.gov">HHS Syndication Storefront</a> we have a form that allows you to easily search for content so that you can easily use and embed syndicated media. You can view the documentation on how to use syndicated Media Items <a href="http://ctacdevteam.bitbucket.org/docs/Syndication_Storefront/pages/UseSyndicatedContent.html#">here.</a>

