/*checker.js*/
 /* ================= API PROVIDER ================= */
function getLoadApi() {
	console.log("get load API method call");
    if (currentMaster === "GST Master") return "/DMAPayoutWeb3/allGstMasChecker";
    if (currentMaster === "Branch Master") return "/DMAPayoutWeb3/allBranchMasCheker";
    if (currentMaster === "Channel Master") return "/DMAPayoutWeb3/allChannelMasChecker";
    if (currentMaster === "Model Master") return "/DMAPayoutWeb3/allModelMasChecker";
	
	if (currentMaster === "Outsource Master") return "/DMAPayoutWeb3/allOutsourceMasChecker";
    if (currentMaster === "GST To Master") return "/DMAPayoutWeb3/allGstToMasChecker";
    if (currentMaster === "GST Mf Master") return "/DMAPayoutWeb3/allGstMfMasChecker";
    if (currentMaster === "Ilens Channel Master") return "/DMAPayoutWeb3/allIlensChannelMasChecker";
	if (currentMaster === "GST State Master") return "/DMAPayoutWeb3/allGstStateMasChecker";
	
	//snz
	if(currentMaster === "SAP Master") return "/DMAPayoutWeb3/getAllSAPMasterByChecker";
	if (currentMaster === "Recovery City Master") return "/DMAPayoutWeb3/getRecoveryCityChecker";
	if (currentMaster === "Hold Code Master") return "/DMAPayoutWeb3/getAllHoldCodeChecker";
	if (currentMaster === "Payment Code Master") return "/DMAPayoutWeb3/getAllPaymentCodeChecker";
	
	// spj
	if (currentMaster === "Flow City Master") return "/flowCityMaster/getAllFlowCityMasterByChecker";
	
	// added for CV & TW Coorgination--start
    if (currentMaster === "Vendor Master")
        return "/DMAPayoutWeb3/getAllVendorMasterByChecker";
    if (currentMaster === "TDS Master")
        return "/DMAPayoutWeb3/getAllTdsCodeRateByChecker";
    //end
	
}
 
function getSaveApi() {
	
	console.log(currentMaster);
	console.log(" save API method call");
    if (currentMaster === "GST Master") return "/DMAPayoutWeb3/updateGstMasChecker";
    if (currentMaster === "Branch Master") return "/DMAPayoutWeb3/updateBranchMasChecker";
    if (currentMaster === "Channel Master") return "/DMAPayoutWeb3/updateChannelMasChecker";
    if (currentMaster === "Model Master") return "/DMAPayoutWeb3/updateModelMasChecker";
	
	
	if (currentMaster === "Outsource Master") return "/DMAPayoutWeb3/updateOutsourceMasChecker";
    if (currentMaster === "GST To Master") return "/DMAPayoutWeb3/updateGstToMasChecker";
    if (currentMaster === "GST Mf Master") return "/DMAPayoutWeb3/updateGstMfMasChecker";
    if (currentMaster === "Ilens Channel Master") return "/DMAPayoutWeb3/updateIlensChannelMasChecker";
	if (currentMaster === "GST State Master") return "/DMAPayoutWeb3/updateGstStateMasChecker";
	
	
	//snz
	if(currentMaster === "SAP Master") return "/DMAPayoutWeb3/updateSAPMasterByChecker";
	if(currentMaster === "Recovery City Master") return "/DMAPayoutWeb3/updateRecoveryCityChecker";
	if(currentMaster === "Hold Code Master") return "/DMAPayoutWeb3/updateHoldCodeChecker";
	if(currentMaster === "Payment Code Master") return "/DMAPayoutWeb3/updatePaymentCodeChecker";
	
	// spj
	if (currentMaster === "Flow City Master") return "/flowCityMaster/updateFlowCityMasterByChecker";
	
	// added for CV & TW Coorgination--start
	if (currentMaster === "Vendor Master")
	    return "/DMAPayoutWeb3/updateVendorMasterByChecker";
	if (currentMaster === "TDS Master")
	    return "/DMAPayoutWeb3/updateTdsCodeRateByChecker";
    //end
}
 
 
 /* ================= GET PRIMARY KEY FIELD BY MASTER ================= */
// Primary key of payload : apsCode = 2635463 ==> apsCode  
function getPrimaryKeyByMaster(){
	
	console.log("get column method call");
    if (currentMaster === "GST Master")
        return "apsCode";
 
    if (currentMaster === "Branch Master")
        return "branchCode";
    if (currentMaster === "Channel Master")
        return "apsCode";
    if (currentMaster === "Model Master")
        return "tempId";

	//amit
   if (currentMaster === "Outsource Master")
        return "empCode";
	if (currentMaster === "GST To Master")
        return "processShop"
	if (currentMaster === "Ilens Channel Master")
        return "userID";
	if (currentMaster === "GST State Master")
        return "partnerId";
	if (currentMaster === "GST Mf Master")
        return "apsCode";


	//snz
	if(currentMaster === "SAP Master")
	    return "unitCode";

	if (currentMaster === "Recovery City Master")
	    return "city";

	if(currentMaster === "Hold Code Master")
	    return "code";

	if(currentMaster === "Payment Code Master")
	    return "code";
	
	if(currentMaster === "Flow City Master")
	    return "cityCode";
	    
	//added for CV & TW Coorgination--start
	 if(currentMaster=="Vendor Master")
	        return "vendorNo";
	  
	    if(currentMaster=="TDS Master"){
	        return "id";
	    }
	  //end
}
 /* ================= HEADERS ================= */
 
function getHeaders() {
	console.log("get header method call");
    if (currentMaster === "GST Master")
        return ["APScode", "Name", "State", "Location", "Status"];
 
    if (currentMaster === "Branch Master")
        return ["Branch Code", "Branch Name", "Hub", "AL_State", "Zone","RBH", "ED State", "ED Zone", "ZH_Name", "State Head", "MIS State","RC State", "Location", "Status"];


    if (currentMaster === "Channel Master")
        return ["Aps Code","IBoxID","SUPPLIER ID","DATES","Suspended","Name of channel as Per Agmt","TYPE OF DSA","PAN NO","YY","SUPPLIER ID 1","SUPPLIER ID 2","SUPPLIER ID 3","SUPPLIER ID 4","SUPPLIER ID 5","SUPPLIER ID 6","SUPPLIER ID 7","SUPPLIER ID 8","SUPPLIER ID 9","SUPPLIER ID 10","SUPPLIER ID 11","SUPPLIER ID 12","Remark","Location","MIS State","C State","ED State","ED Zone","Sourcing","Sourcing BK","Manufactue Name","New Manufactue Name","Old I Box ID","RC Limit","SAP Code","Account No","IFSC Code","Bank Name","I Bank  Yes Non I bank No","Remark bk","Status"];
 
    if (currentMaster === "Model Master")
        return ["MANUFACTURER_ID", "MANUFACTURER_DESC", "Model_ID","Model_DESC","ASSET_CATEGORY","BAND","Status"];

	//amit
    if (currentMaster === "Outsource Master")
        return ["EmpCode", "VSTS Code / Counselor SAP Code", "Executive Name","Status"];

	if (currentMaster === "GST To Master")
        return ["Process Shop", "GST State To","Status"];

	if (currentMaster === "Ilens Channel Master")
        return ["S.No.", "User ID", "User Name", "ID Creation Date", "Status", "Email ID", "Mobile Number", "Agency ID", 
					"Agency Type", "VPTS ID", "VPTS Approval Status", "Annual Review Due Date", "MSME Registered", 
					"Udyog Aadhar Number", "Agency Pan Number", "User Group", "Channel Name", "Product Type", 
					"Base CPC / Process Shop", "Mapped Employee ID", "Mapped Employee Name", "Inbound/OutBound Type", 
					"Mapped Sol IDs", "Counsellor IDs", "TSM ID", "TSM Name", "Child Allowed", "Operating Locations", 
					"Role Code", "VSTS ID", "VSTS Name", "VSTS Status", "Product"," Status of Record"];

	if (currentMaster === "GST State Master")
        return ["PARTNER_ID", "PARTNER NAME", "GST State","Status"];

	if (currentMaster === "GST Mf Master")
        return ["APScode", "Name", "State","Status"];



	//snz
	if(currentMaster === "SAP Master")
		    return ["SR No", "Unit Code", "Clean Unit ID", "Vendor Name", "CCA Call Center", "PAN", "SAP Vendor Code", "State", "TDS Rate", "Tax Code",
		        "Finali Boxids", "Status Of Blocking","Account Status", "Cred Info No", "GSTN No", "SAC Code","Service Provider ID Status","GST Applicable",
		        "Hold Status","Payment Mode", "Status"
		    ];
	   // return [ "SAP Vendor Code", "Vendor Name", "PAN", "GST No", "State", "Payment Mode", "Status" ];

	else if (currentMaster === "Recovery City Master") {
	    return [ "City", "Branch Name", "Main Branch", "Zone", "360+ Existing Category", "Category 181-360", "Zone Code", "Status" ];
	}

	else if (currentMaster === "Payment Code Master") {
	    return [ "Payment Code", "Status" ];
	}

	else if (currentMaster === "Hold Code Master") {
	    return ["Hold Code",  "Hold Reason", "Status"];
	}
	
	//snz- end
	
	else if (currentMaster === "Flow City Master") {
	    return ["ID", "CITY_CODE", "CITY_NAME", "ZONE", "CAT", "MAIN_BRANCH_FOR_PAYOUT_CALCULATION", "STATUS"];
	}
	
	//added for CV & TW Coorgination--start
	else if(currentMaster == "Vendor Master"){
	    return [
	        "Vendor No",
	        "Name1",
	        "Name2",
	        "Search Item",
	        "Street House",
	        "Street4",
	        "Street5",
	        "Post Code",
	        "City",
	        "Country",
	        "Region",
	        "State",
	        "Tel No",
	        "Mobile No",
	        "Fax",
	        "CTR",
	        "Bank Key",
	        "Bank Account",
	        "Account Holder",
	        "Control Key",
	        "Bank Type",
	        "Reference Details",
	        "Rec AC",
	        "Payment Method",
	        "Alter Pay",
	        "PB",
	        "HBank",
	        "PAN Number",
	        "CIN PAN Number",
	        "Excise Reg Number",
	        "Central Sales Tax Number",
	        "Local Sales Tax Number",
	        "Service Tax Regis Number",
	        "Service Tax No",
	        "Sales Tax No",
	        "Name3",
	        "Name4",
	        "Bank Name",
	        "Bank Branch",
	        "Branch Address",
	        "Tax Code",
	        "WCT Code",
	        "Email Address",
	        "Out Sourcing Activity",
	        "VPTS ID",
	        "Activity No",
	        "GST Vendor Classification",
	        "GST Vendor Classification Desc",
	        "Tax Number 3",
	        "Black Listing Reason",
	        "Search Term 2",
	        "Withholding Tax Type 1",
	        "Withholding Tax Code 1",
	        "Withholding Tax Type 2",
	        "Withholding Tax Code 2",
	        "Withholding Tax Type 3",
	        "Withholding Tax Code 3",
	        "Withholding Tax Type 4",
	        "Withholding Tax Code 4",
	        "MSMED Status",
	        "Vendor Tagging",
	        "Comments",
	        "Vendor Block",
	        "Modified Time",
	        "Cred Info No",
	        "Specific Person 206AB 206CCA",
	        "Adhaar PAN Linked",
	        "Vendor Return Filing",
	        "PO Box Number",
	        "Tax Number 1",
	        "Department",
	        "Udyam",
	        "Exemption Number",
	        "Tax Number 1-2",
	        "Department 2",
	        "Create Date",
	        "Last Ext Review",
	        "Exemption From",
	        "Exemption To",
	        "Exemption Percentage",
	        "Threshold Amount Exemption",
	        "Lower TDS Rate",
	        "Standard Rate Related Party",
	        "Status"
	    ];
	}
	
	else if(currentMaster && currentMaster.trim() === "TDS Master"){
	    console.log("inside tds master getHeader");
	    return [
	        "ID",
	        "WTax Type",
	        "WTx",
	        "TDS Rate",
	        "Status"
	    ];
	}
	//end
}
 

/* ================= COLUMNS ================= */
 
function getColumns() {
	
	console.log("get column method call");
    if (currentMaster === "GST Master"){
        return ["apsCode", "name", "state","location","status"];
 
   }else if (currentMaster === "Branch Master"){
        return ["branchCode", "branchName", "hub", "aLState", "zone","rBH", "eDState", "eDZone", "zHName", "stateHead", "mISState","rCState", "location", "status"];
 
   }else if (currentMaster === "Channel Master"){
        return ["apsCode", "iBoxId","supplierId","dates", "suspended", "nameOfChannelAsPerAgmt", "typeOfDsa", "panNo","yy", "supplierId1", "supplierId2", "supplierId3", "supplierId4", "supplierId5","supplierId6", "supplierId7", "supplierId8"
					, "supplierId9", "supplierId10","supplierId11", "supplierId12", "remarks", "location", "misState","cState", "edState", "edZone"
					, "sourcing", "sourcingBk","manufactuName", "newManufactuName", "oldIBoxId", "rcLimit", "sapCode", "accountNo","ifscCode", "bankName", "iBankYesNonIBankNo", "remarksBk","status"];
 
    }else if (currentMaster === "Model Master"){
        return ["manufacturerId", "manufacturerDesc", "modelId","modelDesc", "assetCategory", "band","status"];
        }


// amit
 	if (currentMaster === "Outsource Master"){
        return ["empCode", "vSTSCodeCounselorSAPCode", "executiveName","status"];
	}
	if (currentMaster === "GST To Master"){
        return ["processShop", "gstStateTo", "status"];
	}
	if (currentMaster === "Ilens Channel Master"){
        return ["srNo", "userID", "userName", "iDCreationDate", "status", "emailID", "mobileNumber", "agencyID", "agencyType", 
				"vPTSID", "vPTSApprovalStatus", "annualReviewDueDate", "mSMERegistered", "udyogAadharNumber", "agencyPanNumber", 
				"userGroup", "channelName", "productType", "baseCPCProcessShop", "mappedEmployeeID", "mappedEmployeeName", 
				"inboundOutBoundType", "mappedSolIDs", "counsellorIDs", "tSMID", "tSMName", "childAllowed", "operatingLocations", 
				"roleCode", "vSTSID", "vSTSName", "vSTSStatus", "product","statusA"];
	}
	if (currentMaster === "GST State Master"){
        return ["partnerId", "partnerName", "gstState","status"];
	}
	if (currentMaster === "GST Mf Master"){
        return ["apsCode", "name", "state","status"];
	}
	
	
	//snz
	else if (currentMaster === "SAP Master") { 
		//return [ "sapVendorCode", "vendorName", "pan", "gstnNo", "state", "paymentMode", "status"  ];}
		return ["srNo", "unitCode", "cleanUnitId","vendorName", "ccaCallCenter", "pan", "sapVendorCode", "state", "tdsRate", "taxCode","finaliBoxids", "statusOfBlocking",
		        "accountStatus","credInfoNo",  "gstnNo", "sacCode", "serviceProviderIdStatus", "gstApplicable", "holdStatus","paymentMode", "status"
		    ];
		}

	   // RECOVERY CITY MASTER
	   else if (currentMaster === "Recovery City Master") {
	       return ["city", "branchName", "mainBranch", "zone", "existingCategory",  "cataegory181360", "zoneCode", "status"  ];

	   }

	   // PAYMENT CODE MASTER
	   else if (currentMaster === "Payment Code Master") {
	       return [  "code", "status"
	       ];

	   }

	   // HOLD CODE MASTER
	   else if (currentMaster === "Hold Code Master") {
	       return [ "code", "holdReason","status"  ];

	   }
	   
	   // FLOW CITY MASTER
	   else if (currentMaster === "Flow City Master") {
	       return ["id", "cityCode", "cityName", "zone", "cat", "mainBranchForPayoutCalculation", "status"];
	   }
	
	//added for CV & TW Coorgination--start
	   else if(currentMaster && currentMaster.trim() === "TDS Master"){
		    return [
		        "id",
		        "wtaxType",
		        "wtx",
		        "tdsRate",
		        "status"
		    ];
	   }
		
	   else if(currentMaster && currentMaster.trim() == "Vendor Master"){
		    return [
		        "vendorNo",
		        "name1",
		        "name2",
		        "searchItm",
		        "streetHouse",
		        "street4",
		        "street5",
		        "postCode",
		        "city",
		        "country",
		        "region",
		        "stateName",
		        "telNo",
		        "mobileNo",
		        "fax",
		        "ctr",
		        "bankKey",
		        "bankAccount",
		        "accountHolder",
		        "controlKey",
		        "bankType",
		        "referenceDetails",
		        "recAc",
		        "paymMethd",
		        "alterPay",
		        "pb",
		        "hBank",
		        "extraTextPanNumber",
		        "cinPanNumber",
		        "exciseRegNumber",
		        "centralSalesTaxNumber",
		        "localSalesTaxNumber",
		        "serviceTaxRegisNumber",
		        "serviceTaxNo",
		        "salesTaxNo",
		        "name3",
		        "name4",
		        "bankName",
		        "bankBranch",
		        "branchAddress",
		        "taxCode",
		        "wctCode",
		        "emailAddress",
		        "outSourcingActivity",
		        "vptsId",
		        "activityNo",
		        "gstVendorClassification",
		        "gstVendorClassificationDesc",
		        "taxNumber3",
		        "blackListingReason",
		        "searchTerm2",
		        "withholdingTaxType1",
		        "withholdingTaxCode1",
		        "withholdingTaxType2",
		        "withholdingTaxCode2",
		        "withholdingTaxType3",
		        "withholdingTaxCode3",
		        "withholdingTaxType4",
		        "withholdingTaxCode4",
		        "msmedStatus",
		        "vendorTagging",
		        "comments",
		        "vendorBlock",
		        "modifiedTime",
		        "credInfoNo",
		        "specificPerson206ab206cca",
		        "adhaarPanLinked",
		        "vendorReturnFiling",
		        "poBoxNumber",
		        "taxNumber1",
		        "department",
		        "udyam",
		        "exemptionNumber",
		        "taxNumber12",
		        "department2",
		        "createDateStr",
		        "lastExtReview",
		        "exemptionFrom",
		        "exemptionTo",
		        "exemptionPercentage",
		        "thresholdAmountExemption",
		        "lowerTdsRate",
		        "standardRateRelatedParty",
		        "status"
		    ];
	   }
	//end
}

 
 
 
// ================= GLOBAL =================
//snz 8-7
var currentProduct = "";
var currentSubProduct = "";
var currentMaster = "";

window.addEventListener("load", function () {

    if (document.getElementById("product")) {
        loadProducts();
    }

});


let selectedIds = new Set();      // store selected OR unselected ids
let unselectedIds= new Set();
let isSelectAllGlobal = false;    // flag for full select all
let isProgrammaticChange=false;


//var baseurl ="${pageContext.request.contextPath}";
var isEditOpen = false;
var oldRowHtml = null;

let tableData;
let tableColumns;

let pgdata =[];
let pgFilteredData=[];
let pgCurrentPage = 1;
let pgRowsPerPage  = 15;
let pgTotalPage = 0;
/* ================= MASTER CHANGE ================= */
 
 
 
/*snz 8-7*/ 
function loadProducts() {

    let productDropdown =
        document.getElementById("product");

    productDropdown.innerHTML =
        '<option value="">Select Product</option>';

    fetch(baseurl + "/DMAPayoutWeb3/products", {

        method: "GET",

        headers: {
            "Content-Type": "application/json"
        }

    })

	.then(res => {

	    if(!res.ok){
	        throw new Error("Unable to load products");
	    }

	    return res.json();

	})


    .then(data => {

        data.forEach(function(item){

            let option =
                document.createElement("option");

            option.value = item;
            option.text = item;

            productDropdown.appendChild(option);

        });

    });

}

function loadSubProduct() {

    currentProduct =
        document.getElementById("product").value;

    let subProductDropdown =
        document.getElementById("subProduct");

    let masterDropdown =
        document.getElementById("master");

    subProductDropdown.innerHTML =
        '<option value="">Select Sub Product</option>';

    masterDropdown.innerHTML =
        '<option value="">Select Master</option>';

    if (!currentProduct) {
        return;
    }

    fetch(baseurl +
        "/DMAPayoutWeb3/subProducts?product=" +
        encodeURIComponent(currentProduct))

        .then(res => res.json())

		.then(data => {
		    data.forEach(function(item){
		        let option =
		            document.createElement("option");
		        option.value = item;
		        option.text = item;
		        subProductDropdown.appendChild(option);
		    });
		})
		.catch(err => {
		    console.error(err);
		    alert("Unable to load Sub Products.");
		});
}

function loadMaster() {

    currentSubProduct =
        document.getElementById("subProduct").value;

    let masterDropdown =
        document.getElementById("master");

    masterDropdown.innerHTML =
        '<option value="">Select Master</option>';

    if (!currentProduct || !currentSubProduct) {
        return;
    }

    fetch(baseurl +
        "/DMAPayoutWeb3/masters?product=" +
        encodeURIComponent(currentProduct) +
        "&subProduct=" +
        encodeURIComponent(currentSubProduct))

        .then(res => res.json())

        .then(data => {

            data.forEach(function(master){

                let option =
                    document.createElement("option");

                option.value = master;
                option.text = master;

                masterDropdown.appendChild(option);

            });

        })
		.catch(err => {

		    console.error(err);
		    alert("Unable to load Masters.");

		});

}


function onMasterChange(val) {
	
	
	console.log("on Master Change");
    currentMaster = val.value.trim();

	if(!currentMaster || currentMaster === "Select Master" || currentMaster === ""){
		return;
	}

    console.log(currentMaster);

    clearTable();
	resetSelection();

	// clear / hide  multi aprrove and reject 
	let div = document.getElementById("multipleApproveReject");
	if(div) div.style.display="none";
	
	loadTable();
	

}
 
/* ================= LOAD TABLE ================= */
function getLoggedUser(){
		return localStorage.getItem("user_id");
		/*return 'BAN495699';*/
	} 

function loadTable() {
	
	
    console.log("on Load Table");
    /*if (!currentMaster){
	return;
    }*/ 

	resetSelection();
    var table = document.getElementById("masterTable");
    
    if(currentMaster === "Channel Master" || currentMaster === "SAP Master"){
    table.style.width = "max-content";
    }
    else{
     table.style.width = "-webkit-fill-available";
    }
    
 	
    let loadApi = baseurl + getLoadApi();
 	console.log(baseurl);
 	console.log(loadApi);
//	let loadApi = getLoadApi();
    let headers = getHeaders();
    let columns = getColumns();
 
    if (!loadApi) {
        alert("Load API not found for : " + currentMaster);
        return;
    }
 
    fetch(loadApi, {
		method: "GET",
		headers: {
            "Content-Type": "application/json",
			"user":getLoggedUser()
			}
        })
        .then(res => {
            
			console.log("Response Status :", res.status);
 			return res.text().then(text=>{
					 if (!res.ok) {
	           		
						hideTableStructure(currentMaster);
               			throw {
								status :res.status,
								message : text || "Unexpected Server Error"
							  };
            		  }
					if(!text){
						return [];
					}
				
					try{
						return JSON.parse(text);
					}catch{e}{
						return [];
					}
				
			});
           
        })
        .then(data => {
 
            console.log("Response Data :", data);
 
            if (!data || data.length === 0) {
	            hideTableStructure(currentMaster);

                alert("No Records available for Approval");
      
                return;
            }
 			
            /*createHeader(headers);
            createBody(data, columns);
			tableData=data;
			tableColumns=columns;
            showTableStructure(currentMaster);*/

			tableData=data;
			tableColumns=columns;
			
			// copy for pagination
			pgData = [...data];
			pgFilteredData = [...data];
			pgCurrentPage=1;
			
			// create header 
			createHeader(headers);
			
			//renderFirstPage
			renderPaginatedTable();
			
			showTableStructure(currentMaster);


        })
        .catch(err => {
            console.error("Fetch Error :", err);
            console.error("Fetch Error :", err.message);
            hideTableStructure(currentMaster);
			if(err.status){
				/*alert("Error "+ err.status + " : " + err.message);*/
					alert(err.message);
			}else{
            	alert("Network error : Unable to connect to server");
			}
        });
}
 


/* ================= CREATE HEADER ================= */
 
function createHeader(headers) {
	console.log("create header method call");
    let thead = document.getElementById("thead");
    thead.innerHTML = "";
 
    let row = '<tr style="background-color: #20446C; color: #ffffff; text-align: center;">';

	row += "<th style='width:4%; text-align:center;'>" +
           "<input type='checkbox' style='width:18px; height:18px;' " +
           "name='checker_selectedAll' value='selectAll' id='selectAll' onclick='selectAllRows(this)'/>" +
           "</th>";

    /*headers.forEach(h => row += "<th>" + h + "</th>");*/
    
      headers.forEach((h, index) => {row += `<th onclick="sortByColumn(${index})" class="sortable-th" 	
	  				style="
	                    white-space: nowrap;
	                    min-width: max-content;
	                    padding: 10px 12px;
	                    text-align: center;
	                    vertical-align: middle;"
						> ${h} <span class="sort-arrow" id="arrow-${index}"></span></th>`;});

    row += "<th>Approve</th>";
    row += "<th>Reject</th></tr>";
 
    thead.innerHTML = row;
}
 


/* ================= CREATE BODY ================= */
 
function createBody(data, columns) {
	console.log("create Body method call");
    let tbody = document.getElementById("tbody");
    tbody.innerHTML = "";
	
	/*let keyField = columns[0];*/
 
	let pkField = getPrimaryKeyByMaster();

    data.forEach(obj => {
		let pkValue = obj[pkField];``
		
        let row = `<tr data-pk-field = "${pkField}" data-pk-value="${pkValue}" style="text-align: center;">`;
 
			row += `<td>
                   <input type="checkbox" 
						  class = "rowCheck" 
						  style="width: 100%;height: 18px;" 
						  name="checker_selectedAll" 
						  value="${pkValue}"
						  onchange="syncHeaderCheckbox()">
                </td>`;
        columns.forEach(col => {
            row += "<td>" + (obj[col] || "") + "</td>";
        });
/* onclick="editRow(this)"*/
			row += `<td>
                   <a href="#" class="btn btn-link btn-sm"  onclick="approveSingle(this)" disable>Approve</a>
                </td>`;
		
			   row += `<td>
                   <a href="#" class="btn btn-link btn-sm" onclick="rejectSingle(this)">Reject</a>
                </td>`;
		
        row += "</tr>";
        tbody.innerHTML += row;
    });
}




/*function selectAllRows(masterCheckbox){
	let checkboxes = document.querySelectorAll(".rowCheck");
	checkboxes.forEach(cb=>{
		cb.checked = masterCheckbox.checked;
	})
	syncHeaderCheckbox();
	//showHideMultiApproveRejectButton();
}*/


// ================= HEADER SELECT =================
function selectAllRows(headerCheckbox){

	isProgrammaticChange =true;
	
    isSelectAllGlobal = headerCheckbox.checked;

 	selectedIds.clear();
	unselectedIds.clear();
	
	let checkboxes = document.querySelectorAll("#tbody .rowCheck");
 
    checkboxes.forEach(cb => {
        cb.checked = headerCheckbox.checked;
    });
 
    isProgrammaticChange = false;
 
    syncHeaderCheckbox();

  	showHideMultiApproveRejectButton();
}




function syncHeaderCheckbox(){
	
	let allCheckboxes = document.querySelectorAll("#tbody .rowCheck");
	let checkedCheckboxes = document.querySelectorAll("#tbody .rowCheck:checked");
	let selectAll = document.getElementById("selectAll");
	
	if(allCheckboxes.length ===0)return ;
	
	if(checkedCheckboxes.length ===allCheckboxes.length){
		selectAll.checked = true;
		selectAll.indeterminate = false;
	}
	else if(checkedCheckboxes.length > 0){
		
		selectAll.checked = false;
		selectAll.indeterminate = true;
		
	}
	else {
		selectAll.checked = false;
		selectAll.indeterminate = false;
	}
	
	showHideMultiApproveRejectButton();
}

function showHideMultiApproveRejectButton(){
	
	let selected = document.querySelectorAll(".rowCheck:checked");
	let div = document.getElementById("multipleApproveReject");
	
	if(selected.length > 1){
		div.style.display = "block";
	}else{
		div.style.display = "none";
	}
}

/*document.addEventListener("change", function(e){
	if(e.target.classList.contains("rowCheck")){
		syncHeaderCheckbox();
	}
})*/

// ================= ROW CHECKBOX CHANGE =================
document.addEventListener("change", function(e){
 
	if(isProgrammaticChange) return;
	
    if(!e.target.classList.contains("rowCheck")) return ;
 
        let id = String(e.target.value);
 
        if(isSelectAllGlobal){
            // ALL selected → store only unchecked
            if(e.target.checked){
                unselectedIds.delete(id);
            } else {
                unselectedIds.add(id);
            }
        } else {
            // Normal selection
            if(e.target.checked){
                selectedIds.add(id);
            } else {
                selectedIds.delete(id);
            }
        }
 
        syncHeaderCheckbox();
        showHideMultiApproveRejectButton();
    
});


function applySelectionState(){
 
	isProgrammaticChange = true;
	
    let checkboxes = document.querySelectorAll("#tbody .rowCheck");
 
    checkboxes.forEach(cb => {
 
	let id = String(cb.value);
	
		if(isSelectAllGlobal){
			cb.checked = !unselectedIds.has(id);
		}else{
			cb.checked = selectedIds.has(id);
		}
	
    });
 
	isProgrammaticChange = false;
	
    syncHeaderCheckbox();
 	/*showHideMultiApproveRejectButton();*/
}



/*function getSelectedPrimaryKey(){
	let selected = [];
	let checkboxes = document.querySelectorAll(".rowCheck:checked");
	
	checkboxes.forEach(cb=>{
		selected.push(cb.value);
	});
	
	return selected;
}*/

function getSelectedPrimaryKey(){
 
	let pkField = getPrimaryKeyByMaster();
 
    // extra safety
	if(isSelectAllGlobal){
		
    return pgFilteredData
        .map(obj => String(obj[pkField]))
        .filter(id => !unselectedIds.has(id));
	}else{
	
	return Array.from(selectedIds);
	}
}



function approveMultiple(btn){
	
	let ids =getSelectedPrimaryKey();
	
	console.log("SELECTED Ids  : "+ ids);
	
	if(ids.length === 0){
		alert("please select at least one record");
		return
	}
	
	let payload = {
		primaryIds :ids,
		decision:"A",
		remark:""
	}
	
	tempPayload = payload;
	tempBtn=btn;
	
	openRemarkPopup();
	
	//updateByChecker(payload,btn);
}

function rejectMultiple(btn){
	let ids =getSelectedPrimaryKey();
	
	console.log("SELECTED Ids  : "+ ids);
	
	if(ids.length === 0){
		alert("please select at least one record");
		return
	}
	
	let payload = {
		primaryIds :ids,
		decision:"R",
		remark:""
	}
	
	tempPayload = payload;
	tempBtn=btn;
	
	openRemarkPopup();
	//updateByChecker(payload,btn);
}

function approveSingle(btn){
	let tr=btn.closest("tr");
	let primaryKey = tr.dataset.pkValue;
	
//	let remark = openRemarkPrompt();

	let payload = {
		primaryIds :[primaryKey],
		decision:"A",
		remark:""
	}
	
	tempPayload = payload;
	tempBtn=btn;
	
	openRemarkPopup();
	
		// old running code
//	updateByChecker(payload,btn);
}

function rejectSingle(btn){
	let tr=btn.closest("tr");
	let primaryKey = tr.dataset.pkValue;
	let payload = {
		primaryIds :[primaryKey],
		decision:"R",
		remark:""
	}
	
	tempPayload = payload;
	tempBtn=btn;
	
	openRemarkPopup();
	//updateByChecker(payload,btn);
}

let tempPayload=null;
let tempBtn=null;

function updateByChecker(payload,btn){
	
	if(!btn)return;
	btn.disabled = true ;
	const originalText = btn.innerHTML;
	btn.innerHTML="Submitting...";
	
	    fetch(baseurl+getSaveApi(), {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
			"user":getLoggedUser()
        },
        body: JSON.stringify(payload)
    })
	.then(res => {
        return res.text().then(text => {
            return {
                status: res.status,
                message: text
            };
        });
    }).then(result => {
 
        if (result.status === 200) {
 
            alert(result.message);
            loadTable();  // reload table
 			document.getElementById("multipleApproveReject").style.display = "none";
        } else {

            alert(result.message);
            btn.disabled = false; // enable again
			btn.innerHTML=originalText;
        }
    })
    .catch(err => {
 
        alert("Server Error : " + err.message);
        btn.disabled = false;
		btn.innerHTML=originalText;
    })
	.finally(()=> {
		btn.disabled = false;
		btn.innerHTML=originalText;
	});
}
 


/*function openRemarkPrompt(){
	return prompt("Enter Remark (Optional) : ") || "";
}*/






/*function openRemarkPopup(){
 
    // Overlay
    const overlay = document.createElement("div");
    overlay.id = "remarkOverlay";
    overlay.style.position = "fixed";
    overlay.style.top = "0";
    overlay.style.left = "0";
    overlay.style.width = "100%";
    overlay.style.height = "100%";
    overlay.style.background = "rgba(0,0,0,0.5)";
    overlay.style.display = "flex";
    overlay.style.justifyContent = "center";
    overlay.style.alignItems = "center";
 
    // Modal Box
    const box = document.createElement("div");
    box.id = "remarkBox";
    box.style.background = "#fff";
    box.style.padding = "20px";
    box.style.width = "350px";
    box.style.borderRadius = "8px";
 
    box.innerHTML = `
        <h3>Enter Remark</h3>
        <textarea id="remarkText" 
                  style="width:100%;height:80px;margin-top:10px;"></textarea>
        <div style="text-align:right;margin-top:15px;">
            <button onclick="submitRemark()">Submit</button>
            <button onclick="closeRemark()">Cancel</button>
        </div>
    `;
 
    overlay.appendChild(box);
    document.body.appendChild(overlay);
}
*/

function openRemarkPopup(){
  /*<span onclick="closeRemark()"
                  style="cursor:pointer;font-size:18px;color:#999;"></span>*/
    // 🔹 Overlay
    const overlay = document.createElement("div");
    overlay.id = "remarkOverlay";
    overlay.style.position = "fixed";
    overlay.style.top = "0";
    overlay.style.left = "0";
    overlay.style.width = "100%";
    overlay.style.height = "100%";
    overlay.style.background = "rgba(0,0,0,0.45)";
    overlay.style.display = "flex";
    overlay.style.justifyContent = "center";
    overlay.style.alignItems = "center";
    overlay.style.zIndex = "9999";
 
    // 🔹 Modal Box
    const box = document.createElement("div");
    box.style.width = "420px";
    box.style.background = "#ffffff";
    box.style.borderRadius = "12px";
    box.style.boxShadow = "0 8px 25px rgba(0,0,0,0.2)";
    box.style.padding = "25px";
    box.style.fontFamily = "Segoe UI, sans-serif";
    box.style.animation = "fadeIn 0.2s ease-in-out";
 
    box.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;">
            <h3 style="margin:0;color:#2c3e50;">Add Remark</h3>
        </div>
 
        <textarea id="remarkText"
            placeholder="Enter your remark (optional)..."
            style="
                width:100%;
                height:90px;
                margin-top:15px;
                padding:10px;
                border:1px solid #ccc;
                border-radius:6px;
                resize:none;
                font-size:14px;
                outline:none;
            "></textarea>
 
        <div style="text-align:right;margin-top:20px;">
            <button onclick="closeRemark()"
                style="
                    padding:8px 16px;
                    border:none;
                    border-radius:6px;
                    background:#e0e0e0;
                    cursor:pointer;
                    margin-right:8px;
                ">
                Cancel
            </button>
 
            <button onclick="submitRemark()"
                style="
                    padding:8px 18px;
                    border:none;
                    border-radius:6px;
                    background:linear-gradient(45deg,#007bff,#0056d2);
                    color:white;
                    cursor:pointer;
                    font-weight:500;
                ">
                Submit
            </button>
        </div>
    `;
 
    overlay.appendChild(box);
    document.body.appendChild(overlay);
}

function submitRemark(){
 
    let remark = document.getElementById("remarkText").value.trim();
 
    tempPayload.remark = remark;
 
	let finalPayload = tempPayload;
	let finalBtn=tempBtn;
    closeRemark();
 
    updateByChecker(finalPayload, finalBtn);
	tempPayload = null;
	tempBtn=null;
}
 
function closeRemark(){
 
    const overlay = document.getElementById("remarkOverlay");
 
    if(overlay){
        document.body.removeChild(overlay);
    }
	tempPayload = null;
	tempBtn=null;

}
 










function clearTable() {

    document.getElementById("thead").innerHTML = "";
    document.getElementById("tbody").innerHTML = "";
	pgData=[];
	pgFilteredData=[];
	/*pgFilterdData=[];*/
	pgCurrentPage=1;
	pgTotalPage=0;
	
	selectedIds.clear();
	isSelectAllGlobal=false;
	
	
	  // Clear pagination UI
    document.getElementById("paginationContainer").innerHTML = "";
    document.getElementById("paginationInfo").innerHTML = "";
 
    // Hide wrapper
    let wrapper = document.getElementById("paginationWrapper");
    if (wrapper) wrapper.style.display = "none";
	

	
}


function hideTableStructure(title){
	
	var tableTag= document.getElementById("masterTable");
    if(tableTag)
         tableTag.style.display = "none";

   var addRowButton= document.getElementById("addNewRow");
   if(addRowButton)
        addRowButton.style.display= "none";

     var titleDiv=document.getElementById("tableTitle");
     if(titleDiv)
        titleDiv.innerText="";
        titleDiv.style.display="none";

     // Clear pagination UI
    document.getElementById("paginationContainer").innerHTML = "";
    document.getElementById("paginationInfo").innerHTML = "";
 
    // Hide wrapper
    let wrapper = document.getElementById("paginationWrapper");
    if (wrapper) wrapper.style.display = "none";
	
}

function showTableStructure(title){
    var tableTag= document.getElementById("masterTable");
    if(tableTag)
         tableTag.style.display = "table";

   var addRowButton= document.getElementById("addNewRow");
   if(addRowButton)
        addRowButton.style.display="inline-block";

   var titleDiv=document.getElementById("tableTitle");
   if(titleDiv)
        titleDiv.innerText=title;
        titleDiv.style.display="block";

    let wrapper = document.getElementById("paginationWrapper");
    if (wrapper) wrapper.style.display = "flex";

}

function showPendingPopup(btn) {
 
    // Button position
    let rect = btn.getBoundingClientRect();
 
    let popup = document.createElement("div");
    popup.innerText = "Already waiting for approval";
 
    popup.style.position = "absolute";
    popup.style.background = "#dc3545";
    popup.style.color = "#fff";
    popup.style.padding = "5px 10px";
    popup.style.borderRadius = "4px";
    popup.style.fontSize = "12px";
    popup.style.whiteSpace = "nowrap";
    popup.style.zIndex = "9999";
    popup.style.boxShadow = "0 2px 6px rgba(0,0,0,0.2)";
 
    // Position just above button
    popup.style.left = rect.left + window.scrollX + "px";
    popup.style.top = rect.top + window.scrollY - 35 + "px";
 
    document.body.appendChild(popup);
 
    // Fade out effect
    setTimeout(() => {
        popup.style.transition = "opacity 0.3s";
        popup.style.opacity = "0";
    }, 800);
 
    setTimeout(() => {
        popup.remove();
    }, 1000);
}



/* =========================== Sort Columns ASC and DESC ===============================*/
 
let sortAsc = true;
function sortByColumn(colIndex) {
    let key = tableColumns[colIndex];
    tableData.sort((a, b) => {
        let valA = a[key].toString().toUpperCase();
        let valB = b[key].toString().toUpperCase();
        if (valA < valB) return sortAsc ? -1 : 1;
        if (valA > valB) return sortAsc ? 1 : -1;
        return 0;
    });
    // Remove arrows from all
    document.querySelectorAll(".sort-arrow").forEach(el => {
        el.classList.remove("asc", "desc");
    });
    // Apply arrow to selected column
    let arrow = document.getElementById("arrow-" + colIndex);
    arrow.classList.add(sortAsc ? "asc" : "desc");
    sortAsc = !sortAsc;
    createBody(tableData, tableColumns);
}


// ================= MAIN PAGINATION FUNCTION =================
function renderPaginatedTable() {
 
    // If no data
    if (!pgFilteredData || pgFilteredData.length === 0) {
        document.getElementById("tbody").innerHTML = "";
        document.getElementById("paginationContainer").innerHTML = "";
        document.getElementById("paginationInfo").innerHTML = "";

		let wrapper = document.getElementById("paginationWrapper");
		if(wrapper) wrapper.style.display = "none";
        return;
    }
 

	let wrapper = document.getElementById("paginationWrapper");
	if(wrapper) wrapper.style.display = "flex";
	
    // Calculate total pages
    pgTotalPages = Math.ceil(pgFilteredData.length / pgRowsPerPage);
 
    // Safety check
    if (pgCurrentPage > pgTotalPages) {
        pgCurrentPage = pgTotalPages;
    }
 
    // Calculate slice index
    let startIndex = (pgCurrentPage - 1) * pgRowsPerPage;
    let endIndex = startIndex + pgRowsPerPage;
 
    // Get only 15 records for current page
    let pageData = pgFilteredData.slice(startIndex, endIndex);
 
    // Call your existing createBody()
    createBody(pageData, tableColumns);
 
    // Render buttons
    renderPaginationUI();

	applySelectionState();
}
 
// ================= PAGINATION BUTTON UI =================
function renderPaginationUI() {
 
    let container = document.getElementById("paginationContainer");
    let info = document.getElementById("paginationInfo");
 
    container.innerHTML = "";
 
    if (pgTotalPages <= 1) {
        info.innerHTML = "";
        return;
    }
 
    let startRecord = (pgCurrentPage - 1) * pgRowsPerPage + 1;
    let endRecord = Math.min(pgCurrentPage * pgRowsPerPage, pgFilteredData.length);
 
    info.innerHTML =
        "Showing " + startRecord +
        " to " + endRecord +
        " of " + pgFilteredData.length + " entries";
 
    let html = "";
 
    // Prev Button
    html += `<button 
        ${pgCurrentPage === 1 ? "disabled" : ""}
        onclick="changePage('prev')">Prev</button>`;
 
    // Page Numbers
    for (let i = 1; i <= pgTotalPages; i++) {
 
        if (
            i === 1 ||
            i === pgTotalPages ||
            (i >= pgCurrentPage - 2 && i <= pgCurrentPage + 2)
        ) {
            html += `<button 
                class="${pgCurrentPage === i ? 'active-page' : ''}"
                onclick="changePage(${i})">${i}</button>`;
        }
    }
 
    // Next Button
    html += `<button 
        ${pgCurrentPage === pgTotalPages ? "disabled" : ""}
        onclick="changePage('next')">Next</button>`;
 
    container.innerHTML = html;
}
 
// ================= PAGE CHANGE LOGIC =================
function changePage(page) {
 
    if (page === "prev" && pgCurrentPage > 1) {
        pgCurrentPage--;
    }
    else if (page === "next" && pgCurrentPage < pgTotalPages) {
        pgCurrentPage++;
    }
    else if (!isNaN(page)) {
        pgCurrentPage = page;
    }
 
    renderPaginatedTable();
}
 

function resetSelection(){
 
    isSelectAllGlobal = false;
    selectedIds.clear();
    unselectedIds.clear();
 
    let header = document.getElementById("selectAll");
    if(header){
        header.checked = false;
        header.indeterminate = false;
    }
 
    applySelectionState();
    showHideMultiApproveRejectButton();
}
