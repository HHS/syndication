// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap.min.js
//= require imagesloaded.pkgd.min.js
//= require masonry/masonry.pkgd.min.js
//= require_self/**


$(document).ready(function(){
  var $container = $('#js-masonry');
  // initialize
  $container.masonry({
    // columnWidth: 254,
    itemSelector: '.panel',
    transitionDuration: 0.4
  });
  $container.imagesLoaded(function(){
    setTimeout(function(){$container.masonry();}, 100);
  });
});