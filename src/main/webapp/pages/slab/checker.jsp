<!--slab-checker.jsp-->

<%@ page contentType="text/html;charset=UTF-8" %>
	<!DOCTYPE html>
	<html>

	<head>
		<title>Checker Screen</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabConfig.css">
		<script src="${pageContext.request.contextPath}/js/ospRecoverySlabChecker.js"></script>
		<script src="${pageContext.request.contextPath}/js/flowsPayoutChecker.js"></script> <!--snz-flows-->
		<script src="${pageContext.request.contextPath}/js/valuationPayoutChecker.js"></script>
		<!-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"> -->
	</head>

	<script>
		const CONTEXT_PATH = "${pageContext.request.contextPath}";
	</script>

	<script>

		/* const CONTEXT_PATH =
		"${pageContext.request.contextPath}"; */

		/* var userId = "BAN513834";  */

		var userId = localStorage.getItem("user_id");

	</script>

	<body class="bg-light">

		<div class="container-fluid mt-4">

			<h4 class="pageHeading">Slab Checker</h4>


			<!--snz-->
			<div class="filter-row">
				<div class="form-group">
					<label>Product</label>
					<select id="product" class="form-control" onchange="loadSubProducts()">
						<option value="">Select Product</option>
					</select>
				</div>

				<div class="form-group">
					<label>Sub Product</label>
					<select id="subProduct" class="form-control" onchange="openSelectedProductCheckerScreen()">
						<option value="">Select Sub Product</option>
					</select>
				</div>
				<div class="form-group"></div>
				<div class="form-group"></div>

			</div>

			<!--snz-->
			<div id="agencyChecker" style="display:none;">
				<table class="table table-bordered bg-white">
					<thead class="table-dark">
						<tr>
							<th>ID</th>
							<th>Category</th>
							<th>City</th>
							<th>DPD</th>
							<th>Status</th>
							<th>Created By</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody id="tbody"></tbody>
				</table>
			</div>

			<div id="ospChecker" style="display:none;">
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
							<th>Created By</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody id="ospTbody">
					</tbody>
				</table>
			</div>
			
			<!--snz-flows-->
			<div id="flowsPayoutChecker" style="display:none;">
			    <div class="table-responsive">
			        <table class="table table-bordered bg-white">
			            <thead class="table-dark">
			                <tr>
			                    <th>ID</th>
			                    <th>Category</th>
								<th>City</th>
			                    <th>Bucket</th>
			                    <th>From Date</th>
			                    <th>To Date</th>
			                    <th>Status</th>
			                    <th>Created By</th>
			                    <th>Action</th>
			                </tr>
			            </thead>

			            <tbody id="flowsPayoutCheckerBody">

			            </tbody>
			        </table>
			    </div>
			</div>
			
			<!-- VALUATION CHECKER -->
			<div id="valuationPayoutChecker" style="display:none;">
			    <div class="table-responsive">
			        <table class="table table-bordered bg-white">
			            
						<thead class="table-dark">
			                <tr>
			                    <th>ID</th>
			                    <th>Product</th>
			                    <th>Sub Product</th>
			                    <th>From Date</th>
			                    <th>To Date</th>
			                    <th>Version</th>
			                    <th>Status</th>
			                    <th>Created By</th>
			                    <th>Action</th>
			                </tr>
			            </thead>

			            <tbody id="valuationPayoutCheckerBody">
			            </tbody>
			        </table>
			    </div>
			</div>
			
			<!--vehicle TW -->
			<div id="twChecker" style="display:none;">
				<table class="table table-bordered bg-white">
					<thead class="table-dark">
						<tr>
							<th>ID</th>
							<th>Channel Name</th>
							<th>To Date</th>
							<th>Status</th>
							<th>Created By</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody id="twbody">
					</tbody>
				</table>
			</div>
			
			
			<div class="table-responsive" id="gridSectionForTW" style ="display:none";>
				<!-- <table class="table table-bordered text-center"> -->
				<table class=" table-bordered text-center payout-table" id="UsedAutoDMATable">
				<thead>
					<tr class="red-header">
						<th>Channel Partner Name</th>
						<th>Applicable Retention rate for cases disburse till Dec-22
							from inception</th>
						<th>Applicable Retention rate from Jan-23 on Incremental
							sourcing</th>
						<th>Applicable Retention rate for Aug-23 on all the
							caese(Already disbursed + incremental)</th>

					</tr>
				</thead>
				<tbody id="UsedAutoDMABody">
					<tr>
						<td class="row-label">Lok suvidha finance &amp; LTD</td>
						<td><input type="text" class="pct-input" value="10.75"></td>
						<td><input type="text" class="pct-input" value="11.00"></td>
						<td><input type="text" class="pct-input" value="10.75"></td>
					</tr>
					<tr>
						<td class="row-label">Manba Finance</td>
						<td><input type="text" class="pct-input" value="10.75"></td>
						<td><input type="text" class="pct-input" value="11.00"></td>
						<td><input type="text" class="pct-input" value="10.75"></td>
					</tr>
					<tr>
						<td class="row-label">Wheels &amp; EMI</td>
						<td><input type="text" class="pct-input" value="11.50"></td>
						<td><input type="text" class="pct-input" value="12.50"></td>
						<td><input type="text" class="pct-input" value="12.50"></td>
					</tr>
					<tr class="min-rate-row">
						<td class="row-label">UPI Money ltd</td>
						<td><input type="text" class="pct-input" value="11.50"
							></td>
						<td><input type="text" class="pct-input" value="11.50"></td>
						<td><input type="text" class="pct-input" value="11.50"
							></td>
					</tr>
				</tbody>
			</table>
			</div>
			
			
			<div id="cvChecker" style="display:none;">
				<table class="table table-bordered bg-white">
					<thead class="table-dark">
						<tr>
							<th>ID</th>
							<th>After April 21 Retention Rate</th>
							<th>Before April 21 Retention Rate</th>
							<th>Status</th>
							<th>Created By</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody id="cvbody">
					</tbody>
				</table>
			</div>
			
			
			
		<!-- 	I process Counsellor for kerala -->
		
		
		
			<div id="ipkCheckerSection" style="display:none;">
				<table class="table table-bordered bg-white" id="ipkCheckerTable">
					<thead class="table-dark">
						<tr>
						<th>ID</th>
						<th>State</th>
						<th>Cycle From</th>
						<th>Cycle To</th>
						<th>Created Date</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
					</thead>
					<tbody id="ipkCheckerBody">
					</tbody>
				</table>
			</div>


		<div id="ipkViewSection" style="display: none;">
			<div class="table-panel">
				<table class="table table-bordered text-center payout-table"
					id="ipkInboundTable">
					<thead>
						<tr class="red-header">
							<th>Type</th>
							<th>Slab From</th>
							<th>Slab To</th>
							<th>Incentive %</th>
							<th>Remark</th>
						</tr>
					</thead>
					<tbody id="ipkInboundBody"></tbody>
				</table>
			</div>

			<div class="table-panel">
				<table class="table table-bordered text-center payout-table"
					id="ipkOutboundTable">
					<thead>
						<tr class="red-header">
						<th>Type</th>
							<th>Slab From</th>
							<th>Slab To</th>
							<th>Incentive %</th>
							<th>Remark</th>
						</tr>
					</thead>
					<tbody id="ipkOutboundBody"></tbody>
				</table>
			</div>

			<div class="table-panel">
				<table class="table table-bordered text-center payout-table"
					id="ipkUsedCarTable">
					<thead>
						<tr class="red-header">
							<th>Type</th>
							<th>Slab From</th>
							<th>Slab To</th>
							<th>Incentive %</th>
							<th>Remark</th>
						</tr>
					</thead>
					<tbody id="ipkUsedCarBody"></tbody>
				</table>
			</div>
		</div>
		
		<!-- other than	manipal Counsellor for kerala -->
		
		
		<div id="mpkCheckerSection" style="display:none;">
				<table class="table table-bordered bg-white" id="mpkCheckerTable">
					<thead class="table-dark">
						<tr>
						<th>ID</th>
						<th>State</th>
						<th>Cycle From</th>
						<th>Cycle To</th>
						<th>Created Date</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
					</thead>
					<tbody id="mpkCheckerBody">
					</tbody>
				</table>
			</div>


		<div id="mpkViewSection" style="display: none;">
			<div class="table-panel">
				<table class="table table-bordered text-center payout-table"
					id="mpkInboundTable">
					<thead>
						<tr class="red-header">
							<th>Type</th>
							<th>Slab From</th>
							<th>Slab To</th>
							<th>Incentive %</th>
							<th>Remark</th>
						</tr>
					</thead>
					<tbody id="mpkInboundBody"></tbody>
				</table>
			</div>

			<div class="table-panel">
				<table class="table table-bordered text-center payout-table"
					id="mpkOutboundTable">
					<thead>
						<tr class="red-header">
						<th>Type</th>
							<th>Slab From</th>
							<th>Slab To</th>
							<th>Incentive %</th>
							<th>Remark</th>
						</tr>
					</thead>
					<tbody id="mpkOutboundBody"></tbody>
				</table>
			</div>

			<div class="table-panel">
				<table class="table table-bordered text-center payout-table"
					id="mpkUsedCarTable">
					<thead>
						<tr class="red-header">
							<th>Type</th>
							<th>Slab From</th>
							<th>Slab To</th>
							<th>Incentive %</th>
							<th>Remark</th>
						</tr>
					</thead>
					<tbody id="mpkUsedCarBody"></tbody>
				</table>
			</div>
		</div>


		<div class="table-responsive shadow-sm rounded border"
			id="maniPalCheckerId" style="display: none;">
			<table
				class="table table-bordered border-secondary border-1 align-middle mb-0">
				<thead class="table-dark text-center align-middle">
					<tr>
						<th style="width: 8%;">Slab</th>
						<th style="width: 20%;">Case Type</th>
						<th style="width: 12%;">Cases From</th>
						<th style="width: 12%;">Cases To</th>
						<th scope="col" style="width: 10%;"
							class="border-end border-secondary">Fixed Salary %</th>
						<th scope="col" style="width: 10%;"
							class="border-end border-secondary">Incentive %</th>
						<th scope="col" style="width: 10%;"
							class="border-end border-secondary">Per Case Amt</th>
						<th style="width: 30%;">Remark</th>
						<th style="width: 18%;">Action</th>
					</tr>
				</thead>
				<tbody id="maniPalCheckerBody">
					<!-- Rendered by JavaScript -->
				</tbody>
			</table>
		</div>
		
<div id="personalLoanCheckerContainer" class="table-responsive" style="display: none;">
<table class="table table-bordered table-striped align-middle">
<thead class="table-dark text-center">
<tr>
<th scope="col">Category</th>
<th scope="col">Payout Type</th>
<th scope="col">Min Value</th>
<th scope="col">Max Value</th>
<th scope="col">Payout</th>
<th scope="col">Actions</th>
</tr>
</thead>
<tbody id="personalLoanCheckerBody">
<!-- Data rows will be injected here dynamically -->
</tbody>
</table>
</div>
 
 
<div id="educationLoanCheckerContainer" class="table-responsive" style="display: none;" >
<table class="table table-bordered table-striped align-middle">
<thead class="table-dark text-center">
<tr>
<th class="border-end">Category</th>
<th class="border-end">Payout Type</th>
<th class="border-end">Min</th>
<th class="border-end">Max</th>
<th class="border-end">Payout</th>
<th class="border-end">Cycle From</th>
<th class="border-end">Cycle To</th>
<th>Actions</th>
</tr>
</thead>
<tbody id="educationLoanCheckerBody">
<!-- JavaScript dynamically inserts rows here -->
</tbody>
</table>
</div>
 
 
	</div>

		<script>
			//snz- function name change 
			function loadAgencyPending() {

				/* fetch("/payout/pending?user=checker1") */

				/* fetch(CONTEXT_PATH + "/payout/pending?user=" + userId)
				 */

				fetch("${pageContext.request.contextPath}/payout/pending?user=" + userId)
					.then(r => r.json())

					.then(data => {

						let tbody = document.getElementById("tbody");

						tbody.innerHTML = "";


						if (data.length === 0) {

							tbody.innerHTML =
								"<tr>" +
								"<td colspan='7' style='text-align:center;font-weight:bold;padding:25px;color:#198754;'>" +
								"No Payout Records Pending For Approval" +
								"</td>" +
								"</tr>";

							return;
						}


						data.forEach(function (d) {

							let row = "";

							row += "<tr>";

							row += "<td>" + d.id + "</td>";

							row += "<td>" + d.category + "</td>";

							row += "<td>" + d.city + "</td>";

							row += "<td>" + d.dpd + "</td>";

							row += "<td>" + d.status + "</td>";

							row += "<td>" + d.createdBy + "</td>";

							row += "<td>";

							row += "<button ";
							row += "class='btn btn-success btn-sm me-1' ";
							row += "onclick='approve(" + d.id + ")'>";
							row += "Approve";
							row += "</button>";

							row += "<button ";
							row += "class='btn btn-danger btn-sm me-1' ";
							row += "onclick='rejectRecord(" + d.id + ")'>";
							row += "Reject";
							row += "</button>";

							row += "<button ";
							row += "class='btn btn-info btn-sm' ";
							row += "onclick='viewHistory(" + d.id + ")'>";
							row += "View";
							row += "</button>";

							row += "</td>";

							row += "</tr>";

							tbody.innerHTML += row;

						});



					});
			}


			//snz
			/*
			function approve(id){
			
				fetch("${pageContext.request.contextPath}/payout/approve/"+ id + "?user=" + userId, {
				  method:"POST"
					})
			
				.then(()=>{
			
				alert("Approved");
				loadAgencyPending();
			
				});
			}*/


			//snz
			/*
			function rejectRecord(id){
			
				let remarks=prompt("Enter remarks");
			
				fetch("${pageContext.request.contextPath}/payout/reject/"
					+ id
					+ "?user="
					+ userId
					+ "&remarks="
					+ remarks,
					{
					method:"POST"
					})
				
				.then(()=>{
				alert("Rejected");
				loadAgencyPending();
				});
			} */

			//snz
			function approve(id) {

				console.log("================================");
				console.log("APPROVE BUTTON CLICKED");
				console.log("ID =", id);
				console.log("CHECKER USER =", userId);
				/* 
				   fetch(
				   CONTEXT_PATH + "/payout/approve/" + id + "?user=" + userId,{
					   method:"POST"
				   }) */

				fetch("${pageContext.request.contextPath}/payout/approve/" + id + "?user=" + userId, {
					method: "POST"
				})

					.then(response => {

						console.log("HTTP STATUS =", response.status);

						if (!response.ok) {

							return response.text()
								.then(err => {

									console.error("BACKEND ERROR =", err);

									throw new Error(err);
								});
						}

						return response.text();
					})

					.then(msg => {

						console.log("SUCCESS MESSAGE =", msg);

						alert(msg);

						loadAgencyPending();

						console.log("================================");
					})

					.catch(err => {

						console.error("APPROVE FAILED");
						console.error("ID =", id);
						console.error("USER =", userId);
						console.error("ERROR MESSAGE =", err.message);
						console.error("STACK =", err.stack);
						console.error("********\n", err);
						console.log("================================");
						alert("ERROR : " + err.message);

					});

			}

			//snz
			function rejectRecord(id) {

				let remarks = prompt("Enter remarks");

				console.log("================================");
				console.log("REJECT BUTTON CLICKED");
				console.log("ID =", id);
				console.log("CHECKER USER =", userId);
				console.log("REMARKS =", remarks);

				/*  fetch(
				 CONTEXT_PATH + "/payout/reject/" + id
				 + "?user=" + userId
				 + "&remarks=" + remarks,{
					 method:"POST"
				 }) */

				fetch("${pageContext.request.contextPath}/payout/reject/"
					+ id
					+ "?user="
					+ userId
					+ "&remarks="
					+ remarks,
					{
						method: "POST"
					})

					.then(response => {
						console.log("HTTP STATUS =", response.status);
						if (!response.ok) {
							return response.text()
								.then(err => {
									console.error("BACKEND ERROR =", err);
									throw new Error(err);
								});
						}
						return response.text();
					})
					.then(msg => {
						console.log("SUCCESS MESSAGE =", msg);
						alert(msg);
						loadAgencyPending();
						console.log("================================");
					})
					.catch(err => {
						console.error("REJECT FAILED");
						console.error("ERROR MESSAGE =", err.message);
						console.error(err);
						alert("ERROR : " + err.message);
						console.log("================================");
					});
			}



			function viewHistory(id) {
				console.log("CHECKER ID =", id);
				/* window.location.href =
				"/payoutCompare?id="
				+ id +
				"&mode=checker"; */

				/* window.location.href =
	   CONTEXT_PATH +
	   "/mainPage/load?master=PAYOUTCOMPARECHECKER"
	   + "&id=" + id
	   + "&mode=checker";  */

				window.location.href =
					"${pageContext.request.contextPath}/mainPage/load?master=PAYOUTCOMPARECHECKER"
					+ "&id="
					+ id
					+ "&mode=checker";

			}

		</script>

	</body>

	</html>



