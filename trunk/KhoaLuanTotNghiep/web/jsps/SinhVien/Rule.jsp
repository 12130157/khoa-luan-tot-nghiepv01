<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>


<%@page import="uit.cnpm02.dkhp.model.Rule"%>
<%@page import="java.util.List"%>
<%@include file="MenuSV.jsp"%>
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
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <p><h1><b><u>Quy định trong việc đăng ký học phần</u></b></h1></p>
                </div><br>
                <br>
                <hr/><hr/><br>
                <div id="NewsList">
                <form id="formdetail" name="formdetail">
                   <table>
                        <tr>
                            <td>Số tín chỉ tối thiểu có thể đăng ký:</td>
                            <th><%=(int)rule.get(4).getValue()%> </th>
                        </tr> 
                         <tr>
                            <td>Số tín chỉ tối đa có thể đăng ký:</td>
                            <th><%=(int)rule.get(3).getValue()%> </th>
                        </tr>   
                         <tr>
                            <td>Số sinh viên tối thiểu để mở lớp:</td>
                            <th><%=(int)rule.get(2).getValue()%> </th>
                        </tr> 
                         <tr>
                            <td>Số sinh viên tối đa cho một lớp:</td>
                            <th><%=(int)rule.get(1).getValue()%> </th>
                        </tr>       
                         <tr>
                            <td>Điểm để qua môn học:</td>
                            <th><%=rule.get(0).getValue()%> </th>
                        </tr> 
                    </table>
                </form>
               </div>      
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   
</html>