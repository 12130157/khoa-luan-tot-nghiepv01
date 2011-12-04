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