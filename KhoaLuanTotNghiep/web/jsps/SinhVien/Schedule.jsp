<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@include file="MenuSV.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
String year=(String) session.getAttribute("year");
Integer semester=(Integer) session.getAttribute("semester");
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
                            <th width="100px">Năm học</th><th width="70px">Học kỳ</th><th width="100px">Mã môn</th><th width="300px">Tên môn học</th><th width="70px">Số TC</th><th width="80px">Điểm</th><th width="100px">Nhân hệ số</th>
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
