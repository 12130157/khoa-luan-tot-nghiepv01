<%-- 
    Document   : ManageStudentClass
    Created on : 10-07-2012, 22:47:01
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    List<uit.cnpm02.dkhp.model.Class> allClasses =
            (List<uit.cnpm02.dkhp.model.Class>) session.getAttribute("classes");
    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
    List<Lecturer> lecturer = (List<Lecturer>) session.getAttribute("lecturers");
    List<Course> courses = (List<Course>) session.getAttribute("courses");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Điểm Sinh Viên</title>
        <style media="all" type="text/css">
            
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%-- Title --%>
                <div id="main-title">
                    Trang quản Lớp học
                </div>
                <%--List file--%>
                <br />
                <div id="list-existed-class" class="range">
                    <%-- File from Lecturer --%>
                    <h3><span id="btn-list-existed-class" class="atag">Danh sách các lớp hiện tại</span></h3> 
                    <div id="tbl-existed-class">
                        <table class="general-table">
                            <tr>
                                <th>Mã lớp</th>
                                <th>Tên lớp</th>
                                <th>Khoa</th>
                                <th>Khóa học</th>
                                <th>GVCN</th>
                                <th>Số SV</th>
                                <th></th>
                            </tr>
                            <%
                            if (allClasses != null && !allClasses.isEmpty()) {
                                for (uit.cnpm02.dkhp.model.Class clazz : allClasses) {
                            %>
                                <tr>
                                    <td><%= clazz.getId() %></td>
                                    <td><%= clazz.getClassName() %></td>
                                    <td><%= clazz.getFacultyCode() %></td>
                                    <td><%= clazz.getCourseCode() %></td>
                                    <td><%= clazz.getHomeroom() %></td>
                                    <td><%= clazz.getNumOfStudent() %></td>
                                    <td><span class="atag" onclick="deleteStudentClass('<%=clazz.getId() %>')" > <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/> </span></td>
                                </tr>
                            <%}}
                            %>
                        </table>
                    </div>
                        <div id="msg-response"></div>
                </div>

                <div class="clear"></div>
                <%-- Add a class --%>
                <div class="range">
                    <h3><span id="btn-create-new-class" class="atag">Tạo lớp mới</span></h3> 
                    <div id="form-create-new-class" style="display: none;">
                        <table class="general-table" style="width: 650px;">
                            <tr>
                                <td>Mã lớp</td>
                                <td><input type="text" placeholder="CNPM01, CNPM02, ..." id="txt-class-id" name="txt-class-id" /></td>
                            </tr>
                            <tr>
                                <td>Tên lớp</td>
                                <td><input type="text" placeholder="Lớp CNPM 01, ..." id="txt-class-name" name="txt-class-name" /></td>
                            </tr>
                            <tr>
                                <td>Chọn khoa</td>
                                <td>
                                    <select id="txt-faculty" class="input-minwidth" onchange="changeFacultyUpdateLecturer()">
                                        <%
                                        if ((faculties != null) && !faculties.isEmpty()) {
                                            for (Faculty f : faculties) {
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
                                <td>GVCN</td>
                                <td>
                                    <select id="txt-lecturer" class="input-minwidth" >
                                        <%
                                        if ((lecturer != null) && !lecturer.isEmpty()) {
                                            for (Lecturer l : lecturer) {
                                                %>
                                                <option value="<%=l.getId()%>"> <%= l.getFullName()%> </option>
                                                <%
                                            }
                                        }
                                        %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>Khóa học</td>
                                <td>
                                    <select id="txt-course" class="input-minwidth">
                                        <%
                                        if ((courses != null) && !courses.isEmpty()) {
                                            for (Course c : courses) {
                                                String description = c.getYearIn() + " - " + c.getYearOut();
                                                %>
                                                <option value="<%=c.getId()%>"> <%= description %> </option>
                                                <%
                                            }
                                        }
                                        %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>SLSV</td>
                                <td><input type="text" id="txt-slsv" name="txt-slsv" onkeypress='validate(event)'/></td>
                            </tr>
                        </table>
                        <div class="clear"> <br /> </div>
                        <div class="button-1">
                            <span class="atag" onclick="createNewClass()" ><img src="../../imgs/check.png"/>Submit</span>
                        </div>
                        <div class="clear"></div>
                        <div id="message-handler"></div>
                    </div>
                </div>
                <br />
            </div><!--End Contents-->

            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"> </script>
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            var http = createRequestObject();
            
            $("#btn-list-existed-class").click(function () {
                $('#tbl-existed-class').slideToggle(500);
            });
            
            $("#btn-create-new-class").click(function () {
                $('#form-create-new-class').slideToggle(500);
            });
            
            function validate(evt) {
                var theEvent = evt || window.event;
                var key = theEvent.keyCode || theEvent.which;
                key = String.fromCharCode( key );
                var regex = /[0-9]|\./;
                if( !regex.test(key) ) {
                    theEvent.returnValue = false;
                    if(theEvent.preventDefault) theEvent.preventDefault();
                }
            }

            function createNewClass() {
                //validate
                var id = document.getElementById("txt-class-id").value;
                var name = document.getElementById("txt-class-name").value;
                var faculty = document.getElementById("txt-faculty").value;
                var course = document.getElementById("txt-course").value;
                var lecturer = document.getElementById("txt-lecturer").value;
                var numberStudent = document.getElementById("txt-slsv").value;
                
                if (id == null || id.length <=0
                    || name == null || name.length <=0
                    || faculty == null || faculty.length <=0
                    || course == null || course.length <=0
                    || lecturer == null || lecturer.length <=0
                    || numberStudent == null || numberStudent.length <=0) {
                    alert("Vui lòng nhập đầy đủ thông tin.");
                    return;
                }
                alert(id);
                var controller = "../../StudentClassController?action=create-class"
                        + "&id=" + id
                        + "&name=" + name
                        + "&faculty=" + faculty
                        + "&course=" + course
                        + "&lecturer=" + lecturer
                        + "&numberStudent=" + numberStudent;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = createNewClassHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
                
            }
            
            function createNewClassHandler() {
                //
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("message-handler");
                    detail.innerHTML = http.responseText;
                }
            }

            function changeFacultyUpdateLecturer() {
                var faculty = document.getElementById("txt-faculty").value; // Faculty's id
                var controller = "../../StudentClassController?action=change-faculty"
                        + "&faculty=" + faculty;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = changeFacultyHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }

            function changeFacultyHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("txt-lecturer");
                    detail.innerHTML = http.responseText;
                }
            }
            
            function deleteStudentClass(clazzID) {
                var controller = "../../StudentClassController?action=delete-student-class"
                        + "&clazzid=" + clazzID;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = deleteStudentClassHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function deleteStudentClassHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("msg-response");
                    detail.innerHTML = http.responseText;
                }
            }
            
        </script>
    </body>
</html>