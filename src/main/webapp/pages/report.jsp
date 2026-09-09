<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<style>/* Light overlay */
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


360


deg


)
;


}
}
</style>

<div ng-show="showMaker"
	style="overflow-x: visible; overflow-y: scroll; max-height: 100%;">
	<label class="lable-dma"
		style="font-size: 16px; padding: 0px; color: #800000; font-weight: bold;">File
		Upload</label>

	<form name="MakerForm" class="ng-valid ng-dirty ng-valid-parse">
		<div class="row">
			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Product <i class="red-color">*</i></label>
				<select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					id="product" onchange="loadMaster();resetCycleDates()">

					<option value="" selected="selected">Select Product</option>

					<option value="Home loan" class="ng-binding ng-scope">Home
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
					<!-- end ngRepeat: x in productList | unique:'product_group'  -->
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
						Call centre</option>

				</select>
			</div>


			<div class="col-md-3 col-sm-3">
				<label class="col-form-label">Select Dump<i
					class="red-color">*</i></label> <select
					class="form-control ng-pristine ng-untouched ng-valid ng-empty"
					ng-model="product_id" validation="Product:dummy:Y:1:100:Y"
					my-blur="" onchange="ondumpChange(this);resetCycleDates()"
					id="master">

					<option value="" selected="selected">Select Dump</option>

				</select>
			</div>
			<div class="col-md-2 col-sm-2">
				<label class="col-form-label">Cycle From Date <i
					class="red-color">*</i></label> <input type="date" class="form-control"
					ng-model="cycle_from_date" id="cycleFromDate"
					onchange=""
					validation="Cycle From Date:dummy:Y:1:100:Y" my-blur="">
			</div>
			<div class="col-md-2 col-sm-2">
				<label class="col-form-label">Cycle To Date <i
					class="red-color">*</i></label> <input type="date" class="form-control"
					id="cycleToDate" ng-model="cycle_to_date"
					validation="Cycle To Date:dummy:Y:1:100:Y"
					onchange="" my-blur="">
			</div>
		</div>

		<!-- <div class="row mt-4" id="rcaSection" style="display: none;">
			<label class="col-md-3 col-form-label black-color"> RCA Sheet
				Options </label>

			<div class="col-md-9" style="max-width: 100%; flex: none;">
				<label> <input type="checkbox" id="cibilChk" value="CIBIL"
					name="rcaOption" onclick="selectOnlyOne(this)">
					CIBIL&Entity mapping
				</label> <label style="margin-left: 20px;"> <input type="checkbox"
					value="processShop" id="processShopChk" name="rcaOption"
					onclick="selectOnlyOne(this)">Process Shop
				</label> <label style="margin-left: 20px;"> <input type="checkbox"
					value="cbcMaster" id="cbcMasterChk" name="rcaOption"
					onclick="selectOnlyOne(this)"> CBC Master
				</label>
			</div>
		</div> -->

		<div class="row mt-4">

			<div class="col-md-3 col-sm-3">

				<label class="col-form-label">Upload File From Local <i
					class="red-color">*</i></label> <input type="radio"
					style="display: inline; width: 20px; height: 22px;"
					class="form-control ng-valid ng-not-empty ng-dirty ng-touched ng-valid-parse"
					name="upload_flag" ng-model="upload_flag" value="LOCAL"
					id="uploadLocal" validation="Upload From:dummy:Y:1:100:Y"
					my-blur="" onclick="toggleRadio(this)" disabled>
				<!-- onclick="displayInputFiles()"> -->
			</div>
			<!-- added for dump template download (ban502236)-start -->

			<a href="javascript:void(0);" id="templateFiles" onclick="downloadDumpTemplate()">Click
				here for Template files</a>


			<!-- added for template download (ban502236)-end -->

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
							<input type="file" class="fileselected" multiple=""
								name="chooseFileInput" id="chooseFileInput" accept=".xlsx"
								my-blur="" my-keyup="" validation="File:alphanumeric:N:::N"
								onchange="validateExcelFile(this)">
						</div>
					</div>
					<iframe id="downloadFrame" style="display: none;"></iframe>
					<a id="errorFileLink" href="#" onclick="downloadErrorFile(event)"
						style="color: red; font-weight: bold; float: right; margin-left: 6.5%;">
						Click here for Error file </a>
				</dir>
				<div class="file-select-name mt-2" style="color: #FF0000;"
					id="noFileInput">No file chosen...</div>

			</div>


		</div>


		<div class="form-group mt-4">
			<button id="uploadInputBtn" type="button" class="btn btn-dma col-md-2 ms-3"
				ng-click="initiateMaker()" onclick="uploadFile()">Upload
				Input Files</button>

			<button type="reset" class="btn btn-dma col-md-2 ms-3"
				style="margin-left: 5%;" onclick="resetForm()">Reset</button>

			<!--  	<button type="button" class="btn btn-dma col-md-2 ms-3" id="downloadBtn"
				style="margin-left: 5%;display: none;" onclick="downloadFile()">Download</button>-->


			<button type="button" class="btn btn-dma col-md-2 ms-3"
				id="downloadBtn" style="margin-left: 5%; display: none;"
				onclick="downloadFile()">Download</button>

			<!-- <button type="button" class="btn btn-dma col-md-2 ms-3"
				id="downloadBtn" style="margin-left: 5%;"
				onclick="downloadFile()">Download</button> -->

			<iframe id="downloadFrame" style="display: none;"></iframe>

			<div id="loaderOverlay" style="display: none;">
				<div class="loader"></div>
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
	<div>

		<%-- <table id="branchTable"
			class="table master-table table-bordered table-striped"
			style="display: none; height: 600px;">
					</table> --%>


		<table id="masterTable"
			class="table master-table table-bordered table-striped"
			style="display: none; height: 600px;">

			<thead id="thead"></thead>
			<tbody id="tbody"></tbody>
		</table>

	</div>


	<script>
	function clearRcaSection() {
	    document.getElementById("rcaSection").style.display = "none";
	    document.getElementById("cibilChk").checked = false;
	    document.getElementById("processShopChk").checked = false;
	    document.getElementById("cbcMasterChk").checked = false;
	}
	
	function loadMaster(){
	    const product = document.getElementById("product").value;
	    const master = document.getElementById("master");
	    master.innerHTML = '<option value="" selected="selected">Select Dump</option>'; // reset
	    /* clearRcaSection(); */
	 
	    if (!product) return;
	 
	    // Only call API for Vehicle Loan
	    if (product === "Vehicle loan") {
	    	console.log("In loadMaster Method");
	    	
	    	
	        fetch('${pageContext.request.contextPath}/DMAPayoutWeb2/getProductDump')
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
	}
	// You already have something like this
	
	
		
	
	function ondumpChange(selectElement) {
	var dump = document.getElementById("master").value;
	var downloadBtn = document.getElementById("downloadBtn");
    
    
    var radio = document.getElementById("uploadLocal");
    var fileSection = document.getElementById("input_files");
	var fileInput = document.getElementById("chooseFileInput");
	var fileNameDiv = document.getElementById("noFileInput");
	
	var uploadInputBtn = document.getElementById("uploadInputBtn");
    
	var templateFiles = document.getElementById("templateFiles");
	
    if (dump === "Tagging File") {
    	//display Download button
        downloadBtn.style.display = "inline-block";
        
    	// disable radio and uncheck radio
        radio.disabled=true;
		radio.checked=false
		radioState=false;
		
		// disable upload button 
		uploadInputBtn.disabled = true;
        uploadInputBtn.style.pointerEvents = "none";
        uploadInputBtn.style.opacity = "0.6";
        uploadInputBtn.style.cursor = "not-allowed";
     
 		// hide template Link
        templateFiles.style.display = "none";
       
		
		// hide file selection section
		fileSection.style.display = "none";
		
		// clear selected file
	    fileInput.value = "";

	    // Reset file name display
	    fileNameDiv.innerHTML = "No file chosen...";
	    fileNameDiv.style.color = "red";
		
    } else {
        downloadBtn.style.display = "none";
        
        radioState=false;
        radio.disabled=false;
    	//radio.checked=false;
    	
     // clear selected file
        fileInput.value = "";

        // Reset file name display
        fileNameDiv.innerHTML = "No file chosen...";
        fileNameDiv.style.color = "red";
        selectedFile="";
        
        // display upload button
        uploadInputBtn.disabled = false;
        uploadInputBtn.style.pointerEvents = "auto";
        uploadInputBtn.style.opacity = "1";
        uploadInputBtn.style.cursor = "pointer";
        
        //show template link
        templateFiles.style.display = "inline";
        
    }
 
   
	
}
	
	
	function selectOnlyOne(selectedCheckbox) {
	    var checkboxes = document.getElementsByName("rcaOption");
	 
	    for (var i = 0; i < checkboxes.length; i++) {
	        if (checkboxes[i] !== selectedCheckbox) {
	            checkboxes[i].checked = false;
	        }
	    }
	}
	
	
	function displayInputFiles(){
			document.getElementById('input_files').style.display = "block";			
		}
 
	/* function downloadFile(){	
		document.getElementById("downloadFrame").src =

	       "${pageContext.request.contextPath}/DMAPayoutWeb2/downloadFinnone";
	      
	 
	    setTimeout(function () {
	        alert("Finnone dump downloaded successfully");
	    }, 1000);

	} */
	
	function downloadFile() {
		
		var cycleFromDate = document.getElementById("cycleFromDate").value;
	    var cycleToDate = document.getElementById("cycleToDate").value;
	    
	    if (!cycleFromDate || !cycleToDate) {
	        alert("Please select Cycle From Date and Cycle To Date.");
	        return false;
	    }
	 
	    if (new Date(cycleFromDate) > new Date(cycleToDate)) {
	        alert("Cycle From Date cannot be greater than Cycle To Date.");
	        return false;
	    }
	    
		
		var formUpload = new FormData();
		formUpload.append("cycleFromDate",cycleFromDate);
		formUpload.append("cycleToDate",cycleToDate);
		
		showLoader();
		 
	    fetch('${pageContext.request.contextPath}/DMAPayoutWeb2/downloadFinnone', {
	        method: 'POST',
	        body: formUpload
	    })
	    .then(function(response) {
	 
	       // const contentType = response.headers.get("Content-Type");
	 
	        // ❌ If backend returned error text
	        if (!response.ok) {
	            return response.text().then(function(text) {
	                throw new Error(text);
	            });
	            
	        }
	 
	        // ❌ Error returned as text instead of file
	        /* if (contentType &&
	            (contentType.includes("application/json") || contentType.includes("text/plain"))) {
	 
	            return response.text().then(function(text)  {
	                throw new Error(text);
	            });
	        } */
	        console.log("Finnone download success::",response);
	        // ✅ Valid Excel file
	        return response.blob();
	    })
	    .then(function(blob)  {
	    	
	    	var downloadDate = getTodayDate();
	        var fileName = "Tagging_file_" + cycleFromDate + "_to_" + cycleToDate + "_" + downloadDate + ".xlsx";
		    const url = window.URL.createObjectURL(blob);
		    const a = document.createElement("a");
	        a.href = url;
	        a.download = fileName;
	        document.body.appendChild(a);
	        a.click();
	 
	        a.remove();
	        window.URL.revokeObjectURL(url);
	 
	        alert("Tagging Logic file downloaded successfully");
	        hideLoader();
	        
	        var fileNameDiv = document.getElementById("noFileInput");
	        if (fileNameDiv) {
	            fileNameDiv.innerHTML = "No file chosen...";
	            fileNameDiv.style.color = "red";
	        }
	    })
	    .catch(function(error){
	        alert("Download failed : " + error.message);
	        var fileNameDiv = document.getElementById("noFileInput");
	        if (fileNameDiv) {
	            fileNameDiv.innerHTML = "No file chosen...";
	            fileNameDiv.style.color = "red";
	        }
	        
	        console.log("Finnone download error::",error);
	        hideLoader();
	    });
	}
	
	/* function fileValidation(){
		debugger;
		let isFileUploaded = false;
		 if(isFileUploaded){
		    	alert("File already uploaded. Do you want to replace?");
		    	return false;
		    }
	} */
	
	function uploadFile() {
		let isFileUploaded = false;
		
	    var product = document.getElementById("product").value;
	    var dumpType = document.getElementById("master").value;
	    var userId = resolveUserId();
	    
	    //var noFileInput = document.getElementById("noFileInput").value;
	    var fileInput = document.getElementById("chooseFileInput");
	    var file = fileInput.files[0];
	    
	    if(!fileInput||fileInput.files.length === 0){
	    	alert("Please choose a file before uploading");
	    	return false;
	    }
	    
	    var cycleFromDate = document.getElementById("cycleFromDate").value;
	    var cycleToDate = document.getElementById("cycleToDate").value;
	    
	    if (!cycleFromDate || !cycleToDate) {
	        alert("Please select Cycle From Date and Cycle To Date.");
	        return false;
	    }
	 
	    if (new Date(cycleFromDate) > new Date(cycleToDate)) {
	        alert("Cycle From Date cannot be greater than Cycle To Date.");
	        return false;
	    }	    
	  
	    var formData = new FormData();
	   // formData.append("file", document.getElementById("chooseFileInput").files[0]);
	    formData.append("file", fileInput.files[0]);
	    formData.append("product", product);
	    formData.append("dumpType", dumpType);
	    formData.append("cycleFromDate",cycleFromDate);
	    formData.append("cycleToDate",cycleToDate);
	     
	    console.log("Sending:", formData.get("cycleFromDate"), formData.get("cycleToDate"));
	    
	    showLoader();
	 /*    fetch("pageContext.request.contextPath}/DMAPayoutWeb2/uploadFile", */
	   fetch('${pageContext.request.contextPath}/DMAPayoutWeb2/uploadFile' ,{
		   
	        method: "POST",
	        header:{
	        "user":userId	
	        },
	        body: formData
	        
	    })
	   
	   .then(response => response.text())
	   
	    .then(data => {
	    	console.log("uploadFile Response::",data);
	    	console.log("FormData Response::",formData); 
	    	alert(data)    	
	    	console.log("alert(data)");
	    	  hideLoader();
	    	  
	    	var fileNameDiv = document.getElementById("noFileInput");
	        if (fileNameDiv) {
	            fileNameDiv.innerHTML = "No file chosen...";
	            fileNameDiv.style.color = "red";
	        }
	    	/* if(data.includes("Finnone Dump upload Successfully!")&& dumpType === "Finnone Dump"){
	    		document.getElementById("downloadBtn").style.display="inline-block";
	    	} */
	    	
	        /* if(dumpType === "Finnone Dump"){
	    		document.getElementById("downloadBtn").style.display="inline-block";
	    	} */
	    	
	    	if(data.includes("ALDD transaction Report upload Successfully!") || data.includes("Ilens Dump upload Successfully!")
	    			|| data.includes("Rcas file uploaded Successfully!") || data.includes("Finnone dump downloaded successfully") 
	    			|| data.includes("File is empty") || data.includes("Finnone Dump uploaded Successfully!"))
	    	{
	    		window.location.reload();
	    	}
	    	
	    });
	}
	
	/* window.onload = function () {
		 
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
	
	/* window.onload = function () {
		 
	    var fileInput = document.getElementById("chooseFileInput");
	    console.log("File Upload ::: " , fileInput );
	    var fileNameDiv = document.getElementById("noFileInput");
	 
	    if (!fileInput || !fileNameDiv) {
	        console.error("Required elements not found");
	        return;
	    }
	 
	    fileInput.addEventListener("change", function () {
	        if (this.files && this.files.length > 0) {
	            fileNameDiv.innerHTML = this.files[0].name;
	        } else {
	            fileNameDiv.innerHTML = "No file chosen...";
	        }
	    });
	}; */
	
	
	var radioState = false;
	function toggleRadio(radio) {
	    var fileSection = document.getElementById("input_files");
	   		if(radio.disabled) return;
	        if (radioState) {
	            radio.checked = false;
	            fileSection.style.display = "none";
	            radioState = false;
	        } else {
	            radio.checked = true;
	            fileSection.style.display = "block";
	            radioState = true;
	        }
	 
	};
	// Validate only .xlsx file
	let selectedFile = "";
	function validateExcelFile(input) {
	    let fileNameDiv = document.getElementById("noFileInput");
	    if (!input.files || input.files.length === 0) {
	        fileNameDiv.innerHTML = "No file chosen...";
	        return;
	    }
	    let file = input.files[0];
	    let extension = file.name.split('.').pop().toLowerCase();
	    if (extension !== "xlsx") {
	        fileNameDiv.innerHTML = "Only .xlsx files are allowed!";
	        fileNameDiv.style.color = "red";
	        input.value = ""; // Clear file
	        return;
	    }
	    /* if(file !== "No file chosen..."){
	    	alert("File is already available.Do you want to replace it.");
	    	return;
	    } */
	    if (selectedFile !== "") {
	    	 
	        let replace = confirm("File already selected. Do you want to replace it?");
	 
	        if (!replace) {
	            input.value = "";
	            return;
	        }
	    }
	    
	    selectedFile = file.name;
	    fileNameDiv.style.color = "green";
	    fileNameDiv.innerHTML = file.name;    
		 
	}
	
	function resetForm(){
		document.MakerForm.reset();
		document.getElementById("product").selectedIndex=0;
		const master=document.getElementById("master");
		master.innerHTML = '<option value="" selected="selected">Select Dump</option>'; // reset
		document.getElementById('tableTitle').style.display='none';
        document.querySelectorAll('.master-table')
             .forEach(t => t.style.display='none');
        // Clear file input manually (important)
          resetFileUpload();
          document.getElementById("chooseFileInput").value="";
        var fileInput = document.getElementById("chooseFileInput");
        if (fileInput) {
            fileInput.value = "";
        }
        
        var downloadBtn = document.getElementById("downloadBtn");
        console.log("downloadBtn:: " , downloadBtn)
		downloadBtn.style.display = "none";
     
        // Reset file name display
        var fileNameDiv = document.getElementById("noFileInput");
        if (fileNameDiv) {
            fileNameDiv.innerHTML = "No file chosen...";
            fileNameDiv.style.color = "red";
        }
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
           fileNameDiv.innerHTML = "No file choosen...";
           fileNameDiv.style.color = "red";
       }
	} 
	
	function getLoggedUser(){
		 return localStorage.getItem("user_id"); 
		/* return "BAN495699"; */
	}
	
	function resolveUserId() {
	    var userId = getLoggedUser();
	 
	    // JS-level NOT-NULL handling
	    if (userId === null || userId === undefined || userId === "null" || userId === "") {
	        console.warn("userId is null, continuing without user");
	        return "";   // empty string = safe
	    }
	 
	    return userId;
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
	
	function resetCycleDates() {	
	    document.getElementById("cycleFromDate").value = "";
	    document.getElementById("cycleToDate").value = "";
	    //document.getElementById("chooseFileInput").value = "";
	   /*  var fileInput = document.getElementById("chooseFileInput");
	       if (fileInput) {
	           fileInput.value = "";
	       } */
	    /* var fileNameDiv = document.getElementById("noFileInput");
	       if (fileNameDiv) {
	           fileNameDiv.innerHTML = "No file chosen...";
	           fileNameDiv.style.color = "red";
	       } */
	}
	
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
	
	
	//added for dump template download (ban502236)-start
	function downloadDumpTemplate() {
	    var dumpType = document.getElementById("master").value;
	 
	    if (!dumpType || dumpType.trim() === "" || dumpType ==="Tagging File") {
	        alert("Please select dump");
	        return;
	    }
	 
	    window.location.href = "${pageContext.request.contextPath}/mainPage/downloadTemplate?dumpType="
	        + encodeURIComponent(dumpType);
	}

	//added for dump template download (ban502236)-end
	
	
	function downloadErrorFile(e){
	    e.preventDefault();
	    var dumpType = document.getElementById("master").value;
	    console.log("dumpType::: " , dumpType)
	    var url = "${pageContext.request.contextPath}/DMAPayoutWeb3/error/download?master="+""+"&dumpType="+dumpType;
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
	        link.download = dumpType + "_ERROR.xlsx";
	        link.click();
	    })
	    .catch(error => {
	        alert(error.message || "No record found");
	    });
	}
	 
	
	</script>

</div>