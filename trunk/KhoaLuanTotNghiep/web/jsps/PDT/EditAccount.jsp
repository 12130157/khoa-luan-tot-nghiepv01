<%-- 
    Document   : EditAccount
    Created on : 18-11-2011, 23:32:18
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.Constants"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="MenuPDT.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    String error = (String) session.getAttribute("error");
    session.removeAttribute("error");
    Account acc = (Account) session.getAttribute("account");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tạo tài khoản.</title>
        <style media="all" type="text/css">

            #formdetail table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #formdetail table th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
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
            #backbutton {
                float: right;
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
                    <u><h3>Tạo tài khoản mới.</h3></u>
                </div>

                <br>
                <hr/><hr/><br />

                <div id="error">
                    <%

                        if ((error != null) && (!error.isEmpty())) {%>
                    <%= error%>
                    <%}
                    %>
                </div>

                <form action="../../AccountController?action=editaccount" method="post">
                    <table>
                        <tr>
                        <td> Tên đăng nhập </td> <td> <input type="text" name="txtUsername" id="txtUsername" readonly="readonly" value="<%= acc.getId()%>"> </td>
                        </tr>
                        <tr>
                        <td> Mật khẩu </td> <td> <input type="password" name="txtPassword" id="txtPassword" value="<%= acc.getPassword()%>"> </td>
                        </tr>

                        <tr>
                        <td> Xác nhận mật khẩu </td> <td> <input type="password" name="txtRePassword" id="txtRePassword" value="<%= acc.getPassword()%>"> </td>
                        </tr>
                        <tr>
                        <td> Họ tên </td> <td> <input type="text" name="txtFullName" id="txtFullName" value="<%= acc.getFullName()%>"> </td>
                        </tr>
                        <tr>
                        <td> Loại tài khoản </td>
                        <td>
                            <select name="selectType" id="selectType">
                                <option <% if (acc.getType() == Constants.ACCOUNT_TYPE_PDT) {%> selected="true" <%}%>> PDT </option>
                                <option <% if (acc.getType() == Constants.ACCOUNT_TYPE_LECTURE) {%> selected="true" <%}%>> Giang Vien </option>
                                <option <% if (acc.getType() == Constants.ACCOUNT_TYPE_STUDENT) {%> selected="true" <%}%>> Sinh Vien </option>
                            </select>
                        </td>
                        </tr>
                        <tr>
                        <td> Tình trạng </td>
                        <td>
                            <select name="selectStatus" id="selectStatus">
                                <option> Bình thường </option>
                                <option> Hết hạn </option>
                            </select>
                        </td>
                        </tr>

                        <tr>
                        <td></td><td> 
                            <input type="submit" value="Hoàn thành" />
                        </td>
                        </tr>
                    </table>
                </form>
                <form id="backbutton" action="../../AccountController?action=manager" method="post">
                    <input type="submit" value="Trở lại" />
                </form>

            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
</html>

</html>
