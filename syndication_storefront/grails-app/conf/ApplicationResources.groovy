modules = {
    application {
        resource url:'js/application.js'
    }

    magnific{
        resource url:'js/jquery.magnific-popup.min.js'
        resource url:'css/magnific-popup.css'
    }

    metisMenu{
        resource url:'js/plugins/metisMenu/jquery.metisMenu.js'
    }

    bootstrap{
        resource url:'js/bootstrap.min.js'
        resource url:'css/bootstrap.min.css'
    }

    tokenInput{
        dependsOn 'jquery'
        resource url:'js/tokenInput/jquery.tokeninput.js'

        resource url:'css/tokenInput/token-input-facebook.css'
        resource url:'css/tokenInput/token-input-mac.css'
        resource url:'css/tokenInput/token-input.css'
    }
}