/*valuation.js*/

let valuationRows = [];

let valuationLoadedData = [];

function initValuationScreen() {

    console.log("Initializing Valuation Screen");

    document.getElementById("valuationBody").innerHTML = "";
    document.getElementById("valuationFromDate").value = "";
    document.getElementById("valuationToDate").value = "";

    valuationRows = [];
    valuationLoadedData = [];

    loadValuationStructure();
}

//get Valuations
function loadValuationStructure() {

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/valuations"
    )
    .then(function(response) {

        if (!response.ok) {
            throw new Error("Unable to load valuation structure");
        }
        return response.json();
    })
    .then(function(data) {

        valuationRows = data;
        renderValuationTable();

    })
    .catch(function(error) {

        console.error("Valuation structure error:", error);
        alert("Unable to load Valuation structure");
    });
}

function renderValuationTable() {

    let body =document.getElementById("valuationBody");

    body.innerHTML = "";

    valuationRows.forEach(function(row) {

        let tr =document.createElement("tr");

		tr.innerHTML =
		    "<td>" + row.iboxId + "</td>" +
		    "<td>" + row.valuerName + "</td>" +
		    "<td>" + row.productSubType + "</td>" +
		    "<td>" +
		        "<input " +
		            "type='number' " +
		            "class='form-control valuation-rate' " +
		            "data-master-id='" + row.id + "' " +
		            "step='0.01' " +
		            "min='0'>" +
		    "</td>";
        body.appendChild(tr);
    });
}

function fetchValuationSlab() {

    let fromDate = document.getElementById("valuationFromDate").value;
    let toDate = document.getElementById("valuationToDate").value;

    if (!fromDate || !toDate) {
        alert("Please select From Date and To Date");
        return;
    }

    if (fromDate > toDate) {
        alert("From Date cannot be greater than To Date");
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/fetch" +
        "?product=" +
        encodeURIComponent("Collection - Agency") +
        "&subProduct=" +
        encodeURIComponent("Valuation") +
        "&fromDate=" +
        encodeURIComponent(fromDate) +
        "&toDate=" +
        encodeURIComponent(toDate)
    )
    .then(function(response) {
        if (!response.ok) {
            throw new Error("Unable to fetch slab");
        }
        return response.text();
    })
    .then(function(text) {
        // No approved slab exists
        if (!text || text.trim() === "") {

            alert("No Approved Record found");
            clearValuationRates();
            return;
        }

        let data;

        try {
            data = JSON.parse(text);
        } catch (error) {
            console.error("Invalid JSON response:", text);
            throw new Error("Invalid response received from server");
        }

        if (!data) {
            alert("No approved Valuation slab found");
            clearValuationRates();
            return;
        }

        populateValuationRates(data);
    })
    .catch(function(error) {
        console.error("Valuation fetch error:", error);
        alert(error.message);
    });
}

function populateValuationRates(data) {

    clearValuationRates();

    if (!data.details) {
        return;
    }

    data.details.forEach(function(detail) {

        let input =
            document.querySelector(
                ".valuation-rate" +
                "[data-master-id='" +
                detail.valuationMasterId +
                "']"
            );

        if (input) {
            input.value =
                detail.rateUnderGst != null
                    ? detail.rateUnderGst
                    : "";
        }
    });
}

function clearValuationRates() {

    document
        .querySelectorAll(
            ".valuation-rate"
        )
        .forEach(function(input) {
            input.value = "";
        });
}

function saveValuationSlab() {

    let fromDate = document.getElementById("valuationFromDate").value;
    let toDate = document.getElementById("valuationToDate").value;

    // Validate dates
    if (!fromDate || !toDate) {
        alert("Please select From Date and To Date");
        return;
    }

    if (fromDate > toDate) {
        alert("From Date cannot be greater than To Date");
        return;
    }

    let details = [];
    let invalid = false;

    // Collect valuation rate details
    document.querySelectorAll(".valuation-rate").forEach(function (input) {

        let value = input.value.trim();

        if (value === "") {
            invalid = true;
            return;
        }

        details.push({
            valuationMasterId: Number(input.dataset.masterId),
            rateUnderGst: Number(value)
        });
    });

    // Validate rate inputs
    if (invalid) {
        alert("Please enter rate under GST for all rows");
        return;
    }

    // Prepare request payload
    let request = {
        product: "Collection - Agency",
        subProduct: "Valuation",
        fromDate: fromDate,
        toDate: toDate,
        remarks: "",
        details: details
    };

    // Save valuation slab
    fetch(CONTEXT_PATH + "/valuationPayout/save", {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(request)
    })
        .then(function (response) {

            return response.text().then(function (message) {

                if (!response.ok) {
                    throw new Error(message);
                }

                return message;
            });
        })
        .then(function (message) {

            alert(message);
        })
        .catch(function (error) {

            console.error("Valuation save error:", error);

            alert(error.message);
        });
}

function viewValuationCompare() {

    let fromDate = document.getElementById("valuationFromDate").value;
    let toDate = document.getElementById("valuationToDate").value;

    let product = "Collection - Agency";
    let subProduct = "Valuation";

    if (!fromDate || !toDate) {
        alert("Please select From Date and To Date");
        return;
    }

    if (fromDate > toDate) {
        alert("From Date cannot be greater than To Date");
        return;
    }

    let params = new URLSearchParams();

    params.append("product", product);
    params.append("subProduct", subProduct);
    params.append("fromDate", fromDate);
    params.append("toDate", toDate);

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/latestApprovedId?" +
        params.toString()
    )
        .then(function (response) {

            if (!response.ok) {
                throw new Error(
                    "Unable to find current approved record"
                );
            }

            return response.text();
        })
        .then(function (id) {

            if (
                !id ||
                id.trim() === "" ||
                id.trim() === "null"
            ) {
                alert("No Current Approved Record Found");
                return;
            }

            window.location.href =
                CONTEXT_PATH +
                "/mainPage/load" +
                "?master=VALUATIONPAYOUTCOMPAREMAKER" +
                "&id=" +
                encodeURIComponent(id.trim()) +
                "&mode=maker";
        })
        .catch(function (error) {

            console.error("Valuation Maker Compare Error =", error);

            alert(error.message);
        });
}