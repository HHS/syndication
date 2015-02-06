# Syndication Desktop Client V1.0.0

## How to Build

The build process currently relies on IntelliJ since the GUI was constructed using the IntelliJ GUI form builder tools. It 
may be possible to build using gradle, but no research has been done in that regard.

1. Open the project in IntelliJ, the project file is included in the repo
2. From the build menu, select "Build Artifacts".
3. A Popup menu will appear in the active editor, select SyndicationDesktopClient -> Build
4. Find the built application in <Project Directory>/out/artifacts/SyndicationDesktopClient
5. The Directory will contain the application jar, and its required libs. 

*The application expects to be run from within the SyndicationDesktopClient folder. You can rename it, but do not remove
the jar from its relative access to the libs and data (data is generated at runtime) directories*

## Runtime Requirements

- Java 1.6+ (the app was built in a 1.7 environment, but should work with anything newer than 1.5)
- Internet connection
- A Syndication API instance to publish to
- Authorization keys for the Syndication server

## Configuration

The configuration is stored in a local database which lives in data/localDatabase.h2.db. This folder/file is generated during the first launch of the app. Data can be completely reset by deleting the data/ directory, but keep in mind this will delete keys, the saved Syndication API url, and all publishing records.

**First launch:** You will be prompted to input the address of a Syndication API server. If the server cannot be reached (because the url is incorrect, or the server is not publicly accessible), an error message will be displayed. You cannot precede beyond this step without having a real working Syndication instance. The URL the system expects is the base path to the Syndication API application, for instance: *http://ctacdev.com:8090/Syndication* or *https://digitalmedia.hhs.gov/syndication*. It should not include any additional path information.

*If you visited a valid URL in your browser, you would be presented with the Swagger API documentation*

Once a server address has been added, before you will be able to publish, you will need to provide the client with your security key set. This is the set of three keys an administrator of the Syndication API will provide you when your publishing account is created. You have the option of typing or copy/pasting the keys into the individual text fields in thr Settings tab, or you can paste a json block from the email you were sent into the "Import Keys" text area, and then click the "Import Keys button" to automatically parse the JSON and auto-populate the individual key fields.

*Make sure to click 'Save Settings' after any changes or imports or the changes may not stick.*

You should now be ready to start publishing content.

## Single Publish
The application provides two basic publishing modes at this time - one-off single publish events, and bulk publishing. Publishing a single item does keep any local history or records, but is an excellent way to test a system or page quickly in a staging environment.

The URL field expects the complete public URL to the content you wish to publish. This content must be available and visible to the web before it can be published. It must also contain the standard Syndication markup (a div element with class 'syndicate' surrounding the content to publish). If these requirements have been met, select a language and organization from the provided dropdown menus. If the language or organization you need are not available, please contact a syndication administrator and notify them of your requirements.

The description is optional but strongly recommended. Content without descriptions is hard to find, and not very useful in a storefront environment.

To verify the requirements outlined above, there is a 'Verify' button. Clicking will:

1. Check that the URL is valid and accessible
2. Check for syndication markup (div class='syndicate')
3. Attempt to extract the content

If everything looks valid, several status messages will be displayed. If there were errors, details should be provided in the message box.

If the content verifies, you can click the 'Publish Now' button. A successful publish will result in the Syndication API containing your item, and the Storefront showing your item under 'Newest Media'.

##Bulk Publish
The bulk publish essentially works the same way as the single item publish, except that it repeats the process for all records stored. The settings entered in the bulk publish tab are stored in the local database and will continue to be available until they are deleted or the database is reset.

There is no pre-publish verification on bulk items at this time, so ideally, content should be verified using the single item tools before being added to the bulk publish list. Successful and failed publish items are identified with icons, and any errors encountered are logged to the message text area.