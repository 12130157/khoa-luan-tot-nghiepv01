<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page import="java.util.List"%>
<%@include file="MenuSV.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
String year=(String) session.getAttribute("year");
Integer semester=(Integer) session.getAttribute("semester");
 List<TrainClass> monday=(List<TrainClass>)session.getAttribute("monday");
 List<TrainClass> tuesday=(List<TrainClass>)session.getAttribute("tuesday");
 List<TrainClass> wednesday=(List<TrainClass>)session.getAttribute("wednesday");
 List<TrainClass> thursday=(List<TrainClass>)session.getAttribute("thursday");
 List<TrainClass> friday=(List<TrainClass>)session.getAttribute("friday");
 List<TrainClass> saturday=(List<TrainClass>)session.getAttribute("saturday");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thời khóa biểu</title>
        <style media="all" type="text/css">

           
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
            #formdetail{
                width: 99%;
            }
            #detail{
                width: 90%;
            }
             #detail th{
               text-align: center;
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
                <h2 align="center"><u>Thời khóa biểu học kỳ <%=semester%> năm học <%=year%> </u></h2>
                <br><hr/><hr/>
                 <form id="formdetail" name="formdetail">
                    <u>Chi tiết</u>
                    <table id="detail" name="detail" border="1" bordercolor="yellow" >
                     <tr>
                         <th width="50px"></th><th width="50px">Mã lớp</th><th width="200px">Môn học</th><th width="50px">Phòng</th><th width="50px">Buổi</th><th width="200px">Giảng viên</th>
                     </tr> 
                     <tr>
                         <th><b><u>Thứ 2:</u></b></th><th></th><th></th><th></th><th></th><th></th>
                     </tr>   
                     <%
                     for(int i=0; i<monday.size();i++){%>
                     <tr>
                         <td></td>
                         <td><%=monday.get(i).getId().getClassCode()%></td>
                         <td><%=monday.get(i).getSubjectName()%></td>
                         <td><%=monday.get(i).getClassRoom()%></td>
                         <%if(monday.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=monday.get(i).getLectturerName()%></td>
                     </tr> 
                     <%
                      }
                     %>
                      <tr>
                         <th><b><u>Thứ 3:</u></b></th><th></th><th></th><th></th><th></th><th></th>
                     </tr>   
                     <%
                     for(int i=0; i<tuesday.size();i++){%>
                     <tr>
                         <td></td>
                         <td><%=tuesday.get(i).getId().getClassCode()%></td>
                         <td><%=tuesday.get(i).getSubjectName()%></td>
                         <td><%=tuesday.get(i).getClassRoom()%></td>
                         <%if(tuesday.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=tuesday.get(i).getLectturerName()%></td>
                     </tr> 
                     <%
                      }
                     %>
                      <tr>
                         <th><b><u>Thứ 4:</u></b></th><th></th><th></th><th></th><th></th><th></th>
                     </tr>   
                     <%
                     for(int i=0; i<wednesday.size();i++){%>
                     <tr>
                         <td></td>
                         <td><%=wednesday.get(i).getId().getClassCode()%></td>
                         <td><%=wednesday.get(i).getSubjectName()%></td>
                         <td><%=wednesday.get(i).getClassRoom()%></td>
                         <%if(wednesday.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=wednesday.get(i).getLectturerName()%></td>
                     </tr> 
                     <%
                      }
                     %>
                     <tr>
                         <th><b><u>Thứ 5:</u></b></th><th></th><th></th><th></th><th></th><th></th>
                     </tr>   
                     <%
                     for(int i=0; i<thursday.size();i++){%>
                     <tr>
                         <td></td>
                         <td><%=thursday.get(i).getId().getClassCode()%></td>
                         <td><%=thursday.get(i).getSubjectName()%></td>
                         <td><%=thursday.get(i).getClassRoom()%></td>
                         <%if(thursday.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=thursday.get(i).getLectturerName()%></td>
                     </tr> 
                     <%
                      }
                     %>
                     <tr>
                         <th><b><u>Thứ 6:</u></b></th><th></th><th></th><th></th><th></th><th></th>
                     </tr>   
                     <%
                     for(int i=0; i<friday.size();i++){%>
                     <tr>
                         <td></td>
                         <td><%=friday.get(i).getId().getClassCode()%></td>
                         <td><%=friday.get(i).getSubjectName()%></td>
                         <td><%=friday.get(i).getClassRoom()%></td>
                         <%if(friday.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=friday.get(i).getLectturerName()%></td>
                     </tr> 
                     <%
                      }
                     %>
                      <tr>
                         <th><b><u>Thứ 7:</u></b></th><th></th><th></th><th></th><th></th><th></th>
                     </tr>   
                     <%
                     for(int i=0; i<saturday.size();i++){%>
                     <tr>
                         <td></td>
                         <td><%=saturday.get(i).getId().getClassCode()%></td>
                         <td><%=saturday.get(i).getSubjectName()%></td>
                         <td><%=saturday.get(i).getClassRoom()%></td>
                         <%if(saturday.get(i).getShift()==1){%>
                         <td>Sáng</td>
                         <%}else{%>
                         <td>Chiều</td>
                         <%}%>
                         <td><%=saturday.get(i).getLectturerName()%></td>
                     </tr> 
                     <%
                      }
                     %>
                    </table>
                 </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    
</html>
