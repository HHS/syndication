// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery
//= require slick/slick.min.js
//= require_self/**

"use strict";

var HHSSFMV = HHSSFMV || {};
HHSSFMV.storedContent = []; // RETURNED CONTENT IS STORED IN THIS ARRAY

HHSSFMV.syndicationRequest = function(url, callback){
    var callbackName = 'jsonp_callback_' + Math.round(100000 * Math.random());
    window[callbackName] = function(data) {
        delete window[callbackName];
        document.body.removeChild(script);
        callback(data);
    };

    var script = document.createElement('script');
    script.src = url + (url.indexOf('?') >= 0 ? '&' : '?') + 'callback=' + callbackName;
    document.body.appendChild(script);

    script.onerror = function(){
    	throw 'ERROR LOADING JSONP REQUEST';
    }
}

HHSSFMV.setup = function () {
  var handlebarsMedia; // CONTENT ITEMS HANLDLEBARS VARIABLE
    $('#HHSSFMV > .slick-container').slick({
        accessibility: true,
        arrows: false,
        asNavFor: '.slick-navigation',
        draggable: false,
        infinite: true,
        slidesToScroll: 1,
        slidesToShow: 1,
        variableWidth: true 
    });

    $('#HHSSFMV > .slick-navigation').slick({
        accessibility: true,
        arrows: true,
        asNavFor: '.slick-container',
        centerMode: true,
        centerPadding: '15%',
        dots: false,
        infinite: true,
        focusOnSelect: true,
        slidesToShow: 3,
        slidesToScroll: 1
    });

    $('#HHSSFMV > .slick-container').on('beforeChange', function(event, slick, currentSlide, nextSlide){
        var activeSlide = $('#HHSSFMV .slick-active');
        if(activeSlide.hasClass('video')){
          var ytUrl = activeSlide.find('iframe').attr('src');
          activeSlide.find('iframe').attr('src', '');
          activeSlide.find('iframe').attr('src', ytUrl);
        }
    });
};
