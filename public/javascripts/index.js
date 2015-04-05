function doGood ( carid ) {
    $.get("http://@request().host()/eval/" + carid + "/1", function(data){
        alert("ok");
    });
}

function doBad ( carid ) {
    $.get("http://@request().host()/eval/" + carid + "/2", function(data){
        alert("ok");
    });
}