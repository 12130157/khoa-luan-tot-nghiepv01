<%-- 
    Document   : ImportLecturer
    Created on : 24-12-2011, 10:39:33
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    String listFaculties = "";
    String listCourse = "";
    String listClass = "";
    List<Faculty> faculties = DAOFactory.getFacultyDao().findAll();
    int i;
    if ((faculties != null) && !faculties.isEmpty()) {
        for (i = 0; i < faculties.size(); i++) {
            if (i > 0) {
                listFaculties += "-";
            }
            listFaculties += faculties.get(i).getId();
        }
    }
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý sinh viên</title>
        <style media="all" type="text/css">
            #dataTable{
                margin-left: 10px;
                margin-top: 20px;
                margin-bottom: 20px;
                width: 740px;
                border: 3px solid #73726E;
            }
            #dataTable-th{
                height: 32px;
                font-weight: bold;
                background-color: #175F6E;
                text-align: center;
            }
            #dataTable td{
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                padding: 2px 5px 2px 5px;
                text-align: left;
            }
            #formsearch{
                margin-top: 10px;
                margin-left: 20px;
                padding: 5px 10px 5px 10px;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                border: 3px solid #73726E;
                width: 320px;
            }
            #red{
                margin-left: 32px;
                margin-top: 15px;
                background-color: #e4e4e3;
                width: 250px;
                height: 32px;
            }
            #red:hover {
                border: 2px solid #ff092d;
            }
            #sidebar {
                height:400px;
                overflow:auto;
            }

            #error {
                background-color: #73726E;
                color: #ffffff;
                border-width: 2px;
                border-color: #9f8d39;
                font-size: 10px;
                float: right;
            }
        </style>
    </head>
    <body onload="pageLoad('dataTable', 'faculties', 'course', 'clazz');">
        <input type="hidden" name="faculties" id="faculties" value="<%=listFaculties%>">
        <input type="hidden" name="clazz" id="clazz" value="<%=listClass%>">
        <input type="hidden" name="course" id="course" value="<%=listCourse%>">

        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div style="text-align: center;">
                    <u> <h3>Trang nhập Giảng Viên</h3></u>
                </div>

                <br>
                <hr/><hr/><br>
                <p id="error">
                    <%
                        String error = (String) session.getAttribute("error");
                        if ((error != null) && !error.isEmpty()) {
                            session.removeAttribute("error");
                    %>
                    <%= error %>
                    <%  }
                    %>
                </p>

                <p>
                    <INPUT type="button" value="Thêm hàng" onclick="addRow('dataTable')" />
                    <INPUT type="button" value="Xóa mục đã chọn" onclick="deleteRowT('dataTable')" />
                    <INPUT type="submit" value="Hoàn thành" onclick="submitInsertLecturerFromTable('../../ManageLecturerController?function=import', 'dataTable')" />
                </p>
                <hr/><hr/>
                <div id="sidebar">
                    <table id="dataTable" width="450px" border="1">
                        <tr id="dataTable-th">
                            <td><INPUT type="checkbox" name="chkAll" onclick="selectAll('dataTable', 0)" /></td>
                            <td align: center> STT </td>
                            <td align="center"> Mã GV </td>
                            <td align="center"> Họ Và Tên </td>
                            <td align="center"> Khoa </td>
                            <td align="center"> Ngày sinh </td>
                            <td align="center"> Quê quán </td>
                            <td align="center"> CMND </td>
                            <td align="center"> Điện thoại </td>
                            <td align="center"> Email </td>
                            <td align="center"> Giới tính </td>
                            <td align="center"> Học Hàm </td>
                            <td align="center"> Học Vị </td>
                            <td align="center"> Ghi chú</td>
                        </tr>
                    </table>

                </div> 

                <br />
                <hr /><hr />
                <form id="importFromFile" action="../../ManageLecturerController?function=importfromfile" method="post" name="importFromFile" enctype="multipart/form-data">
                    <u>Thêm Giảng Viên Từ File</u><br/>

                    <table id="tblFromFile">
                        <tr>
                            <td><input type="file" name="txtPath" id="txtPath"></td>
                        </tr>
                        <tr>
                            <td><input type="submit" value="Hoàn thành."></td>
                        </tr>
                    </table>
                </form><br>

            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/UtilTable.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <SCRIPT language="javascript">
        var http = createRequestObject();
        var facultiesArray = new Array();
        var courseArray = new Array();
        var clazzsArray = new Array();
        
        //
        // Util functions for edit table.
        //
        function addRow(tableID) {
            var table = document.getElementById(tableID);
            var rowCount = table.rows.length;
            var row = table.insertRow(rowCount);
            row.backgroundColor = '#AA25FF';
            row. color = "#FFDD33";
                
            var cellChkb = row.insertCell(0);
            var elementChkb = document.createElement("input");
            elementChkb.type = "checkbox";
            cellChkb.appendChild(elementChkb);
 
            //STT
            var cellIndex = row.insertCell(1);
            cellIndex.innerHTML = rowCount;

            //Mã GV
            createNewInputCell(row, 'txtMGV', 2);
                
            //Họ Và Tên
            createNewInputCell(row, 'txtName', 3);
            //Khoa
            createNewSelectionCell(row, 'selectFaculty', 4, facultiesArray);
            //createNewInputCell(row, 'txtFaculty', 4);
            
            //Ngày sinh
            //createNewSelectionCell(row, 'selectSex', 5, new Array("Nam", "N\u1eef"));
            createNewInputCell(row, 'txtBirthday', 5);
            
            //Quê quán
            createNewInputCell(row, 'txtAddress', 6);
            
            //CMND
            createNewInputCell(row, 'txtCMND', 7);
            
            //Điện thoại
            createNewInputCell(row, 'txtPhone', 8);
            
            //Email
            createNewInputCell(row, 'txtEmail', 9);
            
            //Giới tính
            //createNewInputCell(row, 'txtGender', 10);
            createNewSelectionCell(row, 'selectSex', 10, new Array("Nam", "N\u1eef"));
            
            //Học Hàm
            createNewSelectionCell(row, 'selectHocHam', 11, new Array("", "Giáo S\u01b0", "PGS"));
            //createNewInputCell(row, 'txtHocHam', 11);
            
            //Học Vị
            createNewSelectionCell(row, 'selectHocVi', 12, new Array("Ti\u1ebfn sĩ", "P. Tiến Sĩ", "Thạc sĩ", "Cử nhân"));
            //createNewInputCell(row, 'txtHocVi', 11);
            
            //Ghi chú
            createNewInputCell(row, 'txtNote', 13);
        }
        
        function pageLoad(tableID, listFaculties, listCourse, listClazz) {
            var faculties = document.getElementById(listFaculties).value ;
            var courses = document.getElementById(listCourse).value ;
            var clazz = document.getElementById(listClazz).value ;
            facultiesArray = faculties. split("-");
            courseArray = courses. split("-");
            clazzsArray = clazz. split("-");
            
            addRow(tableID);
        }
 
        /**
         * Insert lecturers(s) from table
         * 
         * @Param pagename point to controller.
         * @Param tableData table's id
         */
        function submitInsertLecturerFromTable(pagename, tableData){
            datas = getListLecturerFromTable(tableData);
            
            if (datas == 'fail') {
                return;
            }
                
            var controller = pagename + '&data=' + datas;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = handleResponse;
                http.send(null);
                
            }
        }
        
        /**
         * Insert lecturers(s) from file
         * 
         * @Param pagename point to controller.
         */
        function submitInsertFromFile(pagename) {
            if(http){
                http.open("GET", pagename ,true);
                http.onreadystatechange = handleResponse;
                http.send(null);
            }
        }

        function handleResponse(){
            if(http.readyState == 4 && http.status == 200){
                location.reload(true);
            }
        }
        
        /*
        * Get data string from table.
        * 
        * @Param tableID table's id
        * @Return string hold data.
        */
        function getListLecturerFromTable(tableID) {
            var datas = '';
            var selectOne = false;
            try {
                var table = document.getElementById(tableID);
                var rowCount = table.rows.length;
                for(var i = 1; i < rowCount; i++) {
                    var row = table.rows[i];
                    var chkbox = row.cells[0].childNodes[0];
                    if(null != chkbox && true == chkbox.checked) {
                        if (validateInputValue(row) == false) {
                            alert('Vui lòng nh\u1eadp đầy thông tin cần thiết cho dòng ' + i);
                            return 'fail'; //fail
                        }
                        if (selectOne == false)
                            selectOne = true;
                        var elTableCells = row.getElementsByTagName('td');
                        var currentData = '';
                        currentData += elTableCells[2].childNodes[0].value + ','; //MGV
                        currentData += elTableCells[3].childNodes[0].value + ','; //Ho Ten
                        currentData += elTableCells[4].childNodes[0].value + ','; //MaKhoa
                        currentData += row.cells[5].childNodes[0].value + ','; //NgaySinh
                        //currentData += row.cells[6].childNodes[0].value + ','; //QueQuan
                        if (row.cells[6].childNodes[0].value == '')
                            currentData += 'x';
                        else
                            currentData += row.cells[6].childNodes[0].value ; //Ghi chu
                        currentData += row.cells[7].childNodes[0].value + ','; //CMND
                        //currentData += row.cells[8].childNodes[0].value + ','; //DienThoai
                        if (row.cells[8].childNodes[0].value == '')
                            currentData += 'x';
                        else
                            currentData += row.cells[8].childNodes[0].value ; //Ghi chu
                        //currentData += row.cells[9].childNodes[0].value + ','; //Email
                        if (row.cells[9].childNodes[0].value == '')
                            currentData += 'x';
                        else
                            currentData += row.cells[9].childNodes[0].value ; //Ghi chu
                        currentData += row.cells[10].childNodes[0].value + ','; //GioiTinh
                        currentData += row.cells[11].childNodes[0].value + ','; //HocHam
                        currentData += row.cells[12].childNodes[0].value + ','; //HocVi
                        //currentData += row.cells[13].childNodes[0].value + ','; //GhiChu
                        if (row.cells[13].childNodes[0].value == '')
                            currentData += 'x';
                        else
                            currentData += row.cells[13].childNodes[0].value ; //Ghi chu
                        
                        if (i < (rowCount - 1)) {
                            currentData += ';';
                        }
                        
                        datas += currentData;
                    }
                }
            }catch(e) {
                alert(e);
            }
            if (selectOne == false) {
                alert('Vui lòng ch\u1ecdn ít nhất một hàng.');
                return 'fail';
            }
            
            return datas;
        }
        
        function validateInputValue(row) {
            var elTableCells = row. getElementsByTagName('td');
            if ((elTableCells[2].childNodes[0].value == '') ||
                (elTableCells[3].childNodes[0].value == '') ||
                (elTableCells[5].childNodes[0].value == '') ||
                (elTableCells[7].childNodes[0].value == '')) {
                return false;
            }
            
            return true;
        }
        
    </SCRIPT>
</html>
