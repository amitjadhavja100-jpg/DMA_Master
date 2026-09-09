/* flowsPayoutChecker.js */

function loadFlowsPayoutChecker() {

    var product =document.getElementById("product").value.trim();

    var subProduct =document.getElementById("subProduct").value.trim();

    var userId = localStorage.getItem("user_id");

    console.log("Flows Product =", product);
    console.log("Flows Sub Product =", subProduct);
    console.log("Flows Checker User =", userId);

    if (!userId) {
        alert("User ID not found");
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/flowsPayout/checker?user=" +
        encodeURIComponent(userId)
    )
    .then(function(response) {

        if (!response.ok) {
            throw new Error("Unable to load Flows Payout Checker");
        }
        return response.json();
    })
    .then(function(data) {
        console.log("Flows Checker Data =", data);

        var filteredData = data.filter(function(record) {
            return String(record.product).trim() === product
                &&
                String(record.subProduct).trim() === subProduct;

        });

        console.log( "Flows Filtered Data =",filteredData);

        renderFlowsPayoutChecker(filteredData);

    })
    .catch(function(error) {

        console.error("Flows Payout Checker Error =",error);

        var tbody =document.getElementById("flowsPayoutCheckerBody");

        if (tbody) {

            tbody.innerHTML =
                "<tr>" +
                    "<td colspan='11' " +
                    "style='text-align:center;" +
                    "font-weight:bold;" +
                    "padding:25px;'>" +
                    "Unable to load Flows Payout Records" +
                    "</td>" +
                "</tr>";
        }
    });
}

function showFlowsPayoutChecker() {

    var section = document.getElementById("flowsPayoutChecker");

    if (section) {
        section.style.display ="block";
    }
}


function hideFlowsPayoutChecker() {

    var section =document.getElementById("flowsPayoutChecker");

    if (section) {
        section.style.display ="none";
    }

    var tbody = document.getElementById("flowsPayoutCheckerBody");

    if (tbody) {
        tbody.innerHTML = "";
    }
}


function renderFlowsPayoutChecker(data) {

    var tbody = document.getElementById("flowsPayoutCheckerBody");

    if (!tbody) {
        return;
    }

    tbody.innerHTML = "";
	
    if (!data || data.length === 0) {
        tbody.innerHTML =
            "<tr>" +
            "<td colspan='12' " +
            "style='text-align:center;" +
            "font-weight:bold;" +
            "padding:25px;color:#198754'>" +

            "No Payout Records Pending For Approval" +
            "</td>" +
            "</tr>";
        return;
    }
    data.forEach(function(d) {

        var row = "";

        row += "<tr>";
        row += "<td>" + safeValue(d.id) + "</td>";
		row += "<td>" + safeValue(d.categoryName) + "</td>";
		row += "<td>" + safeValue(d.cityName) + "</td>";
		row += "<td>" + safeValue(d.bucketName) + "</td>";
        row += "<td>" + formatFlowsDate(d.fromDate) + "</td>";
        row += "<td>" + formatFlowsDate(d.toDate) + "</td>";
        row += "<td>" + safeValue(d.status) + "</td>";
        row += "<td>" + safeValue(d.createdBy) + "</td>";
        row += "<td>";
        row +=
            "<button " +
            "type='button' " +
            "class='btn btn-success btn-sm me-1' " +
            "onclick='approveFlowsPayout(" +
            d.id +
            ")'>" +
            "Approve" +
            "</button>";

        row +=
            "<button " +
            "type='button' " +
            "class='btn btn-danger btn-sm' " +
            "onclick='rejectFlowsPayout(" +
            d.id +
            ")'>" +
            "Reject" +
            "</button>";

        row +=
            "<button " +
            "type='button' " +
            "class='btn btn-info btn-sm me-1' " +
            "onclick='viewFlowsPayout(" +
            d.id +
            ")'>" +
            "View" +
            "</button>";

        row += "</td>";
        row += "</tr>";

        tbody.innerHTML += row;
    }
    );
}

function approveFlowsPayout(id) {
	
    let userId =localStorage.getItem("user_id");

    if (!userId) {
        alert("User ID not found");
        return;
    }

    console.log("Flows Approve ID =", id);

/*    if (!confirm( "Are you sure you want to approve this record?")
    ) {
        return;
    }*/

	fetch(
	    CONTEXT_PATH +
	    "/flowsPayout/approve/" +
	    id +
	    "?user=" +
	    encodeURIComponent(userId),
	    {
	        method: "POST"
	    }
	)

    .then(function (response) {

        console.log("Approve HTTP Status =",response.status);

        return response.text()
            .then(function (message) {

                if (!response.ok) {
                    throw new Error(message);
                }
                return message;
            });
    })

    .then(function (message) {
        console.log("Approve Response =",message);
        alert(message);

        loadFlowsPayoutChecker();
    })

    .catch(function (error) {
        console.error( "Flows Approve Error =", error);
        alert("ERROR : " + error.message);
    });

}

function rejectFlowsPayout(id) {

    var remarks =prompt("Enter remarks");
	
	let userId = localStorage.getItem("user_id");

	   if(!userId){
	       alert("User ID not found");
	       return;
	   }

    if (remarks === null) {
        return;
    }

    if (remarks.trim() === "") {
        alert("Remarks are required");
        return;
    }
    console.log("Flows Reject ID =", id);
    console.log("Flows Reject Remarks =",remarks);

    fetch(
        CONTEXT_PATH +
        "/flowsPayout/reject/" +
        id +
        "?user=" +
        encodeURIComponent(userId) +
        "&remarks=" +
        encodeURIComponent(remarks),
        {
            method: "POST"
        }
    )

    .then(function (response) {

        return response.text()
            .then(function (message) {

                if (!response.ok) {
                    throw new Error(message);
                }
                return message;
            });
    })

    .then(function (message) {
        alert(message);
        loadFlowsPayoutChecker();
    })

    .catch(function (error) {
        console.error("Flows Reject Error =",error);
        alert("ERROR : " +error.message);
    });
}

function viewFlowsPayout(id) {

    console.log("Flows Compare ID =", id);

    window.location.href =
        CONTEXT_PATH +
        "/mainPage/load" +
        "?master=FLOWSPAYOUTCOMPARECHECKER" +
        "&id=" +
        id +
        "&mode=checker";
}

function formatFlowsDate(value) {

    if (value === null || value === undefined || value === "") {
        return "";
    }

    try {
        return new Date(value).toLocaleDateString("en-GB");
    }
    catch (e) {
        return value;
    }
}

function safeValue(value) {
    if (value === null || value === undefined) {
        return "";
    }
    return value;
}