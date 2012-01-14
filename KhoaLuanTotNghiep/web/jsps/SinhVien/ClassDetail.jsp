<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@include file="MenuSV.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
TrainClass trainClass=(TrainClass) session.getAttribute("classInfo");
List<Student> studentList =(List<Student>) session.getAttribute("studentList");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chiết tiết lớp học</title>
        <style media="all" type="text/css">

            #classDetail{
                margin-left: 20px;
                margin-top: 10px;
                padding-top: 20px;
                padding-bottom: 20px;
                padding-right: 10px;
                padding-left: 10px;                                
                width: 100%;
            }
            #classDetail table{
                width: 100%;
                padding-left: 5px;
                padding-right: 5px;
            }
            #classDetail table td{
                width: 100px;
            }
            #classDetail table th{
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
                text-align: center;
            }

            #formdetail table td{
                text-align: center;
                background-color: #474C52;
                border-color: #7D8103;
            }

           
            #info{
                width: 100%;
            }
                
          
        </style>
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
                <h2 align="center"><u>DANH SÁCH ĐĂNG KÝ LỚP HỌC</u></h2>
                <div>
                    <form action="" name="classDetail"  id="classDetail">
                        <table id="info">
                        <tr>
                            <td width="200px">Mã lớp: </td>
                            <th width="100px"><%=trainClass.getId().getClassCode()%></th>
                            <td width="200px">Môn học: </td>
                            <th><%=trainClass.getSubjectName()%></th>
                        </tr>
                        <tr>
                            <td width="200px">Giảng viên:  </td>
                            <th width="100px"><%=trainClass.getLectturerName()%></th>
                            <td width="200px">Ngày: </td>
                            <th>Thứ <%=trainClass.getStudyDate()%></th>
                        </tr>
                        <tr>
                            <td width="200px">Ca học: </td>
                            <%if(trainClass.getShift()==1){%>
                            <th width="100px">Sáng</th>
                            <%}else{%>
                            <th width="100px">Chiều</th>
                            <%}%>
                            <td width="200px">Phòng học:</td>
                            <th><%=trainClass.getClassRoom()%></th>
                        </tr>
                        
                    </table>
                </form>
                </div>
               <hr/><hr/>
                 <form id="formdetail" name="formdetail">
                     <b>Danh sách các sinh viên đăng ký: <%=studentList.size()%> sinh viên</b>
                    <table id="detail" name="detail" border="1" bordercolor="yellow" >
                     <tr>
                            <th width="20px">STT</th><th width="100px">MSSV</th><th width="200px">Họ tên</th><th width="100px">Lớp</th>
                     </tr>   
                     <%for(int i=0; i< studentList.size();i++){%>
                     <tr>
                         <td><%=i+1%></td>
                         <td><%=studentList.get(i).getId()%></td>
                         <td><%=studentList.get(i).getFullName()%></td>
                         <td><%=studentList.get(i).getClassCode()%></td>
                     </tr>
                     <%}%>
                    </table>
                 </form>
                    <br><br>        
            </div><!--End Contents-->
            
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   
</html>
