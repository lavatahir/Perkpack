/* ---------- MAIN --------- */

var getPerkTileHTML = function(perk) {
	return '<div class="section perk-tile"><div class="perk-vote"><div class="perk-upvote perk-vote-button" onclick=vote(this,1)><i class="material-icons">thumb_up</i></div><div class="perk-score">'+perk.score+'</div><div class="perk-downvote perk-vote-button" onclick=vote(this,-1)><i class="material-icons">thumb_down</i></div></div><div class="perk-info"><div class="perk-name subtitle"><span>'+perk.name+'</span></div><div class="perk-desc">'+perk.description+'</div><div class="perk-cat"><i class="material-icons">restaurant</i></div></div></div>';
}

var populatePerkList = function() {
	$.getJSON('/perks', function(data) {
		$('#top-perks').empty();
		var perks = data._embedded.perks;
		for (var i = 0; i < perks.length; i++) {
			$('#top-perks').append(getPerkTileHTML(perks[i]));
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
	$('.first-name').text(user.firstName);

	activatePage('home');
};

var authenticate = function() {
	/*
	$.getJSON('/authenticate', function(data) {
		stopLoad();
		if (Object.keys(data).length === 0 && data.constructor === Object) {
			applyUser(data);
		}
	})
	.fail(function() {
		stopLoad();
	});
	*/
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
		console.log(data);
		applyUser(JSON.parse(data));
	})
	.fail(function() {
		stopLoad();
		$('#email, #password').addClass('invalid');
		//applyUser({firstName: 'Vanja', lastName: 'Veselinovic'});
	});
};

/* ---------- PAGES ---------- */

var activatePage = function(page, menu) {
	if (menu === undefined) menu = page;

	$('.navbar-menu-item.active').removeClass('active');
	$('#menu-'+menu).addClass('active');

	currentPage = page;
	$('.page.active').removeClass('active');
	$('#page-'+page).addClass('active');

	if (page !== 'perk') {
		$('#menu-perk .big.button').removeClass('active');
		$('#menu-perk .button-expand').show();
	}
};

/* ---------- NAVBAR ---------- */

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
	$('#menu-perk .big.button').addClass('active');
	setTimeout(function() {
		activatePage('perk');
	}, 200);
	setTimeout(function() {
		$('#menu-perk .button-expand').hide();
	}, 700);
};

/* ---------- VOTING ---------- */

var vote = function(element, vote) {
	var perkName = $($(element)[0].parentElement.parentElement).find('.perk-name span').text();

	if ($(element).hasClass('voted')) {
		vote *= -1;
	}
	else {
		if ($($(element)[0].parentElement).find('.voted').length > 0) {
			vote *= 2;
		}
	}

	var currentScore = parseInt($($(element)[0].parentElement).find('.perk-score').text());
	var newScore = currentScore + vote;

	$.ajax({
        method: 'POST',
        url: '/perks/vote',
        contentType: 'application/json',
        data: '{"name": "' + perkName + '", "vote": "' + newScore + '"}'
    }).done(function(perk) {
    	if ($(element).hasClass('voted')) {
			$(element).removeClass('voted');
		}
		else {
			$($(element)[0].parentElement).find('.perk-vote-button').removeClass('voted');
			$(element).toggleClass('voted');
		}
    	$($(element)[0].parentElement).find('.perk-score').text(perk.score);
    });
}

/* ---------- PAGE LOAD ---------- */

var setButtonExpandScale = function() {
	var screenSize = Math.max($(document).width(), $(document).height());
	var buttonSize = $('#menu-perk .big.button').width();

	$('<style>.big.button.active .button-expand { transform: scale('+(2*screenSize/buttonSize)+'); }</style>').appendTo(document.documentElement);
};

$(window).on('resize', function() {
	setButtonExpandScale();
});

var currentPage = 'home';

$(document).ready(function() {
	populatePerkList();
	authenticate();

	// general

	$('.text-input').on('click', function() {
		$('.text-input').removeClass('invalid');
	});

	// search

	$('#search-bar').on('blur', function() {
		if (currentPage === 'home') {
			goHome();
		}
	});

	$('#menu-search').on('click', function() {
		if (currentPage !== 'search') goSearch();
	});

	// discover

	$('#menu-discover').on('click', function() {
		if (currentPage !== 'discover') goDiscover();
	});

	// home

	$('#menu-home').on('click', function() {
		goHome();
	});

	// account

	$('#menu-account').on('click', function() {
		if (currentPage !== 'account') goAccount();
	});

	$('#login-button').on('click', function() {
		if ($('#email').val().length > 0 && $('#password').val().length > 0) {
			tryLogIn($('#email').val(), $('#password').val());
		}
		else {
			$('#email, #password').addClass('invalid');
		}
	})

	// perk

	setButtonExpandScale();

	$('#menu-perk').on('click', function() {
		if (currentPage !== 'perk') goPerk();
	});
});