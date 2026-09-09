<!--ospHistory.jsp-->
<%@ page contentType="text/html;charset=UTF-8" %>

	<!DOCTYPE html>
	<html>
	<head>
		<title>OSP Payout History</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabConfig.css">
		<script>
			const CONTEXT_PATH ="${pageContext.request.contextPath}";
		</script>
	</head>
	<body class="bg-light">
		<div class="container-fluid mt-4">
			<h4 class="mb-4"> OSP Payout History </h4>
			<table class="table table-bordered bg-white">
				<thead class="table-dark">
					<tr>
						<th>ID</th>
						<th>Product</th>
						<th>Sub Product</th>
						<th>DPD</th>
						<th>From Date</th>
						<th>To Date</th>
						<th>Status</th>
						<th>Version</th>
						<th>Created By</th>
						<th>Created Date</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>
		</div>
		<script>
			loadHistory();
			function loadHistory() {
				fetch( CONTEXT_PATH + "/ospPayout/history" )
					.then(r => r.json())
					.then(data => {
						let tbody = document.getElementById("tbody");
						tbody.innerHTML = "";
						if (data.length == 0) {
							tbody.innerHTML =
								"<tr>" +
								"<td colspan='11' style='text-align:center;font-weight:bold;'>" +
								"No Records Found" +
								"</td>" +
								"</tr>";
							return;
						}

						data.forEach(function (d) {

							let row = "";
							row += "<tr>";
							row += "<td>" + d.id + "</td>";
							row += "<td>" + d.product + "</td>";
							row += "<td>" + d.subProduct + "</td>";
							row += "<td>" + d.dpd + "</td>";
							row += "<td>" + formatOnlyDate(d.fromDate) + "</td>";
							row += "<td>" + formatOnlyDate(d.toDate) + "</td>";
							row += "<td>" + d.status + "</td>";
							row += "<td>" + d.version + "</td>";
							row += "<td>" + d.createdBy + "</td>";
							row += "<td>" + formatDate(d.createdDate) + "</td>";
							row += "<td>";
							row += "<button ";
							row += "class='btn btn-primary btn-sm' ";
							row += "onclick='viewCompare(" + d.id + ")'>";
							row += "View";
							row += "</button>";
							row += "</td>";
							row += "</tr>";

							tbody.innerHTML += row;
						});
					});
			}

			function formatDate(date) {

				if (date == null) {
					return "";
				}
				return new Date(date)
					.toLocaleString();
			}
			
			function formatOnlyDate(date) {

			    if (date == null) {
			        return "";
			    }

			    return new Date(date).toLocaleDateString("en-GB");
			}

			function viewCompare(id) {

				window.location.href = CONTEXT_PATH +
					"/mainPage/load?master=OSPPAYOUTCOMPAREMAKER"
					+ "&id=" + id
					+ "&mode=maker";
			}
		</script>
	</body>

	</html>