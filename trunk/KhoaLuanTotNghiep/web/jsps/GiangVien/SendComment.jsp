<%-- 
    Document   : SendComment
    Created on : 09-08-2012, 22:15:05
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.LECTUTER, session, response);
    
    Lecturer lecturer = (Lecturer) session.getAttribute("lecturer");
    String msg = (String) session.getAttribute("msg");
    session.removeAttribute("msg");
%>

<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link rel="stylesheet" type="text/css" href="../../editor/editor.css">
        <meta http-equiv="Pragma" content="No-cache">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="Description" content="">
        <meta name="keywords" content="">
        <script type="text/javascript" src="../../editor/editor.js"></script>
        <title>Ý kiến</title>
       <style media="all" type="text/css">
            #editor{
                padding-left: 150px;
            }
            #Info{
                padding-left: 150px;
                width: 500px;
            }
            #viettype{
                background-color: aquamarine;
            }
       </style>
    </head>
 
    <body>
       <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuGV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Trang gửi ý kiến của bạn
                </div>
                <br /><br />
                <form id="myform" name="myform" method="post" action="../../LecturerPrivateController?function=submit-sent-comment">
                    <div id="Info">
                        <table>
                            <tr>
                                <td width="70px">Họ Tên:</td>
                                <td><input type="text" readonly name="txtName" id="txtName" value="<%=lecturer.getFullName()%>"></td>
                            </tr>
                            <tr>
                                <td>Email:</td>
                                <td><input type="text" readonly name="txtEmail" id="txtEmail" value="<%=lecturer.getEmail()%>"></td>
                            </tr>
                        </table>
                    </div>
                    <table id="editor" width="50%" height="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                            <td colspan="2" align="center">
                                <span id="postArea"><br><br><br><br> Loading...</span>	
                                <script type="text/javascript">
                                    RTE=new Editor('RTE','postArea','',600, 350);
                                    RTE.display();
                                </script>
                                <script type="text/javascript" src="../../editor/avim.js"></script>
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" id="content" name="content" value="">
            </form>
            <div class="clear"></div>
            <div <% if (msg != null && !msg.isEmpty()) {%> class="msg-response"<%}%> style="text-align: center; display: block;">
                <% if (msg != null && !msg.isEmpty()) {%> 
                    <%= msg %>
                <%}%>
            </div>
            <br />
        </div><!--End Contents-->

        <div id="footer"><!--Footer-->
            <%@include file="../Footer.jsp" %>
        </div><!--End footer-->
    </div>
    <!--End Wrapper-->
    </body>
</html>