<%-- 
    Document   : Login
    Created on : Apr 23, 2011, 4:34:47 PM
    Author     : ngloc_it
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng Nhập</title>

        <style media="all" type="text/css">
            .pop_login {
            background: none repeat scroll 0 0 #FFFFFF;
            margin: 0 auto;
            width: 475px;
        }
        .pop_login_top_around {
            float: left;
            height: 47px;
        }
        .pop_login_content {
            background: url("../imgs/login/ico_tit_lg.jpg") repeat-x scroll left top transparent;
            float: left;
            height: 37px;
            padding-top: 10px;
            width: 451px;
        }

        .pop_login_content p.tit {
            background: url("../imgs/login/icon_dn.jpg") no-repeat scroll left top transparent;
            border-radius: 5px 5px 5px 5px;
            color: #ECB000;
            font-size: 18px;
            padding-left: 30px;
            text-shadow: none;
        }
        .pop_login_middle {
            border-bottom: 1px solid #CCCCCC;
            border-bottom-left-radius: 5px;
            border-bottom-right-radius: 5px;
            border-left: 1px solid #CCCCCC;
            border-right: 1px solid #CCCCCC;
            overflow: hidden;
            padding: 20px 10px;
        }
        .pop_login_middle_input {
            border: 1px solid #999999;
            border-radius: 5px 5px 5px 5px;
            height: 25px;
            margin-bottom: 20px;
            padding: 8px;
        }
        .pop_login_middle_input input {
            border: medium none;
            color: #CCCCCC;
            display: block;
            float: left;
            font-size: 14px;
            height: 20px;
            width: 380px;
        }
        .pop_login_middle_input img {
            display: block;
            float: left;
            margin: 3px 10px 0 0;
        }
        .bnt_dn_popup {
            display: block;
            float: left;
            margin: 0 auto 5px;
            width: 460px;
        }
        .bnt_dn_popup a, .bnt_dn_popup a:visited {
            background: url("../imgs/login/bnt_dn.jpg") no-repeat scroll left top transparent;
            display: block;
            font-size: 22px;
            height: 43px;
            line-height: 40px;
            margin: 0 auto;
            text-align: center;
            text-transform: uppercase;
            width: 274px;
            text-decoration: none;
        }
        .bnt_dn_popup a:hover {
            background: url("../imgs/login/bnt_dn.jpg") no-repeat scroll left bottom transparent;
            display: block;
            font-size: 22px;
            height: 43px;
            line-height: 40px;
            text-align: center;
            text-transform: uppercase;
            width: 274px;
        }
       </style>
    </head>
    <body>
        <%--Div Wrapper--%>
        <div id="wrapper">
            <%--Menu--%>
            <%@include file="Menu.jsp" %>
            <%--Main Navigation--%>
            <div id="mainNav">
                <%@include file="MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <form name="myform" id="myform" method="post" action="../Login?function=login">
                    <div class="pop_login">
                       <div class="pop_login_top">
                         <div class="pop_login_top_around"><img src="../imgs/login/left_icon_lg.jpg"></div>
                         <div class="pop_login_content">
                           <p class="tit">Đăng nhập</p>
                         </div>
                         <div class="pop_login_top_around"><img src="../imgs/login/right_icon_lg.jpg"></div>
                         <div class="clears"></div>
                       </div>
                       <div class="pop_login_middle">
                         <div class="pop_login_middle_input">
                           <img src="../imgs/login/icon_user.jpg">
                           <input type="text"
                                  name="txtUsername"
                                  id="txtUsername"
                                  maxlength="20"
                                  value="Tên truy cập"
                                  onfocus="if(this.value=='Tên truy cập')
                                      { this.style.color='#000';this.value='';}"
                                  onblur="if(this.value=='')
                                      { this.style.color='#ccc';this.value='Tên truy cập';}"
                                  onkeypress="keypressed()"
                                      style="color: rgb(204, 204, 204); ">
                         </div>  
                         <div class="pop_login_middle_input">
                           <img src="../imgs/login/icon_pass.jpg">
                           <input id="fauxPassword"
                                  name="fauxPassword"
                                  type="text"
                                  value="Mật khẩu"
                                  onfocus="onFocusHandler_p(this)">
                           <input id="txtPassword"
                                  style="display:none;"
                                  name="txtPassword"
                                  type="password"
                                  maxlength="20"
                                  value=""
                                  onblur="onBlurHandler_p(this);"
                                  onkeypress="keypressed()">		 
                         </div> 
                         <div class="clear"></div>
                         <div class="bnt_dn_popup">
                             <a href="javascript: void(0)" onclick="Login()">
                                 Đăng nhập</a>
                         </div>
                         
                         <%--
                         <div class="forget_ct">
                             <a href="img///forgotpass" class="nyroModal">Quên mật khẩu</a> | 
                             <a href="img///register">Đăng kí</a>
                         </div>
                         --%>
                         <%
                        String error = (String) session.getAttribute("error");
                            if (error != null) {
                                session.removeAttribute("error");
                        %>
                            <p style="padding-top: 5px; background-color: #0f0; text-align: center;">
                                <%=error%>
                            </p>
                        <%
                         }
                        %>
                       </div>
                       
                     </div>
                    </table>
                </form>
                     <br /><br />
                <%--
                <form name="myform" id="myform" method="post" action="../Login?function=login">
                    <table>
                        <tr>
                            <td>Tên Đăng Nhập</td>
                            <td>
                                <input type="text" name="txtUsername" id="txtUsername" onKeyPress="keypressed()"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Mật Khẩu</td>
                            <td>
                                <input type="password" name="txtPassword" id="txtPassword" onKeyPress="keypressed()"/>
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
                --%>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
      </body>
    
    <script type = "text/javascript">
        function keypressed() { 
            if(event.keyCode=='13') {
                Login();
            } 
        }
        
        function Login(){
            var txtUsername = document.myform.txtUsername.value;
            var txtPassword = document.myform.txtPassword.value;
            if((txtUsername.length == 0) || (txtUsername == "Tên truy cập")){
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
        
        function onFocusHandler_p(obj) {
            obj.style.display = 'none';
            document.getElementById("txtPassword").style.display = 'block';
            document.getElementById("txtPassword").focus();
        }
        function onBlurHandler_p(obj) {
            var pass = document.getElementById("txtPassword").value;
            if ((pass == null) || (pass.length == 0)) {
                obj.style.display = 'none';
                document.getElementById("fauxPassword").style.display = 'block';
            }
        }
    </script>
</html>
