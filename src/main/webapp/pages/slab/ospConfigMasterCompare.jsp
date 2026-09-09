<!--configMasterCompare.jsp-->
<%@ page contentType="text/html;charset=UTF-8" %>

	<!DOCTYPE html>
	<html>

	<head>
		<title>Config Master Compare</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/configMaster.css">
	</head>
	
	<script src="${pageContext.request.contextPath}/js/ospConfigMasterCompare.js"></script>
	<script>
		const CONTEXT_PATH = "${pageContext.request.contextPath}";
	</script>

	<body>

		<div class="container-fluid">
	
			<div class="compareCard">
				<script>
				</script>

				<div class="compare-header">
					<button type="button" class="btn btn-primary backBtn" onclick="goBack()">
						Back
					</button>
					<h2>Config Master Screen</h2>
				</div>

				<div id="topTitle" class="compareTitle">
					TOP
				</div>
				<table class="compare-table">
					<thead>
					</thead>

					<tbody id="topBody">
					</tbody>

				</table>

				<div id="bottomTitle" class="title">
					BOTTOM
				</div>
				<table class="compare-table">
					<thead>
					</thead>

					<tbody id="bottomBody">
					</tbody>
				</table>

			</div>

		</div>

	</body>

	</html>