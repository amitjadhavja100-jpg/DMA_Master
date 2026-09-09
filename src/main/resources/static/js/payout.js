/*payout.js*/

let dpd = "";
let payoutData = {};

let ceCols = [];
let rows = [];
/*var userId = "BAN513834";*/
var userId = localStorage.getItem("user_id");
let pendingRows = [];
let pendingColumns = [];

//window.onload = function () {
console.log("WINDOWS LOADED 24/7 ICICI");

    // INITIAL DROPDOWNS

	
//snz
/*document.addEventListener("DOMContentLoaded", function(){

    document.getElementById("city").innerHTML =
    "<option value=''>Select City</option>";

    document.getElementById("dpdDropdown").innerHTML =
    "<option value=''>Select DPD</option>";

	loadProducts();  //snz
	
    loadCategories();
	console.log("AFTER LOADED CATEGORIES");

});*/

//snz
document.addEventListener("DOMContentLoaded", function(){

    loadProducts();

    document.getElementById("collectionAgencySlab").style.display="none";

    document.getElementById("collectionOspSlab").style.display="none";
	
	document.getElementById("flowsSlab").style.display="none"; //snz
    
    document.getElementById("vehicleTWStructure").style.display="none";
   
    document.getElementById("vehicleCVStructure").style.display="none";
   
    document.getElementById("iProcessKeralaSlab").style.display="none";
    
    document.getElementById("manipalKeralaSlab").style.display="none";

});


/*snz*/
function loadProducts(){

    fetch(CONTEXT_PATH + "/DMAPayoutWeb3/products")

    .then(r=>r.json())

    .then(data=>{

        let product=document.getElementById("product");

        product.innerHTML="<option value=''>Select Product</option>";

        data.forEach(function(p){

            product.innerHTML +=
            "<option value='"+p+"'>"+p+"</option>";

        });

    });

}

function loadSubProducts(){

    // Hide both slabs when Product changes
    document.getElementById("collectionAgencySlab").style.display = "none";
    document.getElementById("collectionOspSlab").style.display = "none";
    document.getElementById("flowsSlab").style.display = "none"; //snz
    document.getElementById("valuationSlab").style.display = "none";
	
     document.getElementById("vehicleTWStructure").style.display="none";
   
    document.getElementById("vehicleCVStructure").style.display="none";
   
    document.getElementById("iProcessKeralaSlab").style.display="none";
   
    document.getElementById("manipalKeralaSlab").style.display="none";

    let product = document.getElementById("product").value;

    let sub = document.getElementById("subProduct");

    // Reset Sub Product dropdown
    sub.innerHTML =
        "<option value=''>Select Sub Product</option>";

    if(product === ""){
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/DMAPayoutWeb3/subProducts?product=" +
        encodeURIComponent(product)
    )
    .then(r => r.json())
    .then(data => {

        data.forEach(function(s){

            sub.innerHTML +=
                "<option value='" + s + "'>" + s + "</option>";

        });

    });
}

/*snz*/
function openSelectedSlab(){

    let product = document.getElementById("product").value.trim();
    let subProduct = document.getElementById("subProduct").value.trim();
        
    console.log("Selected Product: ", product);
    console.log("Selected Subproduct: ", subProduct);

    document.getElementById("collectionAgencySlab").style.display="none";
    document.getElementById("collectionOspSlab").style.display="none";
	document.getElementById("flowsSlab").style.display="none"; //snz
	document.getElementById("valuationSlab").style.display = "none";
    document.getElementById("vehicleTWStructure").style.display="none";
     document.getElementById("vehicleCVStructure").style.display="none";
      document.getElementById("iProcessKeralaSlab").style.display="none";
      document.getElementById("manipalKeralaSlab").style.display="none";
 

    if(product==="Collection - Agency" &&
       subProduct==="Recovery Credit Card"){
       
       console.log("Agency slab page loaded");
       console.log("Loading using Sub Product: ", subProduct);

        document.getElementById("collectionAgencySlab").style.display="block";
        loadCurrentSlab();

    }
	/*snz*/   
	else if(product==="Collection - Agency" &&
	        subProduct==="Flows - Credit Card"){

        document.getElementById("flowsSlab").style.display = "block";
	    initFlowsScreen();

	}
	else if (
	    product === "Collection - Agency" &&
	    subProduct === "Valuation"
	) {
	    document.getElementById("valuationSlab").style.display = "block";
	    initValuationScreen();
	}

	else if(product==="Collection- Osp" &&
            subProduct==="Recovery Osp"){
          
        document.getElementById("collectionOspSlab").style.display="block";
        loadOSPCurrentSlab();

    } 
   	else if(product==="Vehicle loan" &&
            subProduct==="TW Coorgination"){
          
        document.getElementById("vehicleTWStructure").style.display="block";

    }
    	else if(product==="Vehicle loan" &&
            subProduct==="CV Coorgination"){
          
        document.getElementById("vehicleCVStructure").style.display="block";

    }
    
    else if(product==="Vehicle loan" &&
            subProduct==="AUTO COUNSELLOR - I process"){
          
      document.getElementById("iProcessKeralaSlab").style.display="block";

    }
    
    else if(product==="Vehicle loan" &&
            subProduct==="AUTO COUNSELLOR_ other than Manipal"){
          
      document.getElementById("manipalKeralaSlab").style.display="block";

    }
    //vinayak--personal loan & educational loan--start
    else if(product==="Personal loan, Education loan,others" && subProduct === "Personal loan"){

    	resetValue();

         document.getElementById("collectionAgencySlab").style.display="none";

                  manipalContent.style.display = "none";

                  saveManiPal.style.display = "block";

    	 personalLonecategory.style.display = "block";	

		 category2.innerHTML = '<option value="">Select Category</option>';

    	 selectCategories["Personal loan, Education Loan ,Others"].categories.forEach(function(s){

         category2.innerHTML +=

         "<option value='" + s + "'>" + s + "</option>";

        });			

    }else if (product==="Personal loan, Education loan,others" && subProduct === "Education Loan"){

    	 personalLonecategory.style.display = "block";

         document.getElementById("collectionAgencySlab").style.display="none";	

         manipalContent.style.display = "none";

         		saveManiPal.style.display = "block";

    	 category2.innerHTML = '<option value="">Select Category</option>';	

    	 selectCategories["Personal loan, Education Loan ,Others"].subCategories.forEach(function(s){

            category2.innerHTML +=

            "<option value='" + s + "'>" + s + "</option>";

            resetValue();

        });	

    }
 //end
   
    	
    else{
    	console.log("No page mapped for: ");    	
    	console.log("Selected Product: ", product);
    	console.log("Selected Subproduct: ", subProduct);
    }
    
    
    

}

function loadOSPCurrentSlab(){

    if(typeof initOspScreen==="function"){
        initOspScreen();
    }

}


/*snz*/
function loadCurrentSlab(){

    loadCategories();
    loadCities();
    loadDPDs();
	
	loadLabels(); //nitin

}


// CATEGORY
function loadCategories() {

console.log("CAT LOADED 24/7 ICICI");

   // fetch("/payout/categories") // `${pageContext.request.contextPath/payout/categories}`
fetch(CONTEXT_PATH + "/payout/categories")
    .then(res => res.json())
    .then(data => {

        let cat = document.getElementById("category");

      /*  cat.innerHTML = "";

        data.forEach(c => {

            cat.innerHTML += `
                <option value="${c}">
                    Category ${c}
                </option>`;
        });
*/

cat.innerHTML = "<option value=''>Select Category</option>";

 data.forEach(c => {

    cat.innerHTML += `
        <option value="${c}">
            Category ${c}
        </option>`;
});


        /*loadCities();*/
    });
}

// CITY
function loadCities() {

    let category =
    document.getElementById("category").value;

    // FOR 360 CATEGORY

    /*if(category.includes("360")){*/
		
		if(category.includes("360")
		|| category === "NTC"
		|| category === "TCC"
		|| category === "CCA"
		|| category === "Settlement Incentive/Penalty"){

        // HIDE CITY

        document.getElementById("cityDiv")
        .style.display = "none";

        // SET CITY VALUE

        document.getElementById("city").innerHTML =
        "<option value='NA'>NA</option>";

        loadDPDs();

        return;
    }

    // NORMAL CATEGORY

    document.getElementById("cityDiv")
    .style.display = "block";

    //fetch(`/payout/cities?category=${category}`)
fetch(`${CONTEXT_PATH}/payout/cities?category=${category}`)

    .then(res => res.json())

    .then(data => {

        let city =
        document.getElementById("city");

		city.innerHTML =
		"<option value=''>Select City</option>";

        data.forEach(c => {

            city.innerHTML += `

            <option value="${c}">
                ${c}
            </option>
            `;
        });

        loadDPDs();
    });
}


function loadDPDs() {
	
    let category =
    document.getElementById("category").value;

    // NTC / CCA / TCC

    if(category === "NTC"
    || category === "CCA"
    || category === "TCC"
    || category === "Settlement Incentive/Penalty"){

        // HIDE DPD DROPDOWN

        document.getElementById("dpdDiv")
        .style.display = "none";

        // STATIC DPD

        dpd = "ALL";

        loadStructure();

        updateHeader();

        return;
    }

    // SHOW DPD FOR NORMAL CATEGORIES
    document.getElementById("dpdDiv")
    .style.display = "block";

fetch(`${CONTEXT_PATH}/payout/dpds?category=${encodeURIComponent(category)}`)

    .then(res => res.json())

    .then(data => {

        console.log("DPDS =", data);
        
        data.sort((a, b) => {

    let aStart = parseInt(a.split("-")[0].trim());
    let bStart = parseInt(b.split("-")[0].trim());

    return aStart - bStart;
});

        let dropdown =
        document.getElementById("dpdDropdown");

		dropdown.innerHTML =
		"<option value=''>Select DPD</option>";
		
        data.forEach(d => {

            dropdown.innerHTML += `

            <option value="${d}">
                ${d}
            </option>
            `;
			
        });
		
		if(data.length > 0){

		    dpd = "";

		    dropdown.value = "";

		    console.log("DPD WAITING FOR SELECTION");
		}
		
    });
}


//Structure
function loadStructure() {

    let category =
    document.getElementById("category").value;

    let city =
    document.getElementById("city").value;

    // FOR 360 CATEGORY

   /* if(category === "360 + CAT A"
    || category === "360 + CAT B"){*/
		
		if(category === "360 + CAT A"
		|| category === "360 + CAT B"
		|| category === "NTC"
		|| category === "TCC"
		|| category === "CCA"
		|| category === "Settlement Incentive/Penalty"){

        city = "NA";
    }

    console.log("CATEGORY =", category);

    console.log("CITY =", city);

    console.log("DPD =", dpd);

    let url = 
    `${CONTEXT_PATH}/payout/existingStructure?category=${
        encodeURIComponent(category)
    }&city=${
        encodeURIComponent(city)
    }&dpd=${
        encodeURIComponent(dpd)
    }`;

    console.log("URL =", url);

    fetch(url)

    .then(res => res.json())

    .then(data => {

        console.log("STRUCTURE =", data);
		
		if(isBandDpd()){

		    ceCols = data.rows || [];

		    rows = [dpd];

		}else{

		    ceCols = data.cols || [];

		    rows = data.rows || [];
		}

        renderHeader();

        renderTable();
        
         loadAvailableRows();

        loadAvailableColumns();
    })

    .catch(err => {

        console.log("STRUCTURE ERROR =", err);
    });
}


function renderHeader() {

    let h =
    document.getElementById("ceHeader");

    h.innerHTML = "";

    // SPECIAL BAND DPD

    if(isBandDpd()){

        document.getElementById(
        "leftHeader"
        ).innerText = "DPD Band";

        document.getElementById(
        "ceColSpanText"
        ).innerText = "Collection";

    }else{

        document.getElementById(
        "leftHeader"
        ).innerText = "Collection";

        let category =
        document.getElementById("category").value;
        
        if(category === "Settlement Incentive/Penalty"){

        document.getElementById(
        "leftHeader"
        ).innerText =
        "Total Waiver %";

    }else{

        document.getElementById(
        "leftHeader"
        ).innerText =
        "Collection";
    }

        // TCC

        if(category === "TCC"){

            document.getElementById(
            "ceColSpanText"
            ).innerText = "Payout %";

        // NTC + CCA

        }else if(category === "NTC"
        || category === "CCA"){

            document.getElementById(
            "ceColSpanText"
            ).innerText = "DPD Band";

        // NORMAL

        /*}else{

            document.getElementById(
            "ceColSpanText"
            ).innerText = "CE% on ENR";
        }*/
        
        }else if(category ===
     "Settlement Incentive/Penalty"){

    document.getElementById(
    "ceColSpanText"
    ).innerText =
    "Incentive / Penalty";

}else{

    document.getElementById(
    "ceColSpanText"
    ).innerText =
    "CE% on ENR";
}
        
    }

    ceCols.forEach(c => {

        h.innerHTML += `<th>${c}</th>`;
    });

    document.getElementById(
    "ceColSpanText"
    ).setAttribute(
    "colspan",
    ceCols.length
    );
}



function preserveValues(){

    document
    .querySelectorAll("#tbody tr")
    .forEach(tr => {

        let row =
        tr.cells[0].innerText;

        for(let i=1; i<tr.cells.length; i++){

            let input =
            tr.cells[i]
            .querySelector("input");

            if(input){

                let col =
                ceCols[i - 1];

                payoutData[
                row + "-" + col
                ] = input.value;
            }
        }
    });
}


// TABLE
function renderTable() {

    let tbody =
        document.getElementById("tbody");

    tbody.innerHTML = "";

    rows.forEach(r => {

        let tr = `<tr><td>${r}</td>`;

		ceCols.forEach(c => {

		    let key =
		    r + "-" + c;

		    let value =
		    payoutData[key] || "";

		    tr += `
		    <td>

		        <input
		        type="number"
		        step="0.01"
		        value="${value}"/>

		    </td>`;
		});

        tr += "</tr>";

        tbody.innerHTML += tr;
    });
}


function addRow(){

    let selected =
    document.getElementById("newRowSelect").value;

    if(!selected){

        alert("Select Row");

        return;
    }

    if(rows.includes(selected)){

        alert("Row Already Added");

        return;
    }

    preserveValues();

    rows.push(selected);

    renderTable();

    alert("Row Added Successfully");
}


function confirmAddRow(){

    let selected =
    document.getElementById(
    "newRowSelect"
    ).value;

    if(!selected){

        alert("Select Row");

        return;
    }

    if(
    pendingRows.includes(
    selected
    )){

        alert(
        "Already Added"
        );

        return;
    }

    pendingRows.push(
    selected
    );

    alert(
    "Row Added For Save"
    );

    console.log(
    pendingRows
    );
}

function addColumn(){

    let selected =
    document.getElementById("newColSelect").value;

    if(!selected){

        alert("Select Column");

        return;
    }

    if(ceCols.includes(selected)){

        alert("Column Already Added");

        return;
    }

    preserveValues();

    ceCols.push(selected);

    renderHeader();

    renderTable();

    alert("Column Added Successfully");
}


function confirmAddColumn(){

    let selected =
    document.getElementById(
    "newColSelect"
    ).value;

    if(!selected){

        alert(
        "Select Column"
        );

        return;
    }

    if(
    pendingColumns.includes(
    selected
    )){

        alert(
        "Already Added"
        );

        return;
    }

    pendingColumns.push(
    selected
    );

    alert(
    "Column Added For Save"
    );

    console.log(
    pendingColumns
    );
}

function updateHeader() {

    let cat =
    document.getElementById("category").value;

    let city =
    document.getElementById("city").value;

    // COMMON CATEGORY

    if(cat === "NTC"
    || cat === "CCA"
    || cat === "TCC"
    || cat === "Settlement Incentive/Penalty"){

        document.getElementById("headerText")
        .innerText = `CATEGORY ${cat}`;

        return;
    }

    // 360 CATEGORY

    if(cat.includes("360")){

        document.getElementById("headerText")
        .innerText =
        `CATEGORY ${cat} - DPD ${dpd}`;

    }else{

        document.getElementById("headerText")
        .innerText =
        `${city} - CATEGORY ${cat} - DPD ${dpd}`;
    }
}


// FETCH
function fetchData() {

    let cat =
    document.getElementById("category").value;

    let city =
    cat.includes("360")
    ? "NA"
    : document.getElementById("city").value;

    let fromDate =
    document.getElementById("fromDate").value;

    let toDate =
    document.getElementById("toDate").value;
    
    if(!fromDate){
    alert("Please Select From Date");
    return;
    }

    if(!toDate){
    alert("Please Select To Date");
    return;
    }
    if(new Date(fromDate) > new Date(toDate)){
    alert("From Date cannot be greater than To Date");
    return;
    }
    
    let url = CONTEXT_PATH + 
`/payout/fetch?category=${encodeURIComponent(cat)}
&city=${encodeURIComponent(city)}
&dpd=${encodeURIComponent(dpd)}
&fromDate=${fromDate}
&toDate=${toDate}`;

    console.log("FETCH URL =", url);

    fetch(url)

    .then(async r => {

        let text = await r.text();

        console.log(
        "FETCH RESPONSE =",
        text
        );

        if(!text){

            alert("No Approved Data Found");

            return null;
        }

        return JSON.parse(text);
    })

    .then(data => {

        if(!data || !data.details){

            alert("No Details Found");

            return;
        }

		let map = {};

		data.details.forEach(d => {
		
			/*let mapKey =
			d.collectionSlab.trim()
			+ "-" +
			d.ceRange.trim();*/
			
			let mapKey =
              (d.collectionSlab || "").trim()
                   + "-" +
              (d.ceRange || "").trim();

		    map[mapKey] = d.payout;
		});

		console.log("MAP =", map);

		document
		.querySelectorAll("#tbody tr")
		.forEach(tr => {

		    let row =
		    tr.cells[0].innerText;

		    for(let i=1;
		        i<tr.cells.length;
		        i++){

		        /*let key =
		        row + "-" +
		        ceCols[i - 1];*/
				
				let key = "";

				if(isBandDpd()){

				   /* key =
				    ceCols[i - 1].trim()
				    + "-" +
				    row.trim();*/
				    
				    key =
                ((ceCols[i - 1]) || "").trim()
                   + "-" +
                (row || "").trim();

				}else{

				    /*key =
				    row.trim()
				    + "-" +
				    ceCols[i - 1].trim();*/
				    
				    key =
                    (row || "").trim()
                          + "-" +
                    ((ceCols[i - 1]) || "").trim();
				    
				}

		        console.log("KEY =", key);

		        let val =
		        map[key] || "";

		        /*tr.cells[i]
		        .querySelector("input")
		        .value = val;*/
		        
		        tr.cells[i]
                .querySelector("input")
                .value =
                map[key] !== undefined
                ? map[key]
                 : "";
		    }
		});
		
    })

    .catch(err => {

        console.log(
        "FETCH ERROR =",
        err
        );

        alert("Fetch Failed");
    });
}



// SAVE
function saveData() {

    let matrix = [];

    document
        .querySelectorAll("#tbody tr")
        .forEach(tr => {

        let row = {

            collection:
                tr.cells[0].innerText,

            values: []
        };

        for (let i = 1; i < tr.cells.length; i++) {
			
			if(isBandDpd()){

			    row.values.push({

			        collectionSlab:
			        ceCols[i - 1],

			        ceRange:
			        row.collection,

			        payout:
			        tr.cells[i]
			        .querySelector("input")
			        .value
			    });

			}else{

			    row.values.push({

			        ceRange:
			        ceCols[i - 1],

			        payout:
			        tr.cells[i]
			        .querySelector("input")
			        .value
			    });
			}
        }

        matrix.push(row);
    });



fetch(CONTEXT_PATH + "/payout/save?user=" + userId, {

	    method: "POST",

	    headers: {
	        "Content-Type": "application/json"
	    },

	    body: JSON.stringify({
		
			
	        category: document.getElementById("category").value,

	        /*city: document.getElementById("city").value,*/
			city:
			document.getElementById("category")
			.value.includes("360")
			? "NA"
			: document.getElementById("city").value,
			
			

	        dpd: dpd,

	        fromDate:
	            document.getElementById("fromDate").value,

	        toDate:
	            document.getElementById("toDate").value,
	            
	        pendingRows: pendingRows,
            pendingColumns: pendingColumns,

	        matrix: matrix
	    })  
	})

	.then(res => res.json())

	.then(data => {

	    console.log("SAVE RESPONSE =", data);

	    /*localStorage.setItem(
	        "masterId",
	        data.id
	    );*/

	    alert("Saved Successfully");
	});
}

// RESET
/*function resetTable() {

    document
        .querySelectorAll("#tbody input")
        .forEach(i => {

            i.value = "";
        });
}*/

function resetTable() {

    // RESET CATEGORY

    document.getElementById("category")
    .selectedIndex = 0;

    // RESET CITY

    document.getElementById("city")
    .innerHTML = `
    <option value="">
        Select City
    </option>`;

    // RESET DPD

    document.getElementById("dpdDropdown")
    .innerHTML = `
    <option value="">
        Select DPD
    </option>`;
    
     document.getElementById("newRowSelect")
    .innerHTML = `
    <option value="">
        Select Row
    </option>`;
    
     document.getElementById("newColSelect")
    .innerHTML = `
    <option value="">
        Select Column
    </option>`;

    // RESET DATES

    document.getElementById("fromDate")
    .value = "";

    document.getElementById("toDate")
    .value = "";

    // RESET TABLE

    document.getElementById("tbody")
    .innerHTML = "";

    // RESET HEADERS

    document.getElementById("headerText")
    .innerText = "";

    document.getElementById("ceHeader")
    .innerHTML = "";

    /*document.getElementById("leftHeader")
    .innerText = "Collection";*/
	//nitin
	document.getElementById("leftHeader").innerHTML =
    uiConfig.COLLECTION || "Collection";


	//nitin
    /*document.getElementById("ceColSpanText")
    .innerText = "CE% on ENR";*/
	document.getElementById("ceColSpanText").innerHTML =
    uiConfig.CE_RANGE || "CE% on ENR";

    // RESET GLOBAL VARIABLES

    payoutData = {};

    ceCols = [];

    rows = [];

    dpd = "";

    // SHOW DROPDOWNS AGAIN

    document.getElementById("cityDiv")
    .style.display = "block";

    document.getElementById("dpdDiv")
    .style.display = "block";
}


// HISTORY

function goHistory(){

fetch(CONTEXT_PATH +  `/payout/latestApprovedId?category=${
document.getElementById("category").value
}&city=${
document.getElementById("city").value
}&dpd=${dpd}`)

.then(r=>r.text())

.then(id=>{

window.location.href= CONTEXT_PATH + 
`/payoutCompare?id=${id}&mode=userId`;

});
}


document.addEventListener("change", function(e){

  if(e.target.id === "category"){

        loadCities();
    }
	
	/*if(e.target.id === "category"){

	    handleCategoryChange();
	}*/

    if(e.target.id === "city"){
		
		loadDPDs();
        updateHeader();
    }

    if(e.target.id === "dpdDropdown"){

        dpd = e.target.value;

        loadStructure();

        updateHeader();
    }
});

function isBandDpd(){

    return dpd === "721-900"
    || dpd === "901-1080"
    || dpd === "1080+";
}

function loadAvailableRows(){

    let category =
    document.getElementById("category").value;

    fetch(
    CONTEXT_PATH +
    "/payout/allSlabs?category="
    + encodeURIComponent(category)
    + "&dpd="
    + encodeURIComponent(dpd))

    .then(r => r.json())

    .then(data => {

        let dropdown =
        document.getElementById("newRowSelect");

        dropdown.innerHTML =
        "<option value=''>Select Row</option>";

        data.forEach(d => {

            dropdown.innerHTML +=
            `<option value="${d}">
                ${d}
            </option>`;
        });
    });
}


function loadAvailableColumns(){

    let category =
    document.getElementById("category").value;

    fetch(
    CONTEXT_PATH +
    "/payout/allCeRanges?category="
    + encodeURIComponent(category)
    + "&dpd="
    + encodeURIComponent(dpd))

    .then(r => r.json())

/* .then(data => {

        let dropdown =
        document.getElementById("newColSelect");

        dropdown.innerHTML =
        "<option value=''>Select Column</option>";

        data.forEach(d => {

            dropdown.innerHTML +=
            `<option value="${d}">
                ${d}
            </option>`;
        });*/
        
        .then(data => {

    console.log("COLUMN DATA =", data);

    let dropdown =
    document.getElementById("newColSelect");

    dropdown.innerHTML =
    "<option value=''>Select Column</option>";

    data.forEach(d => {

        dropdown.innerHTML +=
        `<option value="${d}">
            ${d}
        </option>`;
    });
});
        
   
}


function isCommonCategory(){

    let category =
    document.getElementById("category").value;

    return category === "NTC"
    || category === "TCC"
    || category === "CCA";
}


function rejectData(id){

    let remark = prompt("Enter Remark");

    fetch(CONTEXT_PATH + "/payout/reject/" + id + "?user=" + userId,{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({
            remark:remark
        })
    })
    .then(()=>{
        alert("Rejected");
        location.reload();
    });
	 
}


function loadLabels() {

    fetch(CONTEXT_PATH + "/config/labels")
        .then(r => r.json())
        .then(data => {

            setText("screenTitle", data.PAYOUT_SCREEN);

            setText("lblCategory", data.CATEGORY);
            setText("lblCity", data.CITY);
            setText("lblDpd", data.DPD);
            setText("lblFromDate", data.FROM_DATE);
            setText("lblToDate", data.TO_DATE);
            
            setText("btnFetch", data.FETCH_BUTTON);
            setText("btnReset", data.RESET_BUTTON);
            setText("btnSubmit", data.SUBMIT_BUTTON);
            setText("btnHistory", data.HISTORY_BUTTON);
            setText("btnAddRow", data.ADD_ROW);
            setText("btnAddColumn", data.ADD_COLUMN);

            setText("leftHeader", data.COLLECTION);
            setText("ceColSpanText", data.CE_RANGE);

        });
}

function setText(id, value) {

    if (value != null && value !== "") {

        let e = document.getElementById(id);

        if (e) {

            e.innerHTML = value;

        }

    }

}


function loadUiConfig(){
    fetch(CONTEXT_PATH + "/config/ui")
    .then(res=>res.json())
    .then(data=>{
        setText("screenTitle",data.PAYOUT_SCREEN);
        setText("lblCategory",data.CATEGORY);
        setText("lblCity",data.CITY);
        setText("lblDpd",data.DPD);
        setText("lblFromDate",data.FROM_DATE);
        setText("lblToDate",data.TO_DATE);
        setText("btnFetch",data.FETCH_BUTTON);
        setText("btnReset",data.RESET_BUTTON);
        setText("btnSubmit",data.SUBMIT_BUTTON);
        setText("btnHistory",data.HISTORY_BUTTON);
        setText("btnAddRow",data.ADD_ROW);
        setText("btnAddColumn",data.ADD_COLUMN);
    });
}

function setText(id,value){
    if(value){
        let e=document.getElementById(id);
        if(e){
            e.innerHTML=value;
        }
    }
}

function fetchDataTWStructure(){
		
		let cycleFrom = document.getElementById("cyclefromDate").value;
		let cycleTo = document.getElementById("cycletoDate").value;

	if (!cycleFrom || !cycleTo) {
		alert("Please select both Cycle From and Cycle To dates.");
		return;
	}

	let fromDate = new Date(cycleFrom);
	let toDate = new Date(cycleTo);

	if (fromDate >= toDate) {
		alert("Cycle From date must be earlier than Cycle To date.");
		return;
	}

	let diffInMs = toDate - fromDate;
	let diffInDays = diffInMs / (1000 * 60 * 60 * 24);

	if (diffInDays > 30) {
		alert("The gap between Cycle From and Cycle To must not exceed 30 days.");
		return;
	}

		var grid = document.getElementById("gridSectionForTW");
		grid.style.display = "block";

		


}
function addUsedAutoDMARow() {
	const tbody = document.getElementById("UsedAutoDMABody");
	const minRateRow = tbody.querySelector(".min-rate-row"); // UPI Money Ltd row, stays last

	const newRow = document.createElement("tr");
	newRow.innerHTML = `
<td class="row-label">
<input type="text" class="partner-input" placeholder="Channel Partner Name">
</td>
<td><input type="text" class="pct-input" value=""></td>
<td><input type="text" class="pct-input" value=""></td>
<td><input type="text" class="pct-input" value=""></td>
<td><button type="button" class="removeRowBtn" onclick="this.closest('tr').remove()">✕</button></td>
`;

	tbody.insertBefore(newRow, minRateRow);
}


function saveUsedAutoDMAStructure() {
	const productType = document.getElementById("product").value;
	const structureType = document.getElementById("subProduct").value;
	const cycleFrom = document.getElementById("cyclefromDate").value;
	const cycleTo = document.getElementById("cycletoDate").value;

	const rows = document.querySelectorAll("#UsedAutoDMABody tr");
	const details = [];

	rows.forEach(row => {
		const partnerCell = row.querySelector(".row-label");
		const partnerName = partnerCell.querySelector("input")
			? partnerCell.querySelector("input").value.trim()
			: partnerCell.textContent.trim();

		const rateInputs = row.querySelectorAll(".pct-input");
		if (!partnerName || rateInputs.length < 3) return;

		details.push({
			channelPartnerName: partnerName,
			rrTillDec22: rateInputs[0].value,
			rrFromJan23: rateInputs[1].value,
			rrFromAllAug23: rateInputs[2].value
		});
	});

	const payload = { productType, structureType, cycleFrom, cycleTo, details };

	fetch("/vehicle/saveTWStructure", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(payload)
	})
		.then(res => res.text())
		.then(data => {
			alert(data.message || "Saved successfully");
		})
		.catch(err => {
			console.error(err);
			alert("Save failed. Check console.");
		});
}

function fetchDataCVStructure(){

let cycleFrom = document.getElementById("cyclefromDateCV").value;
		let cycleTo = document.getElementById("cycletoDateCV").value;

	if (!cycleFrom || !cycleTo) {
		alert("Please select Cycle From and Cycle To dates.");
		return;
	}

	let fromDate = new Date(cycleFrom);
	let toDate = new Date(cycleTo);

	if (fromDate >= toDate) {
		alert("Cycle From date must be earlier than Cycle To date.");
		return;
	}

	let diffInMs = toDate - fromDate;
	let diffInDays = diffInMs / (1000 * 60 * 60 * 24);

	if (diffInDays > 30) {
		alert("The gap between Cycle From and Cycle To must not exceed 30 days.");
		return;
	}
	
		var grid = document.getElementById("gridSectionForCV");
		grid.style.display = "block";
}
function saveCvVehicleStructure() {
var productType = document.getElementById("product").value;
var structureType = document.getElementById("subProduct").value;
var cycleFrom = document.getElementById("cyclefromDateCV").value;
var cycleTo = document.getElementById("cycletoDateCV").value;
 
var rows = document.querySelectorAll("#CvVehicleBody tr");
var details = [];
 
for (var i = 0; i < rows.length; i++) {
var rateInputs = rows[i].querySelectorAll(".pct-input");
if (rateInputs.length < 2) continue;
 
details.push({
rrFromApril22: rateInputs[0].value,
rrBeforeApril22: rateInputs[1].value
});
}
 
if (details.length === 0) {
alert("Please add at least one row before saving.");
return;
}
 
var payload = { productType: productType, structureType: structureType,
cycleFrom: cycleFrom, cycleTo: cycleTo, details: details };
 
fetch("/vehicle/cv/save", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify(payload)
})
.then(function(response) {
return response.text().then(function(text) {
if (!response.ok) { throw new Error(text || "Save failed."); }
alert(text || "Saved successfully");
});
})
.catch(function(error) {
alert(error.message);
});
}






