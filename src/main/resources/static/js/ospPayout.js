/*ospPayout.js*/

let ospRanges = [];

function loadOspDpds() {

    fetch(CONTEXT_PATH + "/ospPayout/dpds")
        .then(r => r.json())
        .then(data => {

            let ddl = document.getElementById("ospDpd");

            ddl.innerHTML = '<option value="">Select DPD</option>';

            data.forEach(function(d) {

                ddl.innerHTML +=
                    '<option value="' + d.id + '">' +
                    d.dpdName +
                    '</option>';

            });

        });

}

function initOspScreen(){
    document.getElementById("ospBody").innerHTML="";
   // document.getElementById("ospDpd").selectedIndex=0;
   loadOspDpds();

}

function loadOspRanges() {
    let dpd =document.getElementById("ospDpd").value;
    if (dpd == "") {
        return;
    }
	//snz
    //fetch(CONTEXT_PATH +  "/ospPayout/ranges?dpd=" +
	fetch(CONTEXT_PATH +  "/ospPayout/ranges?dpdId=" +
        encodeURIComponent(dpd)
    )
        .then(r => r.json())
		.then(data => {
		    document.getElementById("ospBody").innerHTML="";
		    ospRanges = data;
		    renderOspTable();
		});
}

function renderOspTable() {
    
	let body = document.getElementById("ospBody");
    body.innerHTML = "";

	let html="";

	ospRanges.forEach(function(r){
	    html += `
	    <tr>
	        <td>${r.fromAmount}</td>
	        <td>${r.isMax ? "MAX" : r.toAmount}</td>
	        <td>
	            <input
	                type="number"
	                step="0.01"
	                class="form-control">
	        </td>
	    </tr>
	    `;
	});
	body.innerHTML = html;
}

function fetchOSP() {

    let product = document.getElementById("product").value;
    let subProduct = document.getElementById("subProduct").value;
    let dpd = document.getElementById("ospDpd").value;
	
    let fromDate = document.getElementById("ospFromDate").value;
    let toDate = document.getElementById("ospToDate").value;
	
	// Clear previous fetched incentives
	document.querySelectorAll("#ospBody input").forEach(function(input) {
	        input.value = "";
	    });
	
	if(fromDate==""){
	    alert("Select From Date");
	    return;
	}

	if(toDate==""){
	    alert("Select To Date");
	    return;
	}

	if(new Date(fromDate)>new Date(toDate)){
	    alert("From Date cannot be greater than To Date");
	    return;
	}

    fetch(CONTEXT_PATH +
		 "/ospPayout/fetch"
        + "?product=" + encodeURIComponent(product)
        + "&subProduct=" + encodeURIComponent(subProduct)
        + "&dpdId=" + encodeURIComponent(dpd)
        + "&fromDate=" + fromDate
        + "&toDate=" + toDate
    )
	.then(async response => {

	    let text = await response.text();

	    if (text == "") {

	        // Clear all incentives again
	        document.querySelectorAll("#ospBody input").forEach(function(input) {
	            input.value = "";
	        });

	        alert("No Approved Record Found");
	        return null;
	    }

	    return JSON.parse(text);
	})
/*	.then(async response=>{

	    let text = await response.text();
	    if(text==""){
	        alert("No Approved Record Found");
	        return null;
	    }
	    return JSON.parse(text);
	})*/
	.then(data=>{
	    if(data==null){
	        return;
	    }
	    fillIncentives(data.details);

	})
}

function fillIncentives(details) {

    let rows = document.querySelectorAll("#ospBody tr");

    rows.forEach(function(tr, index) {
        if (details[index]) {
            tr.querySelector("input").value =
                details[index].incentivePercent;
        }
    });
}

function saveOSP() {

    let dpd = document.getElementById("ospDpd").value;
    let fromDate = document.getElementById("ospFromDate").value;
    let toDate = document.getElementById("ospToDate").value;

    if (dpd == "") {
        alert("Please Select DPD");
        return;
    }

    if (fromDate == "") {
        alert("Please Select From Date");
        return;
    }

    if (toDate == "") {
        alert("Please Select To Date");
        return;
    }

    if (new Date(fromDate) > new Date(toDate)) {
        alert("From Date cannot be greater than To Date");
        return;
    }

    if (ospRanges.length == 0) {
        alert("Please Click Fetch After Selecting DPD");
        return;
    }

	let trs = document.querySelectorAll("#ospBody tr");
	let hasValue = false;

	trs.forEach(function(tr) {
	    let value = tr.querySelector("input").value.trim();
	    if (value !== "") {
	        hasValue = true;
	    }
	});

	if (!hasValue) {
	    alert("Please Enter Incentives %");
	    return;
	}
	
    let request = {};

    request.product = document.getElementById("product").value;
    request.subProduct = document.getElementById("subProduct").value;
	//snz    
	//request.dpd = document.getElementById("ospDpd").value;
	request.dpdId = parseInt(document.getElementById("ospDpd").value);
    request.fromDate = document.getElementById("ospFromDate").value;
    request.toDate = document.getElementById("ospToDate").value;
    request.rows = [];

    trs.forEach(function(tr, index) {
		let value =
		tr.querySelector("input").value;
		
		request.rows.push({
		    fromAmount: ospRanges[index].fromAmount,
		    toAmount: ospRanges[index].toAmount,
		    incentive: value=="" ? 0 : parseFloat(value)

		});
    });

	fetch(CONTEXT_PATH + "/ospPayout/save?user=" + userId, {
	    method: "POST",
	    headers: {
	        "Content-Type": "application/json"
	    },
	    body: JSON.stringify(request)
	})
	.then(async response => {

	    let msg = await response.text();

	    if (!response.ok) {
	        throw new Error(msg);
	    }

	    return msg;
	})
	.then(msg => {

	    alert(msg);

	})
	.catch(err => {

	    alert(err.message);

	});
    /*fetch(CONTEXT_PATH + "/ospPayout/save?user=" + userId,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        }
    )
	.then(r => r.json())

	.then(data => {

	    console.log("Saved Response =", data);

	    alert("Submitted Successfully");

	});*/
}


function resetOSP(){

    document.getElementById("ospDpd").selectedIndex = 0;
    document.getElementById("ospFromDate").value = "";
    document.getElementById("ospToDate").value = "";
    document.getElementById("ospBody").innerHTML = "";

    ospRanges = [];

}

/*function viewOSPHistory(){

    window.location.href=

    CONTEXT_PATH+

    "/mainPage/load?master=OSPPAYOUTHISTORY";

}
*/

function viewOSPHistory() {

    let product = document.getElementById("product").value;
    let subProduct = document.getElementById("subProduct").value;
    let dpd = document.getElementById("ospDpd").value;

    let fromDate = document.getElementById("ospFromDate").value;
    let toDate = document.getElementById("ospToDate").value;

    if (dpd == "") {
        alert("Please Select DPD");
        return;
    }

    if (fromDate == "" || toDate == "") {
        alert("Please Select Date Range");
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/ospPayout/latestApprovedId"
        + "?product=" + encodeURIComponent(product)
        + "&subProduct=" + encodeURIComponent(subProduct)
        + "&dpdId=" + encodeURIComponent(dpd)
        + "&fromDate=" + fromDate
        + "&toDate=" + toDate
    )
    .then(r => r.text())
    .then(id => {

        if (!id) {
            alert("No Approved Record Found");
            return;
        }

        window.location.href =
            CONTEXT_PATH +
            "/mainPage/load?master=OSPPAYOUTCOMPAREMAKER"
            + "&id=" + id
            + "&mode=maker";
    });
}