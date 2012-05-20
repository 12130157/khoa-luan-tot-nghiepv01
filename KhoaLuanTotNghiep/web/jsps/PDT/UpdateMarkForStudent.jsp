<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.DAO.SubjectDAO"%>
<%@page import="uit.cnpm02.dkhp.model.StudyResult"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Class"%>
<%@include file="MenuPDT.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
StudyResult result=(StudyResult) session.getAttribute("result");
Student student=(Student) session.getAttribute("student");
Class classes=(Class) session.getAttribute("classes");
Faculty faculty=(Faculty)session.getAttribute("faculty");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cập nhật điểm học tập</title>
        <style media="all" type="text/css">

            #formstudent{
                margin-left: 20px;
                margin-top: 10px;
                padding-top: 20px;
                padding-bottom: 20px;
                padding-right: 10px;
                padding-left: 10px;                                
                width: 100%;
            }
            #formstudent table{
                width: 100%;
                padding-left: 5px;
                padding-right: 5px;
            }
            #formstudent table td{
                width: 100px;
            }
            #formstudent table th{
                width: 200px;
                text-align: left;
            }
            #formdetail table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;

            }
            #formdetail table th{
                background-color:#175F6E;
                height: 30px;
                border-color: black;
            }

            #formdetail table td{
                text-align: center;
                background-color: #474C52;
                border-color: #7D8103;
            }

                
            #form-result{
                margin-left: 20px;
                margin-bottom: 20px;
                padding-top: 20px;
                padding-bottom: 20px;
                padding-right: 10px;
                padding-left: 10px;                
                border: 3px solid #7F38EC;
                width: 99%;
            }
            #info{
                width: 100%;
            }
            #formdetail{
                width: 99%;
            }
            #detail{
                width: 50%;
            }
             #detail th{
               text-align: center;
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
                <br>
                <h2 align="center"><u>CẬP NHẬT KẾT QUẢ HỌC TẬP CỦA SINH VIÊN</u></h2>
                <div>
                    <form action="" name="formstudent"  id="formstudent">
                    <table id="info">
                        <tr>
                            <td width="200px">MSSV: </td>
                            <th width="100px"><%=student.getId()%></th>
                            <td width="200px">Họ và tên: </td>
                            <th><%=student.getFullName()%></th>
                        </tr>
                        <tr>
                            <td width="200px">Lớp: </td>
                            <th width="100px"><%=classes.getClassName()%></th>
                            <td width="200px">Khoa: </td>
                            <th><%=faculty.getFacultyName()%></th>
                        </tr>
                        <tr>
                    </table>
                </form>
                </div>
               <hr/><hr/>
                 <form id="formdetail" name="formdetail">
                    <u>Chi tiết môn học</u>
                    <table id="detail" name="detail" border="1" >
                        <tr>
                            <td>Mã môn học: </td>
                            <th><%=result.getId().getSubjectCode()%></th>
                        </tr>
                        <tr>
                            <td>Môn học: </td>
                            <th><%=result.getSubjectName()%></th>
                        </tr>
                        <tr>
                            <td>Điểm hiện tại </td>
                            <th><%=result.getMark()%></th>
                        </tr>
                        <tr>
                            <td>Điểm cập nhật </td>
                            <th>
                                <select id="newMark" name="newMark">
                                    <%for(double i=0; i<=10; i= i+ 0.5){%>
                                    <option value="<%=i%>"><%=i%></option>
                                    <%}%>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <td>Cập nhật </td>
                            <th><input  type="button" value="Thay đổi" /></th>
                        </tr>
                    </table>
                 </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   <script  type = "text/javascript" >
        
   </script>
</html>
