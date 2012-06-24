<%-- 
    Document   : EditStudent
    Created on : 16-11-2011, 21:27:49
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="uit.cnpm02.dkhp.model.type.StudyType"%>
<%@page import="uit.cnpm02.dkhp.model.type.StudyLevel"%>
<%@page import="uit.cnpm02.dkhp.model.type.StudentStatus"%>
<%@page import="java.util.EnumSet"%>
<%@page import="java.util.Calendar"%>
<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    Student student = (Student) session.getAttribute("student");
    
    List<uit.cnpm02.dkhp.model.Class> clazzes = 
            (List<uit.cnpm02.dkhp.model.Class>)session.getAttribute("clazzes");
    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
    List<Course> courses = (List<Course>) session.getAttribute("courses");
%>
<html>
    <%--File header--%>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý sinh viên</title>
        <%--CSS override--%>
        <style media="all" type="text/css">
        </style>
    </head>
    <body onload="">
        <!--Div Wrapper-->
        <div id="wrapper">
            <%--Menu--%>
            <%@include file="MenuPDT.jsp"%>
            <!--Main Navigation-->
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%--Page's title--%>
                <div id="main-title">
                    Trang chỉnh sửa thông tin Sinh Viên
                </div>
                <br /><br /><br />
                <p id="error">

                </p>

                <div id="import-student-one">
                    <div class="clear"></div>
                    <%-- Persional information input range --%>
                    <div class="range" style="float: left; width: 45%;">
                        <h3>Thông tin căn bản</h3>
                        <div id="student-persional-info">
                            <table id="tbl-persional-info">
                                <tr>
                                    <td> MSSV </td>
                                    <td> <input type="text" id="txt-mssv" readonly="readonly" onfocus="this.blur();" value="<%= student.getId() %>" /> </td>
                                </tr>
                                <tr>
                                    <td> Họ và tên </td>
                                    <td> <input type="text" id="txt-fullname" value="<%= student.getFullName() %>" /> </td>
                                </tr>
                                <tr>
                                    <td> Ngày sinh </td>
                                    <td>
                                        <input type="text" id="txt-birthday" name="txt-birthday" value="<%= DateTimeUtil.format(student.getBirthday()) %>">
                                        <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('txt-birthday','YYMMMDD')" />
                                    </td>
                                </tr>
                                <tr>
                                    <td> Giới tính </td>
                                    <td>
                                        <select id="txt-gender" class="input-minwidth">
                                            <%
                                            String[] genders = new String[] {"Nam", "Nữ"};
                                            for (int i = 0; i < 2; i++) {
                                            %>
                                                <option value="<%= genders[i]%>"
                                                        <%
                                                        if (student.getGender().equalsIgnoreCase(genders[i])) {
                                                        %>
                                                        selected="selected"
                                                        <%
                                                        }
                                                        %>
                                                >
                                                    <%= genders[i]%>
                                                </option>
                                            <%
                                            }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> CMND </td>
                                    <td> <input type="text" id="txt-cmnd" value="<%= student.getIdentityNumber() %>" /> </td>
                                </tr>
                                <tr>
                                    <td> Điện thoại </td>
                                    <td> <input type="tel" id="txt-phone" value="<%= student.getPhone() %>" /> </td>
                                </tr>
                                <tr>
                                    <td> Email </td>
                                    <td> <input type="emal" id="txt-email" value="<%= student.getEmail() %>"/> </td>
                                </tr>
                                <tr>
                                    <td> Quê quán </td>
                                    <td> 
                                        <textarea id="txt-home" class="input-text-area">
                                            <%= student.getHomeAddr() %>
                                        </textarea> 
                                    </td>
                                </tr>
                                <tr>
                                    <td> ĐC thường trú </td>
                                    <td> 
                                        <textarea id="txt-address" class="input-text-area">
                                            <%= student.getAddress() %>
                                        </textarea> 
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    
                    <%--Externam information at university--%>
                    <div class="range" style="float: left; width: 45%; margin-left: 30px;">
                        <h3>Thông tin bổ sung</h3>
                        <div id="student-university-info">
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
                                                    <option value="<%=f.getId()%>"
                                                            <%
                                                            if (student.getFacultyCode().equalsIgnoreCase(f.getId())) {
                                                            %>
                                                            selected="selected"
                                                            <%
                                                            }
                                                            %>
                                                            >
                                                        <%= f.getFacultyName()%>
                                                    </option>
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
                                                    <option value="<%=clazz.getId()%>"
                                                            <%
                                                            if (student.getClassCode().equalsIgnoreCase(clazz.getId())) {
                                                            %>selected="selected"<%
                                                            }
                                                            %>
                                                            >
                                                        <%= clazz.getClassName()%>
                                                    </option>
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
                                                    <option value="<%=c.getId()%>"
                                                            <%
                                                            if (student.getCourseCode().equalsIgnoreCase(c.getId())) {
                                                            %>selected="selected"<%
                                                            }
                                                            %>
                                                            >
                                                        <%= c.getYearIn() + " - " + c.getYearOut() %>
                                                    </option>
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
                                            <option value="<%= st.value()%>"
                                                    <%
                                                    if (student.getStatus().equalsIgnoreCase(st.description())) {
                                                    %>selected="selected"<%
                                                    }
                                                    %>
                                                    >
                                                <%= st.description()%>
                                            </option>
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
                                            <option value="<%= sl.value()%>"
                                                    <%
                                                    if (student.getStudyLevel().equalsIgnoreCase(sl.description())) {
                                                    %>selected="selected"<%
                                                    }
                                                    %>
                                                    >
                                                <%= sl.description()%>
                                            </option>
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
                                        <input type="text" id="txt-date-enter" name="txt-date-enter" value="<%= DateTimeUtil.format(student.getDateStart()) %>"/>
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
                                            <option value="<%= st.value()%>"
                                                    <%
                                                    if (student.getStudyType().equalsIgnoreCase(st.description())) {
                                                    %>selected="selected"<%
                                                    }
                                                    %>
                                                    >
                                                <%= st.description()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td> Ghi chú </td>
                                    <td> <input type="text" id="txt-note" value="<%= student.getNote() %>"/> </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="clear"></div>
                    <div id="update-result" style="text-align: center;"></div>
                    <div id="import-student-button" class="button-1">
                        <span class="atag" onclick="updateStudent()" ><img src="../../imgs/icon/update.png" />Cập nhật</span>
                    </div>
                    <br />
                    
                </div>
            </div><!--End Contents-->
            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/AjaxUtil.js"></script>
    <SCRIPT language = "javascript">
        var http = createRequestObject();
        
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
        
        function updateStudent() {
            if (!validateData()) {
                return;
            }
            var controller = "../../ManageStudentController?function=update" 
                            + getInformationFromForm();
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = updateResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function updateResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("update-result");
                detail.innerHTML = http.responseText;
            }
        }
        
    </SCRIPT>
</html>
