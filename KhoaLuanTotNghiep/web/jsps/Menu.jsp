<%-- 
    Document   : jspmenu
    Created on : Apr 23, 2011, 3:50:32 PM
    Author     : ngloc_it
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
    </head>
    <body>
    <MARQUEE onmouseover="this.stop();" onmouseout="this.start();" HEIGHT=25 BGCOLOR=transparent BEHAVIOR=scroll SCROLLAMOUNT="4">
        <h3>Đại Học Quốc Gia Tp. Hồ Chí Minh - Trường Đại Học Công Nghệ Thông Tin.</h3>
    </MARQUEE>

    <div id="logo">
        <DIV id="flash" style="Z-INDEX: 1; padding-left: 10%; padding-top: 0px; WIDTH: 80%; POSITION: absolute; TOP: 0px; HEIGHT: 0px; margin-left: -150px; margin-top: 40px">
            <OBJECT height=200 width=1060>
                <embed src="../flash/flash.swf" width="1060" height="120"
                       quality="high" 
                       type="application/x-shockwave-flash" menu="false"
                       wmode="transparent">
                </embed>
            </OBJECT>
        </DIV>
        <%--<span id="digitalclock" class="clock"></span>--%>
    </div>
    <div id = "menu" align="center">
        <ul>
            <li><a href="../HomepageController?action=view&actor=All"> Trang chủ</a></li>
            <li><a href="../ServHompage?action=view"> Giới thiệu</a></li>
            <li><a href="../ServHompage?action=view"> Công Nghệ Phần Mềm</a></li>
            <li><a href="../ServHompage?action=view"> Khoa Học Máy Tính</a></li>
            <li><a href="GiangVien/GVStart.jsp">Kỹ Thuật Máy Tính</a></li>
            <li><a href="PDT/PDTStart.jsp">Mạng Máy Tính</a></li>
            <li><a href="SinhVien/SVStart.jsp">Hệ Thống Thông Tin</a></li>
            <li class="login"><a onclick="showLoginForm();">Đăng nhập</a></li>
            <li><a href="jspLienHe.jsp" class="no-border">Liên Hệ</a></li>
         </ul>
    </div>
    <%-- Login --%>
    <div id="login">
        <table>
            <tr>
                <td>Tên đăng nhập</td>
                <td><input type="text" id="txtUsername" placeholder="Tên đăng nhâp"/></td>
            </tr>
            <tr>
                <td>Mật khẩu</td>
                <td><input id="txtPassword" type="password" maxlength="20" onkeypress="keypressed();" /><td>
            </tr>
            <tr style="height: 50px;">
                <td colspan="2">
                    <input type="button" class="button-1" style="padding: 1px; width: 98%; margin-left: 5px;"  onclick="login();" value="Đăng nhập"/>
                </td>
            </tr>
        </table>
        <%-- Error handler --%>
        <div id="login-error">
            
        </div>
        <div id="btn-forward">
            <img src="../imgs/icon/forward.png" alt="hide" title="hide"/>
        </div>
    </div>
    <form name="myform" id="myform" method="post" action=""></form>
    <script src="../javascripts/AjaxUtil.js"></script>
    <script src="../javascripts/jquery-1.7.1.js"></script>
    <script src="../javascripts/clock.js"></script>
    <script>
        var http = createRequestObject();
        
        $('.login').addClass('hover');
        $('#btn-forward').click(function() {
            //var left = $(this).position().left;
            //var top = $(this).position().bottom;
            //alert(left + " - " + top);
            $('#login').toggle(1000);
            //$("#login").css({ "top": top + "px", "left": left + "px" });
        });
        $('#login input').bind('keypress', function() {
            $(this).removeClass('border-warning');
        });
        function showLoginForm() {
            $('#login').toggle(1000);
        }
        
       function keypressed() { 
            if(event.keyCode=='13') {
                login();
            } 
        }
        
        function login(){
            var txtUsername = $('#txtUsername').val();
            var txtPassword = $('#txtPassword').val();
            if((txtUsername.length == 0) || (txtUsername == "Tên truy cập")){
                //alert("Bạn chưa nhập tên người dùng");
                //txtUsername.focus();
                $('#txtUsername').addClass('border-warning');
                $('#txtUsername').focus();
            }else if(txtPassword.length==0){
                //alert("Bạn chưa nhập mật khẩu");
                //txtPassword.focus();
                $('#txtPassword').addClass('border-warning');
                $('#txtPassword').focus();
            }
            else{
                //document.forms["myform"].submit();
                var controller = "../Login?function=login&txtUsername=" + txtUsername
                            + "&txtPassword=" + txtPassword;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = loginHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
        }
        function loginHandler() {
            if(http.readyState == 4 && http.status == 200){
                var txtRespone = http.responseText;
                if (txtRespone.indexOf('OK') != -1) {
                    $('#login').hide();
                    //
                    var path = "../Login?function=redirect&path=" +  txtRespone.split('-')[1];
                    //var form_url = $("#myform").attr("action");
                    $("#myform").attr("action", path);
                    $("#myform").submit();
                } else {
                    var detail = document.getElementById("login-error");
                    detail.innerHTML = txtRespone;
                }
            }
        }
    </script>
</body>
</html>
