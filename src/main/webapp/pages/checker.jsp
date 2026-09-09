<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<style>

/* ================= PAGINATION STYLE ================= */
/* #paginationWrapper {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-top: 15px;
	padding: 10px 0;
}

#paginationContainer button {
	margin: 0 3px;
	padding: 6px 10px;
	border: 1px solid #ccc;
	background: #fff;
	cursor: pointer;
	border-radius: 4px;
	font-size: 13px;
}

#paginationContainer button:hover {
	background: #20446c;
	color: white;
}

#paginationContainer button.active-page {
	background: #20446c;
	color: white;
}

#paginationContainer button:disabled {
	background: #eee;
	cursor: not-allowed;
} */

/* ==============================
   WRAPPER (Match Table Style)
=================================*/
#paginationWrapper {
    /* display: flex !important; */
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 0;
    padding: 15px 18px;
    border: 1px solid #e5e7eb;
    border-top: none;
    background: #f9fafb; /* same as table footer tone */
    border-radius: 0 0 8px 8px;
    font-family: "Segoe UI", sans-serif;
}
 
/* ==============================
   INFO TEXT
=================================*/
#paginationInfo {
    font-size: 13px;
    color: #6b7280;
    font-weight: 500;
}
 
/* ==============================
   BUTTON CONTAINER
=================================*/
#paginationContainer {
    display: flex;
    gap: 6px;
    align-items: center;
}
 
/* ==============================
   PAGINATION BUTTON STYLE
=================================*/
#paginationContainer button {
    min-width: 34px;
    height: 34px;
    padding: 0 10px;
    border-radius: 6px;
    border: 1px solid #d1d5db;
    background: #ffffff;
    color: #374151;
    font-size: 13px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;
}
 
/* Hover */
#paginationContainer button:hover {
    background: #eef2f7;
    border-color: #9ca3af;
}
 
/* Active Page (Match Approve Button Theme) */
#paginationContainer button.active {
    background: #2563eb;
    color: #ffffff;
    border-color: #2563eb;
}
 
/* Disabled */
#paginationContainer button:disabled {
    opacity: 0.45;
    cursor: not-allowed;
}
 
/* Dots */
#paginationContainer span {
    padding: 0 5px;
    color: #9ca3af;
    font-weight: 600;
}
 
/* ==============================
   RESPONSIVE
=================================*/
@media (max-width: 768px) {
    #paginationWrapper {
        flex-direction: column;
        gap: 10px;
        align-items: flex-start;
    }
}

/* ======================================================= */
</style>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<div ng-show="showCheker" style="max-height: 100%;">
	<!-- <label class="lable-dma" style="font-size: 16px; padding: 0px; color: #800000; font-weight: bold;">Checker</label> -->
	<!-- <a href="/DMAPayoutWeb/DMA_Templets.zip">Click here for Template
		files</a> -->
	<h4 class="pageHeading">Master Checker</h4>

	<form name="ChekerForm" class="ng-valid ng-dirty ng-valid-parse">
		<div class="row">

			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Product <i class="red-color">*</i></label>
				
				<!--snz-->
				<select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					id="product" onchange="loadSubProduct()">

					<option value="" selected="selected">Select Product</option>

				</select>
			</div>
			
			
			<!--snz-->
			<div class="col-md-3 col-sm-3">
			    <label class="col-form-label">
			        Sub Product <i class="red-color">*</i>
			    </label>

			    <select class="form-control"
			            id="subProduct"
			            onchange="loadMaster()">

			        <option value="">Select Sub Product</option>
			    </select>
			</div>

			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Select Master<i
					class="red-color">*</i></label> <select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					ng-model="product_id" validation="Product:dummy:Y:1:100:Y"
					my-blur="" onchange="onMasterChange(this)" id="master">

					<option value="" selected="selected">Select Master</option>

				</select>
			</div>
		</div>


		<span class="red-color CustomValidationError validation"
			style="display: inline;"></span>



		<div class="form-group mt-4">
			<button type="reset" class="btn btn-dma col-md-2 ms-3"
				onclick="resetForm()">Reset</button>
		</div>


	</form>

	<!-- Table Title -->
	<div id="tableTitle"
		style="display: none; font-size: 18px; font-weight: 600; margin: 15px 0; color: #800000;">
	</div>

	<div id="multipleApproveReject" style="display: none;">
		<button ng-click="approveinputDataMultiple('APPROVED')"
			onclick="approveMultiple(this)" class="btn btn-success">Approve</button>
		<button ng-click="approveinputDataMultiple('REJECTED')"
			onclick="rejectMultiple(this)" class="btn btn-danger">Reject</button>
	</div>
	<div style="overflow: scroll;">
		<table id="masterTable"
			class="table master-table table-bordered table-striped"
			style="display: none; height: fit-content; width: -webkit-fill-available;">

			<thead id="thead"></thead>
			<tbody id="tbody"></tbody>
		</table>

		<!-- ================= PAGINATION UI START ================= -->
		<div id="paginationWrapper">
			<div id="paginationInfo"></div>
			<div id="paginationContainer"></div>
		</div>
		<!-- ================= PAGINATION UI END ================= -->

	</div>

	<script>
	
	function getLoggedUser(){
		  return localStorage.getItem("user_id");  
		 /* return 'BAN495699';  */
	}
	
	//snz
		//snz
	/*
	function loadMaster(){
	    const product = document.getElementById("product").value;
	    const master = document.getElementById("master");
	    master.innerHTML = '<option value="" selected="selected">Select Master</option>'; // reset
	 
	    if (!product) return;
	 
	    // Only call API for Vehicle Loan
	    if (product === "Vehicle loan") {
	    	console.log("In loadMaster Method");
	    	
	        fetch('${pageContext.request.contextPath}/DMAPayoutWeb3/getProductMaster')
	            .then(res => res.json())
	            .then(data => {
	                data.forEach(function(m){
	                	var opt = document.createElement("option");
	                	opt.value=m;
	                	opt.text=m;
	                	master.appendChild(opt);
	                	
	                });
	            })
	            .catch(err => console.error(err));
	    }
	}*/
	
	
	function resetForm(){
		document.ChekerForm.reset();
		document.getElementById("product").selectedIndex=0;
		const master=document.getElementById("master");
		master.innerHTML = '<option value="" selected="selected">Select Master</option>'; // reset
		document.getElementById('tableTitle').style.display='none';
        document.querySelectorAll('.master-table')
             .forEach(t => t.style.display='none');
        document.getElementById('multipleApproveReject').style.display='none';
        
        // Clear pagination UI
        document.getElementById("paginationContainer").innerHTML = "";
        document.getElementById("paginationInfo").innerHTML = "";
     
        // Hide wrapper
        let wrapper = document.getElementById("paginationWrapper");
        if (wrapper) wrapper.style.display = "none";
	}
	
	
	
	
	
	 
	let tableNames = ["branchTable","channelTable","GstTable","ModelTable","VendorTable","TdsTable"]
	function displayNone(tableName) {
		/* console.log("hit",tableName); */
		tableNames.forEach(function(table) {
			if(table==tableName){
				document.getElementById(tableName).style.display = "block";
			}else{
				document.getElementById(table).style.display = "none";
			}
		});
	};
	
	function displayInputFiles(){
		document.getElementById('input_files').style.display = "block";
	}
	

    function getMetaDataInMaker() {
        var product = document.getElementById("product").value;
        var master  = document.getElementById("master").value;
		
 		 console.log('hit',product,master);
 
        // Hide all tables
/*         document.querySelectorAll(".master-table").forEach(function (el) {
            el.style.display = "none";
        });  */
 
        if (product === "" || master === "") {
            alert("Please select both Product and Master");
            return;
        }
 
        // Condition mapping
        if (product === "Vehicle loan" && master === "Branch Master") {
            /* document.getElementById("branchTable").style.display = "block";
            document.getElementById("GstTable").style.display = "none";
            document.getElementById("channelTable").style.display = "none"; */
            displayNone("branchTable");
            showTitle("Branch Master");
            
            
        }
        else if (product === "Vehicle loan" && master === "Channel Master") {
           /*  document.getElementById("channelTable").style.display = "block";
            document.getElementById("branchTable").style.display = "none";
            document.getElementById("GstTable").style.display = "none"; */
        	displayNone("channelTable");
        	showTitle("Channel Master");
        }
        else if (product === "Vehicle loan" && master === "Gst Master") {
            /* document.getElementById("GstTable").style.display = "block";
            document.getElementById("channelTable").style.display = "none";
            document.getElementById("branchTable").style.display = "none"; */
        	displayNone("GstTable");
        	showTitle("GST Master");
        }
        else if (product === "Vehicle loan" && master === "Model Master") {
        	displayNone("ModelTable");
        	showTitle("Model Master");
        }
        //added for CV & TW Coorgination--start
        else if (product === "Vehicle loan" && master === "Vendor Master") {
        	displayNone("VendorTable");
        	showTitle("Vendor Master");
        }
        else if (product === "Vehicle loan" && master === "TDS Master") {
        	displayNone("TdsTable");
        	showTitle("TDS Master");
        }
        //end
        else {
            alert("No data available for selected combination");
        }
        
        /* function showTitle(title){
        	var titleDiv=
        		document.getElementById("tableTitle");
        	if(titleDiv)
        	titleDiv.innerText=title;
        	titleDiv.style.display="block"
        } */
    }
      
    
</script>
	<script>
    var baseurl = '${pageContext.request.contextPath}';
</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/checker.js"></script>
	<%-- 	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.dataTables.min.js"></script> --%>
	<%-- 	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/dataTables.bootstrap.min.js"></script> --%>
	<%-- <script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery-3.5.1.min.js"></script> --%>
	<%-- 	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/libs/bootstrap.min.js"></script> --%>
	<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->
	<!-- <script
		src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script> -->

</div>