<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

<title>Payout Compare</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/slabPayoutConfig/slabConfig.css">

<!-- <link
href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
rel="stylesheet"> -->


</head>

<body>

<div class="container-fluid">

<div class="box">



<script>

/* function goBack(){

    let master =
        new URLSearchParams(
            window.location.search)
        .get("master");

    if(master ===
       "PAYOUTCOMPAREMAKER"){

        window.location.href =
            "${pageContext.request.contextPath}"
            + "/mainPage/load?master=SLABMAKER";
    }
    else{

        window.location.href =
            "${pageContext.request.contextPath}"
            + "/mainPage/load?master=SLABCHECKER";
    }
} */

function goBack() {
    window.history.back();
}

</script>

<div class="compare-header"
     style="display:flex;justify-content:space-between;align-items:center;">

    <h3>Payout Compare Screen</h3>

    <button
        type="button"
        class="btn btn-primary"
        onclick="goBack()">

         Back

    </button>

</div>


<!-- TOP -->

<div id="topTitle"
class="title">

TOP

</div>

<table class="table table-bordered">

<thead>

<tr id="topHeader">

<th>Collection</th>

</tr>

</thead>

<tbody id="topBody">

</tbody>

</table>



<!-- BOTTOM -->

<div id="bottomTitle"
class="title">

BOTTOM

</div>

<!-- <table class="table table-bordered"> -->
<table class="table table-bordered compare-table">

<thead>

<tr id="bottomHeader">

<th>Collection</th>

</tr>

</thead>

<tbody id="bottomBody">

</tbody>

</table>

</div>

</div>



<script>

 /* window.onload = function(){  */
	 document.addEventListener("DOMContentLoaded", function(){
	 
	 /* alert("COMPARE JS LOADED"); */
	 
    const params =
    	new URLSearchParams(window.location.search);

   /*  var userId = localStorage.getItem("user_id"); */
/* var userId = "BAN513834"; */
	 
    	const id =
    	params.get("id");

    	const mode =
    	params.get("mode");

    	console.log("COMPARE ID =", id);
    	console.log("MODE =", mode);
    	
    	let api = "";

    	/* if(mode === userId){ */
    		if(mode === "maker"){

    	    api =
    	        "${pageContext.request.contextPath}" +
    	        "/payout/makerCompare/" + id;

    	    console.log("API =", api);

    	    document.getElementById("topTitle")
    	        .innerHTML =
    	        "CURRENT APPROVED RECORD";

    	    document.getElementById("bottomTitle")
    	        .innerHTML =
    	        "PREVIOUS APPROVED RECORD";

    	}else{

    	    api =
    	        "${pageContext.request.contextPath}" +
    	        "/payout/checkerCompare/" + id;

    	    console.log("API =", api);

    	    document.getElementById("topTitle")
    	        .innerHTML =
    	        "EDITED PENDING RECORD";

    	    document.getElementById("bottomTitle")
    	        .innerHTML =
    	        "CURRENT APPROVED RECORD";
    	}

    	console.log("FINAL API =", api);
    	
      

    fetch(api)

    .then(res => res.json())

    .then(data => {

        console.log("FULL DATA =", data);

        window.compareData = {};

        if(data.bottom &&
           data.bottom.details){

            data.bottom.details.forEach(d => {

                if(!window.compareData[d.collectionSlab]){

                    window.compareData[d.collectionSlab] = {};
                }

                window.compareData
                [d.collectionSlab]
                [d.ceRange]
                = d.payout;
            });
        }

        // TOP TABLE
        renderTable(
            data.top,
            "topHeader",
            "topBody"
        );

        window.compareData = {};

        if(data.top &&
           data.top.details){

            data.top.details.forEach(d => {

                if(!window.compareData[d.collectionSlab]){

                    window.compareData[d.collectionSlab] = {};
                }

                window.compareData
                [d.collectionSlab]
                [d.ceRange]
                = d.payout;
            });
        }
        
        /* if(data.bottom){

            renderTable(
                data.bottom,
                "bottomHeader",
                "bottomBody"
            );

        }else{

            document.getElementById("bottomBody")
            .innerHTML =

            "<tr><td colspan='20'>No Previous Approved Record</td></tr>";
        } */
        
        if(data.bottom){

            renderTable(
                data.bottom,
                "bottomHeader",
                "bottomBody"
            );

        }else{

            document.getElementById("bottomHeader")
            .innerHTML = "";

            document.getElementById("bottomBody")
            .innerHTML =
            "<tr>" +
            "<td colspan='20' " +
            "style='text-align:center;font-weight:bold;padding:20px;'>" +
            "No Previous Approved Record" +
            "</td>" +
            "</tr>";
        }
        

    })

    .catch(err => {

        console.log("ERROR =", err);

    });

});


function renderTable(
master,
headerId,
bodyId){

    if(master == null){

        console.log("MASTER NULL");

        return;
    }


    let details =
    master.details;

    console.log("DETAILS =", details);


    if(details == null ||
       details.length == 0){

        console.log("DETAILS EMPTY");

        return;
    }


    let header =
    document.getElementById(headerId);

    let body =
    document.getElementById(bodyId);


    header.innerHTML = "";

    body.innerHTML = "";
    
    let ceRanges = [];

    details.forEach(d => {

    if(d.ceRange &&
    !ceRanges.includes(d.ceRange)){

    ceRanges.push(d.ceRange);
    }

    });

    // IF NO HEADER FOUND

    if(ceRanges.length === 0){

    ceRanges.push("Payout %");
    }


    let headerHtml =
    "<th>Collection</th>";

    ceRanges.forEach(c => {

        headerHtml +=
        "<th>" +
        c +
        "</th>";

    });

    header.innerHTML =
    headerHtml;

    // GROUPING
    let grouped = {};
    details.forEach(d => {

        if(!grouped[d.collectionSlab]){

            grouped[d.collectionSlab] = {};
        }
        
        let key = d.ceRange || "Payout %";

        grouped[d.collectionSlab][key]
        = d.payout;

    });

    console.log("GROUPED =", grouped);
    // ROWS
    let bodyHtml = "";
    Object.keys(grouped).forEach(row => {

        bodyHtml += "<tr>";

        bodyHtml +=
        "<td>" +
        row +
        "</td>";

        ceRanges.forEach(c => {

            let value =
            grouped[row][c];

            let changed = false;

            // COMPARE LOGIC
            if(window.compareData){

                let compareGrouped =
                window.compareData[row];

                if(!compareGrouped){

                    // ENTIRE NEW ROW
                    changed = true;

                }else{

                    let compareValue =
                    compareGrouped[c];

                    // NEW COLUMN
                    if(compareValue == null){

                        changed = true;

                    }else if(value != compareValue){

                        changed = true;
                    }
                }
                
            }

            bodyHtml +=
            "<td class='" +
            (changed ? "changed" : "") +
            "'>" +
            (value != null ? value : "") +
            "</td>";

        });


        bodyHtml += "</tr>";

    });


    body.innerHTML =
    bodyHtml;

}

</script>

</body>

</html>

