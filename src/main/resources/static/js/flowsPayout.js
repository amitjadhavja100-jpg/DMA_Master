/* flowsPayout.js */

let flowsResolutions = [];
let flowsPerformances = [];
let flowsFooters = []; //sn
let flowsPayoutMap = {};

var userId = localStorage.getItem("user_id");


function initFlowsScreen(){

    document.getElementById("flowsBody").innerHTML = "";
    document.getElementById("flowsPerformanceRow").innerHTML = "";
    document.getElementById("flowsCategory").innerHTML ="<option value=''>Select Category</option>"; // Reset Category
    document.getElementById("flowsBucket").innerHTML = "<option value=''>Select Bucket</option>";// Reset Bucket
    document.getElementById("flowsFromDate").value = "";
    document.getElementById("flowsToDate").value = "";
	clearFlowsFooter();//reset footer
	

    flowsResolutions = [];
    flowsPerformances = [];
	flowsFooters = []; //sn
    flowsPayoutMap = {};

    loadFlowsCategories();
}

function loadFlowsCategories(){

    fetch(
        CONTEXT_PATH +
        "/flowsPayout/categories"
    )
    .then(function(response){
        if(!response.ok){
            throw new Error( "Unable to load categories" );
        }
        return response.json();
    })
    .then(function(data){

        let category = document.getElementById("flowsCategory");

        category.innerHTML =
            "<option value=''>" +
            "Select Category" +
            "</option>";

        data.forEach(function(c){
            category.innerHTML +=
                "<option value='" +
                c.id +
                "'>" +
                c.categoryName +
                "</option>";

        });

    })
    .catch(function(error){

        console.error(
            "Category Error:",
            error
        );

        alert(
            "Unable to load Category"
        );

    });
}

function handleFlowsCategoryChange() {

    let categorySelect = document.getElementById("flowsCategory");
    let cityContainer = document.getElementById("flowsCityContainer");
    let citySelect = document.getElementById("flowsCity");
    let bucket = document.getElementById("flowsBucket");
    let categoryId = categorySelect.value;

    // Reset city
    citySelect.innerHTML = "<option value=''>Select City</option>";
    citySelect.value = "";

    // Reset bucket
    bucket.innerHTML = "<option value=''>Select Bucket</option>";

    // Clear table
    document.getElementById("flowsBody").innerHTML = "";
    document.getElementById("flowsPerformanceRow").innerHTML = "";

    clearFlowsFooter();

    flowsResolutions = [];
    flowsPerformances = [];

    // No category selected
    if (categoryId === "") {
        cityContainer.style.display = "";
        citySelect.required = true;
        return;
    }

    let selectedOption =
        categorySelect.options[
            categorySelect.selectedIndex
        ];

    let categoryName = selectedOption.text.trim().toUpperCase();
    console.log("Selected Category:", categoryName);

    // CFP
    if (categoryName === "CFP") {

        console.log("CFP selected - hiding city");
        cityContainer.style.display = "none";

        citySelect.required = false;
        citySelect.value = "";

        // CFP does not require city
        loadFlowsBuckets();

    }

    // OTHER CATEGORIES
    else {

        console.log("Non-CFP selected - showing city");
        cityContainer.style.display = "";
        citySelect.required = true;
        loadFlowsCities();
    }
}

function loadFlowsCities() {

    let categoryId =
        document.getElementById("flowsCategory").value;

    let city =
        document.getElementById("flowsCity");

    city.innerHTML =
        "<option value=''>Select City</option>";

    if (categoryId === "") {
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/flowsPayout/cities?categoryId=" +
        encodeURIComponent(categoryId)
    )
    .then(function(response) {

        if (!response.ok) {
            throw new Error("Unable to load cities");
        }

        return response.json();
    })
    .then(function(data) {

        data.forEach(function(c) {

            city.innerHTML +=
                "<option value='" +
                c.id +
                "'>" +
                c.cityName +
                "</option>";
        });
    })
    .catch(function(error) {

        console.error("City Error:", error);
        alert("Unable to load City");
    });
}

/*function loadFlowsCities(){

    let categoryId =document.getElementById("flowsCategory").value;

    let city =document.getElementById("flowsCity");

    let bucket =document.getElementById("flowsBucket");

    // Reset City
    city.innerHTML =
        "<option value=''>" +
        "Select City" +
        "</option>";

    // Reset Bucket
    bucket.innerHTML =
        "<option value=''>" +
        "Select Bucket" +
        "</option>";

    // Clear payout table
    document.getElementById("flowsBody").innerHTML = "";
    document.getElementById("flowsPerformanceRow").innerHTML = "";
	clearFlowsFooter();//reset footer

    flowsResolutions = [];
    flowsPerformances = [];

    if(categoryId === ""){
        return;
    }

    //console.log("Loading cities for categoryId =",categoryId);

    fetch(
        CONTEXT_PATH +
        "/flowsPayout/cities?categoryId=" +
        encodeURIComponent(categoryId)
    )
    .then(function(response){

        if(!response.ok){
            throw new Error("Unable to load cities");
        }
        return response.json();
    })
    .then(function(data){

        //console.log("FLOWS CITIES =",data);
        data.forEach(function(c){

            city.innerHTML +=
                "<option value='" +
                c.id +
                "'>" +
                c.cityName +
                "</option>";
        });
    })
    .catch(function(error){
        console.error("City Error:",error);
        alert("Unable to load City");
    });
}*/

function handleFlowsCityChange() {

    let categoryId = document.getElementById("flowsCategory").value;

    let cityId = document.getElementById("flowsCity").value;

    if (categoryId === "") {
        return;
    }

    if (cityId === "") {
        return;
    }
    loadFlowsBuckets();
}

function loadFlowsBuckets() {

    let categoryId = document.getElementById("flowsCategory").value;

    let cityId = document.getElementById("flowsCity").value;

    let bucket = document.getElementById("flowsBucket");

    bucket.innerHTML =
        "<option value=''>Select Bucket</option>";

    document.getElementById("flowsBody").innerHTML = "";
    document.getElementById("flowsPerformanceRow").innerHTML = "";

    clearFlowsFooter();

    flowsResolutions = [];
    flowsPerformances = [];

    if (categoryId === "") {
        return;
    }

    let categorySelect = document.getElementById("flowsCategory");

    let selectedOption =
        categorySelect.options[
            categorySelect.selectedIndex
        ];

    let categoryName = selectedOption.text.trim().toUpperCase();

    let url =
        CONTEXT_PATH +
        "/flowsPayout/buckets?categoryId=" +
        encodeURIComponent(categoryId);

    // CITY BASED CATEGORY
    if (categoryName !== "CFP") {

        if (cityId === "") {
            return;
        }

        url +=
            "&cityId=" +
            encodeURIComponent(cityId);
    }

    // CFP category
    // For CFP, cityId is NOT added
    fetch(url)
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Unable to load buckets");
            }
            return response.json();
        })
        .then(function(data) {
            data.forEach(function(b) {
                bucket.innerHTML +=
                    "<option " +
                    "value='" + b.id + "' " +
                    "data-order-id='" + b.orderId + "' " +
                    "data-table-type='" +
                    (b.tableType || "") +
                    "'>" +
                    b.bucketName +
                    "</option>";
            });

        })
        .catch(function(error) {
            console.error("Bucket Error:", error);
            alert("Unable to load Bucket");
        });
}

function loadFlowsStructure(){

    let bucketId =document.getElementById("flowsBucket").value;
	
	let bucketSelect =document.getElementById("flowsBucket");
			
    let selectedOption =bucketSelect.options[bucketSelect.selectedIndex];

    let orderId =selectedOption.getAttribute("data-order-id");


    if(bucketId === ""){
        return;
    }

    Promise.all([
        fetch(
            CONTEXT_PATH +
            "/flowsPayout/resolution" +
            "?bucketId=" +
            encodeURIComponent(bucketId)
        )
        .then(function(response){

            if(!response.ok){
                throw new Error(
                    "Unable to load Resolution"
                );
            }

            return response.json();
        }),
        fetch(
            CONTEXT_PATH +
            "/flowsPayout/performance" +
            "?bucketId=" +
            encodeURIComponent(bucketId)
        )
        .then(function(response){

            if(!response.ok){
                throw new Error("Unable to load Performance");
            }
            return response.json();
        }),
		
        fetch(
            CONTEXT_PATH +
            "/flowsPayout/footers" +
            "?bucketId=" +
            encodeURIComponent(bucketId) +
            "&orderId=" +
            encodeURIComponent(orderId)
        )
            .then(function(response) {

                if (!response.ok) {
                    throw new Error(
                        "Unable to load Footer"
                    );
                }
                return response.json();
            })
    ])
    .then(function(result){

        flowsResolutions = result[0];
        flowsPerformances = result[1];
		flowsFooters = result[2] || [];


        //console.log("FLOWS RESOLUTIONS =",flowsResolutions);
        //console.log("FLOWS PERFORMANCES =",flowsPerformances);
		console.log("FLOWS FOOTERS =",flowsFooters);
		
        renderFlowsTable();
    })
    .catch(function(error){

        console.error("Flows structure error:",error);
        alert("Unable to load Resolution / Performance" );
    });
}


function renderFlowsTable() {

    let header = document.getElementById("flowsPerformanceRow");
    let body = document.getElementById("flowsBody");	
	
	let performanceHeader =document.getElementById("flowsPerformanceHeader");
	performanceHeader.colSpan = flowsPerformances.length;

    header.innerHTML = "";
    body.innerHTML = "";

    flowsPerformances.forEach(function(performance) {
        header.innerHTML +=
            "<th data-performance-id='" +
            performance.id +
            "'>" +
            performance.displayText +
            "</th>";

    });

    let bucketSelect = document.getElementById("flowsBucket");
    let selectedOption = bucketSelect.options[ bucketSelect.selectedIndex];
    let bucketId =Number(selectedOption.value);
    let bucketName = selectedOption.text;
	let orderId = Number(selectedOption.getAttribute("data-order-id"));
    let tableType = selectedOption.getAttribute("data-table-type");

   // console.log("Selected Bucket ID =", bucketId);
   // console.log("Selected Bucket Name =", bucketName);
   // console.log("Selected Table Type =", tableType);
   
    getFlowsTableHeading(orderId);

    document.getElementById("flowsPerformanceHeader").innerText = tableType;

    flowsResolutions.forEach(function(resolution) {

        let tr = document.createElement("tr");
        let label = document.createElement("td");

        label.innerText = resolution.displayText;
        label.setAttribute( "data-resolution-id",resolution.id );
        tr.appendChild(label);

        flowsPerformances.forEach(
            function(performance) {

                let td = document.createElement("td");
                let input = document.createElement("input");

                input.type = "number";
                input.step = "0.01";
                input.min = "0";

                input.className = "form-control flows-payout-input";
                input.dataset.resolutionId = resolution.id;
                input.dataset.performanceId = performance.id;
                td.appendChild(input);
                tr.appendChild(td);
            }
        );
        body.appendChild(tr);
    });
	renderFlowsFooter();
}


function renderFlowsFooter() {

    let body = document.getElementById("flowsBody");

    if (!body) {
        return;
    }

    // No footer
    if (!flowsFooters || flowsFooters.length === 0) {
        return;
    }

    flowsFooters.forEach(function(footer, footerIndex) {

        let tr = document.createElement("tr");

        tr.className = "flows-footer-row";

        let footerType = (footer.footerType || "").trim().toUpperCase();

        let footerText = footer.footerText || "";

  
        tr.setAttribute( "data-source-footer-id",footer.id);

        tr.setAttribute("data-footer-index",footerIndex);

        // INCENTIVE FOOTER
        if (footerType === "INCENTIVE") {

            let labelTd =document.createElement("td");

            labelTd.innerText = footerText;
            labelTd.className ="flows-footer-label";
            labelTd.setAttribute("data-footer-index",footerIndex);
            labelTd.setAttribute("data-source-footer-id",footer.id);

            tr.appendChild(labelTd);

            flowsPerformances.forEach(
                function(performance) {

                    let td = document.createElement("td");
                    let input = document.createElement("input");
                    input.type = "number";
                    input.step = "0.01";
                    input.min = "0";
                    input.className = "form-control flows-footer-value";

                    input.dataset.footerIndex = footerIndex;
                    input.dataset.sourceFooterId = footer.id;
                    input.dataset.performanceId = performance.id;

                    input.value = "";
                    input.disabled = false;	

                    td.appendChild(input);
                    tr.appendChild(td);
                }
            );
        }

        // NON-INCENTIVE FOOTER
        else {

            let td = document.createElement("td");

            td.colSpan = flowsPerformances.length + 1;
            td.className = "flows-footer-full-row";


            let textSpan = document.createElement("span");

            textSpan.innerText = footerText;
            textSpan.className = "flows-footer-text";

            td.appendChild(textSpan);
            tr.appendChild(td);
        }

        body.appendChild(tr);

    });
}


function getFlowsPerformanceText(performanceId){

    if(performanceId === null || performanceId === undefined){
        return "";
    }

    let performance = flowsPerformances.find(function(p){
                return Number(p.id) ===Number(performanceId);
            }
        );
		
    if(!performance){
        return "";
    }
    return performance.displayText || "";
}

function clearFlowsFooter() {
    flowsFooters = [];
}

function getFlowsTableHeading(orderId) {

    if (orderId === 5 || orderId === 6) {
        document.getElementById("flowsLeftHeader").innerText = "BASE";
    } else {
        document.getElementById("flowsLeftHeader").innerText = "Resolution";
    }
}


function fetchFlows() {

    let product = document.getElementById("product").value;
    let subProduct = document.getElementById("subProduct").value;
    let categoryId = document.getElementById("flowsCategory").value;
    let cityId = document.getElementById("flowsCity").value;
    let bucketId = document.getElementById("flowsBucket").value;
    let fromDate = document.getElementById("flowsFromDate").value;
    let toDate = document.getElementById("flowsToDate").value;

	if(categoryId === "") {
	    alert("Please Select Category");
	    return;
	}

    // Check whether CFP
    let categorySelect = document.getElementById("flowsCategory");

    let selectedOption =
        categorySelect.options[
        categorySelect.selectedIndex
        ];

    let categoryName = selectedOption.text.trim().toUpperCase();

    // City required ONLY for non-CFP
	if (categoryName !== "CFP" && cityId === "") {
	    alert("Please Select City");
	    return;
	}

    if(bucketId === "") {
        alert("Please Select Bucket");
        return;
    }

    if(fromDate === "") {
        alert("Select From Date");
        return;
    }

    if(toDate === "") {
        alert("Select To Date");
        return;
    }

    if(new Date(fromDate) > new Date(toDate)) {
        alert(  "From Date cannot be greater than To Date");
        return;
    }

	let url =
	       CONTEXT_PATH +
	       "/flowsPayout/fetch" +
	       "?product=" +
	       encodeURIComponent(product) +
	       "&subProduct=" +
	       encodeURIComponent(subProduct) +
	       "&categoryId=" +
	       encodeURIComponent(categoryId);

	   // City only for non-CFP
	   if (categoryName !== "CFP") {
	       url +="&cityId=" +
	           encodeURIComponent(cityId);
	   }
	   url +=
	       "&bucketId=" +
	       encodeURIComponent(bucketId) +
	       "&fromDate=" +
	       encodeURIComponent(fromDate) +
	       "&toDate=" +
	       encodeURIComponent(toDate);

	   fetch(url)
	       .then(async function(response) {
	           let text = await response.text();
	           if (text === "") {
	               clearFlowsInputs();
	               alert("No Approved Record Found");
	               return null;
	           }
	           return JSON.parse(text);
	       })
    .then(function(data) {

        if(data === null) {
            return;
        }
        fillFlowsPayout(data);
    })
    .catch(function(error) {
        console.error("Fetch Flows Error:",error);
        alert(error.message);
    });
}


function fillFlowsPayout(data) {

    if (!data) {
        return;
    }

    // PAYOUT DETAILS
    if (data.details && data.details.length > 0) {

        data.details.forEach(function(item) {

            let input =
                document.querySelector(
                    "#flowsBody input" +
                    "[data-resolution-id='" +
                    item.resolutionId +
                    "']" +
                    "[data-performance-id='" +
                    item.performanceId +
                    "']"
                );

            if (input) {

                input.value =
                    item.payoutPercent;

            } else {

                console.warn(
                    "Input not found for Resolution:",
                    item.resolutionId,
                    "Performance:",
                    item.performanceId
                );
            }

        });

    } else {

        console.log("No payout details found");
    }

    // FOOTER DETAILS
    if (!data.footers || data.footers.length === 0) {
        console.log("No footer details found");
        return;
    }

    data.footers.forEach(function(footer) {

        if (footer.sourceFooterId == null) {
            console.warn(
                "sourceFooterId missing in footer:",
                footer
            );
            return;
        }

        // INCENTIVE FOOTER
        if (footer.performanceId != null) {

            let input =
                document.querySelector(
                    "#flowsBody input.flows-footer-value" +
                    "[data-source-footer-id='" +
                    footer.sourceFooterId +
                    "']" +
                    "[data-performance-id='" +
                    footer.performanceId +
                    "']"
                );

            if (input) {

                input.value =
                    footer.footerValue != null
                        ? footer.footerValue
                        : "";

                // ALWAYS ENABLE
                input.disabled = false;

                console.log(
                    "Footer value filled:",
                    "sourceFooterId =",
                    footer.sourceFooterId,
                    "performanceId =",
                    footer.performanceId,
                    "value =",
                    footer.footerValue
                );

            } else {

                console.warn(
                    "Footer input not found:",
                    "sourceFooterId =",
                    footer.sourceFooterId,
                    "performanceId =",
                    footer.performanceId
                );
            }

        }
        // NON-INCENTIVE FOOTER
        else {

            let input =
                document.querySelector(
                    "#flowsBody input.flows-footer-full-input" +
                    "[data-source-footer-id='" +
                    footer.sourceFooterId +
                    "']"
                );

            if (input) {

                input.value =
                    footer.footerValue != null
                        ? footer.footerValue
                        : "";

                console.log(
                    "Non-incentive footer value filled:",
                    "sourceFooterId =",
                    footer.sourceFooterId,
                    "value =",
                    footer.footerValue
                );

            } else {

                console.warn(
                    "Non-incentive footer input not found:",
                    "sourceFooterId =",
                    footer.sourceFooterId
                );
            }
        }

    });
}

function saveFlows(){

    let product =document.getElementById("product").value;
    let subProduct =document.getElementById("subProduct").value;
    let categoryId =document.getElementById("flowsCategory").value;
    let cityId =document.getElementById("flowsCity").value;
    let bucketId = document.getElementById("flowsBucket").value;
    let fromDate =document.getElementById("flowsFromDate").value;
    let toDate = document.getElementById("flowsToDate").value;


    if(product === ""){
        alert("Please Select Product");
        return;
    }

    if(subProduct === ""){
        alert("Please Select Sub Product");
        return;
    }

    if(categoryId === ""){
        alert("Please Select Category");
        return;
    }

	let categorySelect = document.getElementById("flowsCategory");

	let selectedOption =
	    categorySelect.options[
	        categorySelect.selectedIndex
	    ];

	let categoryName = selectedOption.text.trim().toUpperCase();

	if (categoryName !== "CFP" && cityId === "") {
	    alert("Please Select City");
	    return;
	}

    if(bucketId === ""){
        alert("Please Select Bucket");
        return;
    }

    if(fromDate === ""){
        alert("Please Select From Date");
        return;
    }

    if(toDate === ""){
        alert("Please Select To Date");
        return;
    }

    if(new Date(fromDate) > new Date(toDate)){
        alert("From Date cannot be greater than To Date");
        return;
    }


	let inputs = document.querySelectorAll("#flowsBody input.flows-payout-input");

    if(inputs.length === 0){
        alert("Please Fetch / Load payout structure first");
        return;
    }

    let payoutList = [];
    let hasValue = false;
	let footerList = [];

    inputs.forEach(function(input){

        let value =input.value.trim();

        if(value !== ""){
            hasValue = true;
        }

        payoutList.push({

            resolutionId:Number(input.dataset.resolutionId),
            performanceId:Number(input.dataset.performanceId),
            payoutPercent:value === ""? 0: Number(value)
        });
    });
	
	let footerRows = document.querySelectorAll(
	    "#flowsBody tr.flows-footer-row"
	);

	footerRows.forEach(function (row) {

	    // data-footer-index is present on the TR itself
	    let footerIndex = Number(
	        row.dataset.footerIndex
	    );

	    if (isNaN(footerIndex)) {
	        return;
	    }

	    let originalFooter = flowsFooters[footerIndex];

	    if (!originalFooter) {
	        return;
	    }

	    let footerType = (
	        originalFooter.footerType || ""
	    ).trim();

	    let footerText = (
	        originalFooter.footerText || ""
	    ).trim();

	    // GET ORDER ID
	    let bucketSelect = document.getElementById( "flowsBucket");
	    let selectedOption = bucketSelect.options[bucketSelect.selectedIndex];
	    let orderId = Number(selectedOption.getAttribute("data-order-id")
	    );

	    // INCENTIVE FOOTER
	    if (footerType.toUpperCase() === "INCENTIVE") {

	        let inputs = row.querySelectorAll(".flows-footer-value");

	        inputs.forEach(function (input) {

	            let performanceId =input.dataset.performanceId;
	            let value = input.value.trim();
				
	            if (value === "") {
	                return;
	            }

	            footerList.push({
	                sourceFooterId:
	                    originalFooter.id != null
	                        ? Number(originalFooter.id)
	                        : null,

	                bucketOrderId: orderId,
	                footerType: footerType,
	                footerText: footerText,
	                performanceId: Number(performanceId),
	                footerValue: Number(value)
	            });
	        });

	    }
	    // NON-INCENTIVE FOOTER
	    else {
            footerList.push({

                sourceFooterId:
                    originalFooter.id != null
                        ? Number(originalFooter.id)
                        : null,

                bucketOrderId: orderId,
                footerType: footerType,
                footerText: footerText,
                performanceId: null,
                footerValue: null
            });
	    }
	});
	
	// VALIDATE FOOTERS
	for (let i = 0; i < footerList.length; i++) {

	    let footer = footerList[i];

	    if (!footer.footerType) {
	        alert("Footer Type is required.");
	        return;
	    }

	    if (!footer.footerText) {
	        alert("Footer Text is required.");
	        return;
	    }

	    if (footer.footerType.toUpperCase() ==="INCENTIVE") {

	        if (footer.performanceId == null) {
	            alert("Performance is required for Incentive Footer." );
	            return;
	        }
	        if (footer.footerValue == null) {
	            alert("Footer Value is required for Incentive Footer.");
	            return;
	        }
	    }
	}
    if(!hasValue){
        alert("Please Enter Payout %");
        return;
    }

    let request = {

        product: product,
        subProduct: subProduct,
        categoryId: Number(categoryId),
        cityId:
            categoryName === "CFP"
                ? null
                : Number(cityId),
        bucketId:Number(bucketId),
        
		tableType:document.getElementById(
                "flowsPerformanceHeader"
            ).innerText,

        fromDate: fromDate,
        toDate: toDate,
        remarks: "",
        payoutList: payoutList,
		footerList: footerList
    };

    console.log("FLOWS SAVE REQUEST =",request);
	
	console.log(
	    "FLOWS FOOTER LIST =",
	    JSON.stringify(
	        footerList,
	        null,
	        2
	    )
	);


    fetch(
        CONTEXT_PATH +
        "/flowsPayout/save?user=" + userId,
        //encodeURIComponent(userId),
        {
            method: "POST",
            headers: {
                "Content-Type":
                    "application/json"
            },
            body:JSON.stringify(request)
        }
    )
    .then(async function(response){

        let msg =await response.text();

        if(!response.ok){
            throw new Error(msg);
        }
        return msg;
    })
    .then(function(msg){
        alert(msg);
    })
    .catch(function(error){
        console.error("Save Flows Error:",error );
        alert(error.message);
    });
}

function resetFlows() {

    document.getElementById("flowsBucket").selectedIndex = 0;
    document.getElementById("flowsCategory").selectedIndex = 0;
	document.getElementById("flowsCity").selectedIndex = 0;

    document.getElementById("flowsFromDate").value = "";
    document.getElementById("flowsToDate").value = "";

    document.getElementById("flowsBody").innerHTML = "";
    document.getElementById("flowsPerformanceRow").innerHTML = "";
	document.getElementById("flowsHeaderText").innerHTML = "";

    flowsResolutions = [];
    flowsPerformances = [];
    flowsPayoutMap = {};
	
}

function clearFlowsInputs() {

    document.querySelectorAll(
        "#flowsBody input"
    ).forEach(function(input) {
        input.value = "";
    });

}

function viewFlowsHistory() {

    window.location.href =
        CONTEXT_PATH +
        "/mainPage/load?master=FLOWS_PAYOUT_HISTORY";

}

function viewFlowsCompare() {

    var product = document.getElementById("product").value.trim();
    var subProduct = document.getElementById("subProduct").value.trim();
    var categoryId = document.getElementById("flowsCategory").value;
	let cityId = document.getElementById("flowsCity").value;
    var bucketId = document.getElementById("flowsBucket").value;
    var fromDate = document.getElementById("flowsFromDate").value;
    var toDate = document.getElementById("flowsToDate").value;

    console.log("Maker Compare Product =", product);
    console.log("Maker Compare Sub Product =", subProduct);
    console.log("Maker Compare Category ID =", categoryId);
    console.log("Maker Compare Bucket ID =", bucketId);
    console.log("Maker Compare From Date =", fromDate);
    console.log("Maker Compare To Date =", toDate);

    if (!product) {
        alert("Please select Product");
        return;
    }

    if (!subProduct) {
        alert("Please select Sub Product");
        return;
    }

    if (!categoryId) {
        alert("Please select Category");
        return;
    }
	
    let categorySelect = document.getElementById("flowsCategory");

    let selectedOption =
        categorySelect.options[
        categorySelect.selectedIndex
        ];

    let categoryName = selectedOption.text.trim().toUpperCase();

    // City required ONLY for non-CFP
    if (categoryName !== "CFP" && !cityId) {
        alert("Please select City");
        return;
    }
	
	/*if(!cityId){
	    alert("Please select City");
	    return;
	}*/

    if (!bucketId) {
        alert("Please select Bucket");
        return;
    }

    if (!fromDate || !toDate) {
        alert("Please select From Date and To Date");
        return;
    }

    var params = new URLSearchParams();

    params.append("product", product);
    params.append("subProduct", subProduct);
    params.append("categoryId", categoryId);
	//params.append("cityId", cityId);
	
    if (categoryName !== "CFP") {
        params.append("cityId", cityId);
    }

    params.append("bucketId", bucketId);
    params.append("fromDate", fromDate);
    params.append("toDate", toDate);

    console.log("Latest Approved Request =", params.toString());

    fetch(CONTEXT_PATH + "/flowsPayout/latestApprovedId?" + params.toString())
        .then(function(response) {

            if (!response.ok) {
                throw new Error("Unable to find current approved record");
            }

            return response.text();
        })
        .then(function(id) {

            console.log("Current Approved ID =", id);

            if (!id || id.trim() === "" || id.trim() === "null") {
                alert("No Current Approved Record Found");
                return;
            }

            window.location.href = CONTEXT_PATH + 
			"/mainPage/load?master=FLOWSPAYOUTCOMPARECHECKER&id=" + 
			encodeURIComponent(id.trim()) + "&mode=maker";
        })
        .catch(function(error) {

            console.error("Maker Compare Error =", error);
            alert(error.message);
        });
}

function updateFlowsHeaderText() {

    let categorySelect = document.getElementById("flowsCategory");
    let citySelect = document.getElementById("flowsCity");
    let header = document.getElementById("flowsHeaderText");

    let categoryName =categorySelect.options[categorySelect.selectedIndex].text;
    let cityName =citySelect.options[citySelect.selectedIndex].text;

    if (categorySelect.value && citySelect.value) {

        header.innerText =
            "For " +
            categoryName +
            " Category - " +
            cityName;
    }
}