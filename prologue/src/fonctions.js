if(window.addEventListener){
    window.addEventListener('load', initBoard("http://localhost:8000/"), false);
}else{
    window.attachEvent('onload', initBoard("http://localhost:8000/"));
}

function httpGet(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, true); // false for synchronous request
	xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4) callback(xmlHttp.responseText);
    }
    xmlHttp.send( null );
}

function httpGetJson(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, true); // false for synchronous request
	xmlHttp.responseType = 'json';
	xmlHttp.onload = function() {
      var status = xmlHttp.status;
      if (status == 200) {
        callback(xmlHttp.response);
      } else {
		console.log("Problem");
      }
    };
    xmlHttp.send( null );
}

function initBoard() 
{
	httpGetJson("http://localhost:8000/", function(response){
		getGraphics();
	});
}
function getGraphics()
{	
	httpGetJson("http://localhost:8000/dashboard", function(response){
		buildDashboard(response);
	});
	
	httpGet("http://localhost:8000/ground", function(response){
		document.getElementById("partGround").innerHTML = response;
	});
}

function playTurn()
{
	httpGetJson("http://localhost:8000/turn", function(response){
		var result;
		if(response.result == "true") {
			getGraphics();
		} else {
			result = "<div  class='panel panel-primary'><div class='panel-heading'><h3 class='panel-title'>Résultat</h3>";
			if(response.result == "draw"){
				result +="</div><div class='panel-body'>Egalité</div></div>";
			} else {
				result +="</div><div class='panel-body'>Vainqueur : " + response.result + "</div></div>";
			}
			document.getElementById("result").innerHTML = result;
			eraseInterval();
		}
	});
}

function buildDashboard(response) 
{
	var dashboard="";
	for(var i=0; i<response.dashboard.length;i++)
	{
		dashboard +="<tr>"; 
		switch(response.dashboard[i].joueur) {
			case 1:
				dashboard +="<td><span class='glyphicon glyphicon-user' style='color: blue;'></td>"
				break;
			case 2:
				dashboard +="<td><span class='glyphicon glyphicon-user' style='color: red;'></td>"
				break;
			default:
				dashboard +="<td><span class='glyphicon glyphicon-user' style='color: black;'></td>";
		}
		switch(response.dashboard[i].orientation) {
			case "nord":
				dashboard +="<td><span class='glyphicon glyphicon-arrow-up' style='color: darkgreen;'></td>"; 
				break;
			case "sud":
				dashboard +="<td><span class='glyphicon glyphicon-arrow-down' style='color: darkgreen;'></td>"; 
				break;
			case "est":
				dashboard +="<td><span class='glyphicon glyphicon-arrow-right' style='color: darkgreen;'></td>"; 
				break;
			case "ouest":
				dashboard +="<td><span class='glyphicon glyphicon-arrow-left' style='color: darkgreen;'></td>"; 
				break;
			default:
				;
		}
		dashboard +="<td>"+response.dashboard[i].vie+"</td>"; 
		dashboard +="<td>"+response.dashboard[i].degats+"</td>"; 
		dashboard +="<td>"+response.dashboard[i].defense+"</td>"; 
		dashboard +="</tr>"; 
	}
	var firstLine = document.getElementById("dashboard").rows[0].innerHTML;
	document.getElementById("dashboard").innerHTML = firstLine + dashboard;
}

var interval;
function playGame(time)
{
	if(interval == null) {
	interval = window.setInterval(function() {playTurn();}, time);
	}
}

function eraseInterval()
{
	window.clearInterval(interval);
	interval = null;
}