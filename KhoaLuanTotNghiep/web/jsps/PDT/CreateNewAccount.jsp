<%-- 
    Document   : CreateNewAccount
    Created on : 17-11-2011, 23:24:03
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.type.AccountStatus"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.EnumSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tạo tài khoản.</title>
        <style media="all" type="text/css">
        </style>
    </head>
    <body>
        <%--Div Wrapper--%>
        <div id="wrapper">
            <%--Menu--%>
            <%@include file="MenuPDT.jsp"%>
            <!--Main Navigation-->
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <div id="title">
                    Tạo tài khoản mới
                </div>
                <br>
                <hr/><hr/><br />

                <div id="form-information">
                    <table style="width: 450px;">
                        <tr>
                            <td> Tên đăng nhập </td>
                            <td> 
                                <input type="text" 
                                       name="txtUsername" 
                                       id="txtUsername"
                                       placeholder="username"/>
                            </td>
                        </tr>
                        <tr>
                            <td> Mật khẩu </td>
                            <td> <input type="password" 
                                        name="txtPassword" 
                                        id="txtPassword" 
                                        placeholder="password"/> 
                            </td>
                        </tr>
                        <tr>
                            <td> Xác nhận mật khẩu </td>
                            <td> <input type="password" 
                                        name="txtRePassword" 
                                        id="txtRePassword"
                                        placeholder="password"/> </td>
                        </tr>
                        <tr>
                            <td> Họ tên </td>
                            <td> <input type="text"
                                        name="txtFullName"
                                        id="txtFullName" 
                                        placeholder="Ho va ten"/> </td>
                        </tr>
                        <tr>
                            <td> Loại tài khoản </td>
                            <td>
                                <select name="selectType" id="selectType" class="input-minwidth">
                                    <%
                                        // Retrieve Student status
                                        for(AccountType at : EnumSet.allOf(AccountType.class)) {
                                        %>
                                        <option value="<%= at.value()%>"> <%= at.description()%> </option>
                                        <%}%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td> Tình trạng </td>
                            <td>
                                <select name="selectStatus" id="selectStatus" class="input-minwidth">
                                    <%
                                        // Retrieve Student status
                                        for(AccountStatus as : EnumSet.allOf(AccountStatus.class)) {
                                        %>
                                        <option value="<%= as.value()%>"> <%= as.description()%> </option>
                                        <%}%>
                                </select>
                            </td>
                        </tr>
                    </table>
                    <%-- Button Submit update --%>
                    <br/>
                    <div style="float: left; padding-left: 220px;">
                        <input type="button" onclick="createNew()" value="Tạo mới" />
                    </div>
                    <div style="float: left; padding-left: 9px;">
                        <form id="backbutton" action="../../AccountController?action=manager" method="post">
                            <input type="submit" value="Trở lại" />
                        </form>
                    </div>
                    <div class="clear"></div>
                    <br/>
                    <%-- Response Area for Update --%>
                    <div id="respone-area">
                    </div>
                    <br/>
                    <br/>
                </div>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script language="javascript">
        var http = createRequestObject();
        
        function validate(username, pass, re_pass, fullName) {
            if ( (username.length == 0)
                    || (pass.length == 0)
                    || (re_pass.length == 0)
                    || (fullName.length == 0)) {
                    alert("Vui lòng nhập đầy đủ thông tin trước khi submit.");
                    
                    return false;
            }
            
            if (pass.length != re_pass.length) {
                alert("Mật khẩu xác nhận không đúng.");
                return false;
            }
            
            return true;
        }
        
        function createNew() {
            var username = document.getElementById("txtUsername").value;
            var pass = document.getElementById("txtPassword").value;
            var re_pass = document.getElementById("txtRePassword").value;
            var fullName = document.getElementById("txtFullName").value;
            var type = document.getElementById("selectType").value;
            var status = document.getElementById("selectStatus").value;
            
            if (!validate(username, pass, re_pass, fullName)) {
                return;
            }
            
            var controller = "../../AccountController?action=create-new&username=" 
                + username 
                + "&password=" + pass
                + "&repassword=" + re_pass
                + "&fullName=" + fullName
                + "&type=" + type
                + "&status=" + status;
            
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = createNewResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        function createNewResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("respone-area");
                detail.innerHTML = http.responseText;
            }
        }
    </script>>
</html>
