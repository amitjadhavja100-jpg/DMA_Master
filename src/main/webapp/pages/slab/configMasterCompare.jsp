<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

<title>Config Master Compare</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/slabPayoutConfig/configMaster.css">

</head>

<body>

<div class="container-fluid">

<!-- <div class="box">
 -->
 <div class="compareCard">
 
 
<script>
/* function goBack(){
    let master =
        new URLSearchParams(window.location.search)
        .get("master");
    if(master === "CONFIGCOMPAREMAKER"){
        window.location.href =
            "${pageContext.request.contextPath}"
            + "/mainPage/load?master=CONFIGMAKER";
    }else if(master === "CONFIGCOMPARECHECKER"){
        window.location.href =
            "${pageContext.request.contextPath}"
            + "/mainPage/load?master=CONFIGCHECKER";
    }else{
        window.location.href =
            "${pageContext.request.contextPath}"
            + "/mainPage/load?master=CONFIGCHECKER";
    }
} */

function goBack() {
    window.history.back();
}
</script>
 
<!--  <div class="compare-header"
     style="display:flex;justify-content:space-between;align-items:center;">
<h2>Config Master Screen</h2>

<button
        type="button"
        class="btn btn-primary"
        onclick="goBack()">
         Back
    </button> -->
    
    <div class="compare-header">

    <button
        type="button"
        class="btn btn-primary backBtn"
        onclick="goBack()">
        Back
    </button>

    <h2>Config Master Screen</h2>

</div>



<!-- TOP -->

<!-- <div id="topTitle"
class="title"> -->
<div id="topTitle" class="compareTitle">

TOP

</div>

<!-- <table class="table table-bordered"> -->
<table class="compare-table">

<thead>

<!-- <tr id="topHeader">

<th>ID</th>

</tr> -->

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
<!-- <table class="table table-bordered compare-table"> -->
<table class="compare-table">

<thead>

<!-- <tr id="bottomHeader">

<th>ID</th>

</tr> -->

</thead>

<tbody id="bottomBody">

</tbody>

</table>

</div>

</div>



<script>

document.addEventListener("DOMContentLoaded", function(){

    const params =
    new URLSearchParams(window.location.search);

     const id =
        params.get("id");

        const tempId =
        params.get("tempId");

        const mode =
        params.get("mode");

        let api="";

        if(mode==="maker"){

            api =
            "${pageContext.request.contextPath}" +
            "/config/makerCompare/" +
            id;

            document.getElementById("topTitle").innerHTML =
            "CURRENT APPROVED RECORD";

            document.getElementById("bottomTitle").innerHTML =
            "PREVIOUS APPROVED RECORD";

        }else{

            api =
            "${pageContext.request.contextPath}" +
            "/config/checkerCompare/" +
            tempId;

            document.getElementById("topTitle").innerHTML =
            "EDITED PENDING RECORD";

            document.getElementById("bottomTitle").innerHTML =
            "CURRENT APPROVED RECORD";
        } 
  
    fetch(api)

    .then(r => r.json())

    .then(data => {

        console.log("COMPARE DATA =", data);

        if(mode === "maker"){

            renderMaker(data);

        }else{

            renderChecker(data);

        }

    })

    .catch(err => {

        console.log("ERROR =", err);

    });


function renderMaker(data){

let top=data.top;
let bottom=data.bottom;

document.getElementById("topBody").innerHTML=

"<tr><td><b>Config Type</b></td><td>"+(top.configType||"")+"</td></tr>"+

"<tr><td><b>Parent Key</b></td><td>"+(top.parentKey||"-")+"</td></tr>"+

"<tr><td><b>Config Key</b></td><td>"+(top.configKey||"")+"</td></tr>"+

"<tr><td><b>Config Value</b></td><td>"+(top.configValue||"")+"</td></tr>"+

"<tr><td><b>Display Order</b></td><td>"+(top.displayOrder||"")+"</td></tr>"+

 "<tr><td><b>Status</b></td><td><span class='approvedStatus'>"
 +(top.status||"")+
 "</span></td></tr>";
 
if(bottom){

document.getElementById("bottomBody").innerHTML=

"<tr><td><b>Action</b></td><td>"+(bottom.actionType||"")+"</td></tr>"+

"<tr><td><b>Previous Value</b></td><td>"+(bottom.oldValue||"")+"</td></tr>"+

"<tr><td><b>Approved Value</b></td><td>"+(bottom.newValue||"")+"</td></tr>"+

"<tr><td><b>Checker</b></td><td>"+(bottom.checkerId||"")+"</td></tr>"+

"<tr><td><b>Approved Date</b></td><td>"+(bottom.checkerDate||"")+"</td></tr>"+
 
"<tr><td><b>Status</b></td><td><span class='approvedStatus'>"
+(bottom.status||"")+
"</span></td></tr>";

}
else{

document.getElementById("bottomBody").innerHTML=
 "<tr><td colspan='2'>No Previous Approved Record Found</td></tr>"; 

}

}

function renderChecker(data){

let top=data.top;
let bottom=data.bottom;

document.getElementById("topBody").innerHTML=

"<tr><td><b>Action</b></td><td>"+(top.actionType||"")+"</td></tr>"+

"<tr><td><b>Config Type</b></td><td>"+(top.configType||"")+"</td></tr>"+

"<tr><td><b>Parent Key</b></td><td>"+(top.parentKey||"-")+"</td></tr>"+

"<tr><td><b>Config Key</b></td><td>"+(top.configKey||"")+"</td></tr>"+

"<tr><td><b>New Value</b></td><td>"+(top.newValue||"")+"</td></tr>"+

"<tr><td><b>Display Order</b></td><td>"+(top.displayOrder||"")+"</td></tr>"+

"<tr><td><b>Maker</b></td><td>"+(top.makerId||"")+"</td></tr>"+

 "<tr><td><b>Status</b></td><td><span class='pendingStatus'>"
 +(top.status||"")+
 "</span></td></tr>";
 
if(bottom){

document.getElementById("bottomBody").innerHTML=

"<tr><td><b>Config Type</b></td><td>"+(bottom.configType||"")+"</td></tr>"+

"<tr><td><b>Parent Key</b></td><td>"+(bottom.parentKey||"-")+"</td></tr>"+

"<tr><td><b>Config Key</b></td><td>"+(bottom.configKey||"")+"</td></tr>"+

"<tr><td><b>Current Value</b></td><td>"+(bottom.configValue||"")+"</td></tr>"+

"<tr><td><b>Display Order</b></td><td>"+(bottom.displayOrder||"")+"</td></tr>"+
 
"<tr><td><b>Status</b></td><td><span class='approvedStatus'>"
+(bottom.status||"")+
"</span></td></tr>";
 }
else{

document.getElementById("bottomBody").innerHTML=
"<tr><td colspan='2'>No Approved Record Found</td></tr>";

}

}          
});        
</script>

</body>

</html>






