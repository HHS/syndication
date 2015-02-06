#!/bin/bash

echo ""
echo "This script will remove all CM Syndication files except ${HOME}/cmsyndication.properties"
echo ""
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

cp ${HOME}/cmsyndication.properties ${HOME}/cmsyndication.properties.backup
rm ${HOME}/cmsyndication.properties.default > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/cm-syndication-*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/commons-http-*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/httpcore-*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/httpclient-*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/jlo-*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/cmsyndication.war > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/config/user/spring/syndication.delivery.handler.xml > /dev/null 2>&1

# Possible Legacy Jars
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/rhythmyx-ws*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/cmsyndication.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/cms-prototype*.jar > /dev/null 2>&1
rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/syndication-delivery-handler*.jar > /dev/null 2>&1

exit 0
