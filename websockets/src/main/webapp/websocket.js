var ws;

function connect() {
    var username = document.getElementById("username").value;

    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);

    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        console.log(event.data);
        var message = event.data;
        log.innerHTML += message + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    ws.send(content);
}