<%-- 
    Document   : AccountManager
    Created on : 17-11-2011, 23:02:03
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="java.util.EnumSet"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountStatus"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.StringUtils"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="uit.cnpm02.dkhp.model.Account"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    //
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
            #search-area {
                float: left;
            }
            #add-new-btn {
                float: right;
                padding: 10px 10px;
            }
            
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
                <%--------------Title--------------%>
                <div id="main-title">
                    Quản lý tài khoản
                </div>
                <br /><br />
                <%------------Main content---------%>
                <div id="control-range">
                    <%--------------Search-------------%>
                    <div id="search-area">
                        <div id="searchbox">
                            <input id="search" type="text" placeholder="Search" onKeyPress="keypressed()">
                            <input type="button" id="submit" onclick="searchAccount();" value="Tìm kiếm">
                        </div>
                        <%--
                        <input type="text" name="txtSearch" onKeyPress="keypressed()" id="txtSearch" />
                        <input type="button" onclick="search()" value="Tìm kiếm">
                        --%>
                    </div>
                    <%-- Button add new account --%>
                    <div id="add-new-btn">
                        <span class="atag">
                            <a href="#create-new-range"> <img src="../../imgs/icon/add.png" /> Tạo mới</a>
                        </span>
                    </div>
                    <%--<div id="add-new-btn">
                        <a class="atag" href="CreateNewAccount.jsp">
                            <img src="../../imgs/icon/add.png" /> Tạo mới
                        </a>
                    </div>
                    --%>
                </div>
                <div class="clear"></div>
                <%-- List account --%>
                <div id="formdetail">
                    <table id="accountdetail" name="accountdetail" class="general-table">
                        <tr>
                            <th> STT </th>
                            <th>  Tên đăng nhập </th>
                            <th>  Họ tên </th>
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
                            <%if(acc.getStatus()== AccountStatus.NORMAL.value()){%>
                                <td> <img src="../../imgs/icon/unlock.png" title="Đang hoạt động" alt="Bình thường"/> </td>
                            <%}else if(acc.getStatus()== AccountStatus.LOCKED.value()){%>
                                <td> <img src="../../imgs/icon/lock.png" title="Đang bị khóa" alt="Bị khóa"/> </td>
                            <%} else {%>
                                <td> </td>
                            <%} %>
                            <td> <%= AccountType.getDescription(acc.getType())%> </td>
                            <td>
                                <span class="atag" onclick="loadDataToEdit('<%= acc.getId() %>')">
                                    <a href="#edit-account-range"> <img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/> </a>
                                </span>
                            </td>
                            <td>
                                <span class="atag" onclick="deleteUser('<%= acc.getId() %>')">
                                    <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/>
                                </span>
                            </td>
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
                <%-- Add new account --%>
                <div id="back-top-page">
                    <a href="#content"> <img src="../../imgs/icon/back_top.png" title="Về đầu trang" alt="Về đầu trang"/> </a>
                </div>
                <div class="range" id="create-new-range">
                    <h3><span id="btn-to-create-new" class="atag" >Tạo tài khoản mới</span></h3>
                    <div id="form-create-new" style="display: none;">
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
                            <div class="button-1">
                                <span class="atag" onclick="createNew()" ><img src="../../imgs/check.png" />Tạo mới</span>
                            </div>
                            <div class="clear"></div>
                            <br/>
                            <%-- Response Area for Update --%>
                            <div id="respone-area">
                            </div>
                        </div>
                    </div>
                </div>
                <br/>
                <%-- Edit account --%>
                <div id="back-top-page">
                    <a href="#content"> <img src="../../imgs/icon/back_top.png" title="Về đầu trang" alt="Về đầu trang"/> </a>
                </div>
                <div class="range" id="edit-account-range">
                    <h3><span id="btn-to-edite-account" class="atag" >Cập nhật tài khoản</span></h3>
                    <div id="form-edit-account" style="display: none;">
                        <u>Cập nhật thông tin tài khoản và nhấn <b>Hoàn Thành</b></u>
                        <br/>
                        <div id="form-edit-account">
                            <br />
                            <u>
                                <li>
                                    Để thay đổi thông tin user, vui long click vào biểu tượng <img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/> tương ứng vơi account.
                                </li>
                            </u>
                        </div>
                    </div>
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
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <script  type = "text/javascript" >
        var currentpage = 1;
        var http = createRequestObject();
        numpage = document.getElementById("numpage").value;
        
        $("#btn-to-create-new").click(function () {
            $('#form-create-new').slideToggle(500);
        });
        
        $("#btn-to-edite-account").click(function () {
            $('#form-edit-account').slideToggle(500);
        });
        
        function loadDataToEdit(username) {
            // Load data
            preDataForEditAccount(username);   
            // Open edit range
            $('#form-edit-account').slideDown(500);
        }
        
        function preDataForEditAccount(username) {
            var pagename = "../../AccountController?action=editaccount&username="
                + username;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = preDataForEditAccountHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }
        
        function preDataForEditAccountHandler() {
            if((http.readyState == 4) && (http.status == 200)){
                var detail = document.getElementById("form-edit-account");
                detail.innerHTML = http.responseText;
            }
        }
        
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
                var key = document.getElementById("search").value;
                ajaxfunction("../../AccountController?action=Filter&curentPage="
                    +currentpage + "&key=" + key);
            }
        }
        function keypressed(event) {
            if(event.keyCode == '13') {
                searchAccount();
            } 
        }
        
        function searchAccount() {
            currentpage = 1;
            var key = document.getElementById("search").value;
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
                var detail = document.getElementById("accountdetail");
                detail.innerHTML = http.responseText;
                
                formatGeneralTable();
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
                
                formatGeneralTable();
            }
        }

        /*function deleteUser(username) {
            alert("Ham 1" + username);
            return;
            var pagename = "../../AccountController?action=delete&username=" + username;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }*/
        
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
            var key = document.getElementById("search").value;
            var pagename = "../../AccountController?action=delete&user="
                + username + "&key=" + key +"&curentPage="+currentpage;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            } else {
                alert("Error: Could not create http object.");
            }
        }
        
        // Add account ++
        function validate(username, pass, re_pass, fullName) {
            if ( (username.length == 0)
                || (pass.length == 0)
                || (re_pass.length == 0)
                || (fullName.length == 0)) {
                alert("Vui lòng nhập đầy đủ thông tin trước khi submit.");

                return false;
            }

            if (pass != re_pass) {
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
                
                formatGeneralTable();
            }
        }
        
        
        function update() {
            var username = document.getElementById("txtUsername_edit").value;
            var pass = document.getElementById("txtPassword_edit").value;
            var re_pass = document.getElementById("txtRePassword_edit").value;
            var fullName = document.getElementById("txtFullName_edit").value;
            var type = document.getElementById("selectType_edit").value;
            var status = document.getElementById("selectStatus_edit").value;
           
           if (!validate(username, pass, re_pass, fullName)) {
                return;
            }
            var controller = "../../AccountController?action=update-account&username=" 
                + username 
                + "&password=" + pass
                + "&repassword=" + re_pass
                + "&fullName=" + fullName
                + "&type=" + type
                + "&status=" + status;
                        
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = updateResponseHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        function updateResponseHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("respone-edit-area");
                detail.innerHTML = http.responseText;
                
                formatGeneralTable();
            }
        }
        // Update account --
    </script>
</html>