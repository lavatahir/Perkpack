/* ---------- MAIN --------- */

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

// authentication

var applyUser = function(user) {
	$('#menu-account .name').addClass('logged-in');
	$('#menu-account .name').text(user.name);
};

var authenticate = function() {
	$.getJSON('/authenticate', function(data) {
		if (Object.keys(data).length === 0 && data.constructor === Object) {
			applyUser(data)
		}
	})
	.fail(function() {
		applyUser({name: 'Vanja'});
	});
};

/* ---------- PAGES ---------- */

var activatePage = function(name) {
	currentPage = name;
	$('.page.active').removeClass('active');
	$('#page-'+name).addClass('active');
};

/* ---------- FOOTER ---------- */

var goSearch = function() {
	activatePage('home');
	$('#search-bar').focus();
};

var goDiscover = function() {
	activatePage('discover');
};

var goHome = function() {
	activatePage('home');
};

var goAccount = function() {
	authenticate();
	activatePage('account');
};

var goPerk = function() {
	activatePage('perk');
};

/* ---------- PAGE LOAD ---------- */

var currentPage = 'home';

$(document).ready(function() {
	populatePerkList();
	authenticate();

	// footer

	$('.footer-menu-item').on('click', function() {
		$('.footer-menu-item.active').removeClass('active');
		$(this).addClass('active');
	});

	// search

	$('#search-bar').on('blur', function() {
		if (currentPage === 'home') {
			$('.footer-menu-item.active').removeClass('active');
			$('#menu-home').addClass('active');
		}
	});

	$('#menu-search').on('click', function() {
		goSearch();
	});

	// discover

	$('#menu-discover').on('click', function() {
		goDiscover();
	});

	// home

	$('#menu-home').on('click', function() {
		goHome();
	});

	// account

	$('#menu-account').on('click', function() {
		goAccount();
	});

	// perk

	$('#menu-perk').on('click', function() {
		goPerk();
	});
});