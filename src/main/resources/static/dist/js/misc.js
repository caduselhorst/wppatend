function goToProductFilter(pName) {
	url = 'buscar?pNome=' + pName + '&pg=1';
	console.log('URL: ', url);
	window.location.href=url;
}

function saveSupplierInfoInProduct(prodId, suppId, suppCode) {
	var url = "/fornecedorInsumo/add?insumoId=" + prodId + "&fornecedorId=" + suppId + "&codInsumoFornecedor=" + suppCode;
	
	console.log("URL:", url);
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		
	    if (this.readyState == 4 && this.status == 200) {
	    	var response = xhttp.responseText;
	        var toJson = JSON.parse(response);
	        console.log("Insumo: ", toJson);
	        location.reload();
	        
	    }
	}
	
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
		
}