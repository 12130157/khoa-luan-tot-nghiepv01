<%-- 
    Document   : ListLecturer
    Created on : 24-12-2011, 10:33:56
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<Lecturer> listLecturer = (List<Lecturer>) session.getAttribute("listlecturer");
    Integer numpage = (Integer) session.getAttribute("numpage");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Quản lý giảng viên</title>
        <style media="all" type="text/css">
            /* CSS definition */
            #btn-delete-selected {
                float: left;
                padding-top: 8px;
                margin-left: 122px;
            }
            #btn-to-import-lecturer {
                float: right;
                padding-top: 6px;
                margin-right: 20px;
            }
        </style>
    </head>
    <body>
        <%--Div Wrapper--%>
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <%--Main Navigation--%>
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><%--End Navigation--%>
            <%--Main Contents--%>
            <div id="content">
                <%--------------Title--------------%>
                <div id="main-title">
                    Quản lý giảng Viên
                </div>
                <br /><br /><br />
                <div id="control-range">
                    <%--------------Search-------------%>
                    <div id="search-area" style="float: left;">
                        <%--SEARCH FORM--%>
                        <div id="searchbox" action="#">
                            <%--SEARCH FORM--%>
                            <input type="text" id="search" onKeyPress="txtBoxSearchLecturerkeypressed(event)" placeholder="Search" />
                            <input type="button" id="submit" value="Tìm kiếm" onclick="searchLecturer()" />
                        </div>
                    </div>
                    <%-- BUTTON DELETE SELECT ROW --%>
                    <div id="btn-delete-selected" class="clear-left">
                        <span class="atag" type="button" onclick="deleteLecturer('tablelistlecturer')"><img src="../../imgs/icon/delete.png"/>Xóa mục đã chọn</span>
                    </div>

                    <%-- Link import lecturer page --%>
                    <div id="btn-to-import-lecturer">
                        <a href="../../ManageLecturerController?function=pre-import-lecturer"><img src="../../imgs/icon/add.png"/>Tiếp nhận giảng viên</a>
                    </div>
                </div>
                <hr><hr>
                <div class="clear"></div>
                <div id="list-lecturers">
                <%-- List lecturers (Table)--%>
                    <table id="tablelistlecturer" name="tablelistlecturer" class="general-table">
                        <tr>
                            <th><INPUT type="checkbox" name="chkAll" onclick="selectAll('tablelistlecturer', 0)" /></th>
                            <th> STT </th>
                            <th> Mã GV </th>
                            <th> Họ Tên </th>
                            <th> Khoa </th>
                            <th> Địa chỉ </th>
                            <th> Ngày sinh </th>
                            <th> Giới tính </th>
                            <%--<th><span class="atag" onclick="sort('Email')" > Email </span></th>--%>
                            <th> Học Hàm </th>
                            <th> Học Vị </th>
                            <th> Sửa </th>
                            <th> Xóa </th>
                        <%--Should be sorted when click on table's header ? --%>
                        </tr>
                        <%if ((listLecturer != null) && !listLecturer.isEmpty()) {%>
                        <% for (int i = 0; i < listLecturer.size(); i++) {%>
                        <tr id="tr_lecturer<%=i%>">
                            <td><INPUT type="checkbox" name="chk<%= i%>" onchange="highLightSelectRow(this, 'tr_lecturer<%=i%>')" />
                            <td> <%= (i + 1)%> </td>
                            <td> <%= listLecturer.get(i).getId()%> </td> 
                            <td> <%= listLecturer.get(i).getFullName()%> </td>
                            <td> <%= listLecturer.get(i).getFacultyCode() %> </td>
                            <td> <%= listLecturer.get(i).getAddress() %> </td>
                            <td> <%= listLecturer.get(i).getBirthday()%> </td>
                            <td> <%= listLecturer.get(i).getGender()%> </td>
                            <%--<td> <%= listLecturer.get(i).getEmail() %> </td>--%>
                            <td> <%= listLecturer.get(i).getHocHam() %> </td>
                            <td> <%= listLecturer.get(i).getHocVi() %> </td>
                            <td><a href="../../ManageLecturerController?function=editlecturer&magv=<%= listLecturer.get(i).getId()%>"><img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/></a></td>
                            <td><span class="atag" onclick="deleteOneLecturer('<%= listLecturer.get(i).getId()%>')"><img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                </div>
                <%--  PAGGING --%>
                <div id="paggind">
                    <input type="button" value="|<<" onclick="firstPage()">
                    <input type="button" value="<<" onclick="prePage()">
                    <input type="button" value=">>" onclick="nextPage()">
                    <input type="button" value=">>|" onclick="endPage()"><br>
                    <input type="hidden" value="<%= numpage%>" id="numpage" />
                </div>
                 <%-- Download file form --%>
                <form id="formdown" name="formdown" action="../DownloadFile?action=test" method="post">    
                </form>
            </div><%--End Contents--%>
            <%--Footer--%>
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><%--End footer--%>
        </div>
        <%--End Wrapper--%>
    
    <script src="../../javascripts/UtilTable.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var ajax = false;
        var currentpage = 1;
        var numpage = document.getElementById("numpage").value;
        var http = createRequestObject();
        
        function firstPage(){
            currentpage = 1;
            submitSearch();
        }
        
        function prePage(){
            currentpage--;
            if(currentpage < 1) {
                currentpage = 1;
                return;
            }
            submitSearch();
        }
        
        function nextPage(){
            currentpage ++;
            if(currentpage > numpage) {
                currentpage = numpage;
                return;
            }
            submitSearch();
        }
        
        function endPage(){
            currentpage = numpage;
            submitSearch();
        }
        
        function submitSearch() {
            var pagename = "../../ManageLecturerController?function=pagging"
                + "&currentpage=" + currentpage;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            }
        }
        
        function searchHandler() {
            if((http.readyState == 4) && (http.status == 200)){
                var detail = document.getElementById("list-lecturers");
                detail.innerHTML = http.responseText;
                formatGeneralTable();
            }
        }
        
        function txtBoxSearchLecturerkeypressed() {
            if(event.keyCode == '13') {
                searchLecturer();
            }
        }
        
        function searchLecturer() {
            var key = document.getElementById("search").value;
            var pagename = "../../ManageLecturerController?function=search"
                            + "&key=" + key;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            }
        }
        
        function deleteOneLecturer(id) {
            var pagename = "../../ManageLecturerController?function=delete-one&magv=" + id + "&currentPage=" + currentpage;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = handleResponseDelete
                http.send(null);
            }
        }
        
        function handleResponseDelete(){
            if((http.readyState == 4) && (http.status == 200)){
                var responeResult = http.responseText;
                if (responeResult.substring(0, 5) == "error") {
                    var error = responeResult.substring(6, responeResult.length-1);
                    alert("Error: " + error);
                    return;
                }
                
                var detail = document.getElementById("list-lecturers");
                detail.innerHTML = http.responseText;
                formatGeneralTable();
            }
        }
        

        function deleteLecturer(tableId) {
            try {
                var table = document.getElementById(tableId);
                var rowCount = table.rows.length;
                var data = '';
                var selectOne = false;
                
                for(var i = 1; i < rowCount; i++) {
                    var row = table.rows[i];
                    var chkbox = row.cells[0].childNodes[0];
                    if(null != chkbox && true == chkbox.checked) {
                        if (selectOne == false) {
                            selectOne = true;
                        } else {
                            data += '-';
                        }
                        data += row.cells[2].innerHTML;
                    }
                }
                data = data.replace(/\s/g,'');
                var controller = '../../ManageLecturerController?function=delete-multi' 
                    + '&data=' + data + "&ajax=true";
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = handleResponseDelete;
                    http.send(null);
                }
            }catch(e) {
                alert(e);
            }
        }

        function handleResponseDelete(){
            if((http.readyState == 4) && (http.status == 200)){
                var responeResult = http.responseText;
                if (responeResult.substring(0, 5) == "error") {
                    var error = responeResult.substring(6, responeResult.length-1);
                    alert("Error: " + error);
                    return;
                }
                
                var detail = document.getElementById("list-lecturers");
                detail.innerHTML = http.responseText;
                formatGeneralTable();
            }
        }
    </script>
    </body>
</html>
