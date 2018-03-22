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

// loader

var startLoad = function() {
	$('#app-loader').addClass('loading');
};

var stopLoad = function() {
	$('#app-loader').removeClass('loading');
};

// authentication

var loggedIn = false;

var applyUser = function(user) {
	loggedIn = true;

	$('#page-account').addClass('logged-in');

	$('#menu-account .name').addClass('logged-in');
	$('#menu-account .name').text(user.name);
};

var authenticate = function() {
	$.getJSON('/authenticate', function(data) {
		stopLoad();
		if (Object.keys(data).length === 0 && data.constructor === Object) {
			applyUser(data)
		}
	})
	.fail(function() {
		stopLoad();
	});
};

var tryLogIn = function(username, password) {
	startLoad();
	$.ajax({
		type: 'POST',
		url: '/login',
		data: $.param({
			username: username,
			password: password
		}),
		contentType: 'application/x-www-form-urlencoded'
	})
	.done(function(data) {
		stopLoad();
	})
	.fail(function() {
		stopLoad();
		applyUser({firstName: 'Vanja', lastName: 'Veselinovic'});
	});
};

/* ---------- PAGES ---------- */

var activatePage = function(page, menu) {
	if (menu === undefined) menu = page;

	$('.footer-menu-item.active').removeClass('active');
	$('#menu-'+menu).addClass('active');

	currentPage = page;
	$('.page.active').removeClass('active');
	$('#page-'+page).addClass('active');
};

/* ---------- FOOTER ---------- */

var goSearch = function() {
	activatePage('home', 'search');
	$('#search-bar').focus();
};

var goDiscover = function() {
	activatePage('discover');
};

var goHome = function() {
	activatePage('home');
};

var goAccount = function() {
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

	// search

	$('#search-bar').on('blur', function() {
		if (currentPage === 'home') {
			goHome();
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

	$('#login-button').on('click', function() {
		if ($('#email').val().length > 0 && $('#password').val().length > 0) {
			tryLogIn($('#email').val(), $('#password').val());
		}
	})

	// perk

	$('#menu-perk').on('click', function() {
		goPerk();
	});
});