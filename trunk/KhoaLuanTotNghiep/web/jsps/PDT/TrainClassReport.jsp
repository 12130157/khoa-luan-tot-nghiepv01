<%-- 
    Document   : TrainClassReport
    Created on : 01-05-2012, 08:49:24
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
  
    TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
    List<Student> students = (List<Student>) session.getAttribute("students");
    String key = trainClass.getId().getClassCode() + ";"
          + trainClass.getId().getYear() + ";"
          + trainClass.getId().getSemester();
%>
<html>
   <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết lớp học</title>
        <style media="all" type="text/css">
            #list-student {
                
            }
        </style>
    </head>
    <body>
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
                    Chi tiết lớp học:<b> <%= trainClass.getId().getClassCode()%>
                    <u> (<%=trainClass.getSubjectName()%>)</u></b>
                </div>
                <br />
                <div class="range">
                    <h3><span id="btn-trainclass-detail" class="atag">
                        Chi tiết lớp
                        </span></h3>
                    <div id="trainclass-detail" style="display: none;">
                        <ul>
                            <li><u>- Mã lớp học:</u> <b> <%= trainClass.getId().getClassCode() %> </b></li>
                            <li><u>- Học kỳ, năm học: </u> <b> <%= trainClass.getId().getSemester() +"/"%><%= trainClass.getId().getYear()%> </b></li>
                            <li><u>- Môn học:</u> <b> <%= trainClass.getSubjectName() %> (<%= " "+ trainClass.getSubjectCode() %>) </b></li>
                            <li><u>- Giảng viên:</u> <b> <%= trainClass.getLectturerName() + " (" %> <%= trainClass.getLecturerCode()%>) </b></li>
                            <li><u>- SLSV đăng ký:</u> <b> <%= trainClass.getNumOfStudentReg() %> </b></li>
                            <li><u>- SLSV tối đa:</u> <b> <%= trainClass.getNumOfStudent() %> </b></li>
                        </ul>
                    </div>
                </div>
                <%-- List student --%>
                <div class="range">
                    <h3><span id="btn-list-student" class="atag">
                        Danh sách SV
                    </span></h3>
                    <div id="list-student" style="display: none;">
                        <table class="general-table" style="width: 600px;">
                            <tr>
                            <th>STT</th>
                            <th>MSSV</th>
                            <th>Họ Tên</th>
                            <th>Lớp</th>
                            </tr>
                            <%
                                for (int i = 0; i < students.size(); i++) {
                            %>
                            <tr>
                            <td> <%= (i + 1)%> </td>
                            <td> <%= students.get(i).getId()%> </td>
                            <td> <%= students.get(i).getFullName()%> </td>
                            <td> <%= students.get(i).getClassCode()%> </td>
                            </tr>
                            <%
                                }
                            %>
                        </table>
                        <div>
                            <%--<input type="button" value=" Back " onclick="" />--%>
                            <a href="../../DownloadController?action=download-list-student-in-trainclass&key=<%= key%>"
                               class="atag" style="margin-left: 500px;">
                                <img src="../../imgs/download.png" title="download"/>Download
                            </a>
                        </div>
                    </div>
                </div>
                
            </div><!--End Contents-->

            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <script  type = "text/javascript" >

        $("#btn-trainclass-detail").click(function () {
            $('#trainclass-detail').slideToggle(500);
        });
        
        $("#btn-list-student").click(function () {
            $('#list-student').slideToggle(500);
        });
    </script>
   
</html>
