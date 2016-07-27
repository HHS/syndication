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
Syndication Content Client, a Drupal7 Module.

The Syndicated Content module adds the capability to publish local content to an external source, Import external content from an external source, and provides a callback url for handling messages from the external source.

REQUIREMENTS
------------
This module requires Drupal 7. No other modules are required.

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

Local Content Types chosen as *syndicatable* get a new section in their edit screen for managing the publishing process. It provides a publish form for available content. It displays metadata for content already published. It provides an *update* button to manually send a new copy of your content to inform your source of any content changes. You can add more roles to publish to Syndication by giving them the Syndication Publish Right under permissions.

**Configuration**
Admin has configuration rights by default. You can give other users Syndiction configuration rights. This can be done by going to Drupal permissions and assign the Syndication Configuration Rights to the role you want to access the configuration page.

Download Link
----------------------
[Download Latest Release](https://github.com/HHS/syndication/blob/master/binaries/syndication-client-drupal7-module.1.16.3.2.zip?raw=true)

CHANGE LOG
------------

**1.16.7.27**

Added delete option on the Drupal side so the content will be removed on the Syndication system. We have
also added archive/unarchive option for the Syndicated content.

**1.16.6.8**

Added permission for Syndicaiton configuration page.

**1.16.5.18**

Added Drupal hook_permission for Syndication publishing rights.

**1.16.3.11**

Custom Patching Level to fix updates

**1.16.3.2**

Add support for custom public URLs when publishing content.

**1.15.5.29**

Organization Save and send to API Fix

New Source - Soft Variable Checks

Style Updates for Multiple Distributions

**1.15.5.28**

Version's Added

More Descriptive Error Messaging on Admin and Publish

Error Message Hints on Admin

CMS Manager URL Fix on Configuration

**1.15.5.20**

Suppress LibXML Warnings

Gentle Syndication Class Search

Secondary Failover PREG Search for Class on Fail

Ordering of Content Types on Admin Page

Organization Name Update

DIV Exceptions

JSON Quick Add for Admin Keys

Extended / Automatic Custom Content Type Mappings

**1.15.4.30**

Override SSL via Param Set for Test Connection

Auto-Push Content Changes to Syndication

Public Content as Infographic and Image Type

Module Settings UI Redo

Module Settings UI - Test Connection Replacement

Auto-Prepare Content for Syndication

Update Functionality for Upgrades

Pass Tags to Syndication

Publish Content as Youtube Video

Full Custom Content Type Field to Field Mapping via UI

Type Mapping Extension

Expand Auto-Prepare to allow optional inclusion
