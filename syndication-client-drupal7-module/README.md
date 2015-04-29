CONTENTS OF THIS FILE
---------------------
* Introduction
* Requirements
* Installation
* Configuration
* Usage

INTRODUCTION
------------
Syndication Content Client, a Drupal7 Module.

The Syndicated Content module adds the capability to publish local content to an external source, Import external content from an external source, and provides a callback url for handling messages from the external source.

REQUIREMENTS
------------
This module requires Drupal 7. Now other modules are required. 

Searching and Importing content do not require an *api identity*. 

Publishing and Subscribing to content does require an *api identity*.

Api Identity Keys are acquired directly from the Syndication Source's CMS Manager. Contact the desired source's site admin to acquire identity keys.


INSTALLATION
------------
* Install as you would normally install a contributed drupal module. Uncompress the module file into the modules directory, install and enable. See:
   https://drupal.org/documentation/install/modules-themes/modules-7
   for further information.
   
CONFIGURATION
-------------
Configure user permissions in *Administration » Config » Syndicated Content Sources*: 

**API Urls**

*Base Syndication API Url*: hostname, port, and path to the api including version number (ex. http://SyndicationSource:80/Syndication/api/v2)

*Base Syndication Tiny Url*: hostname and port of the tiny url shortening service provided by the source. This is most likely the host/port of the API Url without a path (ex. http://SyndicationSource:80)

**API Identity**

Enter the keys as acquired by your source's manager.

The *Test Credentials* button sends  your keys to your Syndication Source and reports back their validity status. 

**Content Types** 

Choose which local content types should be used by this module.

**Organization Name**

Syndication Sources require an Organization Name to categorize who is using their service. The list of allowable organization names is acquired from the Source Server once the *Base Syndication API Url* is set correctly.

USAGE
------------
**Ingestion**

A new *Syndicated Content* content type is provided. 

Adding a new *Syndicated Content* type brings you to a page providing browsing and searching of all public content available in your external source. Advanced Searching is available. Preview of content is provided if you know the id of the item you want to preview. 

Pressing the *Subscribe* button will send you to an auto-populated local content type creation page. You must complete the creation process normally.

**Publishing**

Local Content Types chosen as *syndicatable* get a new section in their edit screen for managing the publishing process. It provides a publish form for available content. It displays metadata for content already published. It provides an *update* button to manually send a new copy of your content to inform your source of any content changes.