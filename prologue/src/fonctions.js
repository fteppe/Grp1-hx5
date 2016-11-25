if(window.addEventListener){
    window.addEventListener('load', replace("http://localhost:8000/"), false);
}else{
    window.attachEvent('onload', replace("http://localhost:8000/"));
}

function httpGet(theUrl)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function replace(theUrl)
{
	var response = httpGet(theUrl);
	document.getElementById("yolo").innerHTML = response;
}