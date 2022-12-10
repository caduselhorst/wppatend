var stompClient = null;
var baseUrl = "http://" + location.href.split("/")[2];
var alturaTela = window.screen.availHeight;
var alturaComponentes = 366;

function dataAtualFormatada() {
	var data = new Date();
    var dia  = data.getDate().toString();
    var diaF = (dia.length == 1) ? '0' + dia : dia;
    var mes  = (data.getMonth()+1).toString(); //+1 pois no getMonth() Janeiro começa com zero.
    var mesF = (mes.length == 1) ? '0' + mes : mes;
    var anoF = data.getFullYear();
    var hora = data.getHours();
    var minuto = data.getMinutes();
    var segundos = data.getSeconds();
	return diaF + "/" + mesF + "/" + anoF + " " + hora + ":" + minuto + ":" + segundos;
} 

function sendMessage(numero, mensagem, protocolo) {
	
	var url = baseUrl + "/web/api/v1/chat/send"
	
	var data = {
		phoneNumber: numero,
		message: mensagem,
		protocolo: protocolo
	}
	
	var xhttp = new XMLHttpRequest();
		
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
			console.log("Mensagem enviada", xhttp.responseText);
			
			$("textarea[name='mensagem']").val("");		
		}
	}
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(JSON.stringify(data));
	
}

function tocaAlerta() {
	document.getElementById("alerta").play();
}

function mostraEscondePainelEmojis() {
	
	if($("#panelEmojis").css("display") == "inline") {
		$("#panelEmojis").css({"display": "none"});
	} else {
		$("#panelEmojis").css({"display": "inline"});
	}
}

function mostraEscondeEstadoOperador() {
	if($("#panelEstado").css("display") == "inline") {
		$("#panelEstado").css({"display": "none"});
		$("#maisImg").css({"display": "inline"});
		$("#menosImg").css({"display": "none"});
		$("#panelProtocolo").css({"height": (alturaTela - alturaComponentes + 38) + "px"})
	} else {
		$("#panelEstado").css({"display": "inline"});
		$("#maisImg").css({"display": "none"});
		$("#menosImg").css({"display": "inline"});
		$("#panelProtocolo").css({"height": (alturaTela - alturaComponentes) + "px"})
	}
}

function finalizarAtendimento() {
	
	var url = baseUrl + "/web/api/v1/protocolo"
	
	if($("#finalizacao").val() == 0) {
		alert("Informe o resultado desse atendimento, no campo \"Escolha para finalizar\"");
	} else {
		
		var dados = {
			idProtocolo: $("#idProtocolo").val(),
			idFinalizacao: $("#finalizacao").val()
		}
		
		var xhttp = new XMLHttpRequest();
		
		xhttp.onreadystatechange = function() {
		
		    if (this.readyState == 4 && this.status == 200) {
				$("#idProtocolo").attr({
					"value": 0
				});
				$("#protocolo").html("");
				$("#boxProtocolo").css({"display": "none"});
				$("#mensagem").attr({"value": ""});
				$("#finalizacao").val("0").change();
				
				
				$("#panelMessage").html("");
				
				alert("Atendimento finalizado com sucesso");
				
			}
			
			
			
		}
		
				
		
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send(JSON.stringify(dados));
	}
	
		
}

function disconnect() {
	if(stompClient != null) {
		stompClient.disconnect();
	}
	
	var dados = {
		idUser: $("#usuarioId").val()
	}
	
	var url = baseUrl + "/web/api/v1/user/logout"
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
			window.location.href = "/logout";
		}
	}
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(JSON.stringify(dados));
	
}


function onMessage(message) {
	var jsoned = JSON.parse(message.body)
	
	if(jsoned.type === "PROTOCOLO") {
		$("#idProtocolo").attr({
			"value": jsoned.message.id
		});
		$("#protocolo").html(jsoned.message.protocolo);
		$("#telefone").html(jsoned.message.fone);
		$("#nroTelefone").attr({"value": jsoned.message.fone});
		$("#boxProtocolo").css({"display": "inline"});
		
		var msgInicial = "Olá! Me chamo " + $("#usuarioName").val() + " e o número do seu protocolo é "
			 + jsoned.message.protocolo + ". Como posso ajudar?";
			 
		sendMessage(jsoned.message.fone, msgInicial, jsoned.message.id);
		
		tocaAlerta();
	} else {
		
		//console.log("Tipo de mensagem de chat", jsoned.message.tipo);
		
		var classe;
		var style;
		var msg = jsoned.message.body;
		
		if(jsoned.message.tx_rx == "RECEBIDA") {
			classe = "box-tools pull-left";
			style = "background-color: #72afd2"
		} else {
			classe = "box-tools pull-right";
			style = "background-color: #c0c0c0"
		}
		
		var html = "<div id=\"" + jsoned.message.messageId + "\" class=\"" + classe + "\" style=\"width: 400px; border-radius: 10px; " + style  + "; margin: 3px;\">";;
		html = html + msg + "<br /><span style=\"font-size: 10px;\">" + dataAtualFormatada() + "</span><br />";
		html = html + "</div><br clear=\"all\" />";
		$("#panelMessage").append(html);
		$("#panelMessage").animate({scrollTop: $("#panelMessage")[0].scrollHeight}, 500);
	}
}
 
function connect(username) {
	var socket = new SockJS('/hello');
	stompClient = Stomp.over(socket);
	stompClient.connect({ username: username, }, function() {
		console.log('Web Socket is connected');
		stompClient.subscribe('/users/queue/messages', function(message) {
			onMessage(message);			
		});
	});
}

function getCodUsuario() {
	var url = baseUrl + "/web/api/v1/user/" + $("#usuario").val();
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
		
			var response = xhttp.responseText;
	        var jsoned = JSON.parse(response);
	        
	        $("#usuarioId").attr({
				"value" : jsoned.id
			});
		
		
		}
	}
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
}

function conectaOperador() {
	
	var url = baseUrl + "/web/api/v1/user/" + $("#usuario").val() + "/roteirizador";
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
			var jsoned = JSON.parse(xhttp.responseText);
			$("#ematendimento").attr({"value": jsoned.emAtendimento});
			$("#disponivel").attr({"value": jsoned.disponivel});
			$("#userId").attr({"value": jsoned.userId});
			
			alteraEstado();
		}
	}
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
	
}

function enviaEstado() {
	
	var dado = null;
	var url = baseUrl + "/web/api/v1/user/estado";
	var xhttp = new XMLHttpRequest();
	
	
	
	if($("#disponivel").val() == "0") {
		dado = {
			idUser: $("#usuarioId").val(),
			disponivel: true,
			emAtendimento:	false		
		}
	} else {
		dado = {
			idUser: $("#usuarioId").val(),
			disponivel: false,
			emAtendimento: false		
		}
	}
	
	xhttp.onreadystatechange = function() {
	
	    if (this.readyState == 4 && this.status == 200) {
		
			var response = xhttp.responseText;
	        
	        console.log(response);
	        alteraEstado();
		
		
		}
	}
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(JSON.stringify(dado));
	
}

function colaEmogi(obj) {

	var $edit = $("#mensagem");

	var curValue = $edit.val();
	
	var newValue = curValue + obj.innerHTML
	
	$edit.val(newValue); 
}

function alteraEstado() {
	if($("#disponivel").val() == "0") {
		$("#disponivel").attr({
			"value" : "1"
		});
		$("#btnestado").attr({
			"class" : "btn btn-success",
			"value": "Disponível"
		});
		$("#btndesconectar").prop("disabled", true);
	} else {
		$("#disponivel").attr({
			"value" : "0"
		});
		$("#btnestado").attr({
			"class" : "btn btn-danger",
			"value": "Indisponível"
		});
		$("#btndesconectar").prop("disabled", false);
	}
}
 
$(function() {
	
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect($("#usuario").val());
	});
	$("#btnestado").click(function() {
		enviaEstado();
	});
	$("#btndesconectar").click(function() {
		disconnect();
	});
	$("#btnfinalizar").click(function() {
		finalizarAtendimento();
		
	});
	$("#mostraEscondeButton").click(function() {
		mostraEscondeEstadoOperador();
	});
	$("#btnEmojis").click(function() {
		mostraEscondePainelEmojis();
	});
	$("#btnSendMessage").click(function() {
		sendMessage($("#nroTelefone").val(), $("#mensagem").val(), $("#idProtocolo").val());
	});
	$("#btnSendFile").click(function() {
		$("#chooseFile").click();
	})
	$("#mensagem").on('keypress', function(event) {
		if(event.ctrlKey && event.charCode == 10) {
			sendMessage($("#nroTelefone").val(), $("#mensagem").val(), $("#idProtocolo").val());
		}
	});
	
	getCodUsuario();
	conectaOperador();
	connect($("#usuario").val());
	$("#panelProtocolo").css({"height": alturaTela - alturaComponentes + "px"});
	
});