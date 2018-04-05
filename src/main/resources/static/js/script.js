/* ---------- MAIN --------- */

var getPerkTileHTML = function(perk) {
	return '<div class="section perk-tile"><div class="perk-vote"><div class="perk-upvote perk-vote-button" onclick=vote(this,1)><i class="material-icons">thumb_up</i></div><div class="perk-score">'+perk.score+'</div><div class="perk-downvote perk-vote-button" onclick=vote(this,-1)><i class="material-icons">thumb_down</i></div></div><div class="perk-info"><div class="perk-name subtitle"><span class="the-name">'+perk.name+'</span> - <span>'+perk.cardName+'</span></div><div class="perk-desc">'+perk.description+'</div><div class="perk-cat"><i class="material-icons">'+categories[perk.categoryName].icon+'</i></div></div></div>';
}

var populatePerkList = function(list) {
	$.getJSON('/'+list, function(perks) {
		$('#'+list+'-perks').empty();
		for (var i = 0; i < perks.length; i++) {
			$('#'+list+'-perks').append(getPerkTileHTML(perks[i]));
		}
	});
};

// prompt

var prompt = function() {
	$('.prompt-container').show();
};

var endPrompt = function() {
	$('.prompt-container').hide();
}

// loader

var startLoad = function() {
	$('#app-loader').addClass('loading');
};

var stopLoad = function() {
	$('#app-loader').removeClass('loading');
};

// authentication

var loggedIn = false;
var cardIds = [];

var applyUser = function(user) {
	loggedIn = true;

	$('#page-account').addClass('logged-in');

	$('#menu-account .name').addClass('logged-in');
	$('.first-name').text(user.firstName);
	$('.last-name').text(user.lastName);
	$('.user-email').text(user.email);
	$('.num-votes').text(user.votes.length);

	cardIds = user.cardIds === undefined ? [] : user.cardIds;
	populateUserCards();

	activatePage('home');
};

var authenticate = function() {
	$.getJSON('/account/authenticate', function(data) {
		stopLoad();
		applyUser(data);
	})
	.fail(function() {
		stopLoad();
	});
};

var tryLogIn = function(username, password, fromSignup) {
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
		authenticate();
	})
	.fail(function() {
		stopLoad();
		if (fromSignup) {
			$('#page-signup input').addClass('invalid');
		}
		else {
			$('#email, #password').addClass('invalid');
		}
	});
};

var tryLogOut = function() {
	startLoad();
	$.ajax({
		type: 'GET',
		url: '/logout'
	})
	.done(function() {
		location.reload();
	})
	.fail(function() {
		location.reload();
	});
};

var trySignUp = function(fname, lname, email, password) {
	startLoad();
	$.ajax({
		type: 'POST',
		url: '/account',
		data: '{"firstName": "'+fname+'", "lastName": "'+lname+'", "email": "'+email+'", "password": "'+password+'"}',
		contentType: 'application/json'
	})
	.done(function() {
		stopLoad();
		tryLogIn(email, password, true);
	})
	.fail(function() {
		stopLoad();
		$('#page-signup input').addClass('invalid');
	});
};

/* ---------- CREATE PERK ---------- */

var tryCreatePerk = function(name, description,cardID, category) {
	startLoad();

	$.ajax({
        type: 'POST',
        url: '/perks',
        data: '{"name": "' + name + '", "description": "' + description + '", "category": "/categories/' + category + '", "card": "/cards/'+cardID+'"}',
        contentType: 'application/json',
    })
    .done(function(perk) {
    	stopLoad();
        populatePerkList('top');
        populatePerkList('recommended');
        goHome();
        $('#perk-name').val('');
        $('#perk-desc').val('');
        $('input[name="cat"]').prop('checked', false);
    })
    .fail(function() {
    	stopLoad();
    	$('#perk-name, #perk-desc, #categories').addClass('invalid');
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

var goCards = function() {
	activatePage('editcards');
}

var goCreateCard = function() {
	activatePage('card');
}

var goSignUp = function() {
	activatePage('signup');
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
	if (loggedIn) {
		var perkName = $($(element)[0].parentElement.parentElement).find('.perk-name span.the-name').text();

		$.ajax({
	        method: 'POST',
	        url: '/perks/vote',
	        contentType: 'application/json',
	        data: '{"name": "' + perkName + '", "vote": "' + vote + '"}'
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
	else {
		prompt();
	}
}

/* ---------- CATEGORIES ---------- */

var categories = {
	'Food': {
		id: 332,
		icon: 'restaurant'
	},
	'Clothing': {
		id: 333,
		icon: 'accessibility'
	},
	'Groceries': {
		id: 334,
		icon: 'local_grocery_store'
	},
	'Electronics': {
		id: 335,
		icon: 'devices_other'
	},
	'Shoes': {
		id: 336,
		icon: 'airline_seat_legroom_normal'
	}
};

var getCategoryHTML = function(category) {
	return '<div class="input-radio"><input type="radio" name="cat" id="cat-'+category+'" data-i="'+categories[category].id+'"><label for="cat-'+category+'"><div class="radio-icon"><i class="material-icons">'+categories[category].icon+'</i></div><div class="radio-label">'+category+'</div></label></div>';
};

var categoryKeys = Object.keys(categories);

/* ---------- CARDS ---------- */

// all cards

var tryAddUserToCard = function(element) {
	var cardId = $(element)[0].dataset.cardi;

	$.ajax({
        type: 'PATCH',
        url: '/cards/'+cardId+'/addUser',
    })
    .done(function() {
    	stopLoad();
    	cardIds.push(cardId);
        populateUserCards();
        $(element).addClass('selected');
    })
    .fail(function() {
    	stopLoad();
    });
};

var cards = {};

var getSmallCardHTML = function(card) {
	return '<div class="section card-element small-card real-card '+(cardIds.indexOf(card.id) >= 0 ? 'selected' : '')+'" data-name="'+card.name+'" data-cardi="'+card.id+'"><div class="small-card-name">'+card.name+'</div><i class="material-icons">check</i></div>';
};

var getPerkCardOptionHTML = function(card) {
	return '<option value="'+card.id+'">'+card.name+'</option>';
}

var populateAllCards = function(success) {
	$.getJSON('/cards', function(data) {
		$('#all-cards').empty();
		$('#perk-cards').empty();

		for (var i = 0; i < data.length; i++) {
			cards[data[i].id] = data[i];

			$('#all-cards').append(getSmallCardHTML(data[i]));
			$('#perk-cards').append(getPerkCardOptionHTML(data[i]));
		}

		$('.real-card:not(.selected)').on('click', function() {
			tryAddUserToCard(this);
		});
	})
	.always(function() {
		$('#all-cards').append('<div class="section card-element small-card" id="create-card"><div class="small-card-name">Don’t see yours?</div><i class="material-icons">add</i></div>');
		$('#create-card').on('click', function() {
			goCreateCard();
		});
		if (success !== undefined) success();
	});
};

// user cards

var getBigCardHTML = function(card) {
	return '<div class="section card-element pp-card" data-name="'+card.name+'"><div class="pp-card-inner">'+card.name+'</div></div>';
};

var populateUserCards = function() {
	if (cardIds.length > 0) {
		$('#user-cards').empty();

		for (var i = 0; i < cardIds.length; i++) {
			$('#user-cards').append(getBigCardHTML(cards[cardIds[i]]));
		}
	}
};

var tryCreateCard = function(name, description) {
	startLoad();

	$.ajax({
        type: 'POST',
        url: '/cards',
        data: '{"name": "' + name + '", "description": "' + description + '"}',
        contentType: 'application/json',
    })
    .done(function(perk) {
    	stopLoad();
        populateAllCards();
        goCards();
        $('#card-name').val('');
        $('#card-desc').val('');
    })
    .fail(function() {
    	stopLoad();
    	$('#card-name, #card-desc').addClass('invalid');
    });
};

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
	populateAllCards(function() {
		populatePerkList('top');
		populatePerkList('recommended');

        authenticate();
	});

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
		endPrompt();
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

	$('#edit-cards-button').on('click', function() {
		goCards();
	});

	$('#create-card-button').on('click', function() {
		if ($('#card-name').val().length > 0) {
			tryCreateCard($('#card-name').val(), $('#card-desc').val());
		}
		else {
			$('#card-name').addClass('invalid');
		}
	});

	$('#log-out').on('click', function() {
		tryLogOut();
	});

	$('#sign-up').on('click', function() {
		goSignUp();
	});

	$('#signup-button').on('click', function() {
		if (
				$('#signup-fname').val().length > 0 &&
				$('#signup-lname').val().length > 0 &&
				$('#signup-email').val().length > 0 &&
				$('#signup-password').val().length > 0
			) {
			trySignUp(
					$('#signup-fname').val(),
					$('#signup-lname').val(),
					$('#signup-email').val(),
					$('#signup-password').val()
				);
		}
		else {
			if ($('#signup-fname').val().length <= 0) {
				$('#signup-fname').addClass('invalid');
			}
			if ($('#signup-lname').val().length <= 0) {
				$('#signup-lname').addClass('invalid');
			}
			if ($('#signup-email').val().length <= 0) {
				$('#signup-email').addClass('invalid');
			}
			if ($('#signup-password').val().length <= 0) {
				$('#signup-password').addClass('invalid');
			}
		}
	});

	// perk

	setButtonExpandScale();

	$('#menu-perk').on('click', function() {
		if (loggedIn) {
			if (currentPage !== 'perk') goPerk();
		}
		else {
			prompt();
		}
	});

	for (var i = 0; i < categoryKeys.length; i++) {
		$('#categories').append(getCategoryHTML(categoryKeys[i]));
	}

	$('#categories').on('click', function() {
		$(this).removeClass('invalid');
	});

	$('#create-button').on('click', function() {
		if ($('#perk-name').val().length > 0 && $('input[name="cat"]:checked').length > 0) {
			tryCreatePerk($('#perk-name').val(), $('#perk-desc').val(), $('#perk-cards').val(), $('input[name="cat"]:checked')[0].dataset.i);
		}
		else {
			if ($('#perk-name').val().length <= 0) {
				$('#perk-name').addClass('invalid');
			}
			if ($('input[name="cat"]:checked').length <= 0) {
				$('#categories').addClass('invalid');
			}
		}
	});
});