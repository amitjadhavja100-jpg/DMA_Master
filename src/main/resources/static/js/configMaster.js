/*const BASE_URL = window.location.origin;*/

const BASE_URL = CONTEXT_PATH;

let editMode = false;

/* ===================================
PAGE LOAD
=================================== */
//window.onload = function(){
document.addEventListener("DOMContentLoaded", function () {

    //alert("NEW JS LOADED");

	loadProducts();
	
    if(document.getElementById("tbody")){
        loadConfigs();
    }

    loadCategories();

    showHideFields();

   /* document.getElementById("configType")
    .addEventListener("change",function(){
	
	    editMode = false;
        resetTable();
        loadConfigs();
        showHideFields();

     document.getElementById("category").value = "";
     document.getElementById("city").value = "";
     document.getElementById("dpd").value = "";


     loadCategories(); 
     loadConfigs();
     loadDpds();

        /*loadCategories();     

        generateConfigKey();

        changeLabels();*/

document.getElementById("configType").addEventListener("change", function () {

    editMode = false;

    // Clear form
    document.getElementById("selectedConfigId").value = "";
    document.getElementById("parentKey").value = "";
    document.getElementById("configKey").value = "";
    document.getElementById("configValue").value = "";
    document.getElementById("displayOrder").value = "";
    document.getElementById("oldValue").value = "";

    // Clear dropdowns
    document.getElementById("category").innerHTML = "<option value=''>Select Category</option>";
    document.getElementById("city").innerHTML = "<option value=''>Select City</option>";
    document.getElementById("dpd").innerHTML = "<option value=''>Select DPD</option>";
    document.getElementById("collection").innerHTML = "<option value=''>Select Collection</option>";
    document.getElementById("ceRange").innerHTML = "<option value=''>Select CE Range</option>";

    // Clear text fields
    document.getElementById("categoryText").value = "";
    document.getElementById("cityText").value = "";
    document.getElementById("dpdText").value = "";

    // Refresh UI
    // Reload master data
    loadCategories();
  /*  loadCities();
    loadDpds();
    loadCollections();
    loadCeRanges();*/

    // Reload Grid
    showHideFields();
    loadConfigs();

});

   

    /*document.getElementById("category")
    .addEventListener("change",function(){

        loadCities();

        loadDpds();

        generateConfigKey();

    });

    document.getElementById("city")
    .addEventListener("change",function(){

        loadDpds();

        generateConfigKey();

    });*/

document.getElementById("category").addEventListener("change", function () {

    loadCities();
    loadDpds();
    loadCollections();
    loadCeRanges();

    loadConfigs();
});

document.getElementById("city").addEventListener("change", function () {

    loadConfigs();
});

document.getElementById("dpd").addEventListener("change", function () {

    loadCollections();
    loadCeRanges();

    loadConfigs();
});

document.getElementById("collection").addEventListener("change", function () {

    loadConfigs();
});

document.getElementById("ceRange").addEventListener("change", function () {

    loadConfigs();
});

    document.getElementById("dpd")
    .addEventListener("change",generateConfigKey);

    document.getElementById("configValue")
    .addEventListener("keyup",generateConfigKey);

    document.getElementById("categoryText")
    .addEventListener("keyup",generateConfigKey);

    document.getElementById("cityText")
    .addEventListener("keyup",generateConfigKey);

    document.getElementById("dpdText")
    .addEventListener("keyup",generateConfigKey);

});

function loadProducts(){

    fetch(BASE_URL + "/DMAPayoutWeb3/products")
    .then(r=>r.json())
    .then(data=>{
        let product=document.getElementById("product");

        product.innerHTML="<option value=''>Select Product</option>";
        data.forEach(function(p){

            product.innerHTML +=
            "<option value='"+p+"'>"+p+"</option>";
        });
    });
}

function loadSubProducts(){

    let product=document.getElementById("product").value;
    let sub=document.getElementById("subProduct");

    sub.innerHTML="<option value=''>Select Sub Product</option>";

    if(product==""){
        return;
    }
    fetch(
        BASE_URL+
        "/DMAPayoutWeb3/subProducts?product="
        +encodeURIComponent(product)
    )
    .then(r=>r.json())
    .then(data=>{
        data.forEach(function(s){
            sub.innerHTML +=
            "<option value='"+s+"'>"
            +s+
            "</option>";
        });
    });
}


function openSelectedConfigScreen(){

    let product = document.getElementById("product").value;
    let subProduct = document.getElementById("subProduct").value;

    document.getElementById("agencyConfigScreen").style.display="none";
    document.getElementById("ospConfigScreen").style.display="none";
	document.getElementById("ospTypeDiv").style.display = "none";

	const PRODUCT_AGENCY="Collection - Agency";
	const PRODUCT_OSP="Collection- Osp";

	const SUB_AGENCY="Recovery Credit Card";
	const SUB_OSP="Recovery Osp";
	
	if(product==PRODUCT_AGENCY && subProduct==SUB_AGENCY){
        document.getElementById("agencyConfigScreen").style.display="block";
        loadConfigs();
    }
	
	else if(product==PRODUCT_OSP &&  subProduct==SUB_OSP){

	    document.getElementById("ospConfigScreen").style.display="block";
	    document.getElementById("ospTypeDiv").style.display="block";
	    document.getElementById("ospType").value="";
	    document.getElementById("ospDpdSection").style.display="none";
	    document.getElementById("ospRangeSection").style.display="none";

	}
}

/* ===================================
LOAD CONFIGS
=================================== */

function loadConfigs(){
	
	 let tbody =
    document.getElementById(
    "tbody");
    tbody.innerHTML = "";
	
let status = "ALL";
if(document.getElementById("statusFilter")){
    status =
    document.getElementById(
    "statusFilter").value;
}
let type = document.getElementById("configType").value;

/*fetch(
BASE_URL +
"/config/all?status=" +
status +
"&type=" +
type)*/


let category = document.getElementById("category") ? document.getElementById("category").value : "";
let city = document.getElementById("city") ? document.getElementById("city").value : "";
let dpd = document.getElementById("dpd") ? document.getElementById("dpd").value : "";
let collection = document.getElementById("collection") ? document.getElementById("collection").value : "";
let ceRange = document.getElementById("ceRange") ? document.getElementById("ceRange").value : "";

fetch(
    BASE_URL +
    "/config/all?status=" + encodeURIComponent(status) +
    "&type=" + encodeURIComponent(type) +
    "&category=" + encodeURIComponent(category) +
    "&city=" + encodeURIComponent(city) +
    "&dpd=" + encodeURIComponent(dpd) +
    "&collection=" + encodeURIComponent(collection) +
    "&ceRange=" + encodeURIComponent(ceRange)
)
.then(r => r.json())
.then(data => {
	let type =
document.getElementById("configType").value;
	
    let tbody =
    document.getElementById(
    "tbody");
    tbody.innerHTML = "";
    if(!data || data.length===0){
        tbody.innerHTML =
        "<tr>" +
        "<td colspan='8' " +
        "style='text-align:center;" +
        "padding:20px;" +
        "font-weight:bold;'>"
        +
        "No Config Records Found"
        +
        "</td>"
        +
        "</tr>";
        return;
    }

  data.forEach(function(row){

    // ================= CATEGORY =================

    if(type=="CATEGORY"){

        let statusBadge =
        row.status=="Y"
        ?
        "<span class='activeBadge'>ACTIVE</span>"
        :
        "<span class='inactiveBadge'>INACTIVE</span>";

        tbody.innerHTML += `
        <tr>
            <td>${row.id}</td>
            <td>CATEGORY</td>
            <td>-</td>
            <td>${row.category}</td>
            <td>${row.category}</td>
            <td>${row.displayOrder}</td>
            <td>${statusBadge}</td>
            <td>
                <button class="actionBtn editBtn"
                onclick="editCategory(${row.id})">
                Edit
                </button>

                ${
                row.status=="Y"

                ?

                `<button class="actionBtn deactivateBtn"
                onclick="deactivateConfig('CATEGORY',${row.id})">
                Deactivate
                </button>`

                :

                `<button class="actionBtn activateBtn"
                onclick="activateConfig('CATEGORY',${row.id})">
                Activate
                </button>`
                }
            </td>
        </tr>`;

        return;
    }

    // ================= CITY =================

    if(type=="CITY"){

        let statusBadge =
        row.status=="Y"
        ?
        "<span class='activeBadge'>ACTIVE</span>"
        :
        "<span class='inactiveBadge'>INACTIVE</span>";

        tbody.innerHTML += `
        <tr>
            <td>${row.id}</td>
            <td>CITY</td>
            <td>${row.category}</td>
            <td>${row.city}</td>
            <td>${row.city}</td>
            <td>${row.displayOrder}</td>
            <td>${statusBadge}</td>
            <td>
                <button class="actionBtn editBtn"
                onclick="editCity(${row.id})">
                Edit
                </button>

                ${
                row.status=="Y"

                ?

                `<button class="actionBtn deactivateBtn"
                onclick="deactivateConfig('CITY',${row.id})">
                Deactivate
                </button>`

                :

                `<button class="actionBtn activateBtn"
                onclick="activateConfig('CITY',${row.id})">
                Activate
                </button>`
                }

            </td>
        </tr>`;

        return;
    }
    
    //====================DPD================
    
    if(type=="DPD"){

    let statusBadge =
    row.status=="Y"
    ?
    "<span class='activeBadge'>ACTIVE</span>"
    :
    "<span class='inactiveBadge'>INACTIVE</span>";

    tbody.innerHTML += `

    <tr>

        <td>${row.id}</td>

        <td>DPD</td>

        <td>${row.category}</td>

        <td>${row.dpd}</td>

        <td>${row.dpd}</td>

        <td>${row.orderNo}</td>

        <td>${statusBadge}</td>

        <td>

            <button
            class="actionBtn editBtn"
            onclick="editDpd(${row.id})">

            Edit

            </button>

            ${
            row.status=="Y"

            ?

            `<button
            class="actionBtn deactivateBtn"
            onclick="deactivateConfig('DPD',${row.id})">

            Deactivate

            </button>`

            :

            `<button
            class="actionBtn activateBtn"
            onclick="activateConfig('DPD',${row.id})">

            Activate

            </button>`

            }

        </td>

    </tr>

    `;

    return;

}
//===================COLLECTION================================
if(type=="COLLECTION"){

    let statusBadge =
    row.status=="Y"
    ?
    "<span class='activeBadge'>ACTIVE</span>"
    :
    "<span class='inactiveBadge'>INACTIVE</span>";

    tbody.innerHTML += `

    <tr>

        <td>${row.id}</td>

        <td>COLLECTION</td>

        <td>${row.category}</td>

        <td>${row.dpd}</td>

        <td>${row.slabValue}</td>

        <td>${row.orderNo}</td>

        <td>${statusBadge}</td>

        <td>

            <button
            class="actionBtn editBtn"
            onclick="editCollection(${row.id})">

            Edit

            </button>

            ${
            row.status=="Y"

            ?

            `<button
            class="actionBtn deactivateBtn"
            onclick="deactivateConfig('COLLECTION',${row.id})">

            Deactivate

            </button>`

            :

            `<button
            class="actionBtn activateBtn"
            onclick="activateConfig('COLLECTION',${row.id})">

            Activate

            </button>`

            }

        </td>

    </tr>

    `;

    return;

}
//=====================CE_RANGE================================

if(type=="CE_RANGE"){

    let statusBadge =
    row.status=="Y"
    ?
    "<span class='activeBadge'>ACTIVE</span>"
    :
    "<span class='inactiveBadge'>INACTIVE</span>";

    tbody.innerHTML += `

    <tr>

        <td>${row.id}</td>

        <td>CE RANGE</td>

        <td>${row.category}</td>

        <td>${row.dpd}</td>

        <td>${row.rangeValue}</td>

        <td>${row.orderNo}</td>

        <td>${statusBadge}</td>

        <td>

            <button
            class="actionBtn editBtn"
            onclick="editCeRange(${row.id})">

            Edit

            </button>

            ${
            row.status=="Y"

            ?

            `<button
            class="actionBtn deactivateBtn"
            onclick="deactivateConfig('CE_RANGE',${row.id})">

            Deactivate

            </button>`

            :

            `<button
            class="actionBtn activateBtn"
            onclick="activateConfig('CE_RANGE',${row.id})">

            Activate

            </button>`

            }

        </td>

    </tr>

    `;

    return;

}

//////============capping===============

if(type=="CAPPING"){

    let statusBadge =
    row.activeFlag=="Y"
    ? "<span class='activeBadge'>ACTIVE</span>"
    : "<span class='inactiveBadge'>INACTIVE</span>";

    tbody.innerHTML += `
    <tr>
        <td>${row.configId}</td>
        <td>CAPPING</td>
        <td>-</td>
        <td>${row.oldValue||''}</td>
        <td>${row.configValue||''}</td>
        <td>-</td>
        <td>${statusBadge}</td>
        <td>
            <button class="actionBtn editBtn"
                onclick="editConfig(${row.configId})">
                Edit
            </button>
        </td>
    </tr>`;
    return;
}

    // ================= FIELD/BUTTON/TITLE =================

    let statusBadge =
    row.activeFlag=="Y"
    ?
    "<span class='activeBadge'>ACTIVE</span>"
    :
    "<span class='inactiveBadge'>INACTIVE</span>";

    tbody.innerHTML += `
    <tr>
        <td>${row.configId}</td>
        <td>${row.configType||''}</td>
        <td>${row.parentKey||''}</td>
        <td>${row.configKey||''}</td>
        <td>${row.configValue||''}</td>
        <td>${row.displayOrder||''}</td>
        <td>${statusBadge}</td>
        <td>

            <button
            class="actionBtn editBtn"
            onclick="editConfig(${row.configId})">

            Edit

            </button>

            ${
            row.activeFlag=="Y"

            ?

            `<button
            class="actionBtn deactivateBtn"
            onclick="deactivateConfig('${row.configType}',${row.configId})">

            Deactivate

            </button>`

            :

            `<button
            class="actionBtn activateBtn"
            onclick="activateConfig('${row.configType}',${row.configId})">

            Activate

            </button>`

            }

        </td>
    </tr>`;
});

});
}


function editCategory(id){
    fetch(BASE_URL+"/config/category/"+id)
    .then(r=>r.json())
    .then(function(data){
        // VERY IMPORTANT
        document.getElementById("selectedConfigId").value = data.id;
        document.getElementById("categoryText").value = data.category;
        document.getElementById("displayOrder").value = data.displayOrder;
        editMode = true;
        window.scrollTo({
            top:0,
            behavior:"smooth"
        });
    });
}

function editCity(id){
fetch(BASE_URL+"/config/city/"+id)
.then(r=>r.json())
.then(function(data){
document.getElementById("selectedConfigId").value=data.id;
document.getElementById("configType").value="CITY";
showHideFields();
document.getElementById("category").value=data.category;
loadCities();
setTimeout(function () {
    document.getElementById("category").value = data.category;
    // Load cities for the selected category
    loadCities();
    setTimeout(function () {
        document.getElementById("cityText").value = data.city;
        document.getElementById("displayOrder").value = data.displayOrder;
    }, 200);
}, 200);
editMode = true;
window.scrollTo({
    top: 0,
    behavior: "smooth"
});
});
}


function editDpd(id){

    fetch(BASE_URL + "/config/dpd/" + id)
    .then(r => r.json())
    .then(function(data){

        document.getElementById("selectedConfigId").value = data.id;

        document.getElementById("configType").value = "DPD";

        showHideFields();        
        loadCategories(data.category);

        setTimeout(function(){

            document.getElementById("dpdText").value = data.dpd;
            document.getElementById("displayOrder").value = data.orderNo;

            editMode = true;

        },300);

        window.scrollTo({
            top:0,
            behavior:"smooth"
        });

    });

}

function editCollection(id){
fetch(BASE_URL+"/config/collection/"+id)
.then(r=>r.json())
.then(function(data){
document.getElementById("selectedConfigId").value=data.id;
document.getElementById("configType").value="COLLECTION";
showHideFields();
document.getElementById("category").value=data.category;
loadCategories(data.category);
loadDpds(data.category);
//loadCities();
setTimeout(function(){
document.getElementById("dpd").value=data.dpd;
document.getElementById("configValue").value=data.slabValue;
document.getElementById("displayOrder").value=data.orderNo;
editMode=true;
window.scrollTo({
top:0,
behavior:"smooth"
});
},300);
});
}

function editCeRange(id){
fetch(BASE_URL+"/config/cerange/"+id)
.then(r=>r.json())
.then(function(data){
document.getElementById("selectedConfigId").value=data.id;
document.getElementById("configType").value="CE_RANGE";
showHideFields();
//document.getElementById("category").value=data.category;
loadCategories(data.category);
loadDpds(data.category);
//loadCities();
setTimeout(function(){
document.getElementById("dpd").value=data.dpd;
document.getElementById("configValue").value=data.rangeValue;
document.getElementById("displayOrder").value=data.orderNo;
editMode=true;
window.scrollTo({
top:0,
behavior:"smooth"
});
},300);
});
}

/*function loadCategories(){

    fetch(BASE_URL+"/config/categories")
    .then(r=>r.json())
    .then(data=>{

        let ddl=document.getElementById("category");

        ddl.innerHTML="<option value=''>Select Category</option>";

        data.forEach(function(category){

            let option=document.createElement("option");

            option.value=category;
            option.text=category;

            ddl.appendChild(option);

        });

    });

}*/

/*function loadCategories(selectedCategory){

    fetch(BASE_URL+"/config/categories")
    .then(r=>r.json())
    .then(data=>{

        let ddl=document.getElementById("category");

        ddl.innerHTML="<option value=''>Select Category</option>";

        data.forEach(function(category){

            let option=document.createElement("option");
            option.value=category;
            option.text=category;

            if(selectedCategory && selectedCategory===category){
                option.selected=true;
            }

            ddl.appendChild(option);

        });

    });

}*/

function loadCategories(selectedCategory){

    fetch(BASE_URL + "/config/categories")
    .then(r => r.json())
    .then(data => {

        let ddl = document.getElementById("category");

        ddl.innerHTML = "<option value=''>Select Category</option>";

        data.forEach(function(category){

            let option = document.createElement("option");
            option.value = category;
            option.text = category;

            ddl.appendChild(option);
        });

        if(selectedCategory){
            ddl.value = selectedCategory;
        }

    });

}

function loadCities(){

    let category=document.getElementById("category").value;

    let ddl=document.getElementById("city");

    ddl.innerHTML="<option value=''>Select City</option>";

    if(category==""){
        return;
    }

    fetch(BASE_URL+"/config/cities?category="+encodeURIComponent(category))
    .then(r=>r.json())
    .then(data=>{

        data.forEach(function(city){

            let option=document.createElement("option");

            option.value=city;
            option.text=city;

            ddl.appendChild(option);

        });

    });

}


function loadDpds(){

    let category=document.getElementById("category").value;

    let ddl=document.getElementById("dpd");

    ddl.innerHTML="<option value=''>Select DPD</option>";

    if(category==""){
        return;
    }

    fetch(BASE_URL+"/config/dpds?category="+encodeURIComponent(category))
    .then(r=>r.json())
    .then(data=>{

        data.forEach(function(dpd){

            let option=document.createElement("option");

            option.value=dpd;
            option.text=dpd;

            ddl.appendChild(option);

        });

    });

}

function loadCollections(){

    let category=document.getElementById("category").value;
    let dpd=document.getElementById("dpd").value;

    let ddl=document.getElementById("collection");

    ddl.innerHTML="<option value=''>Select Collection</option>";

    if(category=="" || dpd==""){
        return;
    }

    fetch(BASE_URL+"/config/collections?category="
        + encodeURIComponent(category)
        + "&dpd="
        + encodeURIComponent(dpd))

    .then(r=>r.json())

    .then(data=>{

        data.forEach(function(c){

            let option=document.createElement("option");

            option.value=c.slabValue;
            option.text=c.slabValue;

            ddl.appendChild(option);

        });

    });

}

function loadCeRanges(){

    let category=document.getElementById("category").value;
    let dpd=document.getElementById("dpd").value;

    let ddl=document.getElementById("ceRange");

    ddl.innerHTML="<option value=''>Select CE Range</option>";

    if(category=="" || dpd==""){
        return;
    }

    fetch(BASE_URL+"/config/ceranges?category="
        + encodeURIComponent(category)
        + "&dpd="
        + encodeURIComponent(dpd))

    .then(r=>r.json())

    .then(data=>{

        data.forEach(function(c){

            let option=document.createElement("option");

            option.value=c.rangeValue;
            option.text=c.rangeValue;

            ddl.appendChild(option);

        });

    });

}



/* ===================================
EDIT
=================================== */

function editConfig(id){

fetch(
    BASE_URL +
    "/config/get/" +
    id)

.then(r => r.json())

.then(data => {
    document.getElementById("selectedConfigId").value = data.configId;
    document.getElementById("configType").value = data.configType || "";
    showHideFields();
    changeLabels();
    document.getElementById("parentKey").value = data.parentKey || "";
    document.getElementById("configKey").value = data.configKey || "";
    document.getElementById("configValue").value = data.configValue || "";
    document.getElementById("displayOrder").value = data.displayOrder || "";
    switch(data.configType){
        case "CATEGORY":
            document.getElementById("categoryText").value =
                data.category || data.configValue || "";
            break;
        case "CITY":
            document.getElementById("category").value =
                data.category || "";
            loadCities();
            setTimeout(function(){
                document.getElementById("cityText").value =
                    data.city || data.configValue || "";
            },200);
            break;
        case "DPD":
            document.getElementById("category").value =
                data.category || "";
            loadCities();
            setTimeout(function(){
                document.getElementById("city").value =
                    data.city || "";
                loadDpds();
                setTimeout(function(){
                    document.getElementById("dpdText").value =
                        data.dpd || data.configValue || "";
                },200);
            },200);
            break;
        case "COLLECTION":
        case "CE_RANGE":
            document.getElementById("category").value =
                data.category || "";
            loadCities();
            setTimeout(function(){
                document.getElementById("city").value =
                    data.city || "";
                loadDpds();
                setTimeout(function(){
                    document.getElementById("dpd").value =
                        data.dpd || "";
                    document.getElementById("configValue").value =
                        data.configValue || "";
                },200);
            },200);
            break;
        case "FIELD":
        case "BUTTON":
        case "TITLE":
            document.getElementById("configKey").value =
                data.configKey || "";
            document.getElementById("configValue").value =
                data.configValue || "";
            break;
    }
    editMode = true;
    window.scrollTo({
        top:0,
        behavior:"smooth"
    });
});

}

/* ===================================
SAVE / UPDATE
=================================== */
function saveData(){
	
    let type = document.getElementById("configType").value;

    let category = "";
    let city = "";
    let dpd = "";

    let configValue = document.getElementById("configValue").value.trim();
    let displayOrder = document.getElementById("displayOrder").value;
            
            switch(type){

case "CATEGORY":

    category = document.getElementById("categoryText").value.trim();

    configValue = category;

    break;

case "CITY":

    category = document.getElementById("category").value;

    city = document.getElementById("cityText").value.trim();

    configValue = city;

    break;

case "DPD":

    category = document.getElementById("category").value;

    city = document.getElementById("city").value;

    dpd = document.getElementById("dpdText").value.trim();

    configValue = dpd;

    break;

        case "COLLECTION":
        case "CE_RANGE":
            category = document.getElementById("category").value;
            city = document.getElementById("city").value;
            dpd = document.getElementById("dpd").value;
            break;

        case "FIELD":
        case "BUTTON":
        case "TITLE":
            break;
    }

    // ---------------- Validation ----------------

    if(type===""){
        alert("Please Select Config Type");
        return;
    }

   if(type=="CATEGORY"){

    if(categoryText==""){
        alert("Please Enter Category");
        return;
    }

}
else if(type=="CITY"){

    if(category==""){
        alert("Please Select Category");
        return;
    }

    if(cityText==""){
        alert("Please Enter City");
        return;
    }

}
else if(type=="DPD"){

    if(category==""){
        alert("Please Select Category");
        return;
    }

    if(dpdText==""){
        alert("Please Enter DPD");
        return;
    }

}
else if(type=="COLLECTION"){

    if(category==""){
        alert("Please Select Category");
        return;
    }

    if(dpd==""){
        alert("Please Select DPD");
        return;
    }

    if(configValue==""){
        alert("Please Enter Collection Slab");
        return;
    }

}
else if(type=="CE_RANGE"){

    if(category==""){
        alert("Please Select Category");
        return;
    }

    if(dpd==""){
        alert("Please Select DPD");
        return;
    }

    if(configValue==""){
        alert("Please Enter CE Range");
        return;
    }

}

    let configKey = "";
   
    if(type==="CATEGORY"){
    configKey = category;
}
if(type==="CITY"){
    configKey = city;
}
if(type==="DPD"){
    configKey = dpd;
}

    if(type==="FIELD"){

        configKey = document.getElementById("configKey").value.trim();

        if(configKey===""){
            alert("Please Enter Config Key");
            return;
        }

        if(configValue===""){
            alert("Please Enter Field Label");
            return;
        }

    }

    if(type==="BUTTON"){

        configKey = document.getElementById("configKey").value.trim();

        if(configKey===""){
            alert("Please Enter Button Key");
            return;
        }

        if(configValue===""){
            alert("Please Enter Button Name");
            return;
        }

    }

    if(type==="TITLE"){

        configKey = document.getElementById("configKey").value.trim();

        if(configKey===""){
            alert("Please Enter Screen Key");
            return;
        }

        if(configValue===""){
            alert("Please Enter Screen Title");
            return;
        }

    }

    /*if(displayOrder===""){
        alert("Please Enter Display Order");
        return;
    }*/
    if(type !== "CAPPING"){
    if(displayOrder==""){
        alert("Please enter Display Order");
        return;
    }
}

if(type=="CAPPING"){

    if(document.getElementById("configValue").value.trim()==""){
        alert("Please Enter New Value");
        return;
    }

}

    // Auto generated key for Business Types

    if(type==="COLLECTION" || type==="CE_RANGE"){

        configKey = document.getElementById("configKey").value;

    }

let payload = {

    configId: document.getElementById("selectedConfigId").value,

    configType: type,

    category: category,

    city: city,

    dpd: dpd,

    parentKey: document.getElementById("parentKey").value,

    configKey: configKey,

    oldValue: document.getElementById("oldValue").value,

    configValue: configValue,

    displayOrder: displayOrder

};

   /* let payload = {

        configId : document.getElementById("selectedConfigId").value,

        configType : type,

        category : category,

        city : city,

        dpd : dpd,

        parentKey : document.getElementById("parentKey").value,

        configKey : configKey,

        configValue : configValue,

        displayOrder : displayOrder
    };*/

    /*let api = editMode
            ? "/config/update?user=maker1"
            : "/config/save?user=maker1";*/
            
    let api = editMode
        ? "/config/update?user=" + LOGIN_USER
        : "/config/save?user=" + LOGIN_USER;        

    fetch(BASE_URL + api,{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify(payload)

    })
    .then(r=>r.text())
    .then(msg => {

    alert(msg);

    resetTable();

    showHideFields();

    loadConfigs();
});

}
/* ===================================
ACTIVATE
=================================== */

function activateConfig(type,id){

fetch(
BASE_URL+
/*"/config/activate/"+type+"/"+id+"?user=maker1",*/

"/config/activate/"+type+"/"+id+"?user="+LOGIN_USER,

{
method:"POST"
})
.then(r=>r.text())
.then(function(msg){

alert(msg);

loadConfigs();

});

}

/* ===================================
DEACTIVATE
=================================== */

function deactivateConfig(type,id){

fetch(
BASE_URL+

/*"/config/deactivate/"+type+"/"+id+"?user=maker1",
*/

"/config/deactivate/"+type+"/"+id+"?user="+LOGIN_USER,

{
method:"POST"
})
.then(r=>r.text())
.then(function(msg){

alert(msg);

loadConfigs();

});

}

/* ===================================
RESET
=================================== */

function resetTable(){

document.getElementById(
"selectedConfigId").value="";

document.getElementById(
"configType").value="";

document.getElementById(
"parentKey").value="";

document.getElementById(
"configKey").value="";

document.getElementById(
"configValue").value="";

document.getElementById(
"displayOrder").value="";

document.getElementById("category").value = "";
document.getElementById("city").value = "";
document.getElementById("dpd").value = "";
document.getElementById("categoryText").value = "";
document.getElementById("cityText").value = "";
document.getElementById("dpdText").value = "";

editMode = false;
showHideFields();

}

/*====================== 
*/

function showHideFields() {
    const type = document.getElementById("configType").value;
    console.log("TYPE = ", type);
    console.log(document.getElementById("categoryText"));
    const categoryDiv = document.getElementById("categoryDiv");
    const cityDiv = document.getElementById("cityDiv");
    const dpdDiv = document.getElementById("dpdDiv");
    const parentKeyDiv = document.getElementById("parentKeyDiv");
    const configKeyDiv = document.getElementById("configKeyDiv");
    const configValueDiv = document.getElementById("configValueDiv");

    const parentKey = document.getElementById("parentKey");
    const configKey = document.getElementById("configKey");
    const oldValueDiv = document.getElementById("oldValueDiv");
    const displayOrderDiv = document.getElementById("displayOrderDiv");

    // Hide everything
document.getElementById("category").style.display = "none";
document.getElementById("categoryText").style.display = "none";
document.getElementById("city").style.display = "none";
document.getElementById("cityText").style.display = "none";
document.getElementById("dpd").style.display = "none";
document.getElementById("dpdText").style.display = "none";
    categoryDiv.style.display = "none";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    parentKeyDiv.style.display = "none";
    configKeyDiv.style.display = "none";
    configValueDiv.style.display = "none";
    parentKey.value = "";
    parentKey.disabled = true;
    configKey.value = "";
    configKey.readOnly = false;
    
    oldValueDiv.style.display = "none";
document.getElementById("oldValue").value = "";
document.getElementById("configValue").value = "";
displayOrderDiv.style.display = "block";
currentValueDiv.style.display = "none";

  if(type === ""){
	  console.log("Inside Empty Type");
    categoryDiv.style.display = "none";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    parentKeyDiv.style.display = "none";
    configKeyDiv.style.display = "none";
    configValueDiv.style.display = "none";
    return;
  }
  
  // Always restore hidden sections first
document.getElementById("configTable").closest(".table-responsive").style.display = "block";
document.querySelectorAll(".filter-row")[1].style.display = "flex";

    switch(type){

    case "CATEGORY":
    categoryDiv.style.display = "block";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    document.getElementById("category").style.display="none";
    document.getElementById("categoryText").style.display="block";
    document.getElementById("city").style.display="none";
    document.getElementById("cityText").style.display="none";
    document.getElementById("dpd").style.display="none";
    document.getElementById("dpdText").style.display="none";
    parentKeyDiv.style.display = "block";
    parentKey.value = "CATEGORY";
    configKeyDiv.style.display="none";
    configValueDiv.style.display="none";
    break;

    case "CITY":
    categoryDiv.style.display="block";
    cityDiv.style.display="block";
    dpdDiv.style.display="none";
    document.getElementById("category").style.display="block";
    document.getElementById("categoryText").style.display="none";
    document.getElementById("city").style.display="none";
    document.getElementById("cityText").style.display="block";
    document.getElementById("dpd").style.display="none";
    document.getElementById("dpdText").style.display="none";
    parentKeyDiv.style.display="block";
    parentKey.value="CATEGORY";
    configKeyDiv.style.display="none";
    configValueDiv.style.display="none";
    loadCategories();   // <<< ADD THIS LINE
    break;

    case "DPD":
    categoryDiv.style.display="block";
    /*cityDiv.style.display="block";*/
    cityDiv.style.display="none";
    dpdDiv.style.display="block";
    document.getElementById("category").style.display="block";
    document.getElementById("categoryText").style.display="none";
    /*document.getElementById("city").style.display="block";
    document.getElementById("cityText").style.display="none";*/
    document.getElementById("city").style.display="none";
    document.getElementById("cityText").style.display="none";
    document.getElementById("dpd").style.display="none";
    document.getElementById("dpdText").style.display="block";
    parentKeyDiv.style.display = "block";
    parentKey.value = "CITY";
    configKeyDiv.style.display="none";
    configValueDiv.style.display="none";
   /* loadCategories();*/
    if(!editMode){
        loadCategories();
    }
    break;

    case "COLLECTION":
    categoryDiv.style.display = "block";
   /* cityDiv.style.display = "block";*/
   cityDiv.style.display="none";
    dpdDiv.style.display = "block";
    document.getElementById("category").style.display = "block";
    document.getElementById("categoryText").style.display = "none";
    /*document.getElementById("city").style.display = "block";
    document.getElementById("cityText").style.display = "none";*/
    document.getElementById("city").style.display="none";
    document.getElementById("cityText").style.display="none";
    document.getElementById("dpd").style.display = "block";
    document.getElementById("dpdText").style.display = "none";
    parentKeyDiv.style.display = "block";
    parentKey.value = "DPD";
    configKeyDiv.style.display = "none";
    configValueDiv.style.display = "block";
    document.getElementById("configValueLabel").innerHTML = "Collection Slab";
    generateConfigKey();
    /*loadCategories();*/
     if(!editMode){
        loadCategories();
    }
    break;

    case "CE_RANGE":
    categoryDiv.style.display = "block";
    /*cityDiv.style.display = "block";*/
    cityDiv.style.display="none";
    dpdDiv.style.display = "block";
    document.getElementById("category").style.display = "block";
    document.getElementById("categoryText").style.display = "none";
    /*document.getElementById("city").style.display = "block";
    document.getElementById("cityText").style.display = "none";*/
    document.getElementById("city").style.display="none";
    document.getElementById("cityText").style.display="none";
    document.getElementById("dpd").style.display = "block";
    document.getElementById("dpdText").style.display = "none";
    parentKeyDiv.style.display = "block";
    parentKey.value = "DPD";
    configKeyDiv.style.display = "none";
    configValueDiv.style.display = "block";
    document.getElementById("configValueLabel").innerHTML = "CE Range";
    generateConfigKey();
    /*loadCategories();*/
     if(!editMode){
        loadCategories();
    }
    break;

    case "FIELD":
    categoryDiv.style.display = "none";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    parentKeyDiv.style.display = "block";
    configKeyDiv.style.display = "block";
    configValueDiv.style.display = "block";
    document.getElementById("configValueLabel").innerHTML = "Field Label";
    parentKeyDiv.style.display = "block";
    parentKey.disabled = false;
    parentKey.value = "";
    configKey.readOnly = false;
    break;
    
    case "BUTTON":
    categoryDiv.style.display = "none";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    configKeyDiv.style.display = "block";
    configValueDiv.style.display = "block";
    document.getElementById("configValueLabel").innerHTML = "Button Name";
    parentKeyDiv.style.display = "block";
    parentKey.value = "UI";
    parentKey.disabled = true;
    configKey.readOnly = false;
    break;

    case "TITLE":
    categoryDiv.style.display = "none";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    parentKeyDiv.style.display = "none";
    configKeyDiv.style.display = "block";
    configValueDiv.style.display = "block";
    document.getElementById("configValueLabel").innerHTML = "Screen Title";
    parentKeyDiv.style.display = "block";
    parentKey.value = "UI";
    parentKey.disabled = true;
    configKey.readOnly = false;
    break;
    
    case "CAPPING":
		    
    // Hide existing table
document.getElementById("configTable").closest(".table-responsive").style.display = "none";
// Hide the Status/Search filter row
document.querySelectorAll(".filter-row")[1].style.display = "none";
		
    categoryDiv.style.display = "none";
    cityDiv.style.display = "none";
    dpdDiv.style.display = "none";
    parentKeyDiv.style.display = "none";
    configKeyDiv.style.display = "none";
    displayOrderDiv.style.display = "none";
    oldValueDiv.style.display = "block";
    configValueDiv.style.display = "block";
    document.getElementById("configValueLabel").innerHTML = "New Value";
    document.getElementById("oldValue").readOnly = true;
    currentValueDiv.style.display = "block";
    loadCapping();
    break;
    
    }
        changeLabels();
        loadConfigs();

}

function changeLabels(){

    const type=document.getElementById("configType").value;

    const lbl=document.getElementById("configValueLabel");

    switch(type){

        case "CATEGORY":
            lbl.innerHTML="Category";
            break;

        case "CITY":
            lbl.innerHTML="City";
            break;

        case "DPD":
            lbl.innerHTML="DPD";
            break;

        case "COLLECTION":
            lbl.innerHTML="Collection Slab";
            break;

        case "CE_RANGE":
            lbl.innerHTML="CE Range";
            break;

        case "FIELD":
            lbl.innerHTML="Field Label";
            break;

        case "BUTTON":
            lbl.innerHTML="Button Name";
            break;

        case "TITLE":
            lbl.innerHTML="Screen Title";
            break;
            
        case "CAPPING":
           lbl.innerHTML = "New Value";
           break;

        default:
            lbl.innerHTML="Config Value";
    }
}


/*function loadCapping(){
    fetch(BASE_URL+"/config/capping")
    .then(r=>r.json())
    .then(function(data){
        if(data){
            document.getElementById("oldValue").value =
                    data.configValue;
        }else{
            document.getElementById("oldValue").value="";
        }
        document.getElementById("configValue").value="";
    })
    .catch(function(){
        document.getElementById("oldValue").value="";
        document.getElementById("configValue").value="";
    });
}*/

function loadCapping(){
    fetch(BASE_URL + "/config/capping")
    .then(r => r.json())
    .then(function(data){
        if(data){
            document.getElementById("oldValue").value =
                    data.oldValue || "";
            document.getElementById("currentValue").value =
                    data.configValue || "";
        }else{
            document.getElementById("oldValue").value = "";
            document.getElementById("currentValue").value = "";
        }
        document.getElementById("configValue").value = "";
    });
}

function generateConfigKey() {

    let type = document.getElementById("configType").value;
    let category = document.getElementById("category").value;
    let city = document.getElementById("city").value;
    let dpd = document.getElementById("dpd").value;
    let value = document.getElementById("configValue").value;

    let key = "";

    switch (type) {

        case "CATEGORY":
            // No Config Key
            key = "";
            break;

        case "CITY":
            // No Config Key
            key = "";
            break;

        case "DPD":
            // No Config Key
            key = "";
            break;

        case "COLLECTION":
            if (category && dpd && value) {
                key = category + "_" + dpd + "_" + value;
            }
            break;

        case "CE_RANGE":
            if (category && dpd && value) {
                key = category + "_" + dpd + "_" + value;
            }
            break;

        case "FIELD":
        case "BUTTON":
        case "TITLE":
            // User enters Config Key manually
            return;

        default:
            key = "";
    }
    document.getElementById("configKey").value = key;
}


/*================================*/

document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("category").addEventListener("change", function () {

        let type = document.getElementById("configType").value;

        if(type==="CITY"){
            loadCities();
        }else if(type==="DPD" || type==="COLLECTION" || type==="CE_RANGE"){
            loadCities();
            loadDpds();
        }

    });

    document.getElementById("dpd").addEventListener("change", function () {

        let type = document.getElementById("configType").value;

        if(type==="COLLECTION"){
            loadCollections();
        }else if(type==="CE_RANGE"){
            loadCeRanges();
        }

    });

});

/* ===================================
HISTORY
=================================== */

function viewMakerHistory(){

let id =

document.getElementById(
"selectedConfigId").value;

if(!id){

    alert(
    "Please Select Record");

    return;
}

window.location.href =
CONTEXT_PATH +
"/mainPage/load?master=CONFIGCOMPAREMAKER&id="+id+"&mode=maker";
}


/*window.location.href =
CONTEXT_PATH +
"/mainPage/load?master=CONFIGCOMPAREMAKER&id="+id;
}*/

/*window.location.href =
    CONTEXT_PATH +
    "/mainPage/load?master=CONFIGCOMPARE"
    + "&id=" + id
    + "&mode=maker";
}*/
