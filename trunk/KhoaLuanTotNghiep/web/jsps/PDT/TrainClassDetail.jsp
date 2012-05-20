<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết lớp học</title>
        <style media="all" type="text/css">

            #table_mh{
                padding-left: 100px;
                padding-right: 10px;
                text-align: left;

            }
            #table th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #table td{
                text-align: center;
                background-color: #5F676D;
                
            }
            #title{
                background-color: #2f4e3d;
                text-align: center;
                padding-top: 12px;
                padding-bottom: 10px;
            }
            #page{
                text-align: center;
            }
            #sidebar {
                height:250px;
                overflow:auto;
            }
            a {
                color: violet;
            }

            #error_code, #error_name, #error_tclt, #error_tcth {
                font-size: 10px;
                color: #cc0033;
            }
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

                <hr/><hr/>
                <div id="title">
                    <u><h3><b>Chi tiết lớp học: <%=trainClass.getId().getClassCode()%></b></h3></u>
                </div>
                <hr/><hr/><br>

                <form id="classDetail" action="../../ManageClassController?action=pre_update&classId=<%=trainClass.getId().getClassCode()%>&year=<%= trainClass.getId().getYear()%>&semester=<%= trainClass.getId().getSemester()%>" method="post" >
                   <table id="table_mh">
                        <tr>
                            <td width="200px"> Mã lớp: </td>
                            <th> <%=trainClass.getId().getClassCode()%> </th>
                        </tr>
                        <tr>
                            <td> Môn học: </td>
                            <th><%=trainClass.getSubjectName()%> </th>
                        </tr>
                        <tr>
                            <td> Giảng viên: </td>
                            <th><%=trainClass.getLectturerName()%></th>
                        </tr>
                        <tr>
                            <td> Đã đăng ký </td>
                            <th> <%=trainClass.getNumOfStudentReg()+" / "+trainClass.getNumOfStudent()%> </th>
                        </tr>
                        <tr>
                            <td> Ngày học: </td>
                            <th><%=trainClass.getStudyDate()%></th>
                        </tr>
                        <tr>
                            <td> Ca học: </td>
                            <%if(trainClass.getShift()==1){%>
                            <th>Sáng</th>
                            <%}else {%>
                            <th>Chiều</th>
                            <%}%>
                        </tr>
                        <tr>
                            <td>Phòng học:</td>
                            <th> <%=trainClass.getClassRoom()%></th>
                        </tr>
                        <tr>
                            <td>Ngày thi:</td>
                            <%if(trainClass.getTestDate() == null ){%>
                            <th>Chưa có</th>
                            <%}else {%>
                            <th><%=DateTimeUtil.format(trainClass.getTestDate())%></th>
                            <%}%>
                        </tr>
                        <tr>
                            <td>Phòng thi:</td>
                            <%if(trainClass.getTestRoom() == null || trainClass.getTestRoom().isEmpty() ){%>
                            <th>Chưa có</th>
                            <%}else {%>
                            <th><%=trainClass.getTestRoom()%></th>
                            <%}%>
                        </tr>
                        <tr>
                            <td>Ca thi:</td>
                            <%if(trainClass.getTestHours() == null || trainClass.getTestHours().isEmpty()){%>
                            <th>Chưa có</th>
                            <%}else {%>
                            <th><%=trainClass.getTestHours()%></th>
                            <%}%>
                        </tr>
                        <tr>
                            <td>Ngày bắt đầu học:</td>
                            <th> <%=DateTimeUtil.format(trainClass.getStartDate())%></th>
                        </tr>
                        <tr>
                            <td>Ngày kết thúc dự kiến:</td>
                            <th> <%=DateTimeUtil.format(trainClass.getEndDate())%></th>
                        </tr>
                        <tr>
                             <td></td>
                             <td>
                             <input type="submit" id="update" name="update" value="  Cập nhật  "/> 
                             </td>
                        </tr>
                    </table>
                        <div id="message"></div>
                </form>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
</html>
