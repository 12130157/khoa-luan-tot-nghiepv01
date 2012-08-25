<%-- 
    Document   : PreSubjectManager
    Created on : 01-12-2011, 21:28:43
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page import="uit.cnpm02.dkhp.model.PreSubID"%>
<%@page import="uit.cnpm02.dkhp.model.PreSubject"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    
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
            <%-- CSS definition --%>
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Quản lý Môn học tiên quyết
                </div>
                <br /><br />
                <%-- Add new range --%>
                <div class="range">
                    <h3><span id="btn-add-pre-subject" class="atag" >Thêm môn học tiên quyết</span></h3>
                    <div id = "add-new" name="add-new">
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
                        <div class="button-1" style="float:left; margin-left: 15%; height: 22px;">
                            <span class="atag" onclick="checkPreSubjectExisted()"><img src="../../imgs/check.png"/>Kiểm tra</span>
                        </div>
                        <div class="button-1" style="float:left; margin-left: 15px; height: 22px;">
                            <span class="atag" onclick="addPreSub()"><img src="../../imgs/check.png"/>Thêm</span>
                        </div>
                        <div class="clear"></div> <br />
                        <div id="check-respone" class="msg-response" style="width: 517px;">
                        </div>
                    </div>
                </div>
                <div class="clear"></div>
                <br />
                <div class="range">
                    <h3><span id="btn-list-pre-subject" class="atag" >Danh sách môn học tiên quyết</span></h3>
                    <div id="list-pre-subject" style="display: none;">
                        <div id="control-range" style="height: 45px;">
                            <div id="searchbox" style="float: left;">
                                <%--<i>Nhập thông tin tìm kiếm </i>--%>
                                <input type="text" id="search" />
                                <input type="button" id="submit" value="Tìm" onclick="search()" />
                            </div>
                            <!--
                            <div id="pre-sub-filter">
                                <%--Filter by faculty--%>
                                Tìm theo khoa
                                <select id="select-search" onchange="" class="input-minwidth">
                                    <option value="all">Tất cả</option>
                                    <option value="cnpm">Thuộc khoa CNPM</option>
                                    <option value="mmt">Thuộc khoa MMT</option>
                                    <option value="ktmt">Thuộc khoa KTMT</option>
                                </select>
                            </div>
                            -->
                        </div>
                        <div class="clear"></div>
                        <div id = "list-pre-sub" name="list-pre-sub">
                            <input type="hidden" id="sort-type" value="ASC" />
                            <div style="padding-top: 25px; font-size: 12px;"> <b><i>Danh sách môn học tiên quyết</i></b></div>
                            <table id="tablelist" class="general-table" name="tablelist">
                                <tr>
                                    <th> STT </th>
                                    <th> <span class="atag" onclick ="sort('TenMH')"> Tên môn học </span></th>
                                    <th> <span class="atag" onclick="sort('TenMHTQ')"> Tên MHTQ </span></th>
                                    <th> <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/> </th>
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
                                        <span class="atag" onclick="deletePreSub('<%=pSub.getId().getSudId()%>', '<%=pSub.getId().getPreSudId()%>')"> <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/> </span>
                                    </td>
                                </tr>
                                    <%        }
                                        }
                                    %>

                            </table>

                        </div>
                    </div>
                    </div>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <%--<script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>--%>
    <script  type = "text/javascript" >
        
        $("#btn-add-pre-subject").click(function () {
            $('#add-new').slideToggle(500);
        });
        $("#btn-list-pre-subject").click(function () {
            $('#list-pre-subject').slideToggle(500);
        });
        
        
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
                $('#check-respone').show('slow');
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
            var key = document.getElementById("search").value;
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
                formatGeneralTable();
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
