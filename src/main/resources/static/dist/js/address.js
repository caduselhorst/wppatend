function loadAddresses() {

	var states = document.getElementById('states');
	var cities = document.getElementById('cities');	
	
	var url='/endereco/' + 
		states.options[states.selectedIndex].text.split('-')[0] + '/' + 
		cities.options[cities.selectedIndex].text + '/' +
		document.getElementById('street').value;
		
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
	    	var response = xhttp.responseText;
	        var toJson = JSON.parse(response);
	        var html = "<tr><th>CEP</th><th>Logradouro</th><th>Complemento</th><th>Bairro</th><th>Localidade</th><th>UF</th><th></th></tr>";
	        for(i = 0; i < toJson.length; i++) {
	        	html += "<tr>" +
	        			"<td>" + toJson[i].cep + "</td>" +
	        			"<td>" + toJson[i].logradouro + "</td>" +
	        			"<td>" + toJson[i].complemento + "</td>" +
	        			"<td>" + toJson[i].bairro + "</td>" +
	        			"<td>" + toJson[i].localidade + "</td>" +
	        			"<td>" + toJson[i].uf + "</td>" +
	        			"<td><button data-dismiss=\"modal\"" +
	        			"class=\"btn btn-primary\" " +
	        			"onclick=\"fillAddress('" + toJson[i].cep.replace('-', '') + "', '" + toJson[i].logradouro + "', '" + toJson[i].bairro + "', " +
	        					"'" + toJson[i].localidade + "', '" + toJson[i].uf + "')\;\">Selecionar</button></td>" +
	        			"</tr>"
	        }
	        
	        var table = document.getElementById('tableAddress');
	        table.innerHTML = html;
	        
	    }
	};
	
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
	
}

function loadCities(codUf) {
	
	var url = '/endereco/cities/'+codUf
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
	    	comp = document.getElementById('cities');
	    	comp.options.length = 0;

	    	let initOpt = document.createElement('option');
	    	initOpt.setAttribute('value', 0);
	    	initOpt.appendChild(document.createTextNode('.:Selecione:.'));
	    	comp.appendChild(initOpt);
	    	
			// Typical action to be performed when the document is ready:
	        var response = xhttp.responseText;
	        var toJson = JSON.parse(response);
	        for(i = 0; i < toJson.length; i++) {
	        	let opt = document.createElement('option');
	        	opt.setAttribute('value', toJson[i].codMunicipio);
	        	opt.appendChild(document.createTextNode(toJson[i].nomeMunicipio));
	        	comp.appendChild(opt);
	        }
	    }
	};
	
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
}

function loadAddressByZipCode(zipcode) {
var url = '/endereco/' + zipcode;
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
	    	
			// Typical action to be performed when the document is ready:
	        var response = xhttp.responseText;
	        var toJson = JSON.parse(response);
	        console.log("Response: ", toJson);
	        fillAddress(toJson.cep.replace('-', ''), toJson.logradouro, toJson.bairro, toJson.localidade, toJson.uf);
	    }
	};
	
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
}

function fillAddress(zipCode, streeName, district, city, state) {
	document.getElementById('zipcode').value = zipCode;
	document.getElementById('address').value = streeName;
	document.getElementById('district').value = district;
	document.getElementById('city').value = city;
	document.getElementById('state').value = state;
}