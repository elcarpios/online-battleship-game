$(document).ready(function() {
	
	let urlParameters = paramObj(window.location.href);
	//let urlJson = 'data.json';
	let urlJson =  ' /api/game_view/' + urlParameters.gp;
	getJsonAndStartFunctions(urlJson);	
});

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
	infoPlayerConstructor(data.gamePlayers);
	gridConstructor(10,10);
	setShipsOnGrid(data.ships);
}

function infoPlayerConstructor(players) {
	let urlParameters = paramObj(window.location.href);
	var id = urlParameters.gp;
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

function gridConstructor(col,row) {
	// Create the grid
	let grid = document.createElement('div');
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
			gridItem.id = letterIndicators[colIdIndicator] + rowIdIndicator.toString();
			
			// Calculate the new indicator
			colIdIndicator += 1;
			if(colIdIndicator % 11 == 0) {
				colIdIndicator = 1;
				rowIdIndicator++;
			}
		}
		
		grid.appendChild(gridItem);
	}
	document.body.appendChild(grid);
}


function setShipsOnGrid(ships) {

	for(let i=0; i<ships.length; i++) {
		let ship = ships[i];
		switch (ship.Type) {
			
		case 'Destroyer': 	implementClasses(' destroyer',ship.Locations);	
												break;
				
		case 'Submarine':		implementClasses(' submarine',ship.Locations);	
												break;
		case 'Patrol Boat': implementClasses(' patrol-boat',ship.Locations);
												break;
		default: 						alert('Type ship doesnt match');
												break;
		}
	}
}

function implementClasses(classes, array) {
	for(let j=0; j<array.length; j++) {
	let gridItem = document.getElementById(array[j]);
	gridItem.className += ' ' + classes;
	} 
}

