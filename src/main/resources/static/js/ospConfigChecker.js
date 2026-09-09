/*ospConfigChecker.js*/

let ospPendingList = [];

function initOspChecker(){
    loadOspPending();
}

/*function loadOspPending(){

    fetch(CONTEXT_PATH + "/osp/config/pending")
        .then(r => r.json())
        .then(renderOspPending);
} */


function loadOspPending(){

    fetch(CONTEXT_PATH + "/osp/config/pending", {
        headers:{
            "userId": LOGIN_USER
        }
    })
    .then(r => r.json())
    .then(renderOspPending);
}

function renderOspPending(data){

    let tbody=document.getElementById("ospCheckerBody");
    tbody.innerHTML="";

    if(data.length==0){
        tbody.innerHTML=
        "<tr><td colspan='10'>No Pending Records</td></tr>";
        return;
    }

    data.forEach(function(r){

        tbody.innerHTML+=`
        <tr>
            <td>${r.tempId}</td>
            <td>${r.type}</td>
            <td>${r.dpdName}</td>
            <td>${r.oldValue}</td>
            <td>${r.newValue}</td>
            <td>${r.actionType}</td>
            <td>${r.makerId}</td>
            <td>${r.status}</td>
            <td>
                <button class="btn btn-success btn-sm" onclick="approveOsp(${r.tempId},'${r.type}')">
                    Approve
                </button>

                <button class="btn btn-danger btn-sm" onclick="rejectOsp(${r.tempId},'${r.type}')">
                    Reject
                </button>

                <button class="btn btn-info btn-sm" onclick="viewOspCompare(${r.tempId},'${r.type}')">
                    View
                </button>
            </td>
        </tr>
        `;
    });
}

function approveOsp(tempId,type){

    fetch(CONTEXT_PATH+ "/osp/config/approve/"
        +tempId+
        "?type="+type,
        {
            method:"POST",
            headers:{
                "userId":LOGIN_USER
            }
        }
    )
    .then(r=>r.text())
    .then(function(msg){
        alert(msg);
        loadOspPending();
    });
}

function rejectOsp(tempId,type){

    let remarks=prompt("Enter Remarks");

    if(remarks==null){
        return;
    }

    fetch( CONTEXT_PATH+ "/osp/config/reject/"
        +tempId+
        "?type="+type+
        "&remarks="+encodeURIComponent(remarks),
        {
            method:"POST",
            headers:{
                "userId":LOGIN_USER
            }
        }
    )
    .then(r=>r.text())
    .then(function(msg){
        alert(msg);
        loadOspPending();
    });
}

function viewOspCompare(tempId,type){

	window.location.href =
	    CONTEXT_PATH +
	    "/mainPage/load?master=OSPCONFIGCOMPARE"
	    +"&tempId="+tempId
	    +"&type="+type
	    +"&mode=checker";

}