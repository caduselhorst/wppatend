/*
 * Função para refresh do dashboard
 */
function refreshData() {
	//console.log("Chamou")
	
	var url = '/web/dashboard'
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		
	    if (this.readyState == 4 && this.status == 200) {
	    	
			// Typical action to be performed when the document is ready:
	        var response = xhttp.responseText;
	        var fromJson = JSON.parse(response);
	       
	        console.log("Dashboad:", fromJson["status"].status);
	        
	        var htmlUsuario = "<tr><th>Login</th><th>Nome</th><th style=\"text-align: center;\">Disponível</th><th style=\"text-align: center;\">Em atendimento</th><th style=\"text-align: center;\">Total de atendimentos</th></tr>"
	        for(i = 0; i < fromJson["usuarios"].length; i++) {
	        	htmlUsuario += "<tr>";
	        	htmlUsuario += "<td>" + fromJson["usuarios"][i].user.username + "</td>"
	        	htmlUsuario += "<td>" + fromJson["usuarios"][i].user.name + "</td>"
	        	if(fromJson["usuarios"][i].disponivel) {
	        		htmlUsuario += "<td style=\"text-align: center;\"><i class=\"fa fa-check-square\" style=\"color: green;\"></i></td>";
	        	} else {
	        		htmlUsuario += "<td style=\"text-align: center;\"><i class=\"fa fa-close\" style=\"color: red;\"></i></td>";
	        	}
	        	if(fromJson["usuarios"][i].emAtendimento) {
	        		htmlUsuario += "<td style=\"text-align: center;\"><i class=\"fa fa-check-square\" style=\"color: green;\"></i></td>";
	        	} else {
	        		htmlUsuario += "<td style=\"text-align: center;\"><i class=\"fa fa-close\" style=\"color: red;\"></i></td>";
	        	}
	        	htmlUsuario += "<td style=\"text-align: center;\">" + fromJson["usuarios"][i].nroAtendimentos + "</td>"
	        	htmlUsuario += "</tr>"
	        }
	        
	        document.getElementById('tbUsuarios').innerHTML = htmlUsuario;
	        
	        var htmlFila = "<tr><th>Data</th><th>Protocolo</th><th>Fone</th><th>Cliente</th></tr>";
	        for(i = 0; i < fromJson["fila"].length; i++) {
				htmlFila += "<tr>"
				htmlFila += "<td>" + fromJson["fila"][i].data + "</td>";
				htmlFila += "<td>" + fromJson["fila"][i].nroProtocolo + "</td>";
				htmlFila += "<td>" + fromJson["fila"][i].foneContato + "</td>";
				htmlFila += "<td>" + fromJson["fila"][i].nomeContato + "</td>";
				htmlFila += "</tr>"
	        }
	        
	        document.getElementById('tbFila').innerHTML = htmlFila;
	        
	        var htmlProcessos = "<tr><th>Processo</th><th style=\"text-align: center;\">Ativo</th></tr>";
	        for(i = 0; i < fromJson["processos"].length; i++) {
	        	htmlProcessos += "<tr>";
	        	htmlProcessos += "<td>" + fromJson["processos"][i].processo + "</td>";
	        	if(fromJson["processos"][i].ativo) {
	        		htmlProcessos += "<td style=\"text-align: center;\"><i class=\"fa fa-check-square\" style=\"color: green;\"></i></td>";
	        	} else {
	        		htmlProcessos += "<td style=\"text-align: center;\"><i class=\"fa fa-close\" style=\"color: red;\"></i></td>";
	        	}
	        	htmlProcessos += "</tr>";
	        }
	        
	        document.getElementById('tbProcessos').innerHTML = htmlProcessos;
	        
	        var htmlStatus = "<tr><th style=\"text-align: center;\">Ativo</th><th>Telefone</th><th>Web Hook</th><th>Mensagem</th></tr>";
	        htmlStatus += "<tr>";
	        if(fromJson["status"].status) {
	        	htmlStatus += "<td style=\"text-align: center;\"><i class=\"fa fa-check-square\" style=\"color: green;\"></i></td>";
	        } else {
	        	htmlStatus += "<td style=\"text-align: center;\"><i class=\"fa fa-close\" style=\"color: red;\"></i></td>";
	        }
	        htmlStatus += "<td>" + (fromJson["status"].fone == null ? " " : fromJson["status"].fone) + "</td>"
	        htmlStatus += "<td>" + (fromJson["status"].webhook == null ? " " : fromJson["status"].webhook) + "</td>"
	        htmlStatus += "<td>" + fromJson["status"].mensagem + "</td>"
	        htmlStatus += "</tr>";
	        
	        document.getElementById('tbStatus').innerHTML = htmlStatus;
	        
	        //var real_data = fromJson["finalizacoes"];
	        google.charts.load("current", {packages:["corechart"]});
	        google.charts.setOnLoadCallback(drawChart);
	      
	        function drawChart() {
				var data = new google.visualization.DataTable();
				data.addColumn('string', 'Finalização');
				data.addColumn('number', 'Total');
				
				var dataOntem = new google.visualization.DataTable();
				dataOntem.addColumn('string', 'Finalização');
				dataOntem.addColumn('number', 'Total');
				
				var dataMesAtual = new google.visualization.DataTable();
				dataMesAtual.addColumn('string', 'Finalização');
				dataMesAtual.addColumn('number', 'Total');
				
				var dataMesAnterior = new google.visualization.DataTable();
				dataMesAnterior.addColumn('string', 'Finalização');
				dataMesAnterior.addColumn('number', 'Total');
				
				var dataAnoAtual = new google.visualization.DataTable();
				dataAnoAtual.addColumn('string', 'Finalização');
				dataAnoAtual.addColumn('number', 'Total');
				
				var dataAnoAnterior = new google.visualization.DataTable();
				dataAnoAnterior.addColumn('string', 'Finalização');
				dataAnoAnterior.addColumn('number', 'Total');

				/*Object.keys(real_data).forEach(function(key) {
					data.addRow([ key.finalizacao, real_data[key] ]);
				});
				*/
				for(i = 0; i < fromJson["finalizacoes"].length; i++) {
					data.addRow([fromJson["finalizacoes"][i].finalizacao, fromJson["finalizacoes"][i].totalFinalizacao]);
				}
				
				for(i = 0; i < fromJson["finalizacoesOntem"].length; i++) {
					dataOntem.addRow([fromJson["finalizacoesOntem"][i].finalizacao, fromJson["finalizacoesOntem"][i].totalFinalizacao]);
				}
				
				for(i = 0; i < fromJson["finalizacoesMesAtual"].length; i++) {
					dataMesAtual.addRow([fromJson["finalizacoesMesAtual"][i].finalizacao, fromJson["finalizacoesMesAtual"][i].totalFinalizacao]);
				}
				
				for(i = 0; i < fromJson["finalizacoesMesAnterior"].length; i++) {
					dataMesAnterior.addRow([fromJson["finalizacoesMesAnterior"][i].finalizacao, fromJson["finalizacoesMesAnterior"][i].totalFinalizacao]);
				}
				
				for(i = 0; i < fromJson["finalizacoesAnoAtual"].length; i++) {
					dataAnoAtual.addRow([fromJson["finalizacoesAnoAtual"][i].finalizacao, fromJson["finalizacoesAnoAtual"][i].totalFinalizacao]);
				}
				
				for(i = 0; i < fromJson["finalizacoesAnoAnterior"].length; i++) {
					dataAnoAnterior.addRow([fromJson["finalizacoesAnoAnterior"][i].finalizacao, fromJson["finalizacoesAnoAnterior"][i].totalFinalizacao]);
				}
				var options = {
					title: 'Finalizações Dia',
					is3D: true,
					pieSliceText: 'value'
				};
				
				var optionsOntem = {
					title: 'Finalizações Dia Anterior',
					is3D: true,
					pieSliceText: 'value'
				};
				
				var optionsMesAtual = {
					title: 'Finalizações Mês Atual',
					is3D: true,
					pieSliceText: 'value'
				};
				
				var optionsMesAnterior = {
					title: 'Finalizações Mês Anterior',
					is3D: true,
					pieSliceText: 'value'
				};
				
				var optionsAnoAtual = {
					title: 'Finalizações Ano Atual',
					is3D: true,
					pieSliceText: 'value'
				};
				
				var optionsAnoAnterior = {
					title: 'Finalizações Ano Anterior',
					is3D: true,
					pieSliceText: 'value'
				};
				
				var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
				var chart1 = new google.visualization.PieChart(document.getElementById('piechart_3d_1'));
				var chart2 = new google.visualization.PieChart(document.getElementById('piechart_3d_2'));
				var chart3 = new google.visualization.PieChart(document.getElementById('piechart_3d_3'));
				var chart4 = new google.visualization.PieChart(document.getElementById('piechart_3d_4'));
				var chart5 = new google.visualization.PieChart(document.getElementById('piechart_3d_5'));
				chart.draw(data, options);
				chart1.draw(dataOntem, optionsOntem);
				chart2.draw(dataMesAtual, optionsMesAtual);
				chart3.draw(dataMesAnterior, optionsMesAnterior);
				chart4.draw(dataAnoAtual, optionsAnoAtual);
				chart5.draw(dataAnoAnterior, optionsAnoAnterior);
			}
	        
	    }
	        
	}
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send();
	
}