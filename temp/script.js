var populatePerkList = function() {
	$.getJSON('http://perkpack.herokuapp.com/perks', function(data) {
		$('#top-perks').empty();
		var perks = data._embedded.perks;
		for (var i = 0; i < perks.length; i++) {
			$('#top-perks').append(
				'<div class="section perk">'+perks[i].name+'</div>'
			);
		}
	});
};

$(document).ready(function() {
	populatePerkList();
});