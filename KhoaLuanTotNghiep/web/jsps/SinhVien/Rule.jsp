<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>


<%@page import="uit.cnpm02.dkhp.model.Rule"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
List<Rule> rule=(List<Rule>) session.getAttribute("rule");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quy định</title>
        <style media="all" type="text/css">

            #formdetail table{
                width: 800px;
                padding-left: 220px;
                padding-right: 10px;
                text-align: center;
                

            }
            #formdetail table th{
                background-color: #5F676D;
                height: 30px;
                font-weight: bold;
                color: purple;
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
            <%@include file="MenuSV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Quy định trong việc đăng ký học phần
                </div>
                <br /><br />
                <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                    Một số quy định về việc đăng ký học phần đối với sinh viên trong mỗi học kỳ
                </div>
                
               <table class="general-table" style="width: 320px;">
                    <tr>
                        <td>Số tín chỉ tối thiểu có thể đăng ký</td>
                        <td><%=(int)rule.get(5).getValue()%> </td>
                    </tr> 
                     <tr>
                        <td>Số tín chỉ tối đa có thể đăng ký</td>
                        <td><%=(int)rule.get(4).getValue()%> </td>
                    </tr>  
                    <tr>
                        <td>Số tín chỉ bắt buộc tối thiếu</td>
                        <td><%=(int)rule.get(3).getValue()%> </td>
                    </tr>  
                     <tr>
                        <td>Số sinh viên tối thiểu để mở lớp</td>
                        <td><%=(int)rule.get(2).getValue()%> </td>
                    </tr> 
                     <tr>
                        <td>Số sinh viên tối đa cho một lớp</td>
                        <td><%=(int)rule.get(1).getValue()%> </td>
                    </tr>       
                     <tr>
                        <td>Điểm để qua môn học</td>
                        <td><%=rule.get(0).getValue()%> </td>
                    </tr> 
                </table>
                <br/>
            </div><!--End Contents-->
            
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   
</html>