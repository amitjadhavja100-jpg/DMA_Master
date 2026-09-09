<!--flowsPayoutCompare.jsp-->
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

    <title>Flows Payout Compare</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabConfig.css">
    <script src="${pageContext.request.contextPath}/js/flowsPayoutCompare.js"></script>

    <script> const CONTEXT_PATH = "${pageContext.request.contextPath}";</script>

</head>

<body class="bg-light">

<div class="container-fluid mt-4">
	
	<div class="pageHeading" style="display:flex;justify-content:space-between;align-items:center;">
	    <h2>Flows Payout Compare</h2>
	    <button type="button" class="btn btn-primary" onclick="goBack()">Back</button>
	</div>
	
	<h5 class="title" id="topTitle">Pending / New Record</h5>

	<div class="table-responsive">
	    <table class="table table-bordered text-center payout-table">
	        <thead>
	            <tr class="red-header">
	                <th rowspan="2">Resolution</th>
	                <th id="pendingTableType"></th>
	            </tr>
	            <tr class="sub-header" id="pendingPerformanceHeader">
	            </tr>
	        </thead>

	        <tbody id="pendingBody">
	        </tbody>
	    </table>
	</div>

	<h5 class="title" id="bottomTitle">Current Approved Record</h5>

	<div class="table-responsive">
		<table id="approvedCompareTable" class="table table-bordered text-center payout-table">
	
			<thead>
				<tr class="red-header">
					<th rowspan="2">Resolution</th>
					<th id="approvedTableType"></th>
				</tr>
	
				<tr class="sub-header" id="approvedPerformanceHeader">
				</tr>
			</thead>
	
			<tbody id="approvedBody">
			</tbody>
	
		</table>
	</div>

</div>

</body>

</html>