<%-- 
    Document   : ManageStudent
    Created on : 16-11-2011, 22:06:04
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.ExecuteResult"%>
<%@page import="uit.cnpm02.dkhp.utilities.Constants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="uit.cnpm02.dkhp.model.type.StudyType"%>
<%@page import="uit.cnpm02.dkhp.model.type.StudyLevel"%>
<%@page import="java.util.EnumSet"%>
<%@page import="uit.cnpm02.dkhp.model.type.StudentStatus"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<uit.cnpm02.dkhp.model.Class> clazzes = 
            (List<uit.cnpm02.dkhp.model.Class>)session.getAttribute("clazzes");
    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
    List<Course> courses = (List<Course>) session.getAttribute("courses");
    
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
        <title>Quản lý sinh viên</title>
        <style media="all" type="text/css">
        </style>
    </head>
    <body>
        <input type="hidden" id="is-respone-for-import-from-file" value="<%= isRespForImpFromFile %>"/>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Trang nhập Sinh Viên
                </div>

                <br /><br />
                <%-- Form import one --%>
                <div class="range">
                    <h3><span id="btn-open-form-import-student" class="atag">
                            Thêm Sinh Viên
                    </span></h3>
                    <div id="import-student-one" class="wrapper-border">
                        <b><u>Phần nhập thông tin SV</u></b></br></br>
                        <div class="clear"></div>
                        <%-- Persional information input range --%>
                        <div id="student-persional-info">
                            <u>Thông tin căn bản</u>
                            <table id="tbl-persional-info">
                                <tr>
                                    <%
                                    String year = "" + Calendar.getInstance().get(Calendar.YEAR);
                                    year = year.substring(2); // get 2 last number
                                    %>
                                    <td> MSSV </td>
                                    <td> <input type="text" id="txt-mssv" value="<%=year%>520" /> </td>
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
                                    <td> ĐC thường trú </td>
                                    <td> 
                                        <textarea id="txt-address" class="input-text-area">
                                        </textarea> 
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <%--Externam information at university--%>
                        <div id="student-university-info">
                            <u>Thông tin bổ sung</u>
                            <table id="tbl-university-info">
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
                                    <td> Lớp </td>
                                    <td>
                                        <select id="txt-class" class="input-minwidth">
                                            <%
                                            if ((clazzes != null) && !clazzes.isEmpty()) {
                                                for (int i = 0; i < clazzes.size(); i++) {
                                                    uit.cnpm02.dkhp.model.Class clazz = clazzes.get(i);
                                                    %>
                                                    <option value="<%=clazz.getId()%>"> <%= clazz.getClassName()%> </option>
                                                    <%
                                                }
                                            }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Khóa </td>
                                    <td>
                                        <select id="txt-course" class="input-minwidth">
                                            <%
                                            if ((courses != null) && !courses.isEmpty()) {
                                                for (int i = 0; i < courses.size(); i++) {
                                                    Course c = courses.get(i);
                                                    %>
                                                    <option value="<%=c.getId()%>"> <%= c.getYearIn() + " - " + c.getYearOut() %> </option>
                                                    <%
                                                }
                                            }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Tình trạng </td>
                                    <td>
                                        <select id="txt-status" class="input-minwidth">
                                            <%
                                            // Retrieve Student status
                                            for(StudentStatus st : EnumSet.allOf(StudentStatus.class)) {
                                            %>
                                            <option value="<%= st.value()%>"> <%= st.description()%> </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Bậc học </td>
                                    <td>
                                        <select id="txt-study-level" class="input-minwidth">
                                            <%
                                            // Retrieve study level (Bậc học)
                                            for(StudyLevel sl : EnumSet.allOf(StudyLevel.class)) {
                                            %>
                                            <option value="<%= sl.value()%>"> <%= sl.description()%> </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Ngày nhập học </td>
                                    <td>
                                        <%--
                                        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
                                        Date today = Calendar.getInstance().getTime();
                                        String aaa = sdf.format(today);
                                        String d = sdf.format(new Date());
                                        --%>
                                        <input type="text" id="txt-date-enter" name="txt-date-enter" />
                                        <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('txt-date-enter','YYMMMDD')" />
                                    </td>
                                </tr>
                                <tr>
                                    <td> Loại hình học </td>
                                    <td>
                                        <select id="txt-study-type" class="input-minwidth">
                                            <%
                                            // Retrieve study type.
                                            for(StudyType st : EnumSet.allOf(StudyType.class)) {
                                            %>
                                            <option value="<%= st.value()%>"> <%= st.description()%> </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Ghi chú </td>
                                    <td> <input type="text" id="txt-note"/> </td>
                                </tr>
                            </table>
                        </div>
                        <div class="clear"></div>
                        <div id="btn-check-and-add-student" style="margin: auto; width: 265px;">
                            <div id="btn-check-add-one-student" style="float: left;">
                                <div class="button-1">
                                    <span class="atag" onclick="checkAddOneStudent()" ><img src="../../imgs/check.png" />Kiểm tra</span>
                                </div>
                            </div>
                            <div id="btn-add-one-student" style="float: left; padding-left: 12px;">
                                <div class="button-1">
                                    <span class="atag" onclick="addStudentFromForm()" ><img src="../../imgs/check.png" />Thêm SV</span>
                                </div>
                            </div>
                        </div>
                        <%--div id="error"></div--%>
                        <div id="add-from-table-result"></div>
                        <div class="clear"></div>
                        <div id="add-one-result" class="msg-response" style="width: 600px;">
                                <%-- 
                                    After submit to add one student from form
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
                    <h3><span id="btn-import-student-from-file" class="atag">
                        Thêm từ file 
                    </span></h3>
                    <div id="form-import-student-from-file" class="wrapper-border">
                        <form id="importFromFile" 
                              action="../../ManageStudentController?function=importfromfile&import-as-possible=false"
                              method="post" name="importFromFile" enctype="multipart/form-data">
                            <u>Chọn File</u>
                            <table id="tblFromFile">
                                <tr><td><input type="file" name="txtPath" id="txtPath"></td></tr>
                                <tr>
                                    <td>
                                        <div class="button-1" style="margin-left: 3px !important; padding:3px; margin-top: 15px;">
                                            <span class="atag" onclick="submitImportFromFile()" ><img src="../../imgs/check.png" />Hoàn thành</span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form>
                        <div id="import-from-file-result">
                            <%-- Show response result --%>
                            <%
                            if (isRespForImpFromFile && (importFromFileER != null)) {
                                List<Student> students = new ArrayList<Student>();
                                if (!importFromFileER.isIsSucces()) {
                                    %>
                                    <i> <%= importFromFileER.getMessage()%> </i>
                                    <%
                                    students = (List<Student>)importFromFileER.getData();
                                    if ((students != null) && !students.isEmpty()) {
                                         %>
                                          </br>
                                         <span class="atag" onclick="retryImportFromFile()">
                                            <b>Thử lại</b>
                                        </span><i> ( *Chỉ Thêm những SV hợp lệ)</i>
                                        <table class="general-table" style="width: 450px;">
                                            <tr><th> STT </th><th>MSSV</th><th> Họ và tên </th><th>CMND</th></tr>
                                        <%
                                        for (int i = 0; i < students.size(); i++) {
                                        %>
                                            <tr>
                                                <td><%= (i + 1) %></td>
                                                <td><%= students.get(i).getId() %></td>
                                                <td><%= students.get(i).getFullName() %></td>
                                                <td><%= students.get(i).getIdentityNumber() %></td>
                                            </tr>
                                        <%
                                        }//End For loop
                                        %>
                                        </table>
                                        <%
                                    }
                                } else {
                                    students = (List<Student>)importFromFileER.getData();
                                    if ((students != null) && !students.isEmpty()) {
                                        %>
                                        <i> Thêm thành công các SV: </i>
                                        <table class="general-table" style="width: 450px;">
                                            <tr><th> STT </th><th>MSSV</th><th> Họ và tên </th><th>CMND</th></tr>
                                        <%
                                        for (int i = 0; i < students.size(); i++) {
                                        %>
                                            <tr>
                                                <td><%= (i + 1) %></td>
                                                <td><%= students.get(i).getId() %></td>
                                                <td><%= students.get(i).getFullName() %></td>
                                                <td><%= students.get(i).getIdentityNumber() %></td>
                                            </tr>

                                        <%
                                        }//End For loop
                                        %>
                                        </table>
                                        <%
                                   } else {
                                        %>
                                        <i>Không có SV nào được thêm, vui lòng kiểm tra lại file</i>
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
                        <%-- File format --%>
                        <div class="range" style="margin:0 0 10px !important; width: 98%; ">
                            <span id="btn-show-file-format" class="atag">
                                File format
                            </span>
                            <div id="file-format-view" class="short_sidebar">
                                <img src="../../imgs/form_format/import_student_format_file.PNG"/>
                            </div>
                        </div>
                        <div class="clear"></div>
                    </div>
                </div>
                
                <%-- ----------- --%>
                <%-- Show result --%>
               <div class="clear"></div>
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
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/UtilTable.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <%--<script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>--%>
    <SCRIPT language="javascript">
        var http = createRequestObject();
        
        $("#btn-open-form-import-student").click(function () {
            $('#import-student-one').slideToggle(500);
        });
        
        $("#btn-import-student-from-file").click(function () {
            $('#form-import-student-from-file').slideToggle(500);
        });
        
        $("#btn-show-file-format").click(function () {
            $('#file-format-view').slideToggle(500);
        });
        
        function submitImportFromFile() {
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
        function changeFaculty() {
            var facultyId = document.getElementById("txt-faculty").value;
            var controller = "../../ManageStudentController?function=faculty-change&facultyId=" + facultyId;

            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = changeFacultyResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function changeFacultyResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("txt-class");
                detail.innerHTML = http.responseText;
            }
        }
        
        function validateData() {
            // Validate not null fields
            // MSSV // Ho Ten // Ngay Sinh // CMND // Que Quan // Ngay Nhap hoc
            var mssv = "";
            var fullName = "";
            var birthDay = "";
            var cmnd = "";
            var homeAddress = "";
            var enterDate = "";
            try {
                mssv = document.getElementById("txt-mssv").value;
                fullName = document.getElementById("txt-fullname").value;
                birthDay = document.getElementById("txt-birthday").value;
                cmnd = document.getElementById("txt-cmnd").value;
                homeAddress = document.getElementById("txt-home").value;
                enterDate = document.getElementById("txt-date-enter").value;
            } catch(err) {
                alert("[ImportStudent][Validate] - An error occur:" + err);
                return;
            }
            var error = "";
            if ((mssv == null) || (mssv.length == 0))
                error = "MSSV, ";
            if ((fullName == null) || (fullName.length == 0))
                error += "Họ tên, ";
            if ((birthDay == null) || (birthDay.length == 0))
                error += "Ngày sinh, ";
            if ((cmnd == null) || (cmnd.length == 0))
                error += "CMND, ";
            if ((homeAddress == null) || (homeAddress.length == 0))
                error += "Quê quán, ";
            if ((enterDate == null) || (enterDate.length == 0))
                error += "Ngày nhập học";
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
            var mssv = null;
            var fullName = null;
            var birthDay = null;
            var gender = null;
            var cmnd = null;
            var phone = null;
            var email = null;
            var homeAddress = null;
            var address = null;
            
            // External information from University
            var faculty = null;
            var clazz = null;
            var course = null;
            var status = null;
            var level = null;
            var enterDate = null;
            var studyType = null;
            var note = null;
            try {
                mssv = document.getElementById("txt-mssv").value;
                fullName = document.getElementById("txt-fullname").value;
                birthDay = document.getElementById("txt-birthday").value;
                cmnd = document.getElementById("txt-cmnd").value;
                homeAddress = document.getElementById("txt-home").value;
                gender = document.getElementById("txt-gender").value;
                phone = document.getElementById("txt-phone").value;
                email = document.getElementById("txt-email").value;
                address = document.getElementById("txt-address").value;
                enterDate = document.getElementById("txt-date-enter").value;
                faculty = document.getElementById("txt-faculty").value;
                clazz = document.getElementById("txt-class").value;
                course = document.getElementById("txt-course").value;
                status = document.getElementById("txt-status").value;
                level = document.getElementById("txt-study-level").value;
                studyType = document.getElementById("txt-study-type").value;
                note = document.getElementById("txt-note").value;
            } catch(err) {
                alert("[ImportStudent][Validate] - An error occur:" + err);
                return;
            }
            var data = "&mssv=" + mssv
                        + "&fullname=" + fullName
                        + "&birthDay=" + birthDay
                        + "&cmnd=" + cmnd
                        + "&homeAddress=" + homeAddress
                        + "&gender=" + gender
                        + "&phone=" + phone
                        + "&email=" + email
                        + "&addsress=" + address
                        + "&enterDate=" + enterDate
                        + "&faculty=" + faculty
                        + "&clazz=" + clazz
                        + "&course=" + course
                        + "&status=" + status
                        + "&level=" + level
                        + "&studyType=" + studyType
                        + "&note=" + note;
            return data;
        }
        
        function checkAddOneStudent() {
            if (!validateData()) {
                return;
            }
            
            // Incase validate passed: continue send data to server
            // for special check...
            var controller = "../../ManageStudentController?function=validate-add-one" 
                            + getInformationFromForm();
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = addOneStudentResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function addStudentFromForm() {
            if (!validateData()) {
                return;
            }
            
            var controller = "../../ManageStudentController?function=add-one" 
                            + getInformationFromForm();
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = addOneStudentResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function addOneStudentResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                $('#add-one-result').show('slow');
                var detail = document.getElementById("add-one-result");
                detail.innerHTML = http.responseText;
            }
        }
        
        function cancelAddOne(mssv) {
            var controller = "../../ManageStudentController?function=cancel-add-one" 
                            + "&mssv=" + mssv;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = addOneStudentResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function retryImportFromFile() {
            var controller = "../../ManageStudentController?function=retry-import-from-file";
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = retryImportStudentResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function retryImportStudentResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("import-from-file-result");
                detail.innerHTML = http.responseText;
            }
        }
        
        var facultiesArray = new Array();
        var courseArray = new Array();
        var clazzsArray = new Array();
        
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
        
    function createNewDateCell(row, name, index) {
        var cell = row.insertCell(index);
        var element = document.createElement("input");
        element.name = name;
        element.type = "text";
        element.id = name;
        cell.appendChild(element);
        
        var el1 = "<img src=\"../../imgs/cal.gif\" style=\"cursor: pointer;\" onclick=\"javascript:NewCssCal('" + name + "','YYMMMDD')\" />";
        var element1 = document.createElement(el1);
        cell.appendChild(element1);
    }
        
    </SCRIPT>
</html>
