<%-- 
    Document   : PreSubjectManager
    Created on : 01-12-2011, 21:28:43
    Author     : LocNguyen
--%>

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

    List<String> subjects = (List<String>) session.getAttribute("list_sub");
    List<PreSubject> preSubjects = (List<PreSubject>) session.getAttribute("list_pre_sub");

%>

<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang quản lý môn học tiên quyết</title>
        
        <style media="all" type="text/css">
            #tableadd, #tablelist{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;
            }
            #tableadd th, #tablelist th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #tableadd td, #tablelist td{
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
                    <u><h3>Quản lý Môn học tiên quyết</h3></u>
                </div>

                <br>
                <hr/><hr/><br>

                <div id = "add-new" name="add-new">
                    <h3> Thêm môn học tiên quyết: </h3>
                    <table id="tableadd" name="tableadd">
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
                                <option> <%= subjects.get(i)%> </option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <td>
                            <select id="select-pre-sub">
                                <% if ((subjects != null) && (!subjects.isEmpty())) {
                                        for (int i = 0; i < subjects.size(); i++) {
                                %>
                                <option> <%= subjects.get(i)%> </option>
                                <% }
                                    }%>
                            </select>
                        </td>
                    </table>

                    <div id="check-respone">
                    </div>

                    <div id = "btn-add">
                        <input type="button" onclick="checkPreSubjectExisted()" value="Kiểm tra" />
                        <input type="button" onclick="addPreSub()" value="Thêm" />
                    </div>
                </div>

                <br /> <hr /> <hr />            
                <div id = "list-pre-sub" name="list-pre-sub">
                    <h3> Danh sách môn học tiên quyết: </h3>
                    <table id="tablelist" name="tablelist">
                        <tr>
                        <th> STT </th>
                        <th> Tên môn học </th>
                        <th> Tên MHTQ </th>
                        <th>  </th>
                        </tr>
                        <%
                            if ((preSubjects != null) && (!preSubjects.isEmpty())) {
                                for (int j = 0; j < preSubjects.size(); j++) {
                                    PreSubID id = preSubjects.get(j).getId();
                        %>
                        <tr>
                        <td> <%= (j + 1)%> </td>
                        <td> <%= preSubjects.get(j).getSubjectName()%> </td>
                        <td> <%= preSubjects.get(j).getPreSubjectName()%> </td>
                        <td> 
                            <form method="post" action="../../PreSubjectController?action=delete&sub-id=<%= id.getSudId()%>&pre-sub-id=<%= id.getPreSudId()%>">
                                <input type="submit" value="Xóa">
                            </form>
                        </td>
                        <!--td> <input type="button" onclick="deletePreSubject()" value="Xóa"> </td-->
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
    </script>
</html>
