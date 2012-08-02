<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
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

    Boolean inTimeRegistry =(Boolean)session.getAttribute("inTimeRegistry");
    List<TrainClass> registried=(List<TrainClass>) session.getAttribute("registried");
    String year=(String) session.getAttribute("year");
    Integer semester=(Integer) session.getAttribute("semester");
    Student student=(Student) session.getAttribute("student");
    Class classes=(Class) session.getAttribute("classes");
    Faculty faculty=(Faculty)session.getAttribute("faculty");
    boolean nonStudy=(Boolean) session.getAttribute("non-study-student");
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
            #formdetail{
                width: 99%;
            }
            #left{
                float: left;
            }
            #right{
                float: right;
                margin-right: 10px;
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
                <br />
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
                 <form id="formdetail" name="formdetail">
                    <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                        Chi tiết
                    </div>
                    <table id="detail" name="detail" class="general-table" >
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
                         <td><a href="../../RegistryController?action=detail&classCode=<%=registried.get(i).getId().getClassCode()%> &semester=<%=registried.get(i).getId().getSemester()%>&year=<%=registried.get(i).getId().getYear()%>"><%=registried.get(i).getId().getClassCode()%></a></td>
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
                <div>
                    <%
                    if (nonStudy) {
                        inTimeRegistry = false;
                    %>
                        <div class="clear"></div>
                        <b>Sinh viên <b><%=" " + student.getFullName() + " "%></b> đang tạm thời bị đình chỉ học tập không được phép đăng ký học phần</b> 
                    <%}%>
                    <%if(inTimeRegistry){%>
                    <div id="left">
                        <a href="../../RegistryController?action=reRegistry" class="button-1" style="text-decoration: none;">Đăng ký lại</a>
                    </div>
                    <%}%>
                    <div id="right">
                    <a class="button-1" href="../../DownloadController?action=studentRegistry&mssv=<%=student.getId()%>" style="text-decoration: none;"><img src="../../imgs/download.png"/> Tải file</a>
                    </div>
                </div>
                <br /> <br />
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script>
        
    </script>
    </body>
 </html>
