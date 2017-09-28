$(document).ready(function() {
	let urlJson =  '/api/leaderboard';
	//let urlJson = 'leaderdata.json';
	getJsonAndStartFunctions(urlJson);	
});

function getJsonAndStartFunctions(url) {
	$.getJSON(url, function (data) {
		console.log(data);
		startFunctions(data);
	});
}

function startFunctions(data) {
	createLeaderBoard(data);
}

function createLeaderBoard(data) {
	let table = document.createElement('table');
	
	let headers = ['Player', 'Win(s)', 'Draw(s)', 'Lost(s)', 'Point(s)'];
	createRow(table,'th',headers);
	
	for(let i=0; i<data.length; i++) {
		
		let player = data[i];
		
		let info = [player.user,
								player.win,
								player.draw,
								player.lost,
								player.total];
		
		createRow(table,'td',info);
		
	}
	
	document.body.append(table);
}

function createRow(table,type,data) {
	let row = document.createElement('tr');
	for (let i=0; i<data.length; i++) {
		let cell = document.createElement(type);
		cell.innerHTML = data[i];
		row.append(cell);
	}
	table.append(row);
}