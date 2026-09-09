/*ospConfigMaterCompare.js*/

let mode;

document.addEventListener("DOMContentLoaded", function(){

    const params=new URLSearchParams(window.location.search);

    const tempId=params.get("tempId");
    const id=params.get("id");
    //const mode=params.get("mode");
    const type=params.get("type");
	
	mode = params.get("mode");

    let api="";

    if(mode==="maker"){

        api=
           CONTEXT_PATH +
            "/osp/config/makerCompare/"
            +id+
            "?type="+type;

        document.getElementById("topTitle").innerHTML=
            "CURRENT APPROVED RECORD";

        document.getElementById("bottomTitle").innerHTML=
            "PREVIOUS APPROVED RECORD";

    }else{

        api=
            CONTEXT_PATH +
            "/osp/config/checkerCompare/"
            +tempId+
            "?type="+type;

        document.getElementById("topTitle").innerHTML=
            "EDITED PENDING RECORD";

        document.getElementById("bottomTitle").innerHTML=
            "APPROVED RECORD";

    }

    fetch(api)
        .then(r=>r.json())
		.then(function(data){
		    console.log(data);
		    renderCompare(data, type);
		});
});

function goBack() {
    window.history.back();
}

function diffClass(a, b) {

    return String(a ?? "") !== String(b ?? "")
        ? "highlightDiff"
        : "";

}

function renderCompare(data, type){

    let top = data.top;
    let bottom = data.bottom;

    if(type === "DPD"){

        renderDpd(top, bottom);

    }else if(type === "COLLECTION_RANGE"){

        renderRange(top, bottom);

    }

}


function renderDpd(top, bottom) {

	if (mode === "maker") {

	    document.getElementById("topBody").innerHTML =
	        "<tr><td><b>Current DPD</b></td><td>" + (top.dpdName || "") + "</td></tr>" +
	        "<tr><td><b>Status</b></td><td>" + (top.status || "") + "</td></tr>";

	    if (bottom && bottom.oldDpdName) {
	        document.getElementById("bottomBody").innerHTML =
	            "<tr><td><b>Previous DPD</b></td><td>" + (bottom.oldDpdName || "") + "</td></tr>" +
	            "<tr><td><b>Status</b></td><td>" + (bottom.status || "") + "</td></tr>";
	    } else {
	        document.getElementById("bottomBody").innerHTML =
	            "<tr>" +
	                "<td colspan='2' style='text-align:center;font-weight:bold;color:#666;'>" + "No Previous Approved Record" + "</td>" +
	            "</tr>";
	    }
	} 
	else {
	    // If no approved record exists (Insert action)
	    if (!bottom) {
			document.getElementById("topTitle").innerHTML = "PENDING RECORD";
	        document.getElementById("bottomTitle").innerHTML =  "APPROVED RECORD NOT FOUND";

	        document.getElementById("topBody").innerHTML =
	            "<tr><td><b>Action</b></td><td>"+(top.actionType||"")+"</td></tr>"+
	            "<tr><td><b>DPD</b></td><td>"+(top.dpdName||"")+"</td></tr>"+
	            "<tr><td><b>Status</b></td><td>"+(top.status||"")+"</td></tr>";

	        document.getElementById("bottomBody").innerHTML =
	            "<tr>" +
	            "<td colspan='2' style='text-align:center;font-weight:bold;color:#666;'>" + "Approved Record Not Found" +"</td>" +
	            "</tr>";
	        return;
	    }
	    // Update case
	    let dpdClass = diffClass(top.dpdName, bottom.dpdName);
		
		document.getElementById("topTitle").innerHTML = "EDITED PENDING RECORD";
        document.getElementById("bottomTitle").innerHTML = "CURRENT APPROVED RECORD";

	    document.getElementById("topBody").innerHTML =
	        "<tr><td><b>Action</b></td><td>"+(top.actionType||"")+"</td></tr>"+
	        "<tr><td><b>DPD</b></td><td class='"+dpdClass+"'>"+(top.dpdName||"")+"</td></tr>"+
	        "<tr><td><b>Status</b></td><td>"+(top.status||"")+"</td></tr>";

	    document.getElementById("bottomBody").innerHTML =
	        "<tr><td><b>DPD</b></td><td class='"+dpdClass+"'>"+(bottom.dpdName||"")+"</td></tr>"+
	        "<tr><td><b>Status</b></td><td>"+(bottom.status||"")+"</td></tr>";
	}
}

function renderRange(top, bottom) {

    if (mode === "maker") {

        document.getElementById("topBody").innerHTML =
            "<tr><td><b>DPD</b></td><td>"+(top.dpdName||"")+"</td></tr>"+
            "<tr><td><b>From Amount</b></td><td>"+(top.fromAmount||"")+"</td></tr>"+
		   	"<tr><td><b>MAX Range</b></td><td>"+(top.isMax ? "Yes" : "No")+"</td></tr>"+
		   	"<tr><td><b>To Amount</b></td><td>"+(top.isMax ? "MAX" : top.toAmount)+"</td></tr>"+
            "<tr><td><b>Order No</b></td><td>"+(top.orderNo||"")+"</td></tr>"+
            "<tr><td><b>Status</b></td><td>"+(top.status||"")+"</td></tr>";

        if (bottom && bottom.fromAmount != null) {

            document.getElementById("bottomBody").innerHTML =
                "<tr><td><b>DPD</b></td><td>"+(bottom.dpdName||"")+"</td></tr>"+
                "<tr><td><b>Previous From Amount</b></td><td>"+(bottom.fromAmount||"")+"</td></tr>"+
				"<tr><td><b>MAX Range</b></td><td>" +(bottom.isMax ? "Yes" : "No")+"</td></tr>"+
				"<tr><td><b>Previous To Amount</b></td><td>"+(bottom.isMax ? "MAX" : bottom.toAmount)+"</td></tr>"+
                "<tr><td><b>Previous Order No</b></td><td>"+(bottom.orderNo||"")+"</td></tr>"+
                "<tr><td><b>Status</b></td><td>"+(bottom.status||"")+"</td></tr>";
        } else {
            document.getElementById("bottomBody").innerHTML =
                "<tr><td colspan='2' style='text-align:center;font-weight:bold;color:#666;'>No Previous Approved Record</td></tr>";
        }
    } 
	else {
	    // Insert case
	    if (!bottom) {

			document.getElementById("topTitle").innerHTML = "PENDING RECORD";
	        document.getElementById("bottomTitle").innerHTML = "APPROVED RECORD NOT FOUND";

	        document.getElementById("topBody").innerHTML =
	            "<tr><td><b>Action</b></td><td>"+(top.actionType||"")+"</td></tr>"+
	            "<tr><td><b>DPD</b></td><td>"+(top.dpdName||"")+"</td></tr>"+
	            "<tr><td><b>From Amount</b></td><td>"+(top.fromAmount||"")+"</td></tr>"+
				"<tr><td><b>MAX Range</b></td><td>"+(top.isMax ? "Yes" : "No")+"</td></tr>"+
				"<tr><td><b>To Amount</b></td><td>"+(top.isMax ? "MAX" : top.toAmount)+"</td></tr>"+
	            //"<tr><td><b>To Amount</b></td><td>"+(top.toAmount||"")+"</td></tr>"+
	            "<tr><td><b>Order No</b></td><td>"+(top.orderNo||"")+"</td></tr>"+
	            "<tr><td><b>Status</b></td><td>"+(top.status||"")+"</td></tr>";

	        document.getElementById("bottomBody").innerHTML =
	            "<tr>" +
	            "<td colspan='2' style='text-align:center;font-weight:bold;color:#666;'>" + "Approved Record Not Found" + "</td>" +
	            "</tr>";

	        return;
	    }
		document.getElementById("topTitle").innerHTML ="EDITED PENDING RECORD";
		document.getElementById("bottomTitle").innerHTML = "CURRENT APPROVED RECORD";

	    // Update case
	    let dpdClass   = diffClass(top.dpdName, bottom.dpdName);
	    let fromClass  = diffClass(top.fromAmount, bottom.fromAmount);
	    //let toClass    = diffClass(top.toAmount, bottom.toAmount);
        let toClass = diffClass(
                top.isMax ? "MAX" : top.toAmount,
                bottom.isMax ? "MAX" : bottom.toAmount
            );
	    let orderClass = diffClass(top.orderNo, bottom.orderNo);

	    document.getElementById("topBody").innerHTML =
	        "<tr><td><b>Action</b></td><td>"+(top.actionType||"")+"</td></tr>"+
	        "<tr><td><b>DPD</b></td><td class='"+dpdClass+"'>"+(top.dpdName||"")+"</td></tr>"+
	        "<tr><td><b>From Amount</b></td><td class='"+fromClass+"'>"+(top.fromAmount||"")+"</td></tr>"+
			"<tr><td><b>MAX Range</b></td><td>"+(top.isMax ? "Yes" : "No")+"</td></tr>"+
			"<tr><td><b>To Amount</b></td><td class='"+toClass+"'>" +(top.isMax ? "MAX" : top.toAmount)+"</td></tr>"+
	        "<tr><td><b>Order No</b></td><td class='"+orderClass+"'>"+(top.orderNo||"")+"</td></tr>"+
	        "<tr><td><b>Status</b></td><td>"+(top.status||"")+"</td></tr>";

	    document.getElementById("bottomBody").innerHTML =
	        "<tr><td><b>DPD</b></td><td class='"+dpdClass+"'>"+(bottom.dpdName||"")+"</td></tr>"+
	        "<tr><td><b>From Amount</b></td><td class='"+fromClass+"'>"+(bottom.fromAmount||"")+"</td></tr>"+
			"<tr><td><b>MAX Range</b></td><td>" +(bottom.isMax ? "Yes" : "No") +"</td></tr>"+
			"<tr><td><b>To Amount</b></td><td class='"+toClass+"'>" +(bottom.isMax ? "MAX" : bottom.toAmount)+"</td></tr>"
	        "<tr><td><b>Order No</b></td><td class='"+orderClass+"'>"+(bottom.orderNo||"")+"</td></tr>"+
	        "<tr><td><b>Status</b></td><td>"+(bottom.status||"")+"</td></tr>";
	}

}
