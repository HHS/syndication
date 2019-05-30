CONTENTS OF THIS FILE
---------------------
* Introduction
* Requirements
* Installation
* Configuration
* Usage
* Download Link
* Change Log

INTRODUCTION
------------
Syndication Content Client, a Drupal 8 Module.

The Syndicated Content module adds the capability to publish local content to an external source and provides a callback url for handling messages from the external source.

REQUIREMENTS
------------
This module requires Drupal 8. No other modules are required.

Publishing and Subscribing to content does require an *api identity*.

Api Identity Keys are acquired directly from the Syndication Source's CMS Manager. Contact the desired source's site admin to acquire identity keys.


INSTALLATION
------------
* Install as you would normally install a contributed drupal module. Uncompress the module file into the modules directory, install and enable. See:
   https://drupal.org/docs/8/extending-drupal-8/installing-drupal-8-modules
   for further information.

CONFIGURATION
-------------
Configure user permissions in *Administration � Configuration � Syndicated Content*:

**API Urls**

*Base Syndication API Url*: hostname, port, and path to the api including version number (ex. http://SyndicationSource:80/Syndication/api/v2)

*Base Syndication Tiny Url*: hostname and port of the tiny url shortening service provided by the source. This is most likely the host/port of the API Url without a path (ex. http://SyndicationSource:80)

**API Identity**

Enter the keys as acquired by your source's manager.

**Content Types**

Choose which local content types should be used by this module.

**Organization Name**

Syndication Sources require an Organization Name to categorize who is using their service. The list of allowable organization names is acquired from the Source Server once the *Base Syndication API Url* is set correctly. 
Add the keys given to you and click save. Then the organization names will show in the drop down box. Choose your organization and save again.

USAGE
------------
**Publishing**

Local Content Types chosen as *syndicatable* get a new section in their edit screen for managing the publishing process. It provides a publish form for available content. It displays metadata for content already published. You can add more roles to publish to Syndication by giving them the Syndication Publish Right under permissions.

**Configuration**
Admin has configuration rights by default. You can give other users Syndiction configuration rights. This can be done by going to Drupal permissions and assign the Syndication Configuration Rights to the role you want to access the configuration page.

Download Link
----------------------
[Download Latest Release]([Download Latest Release](https://github.com/HHS/syndication/blob/master/binaries/syndication_drupal_8.1.18.1.5.zip?raw=true))

CHANGE LOG
------------

**1.18.1.5**
Ported the Drupal 7 module to Drupal 8. The Drupal 8 module does not include the Ingestion section, only the publishing content.

**2.0.5.30**
Added auto syndicate. Added custom content type to be syndicsted.