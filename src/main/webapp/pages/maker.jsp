<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<style>

/* ================= PAGINATION STYLE ================= */
/*  #paginationWrapper {
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
@media ( max-width : 768px) {
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

<style>

/* Light overlay */
#loaderOverlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(255, 255, 255, 0.6); /* light, not black */
	z-index: 9999;
	display: flex;
	align-items: center;
	justify-content: center;
	pointer-events: all;
}

/* Spinner */
.loader {
	width: 50px;
	height: 50px;
	border: 6px solid #e0e0e0;
	border-top: 6px solid #1976d2; /* blue */
	border-radius: 50%;
	animation: spin 1s linear infinite;
}

/* Animation */
@
keyframes spin { 0% {
	transform: rotate(0deg);
}
100


















%
{
transform


















:


















rotate
















(


















360deg


















)
















;
}
}
</style>
<div ng-show="showMaker" style="max-height: 100%;">
	<h4 class="pageHeading">Master Maker</h4>
	<!-- <label class="lable-dma"style="font-size: 16px; padding: 0px; color: #800000; font-weight: bold;">Maker</label> -->
	<!-- <a href="/DMAPayoutWeb/DMA_Templets.zip">Click here for Template
		files</a> -->

	<form name="MakerForm" class="ng-valid ng-dirty ng-valid-parse">
		<div class="row">

			<!-- Product Payout -->
			<div class="col-md-3 col-sm-3">

				<label class="col-form-label">Product <i class="red-color">*</i></label>
				<!--snz-->
				<select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					id="product" onchange="loadSubProduct()">

					<option value="" selected="selected">Select Product</option>

					<!-- <option value="Home loan" class="ng-binding ng-scope">Home
						loan</option>

					<option value="Vehicle loan" class="ng-binding ng-scope">Vehicle
						loan</option>

					<option value="Personal loan, Education loan,others"
						class="ng-binding ng-scope">Personal loan, Education
						loan,others</option>

					<option value="Cards Other" class="ng-binding ng-scope">Cards
						Other</option>

					<option value="Consumer finance" class="ng-binding ng-scope">Consumer
						finance</option>
					end ngRepeat: x in productList | unique:'product_group' 
					<option value="Credit Card" class="ng-binding ng-scope">Credit
						Card</option>

					<option value="RIBG" class="ng-binding ng-scope">RIBG</option>

					<option value="LAS" class="ng-binding ng-scope">LAS</option>

					<option value="Collection - Agency" class="ng-binding ng-scope">Collection-
						Agency</option>

					<option value="Collection -  HL, OSP, insta HL"
						class="ng-binding ng-scope">Collection - HL, OSP, insta
						HL</option>

					<option value="Collection- Osp" class="ng-binding ng-scope">Collection-
						Osp</option>

					<option value="Collection Non- Caps Agency"
						class="ng-binding ng-scope">Collection Non- Caps Agency</option>

					<option value="Collection Non- Caps OSP"
						class="ng-binding ng-scope">Collection Non- Caps OSP</option>

					<option value="Collection Call centre OSP"
						class="ng-binding ng-scope">Collection Call centre OSP</option>

					<option value="Collection Call centre" class="ng-binding ng-scope">Collection
						Call centre</option> -->

				</select>
			</div>

			<!-- Sub Product -->
			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Sub Product <i
					class="red-color">*</i></label> <select class="form-control"
					id="subProduct" onchange="loadMaster()">

					<option value="" selected="selected">Select Sub Product</option>
				</select>
			</div>


			<!-- Payout Master -->
			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Select Master<i
					class="red-color">*</i></label> <select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					ng-model="product_id" validation="Product:dummy:Y:1:100:Y"
					my-blur="" onchange="onMasterChange(this)" id="master">

					<option value="" selected="selected">Select Master</option>

				</select>
			</div>

			<!-- Payout Status -->
			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Select Status<i
					class="red-color">*</i></label> <select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					onchange="onStatusChange(this)" id="status">
					<option value="" selected="selected">Select Status</option>
					<!-- <option value="All" class="ng-binding ng-scope">All</option>
					<option value="P" class="ng-binding ng-scope">Pending</option>
					<option value="A" class="ng-binding ng-scope">Approved</option> -->
					<!-- <option value="R" class="ng-binding ng-scope">Reject</option> -->
				</select>
			</div>
		</div>

		<div class="row mt-4">

			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Upload File From Local <i
					class="red-color">*</i></label> <input type="radio"
					style="display: inline; width: 20px; height: 22px;"
					class="form-control ng-valid ng-not-empty ng-dirty ng-touched ng-valid-parse"
					name="upload_flag" ng-model="upload_flag" value="LOCAL"
					id="uploadLocal" validation="Upload From:dummy:Y:1:100:Y"
					my-blur="" onclick="toggleRadio(this)" disabled>
			</div>
			<!-- <a href="/DMAPayoutWeb/DMA_Templets.zip">Click here for Template
		files</a> -->

			<!-- added for template download(ban502236)-->
			<a href="javascript:void(0);" onclick="downloadTemplate()">Click
				here for Template files</a>
		</div>

		<span class="red-color CustomValidationError validation"
			style="display: inline;"></span>

		<div ng-show="upload_flag=='LOCAL'" class="row" id="input_files"
			style="display: none;">
			<div class="col-md-10 col-sm-10">
				<label class="col-form-label">Input File(s) <i
					class="red-color">*</i></label>
				<dir style="display: flex;">
					<div class="file-upload">
						<div class="file-select">
							<div class="file-select-button" id="fileNameInput">Browse</div>
							<input type="file" class="fileselected" name="chooseFileInput"
								id="chooseFileInput" accept=".xlsx" my-blur="" my-keyup=""
								validation="File:alphanumeric:N:::N"
								onchange="validateExcelFile(this)">
						</div>
					</div>
					<iframe id="downloadFrame" style="display: none;"></iframe>

					<a href="#" onclick="downloadErrorFile(event)"
						style="color: red; font-weight: bold; float: right; margin-left: 6.5%;">
						Click here for Error file </a>
				</dir>

				<div class="file-select-name mt-2" style="color: #FF0000;"
					id="noFileInput">No file chosen...</div>

			</div>

		</div>

		<div class="form-group mt-4">
			<button type="button" class="btn btn-dma col-md-2 ms-3"
				ng-click="initiateMaker()" onclick="uploadFile()">Upload
				Input Files</button>

			<button type="reset" class="btn btn-dma col-md-2 ms-3"
				style="margin-left: 5%;" onclick="resetForm()">Reset</button>

			<button id="addNewRow" type="button"
				class="btn btn-dma col-md-2 ms-3"
				style="display: none; margin-left: 5%;" onclick="addRow()">Add</button>
			<iframe id="downloadFrame" style="display: none;"></iframe>

			<div id="loaderOverlay" style="display: none;">
				<div class="loader"></div>
			</div>

			<!-- <button style="margin-left: 5%;"
				ng-show="showCheckerResultsMaker &amp;&amp; ( dmapayoutstatus.current_status==null || dmapayoutstatus.current_status=='SENT_BACK_TO_MAKER' ) "
				type="button" class="btn btn-dma col-md-2 ng-hide"
				ng-click="submittochecker()">Submit to Checker</button> -->
		</div>

		<!-- 			<div class="form-group mt-4">
        		<button type="button" class="btn btn-dma col-md-2"  ng-click="getMetaDataInMaker()">View Maker Info</button>
      		</div>   -->
	</form>
	<!-- <script>
		$('#chooseFileCaseCancel').bind('change', function() {
			var filename = $("#chooseFileCaseCancel").val();
			if (/^\s*$/.test(filename)) {
				$(".file-upload").removeClass('active');
				$("#noFile").text("No file chosen...");
			} else {
				$(".file-upload").addClass('active');
				$("#noFile").text(filename.replace("C:\\fakepath\\", ""));
			}
		});
	</script>
			-->

	<!-- Table Title -->
	<div id="tableTitle"
		style="display: none; font-size: 18px; font-weight: 600; margin: 15px 0; color: #800000;">
	</div>
	<div style="overflow: scroll;">

		<table id="masterTable"
			class="table master-table table-bordered table-striped"
			style="display: none; height: fit-content; width: -webkit-fill-available;">

			<thead id="thead"></thead>
			<tbody id="tbody"></tbody>
		</table>
		<!-- ================= PAGINATION UI START ================= -->
		<div id="paginationWrapper" style="display: none;">
			<div id="paginationInfo"></div>
			<div id="paginationContainer"></div>
		</div>
		<!-- ================= PAGINATION UI END ================= -->
	</div>




	<script>
	
	
	/*function loadMaster(){
	    const product = document.getElementById("product").value;
	    const master = document.getElementById("master");
	    master.innerHTML = '<option value="" selected="selected">Select Master</option>'; // reset
	    
	    const status = document.getElementById("status");
	    status.innerHTML = '<option value="" selected="selected">Select Status</option>';
	 
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
	} */
	
	//snz
	/* function loadMaster() {

	    currentSubProduct = document.getElementById("subProduct").value;
	    const master = document.getElementById("master");

	    master.innerHTML =
	        '<option value="">Select Master</option>';

	    if (currentProduct === "Collection - Agency" &&
	        currentSubProduct === "Recovery Credit Card") {

	        master.innerHTML +=
	            '<option value="SAP Master">SAP Master</option>';
				
			master.innerHTML +=
		        '<option value="Recovery City Master">Recovery City Master</option>';
				
			master.innerHTML +=
				'<option value="Payment Code Master">Payment Code Master</option>';

			
			master.innerHTML +=
				'<option value="Hold Code Master">Hold Code Master</option>';
			
			    }

	    else if (currentProduct === "Vehicle loan" &&
	             currentSubProduct === "Vehicle") {

	        fetch('/DMAPayoutWeb3/getProductMaster?product=Vehicle loan')
	            .then(res => res.json())
	            .then(data => {
	                data.forEach(function(m){
	                    var opt = document.createElement("option");
	                    opt.value = m;
	                    opt.text = m;
	                    master.appendChild(opt);
	                });
	            });
	    }
	} */
	
	
	
	/* window.onload = function () {
		 s
	    var fileInput = document.getElementById("chooseFileInput");
	    var fileNameDiv = document.getElementById("noFileInput");
	 
	    if (fileInput) {
	        fileInput.addEventListener("change", function () {
	 
	            if (this.files && this.files.length > 0) {
	                fileNameDiv.innerHTML = this.files[0].name;
	            } else {
	                fileNameDiv.innerHTML = "No file chosen...";
	            }
	 
	        });
	    }
	}; */
	
	function uploadFile() {
		
		var fileInput = document.getElementById("chooseFileInput");
		
		if(!fileInput.files || fileInput.files.length === 0){
			alert("Please select a file before uploading.");
			return;
		}
		
	    var product = document.getElementById("product").value;
	    var masterType = document.getElementById("master").value;
	    var formData = new FormData();
	    formData.append("file", fileInput.files[0]);
	    formData.append("product", product);
	    formData.append("master", masterType);	    
	    
	    showLoader();
	    
		
		//snz
		let uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadFile';

		if (masterType === "SAP Master") {
			uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadSAPMaster';
		}	
		else if (masterType === "Recovery City Master") {
		    uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadRecoveryCityExcel';
		}
		else if (masterType === "Payment Code Master") {
		    uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadPaymentCodeExcel';
		}		
		else if (masterType === "Hold Code Master") {
		    uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadHoldCodeExcel';
		}
		
		//spj
		else if (masterType === "Flow City Master") {
		    uploadUrl = '${pageContext.request.contextPath}/flowCityMaster/upload';
		}
		
		//added for CV & TW Coorgination--start
		else if (masterType === "Vendor Master") {
		    uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadVendorMaster';
		}
		else if (masterType === "TDS Master") {
		    uploadUrl = '${pageContext.request.contextPath}/DMAPayoutWeb3/uploadTdsCodeRate';
		}
		//end
		
	   //fetch('${pageContext.request.contextPath}/DMAPayoutWeb3/uploadFile' ,{
		//snz
		fetch(uploadUrl, {
	        method: "POST",
	        headers:{
	        	"user":getLoggedUser()
	        },
	        body: formData
	    })
	    .then(response =>{
	    	
	    	if(!response.ok){
	    		return response.text().then(err=>{throw new Error(err)});
	    	}
	    	
	    	return response.json();
		})
		.then(data => {

		    console.log("Upload Response :", data);
			
			// Save uploadId for error file download
			    if(masterType === "SAP Master"){
			        sessionStorage.setItem("sapUploadId", data.uploadId);
			    }
			    else if(masterType === "Recovery City Master"){
			        sessionStorage.setItem("recoveryUploadId", data.uploadId);
			    }
			    else if(masterType === "Payment Code Master"){
			        sessionStorage.setItem("paymentUploadId", data.uploadId);
			    }
			    else if(masterType === "Hold Code Master"){
			        sessionStorage.setItem("holdUploadId", data.uploadId);
			    }
			
				//spj
			    else if(masterType === "Flow City Master"){
			        sessionStorage.setItem("uploadId", data.uploadId);
			    }
			  //added for CV & TW Coorgination--start
			    else if(masterType === "Vendor Master"){
			        sessionStorage.setItem("vendorUploadId", data.uploadId);
			    }
			    else if(masterType === "TDS Master"){
			        sessionStorage.setItem("tdsUploadId", data.uploadId);
			    }
			//end
			
			var msg = "";

			if ((data.failedRecords || 0) > 0) {

			    msg =
			        "Upload Failed\n\n" +
			        "Total Records : " + (data.totalRecords || 0) + "\n" +
			        "Failed Records : " + (data.failedRecords || 0) + "\n\n" +
			        "No records were uploaded.\n" +
			        "Please download the Error File.";

			} else {

			    msg =
			        "Upload Completed Successfully\n\n" +
			        "Total Records : " + (data.totalRecords || 0) + "\n" +
			        "Successful Records : " + (data.successfulRecords || 0) + "\n"+
			    	"Unsuccessful Records : " + (data.failedRecords || data.ErrorCount  || 0);
			}

		    alert(msg);

		    hideLoader();
			
			loadTable();

		    var fileInput = document.getElementById("chooseFileInput");
		    if (fileInput) {
		        fileInput.value = "";
		    }

		    var fileNameDiv = document.getElementById("noFileInput");
		    if (fileNameDiv) {
		        fileNameDiv.innerHTML = "No file chosen...";
		        fileNameDiv.style.color = "red";
		    }
		})
	.catch(err =>{
		hideLoader();
		alert("upload Failed : " + err.message);
		console.error(err);
	});
	}
	
	/* function downloadErrorFile(e){
	    e.preventDefault();
	 
	    var masterType = document.getElementById("master").value;
	 
	    var url = '${pageContext.request.contextPath}/DMAPayoutWeb3/error/download?master=' + masterType;
	 
	    document.getElementById("downloadFrame").src = url;
	} */
	
	function getTodayDate() {
	    var today = new Date();
	 
	    var yyyy = today.getFullYear();
	    var mm = (today.getMonth() + 1);
	    var dd = today.getDate();
	 
	    // Add leading zero
	    if (mm < 10) mm = "0" + mm;
	    if (dd < 10) dd = "0" + dd;
	 
	    return yyyy + "-" + mm + "-" + dd;
	}
	
	//snz
	function downloadErrorFile(e){

	    e.preventDefault();

	    var masterType = document.getElementById("master").value;

	    // SAP Master special API
	    if(masterType === "SAP Master"){

	        var uploadId = sessionStorage.getItem("sapUploadId");

	        if(!uploadId){
	            alert("No SAP upload found");
	            return;
	        }

	        window.location =
	            "${pageContext.request.contextPath}/DMAPayoutWeb3/downloadSAPErrorExcel?uploadId="
	            + uploadId;

	        return;
	    } else if(masterType === "Recovery City Master"){

		    var uploadId = sessionStorage.getItem("recoveryUploadId");

		    if(!uploadId){
		        alert("No Recovery City upload found");
		        return;
		    }

		    window.location =
		        "${pageContext.request.contextPath}/DMAPayoutWeb3/downloadRecoveryCityErrorExcel?uploadId="
		        + uploadId;

		    return;
		}
		else if(masterType === "Payment Code Master"){

		    var uploadId = sessionStorage.getItem("paymentUploadId");

		    if(!uploadId){
		        alert("No Payment Code upload found");
		        return;
		    }

		    window.location =
		        "${pageContext.request.contextPath}/DMAPayoutWeb3/downloadPaymentCodeErrorExcel?uploadId="
		        + uploadId;

		    return;
		}
		else if(masterType === "Hold Code Master"){

		    var uploadId = sessionStorage.getItem("holdUploadId");

		    if(!uploadId){
		        alert("No Hold Code upload found");
		        return;
		    }

		    window.location =
		        "${pageContext.request.contextPath}/DMAPayoutWeb3/downloadHoldCodeErrorExcel?uploadId="
		        + uploadId;

		    return;
		}
	    
		else if(masterType === "Flow City Master"){

		    var uploadId = sessionStorage.getItem("uploadId");
		    if(!uploadId){
		        alert("No Hold Code upload found");
		        return;
		    }
		    window.location =
		        "${pageContext.request.contextPath}/flowCityMaster/download-error/"
		        + uploadId;
		    return;
		}

		//added for CV & TW Coorgination--start
		else if(masterType === "Vendor Master"){

		    var uploadId = sessionStorage.getItem("vendorUploadId");

		    if(!uploadId){
		        alert("No vendor upload found");
		        return;
		    }

		    window.location =
		        "${pageContext.request.contextPath}/DMAPayoutWeb3/downloadVendorErrorExcel?uploadId="
		        + uploadId;

		    return;
		}
		else if(masterType === "TDS Master"){

		    var uploadId = sessionStorage.getItem("tdsUploadId");

		    if(!uploadId){
		        alert("No tds upload found");
		        return;
		    }

		    window.location =
		        "${pageContext.request.contextPath}/DMAPayoutWeb3/downloadTdsCodeRateErrorExcel?uploadId="
		        + uploadId;

		    return;
		}
		//end

	    // Existing logic for other masters
	    var url =
	        "${pageContext.request.contextPath}/DMAPayoutWeb3/error/download?master="
	        + masterType;

	    fetch(url,{
	    	method: "GET",
	    	headers:{
	    		"user":getLoggedUser()
	    	}
	    })
	    .then(response => {

	        if (!response.ok) {
	            return response.text().then(err => {
	                throw new Error(err || "No record found");
	            });
	        }

	        var disposition = response.headers.get("content-disposition");

	        if(!disposition){
	            throw new Error("No record found");
	        }

	        return response.blob();
	    })
	    .then(blob => {

	        if(blob.size === 0){
	            alert("No record found");
	            return;
	        }

	        const link = document.createElement("a");
	        link.href = window.URL.createObjectURL(blob);
	        link.download = masterType + "_ERROR_" + getTodayDate() + ".xlsx";
	        link.click();

	    })
	    .catch(error => {
	        alert(error.message || "No record found");
	    });
	}
	
	/* function downloadErrorFile(e){
		 
	    e.preventDefault();
	 
	    var masterType = document.getElementById("master").value;
	 
	    var url = "${pageContext.request.contextPath}/DMAPayoutWeb3/error/download?master=" + masterType;
	 
	    fetch(url)
	    .then(response => {
	 
	        if (!response.ok) {
	            return response.text().then(err => {
	                throw new Error(err || "No record found");
	            });
	        }
	 
	        var disposition = response.headers.get("content-disposition");
	 
	        if(!disposition){
	            throw new Error("No record found");
	        }
	 
	        return response.blob();
	    })
	    .then(blob => {
	 
	        if(blob.size === 0){
	            alert("No record found");
	            return;
	        }
	 
	        const link = document.createElement("a");
	        link.href = window.URL.createObjectURL(blob);
	        link.download = masterType + "_ERROR_"+ getTodayDate()+".xlsx";
	        link.click();
	 
	    })
	    .catch(error => {
	 
	        alert(error.message || "No record found");
	 
	    });
	} */
	
	
	
	
	
	function resetForm(){
		
		document.MakerForm.reset();
		
		// reset product dropdown
		document.getElementById("product").selectedIndex=0;
		
		// reset master dropdown
		const master=document.getElementById("master");
		master.innerHTML = '<option value="" selected="selected">Select Master</option>'; // reset dropdown
		
		document.getElementById("status").innerHTML='<option value="" selected="selected">Select Status</option>';
		
		// hide table
		document.getElementById('tableTitle').style.display='none';
        document.querySelectorAll('.master-table')
             .forEach(t => t.style.display='none');
        var addRowButton= document.getElementById("addNewRow");
        if(addRowButton)
             addRowButton.style.display= "none";
        
        // Clear pagination UI
        document.getElementById("paginationContainer").innerHTML = "";
        document.getElementById("paginationInfo").innerHTML = "";
     
        // Hide wrapper
        let wrapper = document.getElementById("paginationWrapper");
        if (wrapper) wrapper.style.display = "none";
        
        resetFileUpload();
        
        currentMaster="";
      
	}
	
	function resetFileUpload(){
		//radio rest 
        var radio = document.getElementById("uploadLocal");
       if(radio){
       	radio.checked = false;
       	 radio.disabled = true; 
       }
       radioState = false;
       
       //file selection hide
       var fileSection = document.getElementById("input_files");
       if(fileSection){
       	fileSection.style.display="none";
       }
       
       
       // clear file input 
       var fileInput = document.getElementById("chooseFileInput");
       if (fileInput) {
           fileInput.value = "";
       }
    
       // Reset file name display
       var fileNameDiv = document.getElementById("noFileInput");
       if (fileNameDiv) {
           fileNameDiv.innerHTML = "No file chosen...";
           fileNameDiv.style.color = "red";
       }
	} 

	function getLoggedUser(){
		 return localStorage.getItem("user_id"); 
		 /* return 'BAN49380';  */
	}
	
	function showLoader() {
	    document.getElementById("loaderOverlay").style.display = "flex";
	 
	    // Disable all buttons & inputs
	    document.querySelectorAll("button, input, select").forEach(el => {
	        el.disabled = true;
	    });
	}
	 
	function hideLoader() {
	    document.getElementById("loaderOverlay").style.display = "none";
	 
	    // Enable again
	    document.querySelectorAll("button, input, select").forEach(el => {
	        el.disabled = false;
	    });
	}
 
</script>
	<script>
    var baseurl = '${pageContext.request.contextPath}';
</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/maker.js"></script>

</div>