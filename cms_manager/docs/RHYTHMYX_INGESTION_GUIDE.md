# CMS Manager - Rhythmyx Subscription Guide

**Software Version:** 2.0.6

**Authoring Date:** 2-12-2015

## Background

CMS Manager is the successor to the CM Syndication Rhythmyx plugin. CMS Manager adds HHS Syndication subscription support for non-Rhythmyx systems, a more robust content delivery service, and better handling and reporting.

## Creating a Rhythmyx Subscription

***Note:*** *The Rhythmyx instance you are using must already be configured for the importation of syndicated content.*

### 1. Choose the Destination Folder

It is expected that Rhythmyx user's will access CMS Manager using a right-click menu entry that has been added to the Rhythmyx Content Explorer.

From the Rhythmyx Content Explorer, right-click the folder where CMS Manager will create the content item associated with the subscription.

Click *Syndication -> Import Syndicated Content*

CMS Manager will open in a new browser tab or window, depending on your browser preferences.

### 2. Login to CMS Manager

Unlike it's predecessor, CM Syndication, CMS Manager requires a login. This is because CMS Manager has administrator-only features and providing subscription creation and management features for external Syndication partners.

Login to CMS Manager using the credentials you have been provided by the system administrator's. If you cannot login or have locked your account, click the email address at the bottom of the login page to request support.

![Login](https://dl.dropboxusercontent.com/u/266640464/Syndication%20Document%20Authoring/CMS%20Manager/images/login.png)

### 3. Create a New Subscription

After a successfully login, you will be redirected to the *Create Rhythmyx Subscription* form.

![Create Rhythmyx Subscription](https://dl.dropboxusercontent.com/u/266640464/Syndication%20Document%20Authoring/CMS%20Manager/images/create-form.png)

Take note of the URL in the address bar. It should look similar to the following:

https://cmsmanager.digitalmedia.hhs.gov/rhythmyxSubscription/create?sys_folderid=148008&instance=flu

The *instance* query parameter in the URL identifies the Rhythmyx instance that will be associated with the new subscription. The *sys_folderid* parameter specifies the ID of the Rhythmyx folder where the content item will be created.

Notice that CMS Manager has preselected the *Rhythmyx Instance* field to match the *instance* parameter, and has selected the Rhythmyx content types that have the fields required by CMS Manager. Likewise, the *Target Folder* field has been auto-filled with the *sys_folder* parameter.

These fields are provided by Rhythmyx and allow CMS Manager to assist in filling out the form. If you attempt to create a new subscription without without right-clicking from the Rhythmyx Content Explorer, you will need to know the *Target Folder*, *Content Type* and *Rhythmyx Instance* in advance.

#### 3.1 Select a Source URL

To create a new subscription, you must provide the source URL of a syndicated media item.

CMS Manager has a built-in search feature that will allow you to browse the media items listed in the [HHS Media Services API](https://api.digitalmedia.hhs.gov/).  You can access the built-in search by clicking the *Search* button.

![Search](https://dl.dropboxusercontent.com/u/266640464/Syndication%20Document%20Authoring/CMS%20Manager/images/search.png)

When you have found the media item you want to import, click the *Select* button on the item's row. The *Source URL* will be auto-filled with value of the select item.

##### Additional Search Options

Optionally, you can search for syndicated media uing the [HHS Syndication Storefront](https://digitalmedia.hhs.gov/). You will need to manually copy the source URL of the desired media item and paste it into the *Source URL* field in form.

#### 3.2 About the Rhythmyx Workflow Transitions

Content items created by CMS Manger can be configured to execute  Rhythmyx workflow transitions for each CMS Manager subscription event.

There are three events handled by CMS Manager:

1. Import - User has created a subscription. CMS Manager will attempt to create a Rhythmyx content item, using the content from the syndicated media item.
2. Update - An update to a syndicated media item has triggered CMS Manager to update to the associated Rhythmyx content item.
3. Delete - A syndicated media item was removed from the Syndication Storefront, triggering CMS Manager to execute a set of Rhythmyx workflow changes (typically to move an item from the published state to an unpublished state).

You can configure a set of Rhythmyx workflow transitions to execute for the three event types listed above. Additionally, you can toggle an auto-publish state. The auto-publish option allows you to move a content item directly to a published state after an update, for example.

You can save a set of workflow transitions for use by all/most subscriptions you will create, or configure a completely different set of transitions on a per subscription basis. The predecessor to CMS Manager, CM Syndication, only allowed one global set of workflow transitions and individual workflow configuration was not allowed.

#### 3.3 Configure the Rhythmyx Workflow Transitions

For any the the *...Transitions* fields, you can add a comma separated list of Rhythmyx workflow transition trigger names. Consider the following Rhythmyx Workflow transition transition:

![Rhythmyx Workflow Transition](https://dl.dropboxusercontent.com/u/266640464/Syndication%20Document%20Authoring/CMS%20Manager/images/rhythmyx-transition.png)

In the above picture, the trigger name would be "Approve", from the field *Trigger*.

Below is an example of how you would configure non-auto-publish transitions for this item, saving this set of workflow transitions for future subscriptions. Each time you create a subsequent subscription, the *...Transitions* fields will be auto-filled with the saved values.

![Rhythmyx Workflow](https://dl.dropboxusercontent.com/u/266640464/Syndication%20Document%20Authoring/CMS%20Manager/images/cmsmanager-workflow.png)

#### 3.4 Create the Subscription

After filling out the required form fields and optionally the Rhythmyx workflow transitions, click the *Create* button. You should see a status message indicated that the subscription was created.

### 4. Rhythmyx Subscription List

You can see the state of all created subscriptions by click the *Subscriptions -> Rhythmyx Subscriptions* menu item at the top of the page.

![Subscription List](https://dl.dropboxusercontent.com/u/266640464/Syndication%20Document%20Authoring/CMS%20Manager/images/subscription-list.png)

There are 4 meaningful *Status* messages:

1. Pending - CMS Manager has not yet created the Rhythmyx content item.
2. Failed - The initial attempt to create the content item failed.
3. Updated - The content item has been created or updated successfully.
4. Update Failed - The initial import was successful, but the most recent update to the content item has failed.

The *Error Code* field contains a code that the administrators of the system can use to locate the source of the error in the log files. Please notify the administrators if items are failing and you can't identify the cause on the Rhythmyx side.

Common reasons content items fail are due to Rhythmyx validation errors and having a content items of the same existing in the target folder.
