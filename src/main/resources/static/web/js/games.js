$(document).ready(function() {
  urlJson = 'http://localhost:8080/api/games';
  //urlJson = 'gamesdata.json';
  getJsonAndStartFunctions(urlJson);
});

function getJsonAndStartFunctions(url) {
	$.getJSON(url, function (data) {
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