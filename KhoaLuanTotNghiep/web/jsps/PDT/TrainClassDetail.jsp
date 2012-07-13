<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
  List<Student> students
            = (List<Student>) session.getAttribute("students");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết lớp học</title>
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
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Chi tiết lớp học: <%=trainClass.getId().getClassCode()%>
                </div>
                <br /><br />
                <div class="range">
                    <h3><span id="btn-trainclass-information" class="atag">Thông tin lớp học</span></h3> 
                    <div id="trainclass-information" class="div-range">
                    <form id="classDetail" action="../../ManageClassController?action=pre_update&classId=<%=trainClass.getId().getClassCode()%>&year=<%= trainClass.getId().getYear()%>&semester=<%= trainClass.getId().getSemester()%>" method="post" >
                       <table class="general-table">
                            <tr>
                                <td width="200px"> Mã lớp: </td>
                                <td> <%=trainClass.getId().getClassCode()%> </td>
                                <td> Môn học: </td>
                                <td><%=trainClass.getSubjectName()%> </td>
                            </tr>
                            <tr>
                                <td> Giảng viên: </td>
                                <td><%=trainClass.getLectturerName()%></td>
                                <td> Đã đăng ký </td>
                                <td> <%=trainClass.getNumOfStudentReg()+" / "+trainClass.getNumOfStudent()%> </td>
                            </tr>
                            <tr>
                                <td> Ngày học: </td>
                                <td><%=trainClass.getStudyDate()%></td>
                                <td> Ca học: </td>
                                <%if(trainClass.getShift()==1){%>
                                <td>Sáng</td>
                                <%}else {%>
                                <td>Chiều</td>
                                <%}%>
                            </tr>
                            <tr>
                                <td>Phòng học:</td>
                                <td> <%=trainClass.getClassRoom()%></td>
                                <td>Ngày thi:</td>
                                <%if(trainClass.getTestDate() == null ){%>
                                <td>Chưa có</td>
                                <%}else {%>
                                <td><%=DateTimeUtil.format(trainClass.getTestDate())%></td>
                                <%}%>
                            </tr>
                            <tr>
                                <td>Phòng thi:</td>
                                <%if(trainClass.getTestRoom() == null || trainClass.getTestRoom().isEmpty() ){%>
                                <td>Chưa có</td>
                                <%}else {%>
                                <td><%=trainClass.getTestRoom()%></td>
                                <%}%>
                                <td>Ca thi:</td>
                                <%if(trainClass.getTestHours() == null || trainClass.getTestHours().isEmpty()){%>
                                <td>Chưa có</td>
                                <%}else {%>
                                <td><%=trainClass.getTestHours()%></td>
                                <%}%>
                            </tr>
                            <tr>
                                <td>Ngày bắt đầu học:</td>
                                <td> <%=DateTimeUtil.format(trainClass.getStartDate())%></td>
                                <td>Ngày kết thúc dự kiến:</td>
                                <td> <%=DateTimeUtil.format(trainClass.getEndDate())%></td>
                            </tr>
                        </table>
                        <input type="submit" id="submit" name="update" value="Cập nhật"/> 
                        <div id="message"></div>
                    </form>
                    </div>
                </div>
                <%-- Danh sach SV --%>
                <br />
                <div class="range">
                    <h3><span id="btn-list-student" class="atag">Danh sách SV</span></h3> 
                    <div id="list-student" style="display: none;" class="div-range">
                        <%-- Danh sach cac lop chua cap nhat diem J --%>
                        <%if (students == null || students.isEmpty()) {%>
                            <i>Không có SV</i>
                        <%} else {
                            int counter = 1;
                        %>
                        <table class="general-table" style="width: 720px;">
                                <tr>
                                    <th>STT</th>
                                    <th>MSSV</th>
                                    <th>Họ và tên</th>
                                    <th>Lớp</th>
                                    <th>Khoa</th>
                                </tr>
                                <%for (Student s : students) {%>
                                    <tr>
                                        <td><%= counter++ %></td>
                                        <td><%= s.getId() %></td>
                                        <td><%= s.getFullName() %></td>
                                        <td><%= s.getClassCode() %></td>
                                        <td><%= s.getCourseCode() %></td>
                                    </tr>
                                <%}%>
                            </table>
                        <%}%>
                    </div>
                </div>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div> <!--Wrapper-->
        <!--End Wrapper-->
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            //
            // Manual input process...
            //
            $("#btn-trainclass-information").click(function () {
                $('#trainclass-information').slideToggle(500);
            });
            $("#btn-list-student").click(function () {
                $('#list-student').slideToggle(500);
            });
            //
        </script>
    </body>
</html>