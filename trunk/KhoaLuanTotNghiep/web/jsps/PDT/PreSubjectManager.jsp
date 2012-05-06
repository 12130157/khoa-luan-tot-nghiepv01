<%-- 
    Document   : PreSubjectManager
    Created on : 01-12-2011, 21:28:43
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page import="uit.cnpm02.dkhp.model.PreSubID"%>
<%@page import="uit.cnpm02.dkhp.model.PreSubject"%>
<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    int numpage;
    try {
        numpage = (Integer) session.getAttribute("numpage_pre_sub");
    } catch (Exception ex) {
        numpage = 1;
    }

    String error = (String) session.getAttribute("error");
    session.removeAttribute("error");

    List<Subject> subjects = (List<Subject>) session.getAttribute("list_sub");
    List<PreSubject> preSubjects = (List<PreSubject>) session.getAttribute("list_pre_sub");

%>

<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang quản lý môn học tiên quyết</title>
        
        <style media="all" type="text/css">
            #page{
                text-align: center;
            }
            a {
                color: violet;
            }
            #sidebar {
                height:400px;
                overflow:auto;
            }
            #list-pre-sub {
                height:400px;
                overflow:auto;
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
                    <u><h3>Quản lý Môn học tiên quyết</h3></u>
                </div>

                <br>
                <hr/><hr/><br>

                <div id = "add-new" name="add-new">
                    <h3> Thêm môn học tiên quyết: </h3>
                    <table id="tableadd" name="tableadd" class="general-table" style="width:450px !important">
                        <tr>
                            <th> Chọn môn học </th>
                            <th> Môn học tiên quyết </th>
                        </tr>
                        <tr>
                        <td>
                            <select id="select-sub">
                                <% if ((subjects != null) && (!subjects.isEmpty())) {
                                        for (int i = 0; i < subjects.size(); i++) {
                                %>
                                <option value="<%=subjects.get(i).getId()%>" > <%= subjects.get(i).getSubjectName() %> </option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <td>
                            <select id="select-pre-sub">
                                <% if ((subjects != null) && (!subjects.isEmpty())) {
                                        for (int i = 0; i < subjects.size(); i++) {
                                %>
                                <option value="<%=subjects.get(i).getId()%>" > <%= subjects.get(i).getSubjectName() %> </option>
                                <% }
                                    }%>
                            </select>
                        </td>
                    </table>
                    <div id = "btn-add">
                        <input type="button" onclick="checkPreSubjectExisted()" value="Kiểm tra" />
                        <input type="button" onclick="addPreSub()" value="Thêm" />
                    </div>
                            
                    <div id="check-respone">
                    </div>
                </div>

                <br /> <hr /> <hr />            
                <div id="pre-sub-search">
                    <%--<i>Nhập thông tin tìm kiếm </i>--%>
                    <input type="text" id="txt-search" />
                    <input type="button" value="Tìm" onclick="search()" />
                </div>
                <div id = "list-pre-sub" name="list-pre-sub">
                    <input type="hidden" id="sort-type" value="ASC" />
                    <h3> Danh sách môn học tiên quyết: </h3>
                    <table id="tablelist" class="general-table" name="tablelist">
                        <tr>
                        <th> STT </th>
                        <th> <a href="#" onclick ="sort('TenMH')">Tên môn học </a></th>
                        <th> <a href="#" onclick="sort('TenMHTQ')"> Tên MHTQ </a></th>
                        <th> </th>
                        </tr>
                        <%
                            if ((preSubjects != null) && (!preSubjects.isEmpty())) {
                                for (int j = 0; j < preSubjects.size(); j++) {
                                    PreSubject pSub = preSubjects.get(j);
                        %>
                        <tr>
                        <td> <%= (j + 1)%> </td>
                        <td> <%= pSub.getSubjectName()%> </td>
                        <td> <%= pSub.getPreSubjectName()%> </td>
                        <td> 
                            <a href="#" onclick="deletePreSub('<%=pSub.getId().getSudId()%>', '<%=pSub.getId().getPreSudId()%>')"> Xóa </a>
                        </td>
                        </tr>
                        <%        }
                            }
                        %>

                    </table>
                    <!--div id="page">
                        <input type="button" value="|<<" onclick="firstPage()"/>- 
                        <input type="button" value="<<" onclick="prePage()"/>-
                        <input type="button" value=">>" onclick="nextPage()"/>-
                        <input type="button" value=">>|" onclick="endPage()"/>
                        <input type="hidden" value="<!--%= numpage %>" id="numpage" />
                    </div-->
                </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var currentpage = 1;
        var http = createRequestObject();
        numpage = document.getElementById("numpage").value;

        /**
         * Check if the current subject already existed
         * 
         * A message respone throught ajax call will be
         * showed after the call to this function finished.
         */
        function checkPreSubjectExisted() {
            // In ID not name
            var sub = document.getElementById("select-sub").value;
            var preSub = document.getElementById("select-pre-sub").value;
            var pageName = "../../PreSubjectController?action=check-existed&sub=" + sub
                + "&pre-sub=" + preSub;
            if(http){
                http.open("GET", pageName ,true);
                http.onreadystatechange = checkSubExistedRespone;
                http.send(null);
            }
        }
        
        function checkSubExistedRespone() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("check-respone");
                detail.innerHTML = http.responseText;
            }
        }
        
        function addPreSub() {
            // In ID not name
            var sub = document.getElementById("select-sub").value;
            var preSub = document.getElementById("select-pre-sub").value;
            var pageName = "../../PreSubjectController?action=add-pre-sub&sub=" + sub
                + "&pre-sub=" + preSub;
            if(http){
                http.open("GET", pageName ,true);
                http.onreadystatechange = checkSubExistedRespone;
                http.send(null);
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
                //ajaxfunction("../../AccountController?action=Filter&curentPage="+currentpage);
            }
        }
        
        function search() {
            var key = document.getElementById("txt-search").value;
            if(http){
                http.open(
                "GET", "../../PreSubjectController?action=search&key=" + key ,true);
                http.onreadystatechange = mainListPreSubject;
                http.send(null);
            }
        }
        
        function mainListPreSubject() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("list-pre-sub");
                detail.innerHTML = http.responseText;
            }
        }
        
        function deletePreSub(subId, preSubId) {
            if(http){
                http.open(
                    "GET", "../../PreSubjectController?action=delete&subid="
                                    + subId + "&presubid=" + preSubId ,true);
                http.onreadystatechange = mainListPreSubject;
                http.send(null);
            }
        }
        
        function sort(by) {
            var type = "ASC";
            try {
                type = document.getElementById("sort-type").value;
            } catch(err) {
                type = "ASC";
            }
            
            if(http){
                http.open(
                    "GET", "../../PreSubjectController?action=sort&by="
                                    + by + "&type=" + type, true);
                http.onreadystatechange = mainListPreSubject;
                http.send(null);
            }
            return false;
        }
        
    </script>
</html>
