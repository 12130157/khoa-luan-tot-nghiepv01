<%-- 
    Document   : AccountManager
    Created on : 17-11-2011, 23:02:03
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.StringUtils"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Account"%>
<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    List<Account> accounts = (List<Account>) session.getAttribute("account");
    Integer numpage = (Integer) session.getAttribute("numpage");
    if (numpage == null) {
        numpage = 1;
    }

%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý tài khoản</title>
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
                    <u><h3>Quản lý tài khoản</h3></u>
                </div>

                <br>
                <hr/><hr/><br>
                <div id="accounts">
                    <a href="CreateNewAccount.jsp"> Tạo mới tài khoản </a>
                    <br /><br />
                    <form action="../../AccountController?action=search" method="post">
                        <input type="text" name="txtSearch" id="txtSearch" />
                        <input type="submit" value="Tìm kiếm">
                    </form>

                    <form id="formdetail" name="formdetail">
                        <table id="accountdetail" name="accountdetail" border="2" bordercolor="yellow" >
                            <tr>
                            <th>STT</th>
                            <th>Tên đăng nhập</th>
                            <th >Họ tên NSD</th>
                            <th>Tình trạng</th>
                            <th>Loại tài khoản</th>
                            <th>Sửa</th>
                            <th>Xóa</th>
                            </tr>
                            <%
                                if ((accounts != null) && (!accounts.isEmpty())) {
                                    for (int i = 0; i < accounts.size(); i++) {
                                        Account acc = accounts.get(i);
                            %>
                            <tr>
                            <td><%= i + 1%></td>
                            <td><%= acc.getUserName()%></td>
                            <td><%= acc.getFullName()%></td>
                            <td><%= acc.getStatus()%></td>
                            <td><%= StringUtils.getAccountTypeDescription(acc.getType())%></td>
                            <td><a href="../../AccountController?action=editaccount&username=<%= acc.getId()%>">Sửa</a> </td>
                            <td><a href="../../AccountController?action=deleteaccount&username=<%= acc.getId()%>">Xóa</a> </td>
                            </tr>
                            <%}
                                }%>
                        </table>
                        <div id="page">
                            <input type="button" value="|<<" onclick="FirstPage()"/>- 
                            <input type="button" value="<<" onclick="PrePage()"/>-
                            <input type="button" value=">>" onclick="NextPage()"/>-
                            <input type="button" value=">>|" onclick="EndPage()"/>
                            <input type="hidden" value="<%=numpage%>" id="numpage" />
                        </div>
                        <br/>
                    </form>
                </div>      
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/AccountManager.js"></script>
    <script  type = "text/javascript" >
        var currentpage = 1;
        var http = createRequestObject();
        numpage = document.getElementById("numpage").value;
        function FirstPage(){
            currentpage = 1;
            SendRequest();
        }
        function PrePage(){
            currentpage--;
            if(currentpage < 1) currentpage = 1;
            SendRequest();
        }
        function NextPage(){
            currentpage ++;
            if(currentpage > numpage)
                currentpage = numpage;
            SendRequest();
        }
        function EndPage(){
            currentpage = numpage;
            SendRequest();
        }
        function SendRequest(){
            if(http){
                ajaxfunction("../../AccountController?action=Filter&curentPage="+currentpage);
            }
        }
    </script>
</html>