$(document).ready(function() {
	
	// Show login or logout depends on user is logged in
	isUserLogged();
	
	// Check if some user has authenticated
	getJsonAndStartFunctions();
	
	// Set Logx Events
  setLoginForm();
	setLogoutForm();
	setCreateGame();
	
});


function isUserLogged() {

	if($('#playerInfo').length) {

		$('.login').hide();
		$('.logout').show();
		
	}else {

		$('.login').show();
		$('.logout').hide();
		
	}
	
}


function setLoginForm() {
	
	let user = document.getElementById('username');
	let pwd = document.getElementById('userpwd');
	
	// Login
	let loginButton = document.getElementById('login-button');
	loginButton.addEventListener('click', function() {
		login(user,pwd)
	});
	
	// Signup
	let signupButton = document.getElementById('signup-button');
	signupButton.addEventListener('click', function() {
		signup(user,pwd)
	});
	
}

function setLogoutForm() {
	
	let logoutButton = document.getElementById('logout-button');
	
	logoutButton.addEventListener('click',logout);
	
}



function setCreateGame() {
	
	let createButton = document.getElementById('create-game');
	
	createButton.addEventListener('click',createGame);
	
}


function createGame() {
	
	$.post('/api/games')
		 .done(function(response) {goToGP(response.idGP)})
		 .fail(function(response) {alert('Game has not created')});
	
}



function checkFields(arrayFields) {
	
	let checker = true;
	let empty = '';
	
	for(field of arrayFields) {

		if (!(isEmpty(field.value) && isEmpty(field.value))) {
			
			empty += field.name + '\n';
			checker = false;
			
		}
		
	}
	
	if(checker) {
		return true;
	} else {
		alert('Please fill the next fields: \n' + empty);
	}
	
}
	

function signup(user, pwd) {
	
	if(checkFields([user,pwd])) {
		$.post('/api/players',
					 { name: user.value, 
						 password: pwd.value })
		.done(function() {login(user,pwd)})
		.fail(function() {alert('Username is already in use')});
	}
	
}


function login(user, pwd) {
	if(checkFields([user,pwd])) {
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
	
	// Create an empty id player, if exists will get the value
	let idPlayer = '';
	
	// Check if there are some logged player to print
	if(data.player) {
		
		// Create a div to put player info
		let playerInfo = document.createElement('div');
		playerInfo.id = 'playerInfo';
		let player = createPlayerInfo(data.player);
		content.appendChild(playerInfo).appendChild(player);
		
		// Get the ID to compare in games
		idPlayer = data.player.id;
		
	}
	
	// Create a div to put games info
	let gamesInfo = document.createElement('div');
	gamesInfo.id = 'gamesInfo';
	let gameList = createGameList(data.game,idPlayer);
	content.appendChild(gamesInfo).appendChild(gameList);
	
}

function createPlayerInfo(data) {

	let p = document.createElement('p');
	p.innerHTML = 'ID: ' + data.id + ' / User: ' + data.name;
	return p;

}


function createGameList(games, idPlayer) {
	
	console.log(games);
	
	// Create the ordered list
  let ol = document.createElement('ol');
	ol.innerHTML = 'Games:';
	
	// Go throw all the JSON Data
  for(let i=0; i<games.length; i++) {
		
		let game = games[i];
		
		// Boleean to check if the current user is a player in that game
		let isPlaying = false;
		
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
			
			// Test if the user is playing this game
			if(idPlayer === gamePlayer.player.id) {
				isPlaying = true;
				var gpNum = gamePlayer.id;
				
			}
			
		}
		
		// Create the buttons to join if the player is playing
		if(isPlaying) {
		
			let join = document.createElement('button');
			join.id = gpNum;
			join.innerHTML = 'Join';
			join.addEventListener('click',function() {
				goToGP(this.id);
			});
			li.appendChild(join);
			
		}
		
		// Append li to the list
		ol.appendChild(li);
  }
	
	return ol;
}


function goToGP(id) {
	
	window.location.href = '/web/game.html?gp=' + id;
	
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