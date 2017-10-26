// Global variables
	var salvo = {
		turn: 1,
		locations: []
	};

$(document).ready(function() {
	
	let urlParameters = paramObj(window.location.href);
	//let urlJson = 'gamedummy.json';
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

	// Construct players panel
	infoPlayerConstructor(data.gamePlayers);
	
	
	// Own
		let ownPrefix = 'own';
		let ownTurnsSalvoes = (data.salvoes != null) ? data.salvoes[1] : 0;
	
		let ownTitle = document.createElement('h1');
		ownTitle.className = 'title-board';
		ownTitle.innerHTML = 'Your Board';
		document.getElementById('own-board').appendChild(ownTitle);

		let ownGrid = gridConstructor('ownGrid',ownPrefix);
		document.getElementById('own-board').appendChild(ownGrid);
		
		// Print or positioning ships
		if(data.ships != null) {
			var usedShips = shipsUsed(data.ships);
			putShipsOnGrid(ownPrefix, data.ships);
			// Check if all ships are placed to be ready to atack			
		}
		else {
			var usedShips = [''];
		}
			let ownShips = shipsContainerConstructor('own', usedShips);
			document.getElementById('own-board').appendChild(ownShips);
			
			
		


		// Exists salvoes to print?
		if(ownTurnsSalvoes != null && ownTurnsSalvoes.length > 0) 
			putSalvoesOnGrid(ownPrefix, ownTurnsSalvoes);
	
	
	// Opposite	
		let opositePrefix = 'op';
		let opositeTurnsSalvoes = (data.salvoes != null) ? data.salvoes[0] : 0;
	
		let opTitle = document.createElement('h1');
		opTitle.className = 'title-board';
		opTitle.innerHTML = 'Enemy Board';
		document.getElementById('op-board').appendChild(opTitle);

		let oppositeGrid = gridConstructor('oppositeGrid',opositePrefix);
		document.getElementById('op-board').appendChild(oppositeGrid);
	
		let opShips = shipsContainerConstructor('op');
		document.getElementById('op-board').appendChild(opShips);
	
		if(data.ships.length == 3) {
				setOpositeGrid();
			}
		
		// Exists salvoes to print?
		if(opositeTurnsSalvoes.length > 0) 
				putSalvoesOnGrid(opositePrefix, opositeTurnsSalvoes);
	

}


function shipsUsed(ships) {

	let shipsName = [];
	
	for(let ship of ships) {
		
		shipsName.push(ship.type);
		
	}
	
	return shipsName;
	
}


// Create the legend to contain the ships for each board
// Check which ships are needed to positioning
function shipsContainerConstructor(who, usedShips) {
	
	let container = document.createElement('div');
	
	let shipsList = ['Destroyer','Submarine','PatrolBoat'];
	
	// Add the ships into the box
	return addShipsToLegend(who,shipsList,usedShips);
	
}


function addShipsToLegend(who,listShips,usedShips) {
	
	let ships = document.createElement('div');
	ships.className = 'legend-ships';
	
	for(let i=0; i<listShips.length; i++) {
		
		let ship = document.createElement('div');

		// Check is the ship has to be put
		if(who == 'own' && usedShips && usedShips.indexOf(listShips[i]) == -1) {
			ship.setAttribute('draggable','true');
			ship.setAttribute('ondragstart','drag(event)');
			ship.className += ' bold';
		}
	
		
		ship.id = (who == 'own') ? listShips[i] : who + listShips[i];
		ship.className += ' legend-ship'
		ship.innerHTML = listShips[i];
		ships.appendChild(ship);
		
	}
	
	return ships;
	
}


function getGP() {
	let urlParameters = paramObj(window.location.href);
	return urlParameters.gp;
}



function infoPlayerConstructor(players) {
	var id = getGP();
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

	
	document.getElementById('players-info').appendChild(infoPlayer);
}

function gridConstructor(idDiv,idPrefix) {
	
	// Create the grid
	let grid = document.createElement('table');
	grid.id = idDiv;
	grid.className = 'grid';
	
	
	// Initialize all the indicators (references in grid)
	var header = ["","A","B","C","D","E","F","G","H","I","J"];
	
	
	// Add 1 to row and col to include headers
	for(let i=0; i<=10; i++) {
		
		var row = document.createElement('tr');
		
		for(let j=0; j<=10; j++) {
			
			// Create standard cell
			let cell = document.createElement('td');
			cell.className = 'grid-item';

			// Column header
			if( i == 0 ) {
				cell.className += ' col-header';
				cell.innerHTML = header[j];
			} 
			// Row header
			else if( (j == 0) && (i > 0) ) {
				cell.className += ' row-header';
				cell.innerHTML = i;
			}
			// Normal cells
			else {
				cell.id = idPrefix + header[j] + i;
				cell.className += ' cell';
				if(idPrefix == 'own') {
					cell.setAttribute('ondrop','drop(event)');
					cell.setAttribute('ondragover','allowDrop(event)');
				}
			}
			
			row.appendChild(cell);
			
		}
		
		grid.appendChild(row);

	}
	
	return grid;
}


function putShipsOnGrid(prefix,ships) {
	
	for(let i=0; i<ships.length; i++) {
		
		let ship = ships[i];

		switch (ship.type) {
			
		case 'Destroyer': 	implementClasses(prefix,' destroyer-color',ship.locations);	
												break;
				
		case 'Submarine':		implementClasses(prefix,' submarine-color',ship.locations);	
												break;
		case 'PatrolBoat': implementClasses(prefix,' patrol-boat-color',ship.locations);
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

function putSalvoesOnGrid(prefix, turns) {
	
	// Go throw all the turns
	for (var i=0; i<turns.length; i++) {
		
		let salvoes = turns[i];
		
		for (let j=0; j<salvoes.length; j++) {
			
			let salvo = salvoes[j];
			
			let salvoCell = document.getElementById(prefix + salvo);
			let classesCell = salvoCell.classList;
			
			if((classesCell.value.indexOf('destroyer-color') != -1) ||
				(classesCell.value.indexOf('submarine-color') != -1) ||
				(classesCell.value.indexOf('patrol-boat-color') != -1)) {
				
				salvoCell.className += ' bombed';
				
			}
			else {
				
				salvoCell.className += ' water';
				
			}
			
			salvoCell.innerHTML = i + 1;
			
		}
	}
}



// DRAG CODE

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
	
    ev.preventDefault();
	
    var type = ev.dataTransfer.getData("text");
		
		// Drag only ships
		if(!isShip(type)) {
			return;
		}
	
		// Get the prefix of the row
		let prefix = ev.target.id.slice(0,3);
		// Get the letter of the row
		let letter = ev.target.id.slice(3,4);
		// Get the number of the col
		let position = parseInt(ev.target.id.slice(4));
	
	
		// Set the ship size
		let length = (type == 'PatrolBoat') ? 2 : 3;
	
		// Check if fits in the position
		if(isFit(position,length)) {
			
			let locations = createLocations(letter,position,length);
			
			if(locationsAreFree(prefix, locations)) {
				let ship = createShipObject(type,locations);
				
				console.log(type);
				postShip(prefix,ship);

				// Delete drag attributes in the legend
				let legend = document.getElementById(type);
				legend.removeAttribute('draggable');
				legend.removeAttribute('ondragstart');
				legend.classList.remove('bold');

			
				
			}
			else {
				return false;
			}

		}	
			
		 else {
			alert('No real position');
		}
}


function locationsAreFree(prefix,locations) {
	
	for(let location of locations) {
		
		let cellName = prefix + location;
		let cell = document.getElementById(cellName);
		
		let classes = cell.className.split(' ');

		if((classes.indexOf('destroyer-color') != -1) ||
			 (classes.indexOf('submarine-color') != -1) ||
			 (classes.indexOf('patrol-boat-color') != -1)) {
			alert('This position is not available');
			return false;
		}
	}
	
	return true;
	
}


function isShip(type) {
	return type == 'Destroyer' || type == 'Submarine' || type == 'PatrolBoat';
}


// Create locations array
function createLocations(letter,position,length) {
	
	let locations = [];
	
	for(let i=position; i<position+length; i++ ) {
		
		locations.push(letter + i);
		
	}
	
	return locations;
}


function createShipObject(data,locations) {
	return [{
				type: data,
				locations: locations
			}];
}


// Check if the ship fits into the grid
function isFit(pos,length) {
	return pos + length <= 11;
}


function postShip(prefix,ship) {
	var id = getGP();
	$.post({
  url: '/api/games/players/' + id + '/ships', 
  data: JSON.stringify(ship),
  dataType: "text",
  contentType: "application/json"
	})
	.done(function (response) {
		putShipsOnGrid(prefix,ship);

		if(response == 'Start-Game'){
			setOpositeGrid();
		}
	})
	.fail(function (response) {
		let legend = document.getElementById(ship.type);
		legend.setAttribute('draggable','true');
		legend.setAttribute('ondragstart','drag(event)');
		legend.className += ' bold';
	});
	
	
}

// Set listeners for enemy grid
function setOpositeGrid() {
	console.log(salvo);
	$('#oppositeGrid td.cell').on('click', function() {
		console.log(salvo);
		if((salvo.locations.length <= 3) && isPossibleSalvo(this.id)) {
			salvo.locations.push(this.id);
			this.className += ' bombed';
			console.log('bombed');
			if(salvo.locations.length == 3) {
				fireSalvo(salvo);
				salvo.turn++;
				salvo.locations = [];
				console.log('lets fire');
			}
		}
	
	});
	
}


function fireSalvo(salvo) {
	var id = getGP();
	for(let i=0; i<salvo.locations.length; i++) {
		salvo.locations[i] = salvo.locations[i].slice(2);
		console.log(salvo.locations[i]);
	}
	console.log(salvo);
	$.post({
  url: '/api/games/players/' + id + '/salvoes', 
  data: JSON.stringify(salvo),
  dataType: "text",
  contentType: "application/json"
	})
	.done(function (response) {
		console.log('done');
	})
	.fail(function (response) {

	});
}


function isPossibleSalvo(idCell) {
	
	// Check if that cell has some before salvo
	if($(this.id).hasClass('bombed')) {
		alert('This locations has already been bombed')
	}
	else {
		return true;
	}
}
