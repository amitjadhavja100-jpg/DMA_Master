/*ospRecoverySlabChecker.js*/

document.addEventListener("DOMContentLoaded",function(){

    loadProducts();

    document.getElementById("agencyChecker").style.display="none";
    document.getElementById("ospChecker").style.display="none";

});



function loadProducts(){

    fetch(CONTEXT_PATH + "/DMAPayoutWeb3/products")

    .then(r=>r.json())

    .then(data=>{

        let product =
        document.getElementById("product");

        product.innerHTML =
        "<option value=''>Select Product</option>";

        data.forEach(function(p){

            product.innerHTML +=
            "<option value='"+p+"'>"+p+"</option>";

        });

    });

}

function loadSubProducts(){

    document.getElementById("agencyChecker").style.display="none";
    document.getElementById("ospChecker").style.display="none";

    let product =
    document.getElementById("product").value;

    let sub =
    document.getElementById("subProduct");

    sub.innerHTML =
    "<option value=''>Select Sub Product</option>";

    if(product==""){
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/DMAPayoutWeb3/subProducts?product=" +
        encodeURIComponent(product)
    )

    .then(r=>r.json())

    .then(data=>{

        data.forEach(function(s){

            sub.innerHTML +=
            "<option value='"+s+"'>"+s+"</option>";

        });

    });

}

function openSelectedProductCheckerScreen(){

    let product =
    document.getElementById("product").value.trim();

    let subProduct =
    document.getElementById("subProduct").value.trim();

    document.getElementById("agencyChecker").style.display="none";
	document.getElementById("flowsPayoutChecker").style.display="none"; //snz-flows
	document.getElementById("valuationPayoutChecker").style.display="none";
    document.getElementById("ospChecker").style.display="none";
    document.getElementById("twChecker").style.display="none";
    document.getElementById("cvChecker").style.display="none";
    document.getElementById("ipkCheckerSection").style.display="none";
        document.getElementById("mpkCheckerSection").style.display="none";
    

    if(product=="Collection - Agency"
        &&
       subProduct=="Recovery Credit Card"){

        document.getElementById("agencyChecker").style.display="block";

        loadAgencyPending();

    }
	else if(product=="Collection - Agency"
	        &&
	        subProduct=="Flows - Credit Card"){

	    console.log("Flows Payout Checker selected");

	    document.getElementById("flowsPayoutChecker").style.display="block";
	    loadFlowsPayoutChecker();

	}
	else if(product=="Collection - Agency"
	        &&
	        subProduct=="Valuation"){

	    console.log("Valuation Payout Checker selected");

	    document.getElementById("valuationPayoutChecker").style.display = "block";
	    loadValuationPayoutChecker();
	}
    else if(product=="Collection- Osp"
        &&
        subProduct=="Recovery Osp"){

        document.getElementById("ospChecker").style.display="block";

        loadOspPending();

    }
     else if(product=="Vehicle loan"
        &&
        subProduct=="TW Coorgination"){
         document.getElementById("cvChecker").style.display="none";
    document.getElementById("mpkCheckerSection").style.display="none";

        document.getElementById("twChecker").style.display="block";

        loadTWPending();

    }
    
     else if(product=="Vehicle loan"
        &&
        subProduct=="CV Coorgination"){
         document.getElementById("twChecker").style.display="none";
         document.getElementById("gridSectionForTW").style.display="none";
    document.getElementById("mpkCheckerSection").style.display="none";

        document.getElementById("cvChecker").style.display="block";

        loadCVPending();

    }
     else if(product=="Vehicle loan"
        &&
        subProduct=="AUTO COUNSELLOR - I process"){
          document.getElementById("twChecker").style.display="none";
    document.getElementById("cvChecker").style.display="none";
    document.getElementById("mpkCheckerSection").style.display="none";
    document.getElementById("ipkCheckerSection").style.display="block";

        loadIPKPending();

    }
    
     else if(product=="Vehicle loan"
        &&
        subProduct=="AUTO COUNSELLOR_ other than Manipal"){
          document.getElementById("twChecker").style.display="none";
    document.getElementById("cvChecker").style.display="none";
    document.getElementById("ipkCheckerSection").style.display="none";
    document.getElementById("mpkCheckerSection").style.display="block";

        loadMPKPending();

    }
     else if(product=="Vehicle loan"
        &&
        subProduct=="Vehicle"){
          document.getElementById("twChecker").style.display="none";
    document.getElementById("cvChecker").style.display="none";
    document.getElementById("ipkCheckerSection").style.display="none";
    document.getElementById("mpkCheckerSection").style.display="none";

        loadManiPalPending();

    }

}

function loadOspPending() {

    fetch(
        CONTEXT_PATH +
        "/ospPayout/pending?user=" + userId
    )

        .then(r => r.json())

        .then(data => {

            let tbody =
                document.getElementById("ospTbody");

            tbody.innerHTML = "";

            data.forEach(function(d) {

                let row = "";

                row += "<tr>";

                row += "<td>" + d.id + "</td>";
                row += "<td>" + d.product + "</td>";
                row += "<td>" + d.subProduct + "</td>";
                row += "<td>" + d.dpdName + "</td>";
                row += "<td>" + formatDateOnly(d.fromDate) + "</td>";
                row += "<td>" + formatDateOnly(d.toDate) + "</td>";
                row += "<td>" + d.status + "</td>";
                row += "<td>" + d.createdBy + "</td>";

                row += "<td>";

                row += "<button class='btn btn-success btn-sm' onclick='approveOSP(" + d.id + ")'>Approve</button>";

                row += " ";

                row += "<button class='btn btn-danger btn-sm' onclick='rejectOSP(" + d.id + ")'>Reject</button>";

                row += " ";

                row += "<button class='btn btn-info btn-sm' onclick='viewOSP(" + d.id + ")'>View</button>";

                row += "</td>";

                row += "</tr>";

                tbody.innerHTML += row;

            });

        });

}

function approveOSP(id){

    fetch(
        CONTEXT_PATH +
        "/ospPayout/approve/" +
        id +
        "?user=" +
        userId,
        {
            method:"POST"
        })

    .then(r=>r.text())

    .then(msg=>{

        alert(msg);

        loadOspPending();

    });

}

function rejectOSP(id){

    let remarks =
    prompt("Enter Remarks");

    fetch(
        CONTEXT_PATH +
        "/ospPayout/reject/" +
        id +
        "?user=" +
        userId +
        "&remarks=" +
        remarks,
        {
            method:"POST"
        })

    .then(r=>r.text())

    .then(msg=>{

        alert(msg);

        loadOspPending();

    });

}

function viewOSP(id){

    window.location.href =
        CONTEXT_PATH +
        "/mainPage/load?master=OSPPAYOUTCOMPARECHECKER"
        + "&id=" + id
        + "&mode=checker";

}

function formatDateOnly(date){

    if(date==null){
        return "";
    }

    return new Date(date).toLocaleDateString("en-GB");
}


function loadTWPending() {

  document.getElementById("cvChecker").style.display="none";

    fetch("/vehicle/tw/pending")

        .then(r => r.json())

        .then(data => {

            let tbody =
                document.getElementById("twbody");

            tbody.innerHTML = "";

			for (let i = 0; i < data.length; i++) {

				let d = data[i];

				let row = "<tr>";

				row += "<td>" + d.id + "</td>";
				row += "<td>" + d.channelPartnerName + "</td>";
				row += "<td>" + formatDateOnly(d.toDate) + "</td>";
				row += "<td>" + d.status + "</td>";
				row += "<td>" + d.createdBy + "</td>";

				// Show Action only for first record of every request
				if (i % 4 === 0) {

					let ids = [
						data[i].id,
						data[i + 1].id,
						data[i + 2].id,
						data[i + 3].id
					];

					row += "<td rowspan='4' style='vertical-align:middle;text-align:center;'>";

					row += "<button class='btn btn-info btn-sm mb-2' " +
						"onclick='viewTW(" + JSON.stringify(ids) + ")'>View</button><br>";

					row += "<button class='btn btn-success btn-sm mb-2' " +
						"onclick='approveTW(" + JSON.stringify(ids) + ")'>Approve</button><br>";

					row += "<button class='btn btn-danger btn-sm' " +
						"onclick='rejectTW(" + JSON.stringify(ids) + ")'>Reject</button>";

					row += "</td>";
				}

				row += "</tr>";

				// Divider after every request
				if ((i + 1) % 4 === 0) {
					row += "<tr><td colspan='6' style='background:#f2f2f2;height:12px;border:none;'></td></tr>";
				}

				tbody.innerHTML += row;
			}

        });

}
function approveTW(ids) {

	fetch( "/vehicle/tw/approve", {

		method: "POST",

		headers: {
			"Content-Type": "application/json"
		},

		body: JSON.stringify(ids)

	})
		.then(r => r.text())
		.then(msg => {
			alert(msg);
			loadTWPending();
		});

}





function rejectTW(ids) {

	fetch(  "/vehicle/tw/reject", {

		method: "POST",

		headers: {
			"Content-Type": "application/json"
		},

		body: JSON.stringify(ids)

	})
		.then(r => r.text())
		.then(msg => {
			alert(msg);
			loadTWPending();
		});

}

function viewTW(ids) {

document.getElementById("twChecker").style.display = "none";
document.getElementById("gridSectionForTW").style.display="none";
	fetch("/vehicle/tw/view", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(ids)
	})
		.then(r => r.json())
		.then(data => {

			// show the existing maker table
			document.getElementById("gridSectionForTW").style.display = "block";

			// hide Add Row, Add Column, Submit
			$("#btnAddRow").hide();
			$("#btnAddColumn").hide();
			$("#submit").hide();

			// bind received data
			bindViewTable(data);
		});
}

function bindViewTable(data){
 
let tbody = document.getElementById("UsedAutoDMABody");
 
tbody.innerHTML="";
 
data.forEach(function(d){
 
let row="<tr>";
 
row+="<td>"+d.channelPartnerName+"</td>";
 
row+="<td><input type='text' value='"+d.rrTillDec22+"' readonly></td>";
 
row+="<td><input type='text' value='"+d.rrFromJan23+"' readonly></td>";
 
row+="<td><input type='text' value='"+d.rrFromAllAug23+"' readonly></td>";
 
row+="</tr>";
 
tbody.innerHTML+=row;
 
});
 
}
 
 
function loadCVPending() {
 document.getElementById("twChecker").style.display="none";

    fetch("/vehicle/cv/pending")

        .then(r => r.json())

        .then(data => {

            let tbody = document.getElementById("cvbody");

            tbody.innerHTML = "";
var grouped ={};
data.forEach(function(d){

var key = d.id;
if(!grouped[key]){
grouped[key]=[];
}
grouped[key].push(d);
});

			for(var key in grouped){
var records = grouped[key];
				records.forEach(function(d, index) {

					let row = "<tr>";

					row += "<td>" + d.id + "</td>";
					row += "<td>" + d.rrFromApril22 + "</td>";
					row += "<td>" + d.rrBeforeApril22 + "</td>";
					row += "<td>" + d.status + "</td>";
					row += "<td>" + d.createdBy + "</td>";

					if (index == 0) {

						let ids = records.map(r => r.id);

						row += "<td rowspan='" + records.length + "'>";

						row += "<button class='btn btn-info btn-sm mb-2' onclick='viewCV(" + JSON.stringify(ids) + ")'>View</button><br>";
						row += "<button class='btn btn-info btn-sm mb-2' onclick='approveCV(" + JSON.stringify(ids) + ")'>Approve</button><br>";
						row += "<button class='btn btn-info btn-sm mb-2' onclick='rejectCV(" + JSON.stringify(ids) + ")'>Reject</button>";

						row += "</td>";
					}

					row += "</tr>";

					tbody.innerHTML += row;
				});

				// Divider between requests
				tbody.innerHTML += "<tr><td colspan='6' style='background:#f2f2f2;height:8px;'></td></tr>";
			
			}

        });

}



function approveCV(ids) {

	fetch( "/vehicle/cv/approve", {

		method: "POST",

		headers: {
			"Content-Type": "application/json"
		},

		body: JSON.stringify(ids)

	})
		.then(r => r.text())
		.then(msg => {
			alert(msg);
			loadTWPending();
		});

}





function rejectCV(ids) {

	fetch(  "/vehicle/cv/reject", {

		method: "POST",

		headers: {
			"Content-Type": "application/json"
		},

		body: JSON.stringify(ids)

	})
		.then(r => r.text())
		.then(msg => {
			alert(msg);
			loadTWPending();
		});

}





/*I process Counsellor for kerala*/



function loadIPKPending() {
	document.getElementById("ipkViewSection").style.display = "none";
	document.getElementById("maniPalCheckerId").style.display = "none";
	document.getElementById("ipkCheckerSection").style.display = "block";

	fetch("/counsellor/pending")
		.then(function(r) { return r.json(); })
		.then(function(data) {

			var tbody = document.getElementById("ipkCheckerBody");
			tbody.innerHTML = "";

			for (var i = 0; i < data.length; i++) {
				try {
					var d = data[i];
					var row = "<tr>";
					row += "<td>" + d.sequence + "</td>";
					row += "<td>" + d.state + "</td>";
					row += "<td>" + formatDateOnly(d.cycleFrom) + "</td>";
					row += "<td>" + formatDateOnly(d.cycleTo) + "</td>";
					row += "<td>" + formatDateOnly(d.createdDate) + "</td>";
					row += "<td>" + d.status + "</td>";
					row += "<td>";
					row += "<button class='btn btn-info btn-sm' onclick='viewIPK(" + d.sequence + ")'>View</button> ";
					row += "<button class='btn btn-success btn-sm' onclick='approveIPK(" + d.sequence + ")'>Approve</button> ";
					row += "<button class='btn btn-danger btn-sm' onclick='rejectIPK(" + d.sequence + ")'>Reject</button>";
					row += "</td>";
					row += "</tr>";

					tbody.innerHTML += row;
				} catch (err) {
					console.error("Row " + i + " failed to render:", err, data[i]);
				}

			}
			loadPendingCommonIPStructure(tbody);
			loadPendingOtherIPStructure(tbody);
			
			
		})
		.catch(function(err) {
			console.error("loadMPKPending fetch failed:", err);
		});


}



function loadPendingCommonIPStructure(tbody){

fetch("/counsellor/ip/common/pending")
.then(function(r) { return r.json(); })
.then(function(data) {
 

 
for (var i = 0; i < data.length; i++) {
var d = data[i];
var row = "<tr>";
row += "<td>" + d.sequence + "</td>";
row += "<td>" + d.state + "</td>";
row += "<td>" + d.cycleFrom + "</td>";
row += "<td>" + d.cycleTo + "</td>";
row += "<td>" + formatDateOnly(d.createdDate) + "</td>";
row += "<td>" + d.status + "</td>";
row += "<td>";
row += "<button class='btn btn-info btn-sm' onclick='viewCommonIPK(" + d.sequence + ")'>View</button> ";
row += "<button class='btn btn-success btn-sm' onclick='approveCommonIPK(" + d.sequence + ")'>Approve</button> ";
row += "<button class='btn btn-danger btn-sm' onclick='rejectCommonIPK(" + d.sequence + ")'>Reject</button>";
row += "</td>";
row += "</tr>";
 
tbody.innerHTML += row;
}
});

}
 
 
function approveIPK(sequence) {
	fetch("/counsellor/approve/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadIPKPending();
		});
}

function rejectIPK(sequence) {
	fetch("/counsellor/reject/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadIPKPending();
		});
}



function viewIPK(sequence) {
	document.getElementById("ipkCheckerSection").style.display = "none";

	fetch("/counsellor/view/" + sequence, { method: "POST" })
		.then(function(r) { return r.json(); })
		.then(function(data) {
			document.getElementById("ipkViewSection").style.display = "block";
			bindIPKViewTables(data);
		});
}


function bindIPKViewTables(data) {
	var inboundRows = "";
	var outboundRows = "";
	var usedCarRows = "";

	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var rowHtml = "<tr>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.category + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.slabFrom + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.slabTo + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.incentiveAmount + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.remarks + "' readonly></td>";
		rowHtml += "</tr>";

		if (d.category === "INBOUND") {
			inboundRows += rowHtml;
		} else if (d.category === "OUTBOUND") {
			outboundRows += rowHtml;
		} else if (d.category === "USED CAR") {
			usedCarRows += rowHtml;
		}
	}

	document.getElementById("ipkInboundBody").innerHTML = inboundRows;
	document.getElementById("ipkOutboundBody").innerHTML = outboundRows;
	document.getElementById("ipkUsedCarBody").innerHTML = usedCarRows;
}


function approveCommonIPK(sequence) {
	fetch("/counsellor/ip/common/approve/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadIPKPending();
		});
}

function rejectCommonIPK(sequence) {
	fetch("/counsellor/ip/common/reject/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadIPKPending();
		});
}

/*other the manipal Counsellor for kerala*/



function loadMPKPending() {
    document.getElementById("mpkViewSection").style.display = "none";
    document.getElementById("maniPalCheckerId").style.display = "none";
    document.getElementById("mpkCheckerSection").style.display = "block";
 
    fetch("/counsellor/mp/pending")
    .then(function(r) { return r.json(); })
    .then(function(data) {
 
        console.log("MPK pending data:", data);
 
        var tbody = document.getElementById("mpkCheckerBody");
        tbody.innerHTML = "";
 
        for (var i = 0; i < data.length; i++) {
            try {
                var d = data[i];
                var row = "<tr>";
                row += "<td>" + d.sequence + "</td>";
                row += "<td>" + d.state + "</td>";
                row += "<td>" + formatDateOnly(d.cycleFrom) + "</td>";
                row += "<td>" + formatDateOnly(d.cycleTo) + "</td>";
                row += "<td>" + formatDateOnly(d.createdDate) + "</td>";
                row += "<td>" + d.status + "</td>";
                row += "<td>";
                row += "<button class='btn btn-info btn-sm' onclick='viewMPK(" + d.sequence + ")'>View</button> ";
                row += "<button class='btn btn-success btn-sm' onclick='approveMPK(" + d.sequence + ")'>Approve</button> ";
                row += "<button class='btn btn-danger btn-sm' onclick='rejectMPK(" + d.sequence + ")'>Reject</button>";
                row += "</td>";
                row += "</tr>";
 
                tbody.innerHTML += row;
            } catch (err) {
                console.error("Row " + i + " failed to render:", err, data[i]);
            }
        }
 
        loadPendingCommonStructure(tbody);
        loadPendingOtherMPStructure(tbody);
    })
    .catch(function(err) {
        console.error("loadMPKPending fetch failed:", err);
    });
}
 
function approveMPK(sequence) {
	fetch("/counsellor/mp/approve/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadMPKPending();
		});
}

function rejectMPK(sequence) {
	fetch("/counsellor/mp/reject/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadMPKPending();
		});
}



function viewMPK(sequence) {
	document.getElementById("mpkCheckerSection").style.display = "none";

	fetch("/counsellor/mp/view/" + sequence, { method: "POST" })
		.then(function(r) { return r.json(); })
		.then(function(data) {
			document.getElementById("mpkViewSection").style.display = "block";
			bindMPKViewTables(data);
		});
}


function bindMPKViewTables(data) {
	var inboundRows = "";
	var outboundRows = "";
	var usedCarRows = "";

	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var rowHtml = "<tr>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.category + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.slabFrom + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.slabTo + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.incentiveAmount + "' readonly></td>";
		rowHtml += "<td><input type='text' class='pct-input' value='" + d.remarks + "' readonly></td>";
		rowHtml += "</tr>";

		if (d.category === "INBOUND") {
			inboundRows += rowHtml;
		} else if (d.category === "OUTBOUND") {
			outboundRows += rowHtml;
		} else if (d.category === "USED CAR") {
			usedCarRows += rowHtml;
		}
	}

	document.getElementById("mpkInboundBody").innerHTML = inboundRows;
	document.getElementById("mpkOutboundBody").innerHTML = outboundRows;
	document.getElementById("mpkUsedCarBody").innerHTML = usedCarRows;
}
function loadPendingCommonStructure(tbody){

fetch("/counsellor/mp/common/pending")
.then(function(r) { return r.json(); })
.then(function(data) {
 

 
for (var i = 0; i < data.length; i++) {
var d = data[i];
var row = "<tr>";
row += "<td>" + d.sequence + "</td>";
row += "<td>" + d.state + "</td>";
row += "<td>" + d.cycleFrom + "</td>";
row += "<td>" + d.cycleTo + "</td>";
row += "<td>" + formatDateOnly(d.createdDate) + "</td>";
row += "<td>" + d.status + "</td>";
row += "<td>";
row += "<button class='btn btn-info btn-sm' onclick='viewCommonMPK(" + d.sequence + ")'>View</button> ";
row += "<button class='btn btn-success btn-sm' onclick='approveCommonMPK(" + d.sequence + ")'>Approve</button> ";
row += "<button class='btn btn-danger btn-sm' onclick='rejectCommonMPK(" + d.sequence + ")'>Reject</button>";
row += "</td>";
row += "</tr>";
 
tbody.innerHTML += row;
}
});

}
function approveCommonMPK(sequence) {
	fetch("/counsellor/mp/common/approve/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadMPKPending();
		});
}

function rejectCommonMPK(sequence) {
	fetch("/counsellor/mp/common/reject/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadMPKPending();
		});
}

function loadPendingOtherIPStructure(tbody){

fetch("/counsellor/ip/other/pending")
.then(function(r) { return r.json(); })
.then(function(data) {
 

 
for (var i = 0; i < data.length; i++) {
var d = data[i];
var row = "<tr>";
row += "<td>" + d.sequence + "</td>";
row += "<td>" + d.state + "</td>";
row += "<td>" + d.cycleFrom + "</td>";
row += "<td>" + d.cycleTo + "</td>";
row += "<td>" + formatDateOnly(d.createdDate) + "</td>";
row += "<td>" + d.status + "</td>";
row += "<td>";
row += "<button class='btn btn-info btn-sm' onclick='viewOtherIPK(" + d.sequence + ")'>View</button> ";
row += "<button class='btn btn-success btn-sm' onclick='approveOtherIPK(" + d.sequence + ")'>Approve</button> ";
row += "<button class='btn btn-danger btn-sm' onclick='rejectOtherIPK(" + d.sequence + ")'>Reject</button>";
row += "</td>";
row += "</tr>";
 
tbody.innerHTML += row;
}
});

}



function approveOtherIPK(sequence) {
	fetch("/counsellor/ip/other/approve/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadIPKPending();
		});
}

function rejectOtherIPK(sequence) {
	fetch("/counsellor/ip/other/reject/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadIPKPending();
		});
}

function loadPendingOtherMPStructure(tbody){

fetch("/counsellor/mp/other/pending")
.then(function(r) { return r.json(); })
.then(function(data) {
 

 
for (var i = 0; i < data.length; i++) {
var d = data[i];
var row = "<tr>";
row += "<td>" + d.sequence + "</td>";
row += "<td>" + d.state + "</td>";
row += "<td>" + d.cycleFrom + "</td>";
row += "<td>" + d.cycleTo + "</td>";
row += "<td>" + formatDateOnly(d.createdDate) + "</td>";
row += "<td>" + d.status + "</td>";
row += "<td>";
row += "<button class='btn btn-info btn-sm' onclick='viewOtherMPK(" + d.sequence + ")'>View</button> ";
row += "<button class='btn btn-success btn-sm' onclick='approveOtherMPK(" + d.sequence + ")'>Approve</button> ";
row += "<button class='btn btn-danger btn-sm' onclick='rejectOtherMPK(" + d.sequence + ")'>Reject</button>";
row += "</td>";
row += "</tr>";
 
tbody.innerHTML += row;
}
});

}

function approveOtherMPK(sequence) {
	fetch("/counsellor/mp/other/approve/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadMPKPending();
		});
}

function rejectOtherMPK(sequence) {
	fetch("/counsellor/mp/other/reject/" + sequence, { method: "POST" })
		.then(function(r) { return r.text(); })
		.then(function(msg) {
			alert(msg);
			loadMPKPending();
		});
}
function loadManiPalPending() {
    const container = document.getElementById("maniPalCheckerId");
    const tbody = document.getElementById("maniPalCheckerBody");
    if (!tbody) {
        console.error("Error: Element with id 'maniPalCheckerBody' was not found in the DOM.");
        return;
    }
    if (container) container.style.display = "block";
    fetch("/counsellor/retainer/fetchingPending")
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                tbody.innerHTML = `
<tr>
<td colspan="9" class="text-center text-muted py-4 fw-semibold">
                            No pending records found.
</td>
</tr>`;
                return;
            }
            // Reset Action Header visibility if previously hidden
            const table = tbody.closest("table");
            const actionHeader = table ? table.querySelector("thead th:nth-child(9)") : document.querySelector("table th:last-child");
            if (actionHeader) actionHeader.style.display = "";
            // 1. Separate into INBOUND and USED arrays
            const inboundData = data.filter(d => (d.caseType || '').toUpperCase().includes("INBOUND"));
            const usedData = data.filter(d => (d.caseType || '').toUpperCase().includes("USED"));
            // 2. Sort both arrays by Slab
            const sortBySlab = (a, b) => (a.slab || '').localeCompare(b.slab || '');
            inboundData.sort(sortBySlab);
            usedData.sort(sortBySlab);
            // 3. Combine both into a single dataset reference
            const combinedData = [...inboundData, ...usedData];
            const mainSeqId = combinedData[0]?.sequence || combinedData[0]?.id;
            let htmlContent = "";
            const GROUP_SIZE = 6;
            // 4. Render INBOUND rows
            inboundData.forEach((d, index) => {
                htmlContent += `
<tr class="align-middle border-bottom border-secondary">
<td class="fw-bold text-center py-2 border-end border-secondary">${d.slab || ''}</td>
<td class="text-center fw-semibold py-2 border-end border-secondary">
<span class="badge ${d.badgeBgClass || ''} px-2 py-1">${d.caseType || ''}</span>
</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.casesFrom ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.casesTo ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.currentFixedSalaryPerc ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.currentIncentivePerc ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.perCaseAmount ?? ''}</td>
<td class="text-start text-wrap small py-2 border-end border-secondary px-3">${d.remarks || ''}</td>`;
                if (index % GROUP_SIZE === 0) {
                    const remainingRows = inboundData.length - index;
                    const currentSpan = Math.min(GROUP_SIZE, remainingRows);
                    htmlContent += `
<td rowspan="${currentSpan}" class="text-center align-middle py-2 border-start border-secondary">
<div class="d-flex flex-column justify-content-center align-items-center gap-2">
<button class="btn btn-outline-info btn-sm px-3 mb-3 fw-bold w-100" onclick="viewManiPal(this)">View</button>
<button class="btn btn-success btn-sm px-3 fw-bold mb-3 w-100" onclick="approveManiPal(${mainSeqId})">Approve</button>
<button class="btn btn-danger btn-sm px-3 fw-bold mb-3 w-100" onclick="rejectManiPal(${mainSeqId})">Reject</button>
</div>
</td>`;
                }
                htmlContent += `</tr>`;
            });
            // 5. Render Section Separator
            if (inboundData.length > 0 && usedData.length > 0) {
                htmlContent += `
<tr class="table-dark">
<td colspan="6" class="text-center fw-bold text-uppercase py-2 text-white" style="letter-spacing: 1px;">
                        USED SECTION
</td>
</tr>`;
            }
            // 6. Render USED rows
            usedData.forEach((d, index) => {
                htmlContent += `
<tr class="align-middle border-bottom border-secondary">
<td class="fw-bold text-center py-2 border-end border-secondary">${d.slab || ''}</td>
<td class="text-center fw-semibold py-2 border-end border-secondary">
<span class="badge ${d.badgeBgClass || ''} px-2 py-1">${d.caseType || ''}</span>
</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.casesFrom ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.casesTo ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.currentFixedSalaryPerc ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.currentIncentivePerc ?? ''}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${d.perCaseAmount ?? ''}</td>
<td class="text-start text-wrap small py-2 border-end border-secondary px-3">${d.remarks || ''}</td>`;
                if (index % GROUP_SIZE === 0) {
                    const remainingRows = usedData.length - index;
                    const currentSpan = Math.min(GROUP_SIZE, remainingRows);
                    htmlContent += `
<td rowspan="${currentSpan}" class="text-center align-middle py-2 border-start border-secondary">
<div class="d-flex flex-column justify-content-center align-items-center gap-2">
<button class="btn btn-outline-info btn-sm px-3 mb-3 fw-bold w-100" onclick="viewManiPal(this)">View</button>
<button class="btn btn-success btn-sm px-3 fw-bold mb-3 w-100" onclick="approveManiPal(${mainSeqId})">Approve</button>
<button class="btn btn-danger btn-sm px-3 fw-bold mb-3 w-100" onclick="rejectManiPal(${mainSeqId})">Reject</button>
</div>
</td>`;
                }
                htmlContent += `</tr>`;
            });
            tbody.innerHTML = htmlContent;
        })
        .catch(error => {
            console.error("Fetch error:", error);
            tbody.innerHTML = `
<tr>
<td colspan="6" class="text-center text-danger py-4 fw-bold">
                        Failed to load data. Please try again later.
</td>
</tr>`;
        });
}


function viewManiPal(buttonElement) {

    // 1. Get the table element containing the button

    const table = buttonElement.closest("table");

    if (!table) return;

    // 2. Hide the "Action" header in <thead> (assuming it's the 9th column)

    const actionHeader = table.querySelector("thead th:nth-child(9)")

                      || table.querySelector("thead th:last-child");

    if (actionHeader) {

        actionHeader.style.display = "none";

    }

    // 3. Hide all Action column cells (<td>) with rowspan in <tbody>

    const actionCells = table.querySelectorAll("tbody td[rowspan]");

    actionCells.forEach(td => {

        td.style.display = "none";

    });

    // Optional: Add any extra view logic here (e.g., opening a modal or expanding details)

}

 
function approveManiPal(sequence) {

	fetch("/counsellor/retainer/approve/" + sequence, { method: "POST" })

		.then(function(r) { return r.text(); })

		.then(function(msg) {

			alert(msg);

			loadManiPalPending();

		});

}

function rejectManiPal(sequence) {

	fetch("/counsellor/retainer/reject/" + sequence, { method: "POST" })

		.then(function(r) { return r.text(); })

		.then(function(msg) {

			alert(msg);

			loadManiPalPending();

		});

}

//Wrapper function called by the Approve button

function approveStatusEducation(recordId) {

    if (confirm("Are you sure you want to approve this record?")) {

        processCheckerAction(recordId, "APPROVE");

    }

}

// Wrapper function called by the Reject button

function rejectStatusEducation(recordId) {

    if (confirm("Are you sure you want to reject this record?")) {

        processCheckerAction(recordId, "REJECT");

    }

}
function processCheckerAction(recordId, actionType) {

    if (!recordId) {

        alert("Error: Record ID is missing!");

        return;

    }

    // Match your Java DTO structure perfectly

    const payload = {

        tempIds: [Number(recordId)], // Must be an Array/List: [33]

        action: actionType,          // "APPROVE" or "REJECT"

        checkerId: "CHECKER_USER",   // Pass checker ID or empty string

        rejectionReason: actionType === "REJECT" ? "Rejected by checker" : null

    };

    fetch("/api/v1/slabs/checker/action", {

        method: "POST",

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(payload)

    })

    .then(async response => {

        const data = await response.json();

        if (!response.ok || data.success === false) {

            throw new Error(data.message || data.error || "Action failed.");

        }

        return data;

    })

    .then(data => {

        alert(data.message || `Record successfully ${actionType.toLowerCase()}d!`);

        // Refresh table

        if (typeof loadEducationLoanPending === "function") {

            loadEducationLoanPending();

        }

    })

    .catch(error => {

        console.error("Checker Action Error:", error);

        alert("Error: " + error.message);

    });

}

function loadEducationLoanPending() {

    const container = document.getElementById("educationLoanCheckerContainer");

    const tbody = document.getElementById("educationLoanCheckerBody");

    if (!tbody) {

        console.error("Error: Element with id 'educationLoanCheckerBody' was not found in the DOM.");

        return;

    }

    if (container) {

        container.style.display = "block";

    }

    // Call Education Loan Pending API Endpoint

    fetch("/api/v1/slabs/checker/pending")

        .then(response => {

            if (!response.ok) {

                throw new Error(`HTTP error! Status: ${response.status}`);

            }

            return response.json();

        })

        .then(data => {

            // Check if array is empty

            if (!Array.isArray(data) || data.length === 0) {

                tbody.innerHTML = `
<tr>
<td colspan="8" class="text-center text-muted py-4 border-bottom">

                            No pending education loan records found.
</td>
</tr>`;

                return;

            }

            let sectionHtml = "";

            data.forEach(d => {

                // Mapping API keys correctly

                const category = d.productCategory ?? "";

                const payoutType = d.bucketCode ?? "";

                const minVal = (d.minAmount !== null && d.minAmount !== undefined) ? d.minAmount : "-";

                const maxVal = (d.maxAmount !== null && d.maxAmount !== undefined) ? d.maxAmount : "-";

                const payout = d.payoutPercentage ?? "";

                // Format dates (YYYY-MM-DD)

                const cycleFrom = d.cycleFromDate ? d.cycleFromDate.split("T")[0] : "";

                const cycleTo = d.cycleToDate ? d.cycleToDate.split("T")[0] : "";

                const recordId = d.tempId;

                sectionHtml += `
<tr class="align-middle border-bottom">
<td class="fw-bold text-center py-2 border-end">${category}</td>
<td class="text-center fw-semibold py-2 border-end">${payoutType}</td>
<td class="text-center fw-medium py-2 border-end">${minVal}</td>
<td class="text-center fw-medium py-2 border-end">${maxVal}</td>
<td class="text-center fw-medium py-2 border-end">${payout}</td>
<td class="text-center fw-medium py-2 border-end">${cycleFrom}</td>
<td class="text-center fw-medium py-2 border-end">${cycleTo}</td>
<td class="text-center py-2">
<div class="d-flex justify-content-center gap-1">
<button class="btn btn-success btn-sm px-2 mr-2" onclick="approveStatusEducation(${recordId})">Approve</button>
<button class="btn btn-danger btn-sm px-2" onclick="rejectStatusEducation(${recordId})">Reject</button>
</div>
</td>
</tr>`;

            });

            tbody.innerHTML = sectionHtml;

        })

        .catch(error => {

            console.error("Fetch error:", error);

            tbody.innerHTML = `
<tr>
<td colspan="8" class="text-center text-danger py-4 border-bottom">

                        Failed to load data. Please try again later.
</td>
</tr>`;

        });

}
 
function loadPersonalLoanPending() {

    const container = document.getElementById("personalLoanCheckerContainer");

    const tbody = document.getElementById("personalLoanCheckerBody");

    if (!tbody) {

        console.error("Error: Element with id 'personalLoanCheckerBody' was not found in the DOM.");

        return;

    }

    if (container) {

        container.style.display = "block";

    }

    // Call Personal Loan Pending API Endpoint

    fetch("/api/slabs/pending")

        .then(response => {

            if (!response.ok) {

                throw new Error(`HTTP error! Status: ${response.status}`);

            }

            return response.json();

        })

        .then(data => {

            if (!Array.isArray(data) || data.length === 0) {

                tbody.innerHTML = `
<tr>
<td colspan="7" class="text-center text-muted py-4">

                            No pending personal loan records found.
</td>
</tr>`;

                return;

            }

            let sectionHtml = "";

            data.forEach((d) => {

                const minVal = d.min ?? '';

                const maxVal = d.max ?? '';

                const payout = d.payout ?? '';

                const payoutType = d.payoutType ?? '';

                const category = d.category ?? '';

                const recordId = d.id;

                sectionHtml += `
<tr class="align-middle border-bottom border-secondary">
<td class="fw-bold text-center py-2 border-end border-secondary">${category}</td>
<td class="text-center fw-semibold py-2 border-end border-secondary">${payoutType}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${minVal}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${maxVal}</td>
<td class="text-center fw-medium py-2 border-end border-secondary">${payout}</td>
<td class="text-center py-2">
<div class="">
<button class="btn btn-success btn-sm px-2" onclick="approveStatus(${recordId})">Approve</button>
<button class="btn btn-danger btn-sm px-2" onclick="rejectStatus(${recordId})">Reject</button>
</div>
</td>
</tr>`;

            });

            tbody.innerHTML = sectionHtml;

        })

        .catch(error => {

            console.error("Fetch error:", error);

            tbody.innerHTML = `
<tr>
<td colspan="7" class="text-center text-danger py-4">

                        Failed to load data. Please try again later.
</td>
</tr>`;

        });

}

function approveStatus(id) {
    if (confirm("Are you sure you want to approve this slab?")) {
        updateSlabStatus(id, "APPROVED");
    }
}
// Triggered on clicking "Reject" button
function rejectStatus(id) {
    if (confirm("Are you sure you want to reject this slab?")) {
        updateSlabStatus(id, "REJECTED");
    }
}
 