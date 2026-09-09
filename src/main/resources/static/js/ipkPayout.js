/* I process counsellor for kerala */





function fetchDataIPKStructure(){
let stateIPK = document.getElementById("stateIPK").value;
let cycleFrom = document.getElementById("cyclefromDateIPK").value;
let subProductIPK = document.getElementById("subProduct").value;
	

		let cycleTo = document.getElementById("cycletoDateIPK").value;

	if (!cycleFrom || !cycleTo || !stateIPK) {
		alert("Please select Cycle From and Cycle To dates and state.");
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
	
		
		
		
		if(stateIPK === "kerala"){
			var grid = document.getElementById("commonStructureIPCAllTables");
		grid.style.display = "none";
			var grid = document.getElementById("commonStructureIPPCTables");
		grid.style.display = "none";
			var grid = document.getElementById("IPCPANIndiaAllTables");
		grid.style.display = "none";
var gridAll = document.getElementById("counsellorAllTables");
		gridAll.style.display = "block";
		bindInboundTable();
		bindOutboundTable();
		usedCarTable();
	} else if (stateIPK === "commonStructure") {
	alert("common structure");
	var gridAll=document.getElementById("counsellorAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("commonStructureIPPCTables");
		grid.style.display = "none";
			var grid = document.getElementById("IPCPANIndiaAllTables");
		grid.style.display = "none";
		var grid = document.getElementById("commonStructureIPCAllTables");
		grid.style.display = "block";
		bindApsCodeIPCTable();
				bindBrokerIPCTable();
				bindSourcingIPCTable();
	}
	
	else if (stateIPK === "otherRegion") {
	alert("otherRegion");
	var gridAll=document.getElementById("counsellorAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("commonStructureIPCAllTables");
		grid.style.display = "none";
			var grid = document.getElementById("IPCPANIndiaAllTables");
		grid.style.display = "none";
		var grid = document.getElementById("commonStructureIPPCTables");
		grid.style.display = "block";
		
		bindApsIPKPUTable();
	    bindBrokerIPKPUTable();
				
	}
	else if (stateIPK === "PANIndia") {
	alert("pan india");
	var gridAll=document.getElementById("counsellorAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("commonStructureIPCAllTables");
		grid.style.display = "none";
		var grid = document.getElementById("commonStructureIPPCTables");
		grid.style.display = "none";
		var grid = document.getElementById("IPCPANIndiaAllTables");
		grid.style.display = "block";
		
				
	}
	
	else if (stateIPK === "retainers") {
	alert("retainers");
	
	let gridRet = document.getElementById("table-container");
	 
	var gridAll=document.getElementById("counsellorAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("commonStructureIPCAllTables");
		grid.style.display = "none";
		var grid = document.getElementById("commonStructureIPPCTables");
		grid.style.display = "none";
		var grid = document.getElementById("IPCPANIndiaAllTables");
		grid.style.display = "none";
		var grid = document.getElementById("manipalContent");
		grid.style.display = "block";
		gridRet.style.display = "block";
		console.log("script loaded");
		
            // Define Column Headers and field mapping
console.log("inside DOMContentLoaded");        
    const columns = [
                { header: "Slab", field: "slab" },
                { header: "New car and Used car Inbound / Outbound", field: "newAndUsedCar" },
                { header: "Used Car ( All used car cases)", field: "usedCar" },
                { header: "Structure", field: "remark" }
            ];
 
            // Define Table Row Data (Extracted from image)
            const tableData = [
                {
                    slab: "L1",
                    newAndUsedCar: ">=6 cases",
                    usedCar: ">=5 cases",
                    remark: "100% of current Fixed salary + incentive as per current incentive structure"
                },
                {
                    slab: "L2",
                    newAndUsedCar: "4-5 cases",
                    usedCar: "3 to 4 cases",
                    remark: "50% of current Fixed salary + incentive as per current incentive structure"
                },
                {
                    slab: "L3",
                    newAndUsedCar: "1-3 cases",
                    usedCar: "1 to 2 cases",
                    remark: "Rs. 1500 per case only"
                }
            ];
 
 
            // Call standard function to build the table
            createDynamicTables({
                containerId: "table-container",
                columns: columns,
                data: tableData,
            });
       
		
				
	}
}


function bindApsIPKPUTable() {
	var tbody = document.getElementById("ApsCodeIPPNBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='230552' ></td>";
	rows += "<td><input type='text' class='pct-input' value='FINSURE FINANCIAL SERVICES LLP' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='283900' ></td>";
	rows += "<td><input type='text' class='pct-input' value='RULOANS DISTRIBUTION SER P LTD' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='206136' ></td>";
	rows += "<td><input type='text' class='pct-input' value='	SHRI SAI FINANCIAL SERVICES' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='224548' ></td>";
	rows += "<td><input type='text' class='pct-input' value='NORTHWEST CAPITAL' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='230551' ></td>";
	rows += "<td><input type='text' class='pct-input' value='ASCENT AUTOFIN CONSULTANCY LLP' ></td>";
	rows += "</tr>";
	
	
	

	tbody.innerHTML = rows;
}
function bindBrokerIPKPUTable() {
	var tbody = document.getElementById("ApsCodeIPKOBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='171659' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Almighty Financial Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='194573' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='197846' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Jayalakshmi Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='183737' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Kavin Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='175552' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Yuvasree Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207040' ></td>";
	rows += "<td><input type='text' class='pct-input' value='A S Enterprises' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207946' ></td>";
	rows += "<td><input type='text' class='pct-input' value='R D Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='208632' ></td>";
	rows += "<td><input type='text' class='pct-input' value='D S Credit' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='212170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Marketing Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='341170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='V S ENTERPRISES' ></td>";
	rows += "</tr>";
	

	tbody.innerHTML = rows;
}


function bindInboundTable() {

	var tbody = document.getElementById("InboundBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='6'>Inbound</td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='1' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='5' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td  rowspan='6'><textarea style ='height:140px; width:100%;' class='pct-input'>Payment to be done based on per car incentive only starting 6th case</textarea></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='6' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='10' ></td>";
	rows += "<td><input type='text' class='pct-input' value='700' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='15' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1000' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='16' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='20' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1500' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='21' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='30' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2000' ></td>";
	rows += "</tr>";

	rows += "<tr class='min-rate-row'>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='31' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='99999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2500' ></td>";
	rows += "</tr>";

	tbody.innerHTML = rows;
}



function bindOutboundTable() {

	var tbody = document.getElementById("OutboundBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='6'>OutboundBody</td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='1' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='5' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td  rowspan='6'><textarea style ='height:140px; width:100%;' class='pct-input'>Payment to be done based on per car incentive only starting 6th case</textarea></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='6' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='10' ></td>";
	rows += "<td><input type='text' class='pct-input' value='700' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='15' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1000' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='16' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='20' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1500' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='21' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='30' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2000' ></td>";
	rows += "</tr>";

	rows += "<tr class='min-rate-row'>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='31' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='99999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2500' ></td>";
	rows += "</tr>";

	tbody.innerHTML = rows;
}

function usedCarTable() {

	var tbody = document.getElementById("UsedCarsBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='6'>Used Car</td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='3' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td  rowspan='6'><textarea style ='height:140px; width:100%;' class='pct-input'>Payment to be done based on per car incentive only starting 6th case</textarea></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='4' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='6' ></td>";
	rows += "<td><input type='text' class='pct-input' value='800' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='7' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1300' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='12' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='20' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2000' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='21' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='9999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2500' ></td>";
	rows += "</tr>";

	

	tbody.innerHTML = rows;
}


function saveIPKStructure() {
	var state = document.getElementById("stateIPK").value;
	var subProduct = document.getElementById("subProduct").value;
	var fromDate = document.getElementById("cyclefromDateIPK").value;
	var toDate = document.getElementById("cycletoDateIPK").value;
	

	if (state === "kerala") {
		var categories = [];
		categories.push(collectCategoryRows("InboundBody", "INBOUND"));
		categories.push(collectCategoryRows("OutboundBody", "OUTBOUND"));
		categories.push(collectCategoryRows("UsedCarsBody", "USED CAR"));

		var payload = {
			state: state,
			cycleFrom: fromDate,
			cycleTo: toDate,
			categories: categories,
			counsellorType: subProduct
		};
		fetch("/counsellor/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		})
			.then(function(response) {
				if (!response.ok) {
					throw new Error("Save failed");
				}
				return response.text();
			})
			.then(function(data) {
				alert("Structure submitted for approval successfully");
				window.location.reload();
			})
			.catch(function(error) {
				alert("Error saving structure: " + error.message);
			});
	} else if (state === "commonStructure") {

		var payload = {
			state: state,
			cycleFromDate: fromDate,
			cycleToDate: toDate,
			counsellorType: subProduct,
			apsRows: collectApsIPCRows(),
			brokerRows: collectBrokerIPCRows(),
			sourcingRows: collectSourcingIPCRows()
		};

		fetch("/counsellor/common/structure/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		})
			.then(function(response) {
				if (!response.ok) { throw new Error("Save failed"); }
				return response.text();
			})
			.then(function(msg) {
				alert(msg);
				window.location.reload();
			})
			.catch(function(error) {
				alert("Error saving structure: " + error.message);
			});
	}
	
	
	else if (state === "otherRegion") {

		var payload = {
			state: state,
			cycleFromDate: fromDate,
			cycleToDate: toDate,
			counsellorType: subProduct,
			apsPNRows: collectApsPNIPCRows(),
			apsKLRows: collectApsKLIPCRows(),
			
		};

		fetch("/counsellor/other/structure/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		})
			.then(function(response) {
				if (!response.ok) { throw new Error("Save failed"); }
				return response.text();
			})
			.then(function(msg) {
				alert(msg);
				window.location.reload();
			})
			.catch(function(error) {
				alert("Error saving structure: " + error.message);
			});
	}
	
	/* pan india i process */
	else if (state === "PANIndia") {

		  const datalist = [];
        const rows = document.querySelectorAll("#incentiveTable .data-row");
        rows.forEach(row => {
            const rowData = {
    		srNo: parseInt(row.querySelector(".srNo").value) || 0,
        type: row.querySelector(".catType").value || "",
        state: row.querySelector(".state").value || "",
        fromSlab: parseInt(row.querySelector(".fromSlab").value) || 0,
        toSlab: parseInt(row.querySelector(".toSlab").value) || 0,
        percentage: parseFloat(row.querySelector(".percentage").value) || 0.0,
        fixedAmount: parseFloat(row.querySelector(".fixAmt").value) || 0.0,
        maxIncentive: parseFloat(row.querySelector(".maxIncentive").value) || 0.0,
        capping: parseFloat(row.querySelector(".cap").value) || 0.0,
        maxSalaryCap: parseFloat(row.querySelector(".maxSalaryCap").value) || 0.0,
        remark: row.querySelector(".remark").value || ""
            };
            datalist.push(rowData);
        }); 
        const payload = {
                cycleFromDate: fromDate,
                cycleToDate: toDate,
                dtos: datalist
            };
        console.log("Submitting Payout Matrix to Backend:", datalist);
        fetch("/counsellor/panindia/saveAll", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload) 
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Server returned error response code: " + response.status);
            }
        })
        .then(message => {
            //alert(message);
            alert("Iprocess structure saved sucessfully saved into Database ");
            window.location.reload();
        })
        .catch(error => {
            console.error("Network Transmission Fault:", error);
            alert("Failed to transmit incentive mapping updates: " + error.message);
        }); 
    }


/* retainer i process */
	else if (state === "retainers") {

		   saveData(state,subProduct,fromDate,toDate);
    }

	}
	
	
	function collectApsPNIPCRows() {
	var tbody = document.getElementById("ApsCodeIPPNBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			apsCode: inputs[0].value,
			channelName: inputs[1].value
		});
	}
	return rowsArr;
}
	function collectApsKLIPCRows() {
	var tbody = document.getElementById("ApsCodeIPKOBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			apsCode: inputs[0].value,
			channelName: inputs[1].value
		});
	}
	return rowsArr;
}

function collectApsIPCRows() {
	var tbody = document.getElementById("ApsCodeIPCBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			apsCode: inputs[0].value,
			channelName: inputs[1].value
		});
	}
	return rowsArr;
}

function collectBrokerIPCRows() {
	var tbody = document.getElementById("BrokerIPCBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			brokerId: inputs[0].value,
			brokerName: inputs[1].value,
			newCar: inputs[2].value,
			usedCar: inputs[3] ? inputs[3].value : "",
			attachmentIncentive: inputs[4] ? inputs[4].value : ""
		});
	}
	return rowsArr;
}


function collectSourcingIPCRows() {
	var tbody = document.getElementById("SourcingIPCBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");

		var pctRaw = inputs[2].value;
		var pctNum = parseFloat(pctRaw);
		if (isNaN(pctNum)) { pctNum = 0; }

		var row = {
			category: "USED_CARS",
			slabFrom: inputs[0].value,
			slabTo: inputs[1].value,
			percentage: pctNum,
			remarks: inputs[3] ? inputs[3].value : ""
		};

		// designation input only present on rowspan rows (first row of each group)
		if (inputs.length > 4) {
			row.designation = inputs[4].value;
		}

		rowsArr.push(row);
	}
	return rowsArr;
}


function collectCategoryRows(tbodyId, categoryName) {
	var tbody = document.getElementById(tbodyId);
	var trList = tbody.getElementsByTagName("tr");
	var remarksVal = "";
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		var slabFrom = inputs[0].value;
		var slabTo = inputs[1].value;
		var incentiveAmount = inputs[2].value;

		rowsArr.push({
			slabFrom: slabFrom,
			slabTo: slabTo,
			incentiveAmount: incentiveAmount
		});

		var textarea = trList[i].querySelector("textarea.pct-input");
		if (textarea != null) {
			remarksVal = textarea.value;
		}
	}

	return {
		category: categoryName,
		remarks: remarksVal,
		rows: rowsArr
	};
}




/* other than manipal for kerala */




function fetchDataMPKStructure(){
let stateMPK = document.getElementById("stateMPK").value;
let cycleFromMPK = document.getElementById("cyclefromDateMPK").value;
		let cycleToMPK = document.getElementById("cycletoDateMPK").value;

	if (!cycleFromMPK || !cycleToMPK || !stateIPK) {
		alert("Please select Cycle From and Cycle To dates and state.");
		return;
	}

	let fromDateMPK = new Date(cycleFromMPK);
	let toDateMPK = new Date(cycleToMPK);

	if (fromDateMPK >= toDateMPK) {
		alert("Cycle From date must be earlier than Cycle To date.");
		return;
	}

	let diffInMs = toDateMPK - fromDateMPK;
	let diffInDays = diffInMs / (1000 * 60 * 60 * 24);

	if (diffInDays > 30) {
		alert("The gap between Cycle From and Cycle To must not exceed 30 days.");
		return;
	}
	if(stateMPK === "kerala"){
	var gridcommon=document.getElementById("commonStructureAllTables");
	gridcommon.style.display = "none";
		var grid = document.getElementById("manipalAllTables");
		grid.style.display = "block";
		bindInboundMPTable();
		bindOutboundMPTable();
		usedCarMPTable();
	} else if (stateMPK === "commonStructure") {
	alert("common structure");
	var gridAll=document.getElementById("manipalAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("commonStructureAllTables");
		grid.style.display = "block";
		bindApsCodeTable();
				bindBrokerTable();
				bindSourcingTable();
	}
	 else if (stateMPK === "otherRegion") {
	alert("other region");
	var gridAll=document.getElementById("manipalAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("commonStructureMPPCTables");
		grid.style.display = "block";
		bindBrokerMPKPUTable();
		bindApsMPKPUTable();
	}
	 else if (stateMPK === "PANIndia") {
	alert("Pan india");
	var gridAll=document.getElementById("manipalAllTables");
	gridAll.style.display = "none";
		var grid = document.getElementById("manipalTable");
		grid.style.display = "block";
		
	}
}




function bindInboundMPTable() {

	var tbody = document.getElementById("InboundMPBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='6'>Inbound</td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='1' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='5' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td  rowspan='6'><textarea style ='height:140px; width:100%;' class='pct-input'>Payment to be done based on per car incentive only starting 6th case</textarea></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='6' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='10' ></td>";
	rows += "<td><input type='text' class='pct-input' value='700' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='15' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1000' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='16' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='20' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1500' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='21' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='30' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2000' ></td>";
	rows += "</tr>";

	rows += "<tr class='min-rate-row'>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='31' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='99999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2500' ></td>";
	rows += "</tr>";

	tbody.innerHTML = rows;
}



function bindOutboundMPTable() {

	var tbody = document.getElementById("OutboundMPBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='6'>OutboundBody</td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='1' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='5' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td  rowspan='6'><textarea style ='height:140px; width:100%;' class='pct-input'>Payment to be done based on per car incentive only starting 6th case</textarea></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='6' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='10' ></td>";
	rows += "<td><input type='text' class='pct-input' value='700' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='15' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1000' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='16' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='20' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1500' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='21' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='30' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2000' ></td>";
	rows += "</tr>";

	rows += "<tr class='min-rate-row'>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='31' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='99999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2500' ></td>";
	rows += "</tr>";

	tbody.innerHTML = rows;
}

function usedCarMPTable() {

	var tbody = document.getElementById("UsedCarsMPBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='6'>Used Car</td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='3' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td  rowspan='6'><textarea style ='height:140px; width:100%;' class='pct-input'>Payment to be done based on per car incentive only starting 6th case</textarea></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='4' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='6' ></td>";
	rows += "<td><input type='text' class='pct-input' value='800' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='7' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td><input type='text' class='pct-input' value='1300' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='12' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='20' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2000' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='21' ></td>";
	rows += "<td class='row-label'><input type='text' class='pct-input' value='9999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='2500' ></td>";
	rows += "</tr>";

	

	tbody.innerHTML = rows;
}


function saveMPKStructure() {
	var state = document.getElementById("stateMPK").value;
	var subProduct = document.getElementById("subProduct").value;
	var fromDate = document.getElementById("cyclefromDateMPK").value;
	var toDate = document.getElementById("cycletoDateMPK").value;
	
	if (state === "kerala") {
		var categories = [];
		categories.push(collectCategoryRows("InboundMPBody", "INBOUND"));
		categories.push(collectCategoryRows("OutboundMPBody", "OUTBOUND"));
		categories.push(collectCategoryRows("UsedCarsMPBody", "USED CAR"));

		var payload = {
			state: state,
			cycleFrom: fromDate,
			cycleTo: toDate,
			categories: categories,
			counsellorType: subProduct
		};
		fetch("/counsellor/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		})
			.then(function(response) {
				if (!response.ok) {
					throw new Error("Save failed");
				}
				return response.text();
			})
			.then(function(data) {
				alert("Structure submitted for approval successfully");
				window.location.reload();
			})
			.catch(function(error) {
				alert("Error saving structure: " + error.message);
			});
	} else if (state === "commonStructure") {

		var payload = {
			state: state,
			cycleFromDate: fromDate,
			cycleToDate: toDate,
			counsellorType: subProduct,
			apsRows: collectApsRows(),
			brokerRows: collectBrokerRows(),
			sourcingRows: collectSourcingRows()
		};

		fetch("/counsellor/common/structure/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		})
			.then(function(response) {
				if (!response.ok) { throw new Error("Save failed"); }
				return response.text();
			})
			.then(function(msg) {
				alert(msg);
				window.location.reload();
			})
			.catch(function(error) {
				alert("Error saving structure: " + error.message);
			});
	}
	
	else if (state === "otherRegion") {

		var payload = {
			state: state,
			cycleFromDate: fromDate,
			cycleToDate: toDate,
			counsellorType: subProduct,
			apsPNRows: collectApsPNMPCRows(),
			apsKLRows: collectApsKLMPCRows(),
			
		};

		fetch("/counsellor/other/structure/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		})
			.then(function(response) {
				if (!response.ok) { throw new Error("Save failed"); }
				return response.text();
			})
			.then(function(msg) {
				alert(msg);
				window.location.reload();
			})
			.catch(function(error) {
				alert("Error saving structure: " + error.message);
			});
	}
}
	function bindApsCodeIPCTable() {
	var tbody = document.getElementById("ApsCodeIPCBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='171659' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Almighty Financial Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='194573' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='197846' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Jayalakshmi Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='183737' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Kavin Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='175552' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Yuvasree Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207040' ></td>";
	rows += "<td><input type='text' class='pct-input' value='A S Enterprises' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207946' ></td>";
	rows += "<td><input type='text' class='pct-input' value='R D Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='208632' ></td>";
	rows += "<td><input type='text' class='pct-input' value='D S Credit' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='212170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Marketing Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='341170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='V S ENTERPRISES' ></td>";
	rows += "</tr>";
	

	tbody.innerHTML = rows;
}

function bindBrokerIPCTable() {
var tbody = document.getElementById("BrokerIPCBody");
var rows = "";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='266338' ></td>";
rows += "<td><input type='text' class='pct-input' value='Gaadi Web Private Limited / GIRNAR SOFTWARE PVT LTD' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td rowspan='3'><input type='text' class='pct-input' value='0.20% from first case' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='266450' ></td>";
rows += "<td><input type='text' class='pct-input' value='GWPL Online' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='269302' ></td>";
rows += "<td><input type='text' class='pct-input' value='Valuedrive Technologies Pvt Ltd' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='275078' ></td>";
rows += "<td><input type='text' class='pct-input' value='Kuwy Technology Service Pvt Ltd' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='274328' ></td>";
rows += "<td><input type='text' class='pct-input' value='Cars24 Financial Services Pvt Ltd' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td><input type='text' class='pct-input' value='0.10% from first case' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='286067' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "<td rowspan='2'><input type='text' class='pct-input' value='0.20% from first case' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='316343' ></td>";
rows += "<td><input type='text' class='pct-input' value='ACKO TECHNOLOGY & SERVICES PVT LTD' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
tbody.innerHTML = rows;
}

function bindSourcingIPCTable() {
	var tbody = document.getElementById("SourcingIPCBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='3'>Used Cars</td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td><input type='text' class='pct-input' value='3' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td><input type='text' class='pct-input' value='' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Dedicated Used Cars Executive,' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='4' ></td>";
	rows += "<td><input type='text' class='pct-input' value='10' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0.10%' ></td>";
	rows += "<td><input type='text' class='pct-input' value='On net loan amount' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Any Designations' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td><input type='text' class='pct-input' value='99999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0.15%' ></td>";
	rows += "<td><input type='text' class='pct-input' value='On net loan amount' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Not applicable for INBT / Topup ' ></td>";
	rows += "</tr>";

	tbody.innerHTML = rows;
}






/* commom structre for manipal */



function bindApsCodeTable() {
	var tbody = document.getElementById("ApsCodeBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='171659' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Almighty Financial Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='194573' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='197846' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Jayalakshmi Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='183737' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Kavin Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='175552' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Yuvasree Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207040' ></td>";
	rows += "<td><input type='text' class='pct-input' value='A S Enterprises' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207946' ></td>";
	rows += "<td><input type='text' class='pct-input' value='R D Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='208632' ></td>";
	rows += "<td><input type='text' class='pct-input' value='D S Credit' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='212170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Marketing Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='341170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='V S ENTERPRISES' ></td>";
	rows += "</tr>";
	

	tbody.innerHTML = rows;
}

function bindBrokerTable() {
var tbody = document.getElementById("BrokerBody");
var rows = "";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='266338' ></td>";
rows += "<td><input type='text' class='pct-input' value='Gaadi Web Private Limited / GIRNAR SOFTWARE PVT LTD' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td rowspan='3'><input type='text' class='pct-input' value='0.20% from first case' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='266450' ></td>";
rows += "<td><input type='text' class='pct-input' value='GWPL Online' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='269302' ></td>";
rows += "<td><input type='text' class='pct-input' value='Valuedrive Technologies Pvt Ltd' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='275078' ></td>";
rows += "<td><input type='text' class='pct-input' value='Kuwy Technology Service Pvt Ltd' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='274328' ></td>";
rows += "<td><input type='text' class='pct-input' value='Cars24 Financial Services Pvt Ltd' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "<td><input type='text' class='pct-input' value='0.10% from first case' ></td>";
rows += "<td><input type='text' class='pct-input' value='NA' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='286067' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "<td rowspan='2'><input type='text' class='pct-input' value='0.20% from first case' ></td>";
rows += "<td><input type='text' class='pct-input' value='' ></td>";
rows += "</tr>";
 
rows += "<tr>";
rows += "<td><input type='text' class='pct-input' value='316343' ></td>";
rows += "<td><input type='text' class='pct-input' value='ACKO TECHNOLOGY & SERVICES PVT LTD' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "<td><input type='text' class='pct-input' value='As per open market' ></td>";
rows += "</tr>";
 
tbody.innerHTML = rows;
}

function bindSourcingTable() {
	var tbody = document.getElementById("SourcingBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td class='row-label' rowspan='3'>Used Cars</td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td><input type='text' class='pct-input' value='3' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0' ></td>";
	rows += "<td><input type='text' class='pct-input' value='' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Dedicated Used Cars Executive,' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='4' ></td>";
	rows += "<td><input type='text' class='pct-input' value='10' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0.10%' ></td>";
	rows += "<td><input type='text' class='pct-input' value='On net loan amount' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Any Designations' ></td>";
	rows += "</tr>";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='11' ></td>";
	rows += "<td><input type='text' class='pct-input' value='99999' ></td>";
	rows += "<td><input type='text' class='pct-input' value='0.15%' ></td>";
	rows += "<td><input type='text' class='pct-input' value='On net loan amount' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Not applicable for INBT / Topup ' ></td>";
	rows += "</tr>";

	tbody.innerHTML = rows;
}



function collectApsRows() {
	var tbody = document.getElementById("ApsCodeBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			apsCode: inputs[0].value,
			channelName: inputs[1].value
		});
	}
	return rowsArr;
}

function collectBrokerRows() {
	var tbody = document.getElementById("BrokerBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			brokerId: inputs[0].value,
			brokerName: inputs[1].value,
			newCar: inputs[2].value,
			usedCar: inputs[3] ? inputs[3].value : "",
			attachmentIncentive: inputs[4] ? inputs[4].value : ""
		});
	}
	return rowsArr;
}


function collectSourcingRows() {
	var tbody = document.getElementById("SourcingBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");

		var pctRaw = inputs[2].value;
		var pctNum = parseFloat(pctRaw);
		if (isNaN(pctNum)) { pctNum = 0; }

		var row = {
			category: "USED_CARS",
			slabFrom: inputs[0].value,
			slabTo: inputs[1].value,
			percentage: pctNum,
			remarks: inputs[3] ? inputs[3].value : ""
		};

		// designation input only present on rowspan rows (first row of each group)
		if (inputs.length > 4) {
			row.designation = inputs[4].value;
		}

		rowsArr.push(row);
	}
	return rowsArr;
}

function bindApsMPKPUTable() {
	var tbody = document.getElementById("ApsCodeMPPNBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='230552' ></td>";
	rows += "<td><input type='text' class='pct-input' value='FINSURE FINANCIAL SERVICES LLP' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='283900' ></td>";
	rows += "<td><input type='text' class='pct-input' value='RULOANS DISTRIBUTION SER P LTD' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='206136' ></td>";
	rows += "<td><input type='text' class='pct-input' value='	SHRI SAI FINANCIAL SERVICES' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='224548' ></td>";
	rows += "<td><input type='text' class='pct-input' value='NORTHWEST CAPITAL' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='230551' ></td>";
	rows += "<td><input type='text' class='pct-input' value='ASCENT AUTOFIN CONSULTANCY LLP' ></td>";
	rows += "</tr>";
	
	
	

	tbody.innerHTML = rows;
}
function bindBrokerMPKPUTable() {
	var tbody = document.getElementById("ApsCodeMPKOBody");
	var rows = "";

	rows += "<tr>";
	rows += "<td><input type='text' class='pct-input' value='171659' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Almighty Financial Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='194573' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='197846' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Jayalakshmi Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='183737' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Kavin Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='175552' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Yuvasree Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207040' ></td>";
	rows += "<td><input type='text' class='pct-input' value='A S Enterprises' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='207946' ></td>";
	rows += "<td><input type='text' class='pct-input' value='R D Associates' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='208632' ></td>";
	rows += "<td><input type='text' class='pct-input' value='D S Credit' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='212170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='Srishti Marketing Services' ></td>";
	rows += "</tr>";
	rows += "<td><input type='text' class='pct-input' value='341170' ></td>";
	rows += "<td><input type='text' class='pct-input' value='V S ENTERPRISES' ></td>";
	rows += "</tr>";
	

	tbody.innerHTML = rows;
}

function collectApsPNMPCRows() {
	var tbody = document.getElementById("ApsCodeMPPNBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			apsCode: inputs[0].value,
			channelName: inputs[1].value
		});
	}
	return rowsArr;
}
	function collectApsKLMPCRows() {
	var tbody = document.getElementById("ApsCodeMPKOBody");
	var trList = tbody.getElementsByTagName("tr");
	var rowsArr = [];

	for (var i = 0; i < trList.length; i++) {
		var inputs = trList[i].querySelectorAll("input.pct-input");
		rowsArr.push({
			apsCode: inputs[0].value,
			channelName: inputs[1].value
		});
	}
	return rowsArr;
}

function createDynamicTables(config) {
    const container = document.getElementById(config.containerId);
    if (!container) return;
 
    const minLength = 1;
    const maxLength = 10;
 
    container.innerHTML = "";
 
    // 1. Title Rendering
    if (config.title) {
        const titleEl = document.createElement("h2");
        titleEl.textContent = config.title;
        titleEl.className = "fw-bold text-dark mb-4 text-center fs-4";
        container.appendChild(titleEl);
    }
 
    // 2. Table Wrapper & Card Container
    const tableWrapper = document.createElement("div");
    tableWrapper.className = "table-responsive shadow-sm rounded-3 border bg-white mb-3";
 
    const table = document.createElement("table");
    table.className = "table table-bordered align-middle text-center w-100 m-0";
    table.style.tableLayout = "fixed"; // Prevents manual resizing issues
 
    // 3. Header Construction
    const thead = document.createElement("thead");
    thead.style.background = "linear-gradient(180deg, #991b1b 0%, #7f1d1d 100%)";
    thead.className = "text-white";
 
    // Row 1: Top Group Headers (No explicit width styling here—let colSpan handle it)
    const tr1 = document.createElement("tr");
 
    const thSlab = document.createElement("th");
    thSlab.textContent = "Slab";
    thSlab.rowSpan = 2;
    thSlab.className = "align-middle fw-bold border-end border-white-50";
 
    const thNewCar = document.createElement("th");
    thNewCar.textContent = "New car and Used car Inbound / Outbound";
    thNewCar.colSpan = 2;
    thNewCar.className = "py-2 px-1 border-bottom border-end border-white-50 fw-semibold fs-7 lh-sm";
    thNewCar.style.whiteSpace = "normal"; // Allow wrapping
 
    const thUsedCar = document.createElement("th");
    thUsedCar.textContent = "Used Car ( All used car cases )";
    thUsedCar.colSpan = 2;
    thUsedCar.className = "py-2 px-1 border-bottom border-end border-white-50 fw-semibold fs-7 lh-sm";
    thUsedCar.style.whiteSpace = "normal"; // Allow wrapping
 
    const thStructure = document.createElement("th");
    thStructure.textContent = "Structure";
    thStructure.colSpan = 4;
    thStructure.className = "py-2 px-1 border-bottom border-white-50 fw-semibold fs-7";
 
    tr1.appendChild(thSlab);
    tr1.appendChild(thNewCar);
    tr1.appendChild(thUsedCar);
    tr1.appendChild(thStructure);
 
    // Row 2: Sub-headers (Widths defined ONLY here for strict layout)
    const tr2 = document.createElement("tr");
    
    const subHeaders = [
        { text: "Cases From", width: "8%" },
        { text: "Cases To", width: "8%" },
        { text: "Cases From", width: "8%" },
        { text: "Cases To", width: "8%" },
        { text: "Fixed Salary %", width: "10%" },
        { text: "Incentive %", width: "10%" },
        { text: "Per Case Amt", width: "13%" },
        { text: "Remark", width: "30%" } // Expanded for longer text
    ];
 
    // Set width on Slab from Row 1 sub-header alignment
    thSlab.style.width = "5%";
 
    subHeaders.forEach(col => {
        const th = document.createElement("th");
        th.textContent = col.text;
        th.style.width = col.width;
        th.className = "py-2 px-1 border-end border-white-50 fw-bold text-uppercase";
        th.style.fontSize = "0.72rem";
        th.style.letterSpacing = "0.5px";
        th.style.whiteSpace = "normal";
        tr2.appendChild(th);
    });
 
    thead.appendChild(tr1);
    thead.appendChild(tr2);
    table.appendChild(thead);
 
    // 4. Body Construction
    const tbody = document.createElement("tbody");
 
    const columnsList = [
        { field: "slab", disabled: true },
        { field: "newCarCasesFrom" },
        { field: "newCarCasesTo" },
        { field: "usedCarCasesFrom" },
        { field: "usedCarCasesTo" },
        { field: "Current fixed Salary %" },
        { field: "Current Incentive %" },
        { field: "Per case Amount" },
        { field: "remark" }
    ];
 
    config.data.forEach((rowData, idx) => {
        const row = document.createElement("tr");
        row.className = idx % 2 === 0 ? "bg-white" : "bg-light bg-opacity-50";
 
        columnsList.forEach(col => {
            const td = document.createElement("td");
            td.className = "p-1 border-end";
 
            const input = document.createElement("input");
            input.type = "text";
            
            if (col.field === "remark") {
                input.className = "form-control form-control-sm text-start px-2 py-1 border-secondary-subtle shadow-none";
                input.style.fontSize = "0.82rem";
            } else {
                input.className = "form-control form-control-sm text-center px-1 py-1 border-secondary-subtle shadow-none";
                input.style.fontSize = "0.85rem";
            }
 
            input.name = col.field;
            const val = rowData[col.field] || "";
            input.value = val;
            input.title = val; // Tooltip hover for truncated text
 
            if (col.field.toLowerCase() === "slab" || col.disabled) {
                input.disabled = true;
                input.className += " bg-secondary-subtle text-dark fw-bold border-0";
            }
 
            if (col.field !== "slab" && col.field !== "remark") {
                input.setAttribute("maxlength", maxLength);
 
                input.addEventListener("input", function() {
                    this.value = this.value.replace(/[^0-9]/g, "");
                });
 
                input.addEventListener("blur", function() {
                    const cleanVal = this.value.trim();
                    if (cleanVal.length > 0 && cleanVal.length < minLength) {
                        alert(`Value must be at least ${minLength} digit(s) long.`);
                        this.classList.add("is-invalid");
                    } else {
                        this.classList.remove("is-invalid");
                    }
                });
            }
 
            td.appendChild(input);
            row.appendChild(td);
        });
 
        tbody.appendChild(row);
    });
 
    table.appendChild(tbody);
    tableWrapper.appendChild(table);
    container.appendChild(tableWrapper);
}



async function saveData(state,subProduct,fromDate,toDate) {
// 1. Get container and rows from the dynamic table
const tableContainer = document.getElementById("table-container");
const rows = tableContainer ? tableContainer.querySelectorAll("tbody tr") : [];
 
if (rows.length === 0) {
    alert("No data available to save.");
    return;
}
 

 
const tableData = [];
 
// 2. Loop through table rows and extract input values
rows.forEach(row => {
    // Helper function to extract input values by name attribute
    const getVal = (name) => {
        const input = row.querySelector(`input[name="${name}"]`);
        return input ? input.value.trim() : "";
    };
 
    const slabVal = getVal("slab");
 
    // Skip row if slab is missing
    if (!slabVal) return;
 
    // Construct object matching @JsonProperty annotations in SlabRowDto
    const rowDto = {
        "slab": slabVal,
        "newCarCasesFrom": parseInt(getVal("newCarCasesFrom"), 10) || 0,
        "newCarCasesTo": parseInt(getVal("newCarCasesTo"), 10) || 0,
        "usedCarCasesFrom": parseInt(getVal("usedCarCasesFrom"), 10) || 0,
        "usedCarCasesTo": parseInt(getVal("usedCarCasesTo"), 10) || 0,
        "Current fixed Salary %": parseFloat(getVal("Current fixed Salary %")) || 0,
        "Current Incentive %": parseFloat(getVal("Current Incentive %")) || 0,
        "Per case Amount": parseFloat(getVal("Per case Amount")) || 0,
        "remark": getVal("remark"),
        "status": "N"
    };
 
    tableData.push(rowDto);
});
 
if (tableData.length === 0) {
    alert("Please enter valid row data before submitting.");
    return;
}
 
// Wrap payload matching RetainerPayloadDto
   const payload = {
        user: "SYSTEM",
        cycleFromDate: fromDate, // <--- ADD THIS
        cycleToDate: toDate,     // <--- ADD THIS
        tableData: tableData
    };
 

 
try {
  
 
    // 4. Send POST Request
    const response = await fetch("/counsellor/retainer/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(payload)
    });
 
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP Error status: ${response.status}`);
    }
 
    const result = await response.json();
    console.log("Server Response:", result);
 
    alert("Data saved successfully!");
 
} catch (error) {
/*    console.error("Error submitting form:", );*/
    alert(`Failed to save: Data Save`);
} finally {
    
}
 
 
}
 
 