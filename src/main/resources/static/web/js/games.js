$(document).ready(function() {
	
	// Show login or logout depends on user is logged in
	isUserLogged();
	
	// Check if some user has authenticated
	getJsonAndStartFunctions();
	
	// Set Logx Events
  setLoginForm();
	setLogoutForm();
	
});


function isUserLogged() {

	if($('#playerInfo').length) {

		$('#login-form').hide();
		$('#logout-form').show();
		
	}else {

		$('#login-form').show();
		$('#logout-form').hide();
		
	}
	
}


function setLoginForm() {
	
	let loginForm = document.getElementById('login-form');
	let loginButton = document.getElementById('login-button');
	
	loginButton.addEventListener('click',checkLogin);
	
}

function setLogoutForm() {
	
	let logoutForm = document.getElementById('logout-form');
	let logoutButton = document.getElementById('logout-button');
	
	logoutButton.addEventListener('click',logout);
	
}


function checkLogin() {
	
	let user = document.getElementById('username');
	let pwd = document.getElementById('userpwd');

	if (isEmpty(user.value) && isEmpty(pwd.value)) {
		login(user,pwd);
	}
	else {
		alert('Please, fill name and password fields');
	}
}


function login(user, pwd) {
	$.post('/api/login', 
         { name: user.value,
           password: pwd.value })
   .done(function() {
			user.value = '',
			pwd.value = '',
			getJsonAndStartFunctions()
		})
   .fail(function() {alert('Username or password are wrong')});
}


function logout() {
  $.post('/api/logout')
   .done(function() {
			alert('You are logging out of your session'),
			getJsonAndStartFunctions()
		})
   .fail(function() {alert('Some problem happened with logout');});
}

				 
function isEmpty(field) {
	return (field && field.length>0);
}


function getJsonAndStartFunctions() {
	
	urlJson = 'http://localhost:8080/api/games';
  //urlJson = 'gamesdata.json';
	
	$.getJSON(urlJson, function (data) {
		
		// Print games info and player info if exists
		printInfo(data);
		
		// Show login or logout depends on user is logged in
		isUserLogged();
		
	});
	
}

function printInfo(data) {
	
	// Get the content element and clean it
	let content = document.getElementById('content');
	content.innerHTML = '';
	
	// Check if there are some logged player to print
	if(data.player) {
		
		// Create a div to put player info
		let playerInfo = document.createElement('div');
		playerInfo.id = 'playerInfo';
		let player = createPlayerInfo(data.player);
		content.appendChild(playerInfo).appendChild(player);
		
	}
	
	// Create a div to put games info
	let gamesInfo = document.createElement('div');
	gamesInfo.id = 'gamesInfo';
	let gameList = createGameList(data.game);
	content.appendChild(gamesInfo).appendChild(gameList);
	
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