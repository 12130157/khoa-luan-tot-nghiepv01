<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>


<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.PreSubject"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);

    List<PreSubject> preSub=(List<PreSubject>) session.getAttribute("preSub");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Môn học tiên quyết</title>
        <style media="all" type="text/css">

            <%-- CSS definition --%>
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
                    Quy định về môn học tiên quyết
                </div>
                <div class="clear"></div> <br />
                
                <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                    (*) Sinh viên muốn đăng ký học môn học bất kỳ thì điều kiện bắt buộc là sinh viên đã hoàn tất các môn học tiên quyết của môn học đó.
                </div>
                <br><hr/><hr/>
                <div style="font-size: 12px; font-weight: inherit;"><b><u>Chi tiết các môn học tiên quyết</u></b></div>
                <table class="general-table" style="width: 625px;">
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
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"></script>
    </body>
   
</html>