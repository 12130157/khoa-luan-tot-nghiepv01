<%-- 
    Document   : ManageStudent
    Created on : 16-11-2011, 22:06:04
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@include file="MenuPDT.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    String listFaculties = "";
    String listCourse = "";
    String listClass = "";
    List<Faculty> faculties = DAOFactory.getFacultyDao().findAll();
    List<Course> courses = DAOFactory.getCourseDao().findAll();
    List<uit.cnpm02.dkhp.model.Class> clazz = DAOFactory.getClassDao().findAll();

    int i;
    if ((faculties != null) && !faculties.isEmpty()) {
        for (i = 0; i < faculties.size(); i++) {
            if (i > 0) {
                listFaculties += "-";
            }
            listFaculties += faculties.get(i).getId();
        }
    }
    if ((courses != null) && !courses.isEmpty()) {
        for (i = 0; i < courses.size(); i++) {
            if (i > 0) {
                listCourse += "-";
            }
            listCourse += courses.get(i).getId();
        }
    }
    if ((clazz != null) && !clazz.isEmpty()) {
        for (i = 0; i < clazz.size(); i++) {
            if (i > 0) {
                listClass += "-";
            }
            listClass += clazz.get(i).getId();
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
                    <u><h3>Trang nhập Sinh Viên</h3></u>
                </div>

                <br>
                <hr/><hr/><br>
                <p id="error">
                    <%
                        String error = (String) session.getAttribute("error");
                        if ((error != null) && !error.isEmpty()) {
                            session.removeAttribute("error");
                    %>
                    <%= error%>
                    <%  }
                    %>
                </p>

                <p>
                    <INPUT type="button" value="Thêm hàng" onclick="addRow('dataTable')" />
                    <INPUT type="button" value="Xóa mục đã chọn" onclick="deleteRow('dataTable')" />
                    <INPUT type="submit" value="Hoàn thành" onclick="submitInsertStudentFromTable('../../ManageStudentController?function=import', 'dataTable')" />
                </p>
                <hr/><hr/>
                <div id="sidebar">
                    <table id="dataTable" width="450px" border="1">
                        <tr id="dataTable-th">
                            <td><INPUT type="checkbox" name="chkAll" onclick="selectAll('dataTable', 0)" /></td>
                            <td align: center> STT </td>
                            <td align="center"> MSSV </td>
                            <td align="center"> Họ Và Tên </td>
                            <td align="center"> Ngày Sinh </td>
                            <td align="center"> Giới Tính </td>
                            <td align="center"> CMND </td>
                            <td align="center"> Quê quán </td>
                            <td align="center"> Địa chỉ </td>
                            <td align="center"> Điện thoại </td>
                            <td align="center"> Email </td>
                            <td align="center"> Lớp </td>
                            <td align="center"> Khoa </td>
                            <td align="center"> Khóa học </td>
                            <td align="center"> Tình Trạng </td>
                            <td align="center"> Bậc học </td>
                            <td align="center"> Ngày nhập học </td>
                            <td align="center"> Loại hình học </td>
                            <td align="center"> Ghi chú</td>
                        </tr>
                    </table>

                </div> 

                <br />
                <hr /><hr />
                <form id="importFromFile" action="../../ManageStudentController?function=importfromfile" method="post" name="importFromFile" enctype="multipart/form-data">
                    <u>Thêm Sinh Viên Từ File</u><br/>

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

            //MSSV
            createNewInputCell(row, 'txtMSSV', 2);
                
            //Họ Và Tên
            createNewInputCell(row, 'txtName', 3);
            //Ngày Sinh
            // ==> share be update to calenda
            createNewInputCell(row, 'txtBirthDay', 4);
            
            //Giới Tính
            createNewSelectionCell(row, 'selectSex', 5, new Array("Nam", "N\u1eef"));
            
            //CMND
            createNewInputCell(row, 'txtCMND', 6);
            
            //Quê quán
            createNewInputCell(row, 'txtHomeAddr', 7);
            
            //Địa chỉ
            createNewInputCell(row, 'txtAddr', 8);
            
            //Điện thoại
            createNewInputCell(row, 'txtPhone', 9);
            
            //Email
            createNewInputCell(row, 'txtEmail', 10);
            
            //Lớp
            createNewSelectionCell(row, 'selectClass', 11, clazzsArray);
            
            //Khoa
            createNewSelectionCell(row, 'selectFaculty', 12, facultiesArray);
            
            //Khóa học
            createNewSelectionCell(row, 'selectCourse', 13, courseArray);
            
            //Tình Trạng
            createNewSelectionCell(row, 'selectStatus', 14, new Array('\u0110ang học', '\u0110ang Bảo Lưu Kq', '\u0110ã Ra Trường'));
            
            //Bậc học
            createNewSelectionCell(row, 'selectLevel', 15, new Array('\u0110ại Học', 'Cao \u0110ẳng', 'Trung C\u1ea5p'));
            
            //Ngày nhập học
            // ==> share be update to calendar input
            createNewInputCell(row, 'txtEnterDay', 16);
            
            //Loại hình học
            createNewSelectionCell(row, 'selectType', 17, new Array('T\u1eadp chung', '\u0110ào tạo từ xa'));
            
            //Ghi chú
            createNewInputCell(row, 'txtPhone', 18);
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
         * Insert students(s) from table
         * 
         * @Param pagename point to controller.
         * @Param tableData table's id
         */
        function submitInsertStudentFromTable(pagename, tableData){
            datas = getListStudentFromTable(tableData);
            
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
         * Insert students(s) from file
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
        function getListStudentFromTable(tableID) {
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
                        currentData += elTableCells[2].childNodes[0].value + ','; //MSSV
                        currentData += elTableCells[3].childNodes[0].value + ','; //Ho Ten
                        currentData += elTableCells[4].childNodes[0].value + ','; //Ngay Sinh
                        currentData += row.cells[5].childNodes[0].value + ','; //Gioi Tinh
                        currentData += row.cells[6].childNodes[0].value + ','; //CMND
                        currentData += row.cells[7].childNodes[0].value + ','; //Que quan
                        if (row.cells[8].childNodes[0].value == '')
                            currentData += 'x,';
                        else
                            currentData += row.cells[8].childNodes[0].value + ','; //Dia chi
                        if (row.cells[9].childNodes[0].value == '')
                            currentData += 'x,';
                        else
                            currentData += row.cells[9].childNodes[0].value + ','; //Dien thoai
                        if (row.cells[10].childNodes[0].value == '')
                            currentData += 'x,';
                        else
                            currentData += row.cells[10].childNodes[0].value + ','; //Email
                        currentData += row.cells[11].childNodes[0].value + ','; //Lop
                        currentData += row.cells[12].childNodes[0].value + ','; //Khoa
                        currentData += row.cells[13].childNodes[0].value + ','; //KhoaHoc
                        currentData += row.cells[14].childNodes[0].value + ','; //TinhTrang
                        currentData += row.cells[15].childNodes[0].value + ','; //Bac hoc
                        currentData += row.cells[16].childNodes[0].value + ','; //Ngay Nhap Hoc
                        currentData += row.cells[17].childNodes[0].value + ','; //Loai hinh hoc
                        if (row.cells[8].childNodes[0].value == '')
                            currentData += 'x';
                        else
                            currentData += row.cells[8].childNodes[0].value ; //Ghi chu
                        
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
                (elTableCells[4].childNodes[0].value == '') ||
                (elTableCells[6].childNodes[0].value == '') ||
                (elTableCells[7].childNodes[0].value == '') ||
                (elTableCells[6].childNodes[0].value == '')
        ) {
                return false;
            }
            
            return true;
        }
        
    </SCRIPT>
</html>
