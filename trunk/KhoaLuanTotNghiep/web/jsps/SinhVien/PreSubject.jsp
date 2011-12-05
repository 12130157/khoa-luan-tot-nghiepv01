<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>


<%@page import="uit.cnpm02.dkhp.model.PreSubject"%>
<%@page import="java.util.List"%>
<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
List<PreSubject> preSub=(List<PreSubject>) session.getAttribute("preSub");
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
                    <p><h1><b><u>Quy định về môn học tiên quyết</u></b></h1></p>
                </div><br>
                <h1>Sinh viên muốn đăng ký học môn học bất kỳ thì điều kiện bắt buộc là sinh viên đã hoàn tất các môn học tiên quyết của môn học đó.</h1>
                <br><hr/><hr/>
                <u>Chi tiết các môn học tiên quyết:</u>
                
                <div id="NewsList">
                <form id="formdetail" name="formdetail">
                    <table>
                        <tr>
                            <th>Môn học</th>
                            <th>Môn học tiên quyết</th>
                        </tr> 
                        <%
                        for(int i=0;i<preSub.size();i++){
                            %>
                            <tr>
                                <td><%=preSub.get(i).getSubjectName()%></td>
                                <td><%=preSub.get(i).getPreSubjectName()%></td>
                            </tr>
                            <%
                        }
                        %>
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