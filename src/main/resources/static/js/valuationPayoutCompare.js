/*valuationPayoutCompare.js*/
let valuationCompareData = null;
let valuationCompareMode = null;
let valuationCompareId = null;

document.addEventListener("DOMContentLoaded", function () {

    loadValuationCompare();
});

function loadValuationCompare() {

    let params = new URLSearchParams(
        window.location.search
    );

    valuationCompareId = params.get("id");
    valuationCompareMode = params.get("mode");

    if (!valuationCompareId) {
        alert("Compare ID not found");
        return;
    }

    if (!valuationCompareMode) {
        alert("Compare mode not found");
        return;
    }

    fetch(
        CONTEXT_PATH +
        "/valuationPayout/compare/" +
        encodeURIComponent(valuationCompareId) +
        "?mode=" +
        encodeURIComponent(valuationCompareMode)
    )
        .then(function (response) {

            if (!response.ok) {

                return response.text().then(function (message) {

                    throw new Error(
                        message ||
                        "Unable to load Valuation compare data"
                    );
                });
            }

            return response.json();
        })
        .then(function (data) {

            valuationCompareData = data;
            renderValuationCompare(data);
        })
        .catch(function (error) {

            console.error("Valuation Compare Error =",error);
            alert(error.message);
        });
}

function renderValuationCompare(data) {

    let top = data.top;
    let bottom = data.bottom;

    if (!top) {
        alert("Compare record not found");
        return;
    }

    let topTitle = document.getElementById("topTitle");
    let bottomTitle = document.getElementById("bottomTitle");

    if (valuationCompareMode === "maker") {

        topTitle.innerText =
            "Current Approved Record";

        bottomTitle.innerText =
            "Previous Approved Record";
    }

    else if (valuationCompareMode === "checker") {

        topTitle.innerText =
            "Pending / New Record";

        bottomTitle.innerText =
            "Current Approved Record";
    }

    renderValuationCompareTable(
        top,
        "pendingBody",
        bottom
    );

    if (bottom) {

        renderValuationCompareTable(
            bottom,
            "approvedBody",
            top
        );

    } else {

        document
            .getElementById("approvedCompareTable")
            .querySelector("thead")
            .style.display = "none";

        let message =
            valuationCompareMode === "maker"
                ? "No Previous Approved Record Found"
                : "No Current Approved Record Found";

        document.getElementById("approvedBody").innerHTML =
            "<tr>" +
                "<td colspan='4' " +
                    "style='text-align:center;" +
                    "font-weight:bold;" +
                    "padding:20px;'>" +
                    message +
                "</td>" +
            "</tr>";
    }
}

function renderValuationCompareTable(
    master,
    bodyId,
    compareMaster
) {

    let body = document.getElementById(bodyId);
    body.innerHTML = "";

    let details = master.details || [];

    details.forEach(function (detail) {

        let tr = document.createElement("tr");

        let iboxTd = document.createElement("td");
        iboxTd.innerText = detail.iboxId || "";

        let valuerTd = document.createElement("td");
        valuerTd.innerText = detail.valuerName || "";

        let subtypeTd = document.createElement("td");
        subtypeTd.innerText = detail.productSubType || "";

        let rateTd = document.createElement("td");
        rateTd.innerText =
            detail.rateUnderGst == null
                ? ""
                : detail.rateUnderGst;

        /*
         * Highlight rate changes
         */
        if (compareMaster) {

            let oldDetail = findValuationDetail(
                compareMaster.details || [],
                detail
            );

            if (!oldDetail) {

                rateTd.classList.add("changed");

            } else if (
                String(detail.rateUnderGst ?? "") !==
                String(oldDetail.rateUnderGst ?? "")
            ) {

                rateTd.classList.add("changed");
            }
        }

        tr.appendChild(iboxTd);
        tr.appendChild(valuerTd);
        tr.appendChild(subtypeTd);
        tr.appendChild(rateTd);

        body.appendChild(tr);
    });
}

function findValuationDetail(details, current) {

    return details.find(function (detail) {

        return Number(detail.valuationMasterId) === Number(current.valuationMasterId);
    });
}

function goBack() {
    window.history.back();
}