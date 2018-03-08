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

            perkRow += '<td class = "upcell"><input type = "button" value = "Upvote" class = "upvote"/></td>';
            perkRow += '<td class = "downcell"><input type = "button" value = "Downvote" class = "downvote"/></td>';
            perkRow += '</tr>';

            $(perkRow).appendTo($("#perkTable"));

            $('.perk').on('click', '.upvote, .downvote', function(event) {
                event.preventDefault();

                modifyScore($(event.target).closest('tr'), $(event.target));
            });
        });
    });

    $(".upvote, .downvote").click(function(event) {
        event.preventDefault();

        modifyScore($(event.target).parent(), $(event.target));
    });

    function modifyScore(perk, button) {
        var score = parseInt($(perk).find('td.score')[0].innerHTML);
        let eventSource = $($(perk).context);

        if (eventSource.hasClass('undo')) {
            if (eventSource.hasClass('upvote')) {
                score--;
                eventSource.prop("value", "Upvote");
            } else {
                score++;
                eventSource.prop("value", "Downvote");
            }

            $(button).removeClass('undo');
        } else {
            if (eventSource.hasClass('upvote')) {
                let downvoteButton = $(perk).find('td.downcell').find('input');
                score++;

                if (downvoteButton.hasClass('undo')) {
                    score++;
                    downvoteButton.removeClass('undo');
                    downvoteButton.prop("value", "Downvote");
                }
            } else {
                let upvoteButton = $(perk).find('td.upcell').find('input');
                score--;

                if (upvoteButton.hasClass('undo')) {
                    score--;
                    upvoteButton.removeClass('undo');
                    upvoteButton.prop("value", "Upvote");
                }
            }

            $(button).prop("value", "Undo");
            $(button).addClass('undo');
        }

        $(perk).find('td.score')[0].innerHTML = score;
    }
});