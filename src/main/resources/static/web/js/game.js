$(document).ready(function() {
	
	let urlParameters = paramObj(window.location.href);
	//let urlJson = 'gamedata.json';
	let urlJson =  ' /api/game_view/' + urlParameters.gp;
	
	getJsonAndStartFunctions(urlJson);	
	
	setLogoutForm();
	
});


function setLogoutForm() {
	
	let logoutButton = document.getElementById('logout-button');
	
	logoutButton.addEventListener('click',logout);
	
}

function logout() {
  $.post('/api/logout')
   .done(function() {
			alert('You are being redirecting into Games web page'),
			window.location.href = '/web/games.html'
		})
   .fail(function() {alert('Some problem happened with logout');});
}


function paramObj(search) {
  var obj = {};
  var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

  search.replace(reg, function(match, param, val) {
    obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
  });

  return obj;
}

function getJsonAndStartFunctions(url) {
	$.getJSON(url, function (data) {
		startFunctions(data);
	});
}

function startFunctions(data) {
	
	// Define the prefixes for duplicate values, like coordinades in grid
	let ownPrefix = 'own-';
	let ownTurnsSalvoes = data.salvoes[1];
	
	let opositePrefix = 'op-';
	let opositeTurnsSalvoes = data.salvoes[0];

	infoPlayerConstructor(data.gamePlayers);
	console.log(data);
	
	var ownGrid = gridConstructor('ownGrid',10,10,ownPrefix);
	document.body.appendChild(ownGrid);
	setShipsOnGrid(ownPrefix, data.ships);
	setSalvoesOnGrid(ownPrefix, ownTurnsSalvoes);
	
	var oppositeGrid = gridConstructor('oppositeGrid',10,10,opositePrefix);
	document.body.appendChild(oppositeGrid);
	setSalvoesOnGrid(opositePrefix, opositeTurnsSalvoes);
}

function infoPlayerConstructor(players) {
	let urlParameters = paramObj(window.location.href);
	var id = urlParameters.gp;
	//var id = 1;
	let infoPlayer = document.createElement('div');
	
	for(let i=0; i<players.length; i++) {
		let player = document.createElement('div');
		player.innerHTML = players[i].player.email;
		if(id == players[i].id) {
			player.className += ' bold';
		}
		infoPlayer.appendChild(player);
	}

	
	document.body.appendChild(infoPlayer);
}

function gridConstructor(idDiv,col,row,cellIdPrefix) {
	// Create the grid
	let grid = document.createElement('div');
	grid.id = idDiv;
	grid.className = 'grid';
	
	
	// Initialize all the indicators (references in grid)
	var letterIndicators = ["","A","B","C","D","E","F","G","H","I","J"];
	var numberIndicators;
	var colIdIndicator = 1;
	var rowIdIndicator = 1;
	
	// Add 1 to row and col to include headers
	for(let i=0; i<((col+1)*(row+1)); i++) {

		let gridItem = document.createElement('div');
		gridItem.className = 'grid-item';
		
		// Check if we are doing the first row to put the indicators
		if( i <= col ) {
			gridItem.className += ' grid-header';
			gridItem.innerHTML = letterIndicators[i];
		}
		
		// Check if we are doing the first column to put the indicators
		if ( i!=0 && i % 11 == 0) {
			gridItem.className += ' grid-header';
			numberIndicators = i.toString(); 
			gridItem.innerHTML = numberIndicators.slice(1);
		}
		
		// If is not a header we add an ID
		if(!gridItem.classList.contains('grid-header')) {
			gridItem.id = cellIdPrefix + letterIndicators[colIdIndicator] + rowIdIndicator.toString();
			
			// Calculate the new indicator
			colIdIndicator += 1;
			if(colIdIndicator % 11 == 0) {
				colIdIndicator = 1;
				rowIdIndicator++;
			}
		}
		
		grid.appendChild(gridItem);
	}
	
	return grid;
}


function setShipsOnGrid(prefix,ships) {

	for(let i=0; i<ships.length; i++) {
		let ship = ships[i];
		switch (ship.Type) {
			
		case 'Destroyer': 	implementClasses(prefix,' destroyer',ship.Locations);	
												break;
				
		case 'Submarine':		implementClasses(prefix,' submarine',ship.Locations);	
												break;
		case 'Patrol Boat': implementClasses(prefix,' patrol-boat',ship.Locations);
												break;
		default: 						alert('Type ship doesnt match');
												break;
		}
	}
}

function implementClasses(prefix,classes, array) {
	for(let j=0; j<array.length; j++) {
	let gridItem = document.getElementById(prefix + array[j]);
	gridItem.className += ' ' + classes;
	} 
}

function setSalvoesOnGrid(prefix, turns) {
	// Go throw all the turns
	for (var i=0; i<turns.length; i++) {
		let salvoes = turns[i];
		for (let j=0; j<salvoes.length; j++) {
			let salvo = salvoes[j];
			let salvoCell = document.getElementById(prefix + salvo);
			let classesCell = salvoCell.classList;
			if((classesCell.value.indexOf('destroyer') != -1) ||
				(classesCell.value.indexOf('submarine') != -1) ||
				(classesCell.value.indexOf('patrol-boat') != -1)) {
				salvoCell.className += ' bombed';
			}
			else {
				salvoCell.className += ' water';
			}
			salvoCell.innerHTML = i + 1;
		}
	}
}
