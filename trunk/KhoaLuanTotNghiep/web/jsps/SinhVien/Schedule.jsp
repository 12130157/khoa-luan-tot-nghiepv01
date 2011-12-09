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
                            <th width="100px"><b><u>Thứ 2:</u></b></th>
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
    
</html>
