#!/bin/bash

echo ""

version="0.2.5"
configFile="syndication_delivery_handler_config.groovy"
keyAgreement="syndication_key_agreement.json"
springConfig="syndication_delivery_handler.xml"
jarFile="syndication-delivery-handler-${version}.jar"

# ENCRYPTS THE KEYAGREEMENT FILE SO ITS NOT IN PLAIN TEXT, AND ZEROS OUT THE KEY AGREEMENT
function encryptKeyAgreement() {

    keyAgreementEncrypted="${HOME}/syndication_key_agreement.dat"
    keySubject="/C=US/ST=DC/L=Washinton/O=HHS/OU=HHS Syndication/CN=Syndication Admin/emailAddress=syndicationAdmin@hhs.gov"
    publicKey="${HOME}/.ssh/syndication_pub.pem"
    privateKey="${HOME}/.ssh/syndication.pem"

    openssl req -x509 -subj "${keySubject}" -nodes -newkey rsa:1024 -keyout ${privateKey} -out ${publicKey}  > /dev/null 2>&1
    openssl smime -encrypt -aes256 -in ${keyAgreement} -binary -outform DEM -out ${keyAgreementEncrypted} ${publicKey}  > /dev/null 2>&1

    echo "The key agreement file has been encrypted and copied to ${keyAgreementEncrypted}"

    echo "{
        \"Public Key\": \"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000=\",
        \"Entity Name\": \"somewhere.gov\",
        \"Private Key\": \"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000==\",
        \"Secret Key\": \"00000000000000000000000000000000000000000000000000000000000000000000000000000000000000==\"
    }" > ${keyAgreement}
}

# MAKES A BACKUP OF THE OLD CONFIG AND COPY A NEW COMMENTED OUT CONFIG TO THE USER HOME
function installExternalConfigs() {

    cp ${HOME}/${configFile} "${HOME}/${configFile}.backup" > /dev/null 2>&1
    rm ${HOME}/${configFile} > /dev/null 2>&1
    cp ${configFile} ${HOME}/${configFile} > /dev/null 2>&1

    echo "The configuration file has been copied to ${HOME}/${configFile}"

    encryptKeyAgreement
}

# UPDATES THE CONFIG AND KEY AGREEMENT FILES IN THE JAR ITSELF
function installInternalConfigs() {
    zip -u ${jarFile} ${configFile}
    zip -u ${jarFile} ${keyAgreement}
}

# PROMPTS USER TO CHOOSE INTERNAL OR EXTERNAL CONFIG
function installConfigs() {

    echo "Use the external config (e) or update the internal (i) config in the delivery handler JAR? [e/i]: "
    read confirm

    if [ "$confirm" = "e" ]; then
        installExternalConfigs
    fi

    if [ "$confirm" == "i" ]; then
        installInternalConfigs
    fi
}

# INSTALLS THE DELIVERY HANDLER JAR TO THE RHYTHMYX WAR LIB DIRECTORY
function installJar() {
    rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/${jarFile} > /dev/null 2>&1
    chmod 750 ${jarFile}
    cp -rp ${jarFile} ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib
}

# INSTALLS THE DELIVERY HANDLER SPRING CONFIG FILE TO THE RHYTHMYX SPRING DIRECTORY
function installSpringConfig() {
    rm ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/config/user/spring/${springConfig} > /dev/null 2>&1
    chmod 740 ${springConfig}
    cp -rp ${springConfig} ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/config/user/spring
}

# SETS RHYTHMYX DIRECTORY PERMISSIONS TO CORRECT VALUES IF THEY HAVE CHANGED
function setPermissions() {
    chmod g+s ${HOME}
    chmod g+s ${HOME}/Rhythmyx/AppServer/server/rx/deploy/
    chmod g+s ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/config/user/spring
    chmod g+s ${HOME}/Rhythmyx/AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib
}

# CHECKS FOR ROOT USER, AS EXECUTING THIS SCRIPT AS ROOT WILL SCREW UP RHYTHMYX DIRECTORY PERMISSIONS
function checkForRoot() {
    if [ `id -un` = "root" ]; then
        echo "This script cannot be run as root"
        echo "You should be running this script as the same user that owns the Rhythmyx installation directory"
        exit 1
    fi
}

# CONFIRMS THE CURRENT USER OWNS THE RHYTHMYX INSTALLATION DIRCETORY
function confirmUser() {
    echo "You are about to run this script as `id -un`"
    echo "You should be running this script as the same user that owns the Rhythmyx installation directory"
    echo "Would you like to continue? [y/n]: "

    read confirm

    if [ "$confirm" = "n" ]; then
        exit 1
    fi
}

# CHECKS THAT RHYTHMYX IS INSTALLED IN THE CURRENT USER'S HOME DIRECTORY
function checkRhythmyxInstalled() {
    if [ ! -d "${HOME}/Rhythmyx" ]; then
        echo "The current user's HOME directory is set to ${HOME} and the directory ${HOME}/Rhythmyx does not exist"
        exit 1
    fi
}

# CHECKS THAT THE FILE IN THE UNZIPPED DISTRIBUTION ARE OWNED BY THE CURRENT USER
function checkDistributionPermissions() {
    for f in ${jarFile} ${springConfig} ${configFile} ${keyAgreement}
    do
     if [ ! -O $f ]; then
        echo "Script cannot run"
        echo "You must first set ownership of all files in this distribution to the same user that owns the Rhythmyx installation directory"
        exit 1
     fi
    done
}

# DO THE INSTALL

checkForRoot
confirmUser
checkRhythmyxInstalled
checkDistributionPermissions
setPermissions
installConfigs
installJar
installSpringConfig

echo ""
echo "Install finished!"

exit 0
