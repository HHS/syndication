# Syndication Delivery Handler 0.3.0 Installation and Configuration Guide

## Build Number

The build number associated with this release of the *Syndication Delivery Handler* is **0.3.0**. This number should be indicated when requesting support or administration of this release.

## Nomenclature

- **Delivery Handler** - The software described by this document that provides publishing capabilities to Syndication.
- **Syndication** - The syndication network as a whole including efforts put forth by CDC and instances deployed at various agencies such as CDC, FDA, and HHS.
- **CMS Manager** - The web application responsible for issuing REST API security key agreements to CMS entities (i.e. hhs.gov).
- **Syndication REST API** - Officially known as the HHS Media Services REST API).
- **Rhythmyx** - The Rhythmyx CM web application.
- **user** - A Rhythmyx content editor (user).
- **${USER}** - The UNIX user account that owns the Rhythmyx installation directory.
- **${HOME}** - The home directory of the UNIX user account that owns the Rhythmyx installation directory.

## About

The Syndication Delivery Handler is a Rhythmyx delivery handler that publishes content to the REST API. It replaces the legacy delivery handler from the *CM Syndication* software package.

## Distribution Contents

The delivery handler is distributed as a zip file, *syndication-delivery-handler-0.2.6.zip*, and contains the following:

- **install.sh** - The installer script that copies the distribution's files to their respective locations.
- **syndication_delivery_handler_config.groovy** - The configuration file for the delivery handler. See the *Installation and Configuration* section for details.
- **syndication_delivery_handler.xml** - The Spring XML configuration file that enables the delivery handler in Rhythmyx.
- **publishing_template.json** - The Velocity template used to publish content to the REST API.
- **key_agreement.json** - The configuration that provides the security keys used to authenticate the delivery handler when publish requests are made to the REST API.
- **syndication-delivery-handler-0.2.6.jar** - The JAR file containing all the required Java classes and resources needed by the delivery handler.

## Installation and Configuration

### Install the Distribution

1. SCP or FTP the distribution ZIP file to the Rhythmyx host and put it in the ${HOME} directory and make sure the distribution ZIP is owned by ${USER}.

2. Check that the *unzip* command is installed by issuing the following command:
```bash
if [ -f /usr/bin/unzip ]; then echo "unzip is installed"; fi
```
3. If *unzip* is installed, the previous command would display "unzip is installed". If *unzip* is not currently installed, run the following command:
```bash
sudo apt-get install unzip
```
4. Unzip the distribution with the following command:
```bash
unzip syndication-delivery-handler-0.2.6.zip
```

### Configure the REST Client

#### Set the Syndication REST API Connection Properties

1. Open the *syndication_delivery_handler_config.groovy* configuration file in the distribution root and edit the following two properties:

	```bash
	publish.url = 'http://localhost:8080/syndication/api/v1/media'
	server.base.url = 'http://localhost:9992'
	```

2. For *publish.url*, use the REST API's publishing endpoint URL. For *server.base.url*, use the Rhythmyx server's hostname and port number.
3. When the install.sh script is run during the install process, a config file will be created at *${HOME}/syndication_delivery_handler_config.groovy*.
4. **Optional**: To adjust the connection and socket timeout settings for the REST client, you can uncomment the following properties in the external config file *${HOME}/syndication_delivery_handler_config.groovy*.
```bash
http.connection.timeout = 30
http.socket.timeout = 30
```

#### REST Authentication / API Key

The REST API requires an Authentication header to authenticate any requests made to it's publish endpoint. The delivery handler requires the key agreement issued by CMS Manager for the 'owning' entity (i.e. HHS) of the Rhythmyx instances where the plugin is installed. Please see the CMS Manager user manual for instructions to create and issue a new key agreement.

Once the key agreement has been issued, configure the security keys by editing the key agreement file *${HOME}/syndication_key_agreement.json*, and replacing the entire contents of the file with the key agreement issued by CMS Manager.

When the install.sh is run during the install process, an encrypted version of this file will be created at *${HOME}/syndication_key_agreement.dat*. A private key and a public key are also created in *${HOME}/.ssh*. These keys are used to encrypt the key agreement so it is not in plain text.

### Install the Delivery Handler

1. Run the install script in the root of the distribution by issuing the following commands:
```bash
cd syndication-delivery-handler-0.2.6/
./install.sh
```
2. Select **'e'** at the following prompt:
```bash
Use the external config (e) or update the internal (i) config in the delivery handler JAR? [e/i]:
```

### Configure a Content Type to be Publishable

**Note:** The following instructions configure the *BasicPage* content type for publishing to Syndication. Select a different content type in step 3 if required.

To make a content type publishable to the REST API, perform the following actions from the Rhythmyx Workbench:

1. Click the 'Content Design' tab from the left pane
2. Expand 'Content Types'
3. Double click the 'BasicPage' content type
4. Click the '+' button from the right pane
5. For the 'Field and Field Sets', set:
	- Name = syndicatable
	- Label = Syndicatable:
	- Control = sys_DropDownSingle
	- Source = Local
6. Double click the 'sys_DropDownSingle' cell from the  'Control' column and click the button that appears in the cell
7. Click the 'Choices' button
8. Select the 'Define choices for this control only' radio button
9. Add the following entries:
	- Label: No, Value: No
	- Label: Yes, Value: Yes
10. Set 'Sort order' = Ascending
11. Click 'OK'
12. For the 'Field Properties', set:
	- Data type = text
	- Storage Size = 50
	- Default Value = No
	- Mime type mode = Default
	- Enable searching for this field = checked
	- Required = checked
	- Mnemonic = Leave unselected
	- Show in preview: checked
13. Click the 'Save' button

### Create the Velocity Publishing Template

***Note:*** *This template is only used to publish content to Syndication. It is not used for site publishing.*

The delivery handler uses a custom Velocity template to format the content for publishing to REST API. To create the template, perform the following actions from the Rhythmyx Workbench:

1. Click the 'Assembly Design' tab from the left pane
2. Expand 'Templates'
3. Right click on 'Shared > New > Template'
4. Click the 'Shared' radio button and click 'Next'
5. Set:
	- Assembler = velocity Assembler
	- Output = Text page
	- Global Template = None
6. Click 'Next'
7. Set:
	- Template name = Syndication_Publish
8. Moving all entries from 'Available Communities' to 'Visible to these communities' will make the 'Syndicated_Publish' template visible to all communities. Make changes according to your security policy.
9. Click 'Next'
10. Moving all entries from 'Available Slots' to 'Contained Slots' will make the 'Syndicated_Publish' template available to all slots. Make changes according to your security policy.
11. Click 'Next'
12. Moving all entries from 'Available Content Types' to 'Associated Content Types' will make the 'Syndicated_Publish' template available to all content types. Make changes according to your security policy.
13. Click 'Finish'
14. Click the 'Save' button
15. In the 'Source Tab', paste the following XML document exactly as given without any formatting:
```xml
##make sure the template is set to never publish, not default.
##also make sure not to leave space in the top line.
#set ($display_title = $tools.esc.xml($sys.item.getProperty("rx:displaytitle").String))
<?xml version="1.0" encoding="UTF-8"?>
<json>
<![CDATA[{"language":1,"source":1,"sourceUrl":"$source_url","name":"$display_title"}]]>
</json>
```
16. Click the 'General' Tab
17. Set:
	- Publish = Never
18. Click the 'Save' button
19. Restart the Rhythmyx application server for the changes to take effect.

##### Marking Up Content for Publishing to Syndication

Your content must be properly marked up to be processed by Syndication. Make sure that any content you wish to make available for syndication should use a HTML div with the *syndicate* class name. For example, the following HTML elements are properly marked up for publishing to Syndication:

```xml
<div class="syndicate">
	This is syndicated content!
</div>
<div class="some-other-class-name syndicate">
	<p>
		This is more syndicated content!
	</p>
<div>
```

### Configure the Publishing Design

These are general instructions for configuring the delivery handler and content lists and attaching the content lists to a publishing edition. Per site publishing configuration will dictate the naming of content lists and editions, and specific publishing requirements may require configuration that is out-of-scope for this document.

#### Add the Syndication_Delivery_Handler Delivery Type

1. Log into the Rhythmyx Content Management application
2. Click the 'Publishing Design' tab
3. Click 'Delivery Types' on the left-hand side
4. Click 'Action > Create Delivery Type'
5. Set:
	- Name = Syndication_Delivery_Handler
	- Description = Publishes content to the Syndication REST API
	- Spring Bean Name = syndication
6. Click 'Save'
7. Restart the Rhythmyx application server for the changes to take effect.

#### Create a new Content List

##### To create a 'Full' publish content list, do the following:

1. Log into the Rhythmyx Content Management application
2. Click the 'Publishing Design' tab
3. Click 'Sites' on the left-hand side
4. Click the site to which the delivery handler will be used and then click 'Content Lists'
5. Click 'Action > Create Content List'
6. Set:
	- Name = Syndication_Publish_Full
	- Description = Full publish to the Syndication REST API
	- Incremental = unchecked
	- Delivery Type = Syndication_Delivery_Handler
	- Item Filter = public
	- Generator = Java/global/percussion/system/sys_SearchGenerator
	- query = select rx:sys_contentid, rx:sys_folderid from rx:BasicPage where rx:syndicatable = 'Yes'
	- Template Expander = Java/global/percussion/system/sys_ListTemplateExpander
	- default_template = Syndication_Publish
7. Using a comma separator, modify the above 'query' to add other content types other than rx:BasicPage
8. Click 'Save'

##### To create an 'Incremental' publish content list, do the following:

1. Log into the Rhythmyx Content Management application
2. Click the 'Publishing Design' tab
3. Click 'Sites' on the left-hand side
4. Click the site to which the delivery handler will be used and then click 'Content Lists'
5. Click 'Action > Create Content List'
6. Set:
	- Name = Syndication_Publish_Incremental
	- Description = Incremental publish to the Syndication REST API
	- Incremental = checked
	- Delivery Type = Syndication_Delivery_Handler
	- Item Filter = public
	- Generator = Java/global/percussion/system/sys_SearchGenerator
	- query = select rx:sys_contentid, rx:sys_folderid from rx:BasicPage where rx:syndicatable = 'Yes'
	- Template Expander = Java/global/percussion/system/sys_ListTemplateExpander
	- default_template = Syndication_Publish
7. Using a comma separator, modify the above 'query' to add other content types other than rx:BasicPage
8. Click 'Save'

#### Associate the Content List with a Publishing Edition

1. Click the 'Publishing Design' tab
2. Click 'Sites' on the left-hand side
3. Click the site to which the delivery handler will be used and then click 'Editions'
4. Click the edition to which the delivery handler will be used
5. Click 'Action > Add Content List Association...'
6. Type 'Syndication' in the 'Filter Content List' and click 'Apply'
6. Click the checkbox the Content List that was created in the previous section (i.e. 'Syndication_Publish_Full')
7. Set:
	- Delivery Context = (set to delivery context of which the delivery handler will be used, ex. Publish)
	- Assembly Context = (set to delivery context of which the delivery handler will be used, ex. Publish)
	- Authtype = unselected
8. Click 'Add'
9. Click 'Save'

## Uninstall Procedures

### Remove the publishing contexts

To remove the publishing contexts, perform the following actions within the Rhythmyx Content Explorer:

1. Click the 'Publishing Design' tab
2. Click 'Editions'
3. Click the edition to which the 'Syndication_Publish_Incremental' or 'Syndication_Publish_Full' content list was added
4. Click the radio button of the 'Syndication_Publish_Incremental' or 'Syndication_Publish_Full' content list
5. Click 'Action > Delete Selected Row'
6. Click 'Save'
7. Click 'Unused Content Lists' on the left-hand side
8. Type 'Syndication' in the 'Filter Content List' and click 'Apply'
9. Click the radio button of the 'Syndication_Publish_Incremental' or 'Syndication_Publish_Full' content list
10. Click 'Action > Delete Selected Content List' and then click 'Delete' on the subsequent page
11. Click 'Delivery Types' in the left-hand side
12. Click the radio button for the 'Syndication_Delivery_Handler'
13. Click 'Action'
14. Click 'Action > Delete Selected Delivery Type' and then click 'Delete' on the subsequent page

### Uninstall the delivery handler

Run the uninstall script in the root of the distribution by issuing the following commands:

```bash
cd syndication-delivery-handler-0.2.6/
./uninstall.sh
```
