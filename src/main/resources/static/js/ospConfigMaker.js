/*ospConfigMaker.js*/
/*const BASE_URL = CONTEXT_PATH;*/

let dpdList = [];
let ospCollectionRanges = [];
let ospEditMode = false;
let selectedOspDpdId = null;	
let selectedOspRangeId = null;


function initOspConfig(){

    resetOspDpd();
    resetOspRange();
    loadOspDpds();
	
    document.getElementById("ospRangeBody").innerHTML="";

}

/*snz*/
function changeOspType(){

    let type = document.getElementById("ospType").value;

    document.getElementById("ospDpdSection").style.display="none";
    document.getElementById("ospRangeSection").style.display="none";

    if(type=="DPD"){
        document.getElementById("ospDpdSection").style.display="block";
        loadOspDpds();

    }
    else if(type=="COLLECTION_RANGE"){
        document.getElementById("ospRangeSection").style.display="block";
        loadOspDpds();

    }

}

function loadOspRanges(){

    let dpdId=document.getElementById("ospDpd").value;
    if(dpdId==""){
        return;
    }
    fetch(
        CONTEXT_PATH+
        "/osp/config/collection-range/approved?dpdId="+dpdId
    )
    .then(r=>r.json())
    .then(renderOspRangeTable);
}

function renderOspRangeTable(data){

    let tbody=document.getElementById("ospRangeBody");
    tbody.innerHTML="";

    data.forEach(function(r){
        tbody.innerHTML+=`
        <tr>
        <td>${r.fromAmount}</td>
        <td>${r.isMax ? "MAX" : r.toAmount}</td>
        <td>${r.orderNo}</td>
		<td>
		<button class="actionBtn editBtn" onclick="editOspRange(${r.id})">Edit</button>
		<button class="actionBtn activateBtn" onclick="viewMakerCompare(${r.id},'COLLECTION_RANGE')"> History</button>
		</td>
        </tr>
        `;
    });
}

function saveOspDpd(){

    let dpdName = document.getElementById("ospDpdName").value.trim();

    if(dpdName==""){
        alert("Please Enter DPD Name");
        return;
    }

	let payload = {
        id: selectedOspDpdId,
        dpdName: dpdName,
    };

    let url;
    let method;

    if (ospEditMode) {
        url = CONTEXT_PATH + "/osp/config/dpd/update";
        method = "PUT";
    } else {
        url = CONTEXT_PATH + "/osp/config/dpd/save";
        method = "POST";
    }

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "userId": LOGIN_USER
        },
        body: JSON.stringify(payload)
    }
    )
    .then(r=>r.text())
	.then(function(msg){

	    alert(msg);
	    ospEditMode = false;
	    selectedOspDpdId = null;
	    document.getElementById("saveDpdBtn").innerHTML = "Save DPD";
	    resetOspDpd();
	    loadOspDpds();
	});
}
function cancelOspDpdEdit(){

    ospEditMode = false;
    selectedOspDpdId = null;

    resetOspDpd();

    document.getElementById("saveDpdBtn").innerHTML = "Save DPD";
}

function cancelOspRangeEdit(){

    ospEditMode = false;
    selectedOspRangeId = null;

    resetOspRange();

    document.getElementById("ospDpd").value = "";

    document.getElementById("saveRangeBtn").innerHTML = "Save Range";
}

function resetOspDpd(){

    document.getElementById("ospDpdName").value = "";

    ospEditMode = false;
    selectedOspDpdId = null;

    document.getElementById("saveDpdBtn").innerHTML = "Save DPD";
}

function saveOspRange(){

    let dpdId = document.getElementById("ospDpd").value;
    let fromAmount = document.getElementById("ospFromAmount").value;
    let toAmount =document.getElementById("ospToAmount").value;
    let orderNo =document.getElementById("ospOrderNo").value;
	let isMax = document.getElementById("ospIsMax").checked;

    if(dpdId==""){
        alert("Please Select DPD");
        return;
    }

    if(fromAmount==""){
        alert("Please Enter From Amount");
        return;
    }

	if(!isMax && toAmount==""){
	    alert("Please Enter To Amount");
	    return;
	}

    if(orderNo==""){
        alert("Please Enter Order");
        return;
    }

	/*let payload={

	    id:selectedOspRangeId,
	    dpdId:dpdId,
	    fromAmount:fromAmount,
	    toAmount:toAmount,
	    orderNo:orderNo
	};*/
	

	let payload = {
	    id: selectedOspRangeId,
	    dpdId: dpdId,
	    fromAmount: fromAmount,
	    toAmount: isMax ? null : toAmount,
	    orderNo: orderNo,
	    isMax: isMax
	};
	let url;
	let method;

	if(ospEditMode){
	    url = CONTEXT_PATH + "/osp/config/collection-range/update";
	    method = "PUT";
	}else{
	    url = CONTEXT_PATH + "/osp/config/collection-range/save";
	    method = "POST";
	}

	fetch(url,
	{
	    method:method,
            headers:{
                "Content-Type":"application/json",
                "userId":LOGIN_USER
            },
            body:JSON.stringify(payload)
        }
    )
    .then(r=>r.text())
	.then(function(msg){

	    alert(msg);
	    ospEditMode = false;
	    selectedOspRangeId = null;
		
	    document.getElementById("saveRangeBtn").innerHTML = "Save Range";

	    resetOspRange();
	    loadOspRanges();

	});
}

function toggleMaxRange(){

    let chk = document.getElementById("ospIsMax");
    let txt =document.getElementById("ospToAmount");

    if(chk.checked){
        txt.value="";
        txt.disabled=true;
    }else{
        txt.disabled=false;
    }
}

function resetOspRange(){

    document.getElementById("ospDpd").value = "";
    document.getElementById("ospFromAmount").value = "";
	document.getElementById("ospToAmount").value= "";
	document.getElementById("ospToAmount").disabled=false;
	document.getElementById("ospIsMax").checked=false;
    document.getElementById("ospOrderNo").value = "";

    ospEditMode = false;
    selectedOspRangeId = null;

    document.getElementById("saveRangeBtn").innerHTML = "Save Range";
}

/*function resetOspRange(){
    document.getElementById("ospFromAmount").value="";
    document.getElementById("ospToAmount").value="";
    document.getElementById("ospOrderNo").value="";
}*/
function loadOspDpds() {

    fetch(CONTEXT_PATH + "/osp/config/dpd/approved")
        .then(r => r.json())
        .then(function(data) {

            let ddl = document.getElementById("ospDpd");
            ddl.innerHTML = "<option value=''>Select DPD</option>";

            let tbody = document.getElementById("ospDpdBody");
            tbody.innerHTML = "";

            data.forEach(function(d) {
                ddl.innerHTML +=
                    "<option value='" + d.id + "'>"
                    + d.dpdName +
                    "</option>";

                tbody.innerHTML +=

                    "<tr>" +
                    "<td>" + d.id + "</td>" +
                    "<td>" + d.dpdName + "</td>" +
                    "<td>" +

                    "<button class='actionBtn editBtn' onclick='editOspDpd(" + d.id + ")'>Edit</button>" +
                    "<button class='actionBtn activateBtn' onclick=\"viewMakerCompare(" + d.id + ",'DPD')\">History</button>" +
                    "</td>" +
                    "</tr>";

            });

        });

}

function editOspDpd(id){

    fetch(CONTEXT_PATH+"/osp/config/dpd/"+id)

    .then(r=>r.json())
    .then(function(data){

        selectedOspDpdId=data.id;
        ospEditMode=true;

        document.getElementById("ospDpdName").value=data.dpdName;
        document.getElementById("saveDpdBtn").innerHTML="Update DPD";

        window.scrollTo({
            top:0,
            behavior:"smooth"
        });
    });
}

function editOspRange(id) {

    fetch( CONTEXT_PATH + "/osp/config/collection-range/" + id)

        .then(r => r.json())
        .then(function(data) {

            selectedOspRangeId = data.id;
            ospEditMode = true;

            document.getElementById("ospDpd").value = data.dpdId;
            document.getElementById("ospFromAmount").value = data.fromAmount;
            //document.getElementById("ospToAmount").value = data.toAmount;
            document.getElementById("ospOrderNo").value = data.orderNo;
            document.getElementById("saveRangeBtn").innerHTML = "Update Range";
			
			if(data.isMax){
			    document.getElementById("ospIsMax").checked=true;
			    document.getElementById("ospToAmount").disabled=true;
			    document.getElementById("ospToAmount").value="";
			}else{
			    document.getElementById("ospIsMax").checked=false;
			    document.getElementById("ospToAmount").disabled=false;
			    document.getElementById("ospToAmount").value=data.toAmount;
			}

            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        });
}


function viewMakerCompare(id, type) {

    window.location.href =
        CONTEXT_PATH +
        "/mainPage/load?master=OSPCONFIGCOMPARE"
        + "&id=" + id
        + "&type=" + type
        + "&mode=maker";
}



