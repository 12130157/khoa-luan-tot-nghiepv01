<%-- 
    Document   : AccountManager
    Created on : 17-11-2011, 23:02:03
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.StringUtils"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Account"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    List<Account> accounts = (List<Account>) session.getAttribute("accountList");
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
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp"%>
            <!--Main Navigation-->
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%---------------------------------%>
                <%--------------Title--------------%>
                <%---------------------------------%>
                <div id="title">
                    Quản lý tài khoản
                </div>
                <br>
                <hr/><hr/><br>
                <%---------------------------------%>
                <%------------Main content---------%>
                <%---------------------------------%>
                <div id="accounts">
                    <a href="CreateNewAccount.jsp"> Tạo mới tài khoản </a>
                    <br /><br />
                    
                    <%--------------Search-------------%>
                    <div id="search-area">
                        <input type="text" name="txtSearch" onKeyPress="keypressed()" id="txtSearch" />
                        <input type="button" onclick="search()" value="Tìm kiếm">
                    </div>
                    <%--
                    <form action="../../AccountController?action=search" method="post">
                        <input type="text" name="txtSearch" id="txtSearch" />
                        <input type="submit" value="Tìm kiếm">
                    </form>
                    --%>
                    <div id="formdetail">
                        <table id="accountdetail" name="accountdetail" class="general-table">
                            <tr>
                                <th> STT </th>
                                <th>  Tên đăng nhập </th>
                                <th>  Họ tên NSD </th>
                                <th>  Tình trạng </th>
                                <th>  Loại tài khoản </th>
                                <th> Sửa </th>
                                <th> Xóa </th>
                            </tr>
                            <%
                                if ((accounts != null) && (!accounts.isEmpty())) {
                                    for (int i = 0; i < accounts.size(); i++) {
                                        Account acc = accounts.get(i);
                            %>
                            <tr>
                                <td> <%= i + 1%> </td>
                                <td> <%= acc.getUserName()%> </td>
                                <td> <%= acc.getFullName()%> </td>
                                <%if(acc.getStatus()==0){%>
                                <td> Bình thường </td>
                                <%}else{%>
                                <td> Đang khóa </td>
                                <%}%>
                                <td> <%= AccountType.getDescription(acc.getType())%> </td>
                                <td> <a href="../../AccountController?action=editaccount&username=<%= acc.getId()%>">Sửa</a> </td>
                                <td> <span class="atag" onclick="deleteUser('<%= acc.getId() %>')">Xóa</span> </td>
                            </tr>
                            <%}
                                }%>
                        </table>
                        
                    </div>
                    <%--------------Pagging-------------%>
                    <div id="paggind">
                        <input type="button" value="|<<" onclick="firstPage()"/>
                        <input type="button" value="<<" onclick="prePage()"/>
                        <input type="button" value=">>" onclick="nextPage()"/>
                        <input type="button" value=">>|" onclick="endPage()"/>
                        <input type="hidden" value="<%=numpage%>" id="numpage" />
                    </div>
                    <br/>
                </div>      
            </div><!--End Contents-->

            <!--Footer-->
            <div id="footer">
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
        function firstPage(){
            currentpage = 1;
            sendRequest();
        }
        function prePage(){
            currentpage--;
            if(currentpage < 1) currentpage = 1;
            sendRequest();
        }
        function nextPage(){
            currentpage ++;
            if(currentpage > numpage)
                currentpage = numpage;
            sendRequest();
        }
        function endPage(){
            currentpage = numpage;
            sendRequest();
        }
        function sendRequest(){
            if(http){
                var key = document.getElementById("txtSearch").value;
                ajaxfunction("../../AccountController?action=Filter&curentPage="+currentpage + "&key=" + key);
            }
        }
         function keypressed()
    { 
       if(event.keyCode=='13')
       {
           search();
       } 
      }
        function search() {
            currentpage = 1;
            var key = document.getElementById("txtSearch").value;
            var pagename = "../../AccountController?action=search&key=" + key;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }
        
        function searchHandler() {
            if((http.readyState == 4) && (http.status == 200)){
                var detail = document.getElementById("formdetail");
                detail.innerHTML = http.responseText;
            }
        }

        function deleteHandler() {
            if((http.readyState == 4) && (http.status == 200)){
                var responseText = http.responseText;
                if (responseText.substring(0, 5) == "error") {
                    var error = responseText.substring(6, responseText.length-1);
                    alert("Error: " + error);
                    return;
                }
                
                var detail = document.getElementById("formdetail");
                detail.innerHTML = responseText;
            }
        }

        function deleteUser(username) {
            var pagename = "../../AccountController?action=delete&username=" + username;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }
        
        function sort(by) {
            var pagename = "../../AccountController?action=sort&by=" + by;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }
        
        function deleteUser(username) {
            var key = document.getElementById("txtSearch").value;
            var pagename = "../../AccountController?action=delete&user=" + username + "&key=" + key +"&curentPage="+currentpage;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }
    </script>
</html>