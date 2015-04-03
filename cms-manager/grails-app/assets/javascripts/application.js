//= require jquery
//= require_self
//= require bootstrap
//= require jquery.dataTables.min

$(document).ready(function()
{
    $('#spinner').ajaxStart(function() {
        $(this).fadeIn();
    }).ajaxStop(function() {
        $(this).fadeOut();
    });

    var subscribersLink = document.getElementById('subscribers-link');
    var subscriptionsLink = document.getElementById('subscriptions-link');
    var usersLink = document.getElementById('users-link');

    if ( $( "#subscribers-link" ).length ) {

        if (subscribersLink.addEventListener) {
            subscribersLink.addEventListener('click', function() {
                activate('subscribers-nav');
            });
        }
        else { //IE 8 specific code to support event
            subscribersLink.attachEvent('onclick', function () {
                activate('subscribers-nav');
            });
        }
    }

    if (subscriptionsLink.addEventListener) {
        subscriptionsLink.addEventListener('click', function() {
            activate('subscriptions-nav');
        });
    }
    else { //IE 8 specific code to support event
        subscriptionsLink.attachEvent('onclick', function () {
            activate('subscriptions-nav');
        });
    }


    if ( $( "#users-link" ).length ) {
        if (usersLink.addEventListener) {
            usersLink.addEventListener('click', function() {
                activate('users-nav');
            });
        }
        else { //IE 8 specific code to support event
            usersLink.attachEvent('onclick', function () {
                activate('users-nav');
            });
        }
    }
});

function activate(navId) {
    deactivateAll();
    var nav = document.getElementById(navId);
    nav.setAttribute('class', nav.getAttribute('class') + ' active');
}

function deactivate(navId) {
    var nav = document.getElementById(navId);
    nav.setAttribute('class', nav.getAttribute('class').replace(' active', ''));
}

function deactivateAll() {
    deactivate('subscribers-nav');
    deactivate('subscriptions-nav');
    deactivate('users-nav');
}

function useAutoPublish(autoPublish) {
    if (autoPublish) {
        document.getElementById('rhythmyxWorkflow.updateTransitions').setAttribute('disabled', 'disabled');
        document.getElementById('rhythmyxWorkflow.updateAutoPublishTransitions').removeAttribute('disabled');
        document.getElementById('rhythmyxWorkflow.deleteTransitions').setAttribute('disabled', 'disabled');
        document.getElementById('rhythmyxWorkflow.deleteAutoPublishTransitions').removeAttribute('disabled');
    } else {
        document.getElementById('rhythmyxWorkflow.updateTransitions').removeAttribute('disabled');
        document.getElementById('rhythmyxWorkflow.updateAutoPublishTransitions').setAttribute('disabled', 'disabled');
        document.getElementById('rhythmyxWorkflow.deleteTransitions').removeAttribute('disabled');
        document.getElementById('rhythmyxWorkflow.deleteAutoPublishTransitions').setAttribute('disabled', 'disabled');
    }
}

function confirmDelete(entityName) {
    if (confirm('Are you sure you want to delete this ' + entityName.toLowerCase() + '?')) {
        return document.forms[2].submit();
    }
}
