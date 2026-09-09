let personalLoneData = {
    plData: [
        { bucket: "A", min: "", max: "49,99,999", slab: "2.07%", inputName: "plSlabA" },
        { bucket: "B", min: "50,00,000", max: "74,99,999", slab: "2.66%", inputName: "plSlabB" },
        { bucket: "C", min: "75,00,000", max: "1,49,99,999", slab: "2.95%", inputName: "plSlabC" },
        { bucket: "D", min: "1,50,00,000", max: "1,99,99,999", slab: "3.25%", inputName: "plSlabD" },
        { bucket: "E", min: "2,00,00,000", max: "9,99,99,999", slab: "3.54%", inputName: "plSlabE" },
        { bucket: "F", min: "10,00,00,000", max: "39,99,99,999", slab: "3.60%", inputName: "plSlabF" },
        { bucket: "G", min: "40,00,00,000", max: "0", slab: "3.66%", inputName: "plSlabG" }
    ],
    bilData: [
        { bucket: "A", min: "", max: "49,99,999", slab: "2.36%", inputName: "bilSlabA" },
        { bucket: "B", min: "50,00,000", max: "74,99,999", slab: "2.95%", inputName: "bilSlabB" },
        { bucket: "C", min: "75,00,000", max: "99,99,999", slab: "3.25%", inputName: "bilSlabC" },
        { bucket: "D", min: "1,00,00,000", max: "1,49,99,999", slab: "3.54%", inputName: "bilSlabD" },
        { bucket: "E", min: "1,50,00,000", max: "2,49,99,999", slab: "3.84%", inputName: "bilSlabE" },
        { bucket: "F", min: "2,50,00,000", max: "5,99,99,999", slab: "3.84%", inputName: "bilSlabF" },
        { bucket: "G", min: "6,00,00,000", max: "15,99,99,999", slab: "4.01%", inputName: "bilSlabG" },
        { bucket: "H", min: "16,00,00,000", max: "0", slab: "4.13%", inputName: "bilSlabH" }
    ],
	
	aggregatorData:[
		{ bucket: 'A', min: '0', max: '19.99', slab: '1.80%', inputName: 'aggSlabA' },
        { bucket: 'B', min: '20.00', max: '39.99', slab: '2.00%', inputName: 'aggSlabB' },
        { bucket: 'C', min: '40.00', max: '59.99', slab: '2.10%', inputName: 'aggSlabC' },
        { bucket: 'D', min: '60.00', max: '99.99', slab: '2.20%', inputName: 'aggSlabD' },
        { bucket: 'E', min: '100.00', max: '+', slab: '2.30%', inputName: 'aggSlabE' }
	],
	paisaBazar:[
		{ bucket: 'A', min: '0', max: '19.99', slab: '1.80%', inputName: 'aggSlabA' },
        { bucket: 'B', min: '20.00', max: '39.99', slab: '2.00%', inputName: 'aggSlabB' },
        { bucket: 'C', min: '40.00', max: '59.99', slab: '2.10%', inputName: 'aggSlabC' },
        { bucket: 'D', min: '60.00', max: '99.99', slab: '2.20%', inputName: 'aggSlabD' },
        { bucket: 'E', min: '100.00', max: '+', slab: '2.30%', inputName: 'aggSlabE' }
	]
	
};


let educationLoanData = {
    elPrimeData: [
        { bucket: "A", min: "", max: "99,99,999", slab: "1.18%", inputName: "elPrimeSlabA" },
        { bucket: "B", min: "1,00,00,000", max: "1,49,99,999", slab: "1.36%", inputName: "elPrimeSlabB" },
        { bucket: "C", min: "1,50,00,000", max: "0", slab: "1.48%", inputName: "elPrimeSlabC", description: "Above 1.5 Cr" }
    ],
    elGenericData: [
        { bucket: "A", min: "", max: "29,99,999", slab: "0.35%", inputName: "elGenericSlabA" },
        { bucket: "B", min: "30,00,000", max: "49,99,999", slab: "0.59%", inputName: "elGenericSlabB" },
        { bucket: "C", min: "50,00,000", max: "74,99,999", slab: "0.77%", inputName: "elGenericSlabC" },
        { bucket: "D", min: "75,00,000", max: "1,50,00,000", slab: "0.89%", inputName: "elGenericSlabD" },
        { bucket: "E", min: "1,50,00,000", max: "0", slab: "1.18%", inputName: "elGenericSlabE", description: "ABOVE 1.5 CR" }
    ],
    elFocusData: [
        { bucket: "A", min: "", max: "", slab: "1.18%", inputName: "elFocusSlabA", description: "Flat 1% payout on Disbursed Amount" }
    ],
    elSupremeData: [
        { bucket: "A", min: "", max: "74,99,999", slab: "1.50%", inputName: "elSupremeSlabA" },
        { bucket: "B", min: "75,00,000", max: "0", slab: "1.75%", inputName: "elSupremeSlabB", description: "ABOVE 75 CR (Booster of 0.25%)" }
    ],
    elFlatData: [
        { bucket: "A", min: "", max: "", slab: "0.59%", inputName: "elFlatSlabA" },
        { bucket: "B", min: "", max: "", slab: "0.94%", inputName: "elFlatSlabB"},
        { bucket: "C", min: "", max: "", slab: "1.18%", inputName: "elFlatSlabC" },
        { bucket: "D", min: "", max: "", slab: "1.36%", inputName: "elFlatSlabD" },
        { bucket: "E", min: "", max: "", slab: "1.48%", inputName: "elFlatSlabE"}
    ],
    elBoosterData: [
        { bucket: "A", min: "1,50,00,000", max: "0", slab: "0.30%", inputName: "elBoosterSlabA", description: "Above 1.5 CR" },
        { bucket: "B", min: "3,00,00,000", max: "0", slab: "0.30%", inputName: "elBoosterSlabB", description: "Above 3 CR" },
        { bucket: "C", min: "", max: "", slab: "0.30%", inputName: "elBoosterSlabC" }
    ]
};
 


// Global array to store newly added rows across user actions
let newlyAddedRows = [];
 


function renderPayoutTables(isReadOnly) {
    populateSection("plTableBody", personalLoneData.plData, isReadOnly, "pl");
    populateSection("bilTableBody", personalLoneData.bilData, isReadOnly, "bil");
    populateSection("paisBazarBody", personalLoneData.paisaBazar, isReadOnly, "pib");
	populateSection("payoutSTRBody", personalLoneData.aggregatorData, isReadOnly, "agg");
    educationPopulateSection("elPrimeTableBody", educationLoanData.elPrimeData, isReadOnly, "elPrime");
	educationPopulateSection("eLGenericTableBody", educationLoanData.elGenericData, isReadOnly, "elGeneric");
	educationPopulateSection("elFocusTableBody", educationLoanData.elFocusData, isReadOnly, "elFocus");
    educationPopulateSection("elSupremeTableBody", educationLoanData.elSupremeData, isReadOnly, "elSupreme");
	educationPopulateSection("elFlatTableBody", educationLoanData.elFlatData, isReadOnly, "elFlat");
	educationPopulateSection("elBoosterTableBody", educationLoanData.elBoosterData, isReadOnly, "elBooster");
}


function formatRange(minVal, maxVal) {
    if (!maxVal || maxVal === '+') {
        return `${minVal} +`;
    }
    if (!minVal) {
        return maxVal;
    }
    return `${minVal} - ${maxVal}`;
}




function educationPopulateSection(containerId, dataArray, isReadOnly, prefix) {
  const tbody = document.getElementById(containerId);
  if (!tbody || !dataArray) return;
 
  // Check if this dataset has any range/description data at all
  const hasRangeData = dataArray.some(item => 
    Boolean((item.description && item.description.trim()) || item.min || item.max)
  );
 
  let html = "";
 
  dataArray.forEach((item, index) => {
    const inputName = item.inputName || `${prefix}Slab${item.bucket || index}`;
    const slabValue = item.slab || "";
 
    html += '<tr>';
 
    // 1. Bucket Column (Column 1)
    html += `<td style="text-align: center; font-weight: bold;">${item.bucket || ''}</td>`;
 
    // 2. Disbursement Amount Column (Only render if dataset has min/max/description)
    if (hasRangeData) {
      const isMergedRow = item.description || item.max === "0" || item.max === "+";
 
      if (isMergedRow) {
        let rangeText = item.description || "";
        if (!rangeText) {
          if (item.min) rangeText = `Above ${item.min}`;
          else if (item.max) rangeText = item.max;
        }
        html += `<td colspan="2" style="text-align: center;">${rangeText}</td>`;
      } else {
        const minDisplay = item.min ? item.min : "-";
        const maxDisplay = item.max ? item.max : "";
        html += `<td style="text-align: center;">${minDisplay}</td>`;
        html += `<td style="text-align: center;">${maxDisplay}</td>`;
      }
    }
 
    // 3. % Payout Input Field Column (Column 2 when range is hidden)
    html += `
      <td style="text-align: center; padding: 4px;">
        <input type="text"
          name="${inputName}"
          id="${inputName}"
          class="table input"
          value="${slabValue}"
          placeholder="0.00%"
          maxlength="4"
          ${isReadOnly ? 'readonly' : ''}
          onblur="updateNewlyAddedRowPayout(this)"
          style="width: 85%; text-align: center; border: 1px solid #ccc; border-radius: 3px;"
          oninput="this.value = this.value.replace(/[^0-9.]/g, '')" />
      </td>
    `;
 
    html += '</tr>';
  });
 
  tbody.innerHTML = html;
}
 

function populateSection(containerId, dataArray, isReadOnly, prefix) {
    const tbody = document.getElementById(containerId);
    if (!tbody) return;
	//dataArray = newlyAddedRows;
	
	
    const pcrCategoryEl = document.getElementById("category2");
    const pcrCategoryVal = pcrCategoryEl ? pcrCategoryEl.value.trim() : "";
    const isAggregator = (pcrCategoryVal === "Aggregator");
 
    tbody.innerHTML = dataArray.map((item, index) => {
        // Safe value fallbacks
        const minVal = (item.min !== undefined && item.min !== null) ? item.min : "";
        const maxVal = (item.max !== undefined && item.max !== null) ? item.max : "";
        const slabVal = (item.slab !== undefined && item.slab !== null) ? item.slab : "";
        const bucketVal = item.bucket || "";
        const inputName = item.inputName || `${prefix}_${index}`;
 
        // 1. Minimum Cell HTML
        let minCellContent = "";
        if (isReadOnly) {
            minCellContent = minVal !== "" ? minVal : "-";
        } else {
            minCellContent = `<input type="text"
                class="range-input table-input"
                value="${minVal}"
                name="${prefix}Min_${bucketVal}"
                style="width:100%; text-align:center;"
                inputmode="numeric"
                maxlength="12"
                oninput="this.value = this.value.replace(/[^0-9]/g, '')" disabled />`;
        }
 
        // 2. Maximum Cell HTML
        let maxCellContent = "";
        if (isReadOnly) {
            maxCellContent = item.max === '+' ? '+' : (maxVal !== "" ? maxVal : "");
        } else {
            maxCellContent = `<input type="text"
                class="range-input table-input"
                value="${maxVal}"
                name="${prefix}Max_${bucketVal}"
                style="width:100%; text-align:center;"
                inputmode="numeric"
                maxlength="12"
                oninput="this.value = this.value.replace(/[^0-9]/g, '')" disabled />`;
        }
 
      	const isAggregator = (pcrCategoryVal === "Aggregator");
 
// Formatted display value for range cell
const displayRangeText = formatRange(minVal, maxVal);
 
// Construct rangeCell
const rangeCell = isAggregator
    ? `<td>${displayRangeText}</td>`
    : `<td>${minCellContent}</td><td>${maxCellContent}</td>`;
 
        // Return standard <tr> row
        return `
            <tr>
                ${!isAggregator ? `<td>${bucketVal}</td>` : ''}
                ${rangeCell}
                <td>
                    <input type="text"
                        name="${inputName}"
                        class="table-input"
                        value="${slabVal}"
                        inputmode="decimal"
                        maxlength="4"
                        style="width:100%; text-align:center;"
                        oninput="this.value = this.value.replace(/[^0-9.]/g, '')"
		    			onblur="updateNewlyAddedRowPayout(this)"
                        ${isReadOnly ? 'readonly' : ''} />
                </td>
            </tr>
        `;
    }).join('');
}
 


function populatePerRowSelect(dataArray) {
    const newPerRowSelect = document.getElementById("newPerRowSelect");
    if (!newPerRowSelect) return;
 
    // Reset dropdown header
    newPerRowSelect.innerHTML = '<option value="">Select Range</option>';
 
    // Guard against undefined/null/non-array inputs
    if (!dataArray || !Array.isArray(dataArray)) {
        console.warn("populatePerRowSelect expected an array but received:", dataArray);
        return;
    }
 
    dataArray.forEach((item) => {
        let displayLabel = "";
 
        // Format rules based on min & max values
        if (item.max === '+' || item.max === '0') {
            displayLabel = `${item.min} +`;
        } else if (!item.min || item.min === "") {
            displayLabel = `0 - ${item.max}`;
        } else {
            displayLabel = `${item.min} - ${item.max}`;
        }
 
        // Pass formatted string into <option>
        const option = document.createElement("option");
        option.value = JSON.stringify({bucket: item.bucket,  min: item.min, max: item.max });
        option.textContent = displayLabel;
 
        newPerRowSelect.appendChild(option);
    });
}
 

// Function: Add Row dynamically
function addDynamicRow(sectionType, isReadOnly) {
    const category2El = document.getElementById("category2");
    const categoryList = category2El ? category2El.value.trim() : "";
    let cycleFromDate = document.getElementById("fromDate2")?.value || "";
    let cycleToDate   = document.getElementById("toDate2")?.value || "";
 
    let dataArray, containerId, prefix;
 
    // --- Personal Loan Categories ---
    if (categoryList === 'PL & Doctor Loans (Salaried & SE across All Location)') {
        dataArray = personalLoneData.plData;
        containerId = "plTableBody";
        prefix = "pl";
    } else if (categoryList === 'BIL Except Doctor Loans') {
        dataArray = personalLoneData.bilData;
        containerId = "bilTableBody";
        prefix = "bil";
    } else if (categoryList === 'PaisaBazaar') {
        dataArray = personalLoneData.paisaBazar;
        containerId = "paisBazarBody";
        prefix = "pib";
    } else if (categoryList === 'Aggregator') {
        dataArray = personalLoneData.aggregatorData;
        containerId = "payoutSTRBody";
        prefix = "agg";
    } 
    // --- Education Loan Categories ---
    else if (categoryList === 'EL Prime') {
        dataArray = educationLoanData.elPrimeData;
        containerId = "elPrimeTableBody";
        prefix = "elPrime";
    } else if (categoryList === 'EL Generic') {
        dataArray = educationLoanData.elGenericData;
        containerId = "eLGenericTableBody";
        prefix = "elGeneric";
    } else if (categoryList === 'EL Supreme') {
        dataArray = educationLoanData.elSupremeData;
        containerId = "elSupremeTableBody";
        prefix = "elSupreme";
    } else if (categoryList === 'EL Flat') {
        dataArray = educationLoanData.elFlatData;
        containerId = "elFlatTableBody";
        prefix = "elFlat";
    } else if (categoryList === 'EL Booster') {
        dataArray = educationLoanData.elBoosterData;
        containerId = "elBoosterTableBody";
        prefix = "elBooster";
    } else if (categoryList === 'EL Focus') {
        dataArray = educationLoanData.elFocusData;
        containerId = "elFocusTableBody";
        prefix = "elFocus";
    } 
    else {
        console.warn("Category not recognized:", categoryList);
        return;
    }
 
    if (!dataArray) return;
 
    // 1. Read selected min and max from #newPerRowSelect
    const selectEl = document.getElementById("newPerRowSelect");
    let selectedMin = "";
    let selectedMax = "";
	let bucketValue = "";	
 
    if (selectEl && selectEl.value) {
        try {
            const rangeObj = JSON.parse(selectEl.value);
            selectedMin = rangeObj.min || "";
            selectedMax = rangeObj.max || "";
			bucketValue = rangeObj.bucket || "";
        } catch (e) {
            console.error("Invalid range selected:", e);
            alert("Please select a valid range from the dropdown.");
            return;
        }
    } else {
        alert("Please select a range first!");
        return;
    }
 
    // 2. Auto-generate next bucket letter (A, B, C... Z)
 /*   const lastBucketChar = (dataArray.length > 0)
        ? dataArray[dataArray.length - 1].bucket
        : '@';*/
    const nextBucket = dataArray.length + 1;
 
    // 3. Push object with the selected MIN and MAX into dataArray
    dataArray.push({
        bucket: bucketValue,
        min: selectedMin,
        max: selectedMax,
        slab: "0.00%",
        inputName: `${prefix}Slab${bucketValue}`
    });
 
    // 4. Re-populate table with updated data
	let subProduct = document.getElementById("subProduct").value;
 
// Push ONLY to global variable for saving later
	newlyAddedRows.push({
    category: categoryList,
    payoutType: bucketValue,
    min: selectedMin ? selectedMin.toString() : "",
    max: selectedMax ? selectedMax.toString() : "",
    payout: "0.00%",
    status: "Pending",
    cycleFrom: cycleFromDate,
    cycleTo: cycleToDate  
});
	
	if(subProduct.toLowerCase() === "personal loan"){
	    populateSection(containerId, dataArray, isReadOnly, prefix);	
	}else{
		educationPopulateSection(containerId, dataArray, isReadOnly, prefix);	
	}

}
 
 


function loadPersonalLoneTable() {
  try {
    renderPayoutTables(false);
    const productEl = document.getElementById("product");
    const subProductEl = document.getElementById("subProduct");
    const personalloneCategoryEl = document.getElementById("personallonecategory");
    const pcrCategoryEl = document.getElementById("category2");
 	const newPerRowSelect = document.getElementById("newPerRowSelect");

    const plDoctorSection = document.getElementById("personal-Lone-id");
    const bilExceptDoctorSection = document.getElementById("bilExceptDoctorSection");
    
    const paisaBazarSection = document.getElementById("paisaBazar");
    const aggregatorSection = document.getElementById("aggregatorId");
	
//  Education Loan Sections (Added missing categories from Image 3 UI)
    const elPrimeSection = document.getElementById("elPrime");
    const elGenericSection = document.getElementById("eLGeneric");
    const elSupremeSection = document.getElementById("elSupreme");
    const elFlatSection = document.getElementById("elFlat");
	const elFocusSection = document.getElementById("elFocus");
    const elBoosterSection = document.getElementById("elBooster");
    let submitBtn = document.getElementById("submitBtn");

	
 
        // Array of all sections to easily hide them during reset
        const allSections = [
            plDoctorSection,
            bilExceptDoctorSection,
            paisaBazarSection,
            aggregatorSection,
            elPrimeSection,
            elGenericSection,
            elSupremeSection,
            elFlatSection,
            elBoosterSection,
			elFocusSection
        ];
 
    const hideAllSections = () => {
      allSections.forEach(sec => {
        if (sec) sec.style.display = "none";
      });
    };
 	
    const subProduct = subProductEl ? subProductEl.value.trim() : "";
    const perCategory = pcrCategoryEl ? pcrCategoryEl.value.trim() : "";
    hideAllSections();
 	document.getElementById("submitBtn").style.display = "block";

    if (subProduct.toLowerCase() === "personal loan") {
      if (personalloneCategoryEl) {
        personalloneCategoryEl.style.display = "block";
         submitBtn.style.display = "block";
      }
 
      switch (perCategory) {
        case "PL & Doctor Loans (Salaried & SE across All Location)":
          if (plDoctorSection) plDoctorSection.style.display = "block";
  			 populatePerRowSelect(personalLoneData.plData);	
  			
        break;
 
        case "BIL Except Doctor Loans":
          if (bilExceptDoctorSection) bilExceptDoctorSection.style.display = "block";
		 	 populatePerRowSelect(personalLoneData.bilData);		
          break;
 
        case "PaisaBazaar":
          if (paisaBazarSection) paisaBazarSection.style.display = "block";
		  populatePerRowSelect(personalLoneData.paisaBazar);	

          break;
 
        case "Aggregator":
          if (aggregatorSection) aggregatorSection.style.display = "block";
		      populatePerRowSelect(personalLoneData.aggregatorData);	
          break;
 
        default:
          console.warn("No matching category section found for value:", perCategory);
          break;
      }
    }     else if (subProduct.toLowerCase() === "education loan") {
            if (personalloneCategoryEl) {
                personalloneCategoryEl.style.display = "block";
            }
 
 // Example in loadPersonalLoneTable for Education Loan:
switch (perCategory) {
                case "EL Prime":
                    if (elPrimeSection) elPrimeSection.style.display = "block";
                    populatePerRowSelect(educationLoanData.elPrimeData);
                    break;
 
                case "EL Generic":
                    if (elGenericSection) elGenericSection.style.display = "block";
                    populatePerRowSelect(educationLoanData.elGenericData);
                    break;

 				case "EL Focus":
                    if (elFocusSection) elFocusSection.style.display = "block";
                    populatePerRowSelect(educationLoanData.elFocusData);
                    break;
 
                case "EL Supreme":
                    if (elSupremeSection) elSupremeSection.style.display = "block";
                    populatePerRowSelect(educationLoanData.elSupremeData);
                    break;
 
                case "EL Flat":
                    if (elFlatSection) elFlatSection.style.display = "block";
                    populatePerRowSelect(educationLoanData.elFlatData);
                    break;
 
                case "EL Booster":
                    if (elBoosterSection) elBoosterSection.style.display = "block";
                    populatePerRowSelect(educationLoanData.elBoosterData);
                    break;
}
 
        }
      else {
      if (personalloneCategoryEl) {
        personalloneCategoryEl.style.display = "none";
      }
    }
  } catch (error) {
    console.error("An error occurred inside loadPersonalLoneTable:", error);
  }
}



function handleResetForm() {
	const submitBtn = document.getElementById("submitBtn");
	submitBtn.style.display = "none";
    // 1. Reset Select and Input Values
    const selectElementIds = [
        "product",
        "subProduct",
        "personallonecategory",
        "category2",
        "newPerRowSelect"
    ];
 
    selectElementIds.forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.value = ""; // Resets dropdown selection to default/empty
        }
    });
 
    // 2. Hide or Reset Specific Section Container IDs (including personal-Lone-id)
    const sectionIds = [
        "personal-Lone-id",
        "bilExceptDoctorSection",
        "paisaBazar",
        "aggregatorId",
        "elPrime",
        "elGeneric",
        "elSupreme",
        "elFlat",
        "elFocus",
        "elBooster"
    ];
 
    sectionIds.forEach(id => {
        const section = document.getElementById(id);
        if (section) {
            // Hide section
            section.style.display = "none";
 
            // If the section contains table rows or dynamic content, clear them out:
            // section.innerHTML = "";
        }
    });
 
    // 3. Reset Global State / Arrays
    if (typeof newlyAddedRows !== "undefined") {
        newlyAddedRows = [];
    }
 
    console.log("Form and sections reset successfully.");
}

function handlxsseResetForm() {
    // 1. Reset Dropdowns & Dates
    //if (document.getElementById("product")) document.getElementById("product").selectedIndex = 0;
    if (document.getElementById("subProduct")) document.getElementById("subProduct").selectedIndex = 0;
    if (document.getElementById("cyclefromDate")) document.getElementById("cyclefromDate").value = "";
    if (document.getElementById("cycletoDate")) document.getElementById("cycletoDate").value = "";
 
    // 2. Target inputs ONLY in columns 2 through 5 (skips Slab column 1 and Structure column 6)
    let inputsToClear = document.querySelectorAll(
        '#table-container tbody tr td:nth-child(2) input, ' +
        '#table-container tbody tr td:nth-child(3) input, ' +
        '#table-container tbody tr td:nth-child(4) input, ' +
        '#table-container tbody tr td:nth-child(5) input'
    );
 
    inputsToClear.forEach(input => {
        input.value = "";
    });
}

function fetchDataPersonlaLone(){
		
		let cycleFrom = document.getElementById("fromDate2").value;
		let cycleTo = document.getElementById("toDate2").value;
			
		console.log(cycleFrom,"value", cycleTo);		
	
	if (!cycleFrom || !cycleTo) {

		alert("Please select both Cycle From and Cycle To dates.");

		return;

	}
 
	let fromDate = new Date(cycleFrom);

	let toDate = new Date(cycleTo);
 
	if (fromDate >= toDate) {

		alert("Cycle From date must be earlier than Cycle To date.");

		return;

	}
 
	let diffInMs = toDate - fromDate;

	let diffInDays = diffInMs / (1000 * 60 * 60 * 24);
 
	if (diffInDays > 30) {

		alert("The gap between Cycle From and Cycle To must not exceed 30 days.");

		return;

	}
		loadPersonalLoneTable()

}
 



function updateNewlyAddedRowPayout(inputElement, inputName) {
    let inputObj = typeof inputElement === "object" ? inputElement : document.getElementsByName(inputName)[0];
    let rawValue = inputObj ? inputObj.value : inputElement;
 
    if (rawValue !== null && rawValue !== undefined && String(rawValue).trim() !== "") {
        
        let formattedValue = String(rawValue).trim();
        
        // 2. Append '%' if missing
        if (!formattedValue.endsWith("%")) {
            formattedValue += "%";
        }
 
        // 3. Update the UI Input Field immediately on blur
        if (inputObj) {
            inputObj.value = formattedValue;
        }
 
        // 4. Update the object inside newlyAddedRows array
        let targetRow = newlyAddedRows.find(row => row.inputName === (inputName || inputObj.name));
        
        // Fallback: Grab the last added row if inputName isn't matched
        if (!targetRow && newlyAddedRows.length > 0) {
            targetRow = newlyAddedRows[newlyAddedRows.length - 1];
        }
 
        if (targetRow) {
            targetRow.payout = formattedValue;
        }
    }
}
 
 

function loadPersonalLoanPending() {
    const container = document.getElementById("personalLoanCheckerContainer");
    const tbody = document.getElementById("personalLoanCheckerBody");
 
    if (!tbody) {
        console.error("Error: Element with id 'personalLoanCheckerBody' was not found in the DOM.");
        return;
    }
 
    if (container) {
        container.style.display = "block";
    }
 
    // Call Personal Loan Pending API Endpoint
    fetch("/api/slabs/pending")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="7" class="text-center text-muted py-4">
                            No pending personal loan records found.
                        </td>
                    </tr>`;
                return;
            }
 
            let sectionHtml = "";
 
            data.forEach((d) => {
                const minVal = d.minVal ?? '';
                const maxVal = d.maxVal ?? '';
                const payout = d.payout ?? '';
                const payoutType = d.payoutType ?? '';
                const category = d.category ?? '';
                const status = d.status ?? 'Pending';
                const recordId = d.id;
 
                sectionHtml += `
                    <tr class="align-middle border-bottom border-secondary">
                        <td class="fw-bold text-center py-2 border-end border-secondary">${category}</td>
                        <td class="text-center fw-semibold py-2 border-end border-secondary">${payoutType}</td>
                        <td class="text-center fw-medium py-2 border-end border-secondary">${minVal}</td>
                        <td class="text-center fw-medium py-2 border-end border-secondary">${maxVal}</td>
                        <td class="text-center fw-medium py-2 border-end border-secondary">${payout}</td>
                        <td class="text-center py-2 border-end border-secondary">
                            <span class="badge bg-warning text-dark px-2 py-1">${status}</span>
                        </td>
                        <td class="text-center py-2">
                            <div class="d-flex justify-content-center gap-1">
                                <button class="btn btn-success btn-sm px-2" onclick="approveSlab(${recordId})">Approve</button>
                                <button class="btn btn-danger btn-sm px-2" onclick="rejectSlab(${recordId})">Reject</button>
                            </div>
                        </td>
                    </tr>`;
            });
 
            tbody.innerHTML = sectionHtml;
        })
        .catch(error => {
            console.error("Fetch error:", error);
            tbody.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center text-danger py-4">
                        Failed to load data. Please try again later.
                    </td>
                </tr>`;
        });
}




 function savePersonalAndEucationLoan(){
 	let subProdcut  = document.getElementById("subProduct").value;
 	if(subProdcut == "Personal loan"){
 	 	saveSlabData();
 	}else if(subProdcut == "Education Loan"){
 	saveEducationLoanSlabData();
 	}
 }



/*async function saveSlabData() {
	

    if (!newlyAddedRows || newlyAddedRows.length === 0) {
        alert("No newly added rows to save!");
        return;
    }
    
 
    console.log("Sending updated newlyAddedRows to DB:", newlyAddedRows);
 
    try {
        const response = await fetch('/api/slabs/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(newlyAddedRows)
        });
 
        if (!response.ok) {
            throw new Error(`Server returned HTTP status ${response.status}`);
        }
 
        const data = await response.json();
 
        if (data.status === "SUCCESS") {
            alert("New row(s) saved successfully!");
            newlyAddedRows = []; // Clear array after successful save
        } else {
            alert("Failed to save: " + (data.message || "Unknown error"));
        }
 
    } catch (error) {
        console.error("Async Save Error:", error);
        alert("An error occurred while saving: " + error.message);
    }
}*/


/*async function saveSlabData(tbodyId) {
	let tbodyValue;
	let category = document.getElementById("category2").value;
	let payoutSTRBody= document.getElementById("payoutSTRBody");
	
	
	
	
	
	 if (category === 'PL & Doctor Loans (Salaried & SE across All Location)') {
        tbodyValue = "plTableBody";
    } else if (category === 'BIL Except Doctor Loans') {
        tbodyValue = "bilTableBody";
    } else if (category === 'PaisaBazaar') {
        tbodyValue = "paisBazarBody";
        prefix = "pib";
    } else if (category === 'Aggregator') {
        tbodyValue = "payoutSTRBody";
    } 

	console.log("tbodyValue ==>",tbodyValue);
	
    const tableContainer = document.getElementById(`${category}`);
    const rows = tableContainer ? tableContainer.querySelectorAll("tr") : [];
 	
 	console.log("Sending updated newlyAddedRows to DB:", newlyAddedRows);
 	
    const tableData = [];
    let dataToSend;
 
    // 1. Collect row data
    rows.forEach(row => {
        const inputs = row.querySelectorAll("input");
        if (inputs.length === 0) return;
 
        const minVal = inputs[0] ? inputs[0].value.trim() : "";
        const maxVal = inputs[1] ? inputs[1].value.trim() : "";
        const slabVal = inputs[2] ? inputs[2].value.trim() : "";
        const slabLabel = row.cells[0] ? row.cells[0].innerText.trim() : "";
 
        if (minVal !== "" || maxVal !== "" || slabVal !== "") {
            tableData.push({
            	category:category,
            	max:maxVal,
            	min: minVal,
            	payout:slabVal,
                payoutType: slabLabel,
                status:"Pending"
            });
        }
    });
 
    if (tableData.length === 0) {
        alert("No filled data found to save!");
        return;
    }
 

 
if (newlyAddedRows.length > 0) {
    dataToSend = newlyAddedRows; // Send newly added rows if present
} else {
    dataToSend = tableData;       // Otherwise, send existing tableData
}
 
 
    const payload = {
        user: "SYSTEM",
        tableData: tableData
    };
 
 
    try {
        const response = await fetch('/api/slabs/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            // Send the JSON Object, NOT the raw array
            body: JSON.stringify(dataToSend) 
        });
 
        if (!response.ok) {
            throw new Error(`Server returned HTTP status ${response.status}`);
        }
 
        const data = await response.json();
 
        if (data.status === "SUCCESS") {
            alert("Data saved successfully!");
        } else {
            alert("Failed to save: " + (data.message || "Unknown error"));
        }
 
    } catch (error) {
        console.error("Async Save Error:", error);
        alert("An error occurred while saving: " + error.message);
    }
}
 */



async function saveSlabData() {
	const cycleFromDate = document.getElementById("fromDate2")?.value || "";
    const cycleToDate   = document.getElementById("toDate2")?.value || "";
    let tbodyValue = "";
    let category = document.getElementById("category2") ? document.getElementById("category2").value : "";
 
    // 1. Map Category to target tbody ID
    if (category === 'PL & Doctor Loans (Salaried & SE across All Location)') {
        tbodyValue = "plTableBody";
    } else if (category === 'BIL Except Doctor Loans') {
        tbodyValue = "bilTableBody";
    } else if (category === 'PaisaBazaar') {
        tbodyValue = "paisBazarBody";
    } else if (category === 'Aggregator') {
        tbodyValue = "payoutSTRBody";
    } else {
        tbodyValue = tbodyId || "payoutSTRBody";
    }
 
    console.log("Target tbodyValue ==>", tbodyValue);
 
    //FIX 1: Fetch element by tbodyValue (NOT category string)
    const tableContainer = document.getElementById(tbodyValue);
    const rows = tableContainer ? tableContainer.querySelectorAll("tr") : [];
 
    const tableData = [];
 
 	rows.forEach(row => {
  const inputs = row.querySelectorAll("input");
  
  // Skip row if there are no inputs at all
  if (inputs.length === 0) return;
 
  const slabLabel = row.cells[0] ? row.cells[0].innerText.trim() : "";
  let minVal = "", maxVal = "", slabVal = "";
 
  // CASE 1: 2-Column Table (1 input for payout percentage)
  if (inputs.length === 1) {
    slabVal = inputs[0]?.value.trim() || "";
 
    // Parse Min and Max directly from row.cells[0] text
    if (slabLabel.includes("-")) {
      const parts = slabLabel.split("-");
      minVal = parts[0]?.trim() || "";
      maxVal = parts[1]?.trim() || "";
    } else if (slabLabel.includes("+")) {
      minVal = slabLabel.replace("+", "").trim();
      maxVal = ""; // Leave blank or set to high default based on backend rules
    }
  }
  // CASE 2: 4-Column Inputs Table
  else if (inputs.length === 4) {
    minVal  = inputs[1]?.value.trim() || "";
    maxVal  = inputs[2]?.value.trim() || "";
    slabVal = inputs[3]?.value.trim() || "";
  }
  // CASE 3: 3-Column Inputs Table
  else if (inputs.length === 3) {
    minVal  = inputs[0]?.value.trim() || "";
    maxVal  = inputs[1]?.value.trim() || "";
    slabVal = inputs[2]?.value.trim() || "";
  }
 
  // FIX 2: Push row if any field has a value
  if (minVal || maxVal || slabVal) {
    tableData.push({
      category: category,
      min: minVal,
      max: maxVal,
      payout: slabVal,
      payoutType: slabLabel,
      status: "Pending",
      cycleFrom: cycleFromDate,
      cycleTo: cycleToDate  
    });
  }
});
 
 
    console.log("Collected Table Data:", tableData);
 
    // 3. Validation Check
    if (tableData.length === 0) {
        alert("No filled data found to save!");
        return;
    }
 
    // 4. Send Data to API
/*    let dataToSend = (typeof newlyAddedRows !== "undefined" && newlyAddedRows.length > 0)
        ? newlyAddedRows
        : tableData;*/
 
    try {
        const response = await fetch('/api/slabs/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(tableData)
        });
 
        const result = await response.json();
        console.log("Saved successfully:", result);
        alert("Saved successfully!");
    } catch (error) {
        console.error("Save Error:", error);
    }
}



async function saveEducationLoanSlabData() {
    const categoryElem = document.getElementById("category2");
    const category = categoryElem ? categoryElem.value : "EL Generic";
 
    const cycleFromDate = document.getElementById("fromDate2")?.value || "";
    const cycleToDate   = document.getElementById("toDate2")?.value || "";
    const makerId       = document.getElementById("makerId")?.value || "MAKER_USER";
 
    // 1. Dynamic Category -> tbody ID mapping (candidates, in case naming varies)
    const categoryMap = [
        { key: "Prime",   ids: ["elPrime", "elPrimeTableBody"] },
        { key: "Generic", ids: ["eLGeneric", "eLGenericTableBody"] },
        { key: "Focus",   ids: ["elFocus", "elFocusTableBody"] },
        { key: "Supreme", ids: ["elSupreme", "elSupremeTableBody"] },
        { key: "Flat",    ids: ["elFlat", "elFlatTableBody"] },
        { key: "Booster", ids: ["elBooster", "elBoosterTableBody"] },
    ];
 
    const match = categoryMap.find(m => category.includes(m.key)) || categoryMap[1]; // default Generic
 
    // Try each candidate ID; if none exist, fall back to any element whose id
    // contains "TableBody" AND the category key (data-driven lookup as backup).
    let tableContainer = match.ids.map(id => document.getElementById(id)).find(Boolean);
 
    if (!tableContainer) {
        // Backup: search by data-category attribute instead of guessing IDs
        tableContainer = document.querySelector(`[data-category="${match.key}"]`);
    }
 
    if (!tableContainer) {
        // Debug aid: show every tbody-like id actually present in the DOM
        const allIds = Array.from(document.querySelectorAll('[id*="TableBody"], [id*="Body"]'))
            .map(el => el.id)
            .filter(Boolean);
        console.error(`[ERROR] No table body found for category "${category}". Tried IDs: ${match.ids.join(", ")}.`);
        console.error(`[ERROR] Elements found in DOM with similar IDs:`, allIds);
        alert(`Could not find the table for category "${category}".\nTried: ${match.ids.join(", ")}\nFound instead: ${allIds.join(", ") || "none"}`);
        return;
    }
 
    const rows = tableContainer.querySelectorAll("tr");
    if (rows.length === 0) {
        console.warn("[WARN] tbody has 0 rows. Row-render may not have finished, or rows are added after this handler fires.");
    }
 
    const tableData = [];
 
    rows.forEach((row, idx) => {
        const inputs = row.querySelectorAll("input");
        const cells = row.querySelectorAll("td");
        if (cells.length === 0 || inputs.length === 0) return;
 
        const bucketLabel = cells[0]?.innerText.trim() || "";
        const slabVal = inputs[0]?.value.trim() || "";
 
        let minVal = null, maxVal = null, description = null;
 
        // Case A: merged row, e.g. "ABOVE 1.5 CR"
        if (cells.length === 3 && cells[1].getAttribute("colspan") === "2") {
            const mergedText = cells[1].innerText.trim();
            description = mergedText;
            if (mergedText.toUpperCase().includes("ABOVE")) {
                if (mergedText.includes("1.5 CR") || mergedText.includes("1.5CR")) {
                    minVal = 15000000;
                } else {
                    const numMatch = mergedText.replace(/,/g, "").match(/\d+(\.\d+)?/);
                    minVal = numMatch ? parseFloat(numMatch[0]) : null;
                }
            }
        }
        // Case B: standard row [Bucket | Min | Max | Payout]
        else if (cells.length >= 4) {
            const minCellText = cells[1].innerText.trim();
            const maxCellText = cells[2].innerText.trim();
            minVal = (minCellText && minCellText !== "-") ? parseFloat(minCellText.replace(/,/g, "")) : 0;
            if (maxCellText && maxCellText !== "-") {
                maxVal = parseFloat(maxCellText.replace(/,/g, ""));
            }
        } else {
            console.log(`[DEBUG] row ${idx} unmatched shape (cells.length=${cells.length})`);
        }
 
        if (slabVal !== "") {
            tableData.push({
                bucket: bucketLabel,
                min: minVal,
                max: maxVal,
                description,
                slab: slabVal.endsWith("%") ? slabVal : `${slabVal}%`,
            });
        }
    });
 
    const payload = { category, makerId, cycleFromDate, cycleToDate, slabs: tableData };
 
    try {
        const response = await fetch("/api/v1/slabs/maker/submit", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });
        const result = await response.json();
        alert(response.ok
            ? "Success: " + (result.message || "Submitted successfully.")
            : "Error: " + (result.error || result.message));
    } catch (error) {
        console.error("API Call Error:", error);
        alert("Server error while submitting!");
    }
}
 




/*async function saveEducationLoanSlabData() {
    let tbodyValue = "";
    const categoryElem = document.getElementById("category2");
    const category = categoryElem ? categoryElem.value : "EL Generic";
 
    const cycleFromDate = document.getElementById("fromDate2") ? document.getElementById("fromDate2").value : "";
    const cycleToDate   = document.getElementById("toDate2") ? document.getElementById("toDate2").value : "";
    const makerId       = document.getElementById("makerId") ? document.getElementById("makerId").value : "MAKER_USER";
 
    // 1. Dynamic Category to tbody ID mapping
    if (category.includes('Prime')) tbodyValue = "elPrimeTableBody";
    else if (category.includes('Generic')) tbodyValue = "elGenericTableBody";
    else if (category.includes('Focus')) tbodyValue = "elFocusTableBody";
    else if (category.includes('Supreme')) tbodyValue = "elSupremeTableBody";
    else if (category.includes('Flat')) tbodyValue = "elFlatTableBody";
    else if (category.includes('Booster')) tbodyValue = "elBoosterTableBody";
    else tbodyValue = "elGenericTableBody";
 
    const tableContainer = document.getElementById(tbodyValue);
    console.log("[DEBUG] category:", category, "-> tbodyValue:", tbodyValue);
    console.log("[DEBUG] tableContainer found?", !!tableContainer);
 
    if (!tableContainer) {
        console.error(`[ERROR] No element with id="${tbodyValue}" found in DOM.`);
        alert(`Could not find table body #${tbodyValue}.`);
        return;
    }
 
    const rows = tableContainer.querySelectorAll("tr");
    console.log("[DEBUG] rows found:", rows.length);
 
    if (rows.length === 0) {
        console.warn("[WARN] tbody has 0 rows. The row-render function may not have finished running yet, " +
                      "or rows are being added AFTER this Save button click handler fires. " +
                      "Check if Add Row / range selection completed before clicking Save.");
    }
 
    const tableData = [];
 
    rows.forEach((row, idx) => {
        const inputs = row.querySelectorAll("input");
        const cells = row.querySelectorAll("td");
 
        console.log(`[DEBUG] row ${idx}: cells=${cells.length} inputs=${inputs.length}`);
 
        if (cells.length === 0 || inputs.length === 0) {
            console.log(`[DEBUG] row ${idx} skipped: no cells or no inputs`);
            return;
        }
 
        const bucketLabel = cells[0] ? cells[0].innerText.trim() : "";
        const slabVal = inputs[0] ? inputs[0].value.trim() : "";
 
        let minVal = null;
        let maxVal = null;
        let description = null;
 
        console.log(`[DEBUG] row ${idx}: bucket="${bucketLabel}" slabVal="${slabVal}"`);
 
        // Case A: Merged row, e.g. "ABOVE 1.5 CR" (bucket + merged colspan=2 cell + input)
        if (cells.length === 3 && cells[1].getAttribute('colspan') === "2") {
            const mergedText = cells[1].innerText.trim();
            description = mergedText;
 
            if (mergedText.toUpperCase().includes("ABOVE")) {
                if (mergedText.includes("1.5 CR") || mergedText.includes("1.5CR")) {
                    minVal = 15000000;
                } else {
                    const numMatch = mergedText.replace(/,/g, '').match(/\d+(\.\d+)?/);
                    minVal = numMatch ? parseFloat(numMatch[0]) : null;
                }
            }
        }
        // Case B: Standard row [Bucket | Min | Max | Payout input]
        else if (cells.length >= 4) {
            const minCellText = cells[1].innerText.trim();
            const maxCellText = cells[2].innerText.trim();
 
            minVal = (minCellText && minCellText !== "-")
                ? parseFloat(minCellText.replace(/,/g, ''))
                : 0;
 
            if (maxCellText && maxCellText !== "-") {
                maxVal = parseFloat(maxCellText.replace(/,/g, ''));
            }
        } else {
            console.log(`[DEBUG] row ${idx} unmatched shape (cells.length=${cells.length})`);
        }
 
        if (slabVal !== "") {
            tableData.push({
                bucket: bucketLabel,
                min: minVal,
                max: maxVal,
                description: description,
                slab: slabVal.endsWith('%') ? slabVal : `${slabVal}%`
            });
        } else {
            console.log(`[DEBUG] row ${idx} skipped: slabVal is empty`);
        }
    });
 
    console.log("Prepared Table Data for DB -->", tableData);
 
    const payload = {
        category: category,
        makerId: makerId,
        cycleFromDate: cycleFromDate,
        cycleToDate: cycleToDate,
        slabs: tableData
    };
 
    try {
        const response = await fetch('/api/v1/slabs/maker/submit', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
 
        const result = await response.json();
        if (response.ok) {
            alert("Success: " + (result.message || "Submitted successfully."));
        } else {
            alert("Error: " + (result.error || result.message));
        }
    } catch (error) {
        console.error("API Call Error:", error);
        alert("Server error while submitting!");
    }
}*/
 

function approveStatus(id) {
    if (confirm("Are you sure you want to approve this slab?")) {
        updateSlabStatus(id, "APPROVED");
    }
}
 
// Triggered on clicking "Reject" button
function rejectStatus(id) {
    if (confirm("Are you sure you want to reject this slab?")) {
        updateSlabStatus(id, "REJECTED");
    }
}
 
// Common function to send POST request to the API
function updateSlabStatus(seqId, statusValue) {
    const payload = {
        id: seqId,
        status: statusValue
    };
 
    fetch("/api/slabs/updateStatus", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.status === "SUCCESS") {
            alert(data.message);
            // Refresh table or pending list after success
            if (typeof getPendingRecords === "function") {
                getPendingRecords();
            } else {
                location.reload(); // Fallback reload
            }
        } else {
            alert("Failed: " + data.message);
        }
    })
    .catch(error => {
        console.error("API Error:", error);
        alert("An error occurred while updating status. Please try again.");
    });
}
