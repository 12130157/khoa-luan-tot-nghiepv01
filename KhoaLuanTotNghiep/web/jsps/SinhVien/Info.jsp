<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Class"%>
<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
Student student=(Student) session.getAttribute("student");
Class classes=(Class) session.getAttribute("classes");
Faculty faculty=(Faculty)session.getAttribute("faculty");
Course course=(Course)session.getAttribute("course");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thông tin cá nhân</title>
        <style media="all" type="text/css">

            #formdetail table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #formdetail table th{
                background-color: #5F676D;
                height: 30px;
            }

            #formdetail table td{
                text-align: center;
                background-color: #5F676D;
            }
            #title{
                text-align: center;
            }
            #page{
                text-align: center;
            }
            a {
                 color: violet;
            }
           
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">            
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <u><h3><%=student.getFullName()%></h3></u>
                    <h3>Mã số sinh viên: <%=student.getId()%></h3>
                </div>
                <h3>Thông tin cá nhân:</h3>
                   <a href="../../AccountController?action=changeinfo">Cập nhật thông tin</a>
                 <hr/><hr/><br>
                <div id="NewsList">
                <form id="formdetail" name="formdetail">
                    <table>
                        <tr>
                            <td><i>Ngày sinh</i> </td>
                            <th><%=student.getBirthday()%></th>
                        </tr>  
                        <tr>
                            <td><i>Giới tính</i> </td>
                            <th><%=student.getGender()%></th>
                        </tr>
                        <tr>
                            <td><i>Lớp</i> </td>
                            <th><%=classes.getClassName()%></th>
                        </tr>
                        <tr>
                            <td><i>Khoa</i> </td>
                            <th><%=faculty.getFacultyName()%></th>
                        </tr>
                        <tr>
                            <td><i>Khóa</i> </td>
                            <th><%=course.getId()%></th>
                        </tr>
                        <tr>
                            <td><i>Bậc học</i> </td>
                            <th><%=student.getStudyLevel()%></th>
                        </tr>
                        <tr>
                            <td><i>Loại hình học</i> </td>
                            <th><%=student.getStudyType()%></th>
                        </tr>
                        <tr>
                            <td><i>Ngày nhập học</i> </td>
                            <th><%=student.getDateStart()%></th>
                        </tr>
                        <tr>
                            <td><i>CMND </i></td>
                            <th><%=student.getIdentityCard()%></th>
                        </tr>
                        <tr>
                            <td><i>Quê quán</i> </td>
                            <th><%=student.getHome()%></th>
                        </tr>
                        <tr>
                            <td><i>Đại chỉ liên lạc</i> </td>
                            <th><%=student.getAddress()%></th>
                        </tr>
                        <tr>
                            <td><i>Điện thoại</i> </td>
                            <th><%=student.getPhone()%></th>
                        </tr>
                        <tr>
                            <td><i>Email</i> </td>
                            <th><%=student.getEmail()%></th>
                        </tr>
                        <tr>
                            <td><i>Tình trạng</i> </td>
                            <th><%=student.getStatus()%></th>
                        </tr>
                                               
                    </table>
                </form>
               </div>      
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   
</html>