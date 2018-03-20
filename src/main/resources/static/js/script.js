var populatePerkList = function() {
	$.getJSON('/perks', function(data) {
		$('#top-perks').empty();
		var perks = data._embedded.perks;
		for (var i = 0; i < perks.length; i++) {
			$('#top-perks').append(
				'<div class="section perk">'+perks[i].name+'</div>'
			);
		}
	});
};

var goSearch = function() {
	$('#search-bar').focus();
};

var goDiscover = function() {
	$('#search-bar').focus();
};

var goHome = function() {
	$('#search-bar').focus();
};

var goAccount = function() {
	$('#search-bar').focus();
};

var goPerk = function() {
	$('#search-bar').focus();
};

$(document).ready(function() {
	populatePerkList();

	$('#menu-search').on('click', function() {
		goSearch();
	})

	$('#menu-discover').on('click', function() {
		goDiscover();
	})

	$('#menu-home').on('click', function() {
		goHome();
	})

	$('#menu-account').on('click', function() {
		goAccount();
	})

	$('#menu-perk').on('click', function() {
		goPerk();
	})
});