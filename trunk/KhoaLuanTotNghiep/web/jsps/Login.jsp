<%-- 
    Document   : Login
    Created on : Apr 23, 2011, 4:34:47 PM
    Author     : ngloc_it
--%>

<%@include file="Menu.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng Nhập</title>

        <style media="all" type="text/css">
            table{
                margin-left: 200px;
                margin-top: 10px;
                margin-bottom: 120px;
                padding-top: 10px;
                padding-left: 25px;
                padding-right: 25px;
                padding-bottom: 120px;
                border: 3px solid #5F676D;
                background: url("../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                width: 340px;
                height: 350px;
            }
            #login-submit{
                background-color: #ff3edf;
                width: 75px;
                border: 2px solid #175F6E;
            }
           </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <form name="myform" id="myform" method="post" action="../Login?function=login">
                    <table>
                        <tr>
                            <td>Tên Đăng Nhập</td>
                            <td>
                                <input type="text" name="txtUsername" id="txtUsername" />
                            </td>
                        </tr>
                        <tr>
                            <td>Mật Khẩu</td>
                            <td>
                                <input type="password" name="txtPassword" id="txtPassword"/>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="checkbox" name="chkRemember"/> Nhớ tên truy cập
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="button" style="color: #000000; font-style: oblique; font-size: 15px; font-variant: inherit" name="login-submit" onclick="Login()" value="Đăng Nhập"/>
                            </td>
                        </tr>
                        <%
                        String error = (String) session.getAttribute("error");
                            if (error != null) {
                                session.removeAttribute("error");
                        %>
                            <tr style="padding-top: 20px; background-color: #ff3edf">
                                <td colspan="2" align="center" style="font-size: 12px"> <%=error%></td>
                            </tr>
                        <%
                         }
                        %>
                        
                    </table>
                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    
    <script type = "text/javascript">
        function Login(){
            var txtUsername = document.myform.txtUsername.value;
            var txtPassword = document.myform.txtPassword.value;
            if(txtUsername.length==0){
                alert("Bạn chưa nhập tên người dùng");
                txtUsername.focus();
            }else if(txtPassword.length==0){
                alert("Bạn chưa nhập mật khẩu");
                txtPassword.focus();
            }
            else{
                document.forms["myform"].submit();
            }
        }
    </script>
</html>
