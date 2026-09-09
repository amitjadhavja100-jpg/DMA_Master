/*valuationPayoutChecker.js*/

function loadValuationPayoutChecker() {

    let tbody = document.getElementById("valuationPayoutCheckerBody");

    if (!tbody) {
        console.error("Valuation checker tbody not found");
        return;
    }

    tbody.innerHTML = "";

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/pending?user=" +
        encodeURIComponent(userId)
    )
        .then(function (response) {

            if (!response.ok) {
                return response.text().then(function (message) {

                    throw new Error(
                        message ||
                        "Unable to load Valuation pending records"
                    );
                });
            }

            return response.json();
        })
        .then(function (data) {

            console.log("VALUATION CHECKER DATA =", data);

            if (!Array.isArray(data) || data.length === 0) {

                tbody.innerHTML =
                    "<tr>" +
                        "<td colspan='9' " +
                        "style='text-align:center;" +
                        "font-weight:bold;padding:20px;'>" +
                        "No pending Valuation records found" +
                        "</td>" +
                    "</tr>";

                return;
            }

            data.forEach(function (record) {

                let row =
                    "<tr>" +

                        "<td>" + (record.id || "") +"</td>" +
                        "<td>" + (record.product || "") +"</td>" +
                        "<td>" + (record.subProduct || "") +"</td>" +
                        "<td>" + formatDateOnly(record.fromDate) + "</td>" +
                        "<td>" + formatDateOnly(record.toDate) + "</td>" +
                        "<td>" + (record.versionNo || "") + "</td>" +
                        "<td>" + (record.status || "") + "</td>" +
                        "<td>" + (record.createdBy || "") + "</td>" +

                        "<td>" +

                            "<button " +
                                "type='button' " +
                                "class='btn btn-success btn-sm me-1' " +
                                "onclick='approveValuation(" +
                                    record.id +
                                ")'>" +
                                "Approve" +
                            "</button>" +

                            "<button " +
                                "type='button' " +
                                "class='btn btn-danger btn-sm' " +
                                "onclick='rejectValuation(" +
                                    record.id +
                                ")'>" +
                                "Reject" +
                            "</button>" +
							
							"<button " +
			                    "type='button' " +
			                    "class='btn btn-info btn-sm me-1' " +
			                    "onclick='viewValuation(" +
			                    record.id +
			                    ")'>" +
			                    "View" +
			                 "</button>" +

                        "</td>" +
                    "</tr>";
                tbody.innerHTML += row;
            });
        })
        .catch(function (error) {
            console.error("Valuation checker error:", error);
            alert(error.message);
        });
}

function viewValuation(id) {

    if (!id) {
        alert("Valuation record ID is missing");
        return;
    }

    console.log("Opening Valuation Compare ID =", id);

    window.location.href =
        CONTEXT_PATH +
        "/mainPage/load?master=VALUATIONPAYOUTCOMPARECHECKER" +
        "&id=" +
        encodeURIComponent(id) +
        "&mode=checker";
}

function approveValuation(id) {

    if (!id) {
        alert("Valuation record ID is missing");
        return;
    }

    if (!confirm("Are you sure you want to approve this Valuation record?")) {
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/approve/" +
        encodeURIComponent(id) +
        "?user=" +
        encodeURIComponent(userId),
        {
            method: "POST"
        }
    )
        .then(function (response) {

            return response.text().then(function (message) {

                if (!response.ok) {
                    throw new Error(
                        message ||
                        "Unable to approve Valuation record"
                    );
                }

                return message;
            });
        })
        .then(function (message) {

            alert(message);

            loadValuationPayoutChecker();
        })
        .catch(function (error) {

            console.error("Valuation approve error:", error);

            alert(error.message);
        });
}


function rejectValuation(id) {

    if (!id) {
        alert("Valuation record ID is missing");
        return;
    }

    let remarks = prompt("Please enter rejection remarks:");

    if (remarks === null) {
        return;
    }

    remarks = remarks.trim();

    if (remarks === "") {
        alert("Rejection remarks are required");
        return;
    }

    if (!confirm("Are you sure you want to reject this Valuation record?")) {
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/reject/" +
        encodeURIComponent(id) +
        "?user=" +
        encodeURIComponent(userId) +
        "&remarks=" +
        encodeURIComponent(remarks),
        {
            method: "POST"
        }
    )
        .then(function (response) {

            return response.text().then(function (message) {

                if (!response.ok) {
                    throw new Error(
                        message ||
                        "Unable to reject Valuation record"
                    );
                }

                return message;
            });
        })
        .then(function (message) {

            alert(message);

            loadValuationPayoutChecker();
        })
        .catch(function (error) {

            console.error("Valuation reject error:", error);

            alert(error.message);
        });
}
