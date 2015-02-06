// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery
//= require jquery.magnific-popup.min.js
//= require_self

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

jQuery(function(){
    $(document).on('click', '.expando', function(event){
        var id = $(this).attr('mediaid');
        $(this).children('i').toggleClass('fa-plus-circle').toggleClass('fa-minus-circle');
        $('#desc_'+id).toggleClass("collapsedMediaDescription");
        $('#medialinkbuttons_'+id).toggle();
    });

    $('.popup-link').magnificPopup({
        showCloseBtn:true,
        closeBtnInside: true,
        type: 'ajax',
        ajax:{
            settings:{
                contentType: "application/json",
                dataType: 'JSONP'
            }
        },
        callbacks:{
            parseAjax: function(mfpResponse){
                mfpResponse.data = "<div class='pop'>" + mfpResponse.data.results[0].content + "</div>"
            }
        }
    });

    $('.popup-form').magnificPopup({
        showCloseBtn:true,
        closeBtnInside: true,
        type: 'ajax'
    });
});