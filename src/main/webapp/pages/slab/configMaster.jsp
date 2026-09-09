<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>CONFIG MASTER MAKER</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/slabPayoutConfig/configMaster.css">

<script>
const CONTEXT_PATH =
"${pageContext.request.contextPath}";
 /* const LOGIN_USER = "${sessionScope.USER_ID}"; */ 
 const LOGIN_USER = "BAN49380";
</script>
<script src="${pageContext.request.contextPath}/js/ospConfigMaker.js"></script>
<script src="${pageContext.request.contextPath}/js/configMaster.js"></script>

</head>

<body>

<input type="hidden"
id="selectedConfigId">

<!-- <div class="main-container"> -->
<div class="main-container" id="configForm">

<!-- HEADER -->

<div class="top-header">

    <h3>CONFIG MASTER MAKER</h3>

</div>
		<div class="filter-row">

			<div class="form-group">
				<label>Product</label> 
				<select id="product" class="form-control" onchange="loadSubProducts()">
					<option value="">Select Product</option>
				</select>
			</div>
			<div class="form-group">
				<label>Sub Product</label> 
				<select id="subProduct" class="form-control" onchange="openSelectedConfigScreen()">
					<option value="">Select Sub Product</option>
				</select>
			</div>

			<div class="form-group" id="ospTypeDiv" style="display: none;">
				<label>Select Type</label> 
				<select id="ospType" class="form-control" onchange="changeOspType()">
					<option value="">Select Type</option>
					<option value="DPD">DPD</option>
					<option value="COLLECTION_RANGE">Collection Range</option>
				</select>

			</div>
			<div class="form-group"></div>
		</div>
		
		<!-- FILTER SECTION -->
<div id="agencyConfigScreen" style="display:none;">
<div class="filter-row">

   <div class="form-group">
        <label>Config Type</label>
    <select id="configType"
        class="form-control"
        onchange="showHideFields()">
    <option value="">Select Type</option>
    <option value="CATEGORY">CATEGORY</option>
    <option value="CITY">CITY</option>
    <option value="DPD">DPD</option>
    <option value="COLLECTION">COLLECTION</option>
    <option value="CE_RANGE">CE RANGE</option>
    <option value="FIELD">FIELD</option>
    <option value="TITLE">TITLE</option>
    <option value="BUTTON">BUTTON</option>
    <option value="CAPPING">CAPPING</option>
    </select>
    </div>
    
   
   
<div class="form-group" id="categoryDiv"  style="display:none;">
    <label id="categoryLabel">Category</label>
    <select id="category" 
            class="form-control">
        <option value="">Select Category</option>
    </select>
    <input type="text"
           id="categoryText"
           class="form-control mt-2"
           placeholder="Enter Category"
           style="display:none;">
</div>

<div class="form-group" id="cityDiv"  style="display:none;">
    <label id="cityLabel">City</label>
    <select id="city"
            class="form-control">
        <option value="">Select City</option>
    </select>
    <input type="text"
           id="cityText"
           class="form-control mt-2"
           placeholder="Enter City"
           style="display:none;">
</div>

<div class="form-group" id="dpdDiv"  style="display:none;">
    <label id="dpdLabel">DPD</label>
    <select id="dpd"
            class="form-control">
        <option value="">Select DPD</option>
    </select>
    <input type="text"
           id="dpdText"
           class="form-control mt-2"
           placeholder="Enter DPD"
           style="display:none;">
</div>
    
    <div class="form-group" id="parentKeyDiv"  style="display:none;">
    <label>Parent Key</label>
    <select id="parentKey"
            class="form-control"
            disabled>
        <option value="">Select Parent</option>
        <option value="CATEGORY">CATEGORY</option>
        <option value="CITY">CITY</option>
        <option value="DPD">DPD</option>
        <option value="COLLECTION">COLLECTION</option>
        <option value="CE_RANGE">CE RANGE</option>
        <option value="FIELD">FIELD</option>
        <option value="BUTTON">BUTTON</option>
        <option value="TITLE">TITLE</option>
    </select>
</div>

   <!--  <div class="form-group">
        <label>Config Key</label>
        <input type="text" id="configKey" readonly>       
    </div> -->
    
    <div class="form-group"
       id="configKeyDiv"  style="display:none;">
    <label id="configKeyLabel">
          Config Key
          </label>
        <input
         type="text"
           id="configKey"
          class="form-control">
         </div>

   <!--  <div class="form-group">
        <label>Config Value</label>
        <input type="text"
               id="configValue">
    </div> -->
    
<!-- <div class="form-group"
id="configValueDiv"  style="display:none;">
<label id="configValueLabel">
Config Value
</label>
<input
type="text"
id="configValue"
class="form-control"
placeholder="Enter Value">
</div> -->

<div class="form-group" id="oldValueDiv" style="display:none;">
    <label>Old Value</label>
    <input type="text"
           id="oldValue"
           class="form-control"
           readonly>
</div>

<div class="form-group"  id="currentValueDiv" style="display:none;">
    <label>Current Value</label>
    <input type="text"
           id="currentValue"
           class="form-control"
           readonly>
</div>

<div class="form-group" id="configValueDiv" style="display:none;">
    <label id="configValueLabel">New Value</label>
    <input
        type="text"
        id="configValue"
        class="form-control">
</div>


<!--     <div class="form-group">
        <label>Display Order</label>
        <input
type="number"
id="displayOrder"
class="form-control"
min="1">
    </div> -->
    
    <div class="form-group" id="displayOrderDiv">
    <label>Display Order</label>
    <input
        type="number"
        id="displayOrder"
        class="form-control"
        min="1">
    </div>
    
    <div class="form-group">
        <button
        type="button"
        class="btn btn-secondary"
        onclick="resetTable()">
            Reset
        </button>
    </div>
    
</div>

<!-- SEARCH SECTION -->
<div class="filter-row">
    <div class="form-group">
        <label>Status</label>
        <select id="statusFilter">
            <option value="ALL">
                ALL
            </option>

            <option value="ACTIVE">
                ACTIVE
            </option>

            <option value="INACTIVE">
                INACTIVE
            </option>

        </select>
        
    </div>

    <div class="form-group">

        <button
        type="button"
        class="btn red-btn"
        onclick="loadConfigs()">

            Search

        </button>
        
    <button
    type="button"
    class="btn red-btn"
    onclick="saveData()">

        Submit For Approval

    </button>

    </div>

</div>

<!-- CONFIG TABLE -->

<div class="table-responsive">

    <table
    id="configTable"
    class="table table-bordered text-center payout-table">

        <thead>

            <tr class="red-header">

                <th>ID</th>

                <th>TYPE</th>

                <th>PARENT KEY</th>

                <th>KEY</th>

                <th>VALUE</th>

                <th>ORDER</th>

                <th>STATUS</th>

                <th>ACTION</th>

            </tr>

        </thead>

         <tbody id="tbody">
        </tbody> 
        <!-- <tbody id="configGridBody">
        </tbody> -->

    </table>

</div>

<!-- FOOTER BUTTONS -->

<!-- <div class="mt-3 d-flex justify-content-end gap-2">

    <button
    type="button"
    class="btn red-btn"
    onclick="saveData()">

        Submit For Approval

    </button>

    <button
    type="button"
    class="btn btn-primary"
    onclick="viewMakerHistory()">

        View History

    </button>

</div> -->
</div>
		<div id="ospConfigScreen" style="display: none;">

			<!-- DPD Section -->
			<div id="ospDpdSection">
				<div class="filter-row">
					<div class="form-group">
						<label>DPD Name</label> <input type="text" id="ospDpdName"
							class="form-control" placeholder="Enter DPD">
					</div>

					<div class="form-group">
						<button type="button" class="btn red-btn" id="saveDpdBtn"
							onclick="saveOspDpd()">Save DPD</button>
						<button type="button" class="btn btn-info" id="resetDpdBtn"
							onclick="cancelOspDpdEdit()">Reset</button>
					</div>
					<div class="form-group"></div>
					<div class="form-group"></div>
				</div>

				<table class="table table-bordered payout-table">
					<thead>
						<tr>
							<th>ID</th>
							<th>DPD</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody id="ospDpdBody">
					</tbody>
				</table>
			</div>

			<!-- Collection Range Section -->
			<div id="ospRangeSection">

				<div class="filter-row">

					<div class="form-group">
						<label>DPD</label> <select id="ospDpd" class="form-control"
							onchange="loadOspRanges()">
							<option value="">Select DPD</option>
						</select>
					</div>

					<div class="form-group">
						<label>From Amount</label> <input type="number" id="ospFromAmount"
							class="form-control">
					</div>

					<div class="form-group">
						<label>To Amount</label> <input type="number" id="ospToAmount"
							class="form-control">
					</div>
					
					<div class="form-group">
						<label>Order</label> <input type="number" id="ospOrderNo"
							class="form-control">
					</div>
					<div class="form-group">
						<label> <input type="checkbox" id="ospIsMax"
							onchange="toggleMaxRange()"> MAX Range
						</label>
					</div>

					<div class="form-group">
						<button type="button" class="btn red-btn" id="saveRangeBtn"
							onclick="saveOspRange()">Save Range</button>
						<button type="button" class="btn btn-info" id="resetRangeBtn"
							onclick="cancelOspRangeEdit()">Reset</button>
					</div>
				</div>

				<!-- Table -->

				<div class="table-responsive">

					<table class="table table-bordered payout-table">
						<thead>
							<tr class="red-header">
								<th>From Amount</th>
								<th>To Amount</th>
								<th>Order</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody id="ospRangeBody">
						</tbody>
					</table>
				</div>
			</div>

		</div>


	</div>

</body>

</html>
