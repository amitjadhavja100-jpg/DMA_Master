<!--payout.jsp-->

<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabConfig.css">
	<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabPanIndia.css"> --%>
	
	<!-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"> -->
	<script src="${pageContext.request.contextPath}/js/payout.js"></script>
	<script src="${pageContext.request.contextPath}/js/ospPayout.js"></script>
	<script src="${pageContext.request.contextPath}/js/ipkPayout.js"></script>
	<script src="${pageContext.request.contextPath}/js/flowsPayout.js"></script>
	<script src="${pageContext.request.contextPath}/js/valuationPayout.js"></script>

	<script>
		const CONTEXT_PATH = "${pageContext.request.contextPath}";
		/* var userId = "BAN513834"; */
		var userId = localStorage.getItem("user_id");
	</script>


</head>

<body>

	<div class="main-container">

		<!-- TOP -->
		<div class="top-header">

			<!-- <h4 class="pageHeading">Slab Maker</h4> -->
			<h4 class="pageHeading">Slab Maker</h4>

			<!--  <div>
            <button class="btn red-btn me-2" onclick="addRow()">Add Row</button>
            <button class="btn red-btn" onclick="addColumn()">Add Column</button>
        </div> -->

		</div>

		<!--snz -->
		<div class="filter-row">
			<div class="form-group">
				<label>Product</label> <select id="product" class="form-control"
					onchange="loadSubProducts()">
					<option value="">Select Product</option>
				</select>
			</div>

			<div class="form-group">
				<label>Sub Product</label> <select id="subProduct"
					class="form-control" onchange="openSelectedSlab()">
					<option value="">Select Sub Product</option>
				</select>
			</div>
			<div class="form-group"></div>
			<div class="form-group"></div>
		</div>

		<!--SNZ-->
		<div id="collectionAgencySlab" style="display: none;">

			<!-- FILTER -->
			<!--  <div class="row mb-3"> -->
			<div class="filter-row">

				<!-- <div class="col-md-2"> -->
				<div class="form-group">
					<!-- nitin -->
					<!-- <label>Category</label> --> 
					<label id="lblCategory">Category</label>
					<select id="category" class="form-control"></select>
				</div>

				<div class="form-group" id="cityDiv">
					<label id="lblCity">City</label> <!-- nitin -->
					<select id="city" class="form-control"></select>
				</div>

				<div class="form-group" id="dpdDiv">
					 <label id="lblDpd">DPD</label> 
					<select id="dpdDropdown" class="form-control">
					</select>
				</div>



				<div class="form-group">
					<!-- nitin -->
					<!-- <label>From Date</label> --> 
					<label id="lblFromDate">From Date</label>
					<input type="date" id="fromDate"
						class="form-control">
				</div>

				<div class="form-group">
					 <label id="lblToDate">To Date</label>
					<input type="date" id="toDate" class="form-control">
				</div>

				<!--         <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button id="btnFetch" class="btn red-btn w-100" onclick="fetchData()">Fetch</button>
				</div>

				<!--  <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button  id="btnReset" class="btn btn-secondary w-100" onclick="resetTable()">Reset</button>
				</div>

			</div>

			<!-- DPD -->
			<!-- <div class="mb-3">
			        <button class="dpd-btn active-dpd"
			                onclick="setDPD('181-270',this)">
			                DPD 181-270
			        </button>
			        <button class="dpd-btn"
			                onclick="setDPD('271-360',this)">
			                DPD 271-360
			        </button>
			    </div> -->

			<!-- TITLE -->

			<div class="title-bar mb-2">
				<span id="headerText"></span>
			</div>

			<div class="action-panel">

				<select id="newRowSelect" class="form-control">
					<option value="">Select Row</option>
				</select>

				<button id="btnAddRow" class="btn btn-success" type="button" onclick="addRow()">
					Add Row</button>

				<select id="newColSelect" class="form-control">
					<option value="">Select Column</option>
				</select>

				<button  id="btnAddColumn" class="btn btn-success" type="button" onclick="addColumn()">
					Add Column</button>

			</div>

			<!-- <div class="action-panel">
			
			    <select id="newRowSelect"
			            class="form-control"
			            onclick="addRow()">
			        <option value="">Select Row</option>
			    </select>
			
			    <button class="btn btn-success"
			            type="button"
			            onclick="confirmAddRow()">
			        Add Row
			    </button>
			
			    <select id="newColSelect"
			            class="form-control"
			            onclick="addColumn()">
			        <option value="">Select Column</option>
			    </select>
			
			    <button class="btn btn-success"
			            type="button"
			            onclick="confirmAddColumn()">
			        Add Column
			    </button>

			</div> -->


			<!-- TABLE -->
			<div class="table-responsive">
				<!-- <table class="table table-bordered text-center"> -->
				<table class="table table-bordered text-center payout-table">
					<thead>
						<tr class="red-header">
							<th rowspan="2" id="leftHeader">Collection</th>
							<th id="ceColSpanText">CE% on ENR</th>
						</tr>
						<tr class="sub-header" id="ceHeader"></tr>
					</thead>
					<tbody id="tbody"></tbody>
				</table>
			</div>

			<!-- BUTTON -->
			<!-- <div class="mt-3 text-end"> -->

			<div class="mt-3 d-flex justify-content-end">
				<button id="btnSubmit" class="btn red-btn me-2" onclick="saveData()">
					Submit For Approval</button>
				<button id="btnHistory" class="btn btn-primary btn-sm" style="margin-left: 15px;"
					onclick="viewMakerHistory()">View History</button>
			</div>
		</div>

		<!--SNZ - collection osp-->
		<div id="collectionOspSlab" style="display: none;">

			<div class="filter-row">
				<div class="form-group">
					<label>DPD</label>
					<select
					    id="ospDpd"
					    class="form-control"
					    onchange="loadOspRanges()">
					    <option value="">Select DPD</option>
					</select>
				</div>

				<div class="form-group">
					<label>From Date</label> <input type="date" id="ospFromDate">
				</div>

				<div class="form-group">
					<label>To Date</label> <input type="date" id="ospToDate">
				</div>

				<div class="form-group">
					<button class="btn red-btn" onclick="fetchOSP()">Fetch</button>
					<button class="btn btn-secondary" onclick="resetOSP()">
						Reset</button>
				</div>

			</div>

			<table class="table table-bordered">
				<thead>
					<tr style="background-color: #b71c1c;">
						<th>From Amount</th>
						<th>To Amount</th>
						<th>Incentive %</th>
					</tr>
				</thead>
				<tbody id="ospBody">

				</tbody>
			</table>

			<div style="margin-top: 20px">
				<button class="btn btn-success" onclick="saveOSP()">Submit
					For Approval</button>
				<button class="btn btn-primary" onclick="viewOSPHistory()">
					View History</button>
			</div>
		</div>
		
		<!--snz-->
		<!-- FLOWS PAYOUT -->
		<div id="flowsSlab" style="display:none;">

		    <div class="filter-row">

				<div class="form-group">
					<label>Category</label>
					<select id="flowsCategory" class="form-control" onchange="handleFlowsCategoryChange()">
						<option value="">Select Category</option>
					</select>
				</div>
				
				<div class="form-group" id="flowsCityContainer">
				    <label>City</label>
				    <select id="flowsCity"
				            class="form-control"
				            onchange="handleFlowsCityChange(); updateFlowsHeaderText()">
				        <option value="">Select City</option>
				    </select>
				</div>
				
				<!--<div class="form-group">
					<label>City</label>
					<select id="flowsCity" class="form-control" onchange="loadFlowsBuckets(); updateFlowsHeaderText()">
						<option value="">Select City</option>
					</select>
				</div>-->
				
				<div class="form-group">
					<label>Bucket</label>
					<select id="flowsBucket" class="form-control" onchange="loadFlowsStructure()">
						<option value="">Select Bucket</option>
					</select>
				</div>

		        <div class="form-group">
		            <label>From Date</label>
					<input type="date" id="flowsFromDate" class="form-control">
		        </div>

		        <div class="form-group">
		            <label>To Date</label>
					<input type="date" id="flowsToDate" class="form-control">
		        </div>
				
		        <div class="form-group">
					<button class="btn red-btn w-100" onclick="fetchFlows()">Fetch</button>
		        </div>

		        <div class="form-group">
					<button class="btn btn-secondary w-100" onclick="resetFlows()">
		                Reset
		            </button>
		        </div>
		    </div>


		    <!--<div class="title-bar mb-2">
		        <span id="flowsHeaderText">
		        </span>
		    </div>-->
			
			<div id="flowsHeaderSection" class="title-bar mb-2" 
					style="
						background-color:#FFD966 !important;
						color:#000 !important;
						border:1px solid #D6B656;
						padding:12px;
						text-align:center;
						font-weight:bold;
						margin-bottom:40px;"
						>
			
				<span id="flowsHeaderText"></span>
			
			</div>

		    <div class="table-responsive">
				<table class="table table-bordered text-center payout-table" id="flowsPayoutTable">
		            <thead>
		                <tr class="red-header">
							<th rowspan="2" id="flowsLeftHeader">Resolution</th>
							<th id="flowsPerformanceHeader">Performance</th>
		                </tr>
						<tr class="sub-header" id="flowsPerformanceRow"></tr>
		            </thead>
					
		            <tbody id="flowsBody">
		            </tbody>
		        </table>
		    </div>	

		    <div class="mt-3 d-flex justify-content-end" style="gap:15px;">
				<button id="btnFlowsSubmit" class="btn red-btn me-2" onclick="saveFlows()">Submit For Approval</button>
				<button id="btnFlowsView" class="btn btn-primary" onclick="viewFlowsCompare()">View</button>
		    </div>
			
		</div>
		
		<!--valuation-slab-->
		<div id="valuationSlab" style="display:none;">
		    <div class="filter-row">

		        <div class="form-group">
		            <label>From Date</label>
		            <input
		                type="date"
		                id="valuationFromDate"
		                class="form-control">
		        </div>

		        <div class="form-group">
		            <label>To Date</label>
		            <input
		                type="date"
		                id="valuationToDate"
		                class="form-control">
		        </div>
				
		        <div class="form-group">
		            <button
		                type="button"
		                class="btn red-btn"
		                onclick="fetchValuationSlab()">
		                Fetch
		            </button>
		        </div>
				<div class="form-group"></div>

		    </div>

		    <div class="table-responsive">

		        <table id="valuationTable" class="table table-bordered text-center payout-table">
		            <thead>
		                <tr class="red-header">
		                    <th>IBOX ID</th>
		                    <th>Valuer Name</th>
		                    <th>Product Sub Type</th>
		                    <th>New rate under GST</th>
		                </tr>
		            </thead>

		            <tbody id="valuationBody">
		            </tbody>

		        </table>
		    </div>

		    <div class="mt-3 d-flex justify-content-end" style="gap:15px;">
				<button type="button" class="btn red-btn" onclick="saveValuationSlab()">
					Submit For Approval
				</button>
				
				<button id="btnValuationView" type="button" class="btn btn-primary" onclick="viewValuationCompare()">
					View
				</button>
		    </div>

		</div>
		
		<!-- personalLone start -->	
<div  id="personalLonecategory" style="display: none;">
<div class="filter-row"  >
<div class="form-group">
<label id="lblCategory2">Category</label>
<select id="category2" class="form-control"></select>
</div>
 
				<div class="form-group">
<label id="lblFromDate2">From Date</label>
<input type="date" id="fromDate2"

						class="form-control">
</div>
 
				<div class="form-group">
<label id="lblToDate2">To Date</label>
<input type="date" id="toDate2" class="form-control">
</div>
</div>
 
	<div class="personalLoneFetch">
<div class="form-group w-100">
<button id="btnPerFetch" class="btn red-btn w-100" onclick="fetchDataPersonlaLone()">Fetch</button>
</div>
 
   <div class="form-group w-100">
<button  id="btnPerReset" class="btn btn-secondary w-100" onclick="handleResetForm()">Reset</button>
</div>
</div>

 
<div class="title-bar mb-2">
<span id="headerText"></span>
</div>
 
<div  class="top-header">
<div class="action-panel">
 
				<select id="newPerRowSelect" class="form-control">
<option value="">Select Row</option>
</select>
 
				<button id="btnPerAddRow" class="btn btn-success" type="button" onclick="addDynamicRow('BIL', false)">

					Add Row
</button>
 
</div>			
</div>
 
<div class="table-responsive"  id="personal-Lone-id" style="display: none;">
<table class="table table-bordered text-center payout-table">
<thead>
<!-- Row 1: Main Title -->
<tr class="red-header">
<th colspan="4" class="paisaBazaarData-table">

            PL & Doctor Loans (Salaried & SE across All Location)
</th>
</tr>    
<tr class="red-header">
<th colspan="3" class="childPaisaBazaarHeader">DSA (NET) Biz Bucket</th>        
<th colspan="1" class="childPaisaBazaarHeader">Slab with GST</th>
</tr> 
<tr class="red-header">
<!--   <th></th> -->        
<th colspan="3" class="childPaisaBazaarHeader">Net Loan Amount Range</th>        
<th></th>
</tr>
</thead>
<tbody id="plTableBody">
</table>
</div>			
 
 
<div class="table-responsive" style="display: none;" id="bilExceptDoctorSection">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header">
<th colspan="4" class="paisaBazaarData-table">

           BIL Except Doctor Loans
</th>
</tr>    
<tr class="red-header">
<th colspan="3" class="childPaisaBazaarHeader">DSA (NET) Biz Bucket</th>        
<th colspan="1" class="childPaisaBazaarHeader">Slab with GST</th>
</tr> 
<tr class="red-header">
<!--   <th></th> -->        
<th colspan="3" class="childPaisaBazaarHeader">Net Loan Amount Range</th>        
<th></th>
</tr>
</thead>
<tbody id="bilTableBody">
</table>
</div>			
</div>
 
 
<div class="table-responsive" style="display: none;" id="paisaBazar">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header">
<th colspan="4" class="paisaBazaarData-table">

          Paisa Bazaar (Personal Loan)
</th>
</tr>    
<tr class="red-header">
<th colspan="3" class="childPaisaBazaarHeader">Net Loan Amount Range</th>        
<th colspan="1" class="childPaisaBazaarHeader">Slab (Incl. GST)</th>
</tr> 
</thead>
<tbody id="paisBazarBody">
</table>
</div>		

<div class="table-responsive" style="display: none;" id="aggregatorId">
<table class="table table-bordered text-center payout-table" style="width: 50%; margin:auto">
<thead>
<tr class="red-header">
<th colspan="2" class="paisaBazaarData-table">

          Payout structure applicable
</th>
</tr>    
<tr class="red-header">
<th colspan="1" class="childPaisaBazaarHeader">Net Loan Amtg(in ` Cr)</th>        
<th colspan="1" class="childPaisaBazaarHeader">PO(incl GST)</th>
</tr> 
</thead>
<tbody id="payoutSTRBody">
</table>
</div>	
<!-- education lone -->	
<div class="table-responsive"  style="display: none;" id="elPrime">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header" >
<th colspan="4" >

           EL Prime
</th>
</tr>    
<tr class="red-header">
<th colspan="3" >Disbursement Amount (EL)</th>        
<th colspan="1">% Payout (With GST)</th>
</tr> 
</thead>
<tbody id="elPrimeTableBody">
</table>
</div>			
 
 
<div class="table-responsive"  style="display: none;" id="eLGeneric">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header">
<th colspan="4" >

       	EL Generic 	
</th>
</tr>    
<tr class="red-header">
<th colspan="3" >Disbursement Amount (EL)</th>        
<th colspan="1">% Payout (With GST)</th>
</tr> 
</thead>
<tbody id="eLGenericTableBody">
</table>
</div>			
 
 
<div class="table-responsive"  style="display: none;" id="elFocus">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header">
<th colspan="4" >

           EL Focus
</th>
</tr>    
<tr class="red-header">
<th colspan="3" >Disbursement Amount (EL)</th>        
<th colspan="1">% Payout (With GST)</th>
</tr> 
</thead>
<tbody id="elFocusTableBody">
</table>
</div>			
 
<div class="table-responsive"  style="display: none;" id="elSupreme">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header">
<th colspan="4" >

          EL Supreme
</th>
</tr>    
<tr class="red-header">
<th colspan="3" >Disbursement Amount (EL)</th>        
<th colspan="1">% Payout (With GST)</th>
</tr> 
</thead>
<tbody id="elSupremeTableBody">
</table>
</div>			
 
 
<div class="table-responsive" style="display: none;" id="elFlat">
<table class="table table-bordered text-center payout-table" style="width: 50%; margin:auto">
<thead>
<tr class="red-header">
<th colspan="2" class="paisaBazaarData-table">

           EL Flat
</th>
</tr>    
<tr class="red-header">
<th colspan="1" class="childPaisaBazaarHeader">Disbursement Amount (EL)</th>        
<th colspan="1" class="childPaisaBazaarHeader">% Payout (With GST)</th>
</tr> 
</thead>
<tbody id="elFlatTableBody">
</table>
</div>	
 
 
<div class="table-responsive"  style="display: none;" id="elBooster">
<table class="table table-bordered text-center payout-table">
<thead>
<tr class="red-header">
<th colspan="4" >

           EL Booster
</th>
</tr>    
<tr class="red-header">
<th colspan="3" >Disbursement Amount (EL)</th>        
<th colspan="1">% Payout (With GST)</th>
</tr> 
</thead>
<tbody id="elBoosterTableBody">
</table>
</div>	
<div style="display: none;" id="submitBtn">
<div class="mt-3 d-flex justify-content-end">
<button id="btnSubmit" class="btn red-btn me-2" onclick="savePersonalAndEucationLoan()">

					Submit For Approval</button>
<button id="btnHistory" class="btn btn-primary btn-sm" style="margin-left: 15px;"

					onclick="">View History</button>
</div>	
 
</div>	
 
		
		<!--  Vehicle loan TW structure -->
		<div id="vehicleTWStructure" style="display: none;">

			<!-- FILTER -->
			<!--  <div class="row mb-3"> -->
			<div class="filter-row">

			
				<div class="form-group">
					<!-- nitin -->
					<!-- <label>From Date</label> --> 
					<label id="lblFromDate">From Date</label>
					<input type="date" id="cyclefromDate"
						class="form-control">
				</div>

				<div class="form-group">
					 <label id="lblToDate">To Date</label>
					<input type="date" id="cycletoDate" class="form-control">
				</div>

				<!--         <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button id="btnFetch" class="btn red-btn w-100" onclick="fetchDataTWStructure()">Fetch</button>
				</div>

				<!--  <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button  id="btnReset" class="btn btn-secondary w-100" onclick="resetTable()">Reset</button>
				</div>

			</div>

		
			<!-- TITLE -->

			<div class="title-bar mb-2">
				<span id="headerText"></span>
			</div>

			<div class="action-panel">

				<select id="newRowSelect" class="form-control">
					<option value="">Select Row</option>
				</select>

				<button id="btnAddRow" class="btn btn-success" type="button" onclick="addRow()">
					Add Row</button>

				<select id="newColSelect" class="form-control">
					<option value="">Select Column</option>
				</select>

				<button  id="btnAddColumn" class="btn btn-success" type="button" onclick="addColumn()">
					Add Column</button>

			</div>

		


			<!-- TABLE -->
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

			<!-- BUTTON -->
			<!-- <div class="mt-3 text-end"> -->

			<div class="mt-3 d-flex justify-content-end">
				<button id="btnSubmit" class="btn red-btn me-2" onclick="saveUsedAutoDMAStructure()">
					Submit For Approval</button>
				<button id="btnHistory" class="btn btn-primary btn-sm" style="margin-left: 15px;"
					onclick="viewMakerHistory()">View History</button>
			</div>
		</div>
		
		
		
		<!--  Vehicle loan CV structure -->
		<div id="vehicleCVStructure" style="display: none;">

			<!-- FILTER -->
			<!--  <div class="row mb-3"> -->
			<div class="filter-row">

			
				<div class="form-group">
					<!-- nitin -->
					<!-- <label>From Date</label> --> 
					<label id="lblFromDate">From Date</label>
					<input type="date" id="cyclefromDateCV"
						class="form-control">
				</div>

				<div class="form-group">
					 <label id="lblToDate">To Date</label>
					<input type="date" id="cycletoDateCV" class="form-control">
				</div>

				<!--         <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button id="btnFetch" class="btn red-btn w-100" onclick="fetchDataCVStructure()">Fetch</button>
				</div>

				<!--  <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button  id="btnReset" class="btn btn-secondary w-100" onclick="resetTable()">Reset</button>
				</div>

			</div>

		
			<!-- TITLE -->

			<div class="title-bar mb-2">
				<span id="headerText"></span>
			</div>

			<div class="action-panel">

				<select id="newRowSelect" class="form-control">
					<option value="">Select Row</option>
				</select>

				<button id="btnAddRow" class="btn btn-success" type="button" onclick="addRow()">
					Add Row</button>

				<select id="newColSelect" class="form-control">
					<option value="">Select Column</option>
				</select>

				<button  id="btnAddColumn" class="btn btn-success" type="button" onclick="addColumn()">
					Add Column</button>

			</div>

		


			<!-- TABLE -->
			<div class="table-responsive" id="gridSectionForCV" style ="display:none";>
				<!-- <table class="table table-bordered text-center"> -->
				<table class="table-bordered text-center payout-table" id="UsedAutoDMATable">
				<thead>
					<tr class="red-header">
						<th>Sr. No</th>
						<th>After April 21 Retention Rate</th>
						<th>Before April 21 Retention Rate</th>
						


					</tr>
				</thead>
				<tbody id="CvVehicleBody">
				<tr>
				<td class ="row-label">1</td>
				<td><input type="text" class="pct-input" value="8.60"></td>
				<td><input type="text" class="pct-input" value="10.40"></td>
				</tr>
				</tbody>

			</table>
			</div>

			<!-- BUTTON -->
			<!-- <div class="mt-3 text-end"> -->

			<div class="mt-3 d-flex justify-content-end">
				<button id="btnSubmit" class="btn red-btn me-2" onclick="saveCvVehicleStructure()">
					Submit For Approval</button>
				<button id="btnHistory" class="btn btn-primary btn-sm" style="margin-left: 15px;"
					onclick="viewMakerHistory()">View History</button>
			</div>
		</div>
		
		
		
		
		
		<!--I process counsellor for kerala-->
		<div id="iProcessKeralaSlab" style="display: none;">

			<div class="filter-row">

			<div class="form-group">
					<label>STATE</label> <select class="form-control" id="stateIPK" >
						<option value="">Select</option>
						<option  value="kerala">Kerala</option>
						<option  value="commonStructure">Common Structure</option>
						<option  value="otherRegion">Other Region</option>
						<option  value="PANIndia">PAN India</option>
						<option  value="retainers">retainers</option>
					
						
					</select>
				</div>
				<div class="form-group">
					<!-- nitin -->
					<!-- <label>From Date</label> --> 
					<label id="lblFromDate">From Date</label>
					<input type="date" id="cyclefromDateIPK"
						class="form-control">
				</div>

				<div class="form-group">
					 <label id="lblToDate">To Date</label>
					<input type="date" id="cycletoDateIPK" class="form-control">
				</div>

				<!--         <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button id="btnFetch" class="btn red-btn w-100" onclick="fetchDataIPKStructure()">Fetch</button>
				</div>

				<!--  <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button  id="btnReset" class="btn btn-secondary w-100" onclick="resetTable()">Reset</button>
				</div>

			</div>

		
			<!-- TITLE -->

			<div class="title-bar mb-2">
				<span id="headerText"></span>
			</div>

			<div class="action-panel">

				<select id="newRowSelect" class="form-control">
					<option value="">Select Row</option>
				</select>

				<button id="btnAddRow" class="btn btn-success" type="button" onclick="addRow()">
					Add Row</button>

				<select id="newColSelect" class="form-control">
					<option value="">Select Column</option>
				</select>

				<button  id="btnAddColumn" class="btn btn-success" type="button" onclick="addColumn()">
					Add Column</button>

			</div>
		
			<div id="counsellorAllTables" style="display: none;">
				<div class="table-panel">
					<table class="table table-bordered text-center payout-table"
						id="InboundTable">
						<thead>
							<tr class="red-header">
								<th>Type</th>
								<th>Slab From</th>
								<th>Slab To</th>
								<th>Incentive %</th>
								<th>Remarks</th>
							</tr>
						</thead>

						<tbody id="InboundBody">
						</tbody>
					</table>
				</div>


				<div class="table-panel">
					<table class="table table-bordered text-center payout-table"
						id="OutboundTable">
						<thead>
							<tr class="red-header">
								<th>Type</th>
								<th>Slab From</th>
								<th>Slab To</th>
								<th>Incentive %</th>
								<th>Remarks</th>
							</tr>
						</thead>

						<tbody id="OutboundBody">


						</tbody>
					</table>
				</div>



				<div class="table-panel">
					<table class="table table-bordered text-center payout-table"
						id="UsedCarsTable">
						<thead>
							<tr class="red-header">
								<th>Type</th>
								<th>Slab From</th>
								<th>Slab To</th>
								<th>Incentive %</th>
								<th>Remarks</th>
							</tr>
						</thead>

						<tbody id="UsedCarsBody">



						</tbody>
					</table>
				</div>
			</div>
			
			
			<div id="commonStructureIPCAllTables" style="display: none;">
			
			
			
			<!-- Table 1: Sourcing Type / Parameters / Incentive % / Remarks / Designation -->
				
					<div class="table-panel">
						<h5>Used Car Slab Structure</h5>
						<table class="table table-bordered text-center payout-table"
							id="SourcingTable">
							<thead>
								<tr class="red-header">
									<th>Sourcing Type</th>
									<th>Slab From</th>
									<th>Slab To</th>
									<th>Incentive %</th>
									<th>Remarks</th>
									<th>Designation</th>
								</tr>
							</thead>
							<tbody id="SourcingIPCBody">
							</tbody>
						</table>
					</div>
				
				
				
				<!-- Table 2: Broker ID / Broker Name / New Cars / Used Cars / Attachment Incentive -->
				<div class="table-panel">
					<h5>Broker Incentive Structure</h5>
					<table class="table table-bordered text-center payout-table"
						id="BrokerTable">
						<thead>
							<tr class="red-header">
								<th>Broker ID</th>
								<th>Broker Name</th>
								<th>New Cars</th>
								<th>Used Cars (only non Internal Topup cases)</th>
								<th>Attachment Incentive</th>
							</tr>
						</thead>
						<tbody id="BrokerIPCBody">
						</tbody>
					</table>
				</div>

				<!-- Table 3: APS Code / Channel Name (Nil Retainer list) -->
				<div class="table-panel">
					<h5>Nil Retainer Incentive - APS Code List</h5>
					<table class="table table-bordered text-center payout-table"
						id="ApsCodeTable">
						<thead>
							<tr class="red-header">
								<th>APS Code</th>
								<th>Channel Name</th>
							</tr>
						</thead>
						<tbody id="ApsCodeIPCBody">
						</tbody>
					</table>
				</div>
				</div>
				
				
				
				
				<div id=commonStructureIPPCTables style="display: none;">
			
		
				<!-- Table 1: APS Code / Channel Name (Pune) -->
				<div class="table-panel">
					<h5>Pune - APS Code List</h5>
					<table class="table table-bordered text-center payout-table"
						id="ApsCodePNTable">
						<thead>
							<tr class="red-header">
								<th>BROKER ID</th>
								<th>Channel Name</th>
							</tr>
						</thead>
						<tbody id="ApsCodeIPPNBody">
						</tbody>
					</table>
				</div>
				<!-- Table 2: APS Code / Channel Name (Kolkata) -->
				<div class="table-panel">
					<h5>Kolkata - APS Code List</h5>
					<table class="table table-bordered text-center payout-table"
						id="ApsCodePCKoTable">
						<thead>
							<tr class="red-header">
								<th>BROKER ID</th>
								<th>Channel Name</th>
							</tr>
						</thead>
						<tbody id="ApsCodeIPKOBody">
						</tbody>
					</table>
				</div>
				
				</div>
				
				
				
				
	<!-- 			I process PAN india structure -->
	
	
	<div id="IPCPANIndiaAllTables" style="display: none;">





				<div class="table-panel">
					<h5>Used Car Slab Structure</h5>
					<table id="incentiveTable">
						<thead>
							<tr>
								<!-- <th rowspan="2" style="width: 3;">Sequence No</th> -->
								<th rowspan="2" style="width: 3;">Sr No</th>
								<th rowspan="2" style="width: 10;">Type</th>
								<th rowspan="2" style="width: 8;">State</th>
								<th rowspan="2" style="width: 15;">From Slab</th>
								<th rowspan="2" style="width: 7;">To Slab</th>
								<th rowspan="2" style="width: 15;">Percentage %</th>
								<th rowspan="2" style="width: 7;">Fixed Amount</th>
								<th rowspan="2" style="width: 8;">Max Incentive</th>
								<th rowspan="2" style="width: 7;">Capping</th>
								<th rowspan="2" style="width: 8;">Max Salary Cap</th>
								<th rowspan="2" style="width: 12;">Remark</th>
							</tr>

						</thead>
						<tbody>
							<!-- Block 1: Outbound Segment -->
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="1"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Outbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="2"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Outbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="3"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Outbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="4"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Outbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="5"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Outbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>

							<!-- INBOUND BLOCK -->

							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="1"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Inbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="2"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Inbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="3"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Inbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="4"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Inbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="5"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Inbound"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>

							<!-- Used cars BLOCK -->
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="1"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Used Cars"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="2"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Used Cars"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="3"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Used Cars"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="4"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Used Cars"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>
							<tr class="data-row">
								<td><input type="number" class="table-input srNo" value="5"
									readonly></td>
								<td><input type="text" class="table-input catType"
									value="Used Cars"></td>
								<td><input type="text" class="table-input state"
									value="PAN INDIA"></td>
								<td><input type="number" class="table-input fromSlab"
									value="" min="0"></td>
								<td><input type="number" class="table-input toSlab"
									value="" min=""></td>
								<td><input type="number" class="table-input percentage"
									value="" min="0"></td>
								<td><input type="number" class="table-input fixAmt"
									value=""></td>
								<td><input type="number" class="table-input maxIncentive"
									value="2000"></td>
								<td><input type="number" class="table-input cap" value=""
									min="0"></td>
								<td><input type="number" class="table-input maxSalaryCap"
									value="75000" min="0"></td>
								<td><input type="text" class="table-input remark"
									value="On net loan amount"></td>
							</tr>

						</tbody>

					</table>
				</div>






			</div>
	
	
	
	
	<div id="manipalContent" style="display: none;">
 <div class="table-responsive">
<div id="table-container" class="table table-bordered text-center payout-table" style="display: none;" ></div>
</div>

 
</div>
	
	
	
	

			<!-- BUTTON -->
			<!-- <div class="mt-3 text-end"> -->

			<div class="mt-3 d-flex justify-content-end">
				<button id="btnSubmit" class="btn red-btn me-2" onclick="saveIPKStructure()">
					Submit For Approval</button>
				<button id="btnHistory" class="btn btn-primary btn-sm" style="margin-left: 15px;"
					onclick="viewMakerHistory()">View History</button>
			</div>
			
			</div>
			
			
			<!--other than manipal for kerala-->
		<div id="manipalKeralaSlab" style="display: none;">

			<div class="filter-row">

			<div class="form-group">
					<label>STATE</label> <select class="form-control" id="stateMPK" >
						<option value="">Select</option>
						<option  value="kerala">Kerala</option>
							<option  value="commonStructure">Common Structure</option>
								<option  value="otherRegion">Other Region</option>
								<option  value="PANIndia">Pan India</option>
						
					</select>
				</div>
				<div class="form-group">
					<!-- nitin -->
					<!-- <label>From Date</label> --> 
					<label id="lblFromDate">From Date</label>
					<input type="date" id="cyclefromDateMPK"
						class="form-control">
				</div>

				<div class="form-group">
					 <label id="lblToDate">To Date</label>
					<input type="date" id="cycletoDateMPK" class="form-control">
				</div>

				<!--         <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button id="btnFetch" class="btn red-btn w-100" onclick="fetchDataMPKStructure()">Fetch</button>
				</div>

				<!--  <div class="form-group d-flex align-items-end"> -->
				<div class="form-group">
					<button  id="btnReset" class="btn btn-secondary w-100" onclick="resetTable()">Reset</button>
				</div>

			</div>

		
			<!-- TITLE -->

			<div class="title-bar mb-2">
				<span id="headerText"></span>
			</div>

			<div class="action-panel">

				<select id="newRowSelect" class="form-control">
					<option value="">Select Row</option>
				</select>

				<button id="btnAddRow" class="btn btn-success" type="button" onclick="addRow()">
					Add Row</button>

				<select id="newColSelect" class="form-control">
					<option value="">Select Column</option>
				</select>

				<button  id="btnAddColumn" class="btn btn-success" type="button" onclick="addColumn()">
					Add Column</button>

			</div>
		
			<div id="manipalAllTables" style="display: none;">
				<div class="table-panel">
					<table class="table table-bordered text-center payout-table"
						id="InboundMPTable">
						<thead>
							<tr class="red-header">
								<th>Type</th>
								<th>Slab From</th>
								<th>Slab To</th>
								<th>Incentive %</th>
								<th>Remarks</th>
							</tr>
						</thead>

						<tbody id="InboundMPBody">
						</tbody>
					</table>
				</div>


				<div class="table-panel">
					<table class="table table-bordered text-center payout-table"
						id="OutboundMPTable">
						<thead>
							<tr class="red-header">
								<th>Type</th>
								<th>Slab From</th>
								<th>Slab To</th>
								<th>Incentive %</th>
								<th>Remarks</th>
							</tr>
						</thead>

						<tbody id="OutboundMPBody">


						</tbody>
					</table>
				</div>



				<div class="table-panel">
					<table class="table table-bordered text-center payout-table"
						id="UsedCarsMPTable">
						<thead>
							<tr class="red-header">
								<th>Type</th>
								<th>Slab From</th>
								<th>Slab To</th>
								<th>Incentive %</th>
								<th>Remarks</th>
							</tr>
						</thead>

						<tbody id="UsedCarsMPBody">



						</tbody>
					</table>
				</div>
			
			</div>
			
			
<!-- 			tables for common structure  -->

			<div id="commonStructureAllTables" style="display: none;">
			
			
			
			<!-- Table 1: Sourcing Type / Parameters / Incentive % / Remarks / Designation -->
				
					<div class="table-panel">
						<h5>Used Car Slab Structure</h5>
						<table class="table table-bordered text-center payout-table"
							id="SourcingTable">
							<thead>
								<tr class="red-header">
									<th>Sourcing Type</th>
									<th>Slab From</th>
									<th>Slab To</th>
									<th>Incentive %</th>
									<th>Remarks</th>
									<th>Designation</th>
								</tr>
							</thead>
							<tbody id="SourcingBody">
							</tbody>
						</table>
					</div>
				
				
				
				<!-- Table 2: Broker ID / Broker Name / New Cars / Used Cars / Attachment Incentive -->
				<div class="table-panel">
					<h5>Broker Incentive Structure</h5>
					<table class="table table-bordered text-center payout-table"
						id="BrokerTable">
						<thead>
							<tr class="red-header">
								<th>Broker ID</th>
								<th>Broker Name</th>
								<th>New Cars</th>
								<th>Used Cars (only non Internal Topup cases)</th>
								<th>Attachment Incentive</th>
							</tr>
						</thead>
						<tbody id="BrokerBody">
						</tbody>
					</table>
				</div>

				<!-- Table 3: APS Code / Channel Name (Nil Retainer list) -->
				<div class="table-panel">
					<h5>Nil Retainer Incentive - APS Code List</h5>
					<table class="table table-bordered text-center payout-table"
						id="ApsCodeTable">
						<thead>
							<tr class="red-header">
								<th>APS Code</th>
								<th>Channel Name</th>
							</tr>
						</thead>
						<tbody id="ApsCodeBody">
						</tbody>
					</table>
				</div>
				</div>

			<div id=commonStructureMPPCTables style="display: none;">
			
		
				<!-- Table 1: APS Code / Channel Name (Pune) -->
				<div class="table-panel">
					<h5>Pune - APS Code List</h5>
					<table class="table table-bordered text-center payout-table"
						id="ApsCodeMPNTable">
						<thead>
							<tr class="red-header">
								<th>BROKER ID</th>
								<th>Channel Name</th>
							</tr>
						</thead>
						<tbody id="ApsCodeMPPNBody">
						</tbody>
					</table>
				</div>
				<!-- Table 2: APS Code / Channel Name (Kolkata) -->
				<div class="table-panel">
					<h5>Kolkata - APS Code List</h5>
					<table class="table table-bordered text-center payout-table"
						id="ApsCodeMPCKoTable">
						<thead>
							<tr class="red-header">
								<th>BROKER ID</th>
								<th>Channel Name</th>
							</tr>
						</thead>
						<tbody id="ApsCodeMPKOBody">
						</tbody>
					</table>
				</div>
				
				</div>

			<table id="manipalTable" style="display: none;">
				<thead>
					<tr>
						<th rowspan="2" style="width: 3;">Sr No</th>
						<th rowspan="2" style="width: 10;">Type</th>
						<th rowspan="2" style="width: 8;">State</th>
						<th rowspan="2" style="width: 15;">From Slab</th>
						<th rowspan="2" style="width: 7;">To Slab</th>
						<th rowspan="2" style="width: 15;">Percentage %</th>
						<th rowspan="2" style="width: 7;">Fixed Amount</th>
						<th rowspan="2" style="width: 8;">Max Incentive</th>
						<th rowspan="2" style="width: 7;">Capping</th>
						<th rowspan="2" style="width: 8;">Max Salary Cap</th>
						<th rowspan="2" style="width: 12;">Remark</th>
					</tr>

				</thead>
				<tbody>
					<!-- Block 1: Outbound Segment -->
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="1"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Outbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="2"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Outbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="3"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Outbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="4"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Outbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="5"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Outbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="1"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Inbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="2"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Inbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="3"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Inbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="4"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Inbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="5"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Inbound"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="1"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Used Cars"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="2"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Used Cars"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="3"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Used Cars"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="4"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Used Cars"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>
					<tr class="data-row">
						<td><input type="number" class="table-input srNo" value="5"
							readonly></td>
						<td><input type="text" class="table-input catType"
							value="Used Cars"></td>
						<td><input type="text" class="table-input state"
							value="PAN INDIA"></td>
						<td><input type="number" class="table-input fromSlab"
							value="" min="0"></td>
						<td><input type="number" class="table-input toSlab" value=""
							min=""></td>
						<td><input type="number" class="table-input percentage"
							value="" min="0"></td>
						<td><input type="number" class="table-input fixAmt" value=""></td>
						<td><input type="number" class="table-input maxIncentive"
							value="2000"></td>
						<td><input type="number" class="table-input cap" value=""
							min="0"></td>
						<td><input type="number" class="table-input maxSalaryCap"
							value="75000" min="0"></td>
						<td><input type="text" class="table-input remark"
							value="On net loan amount"></td>
					</tr>




				</tbody>

			</table>
			<!-- BUTTON -->
			<!-- <div class="mt-3 text-end"> -->

			<div class="mt-3 d-flex justify-content-end">
				<button id="btnSubmit" class="btn red-btn me-2" onclick="saveMPKStructure()">
					Submit For Approval</button>
				<button id="btnHistory" class="btn btn-primary btn-sm" style="margin-left: 15px;"
					onclick="viewMakerHistory()">View History</button>
			</div>
			
			</div>




			
		</div>
	
	<script>


	
function viewMakerHistory(){

    let category =
    document.getElementById("category").value;

    let city =
    document.getElementById("city").value;

    if(category === "NTC"
    || category === "TCC"
    || category === "CCA"
    || category.includes("360")){

        city = "NA";
    }

    /* let url =
    "/payout/latestApprovedId"
    + "?category=" + encodeURIComponent(category)
    + "&city=" + encodeURIComponent(city)
    + "&dpd=" + encodeURIComponent(dpd); */

   /*  let url =
    	CONTEXT_PATH +
    	"/payout/latestApprovedId"
    	+ "?category=" + encodeURIComponent(category)
    	+ "&city=" + encodeURIComponent(city)
    	+ "&dpd=" + encodeURIComponent(dpd); */
    	
    	let url =
    		"${pageContext.request.contextPath}/payout/latestApprovedId"
    		+ "?category=" + encodeURIComponent(category)
    		+ "&city=" + encodeURIComponent(city)
    		+ "&dpd=" + encodeURIComponent(dpd);
    
    console.log("URL =", url);

    fetch(url)

    .then(r => r.text())

    .then(id => {

        console.log("FETCHED ID =", id);

        if(!id){

            alert("No Approved Record Found");

            return;
        }

        /* window.location.href =
        "/payoutCompare?id="
        + id +
        "&mode=maker"; */
        
       /*  window.location.href =
        	CONTEXT_PATH +
        	"/mainPage/load?master=PAYOUTCOMPAREMAKER"
        	+ "&id=" + id
        	+ "&mode="+ userId; */
        	
        window.location.href =
        	"${pageContext.request.contextPath}/mainPage/load?master=PAYOUTCOMPAREMAKER"
        	+ "&id=" + id
        	+ "&mode=maker";
    });
}

</script>

</body>
</html>















