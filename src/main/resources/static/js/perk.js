$(document).ready(function() {
    $("#create").click(function(event) {
        event.preventDefault();

        $.ajax({
            method: "POST",
            url: "/perks",
            contentType: "application/json",
            data: '{"name": "' + $("#name").val() + '", "description": "' + $("#description").val() + '"}'
        }).done(function(perk) {
            var perkRow = '<tr class = "perk"><td>' + perk.name + '</td>' + '<td>' + perk.description + '</td>' + '<td>Score:</td><td class = "score">' + perk.score + '</td>';

            perkRow += '<td><input type = "button" value = "Upvote" class = "upvote"/></td>';
            perkRow += '<td><input type = "button" value = "Downvote" class = "downvote"/></td>';
            perkRow += '</tr>';

            $(perkRow).appendTo($("#perkTable"));

            $('.perk').on('click', '.upvote, .downvote', function(event) {
                event.preventDefault();

                $(this).prop("disabled", true);
                modifyScore($(event.target).closest('tr'), $(this));
            });
        });
    });

    $(".upvote, .downvote").click(function(event) {
        event.preventDefault();

        $(this).prop("disabled", true);
        modifyScore($(event.target).parent());
    });

    function modifyScore(perk, button) {
        var score = parseInt($(perk).find('td.score')[0].innerHTML);
        let eventSource = $($(perk).context);

        console.log(button.innerHTML);

        if (eventSource.hasClass('upvote')) {
            score++;
        } else {
            score--;
        }

        $(perk).find('td.score')[0].innerHTML = score;
    }
});