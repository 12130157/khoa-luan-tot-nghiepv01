<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@include file="MenuSV.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<%
    String pass = (String) session.getAttribute("password");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đổi mật khẩu</title>
        <style media="all" type="text/css">
            #frmimg{
                margin-left: 50px;
                border: 5px solid #98AFC7;
            }
             #title{
                text-align: center;
            }
            #message{
                text-align: center;
                font-weight: bold;
                color: red;
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
                    <u><h3>Đổi mật khẩu</h3></u>
                </div>
                 <hr/><hr/><br>
                <div>
                    <form id="changepast" name="changepast" action="../../AccountController?action=changePass" method="post">
                     <%
                        String message = (String) session.getAttribute("message");
                            if (message != null) {
                                session.removeAttribute("message");
                        %>
                        <p id="message"> <%=message%></p><br>
                        <%
                         }
                        %>
                        <table id="past" name="past">
                        <tr>
                            <td>Mật khẩu hiện tại:</td>
                            <td><input type="password" name="oldPass" id="oldPass"/></td>
                        </tr>
                        <tr>
                            <td>Mật khẩu hiện tại:</td>
                            <td><input type="password" name="newpass" id="newpass"/></td>
                        </tr>
                        <tr>
                            <td>Mật khẩu hiện tại:</td>
                            <td><input type="password" name="confirmPass" id="confirmPass"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="button" name="OK" id="OK" onclick="Change()" value="       OK       "/>
                                <input type="button" name="Can" id="Can" onclick="Cancel();" value="     Đóng     "/>
                            </td>
                        </tr>
                    </table>
                        <input type="text" id="curentpass" name="curentpass" value="<%=pass%>" hidden="true">
               </form>
               <form id="reset" name="reset" action="../../HomepageController?action=view&actor=Student" method="post"></form>
               </div>     
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
<script  type = "text/javascript" >
        function Cancel(){
             document.forms["reset"].submit();
        }
        function Change(){
            var oldPass=document.getElementById("oldPass").value;
            var newPass=document.getElementById("newpass").value;
            var confirmPass=document.getElementById("confirmPass").value;
            var curentpass=document.getElementById("curentpass").value;
            if(oldPass.length==0){
                alert("Hãy nhập mật khẩu hiện tại");
            }
            else if(newPass.length==0){
                alert("Hãy nhập mật khẩu mới");
            }
            else if(confirmPass.length==0){
                alert("Hãy xác nhận lại mật khẩu mới");
            }
            else if(newPass!=confirmPass){
                alert("Xác nhận mật khẩu không khớp");
            }
            else if(oldPass!=curentpass){
                alert("Mật khẩu hện tại không đúng");
            }
            else{
                document.forms["changepast"].submit();
            }
        }
    </script>
  </html>