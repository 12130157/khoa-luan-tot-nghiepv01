<%-- 
    Document   : ImportLecturer
    Created on : 24-12-2011, 10:39:33
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.type.HocVi"%>
<%@page import="java.util.EnumSet"%>
<%@page import="uit.cnpm02.dkhp.model.type.HocHam"%>
<%@page import="java.util.Calendar"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="uit.cnpm02.dkhp.utilities.ExecuteResult"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
    
    /**Is this page load by response when submit import student
        from file ?**/
    boolean isRespForImpFromFile = false;
    try {
        isRespForImpFromFile = (Boolean)session.getAttribute("import.from.file.response");
    } catch (Exception ex) {
        isRespForImpFromFile = false;
    }
    ExecuteResult importFromFileER = null;
    if (isRespForImpFromFile) {
        importFromFileER = (ExecuteResult)session.getAttribute("import-from-file-result");
    }
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Giảng viên</title>
        <style media="all" type="text/css">
            /** CSS override **/
        </style>
    </head>
    <body>
        <input type="hidden" id="is-respone-for-import-from-file" value="<%= isRespForImpFromFile %>"/>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <!--Main Navigation-->
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <div id="main-title">
                    Trang nhập Giảng Viên
                </div>
                <br /><br />
                <%-- Form import one --%>
                <div class="range">
                    <h3><span id="btn-open-form-import-lecturer" class="atag">
                            Thêm GV
                    </span></h3>
                    <div id="import-lecturer-one">
                        <b><u>Phần nhập thông tin GV</u></b></br></br>
                        <div class="clear"></div>
                        <%-- Persional information input range --%>
                        <div id="lecturer-persional-info">
                            <table id="tbl-persional-info">
                                <tr>
                                    <%
                                    String year = "" + Calendar.getInstance().get(Calendar.YEAR);
                                    year = year.substring(2); // get 2 last number
                                    %>
                                    <td> Mã GV </td>
                                    <td> <input type="text" id="txt-magv" value="<%=year%>520" /> </td>
                                </tr>
                                <tr>
                                    <td> Họ và tên </td>
                                    <td> <input type="text" id="txt-fullname"/> </td>
                                </tr>
                                <tr>
                                    <td> Ngày sinh </td>
                                    <td>
                                        <input type="text" id="txt-birthday" name="txt-birthday">
                                        <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('txt-birthday','YYMMMDD')" />
                                    </td>
                                </tr>
                                <tr>
                                    <td> Giới tính </td>
                                    <td>
                                        <select id="txt-gender" class="input-minwidth">
                                            <option value="Nam"> Nam </option>
                                            <option value="Nữ"> Nữ </option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> CMND </td>
                                    <td> <input type="text" id="txt-cmnd"/> </td>
                                </tr>
                                <tr>
                                    <td> Điện thoại </td>
                                    <td> <input type="text" id="txt-phone"/> </td>
                                </tr>
                                <tr>
                                    <td> Email </td>
                                    <td> <input type="text" id="txt-email"/> </td>
                                </tr>
                                <tr>
                                    <td> Quê quán </td>
                                    <td> 
                                        <textarea id="txt-home" class="input-text-area">
                                        </textarea> 
                                    </td>
                                </tr>
                                <tr>
                                    <td> Khoa </td>
                                    <td> 
                                        <select id="txt-faculty" class="input-minwidth" onchange="changeFaculty()">
                                            <%
                                            if ((faculties != null) && !faculties.isEmpty()) {
                                                for (int i = 0; i < faculties.size(); i++) {
                                                    Faculty f = faculties.get(i);
                                                    %>
                                                    <option value="<%=f.getId()%>"> <%= f.getFacultyName()%> </option>
                                                    <%
                                                }
                                            }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Học Hàm </td>
                                    <td>
                                        <select id="txt-hocham" class="input-minwidth">
                                            <%
                                            // Retrieve Student status
                                            for(HocHam st : EnumSet.allOf(HocHam.class)) {
                                            %>
                                            <option value="<%= st.value()%>"> <%= st.description()%> </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Học vị </td>
                                    <td> 
                                        <select id="txt-hocvi" class="input-minwidth">
                                            <%
                                            // Retrieve Student status
                                            for(HocVi st : EnumSet.allOf(HocVi.class)) {
                                            %>
                                            <option value="<%= st.value()%>"> <%= st.description()%> </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Ghi chú </td>
                                    <td> 
                                        <textarea id="txt-note" class="input-text-area">
                                        </textarea> 
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <%-- Button check and add lecturer from form --%>
                        <div class="clear"></div>
                        <div id="btn-check-and-add-lecturer" style="margin-left: 25px; width: 265px;">
                            <div id="btn-check-add-one-lecturer" style="float: left;">
                                <div class="button-1">
                                    <span class="atag" onclick="checkAddOneLecturer()" ><img src="../../imgs/check.png" />Kiểm tra</span>
                                </div>
                            </div>
                            <div id="btn-add-one-lecturer" style="float: left; padding-left: 12px;">
                                <div class="button-1">
                                    <span class="atag" onclick="addLecturerFromForm()" ><img src="../../imgs/check.png" />Thêm SV</span>
                                </div>
                            </div>
                        </div>
                        <%--div id="error"></div--%>
                        <div id="add-from-table-result"></div>
                        <div class="clear"></div>
                        <div id="add-one-result" style="padding-top: 12px;">
                            <%-- 
                                After submit to add one lecturer from form
                                User expect to see result here
                                If add successfylly, user still has a choice
                                to cancel action...
                            --%>
                        </div>
                    </div>
                </div>
                <div class="clear"></div>
                <%-- Import from File --%>
                <div class="range">
                    <h3><span id="btn-import-lecturer-from-file" class="atag">
                        Thêm từ file 
                    </span></h3>
                    <div id="form-import-lecturer-from-file">
                        <form id="importFromFile" 
                              action="../../ManageLecturerController?function=importfromfile&import-as-possible=false"
                              method="post" name="importFromFile" enctype="multipart/form-data">
                            <u>Chọn File</u>
                            <table id="tblFromFile">
                                <tr><td><input type="file" name="txtPath" id="txtPath"></td></tr>
                                <tr>
                                    <td>
                                        <div class="button-1" style="margin-left: 3px !important; padding:3px; margin-top: 15px;">
                                            <span class="atag" onclick="submitImportLecturerFromFile()" ><img src="../../imgs/check.png" />Hoàn thành</span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form>
                        <div id="import-from-file-result">
                            <%-- Show response result --%>
                            <%
                            if (isRespForImpFromFile && (importFromFileER != null)) {
                                List<Lecturer> lecturers = new ArrayList<Lecturer>();
                                if (!importFromFileER.isIsSucces()) {
                                    %>
                                    <i> <%= importFromFileER.getMessage()%> </i>
                                    <%
                                    lecturers = (List<Lecturer>)importFromFileER.getData();
                                    if ((lecturers != null) && !lecturers.isEmpty()) {
                                         %>
                                          </br>
                                         <span class="atag" onclick="retryImportFromFile()">
                                            <b>Thử lại</b>
                                        </span><i> ( *Chỉ Thêm những GV hợp lệ)</i>
                                        <table class="general-table" style="width: 450px;">
                                            <tr><th> STT </th><th>Mã GV</th><th> Họ và tên </th><th>CMND</th></tr>
                                        <%
                                        for (int i = 0; i < lecturers.size(); i++) {
                                        %>
                                            <tr>
                                                <td><%= (i + 1) %></td>
                                                <td><%= lecturers.get(i).getId() %></td>
                                                <td><%= lecturers.get(i).getFullName() %></td>
                                                <td><%= lecturers.get(i).getIdentityCard() %></td>
                                            </tr>
                                        <%
                                        }//End For loop
                                        %>
                                        </table>
                                        <%
                                    }
                                } else {
                                    lecturers = (List<Lecturer>)importFromFileER.getData();
                                    if ((lecturers != null) && !lecturers.isEmpty()) {
                                        %>
                                        <i> Thêm thành công các SV: </i>
                                        <table class="general-table" style="width: 450px;">
                                            <tr><th> STT </th><th>MSSV</th><th> Họ và tên </th><th>CMND</th></tr>
                                        <%
                                        for (int i = 0; i < lecturers.size(); i++) {
                                        %>
                                            <tr>
                                                <td><%= (i + 1) %></td>
                                                <td><%= lecturers.get(i).getId() %></td>
                                                <td><%= lecturers.get(i).getFullName() %></td>
                                                <td><%--= lecturers.get(i).getIdentityNumber() --%></td>
                                            </tr>

                                        <%
                                        }//End For loop
                                        %>
                                        </table>
                                        <%
                                   } else {
                                        %>
                                        <i>Không có GV nào được thêm, vui lòng kiểm tra lại file</i>
                                        <%
                                   }
                                } // end if
                            } //Eend if
                            %>
                            <%--------------------%>
                        </div>
                        <%--Show file format--%>
                        <div class="clear"></div>
                        <br />
                        <div class="range" style="margin:0 0 10px !important; width: 98%; ">
                            <span id="btn-show-file-format" class="atag">
                                File format
                            </span>
                            <div id="file-format-view" class="short_sidebar">
                                <img src="../../imgs/form_format/import_lecturer_format_file.PNG"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script language="javascript">
            //call after page loaded
            window.onload = doAfterPageLoaded; 
        </script>
    </body>

    <script src="../../javascripts/UtilTable.js"> </script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <SCRIPT language="javascript">
        var http = createRequestObject();
        $("#btn-open-form-import-lecturer").click(function () {
            $('#import-lecturer-one').slideToggle(500);
        });
        
        $("#btn-import-lecturer-from-file").click(function () {
            $('#form-import-lecturer-from-file').slideToggle(500);
        });
        
        $("#btn-show-file-format").click(function () {
            $('#file-format-view').slideToggle(500);
        });
        
        function submitImportLecturerFromFile() {
            var path = document.getElementById("txtPath").value;
            if (path.length == 0) {
                alert("Hãy chọn file trước khi submit.");
                return;
            }
            
            if (path.indexOf(".xls") < 0) {
                alert("Hiện tại hệ thống chỉ hỗ trợ file .xls");
                return;
            }
            
            document.forms["importFromFile"].submit();
        }
        
        function validateData() {
            var msgv = "";
            var fullName = "";
            var cmnd = "";
            var address = "";
            try {
                msgv = document.getElementById("txt-magv").value;
                fullName = document.getElementById("txt-fullname").value;
                birthDay = document.getElementById("txt-birthday").value;
                cmnd = document.getElementById("txt-cmnd").value;
                address = document.getElementById("txt-home").value;
            } catch(err) {
                alert("[ImportLecturer][Validate] - An error occur:" + err);
                return;
            }
            var error = "";
            if ((msgv == null) || (msgv.length == 0))
                error = "MaGV, ";
            if ((fullName == null) || (fullName.length == 0))
                error += "Họ tên, ";
            if ((birthDay == null) || (birthDay.length == 0))
                error += "Ngày sinh, ";
            if ((cmnd == null) || (cmnd.length == 0))
                error += "CMND, ";
            if ((address == null) || (address.length == 0))
                error += "Quê quán, ";
            if (error.length > 0) {
                alert("Các trường sau không được để trống: " + error);
                return false;
            } else {
                // Validate something else...
                return true;
            }
        }
        
        function getInformationFromForm() {
            // Basic information
            var magv = null;
            var fullName = null;
            var birthDay = null;
            var gender = null;
            var cmnd = null;
            var phone = null;
            var email = null;
            var address = null;
            
            // External information from University
            var faculty = null;
            var hocham = null;
            var hocvi = null;
            var note = null;
            try {
                magv = document.getElementById("txt-magv").value;
                fullName = document.getElementById("txt-fullname").value;
                birthDay = document.getElementById("txt-birthday").value;
                cmnd = document.getElementById("txt-cmnd").value;
                address = document.getElementById("txt-home").value;
                gender = document.getElementById("txt-gender").value;
                phone = document.getElementById("txt-phone").value;
                email = document.getElementById("txt-email").value;
                faculty = document.getElementById("txt-faculty").value;
                hocham = document.getElementById("txt-hocham").value;
                hocvi = document.getElementById("txt-hocvi").value;
                note = document.getElementById("txt-note").value;
            } catch(err) {
                alert("[ImportLecturer][Validate] - An error occur:" + err);
                return;
            }
            var data = "&magv=" + magv
                        + "&fullname=" + fullName
                        + "&birthDay=" + birthDay
                        + "&cmnd=" + cmnd
                        + "&gender=" + gender
                        + "&phone=" + phone
                        + "&email=" + email
                        + "&address=" + address
                        + "&faculty=" + faculty
                        + "&hocham=" + hocham
                        + "&hocvi=" + hocvi
                        + "&note=" + note;
            return data;
        }
        
        function checkAddOneLecturer() {
            if (!validateData()) {
                return;
            }
            
            // Incase validate passed: continue send data to server
            // for special check...
            var controller = "../../ManageLecturerController?function=validate-add-one" 
                            + getInformationFromForm();
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = addOneLecturerResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function addLecturerFromForm() {
            if (!validateData()) {
                return;
            }
            
            var controller = "../../ManageLecturerController?function=add-one" 
                            + getInformationFromForm();
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = addOneLecturerResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function retryImportFromFile() {
            var controller = "../../ManageLecturerController?function=retry-import-from-file";
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = retryImportLecturerResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function retryImportLecturerResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("import-from-file-result");
                detail.innerHTML = http.responseText;
            }
        }
        
        function addOneLecturerResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("add-one-result");
                detail.innerHTML = http.responseText;
            }
        }
        
        function cancelAddOne(magv) {
            var controller = "../../ManageLecturerController?function=cancel-add-one" 
                            + "&magv=" + magv;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = addOneLecturerResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        
        
        //
        // Shared be cleared.
        //
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
                            currentData += 'x' + ',';
                        else
                            currentData += row.cells[6].childNodes[0].value + ','; //Ghi chu
                        currentData += row.cells[7].childNodes[0].value + ','; //CMND
                        //currentData += row.cells[8].childNodes[0].value + ','; //DienThoai
                        if (row.cells[8].childNodes[0].value == '')
                            currentData += 'x' + ',';
                        else
                            currentData += row.cells[8].childNodes[0].value + ','; //Ghi chu
                        //currentData += row.cells[9].childNodes[0].value + ','; //Email
                        if (row.cells[9].childNodes[0].value == '')
                            currentData += 'x' + ',';
                        else
                            currentData += row.cells[9].childNodes[0].value + ','; //Ghi chu
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
