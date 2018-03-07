$(document).ready(function() {
    $("#create").click(function(event) {
        event.preventDefault();

        $.ajax({
            method: "POST",
            url: "/perks",
            contentType: "application/json",
            data: '{"name": "' + $("#name").val() + '", "description": "' + $("#description").val() + '"}'
        }).done(function(msg) {
            $('#perkTable').append('<tbody/>');
            $('#perkTable > tbody:last-child').append('<tr><td>' + msg.name + '</td><td>' + msg.description + '</td></tr>');
        });
    });
});