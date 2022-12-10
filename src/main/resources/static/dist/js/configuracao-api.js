function loadQRCode() {

	var url = '/web/api/v1/qrcode/gerar';
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	
		console.log('response text', xhttp.responseText);
		
		if(xhttp.responseText) {
			document.getElementById('loading-panel').style.display='none';
		
			var response = xhttp.responseText;
		    var toJson = JSON.parse(response);
		    
		    if(toJson.message) {
		    	document.getElementById('msg-panel').style.display='inline';
		    	document.getElementById('message').innerHTML=toJson.message
		    }
		    
		    if(toJson.qrCode) {
		    	document.getElementById('img-panel').style.display='inline';
		    	document.getElementById('img-qr-code').setAttribute('src', toJson.qrCode);
		    }
		}

			
	}
	
	
	document.getElementById('loading-panel').style.display='inline';
	document.getElementById('msg-panel').style.display='none';
	document.getElementById('img-panel').style.display='none';
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();

}

function setApiWebHook() {

	var webHookUrl = document.getElementById('txt-webhook').value;

	if(webHookUrl != '') {
		var url = '/web/api/v1/webhook';
		var data = {
			webhook : webHookUrl
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		
			if(xhttp.responseText) {
			
				console.log("Response text", xhttp.responseText);
				document.getElementById('loading-panel').style.display='none';
							
				var response = xhttp.responseText;
			    var toJson = JSON.parse(response);
			    
				
				document.getElementById('message').innerHTML=toJson.message
				document.getElementById('msg-panel').style.display='inline';
			}
				
		}
		
		document.getElementById('loading-panel').style.display='inline';
		document.getElementById('msg-panel').style.display='none';
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send(JSON.stringify(data));
		
		
	} else {
		alert('Informe um endereço válido');
	}	
}

function acao(tipoAcao) {
	if(tipoAcao != 'reset' && tipoAcao != 'logout') {
		alert('Ação não conhecida');
	} else {
		var url;
		
		if(tipoAcao == 'reset') {
			url = '/web/api/v1/resetar';
		} else {
			url = '/web/api/v1/logout';
		}
		
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		
			if(xhttp.responseText) {
				
				document.getElementById('loading-panel').style.display='none';
							
				var response = xhttp.responseText;
			    var toJson = JSON.parse(response);
			    
				
				document.getElementById('message').innerHTML=toJson.message
				document.getElementById('msg-panel').style.display='inline';
				
			}
			
		}
		
		document.getElementById('loading-panel').style.display='inline';
		document.getElementById('msg-panel').style.display='none';
		
		document.getElementById('btn-reset').disabled = true;
		document.getElementById('btn-logout').disabled = true;
		
		xhttp.open("GET", url, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send();
		
	}
}