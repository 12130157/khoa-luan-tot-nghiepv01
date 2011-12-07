function selectAll(tableId) {
    var table = document.getElementById(tableId);
    var rowCount = table.rows.length;
    var row = table.rows[0];
    var chkboxSelect = row.cells[0].childNodes[0];
    var select = false;
    if (chkboxSelect != null) {
        if (chkboxSelect.checked) {
            select = true;
        }
    } else {
        alert('Đã có lỗi xảy ra.');
        return;
    }
    for(var i = 1; i < rowCount; i++) {
        var row = table.rows[i];
        var chkbox = row.cells[0].childNodes[0];
        chkbox.checked = select;
    }
}

function deleteRow(tableID) {
    try {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
                
        if (rowCount < 3) {
            alert("Không thể xóa tất cả các dòng");
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

function createNewInputCell(row, name, index) {
    var cell = row.insertCell(index);
    var element = document.createElement("input");
    element.name = name;
    element.type = "text";
    element.id = name;
    cell.appendChild(element);
}
        
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