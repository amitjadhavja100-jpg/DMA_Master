<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>

<title>Config Master Checker</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/slabPayoutConfig/configMaster.css">

<!-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
 -->
</head>
<script src="${pageContext.request.contextPath}/js/ospConfigChecker.js"></script>
<script>
const CONTEXT_PATH = "${pageContext.request.contextPath}";
const LOGIN_USER = "${sessionScope.USER_ID}";
 
/* const LOGIN_USER = "BAN513834"; */
</script>

<body class="bg-light">

<div class="container-fluid mt-4">

<h2 class="mb-4">Config Master Checker Approval Screen</h2>

		<div class="filter-row">

			<div class="form-group">
				<label>Product</label> <select id="product" class="form-control"
					onchange="loadCheckerSubProducts()">
					<option value="">Select Product</option>
				</select>
			</div>

			<div class="form-group">
				<label>Sub Product</label> <select id="subProduct"
					class="form-control" onchange="openCheckerScreen()">
					<option value="">Select Sub Product</option>
				</select>
			</div>
			<div class="form-group"></div>
			<div class="form-group"></div>

		</div>
		
<div id="agencyCheckerScreen" style="display:none;">
<table class="table table-bordered bg-white">

<thead class="table-dark">

<tr>

<th>Temp Id</th>
<th>Type</th>
<th>Key</th>
<th>Old Value</th>
<th>New Value</th>
<th>Action</th>
<th>Maker</th>
<th>Status</th>
<th>Operations</th>

</tr>

</thead>

<tbody id="tbody"></tbody>

</table>

</div>

		<div id="ospCheckerScreen" style="display: none;">

			<table class="table table-bordered bg-white">
				<thead class="table-dark">
					<tr>
						<th>Temp Id</th>
						<th>Type</th>
						<th>DPD</th>
						<th>Old Value</th>
						<th>New Value</th>
						<th>Action</th>
						<th>Created By</th>
						<th>Status</th>
						<th>Operations</th>
					</tr>
				</thead>

				<tbody id="ospCheckerBody">
				</tbody>
			</table>
		</div>

	</div>

<script>

document.addEventListener("DOMContentLoaded", function () {
    console.log("CHECKER LOADED");
    loadCheckerProducts();
});


function loadCheckerProducts(){

    fetch(CONTEXT_PATH+
        "/DMAPayoutWeb3/products")

    .then(r=>r.json())
    .then(data=>{

        let ddl= document.getElementById("product");

        ddl.innerHTML=
        "<option value=''>Select Product</option>";

        data.forEach(function(p){

            ddl.innerHTML+=
            "<option value='"+p+"'>"
            +p+
            "</option>";

        });
    });
}

function loadCheckerSubProducts(){

    let product= document.getElementById("product").value;
    let ddl= document.getElementById("subProduct");

    ddl.innerHTML= "<option value=''>Select Sub Product</option>";

    if(product==""){
        return;
    }

    fetch( CONTEXT_PATH + "/DMAPayoutWeb3/subProducts?product="+
        encodeURIComponent(product)
    )

    .then(r=>r.json())
    .then(data=>{

        data.forEach(function(s){

            ddl.innerHTML+=
            "<option value='"+s+"'>"
            +s+
            "</option>";
        });
    });
}

function openCheckerScreen(){

    let product= document.getElementById("product").value;
    let subProduct= document.getElementById("subProduct").value;

    document.getElementById( "agencyCheckerScreen").style.display = "none";
    document.getElementById("ospCheckerScreen").style.display="none";

    if(product=="Collection - Agency" && subProduct=="Recovery Credit Card"){

        document.getElementById( "agencyCheckerScreen").style.display="block";
        loadPending();
    }

    else if(product=="Collection- Osp" && subProduct=="Recovery Osp"){

        document.getElementById( "ospCheckerScreen").style.display="block";
        loadOspPending();
    }
}


function loadPending(){

/* fetch("/config/pending?user=checker1") */
fetch("${pageContext.request.contextPath}/config/pending?user="+LOGIN_USER)

.then(r=>r.json())

.then(data=>{

let tbody=document.getElementById("tbody");

tbody.innerHTML="";


if(data.length === 0){

	tbody.innerHTML =
		"<tr>" +
		"<td colspan='7' style='text-align:center;font-weight:bold;padding:25px;color:#198754;'>" +
		"No Payout Records Pending For Approval" +
		"</td>" +
		"</tr>";

    return;
}


data.forEach(function(d){

    let row = "";

    row += "<tr>";

    row += "<td>" + d.tempId + "</td>";

    row += "<td>" + d.configType + "</td>";

    row += "<td>" + d.configKey + "</td>";

    row += "<td>" + d.oldValue + "</td>";

    row += "<td>" + d.newValue + "</td>";

    row += "<td>" + d.actionType + "</td>";

    row += "<td>" + d.makerId + "</td>";
    
    row += "<td>" + d.status + "</td>";
   

    
    row += "<td>";

    row += "<button ";
    row += "class='btn btn-success btn-sm me-1' ";
    row += "onclick='approve(" + d.tempId + ")'>";
    row += "Approve";
    row += "</button>";

    row += "<button ";
    row += "class='btn btn-danger btn-sm me-1' ";
    row += "onclick='rejectRecord(" + d.tempId + ")'>";
    row += "Reject";
    row += "</button>";

    row += "<button ";
    row += "class='btn btn-info btn-sm me-1' ";
    row += "onclick='viewHistory(" + d.tempId + ")'>";
    row += "View";
    row += "</button>";

    row += "</td>";

    row += "</tr>";

    tbody.innerHTML += row;

});



});
}

function approve(tempId){

/* fetch("/config/approve/"+tempId+"?user=checker1",{
 */	
 
fetch("${pageContext.request.contextPath}/config/approve/"+tempId+"?user="+LOGIN_USER,{

method:"POST"

})
.then(()=>{

alert("Approved");

loadPending();

});
}

function rejectRecord(tempId){

let remarks=prompt("Enter remarks");

/* fetch("/config/reject/"+tempId+
"?user=checker1&remarks="+remarks,{ */
	
	fetch("${pageContext.request.contextPath}/config/reject/"+tempId+
"?user="+LOGIN_USER+
"&remarks="+remarks,{

method:"POST"

})
.then(()=>{

alert("Rejected");

loadPending();

});
}


	function viewHistory(tempId){
		console.log("CHECKER ID =", tempId);
		
window.location.href =
"${pageContext.request.contextPath}/mainPage/load?master=CONFIGCOMPARECHECKER"
+ "&tempId=" + tempId
+ "&mode=checker"; 
		
		}
	
</script>

</body>
</html>