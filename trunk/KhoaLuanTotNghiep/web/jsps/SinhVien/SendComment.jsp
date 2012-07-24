<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);
    
    Student student = (Student) session.getAttribute("student");
%>
<html>
    <head>
        <link href="../../csss/general.css.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/comment.css" rel="stylesheet" type="text/css" media="screen">
        <link rel="stylesheet" type="text/css" href="clientscript/editor.css">
        <meta http-equiv="Pragma" content="No-cache">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="Description" content="">
        <meta name="keywords" content="">
        <title>Ý kiến</title>
        <link rel="stylesheet" type="text/css" href="clientscript/editor.css">
        <script type="text/javascript" src="clientscript/editor.js"></script>
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
            <%@include file="MenuSV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Trang gửi ý kiến của bạn
                </div>
                <br /><br />
                <form id="myform" name="myform" method="post" action="../../StudentCommentController?action=complete">
                    <div id="Info">
                        <table>
                            <tr>
                                <td width="70px">Họ Tên:</td>
                                <td><input type="text" readonly name="txtName" id="txtName" value="<%=student.getFullName()%>"></td>
                            </tr>
                            <tr>
                                <td>Email:</td>
                                <td><input type="text" readonly name="txtEmail" id="txtEmail" value="<%=student.getEmail()%>"></td>
                            </tr>
                        </table>
                    </div>
                    <table id="editor" width="50%" height="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                            <td colspan="2" align="center" bgcolor="#ffffff">
                                <span id="postArea"><br><br><br><br> Loading...</span>	
                                <script type="text/javascript">
                                    RTE=new Editor('RTE','postArea','',600, 350);
                                    RTE.display();
                                </script>
                                <script type="text/javascript" src="clientscript/avim.js"></script>
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" id="content" name="content" value="">
            </form>
                
        </div><!--End Contents-->

        <div id="footer"><!--Footer-->
            <%@include file="../Footer.jsp" %>
        </div><!--End footer-->
    </div>
    <!--End Wrapper-->
    </body>
</html>