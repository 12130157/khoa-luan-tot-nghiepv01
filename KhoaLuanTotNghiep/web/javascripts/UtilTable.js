/**
 * Select all row of table.
 * @Param tableId the ID of table.
 */
function selectAll(tableId, checkboxIndex) {
    var table = document.getElementById(tableId);
    var rowCount = table.rows.length;
    var row = table.rows[0];
    var chkboxSelect = row.cells[checkboxIndex].childNodes[0];
    var select = false;
    if (chkboxSelect != null) {
        if (chkboxSelect.checked) {
            select = true;
        }
    } else {
        alert('\u0110ã có lỗi xảy ra.');
        return;
    }
    for(var i = 1; i < rowCount; i++) {
        row = table.rows[i];
        var chkbox = row.cells[checkboxIndex].childNodes[0];
        chkbox.checked = select;
    }
}

/**
 * Delete all selected row of table.
 * IN each row of table, there is one
 * check box, and if this control is 
 * checked --> will be delete
 * 
 * @Param tableID the id of table.
 */
function deleteRow(tableID) {
    try {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
                
        if (rowCount < 3) {
            alert("Không th\u1ec3 xóa tất cả các dòng");
            return;
        }

        for(var i = 1; i < rowCount; i++) {
            var row = table.rows[i];
            row. cells[1].innerHTML = i;
            var chkbox = row.cells[0].childNodes[0];
            if(null != chkbox && true == chkbox.checked) {
                table.deleteRow(i);
                rowCount--;
                i--;
            }
        }
    }catch(e) {
        alert(e);
    }
}

/**
 * 
 * Delete row at special index of table
 * 
 * @Param tableID the id of table.
 * @Param index of deleted row.
 */
function deleteRow(tableID, index) {
    try {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
        
        if ((index < 0) || (index >= rowCount)) {
            alert('Not delete any row.');
            return;
        }

        table.deleteRow(index);
    }catch(e) {
        alert(e);
    }
}

/**
 * Create a new input element
 * 
 * @Param row row's index.
 * @Param name name of element
 * @Param index index of element in row.
 */
function createNewInputCell(row, name, index) {
    var cell = row.insertCell(index);
    var element = document.createElement("input");
    element.name = name;
    element.type = "text";
    element.id = name;
    cell.appendChild(element);
}

/**
 * Create a new Selection element
 * 
 * @Param row row's index.
 * @Param name name of element
 * @Param index index of element in row.
 * @Param values an array hold the values of selections.
 */
function createNewSelectionCell(row, name, index, values) {
    var cell = row.insertCell(index);
    var element = document.createElement("select");
    element.name = name;
    element.id = name;
    var i = 0;
    for(e in values) {
        element.options[i] = new Option(values[i], name + values[i]);
        element.options[i].value = values[i];
        i ++;
    }
    cell.appendChild(element);
}

/**
 * Create a new Button
 * 
 * @Param 
 * @Param 
 * @Param 
 * @Param 
 * @Param 
 * @Return
 */
function createNewButton(row, name, index, value) {
    var cell = row.insertCell(index);
    var element = document.createElement("button");
    element.name = name;
    element.id = name;
    element.value = value;
    element.innerHTML = value;
    
    cell.appendChild(element);
    
    return element;
}