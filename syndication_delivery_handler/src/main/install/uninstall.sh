#!/bin/bash

version="0.3.0"
configFile="${HOME}/syndication_delivery_handler_config.groovy"
springConfig="${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/config/user/spring/syndication_delivery_handler.xml"
jarFile="${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/syndication-delivery-handler-${version}.jar"
keyAgreement="${HOME}/syndication_key_agreement.json"

echo ""

if [ `id -un` = "root" ]; then
    echo "This script cannot be run as root"
    echo "You should be running this script as the same user that owns the Rhythmyx installation directory"
	exit 1
fi

echo "You are about to run this script as `id -un`"
echo "You should be running this script as the same user that owns the Rhythmyx installation directory"
echo "Would you like to continue? [y/n]: "

read confirm

if [ "$confirm" = "n" ]; then
	exit 1
fi

if [ ! -d "${HOME}/Rhythmyx" ]; then
	echo "The current user's HOME directory is set to ${HOME} and the directory ${HOME}/Rhythmyx does not exist"
	exit 1
fi

rm ${jarFile} > /dev/null 2>&1
rm ${springConfig} > /dev/null 2>&1
mv ${configFile} "${configFile}.backup" > /dev/null 2>&1
rm ${keyAgreement} > /dev/null 2>&1

echo ""
echo "Uninstall finished!"
echo ""
echo "You will need to remove ${HOME}/syndication-delivery-handler-${version}.zip and ${HOME}/syndication-delivery-handler-${version} manually"
echo ""
echo "If not already done, see the 'Uninstall Procedures' section in the README.md file for instructions on removing the publishing contexts associated with this delivery handler."
echo ""

exit 0
