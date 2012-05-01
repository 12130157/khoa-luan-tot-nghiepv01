<%-- 
    Document   : TrainClassReport
    Created on : 01-05-2012, 08:49:24
    Author     : LocNguyen
--%>

<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
  TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
  List<Student> students = (List<Student>) session.getAttribute("students");
%>
<html>
   <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết lớp học</title>
        <style media="all" type="text/css">

            #title{
                background-color: #2f4e3d;
                text-align: center;
                padding-top: 12px;
                padding-bottom: 10px;
            }
            table {
                /*width: 100%;*/
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            table th {
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            table td {
                text-align: center;
                background-color: #5F676D;
            }
            #list-student {
                
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

                <hr/><hr/>
                <div id="title">
                    <u><h3><b>Chi tiết lớp học: <%= trainClass.getId().getClassCode()%></b></h3></u>
                    <u> <%=trainClass.getSubjectName()%></u>
                </div>
                <hr/><hr/><br>

                <div id="list-student">
                    <table>
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
                                <td> <%= students.get(i).getFullName() %> </td>
                                <td> <%= students.get(i).getClassCode() %> </td>
                            </tr>
                        <%
                            }
                        %>
                    </table>
                    <div>
                        <input type="button" value=" Back " onclick="" />
                        <a href="#"> Tải file </a>
                    </div>
                </div>
                <form id="list-student" action="../../ReportController?action=list-student&classId=<%=trainClass.getId().getClassCode()%>" method="post" >
                  

                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   
</html>
