function loadProductsByNameContaining(name) {
	var url = "/ingredientePreparacao/filtraInsumo?insumo=" + name;
	console.log("URL:", url);
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		
		if (this.readyState == 4 && this.status == 200) {
	    	var response = xhttp.responseText;
	        var toJson = JSON.parse(response);
	        var html = "<tr><th>Id</th><th>Nome</th><th>Unidade</th><th></th></tr>"
	        for(i = 0; i < toJson.length; i++) {
	        	html += "<tr>" +
	        			"<td>" + toJson[i].id + "</td>" +
	        			"<td>" + toJson[i].name + "</td>" +
	        			"<td>" + toJson[i].unity + "</td>" +
	        			"<td><button " +
	        			"class=\"btn btn-primary\" " +
	        			"onclick=\"fillSelectedProduct(" + toJson[i].id + ", '" + toJson[i].name + "')\">Selecionar</button></td>" +
	        			"</tr>"
	        }
	        
	        var table = document.getElementById('tableProducts');
	        table.innerHTML = html;
	        
	    }
	}
	
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
}


function fillSelectedProduct(id, name) {
	document.getElementById('prodId').value = id;
	document.getElementById('insumoSel').value = name;
}


function saveIngredientInPreparation(prepId, prodId, qt, correctionalFactor) {
	var url = "/ingredientePreparacao/add?insumoId=" + prodId + "&preparacaoId=" + prepId + "&qt=" + qt + "&fatorCorrecao=" + correctionalFactor;
	
	console.log("URL:", url);
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		
	    if (this.readyState == 4 && this.status == 200) {
	    	var response = xhttp.responseText;
	        var toJson = JSON.parse(response);
	        console.log("Preparacao: ", toJson);
	        location.reload();
	        
	    }
	}
	
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
		
}