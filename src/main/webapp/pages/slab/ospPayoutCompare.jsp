<!--ospPayoutCompare.jsp-->
<%@ page contentType="text/html;charset=UTF-8" %>

	<!DOCTYPE html>
	<html>

	<head>
		<title>OSP Payout Compare</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabConfig.css">
	</head>

	<body>
		<div class="container-fluid">

			<div class="box">
				<script>
				
					function goBack() {
					    window.history.back();
					}
				</script>

				<div class="compare-header" style="display:flex;justify-content:space-between;align-items:center;">
					<h2>OSP Payout Compare</h2>
					<button type="button" class="btn btn-primary" onclick="goBack()">Back</button>
				</div>

				<!-- TOP -->

				<div id="topTitle" class="title">TOP</div>
				<table class="table table-bordered compare-table">
					<thead>
						<tr>
							<th>From Amount</th>
							<th>To Amount</th>
							<th>Incentive %</th>
						</tr>
					</thead>
					<tbody id="topBody">
					</tbody>
				</table>

				<!-- BOTTOM -->

				<div id="bottomTitle" class="title">BOTTOM</div>
				<table class="table table-bordered compare-table">
					<thead>
						<tr>
							<th>From Amount</th>
							<th>To Amount</th>
							<th>Incentive %</th>				
						</tr>
					</thead>
					<tbody id="bottomBody">
					</tbody>
				</table>
				</div>
		</div>
		<script>
			document.addEventListener("DOMContentLoaded", function () {
				const params = new URLSearchParams(window.location.search);
				const id =params.get("id");
				const mode =params.get("mode");

				let api = "";
				if (mode == "maker") {
					api =
						"${pageContext.request.contextPath}"
						+ "/ospPayout/makerCompare/"
						+ id;
					document.getElementById("topTitle").innerHTML ="CURRENT APPROVED VERSION";
					document.getElementById("bottomTitle").innerHTML ="PREVIOUS APPROVED VERSION";
				}
				else {
					api =
						"${pageContext.request.contextPath}"
						+ "/ospPayout/checkerCompare/"
						+ id;
					document.getElementById("topTitle").innerHTML ="PENDING VERSION";
					document.getElementById("bottomTitle").innerHTML ="CURRENT APPROVED VERSION";
				}

				fetch(api)
					.then(r => r.json())
					.then(data => {
						buildMaps(data.top, data.bottom);
						renderTable(data.top, "topBody", "TOP");
						renderTable(data.bottom, "bottomBody", "BOTTOM");
					});
			});

			function buildMaps(current, previous) {

			    window.currentData = {};
			    window.previousData = {};

			    if (current != null) {
			        current.details.forEach(function(d){
			            let key = d.fromAmount + "-" + d.toAmount;
			            currentData[key] = d.incentivePercent;
			        });
			    }

			    if (previous != null) {
			        previous.details.forEach(function(d)
					{
			            let key = d.fromAmount + "-" + d.toAmount;
			            previousData[key] = d.incentivePercent;
			        });
			    }
			}

			function renderTable(master, bodyId, type) {

			    let body = document.getElementById(bodyId);
			    body.innerHTML = "";

			    if(master == null){
			        body.innerHTML =
			            "<tr>" +
			            "<td colspan='3' style='text-align:center;font-weight:bold;'>" +
			            "No Previous Record Found" +
			            "</td>" +
			            "</tr>";
			        return;
			    }

			    master.details.forEach(function(d){
			        let key = d.fromAmount + "-" + d.toAmount;
			        let changed = false;
			        if(type == "TOP"){
			            changed = currentData[key] != previousData[key];
			        }else{
			            changed = previousData[key] != currentData[key];

			        }

			        let row = "";
			        row += "<tr>";
			        row += "<td>" + d.fromAmount + "</td>";
			        if(d.isMax){
					    row += "<td>MAX</td>";
					}else{
					    row += "<td>" + d.toAmount + "</td>";
					}

			        row += "<td class='" + (changed ? "changed" : "") + "'>";
			        row += (d.incentivePercent == null ? "" : d.incentivePercent);
			        row += "</td>";
			        row += "</tr>";
			        body.innerHTML += row;
			    });

			}
		</script>
	</body>

	</html>