/* flowsPayoutCompare.js */

let flowsCompareData = null;
let compareMode = null;

document.addEventListener("DOMContentLoaded", function() {
    loadFlowsCompare();
});

function loadFlowsCompare() {

    let params = new URLSearchParams(window.location.search);
    let id = params.get("id");
    compareMode = params.get("mode");

    if (!id) {
        alert("Compare ID not found");
        return;
    }

    if (!compareMode) {
        alert("Compare mode not found");
        return;
    }

    console.log("Compare ID =", id);
    console.log("Compare Mode =", compareMode);

    fetch(CONTEXT_PATH + 
		"/flowsPayout/compare/" + 
		encodeURIComponent(id) + 
		"?mode=" + 
		encodeURIComponent(compareMode)
	)
        .then(function(response) {

            if (!response.ok) {
                throw new Error("Unable to load compare data");
            }

            return response.json();
        })
        .then(function(data) {

            console.log("FLOWS COMPARE DATA =", data);
            console.log("TOP =", data.top);
            console.log("BOTTOM =", data.bottom);

            flowsCompareData = data;

            renderCompare(data);
        })
        .catch(function(error) {

            console.error("Flows Compare Error =", error);
            alert(error.message);
        });
}

function renderCompare(data) {

    let top = data.top;
    let bottom = data.bottom;

    if (!top) {
        alert("Compare record not found");
        return;
    }

   /* document.getElementById("productName").innerText = top.product || "";
    document.getElementById("subProductName").innerText = top.subProduct || "";
    document.getElementById("categoryName").innerText = top.categoryName || "";
	document.getElementById("cityName").innerText =top.cityName || "";
    document.getElementById("bucketName").innerText = top.bucketName || "";
    document.getElementById("fromDate").innerText = formatDateOnly(top.fromDate);
    document.getElementById("toDate").innerText = formatDateOnly(top.toDate);
    document.getElementById("tableType").innerText = top.tableType || "";
    document.getElementById("versionNo").innerText = top.versionNo || "";*/

    let topTitle = document.getElementById("topTitle");
    let bottomTitle = document.getElementById("bottomTitle");

    if (compareMode === "maker") {

        topTitle.innerText = "Current Approved Record";
        bottomTitle.innerText = "Previous Approved Record";

    } else if (compareMode === "checker") {

        topTitle.innerText = "Pending / New Record";
        bottomTitle.innerText = "Current Approved Record";
    }

	// TOP TABLE
	renderCompareTable(
	    top,
	    bottom,
	    "pendingTableType",
	    "pendingPerformanceHeader",
	    "pendingBody",
	    true
	);


	// BOTTOM TABLE
	if (bottom) {

	    renderCompareTable(
	        bottom,
	        top,
	        "approvedTableType",
	        "approvedPerformanceHeader",
	        "approvedBody",
	        true
	    );

	} else {

	    let message =
	        compareMode === "maker"
	            ? "No Previous Approved Record Found"
	            : "No Current Approved Record Found";

	    document
	        .getElementById("approvedCompareTable")
	        .querySelector("thead")
	        .style.display = "none";

	    document.getElementById("approvedBody").innerHTML =
	        "<tr>" +
	            "<td colspan='50' " +
	                "style='text-align:center; font-weight:bold; padding:20px;'>" +
	                message +
	            "</td>" +
	        "</tr>";
	}
}

function renderCompareTable(
    master,
    compareMaster,
    tableTypeId,
    performanceHeaderId,
    bodyId,
    highlightChanges
) {

    let resolutions = master.resolutions || [];
    let performances = master.performances || [];
    let payoutList = master.payoutList || [];


    let tableTypeHeader =
        document.getElementById(tableTypeId);

    tableTypeHeader.innerText =
        master.tableType || "";

    tableTypeHeader.colSpan =
        performances.length;


    let performanceHeader =
        document.getElementById(performanceHeaderId);

    performanceHeader.innerHTML = "";


    performances.forEach(function (performance) {

        performanceHeader.innerHTML +=
            "<th data-performance-id='" +
            performance.id +
            "'>" +
            performance.displayText +
            "</th>";
    });


    let body =
        document.getElementById(bodyId);

    body.innerHTML = "";


    resolutions.forEach(function (resolution) {

        let tr =
            document.createElement("tr");


        let resolutionCell =
            document.createElement("td");

        resolutionCell.innerText =
            resolution.displayText;

        tr.appendChild(resolutionCell);

        performances.forEach(function (performance) {

            let td =
                document.createElement("td");


            let newValue =
                findPayoutValue(
                    payoutList,
                    resolution.id,
                    performance.id
                );


            td.innerText = newValue;

            if (
                highlightChanges &&
                compareMaster
            ) {

                let oldValue =
                    findPayoutValue(
                        compareMaster.payoutList || [],
                        resolution.id,
                        performance.id
                    );


                let newNumber =
                    Number(newValue);

                let oldNumber =
                    Number(oldValue);


                if (newNumber !== oldNumber) {

                    td.classList.add("changed");
                }
            }


            tr.appendChild(td);
        });


        body.appendChild(tr);
    });

    renderFooterRow(
        master,
        compareMaster,
        body,
        performances,
        highlightChanges
    );
}

function renderFooterRow(
    master,
    compareMaster,
    body,
    performances,
    highlightChanges
) {

    let footers = master.footers || [];

    let compareFooters = compareMaster
        ? (compareMaster.footers || [])
        : [];


    if (footers.length === 0) {
        return;
    }

    let groupedFooters = {};


    footers.forEach(function (footer) {

        let key =
            String(footer.sourceFooterId || "") +
            "_" +
            String(footer.footerType || "") +
            "_" +
            String(footer.footerText || "");


        if (!groupedFooters[key]) {

            groupedFooters[key] = {
                footerType: footer.footerType || "",
                footerText: footer.footerText || "",
                sourceFooterId: footer.sourceFooterId,
                values: {}
            };
        }


        /*
         * Store value against performanceId
         */

        if (footer.performanceId != null) {

            groupedFooters[key].values[
                Number(footer.performanceId)
            ] = footer.footerValue;
        }
    });


    /*
     * ==================================================
     * CREATE ONE ROW PER FOOTER DEFINITION
     * ==================================================
     */

	Object.values(groupedFooters).forEach(function (group) {

	    let tr = document.createElement("tr");

	    tr.className = "flows-footer-row";

	    let footerType =
	        String(group.footerType || "")
	            .trim()
	            .toUpperCase();


	    /*
	     * ============================================
	     * INCENTIVE FOOTER
	     * ============================================
	     *
	     * One label column + performance columns
	     */
	    if (footerType === "INCENTIVE") {

	        let labelTd = document.createElement("td");

	        labelTd.className = "flows-footer-label";

	        labelTd.innerText =
	            group.footerText || "";

	        tr.appendChild(labelTd);


	        performances.forEach(function (performance) {

	            let td = document.createElement("td");

	            let performanceId =
	                Number(performance.id);

	            let value =
	                group.values[performanceId];


	            if (value != null) {

	                td.innerText = value;

	            } else {

	                td.innerText = "";

	            }


	            /*
	             * Compare old footer value
	             */
	            if (
	                highlightChanges &&
	                compareMaster
	            ) {

	                let oldFooterGroup =
	                    compareFooters.filter(function (oldFooter) {

	                        return (

	                            String(
	                                oldFooter.sourceFooterId || ""
	                            ) ===
	                            String(
	                                group.sourceFooterId || ""
	                            )

	                            &&

	                            String(
	                                oldFooter.footerType || ""
	                            ).toUpperCase() ===
	                            footerType

	                            &&

	                            String(
	                                oldFooter.footerText || ""
	                            ) ===
	                            String(
	                                group.footerText || ""
	                            )

	                        );

	                    });


	                let oldFooter =
	                    oldFooterGroup.find(function (oldFooter) {

	                        return Number(
	                            oldFooter.performanceId
	                        ) === performanceId;

	                    });


	                let oldValue =
	                    oldFooter &&
	                    oldFooter.footerValue != null
	                        ? String(oldFooter.footerValue)
	                        : "";


	                let newValue =
	                    value != null
	                        ? String(value)
	                        : "";


	                if (
	                    newValue !== oldValue &&
	                    (
	                        newValue !== "" ||
	                        oldValue !== ""
	                    )
	                ) {

	                    td.classList.add("changed");

	                }

	            }


	            tr.appendChild(td);

	        });

	    }


	    /*
	     * ============================================
	     * NON-INCENTIVE FOOTER
	     * ============================================
	     *
	     * ONE CELL across the complete payout table.
	     *
	     * Resolution column
	     * +
	     * Performance columns
	     */
	    else {

	        let td =
	            document.createElement("td");


	        /*
	         * IMPORTANT:
	         * Resolution + all performance columns
	         */
	        td.colSpan =
	            performances.length + 1;


	        td.className =
	            "flows-footer-full-row";


	        td.innerText =
	            group.footerText || "";


	        /*
	         * Highlight if footer changed
	         */
	        if (
	            highlightChanges &&
	            compareMaster
	        ) {

	            let oldFooter =
	                compareFooters.find(function (oldFooter) {

	                    return (

	                        String(
	                            oldFooter.footerType || ""
	                        ).toUpperCase() ===
	                        footerType

	                        &&

	                        String(
	                            oldFooter.footerText || ""
	                        ) ===
	                        String(
	                            group.footerText || ""
	                        )

	                    );

	                });


	            if (!oldFooter) {

	                td.classList.add("changed");

	            }

	            else if (
	                String(
	                    group.footerText || ""
	                ) !==
	                String(
	                    oldFooter.footerText || ""
	                )
	            ) {

	                td.classList.add("changed");

	            }

	        }

	        tr.appendChild(td);

	    }


	    body.appendChild(tr);

	});
}

function renderCompareFooters(
    master,
    compareMaster,
    bodyId,
    highlightChanges
) {

    let footers = master.footers || [];

    let compareFooters = compareMaster ? (compareMaster.footers || []) : [];

    let body = document.getElementById(bodyId);

    if (!body) {
        return;
    }

    body.innerHTML = "";

    if (footers.length === 0) {

        body.innerHTML =
            "<tr>" +
            "<td colspan='4' " +
            "style='text-align:center;font-weight:bold;padding:20px;'>" +
            "No Footer Configured" +
            "</td>" +
            "</tr>";

        return;
    }

    footers.forEach(function(footer) {

        let tr = document.createElement("tr");

        // Footer Type
        let typeTd = document.createElement("td");
        typeTd.innerText =footer.footerType || "";

        tr.appendChild(typeTd);

        // Performance
        let performanceTd = document.createElement("td");

        performanceTd.innerText =
            getPerformanceText(
                master.performances || [],
                footer.performanceId
            );

        tr.appendChild(performanceTd);


        // Footer Text
        let textTd = document.createElement("td");
        textTd.innerText =footer.footerText || "";

        tr.appendChild(textTd);

        // Footer Value
        let valueTd = document.createElement("td");

        valueTd.innerText =
            footer.footerValue == null
                ? ""
                : footer.footerValue;

        tr.appendChild(valueTd);


        //Compare with previous/current approved footer
        
        if (highlightChanges && compareMaster) {

            let oldFooter =
                findMatchingFooter(
                    compareFooters,
                    footer
                );

            if (oldFooter) {

                if (String(footer.footerType || "") !== String(oldFooter.footerType || "")) {
                    typeTd.classList.add("changed");
                }

                if (String(footer.footerText || "") !==String(oldFooter.footerText || "")) {
                    textTd.classList.add("changed");
                }

                if (String(footer.footerValue ?? "") !== String(oldFooter.footerValue ?? "")) {
                    valueTd.classList.add("changed");
                }

                let newPerformance = getPerformanceText(
                        master.performances || [],
                        footer.performanceId
                    );

                let oldPerformance = getPerformanceText(
                        compareMaster.performances || [],
                        oldFooter.performanceId
                    );

                if (newPerformance !== oldPerformance) {
                    performanceTd.classList.add("changed");

                }

            } else {
                //New footer exists only in TOP
                typeTd.classList.add("changed");
                performanceTd.classList.add("changed");
                textTd.classList.add("changed");
                valueTd.classList.add("changed");

            }
        }
        body.appendChild(tr);
    });
}

function findMatchingFooter(footers, footer) {

    return footers.find(function(oldFooter) {

        //INCENTIVE footer: match using performanceId
        if (String(footer.footerType || "").toUpperCase() === "INCENTIVE") {

            return Number(oldFooter.performanceId) === Number(footer.performanceId);
        }

        // Non-performance footer: match using footer type
        return String(oldFooter.footerType || "").toUpperCase() ===
               String(footer.footerType || "").toUpperCase();

    });
}

function getPerformanceText(performances,performanceId) {

    if (performanceId == null) {
        return "";
    }

    let performance = performances.find(function(p) {
            return Number(p.id) === Number(performanceId);
        });

    if (!performance) {
        return "";
    }
    return performance.displayText || "";
}

function findPayoutValue(
	payoutList, 
	resolutionId, 
	performanceId) {

    let item = payoutList.find(function(p) {

        return Number(p.resolutionId) === Number(resolutionId) 
		&& 
		Number(p.performanceId) === Number(performanceId);
    });

    if (!item) {
        return "0";
    }

    return item.payoutPercent == null ? "0" : item.payoutPercent;
}

function formatDateOnly(date) {

    if (!date) {
        return "";
    }

    return new Date(date).toLocaleDateString("en-GB");
}

function goBack() {
    window.history.back();
}