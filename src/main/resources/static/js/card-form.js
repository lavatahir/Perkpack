$(document).ready(function() {
    $("#submit-card").click(function(event) {
        event.preventDefault();

        $.ajax({
            method: "POST",
            url: "/card",
            contentType: "application/json",
            data: '{"name": "' + $("#name").val() + '", "description": "' + $("#description").val() + '"}'
        }).done(function(msg) {
        });
    });
});