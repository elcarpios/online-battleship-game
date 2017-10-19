$(document).ready(function() {
	
  setLoginForm();
	
});


function setLoginForm() {
	
	let loginForm = document.getElementById('login-form');
	let loginButton = document.getElementById('login-button');
	
	loginButton.addEventListener('click',checkLogin);
	
}


function checkLogin() {
	
	let user = document.getElementById('username').value;
	let pwd = document.getElementById('userpwd').value;

	if (isEmpty(user) && isEmpty(pwd)) {
		login(user,pwd);
	}
	else {
		alert('Please, fill name and password fields');
	}
}


function login(user, pwd) {
	$.post('/api/login', 
         { name: user,
           password: pwd })
   .done(function() {console.log("done"),getJsonAndStartFunctions()})
   .fail(function() {alert('Username or password are wrong')});
}


function isEmpty(field) {
	return (field && field.length>0);
}


function getJsonAndStartFunctions() {
	
	urlJson = 'http://localhost:8080/api/games';
  //urlJson = 'gamesdata.json';
	
	$.getJSON(urlJson, function (data) {
		startFunctions(data);
	});
	
}

function startFunctions(data) {
	
	let playerInfo = createPlayerInfo(data.player);
	document.body.appendChild(playerInfo);
	
	let gameList = createGameList(data.game);
	document.body.appendChild(gameList);
	
}

function createPlayerInfo(data) {
	
	let p = document.createElement('p');
	p.innerHTML = 'ID: ' + data.id + ' / User: ' + data.name;
	return p;
	
}


function createGameList(data) {
	
	// Create the ordered list
  let ol = document.createElement('ol');
	ol.innerHTML = 'Games:';
	
	// Go throw all the JSON Data
  for(let i=0; i<data.length; i++) {
		
		let game = data[i];
		
		// Create the li element
		let li = document.createElement('li');
		
		// Format Data
		let fullDate = new Date(game.created);
		let date = formatDate(fullDate);
		let time = formatTime(fullDate);

		// Add to li
		li.innerHTML = date.month + '/' +
									 date.day + '/' +
									 date.year + ' ';
		
		li.innerHTML += time.hour + ':' +
										time.minute + ':' +
										time.seconds + ' ';
		
		// Ready to go to gamePlayers
		let gamePlayers = game.gamePlayers;
		
		for(let j=0; j<gamePlayers.length; j++) {
			
			let gamePlayer = gamePlayers[j];
		  li.innerHTML += gamePlayer.player.email + ', ';
			
		}
		// Append li to the list
		ol.appendChild(li);
  }
	
	return ol;
}



function formatDate(fullDate) {
	let date = {
	day: fullDate.getDate(),
	month: fullDate.getMonth(),
	year: fullDate.getFullYear()
	}
		
	return date;
}



function formatTime(fullDate) {
	let time = {
		hour: fullDate.getHours(),
		minute: fullDate.getMinutes(),
		seconds: fullDate.getSeconds()
	}
	return time;
}