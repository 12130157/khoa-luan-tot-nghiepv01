<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page import="uit.cnpm02.dkhp.DAO.SubjectDAO"%>
<%@page import="uit.cnpm02.dkhp.model.StudyResult"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Class"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);

    List<TrainClass> registried=(List<TrainClass>) session.getAttribute("registriedClass");
    String year=(String) session.getAttribute("year");
    Integer semester=(Integer) session.getAttribute("semester");
    Student student=(Student) session.getAttribute("student");
    Class classes=(Class) session.getAttribute("classes");
    Faculty faculty=(Faculty)session.getAttribute("faculty");
    int numTC=0;    
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký môn học</title>
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
            
            #info{
                width: 100%;
            }
            #message{
                text-align: center;
            }
            h1{
                 color: #0000ff;
            }
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuSV.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">Đăng ký học phần hoc kỳ <%=semester%> năm học <%=year%></div>
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
                    </table>
                </form>
                </div>
               <hr/><hr/>
                      <%
                        String error = (String) session.getAttribute("error");
                            if (error != null) {
                                session.removeAttribute("error");
                        %>
                        <br>
                        <div id="message">
                            <h1> <%=error%></h1>
                        </div>
                        <br />
                        <%
                         }
                        %>
               <form id="completeRegistry" name="completeRegistry" action="../../RegistryController?action=completeRegistry" method="post">
                   <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                        Chi tiết
                    </div>
                    <table id="detail" class="general-table" name="detail" style="width: auto;" >
                     <tr>
                         <th width="10px">STT</th><th width="70px">Mã lớp</th><th width="200px">Môn học</th>
                         <th width="200px">Giảng viên</th><th width="10px">Thứ</th>
                         <th width="50px">Ca</th><th width="50px">Phòng</th>
                         <th width="10px">TC</th>
                     </tr>   
                     <%for(int i=0; i<registried.size();i++){
                         numTC+=registried.get(i).getNumTC();
                      %>
                     <tr>
                         <td><%=i+1%></td>
                         <td><a href="../../RegistryController?action=detail&classCode=<%=registried.get(i).getId().getClassCode()%>"><%=registried.get(i).getId().getClassCode()%></a></td>
                         <td><%=registried.get(i).getSubjectName()%></td>
                         <td><%=registried.get(i).getLectturerName()%></td>
                         <td><%=registried.get(i).getStudyDate()%></td>
                         <%if(registried.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=registried.get(i).getClassRoom()%></td>
                         <td><%=registried.get(i).getNumTC()%></td>
                      </tr>
                     <%}%>
                     <tr>
                         <td></td><td></td><td></td>
                         <td><u><b>Tổng số tín chỉ:</b></u></td><td></td>
                         <td></td><td></td>
                         <th><%=numTC%></th> 
                     </tr>
                     </table>
                 </form>
                 <br />
                <form action="../../RegistryController?action=reRegistry" method="post" id="reRegistry" name="reRegistry">
                    <div style="margin-left: 30%;">
                        <input type="button" class="button-1" value="Đăng ký lại" onclick="reRegistration()"/>
                        <input type="button" class="button-1" value="Hoàn tất đăng ký" onclick="completeRegistration()"/>
                    </div>
                </form>
                <br />
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
        <script  type = "text/javascript" >
            function reRegistration(){
                document.forms["reRegistry"].submit();
            }
            function completeRegistration(){
                document.forms["completeRegistry"].submit();
            }
        </script>
    </body>
 </html>
