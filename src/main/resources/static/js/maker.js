/* ================= API PROVIDER ================= */
 
function getLoadApi() {
	console.log(" load API method call");
    if (currentMaster === "GST Master") return "/DMAPayoutWeb3/allGSTMasMaker";
    if (currentMaster === "Branch Master") return "/DMAPayoutWeb3/allBranchMasMaker";
    if (currentMaster === "Channel Master") return "/DMAPayoutWeb3/allChannelMasMaker";
    if (currentMaster === "Model Master") return "/DMAPayoutWeb3/allModelMasMaker";
	
	//amit
    //if (currentMaster === "Outsource Master") return "/DMAPayoutWeb3/allModelMasMaker";
    //if (currentMaster === "GST To Master") return "/DMAPayoutWeb3/allModelMasMaker";
    //if (currentMaster === "Ilens Channel master") return "/DMAPayoutWeb3/allModelMasMaker";
    //if (currentMaster === "GST State Master") return "/DMAPayoutWeb3/allModelMasMaker";
    //if (currentMaster === "GST Mf Master") return "/DMAPayoutWeb3/allModelMasMaker";
	
	
	//snz
	if (currentMaster === "SAP Master")  return "/DMAPayoutWeb3/getAllSAPMasterByMaker";
	if (currentMaster === "Recovery City Master") return "/DMAPayoutWeb3/getRecoveryCityMaker";
	if (currentMaster === "Payment Code Master")return "/DMAPayoutWeb3/getPaymentCodeMaker";
	if (currentMaster === "Hold Code Master") return "/DMAPayoutWeb3/getHoldCodeMaker";
	
	//added for CV & TW Coorgination--start
	if (currentMaster === "Vendor Master") return "/DMAPayoutWeb3/getAllVendorMasterByMaker";
	if (currentMaster === "TDS Master") return "/DMAPayoutWeb3/getAllTdsCodeRateByMaker";
	//end
		
}
 
function getSaveApi() {
	
	console.log(currentMaster);
	console.log(" save API method call");
    if (currentMaster === "GST Master") return "/DMAPayoutWeb3/createGSTMasMaker";
    if (currentMaster === "Branch Master") return "/DMAPayoutWeb3/createBranchMasMaker";
    if (currentMaster === "Channel Master") return "/DMAPayoutWeb3/createChannelMasMaker";
    if (currentMaster === "Model Master") return "/DMAPayoutWeb3/createModelMasMaker";
	
	 //amit
    if (currentMaster === "Outsource Master") return "/DMAPayoutWeb3/createOutsourceMasMaker";
    if (currentMaster === "GST To Master") return "/DMAPayoutWeb3/createGstToMasMaker";
    if (currentMaster === "Ilens Channel Master") return "/DMAPayoutWeb3/createIlensChannelMasMaker";
    if (currentMaster === "GST State Master") return "/DMAPayoutWeb3/createGstStateMasMaker";
    if (currentMaster === "GST Mf Master") return "/DMAPayoutWeb3/createGstMfMasMaker";
	
	
	//snz
	if (currentMaster === "SAP Master") return "/DMAPayoutWeb3/createSAPMasterByMaker";
	if (currentMaster === "Recovery City Master") return "/DMAPayoutWeb3/createRecoveryCityMaker";
	if (currentMaster === "Payment Code Master") return "/DMAPayoutWeb3/createPaymentCodeMaker";
	if (currentMaster === "Hold Code Master") return "/DMAPayoutWeb3/createHoldCodeMaker";
	
	//added for CV & TW Coorgination--start
	if (currentMaster === "Vendor Master") return "/DMAPayoutWeb3/createVendorMasterByMaker";
	if (currentMaster === "TDS Master") return "/DMAPayoutWeb3/createTdsCodeRateByMaker";
	//end
}


function getUpdateApi() {
	
	console.log(currentMaster);
	console.log(" Update API method call");
    if (currentMaster === "GST Master") return "/DMAPayoutWeb3/updateGSTMasMaker";
    if (currentMaster === "Branch Master") return "/DMAPayoutWeb3/updateBranchMasMaker";
    if (currentMaster === "Channel Master") return "/DMAPayoutWeb3/updateChannelMasMaker";
    if (currentMaster === "Model Master") return "/DMAPayoutWeb3/updateModelMasMaker";
	//amit
	if (currentMaster === "Outsource Master") return "/DMAPayoutWeb3/updateOutsourceMasMaker";
    if (currentMaster === "GST To Master") return "/DMAPayoutWeb3/updateGstToMasMaker";
    if (currentMaster === "Ilens Channel Master") return "/DMAPayoutWeb3/updateIlensChannelMasMaker";
    if (currentMaster === "GST State Master") return "/DMAPayoutWeb3/updateGstStateMasMaker";
    if (currentMaster === "GST Mf Master") return "/DMAPayoutWeb3/updateGstMfMasMaker";
	
	
	//snz
	if (currentMaster === "SAP Master") return "/DMAPayoutWeb3/updateSAPMasterByMaker";
	if (currentMaster === "Recovery City Master") return "/DMAPayoutWeb3/updateRecoveryCityMaker";
	if (currentMaster === "Payment Code Master") return "/DMAPayoutWeb3/updatePaymentCodeMaker";
	if (currentMaster === "Hold Code Master") return "/DMAPayoutWeb3/updateHoldCodeMaker";
	
	//	spj
	if (currentMaster === "Flow City Master") return "/flowCityMaster/updateFlowCityMasterByMaker";
	
	//added for CV & TW Coorgination--start
    if (currentMaster === "Vendor Master") return "/DMAPayoutWeb3/updateVendorMasterByMaker";
	if (currentMaster === "TDS Master") return "/DMAPayoutWeb3/updateTdsCodeRateByMaker";
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
					"Role Code", "VSTS ID", "VSTS Name", "VSTS Status", "Product","Status of Record"];

	if (currentMaster === "GST State Master")
        return ["PARTNER_ID", "PARTNER NAME", "GST State","Status"];

	if (currentMaster === "GST Mf Master")
       /* return ["APScode", "Name", "State","Location","Address","dd","LocationMF","Status"];*/
		  return ["APScode", "Name", "State","Status"];


	//snz
	if(currentMaster === "SAP Master") {
	    return [ "SR No", "Unit Code", "Clean Unit ID", "Vendor Name", "CCA Call Center", "PAN", "SAP Vendor Code", "State", "TDS Rate", "Tax Code",
	        "Finali-Boxids", "Status Of Blocking", "Account Status", "Cred Info No", "GSTN No", "SAC Code", "Service Provider ID Status", "GST Applicable",
	        "Hold Status", "Payment Mode","Status"
	    ];
	}
	
	if (currentMaster === "Recovery City Master")
	    return ["City",  "Branch Name", "Main Branch", "Zone", "360+ Existing Category", "Category 181-360", "Zone Code", "Status" ];

	if (currentMaster === "Payment Code Master")
		return ["Code", "Status"];

	if (currentMaster === "Hold Code Master")
	    return ["Code", "Hold Reason", "Status"];
	    
	//spj
	if (currentMaster === "Flow City Master")
		return ["CITY_CODE", "CITY_NAME", "ZONE", "CAT", "MAIN_BRANCH_FOR_PAYOUT_CALCULATION", "STATUS"];

	//added for CV & TW Coorgination--start
	if(currentMaster && currentMaster.trim() === "TDS Master")
	    return [
	        "ID",
	        "WTax Type",
	        "WTx",
	        "TDS Rate",
	        "Status"
	    ];
	
	if(currentMaster && currentMaster.trim() === "Vendor Master")
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
	//end
}
 
/* ================= COLUMNS ================= */
 
function getColumns() {
	
	console.log("get column method call");
    if (currentMaster === "GST Master")
        return ["apsCode", "name", "state","location","status"];
 
    if (currentMaster === "Branch Master")
        return ["branchCode", "branchName", "hub", "aLState", "zone","rBH", "eDState", "eDZone", "zHName", "stateHead", "mISState","rCState", "location", "status"];
 
    if (currentMaster === "Channel Master")
        return ["apsCode", "iBoxId","supplierId","dates", "suspended", "nameOfChannelAsPerAgmt", "typeOfDsa", "panNo","yy", "supplierId1", "supplierId2", "supplierId3", "supplierId4", "supplierId5","supplierId6", "supplierId7", "supplierId8"
, "supplierId9", "supplierId10","supplierId11", "supplierId12", "remarks", "location", "misState","cState", "edState", "edZone"
, "sourcing", "sourcingBk","manufactuName", "newManufactuName", "oldIBoxId", "rcLimit", "sapCode", "accountNo","ifscCode", "bankName", "iBankYesNonIBankNo", "remarksBk","status"];
 
    if (currentMaster === "Model Master")
        return ["manufacturerId", "manufacturerDesc", "modelId","modelDesc", "assetCategory", "band","status"];
	
	// amit
 	if (currentMaster === "Outsource Master")
        return ["empCode", "vSTSCodeCounselorSAPCode", "executiveName","status"];

	if (currentMaster === "GST To Master")
        return ["processShop", "gstStateTo", "status"];

	if (currentMaster === "Ilens Channel Master")
        return ["srNo", "userID", "userName", "iDCreationDate", "status", "emailID", "mobileNumber", "agencyID", "agencyType", 
				"vPTSID", "vPTSApprovalStatus", "annualReviewDueDate", "mSMERegistered", "udyogAadharNumber", "agencyPanNumber", 
				"userGroup", "channelName", "productType", "baseCPCProcessShop", "mappedEmployeeID", "mappedEmployeeName", 
				"inboundOutBoundType", "mappedSolIDs", "counsellorIDs", "tSMID", "tSMName", "childAllowed", "operatingLocations", 
				"roleCode", "vSTSID", "vSTSName", "vSTSStatus", "product","statusA"];

	if (currentMaster === "GST State Master")
        return ["partnerId", "partnerName", "gstState","status"];

	if (currentMaster === "GST Mf Master")
        /*return ["apsCode", "name", "state","location", "address", "dd","locationMf","status"];*/
		return ["apsCode", "name", "state","status"];
	
	
	
	
	//snz
	if (currentMaster === "SAP Master") 
		return [ "srNo", "unitCode", "cleanUnitId","vendorName", "ccaCallCenter", "pan", "sapVendorCode",  "state", "tdsRate", "taxCode", "finaliBoxids",
		    "statusOfBlocking", "accountStatus", "credInfoNo",  "gstnNo",  "sacCode", "serviceProviderIdStatus",  "gstApplicable", "holdStatus",
		    "paymentMode", "status"
		];
		//return ["sapVendorCode", "vendorName", "pan", "gstnNo", "state", "paymentMode", "status"];

	if (currentMaster === "Recovery City Master")
	    return [ "city", "branchName", "mainBranch", "zone", "existingCategory", "cataegory181360", "zoneCode", "status"];

	if (currentMaster === "Payment Code Master") return ["code", "status"];

	if (currentMaster === "Hold Code Master") return ["code", "holdReason", "status"];
	
	//	spj
	if (currentMaster === "Flow City Master") 
		return ["cityCode", "cityName", "zone", "cat", "mainBranchForPayoutCalculation", "status"];

	
	// added for CV & TW Coorgination --start
	   else if(currentMaster && currentMaster.trim() === "TDS Master")
		    return [
		        "id",
		        "wtaxType",
		        "wtx",
		        "tdsRate",
		        "status"
		    ];
		
	   else if(currentMaster && currentMaster.trim() == "Vendor Master")
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
//end
	
}


//snz
function getStatusApi() {

    const statusApiMap = {

        // Vehicle Loan
        "GST Master": "/DMAPayoutWeb3/vhlMaster?master=GST Master",
        "Branch Master": "/DMAPayoutWeb3/vhlMaster?master=Branch Master",
        "Channel Master": "/DMAPayoutWeb3/vhlMaster?master=Channel Master",
        "Model Master": "/DMAPayoutWeb3/vhlMaster?master=Model Master",

		"Ilens Channel Master": "/DMAPayoutWeb3/vhlMaster?master=Ilens Channel Master",
		"Outsource Master": "/DMAPayoutWeb3/vhlMaster?master=Outsource Master",
        "GST State Master": "/DMAPayoutWeb3/vhlMaster?master=GST State Master",
        "GST Mf Master": "/DMAPayoutWeb3/vhlMaster?master=GST Mf Master",
        "GST To Master": "/DMAPayoutWeb3/vhlMaster?master=GST To Master",
		

		//snz
        // Credit Card
        "SAP Master": "/DMAPayoutWeb3/getSAPMasterByStatus",
		"Recovery City Master": "/DMAPayoutWeb3/getRecoveryCityByStatus",
		"Payment Code Master": "/DMAPayoutWeb3/getPaymentCodeByStatus",
		"Hold Code Master": "/DMAPayoutWeb3/getHoldCodeByStatus",
		
		 //	spj
        "Flow City Master": "/flowCityMaster/getRecords",
		
		//added for CV & TW Coorgination
		"Vendor Master": "/DMAPayoutWeb3/getVendorMasterByStatus",
        "TDS Master": "/DMAPayoutWeb3/getTdsCodeRateByStatus",
        //end
    };

    return statusApiMap[currentMaster];
}
function getNonEditableColumns(){
	
	 if (currentMaster === "GST Master")
        return ["status","apsCode","name"];
 
    if (currentMaster === "Branch Master")
        return ["status","branchCode","branchName"];
    if (currentMaster === "Channel Master")
        return ["status","apsCode","nameOfChannelAsPerAgmt","iBoxId","panNo"];
    if (currentMaster === "Model Master")
        return ["status", "manufacturerId", "manufacturerDesc", "modelId","modelDesc"];

	//amit
	if (currentMaster === "Outsource Master")
        return ["status","empCode"];

	if (currentMaster === "GST To Master")
        return ["status","processShop"];

	if (currentMaster === "Ilens Channel master")
        return ["statusA","userID"];

	if (currentMaster === "GST State Master")
        return ["status","partnerId"];

	if (currentMaster === "GST Mf Master")
        return ["status","apsCode"];


	//snz
    if (currentMaster === "SAP Master")
        return ["status", "sapVendorCode"];

    if (currentMaster === "Recovery City Master")
        return ["status", "city"];

    if (currentMaster === "Payment Code Master")
        return ["status", "code"];

    if (currentMaster === "Hold Code Master")
        return ["status", "code"];
       
    //spj    
	if (currentMaster === "Flow City Master")
		return ["cityCode", "status"];
    
    //added for CV & TW Coorgination--start
    if (currentMaster === "Vendor Master")
        return ["status", "VendorNo"];
    if (currentMaster === "TDS Master")
        return ["status", "tdsRate"];
    //end
	
}

/* ================= GET PRIMARY KEY FIELD BY MASTER ================= */

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
        return "processShop";

	if (currentMaster === "Ilens Channel master")
        return "userID";

	if (currentMaster === "GST State Master")
        return "partnerId";

	if (currentMaster === "GST Mf Master")
        return "apsCode";

	//snz
	if (currentMaster === "SAP Master")
	    return "sapVendorCode";	
		
	if (currentMaster === "Recovery City Master")
	    return "city";

	if (currentMaster === "Payment Code Master")
	    return "code";

	if (currentMaster === "Hold Code Master")
	    return "code";
	
	 // added for CV & TW Coorgination--start
	if(currentMaster=="Vendor Master")
        return "vendorNo";
 
    if(currentMaster=="TDS Master"){
        return "id";
    }
    //end
}




/*===================================== A C T I O N S===================================*/


//var baseurl =window.location.origin;

//snz
var currentProduct = "";
var currentSubProduct = "";


var currentMaster = "";
var status = "";


//snz8-7
window.addEventListener("load", function () {

    console.log("Page Loaded");

    if (document.getElementById("product")) {
        loadProducts();
    }

});

var isEditOpen = false;
var oldRowHtml = null;
var radioState = false;


let tableData;
let tableColumns;
let pgdata =[];
let pgFilteredData=[];
let pgCurrentPage = 1;
let pgRowsPerPage  = 10;
let pgTotalPage = 0;

/* ================= MASTER CHANGE ================= */
 
//snz

function loadProducts() {

    console.log("Loading Products...");
	console.log("baseurl =", baseurl);
    let productDropdown = document.getElementById("product");

    productDropdown.innerHTML =
        '<option value="">Select Product</option>';

    fetch(baseurl + "/DMAPayoutWeb3/products", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(res => {

        if (!res.ok) {
            throw new Error("Unable to load products");
        }
		console.log("baseurl =", baseurl);
        return res.json();
    })
    .then(data => {

        console.log("Products :", data);

        data.forEach(function(item) {

            let option = document.createElement("option");
            option.value = item;
            option.text = item;

            productDropdown.appendChild(option);

        });

    })
    .catch(err => {
        console.error(err);
        alert("Unable to load products.");
    });

}

function loadSubProduct() {

    currentProduct = document.getElementById("product").value;

    let subProductDropdown = document.getElementById("subProduct");
    let masterDropdown = document.getElementById("master");
    let statusDropdown = document.getElementById("status");

    subProductDropdown.innerHTML =
        '<option value="">Select Sub Product</option>';

    masterDropdown.innerHTML =
        '<option value="">Select Master</option>';

    statusDropdown.innerHTML =
        '<option value="">Select Status</option>';

    if (!currentProduct) {
        return;
    }

    fetch(baseurl + "/DMAPayoutWeb3/subProducts?product=" + encodeURIComponent(currentProduct))
        .then(res => {
            if (!res.ok) {
                throw new Error("Unable to load sub products");
            }
            return res.json();
        })
        .then(data => {

            console.log("Sub Products :", data);

            data.forEach(function(item) {

                let option = document.createElement("option");
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

/* ================= LOAD MASTER ================= */

function loadMaster() {

    currentSubProduct =
        document.getElementById("subProduct").value;

    let masterDropdown =
        document.getElementById("master");

    let statusDropdown =
        document.getElementById("status");

    masterDropdown.innerHTML =
        '<option value="">Select Master</option>';

    statusDropdown.innerHTML =
        '<option value="">Select Status</option>';

    if (!currentProduct || !currentSubProduct) {
        return;
    }
	
	console.log("baseurl=",baseurl);

    fetch(baseurl +
        "/DMAPayoutWeb3/masters?product=" +
        encodeURIComponent(currentProduct) +
        "&subProduct=" +
        encodeURIComponent(currentSubProduct))

        .then(res => {

            if (!res.ok) {
                throw new Error("Unable to load masters");
            }

            return res.json();

        })

        .then(data => {

            data.forEach(function(master){

                let option = document.createElement("option");

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


/*function loadSubProduct() {

    currentProduct = document.getElementById("product").value;

    let subProductDropdown = document.getElementById("subProduct");
    let masterDropdown = document.getElementById("master");
    let statusDropdown = document.getElementById("status");

    subProductDropdown.innerHTML =
        '<option value="">Select Sub Product</option>';

    masterDropdown.innerHTML =
        '<option value="">Select Master</option>';

    statusDropdown.innerHTML =
        '<option value="">Select Status</option>';

    if (currentProduct === "Collection - Agency") {
        subProductDropdown.innerHTML +=
            '<option value="Recovery Credit Card">Recovery Credit Card</option>';
    }

    else if (currentProduct === "Vehicle loan") {
        subProductDropdown.innerHTML +=
            '<option value="Vehicle">Vehicle</option>';
    }
}*/

/*function onMasterChange(val) {
	
	console.log("on Master Change");
    currentMaster = val.value.trim();
 	isEditOpen = false;
	//var master = document.getElementById("master");
    var radio = document.getElementById("uploadLocal");
    var fileSection = document.getElementById("input_files");
	var fileInput = document.getElementById("chooseFileInput");
	var fileNameDiv = document.getElementById("noFileInput");
	var addRowButton= document.getElementById("addNewRow");

	if(currentMaster ===""){
		radio.disabled=true;
		radio.checked=false
		fileSection.style.display = "none";
		radioState=false;
		
		clearTable();
		
		if(addRowButton){
			addRowButton.style.display="none";
			}
		
		return;
	}else{
		radio.disabled=false;
		
		
   		if(addRowButton){
			addRowButton.style.display="inline-block";
			}
	}
	
	
	
	// always reset on masterchange
	radio.checked=false
	radioState=false;
	
	// hide file selection section
	fileSection.style.display = "none";
	
	// clear selected file
    fileInput.value = "";

    // Reset file name display
    fileNameDiv.innerHTML = "No file chosen...";
    fileNameDiv.style.color = "red";
     
	
	

    console.log(currentMaster);
    clearTable();
    loadTable();
}*/

function onMasterChange(val) {
	
	console.log("on Master Change");
    currentMaster = val.value.trim();
    
    if(!currentMaster || currentMaster === "Select Master" || currentMaster === ""){
     	document.getElementById("status").innerHTML =`<option value="" selected="selected">Select Status</option>`;
		return;
	}
    
    document.getElementById("status").innerHTML =
     `	<option value="" selected="selected">Select Status</option>
		<option value="All" class="ng-binding ng-scope">All</option>
		<option value="P" class="ng-binding ng-scope">Pending</option>
		<option value="A" class="ng-binding ng-scope">Approved</option>
    `;
}

function onStatusChange(val) {
	
	console.log("on Master Change");
	clearTable();
	
    status = val.value.trim();

 	isEditOpen = false;
	/*var master = document.getElementById("master");*/
    var radio = document.getElementById("uploadLocal");
    var fileSection = document.getElementById("input_files");
	var fileInput = document.getElementById("chooseFileInput");
	var fileNameDiv = document.getElementById("noFileInput");
	var addRowButton= document.getElementById("addNewRow");

	if(currentMaster ===""){
		radio.disabled=true;
		radio.checked=false
		fileSection.style.display = "none";
		radioState=false;
		
		clearTable();
		
		if(addRowButton){
			addRowButton.style.display="none";
			}
		
		return;
	}else{
		radio.disabled=false;
		
		
   		if(addRowButton){
			addRowButton.style.display="inline-block";
			}
	}
	
	
	
	// always reset on masterchange
	radio.checked=false
	radioState=false;
	
	// hide file selection section
	fileSection.style.display = "none";
	
	// clear selected file
    fileInput.value = "";

    // Reset file name display
    fileNameDiv.innerHTML = "No file chosen...";
    fileNameDiv.style.color = "red";
     
	
	

    console.log(currentMaster);
    clearTable();
    loadTable();
}
 
/* ================= LOAD TABLE ================= */
 
function loadTable() {
    console.log("on Load Table");
    /*if (!currentMaster){
	return;
    }*/ 
 	
  //  let loadApi = baseurl + getLoadApi();
  
  //snz
	//let url = `/DMAPayoutWeb3/vhlMaster?master=${encodeURIComponent(currentMaster)}&status=${encodeURIComponent(status)}`;
	//let loadApi = baseurl + url;
	
	let api = getStatusApi();

		if (!api) {
		    alert("No API configured for " + currentMaster);
		    return;
		}

		let loadApi = "";

		if (api.includes("/vhlMaster")) {
		    loadApi = baseurl + api + "&status=" + encodeURIComponent(status);
		} else {
		    loadApi = baseurl + api + "?status=" + encodeURIComponent(status);
	}
	//snz -- end
	
	console.log(baseurl);
	console.log(loadApi);
	
//	let loadApi = getLoadApi();
    let headers = getHeaders();
    let columns = getColumns();

    if (!loadApi) {
        alert("Load API not found for : " + currentMaster);
        return;
    }
    var table = document.getElementById("masterTable");
    
    if(currentMaster === "Channel Master"){
    table.style.width = "max-content";
    }
    else{
     table.style.width = "-webkit-fill-available";
    }
 
        fetch(loadApi, {
		method: "GET",
		headers : {"Content-Type": "application/json"}
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
                alert("No data received from API");
      
                return;
            }
 			
			tableData=data;
			tableColumns=columns;
		
			pgdata=[...data];
			pgFilteredData=[...data];
			pgCurrentPage=1;
			
            createHeader(headers);


			renderPaginatedTable();
			
            /*createBody(data, columns);*/

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
   	 //headers.forEach(h => row += "<th >" + h + "</th>");
	 headers.forEach(h => {
	        row += `
	            <th style="
	                white-space: nowrap;
	                min-width: 140px;
	                padding: 10px;
	            ">
	                ${h}
	            </th>`;
	    });
    row += "<th>Action</th></tr>";
 
    thead.innerHTML = row;
}
 

/* ================= GET PRIMARY KEY FIELD BY MASTER ================= */

/*function getPrimaryKeyByMaster(){
	
	console.log("get column method call");
    if (currentMaster === "GST Master")
        return "apsCode";
 
    if (currentMaster === "Branch Master")
        return "branchCode";
    if (currentMaster === "Channel Master")
        return "apsCode";
    if (currentMaster === "Model Master")
        return "tempId";

	//snz
	if (currentMaster === "SAP Master")
	    return "unitCode";	
		
	if (currentMaster === "Recovery City Master")
	    return "city";

	if (currentMaster === "Payment Code Master")
	    return "code";

	if (currentMaster === "Hold Code Master")
	    return "code";
}*/

/* ================= CREATE BODY ================= */
 
function createBody(data, columns) {
	console.log("create Body method call");
    let tbody = document.getElementById("tbody");
    tbody.innerHTML = "";
 
	let pkField = getPrimaryKeyByMaster();
	
    data.forEach(obj => {
	
	let pkValue = obj[pkField];
	
       // let row = `<tr data-pk-field = "${pkField}" data-pk-value="${pkValue}">`;
 		
	let row = `<tr
        data-pk-field="${pkField}"
        data-pk-value="${pkValue}"
        data-original='${JSON.stringify(obj).replace(/'/g, "&apos;")}'>`;

        columns.forEach(col => {
            row += "<td>" + (obj[col] || "") + "</td>";
        });
/* onclick="editRow(this)"*/
		
//snz
		/*if(currentMaster === "SAP Master"  || 
			currentMaster === "Payment Code Master" || 
			currentMaster === "Hold Code Master"){
		    row += `<td>
		           <a href="#"
		              class="btn btn-link btn-sm"
		              onclick="editRow(this)">Edit</a>
		        </td>`;
		} else*/
		
		if(currentMaster ==="Payment Code Master") {
			row+=`<td>
			<span style="color:gray;">N/A</span>
			</td>`;
		}
		else if(obj.status === "P" || obj.status === "R"){
		    row += `<td>
		           <a href="#"
		              class="btn btn-link btn-sm"
		              onclick="showPendingPopup(this,'${obj.status}')">Edit</a>
		        </td>`;
		}
		else{
		    row += `<td>
		           <a href="#"
		              class="btn btn-link btn-sm"
		              onclick="editRow(this)">Edit</a>
		        </td>`;
		}
		
		/* if(obj.status === "P" || obj.status === "R"){
			row += `<td>
                   <a href="#" class="btn btn-link btn-sm"  onclick="showPendingPopup(this,'${obj.status}')" disable>Edit</a>
                </td>`;
		}
		else{
			        row += `<td>
                   <a href="#" class="btn btn-link btn-sm" onclick="editRow(this)">Edit</a>
                </td>`;
		} */
 
		/*snz end */
 
 
        row += "</tr>";
        tbody.innerHTML += row;
    });
}
 


/* ================= ADD New ROW ================= */
 
function addRow() {
	console.log("Add new Row method Called")
  if (isEditOpen) { alert("Finish current edit/add first"); return; }

    let columns = getColumns();
    let tbody = document.getElementById("tbody");

	let table = document.getElementById("masterTable");
	let thead = document.getElementById("thead");
 
    // ======================================
    // CASE: Table hidden (No data returned)
    // ======================================
    if (table && table.style.display === "none") {
 
        // Show table
        showTableStructure(currentMaster);
 
        // If header not created → use OLD method
        if (thead && thead.innerHTML.trim() === "") {
 
            let headers = getHeaders();   // Your existing method
            createHeader(headers);        //  USE OLD LOGIC
        }
    }


    /*let row = "<tr>";*/
    const row = document.createElement("tr");
    row.classList.add("new-row");

    columns.forEach((col, index) => {
        /*row += `<td><input type="text" class="form-control form-control-sm"/></td>`;*/
       const td = document.createElement("td");

		// Non editable column and default status , exceptional 'Ilens Channel Master'
		if (
    	(currentMaster !== "Ilens Channel Master" && col === "status") ||
    	(currentMaster === "Ilens Channel Master" && col === "statusA")
		) {
		//if(col === "status"){
			td.innerHTML = `<input class="form-control form-control-sm" value="P" readonly/>`;
		}
		else{td.innerHTML = `<input class="form-control form-control-sm"/>`;
		}
        row.appendChild(td);
    });


   const actionTd = document.createElement("td");
    actionTd.innerHTML = `
        <button class="btn btn-success btn-sm" onclick="submitNewRow(this)">Submit To Cheker</button>
        <button class="btn btn-secondary btn-sm ml-1" onclick="cancelNewRow(this)">Cancel</button>
    `;
    row.appendChild(actionTd);
 
    tbody.insertBefore(row, tbody.firstChild);
    isEditOpen = true; 

}
 
/* ================= SUBMIT New RECORD ================= */
 
function submitNewRow(btn) {
 	
    let tr = btn.closest("tr");
    let inputs = tr.querySelectorAll("input");
    let columns = getColumns();
 
    let obj = {};
 
    for (let i = 0; i < inputs.length; i++) {
        obj[columns[i]] = inputs[i].value.trim();
    }

	let primaryKeyField=columns[0];
	let primaryValue =obj[primaryKeyField];
	
	

	if(!primaryValue || primaryValue ===""){
		alert(primaryKeyField + " is Required");
		return;
	}
	
    /*fetch(getSaveApi(), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(obj)
    }).then(() => {
        loadTable();
    });*/

	btn.dsabled = true ;
	btn.innerHTML="Submitting...";
	
	
    fetch(baseurl+getSaveApi(), {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
			"user":getLoggedUser()
        },
        body: JSON.stringify(obj)
    })
    .then(res => {
        return res.text().then(text => {
            return {
                status: res.status,
                message: text
            };
        });
    })
    .then(result => {
 
        if (result.status === 200) {
 
            alert(result.message);
 
            isEditOpen = false;
            loadTable();  // reload table
 
        } else {
 
            alert(result.message);
            btn.disabled = false; // enable again
        }
    })
    .catch(err => {
 
        alert("Server Error : " + err.message);
        btn.disabled = false;
    });

isEditOpen = false;
}

/* ================= CANCEL NEW RECORD ================= */
 
function cancelNewRow(rowNum) {
	rowNum.closest("tr").remove(); 
	isEditOpen = false;
   /* loadTable();*/
}



/*function getNonEditableColumns(){
	
	 if (currentMaster === "GST Master")
        return ["status","apsCode","name"];
 
    if (currentMaster === "Branch Master")
        return ["status","branchCode","branchName"];
    if (currentMaster === "Channel Master")
        return ["status","apsCode","nameOfChannelAsPerAgmt","iBoxId","panNo"];
    if (currentMaster === "Model Master")
        return ["status", "manufacturerId", "manufacturerDesc", "modelId","modelDesc"];

	//snz
    if (currentMaster === "SAP Master")
        return ["status", "unitCode"];

    if (currentMaster === "Recovery City Master")
        return ["status", "city"];

    if (currentMaster === "Payment Code Master")
        return ["status", "code"];

    if (currentMaster === "Hold Code Master")
        return ["status", "code"];
	
}*/









/* ================= EDIT ROW ================= */
 
function editRow(btn) {
	if (isEditOpen) { alert("Finish current edit/add first"); return; }
	
    let tr = btn.closest("tr");
 	oldRowHtml = tr.innerHTML;

	let pkField = tr.dataset.pkField;
	let columns = getColumns();
	let skipEditColumn = getNonEditableColumns();
    let tds = tr.querySelectorAll("td");
	 
	
 
	for (let i = 0; i < columns.length; i++) {
			let columnName = columns[i];
			
			/*// skip primary column for edit 
			if(columnName === pkField){
				continue;
			}*/
						
			// skip any column for edit
			if(skipEditColumn.includes(columnName)){
				continue;
			}
			
        let val = tds[i].innerText;

        tds[i].innerHTML = `<input type="text" class="form-control form-control-sm" value="${val}"/>`;
    }

 
    tds[columns.length].innerHTML = `
        <button class="btn btn-success btn-sm" onclick="submitEditRow(this)">Submit To Checker</button>
        <button class="btn btn-secondary btn-sm ml-1" onclick="cancelEdit(this)">Cancel</button>
    `;
   isEditOpen = true;
}
 


/* ================= SUBMIT EDITED RECORD ================= */
function submitEditRow(btn) {
 
    let tr = btn.closest("tr");

	let pkField = tr.dataset.pkField;
	let pkValue = tr.dataset.pkValue;

	let columns = getColumns();
	let tds=tr.querySelectorAll("td");
	
    let skipEditColumns = getNonEditableColumns();
	let original = JSON.parse(tr.dataset.original);
	

 
    let obj = {};
	let isChanged = false;

	
	if(!pkValue){
		alert(pkField + " is missing");
		return;
	}

	obj[pkField]=pkValue;
	
	for (let i = 0; i < columns.length; i++) {
		let columnName = columns[i];
		
		// skip primary key
		if(columnName === pkField) continue;
		
		let input = tds[i].querySelector("input");
		
		let newValue;
		
		if(input){
			newValue = input.value.trim();
		}
        else{
			newValue = tds[i].innerText.trim();
		}
		let oldValue = original[columnName];
		
		if(String(newValue ?? "").trim().toLowerCase() !== String(oldValue ?? "").trim().toLowerCase()){
			isChanged=true;
		}
		
		obj[columnName]=newValue;
		
    }

	if(!isChanged){
		 alert("No changes detected");
		return;
	}

 
    btn.disabled = true;
	let originalText = btn.innerHTML;
    btn.innerHTML = "Submitting...";
 
    fetch(baseurl+getUpdateApi(), {   // update API call
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "user": getLoggedUser()   // header value
        },
        body: JSON.stringify(obj)
    })
    .then(res => {
        return res.text().then(text => ({
            status: res.status,
            message: text
        }));
    })
    .then(result => {
 
        if (result.status === 200) {
            alert(result.message);
            isEditOpen = false;   // 🔥 edit closed after success
            loadTable();          // reload table
 
        } else {
 
            alert(result.message);
            btn.disabled = false;
            btn.innerHTML=originalText;
        }
 
    })
    .catch(err => {
        alert("Server Error : " + err.message);
        btn.disabled = false;
		btn.innerHTML=originalText;"Submit To Checker";
    });

//isEditOpen = false;
}
 

/* ================= CANCEL EDIT ================= */
function cancelEdit(btn) {
    const row = btn.closest("tr");
    row.innerHTML = oldRowHtml;
    oldRowHtml = null;
    isEditOpen = false;
}






function clearTable() {
    document.getElementById("thead").innerHTML = "";
    document.getElementById("tbody").innerHTML = "";

	pgData=[];
	pgFilterdData=[];
	pgCurrentPage=1;
	pgTotalPage=0;
	
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

  /* var addRowButton= document.getElementById("addNewRow");
   if(addRowButton)
        addRowButton.style.display= "none";*/

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

/*	let info = document.getElementById("paginationInfo");
    if (info) info.innerHTML = "";
 
    let container = document.getElementById("paginationContainer");
    if (container) container.innerHTML = "";*/
 
    // 5️⃣ Hide pagination wrapper
    let wrapper = document.getElementById("paginationWrapper");
    if (wrapper) wrapper.style.display = "flex";

}

function showPendingPopup(btn,status) {
 
    // Button position
    let rect = btn.getBoundingClientRect();
 	let message = "";

	if(status === "P"){
		message = "Already waiting for approval";
	}
	else if(status === "R"){
		message = "Already Rejected";
	}
    let popup = document.createElement("div");
    //popup.innerText = "Already waiting for approval";
	popup.innerText = message;
 
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
    popup.style.left = rect.left + window.scrollX - 80 + "px";
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




// redio button 

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
 
    fileNameDiv.style.color = "green";
    fileNameDiv.innerHTML = file.name;
}












// ================= MAIN PAGINATION FUNCTION =================
function renderPaginatedTable() {
 
    // If no data
    if (!pgFilteredData || pgFilteredData.length === 0) {
        document.getElementById("tbody").innerHTML = "";
        document.getElementById("paginationContainer").innerHTML = "";
        document.getElementById("paginationInfo").innerHTML = "";

		let wrapper = document.getElementById("paginationWrapper");
		if(wrapper) wrapper.style.display.display = "none";
	
        return;
    }
 
	let wrapper = document.getElementById("paginationWrapper");
	if(wrapper) wrapper.style.display.display = "flex";
	
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
//added for template download (ban502236)-start
function downloadTemplate() {
	 
    var masterType = document.getElementById("master").value;
 
    if (!masterType) {
        alert("Please select master");
        return;
    }
    
    window.location.href = baseurl + "/mainPage/downloadTemplate?masterType=" + encodeURIComponent(masterType);
}
////added for template download (ban502236)-end 

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

function getLoggedUser(){
	return localStorage.getItem("user_id");
		/*return 'BAN49380';*/
	} 
 