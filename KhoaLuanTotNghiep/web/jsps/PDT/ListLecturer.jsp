<%-- 
    Document   : ListLecturer
    Created on : 24-12-2011, 10:33:56
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
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
        </style>
    </head>
    <body>
        <%--Div Wrapper--%>
        <div id="wrapper">
            <%--Main Navigation--%>
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><%--End Navigation--%>
            <%--Main Contents--%>
            <div id="content">
                <br/>
                <%-- Link import student --%>
                <p align="right"><b><a href="../../ManageLecturerController?function=pre-import-lecturer">Tiếp nhận giảng viên</a></b></p>
                <hr><hr>
                Danh sách giảng viên:<br/>
                <%-- Search Form --%>
                <div id="search-lecturer-form">
                    <%-- BUTTON DELETE SELECT ROW --%>
                    <div id="btn-form" class="clear-left">
                        <input type="button" onclick="deleteLecturer('tablelistlecturer')" value="Xóa mục đã chọn" />
                    </div>
                    <%--SEARCH FORM--%>
                    <div id="search-form" class="clear-right" style="margin-right: 12px !important;">
                        <div id="search-lecturer" style="float: right;">
                            <input type="text" id="txt-search" placeholder="Nhập mã hoặc tên GV" />
                            <input type="button" value="Tìm" onclick="searchLecturer()" />
                        </div>
                    </div>
                </div>
                <div class="clear"></div>
                <div id="list-lecturers">
                <%-- List lecturers (Table)--%>
                    <table id="tablelistlecturer" name="tablelistlecturer" class="general-table">
                        <tr>
                            <th><INPUT type="checkbox" name="chkAll" onclick="selectAll('tablelistlecturer', 0)" /></td>
                            <th> STT </th>
                            <th><span class="atag" onclick="sort('MaGV')" > Mã GV </span></th>
                            <th><span class="atag" onclick="sort('HoTen')" > Họ Tên </span></th>
                            <th><span class="atag" onclick="sort('MaKhoa')" > Khoa </span></th>
                            <th><span class="atag" onclick="sort('QueQuan')" > Địa chỉ </span></th>
                            <th><span class="atag" onclick="sort('NgaySinh')" > Ngày sinh </span></th>
                            <th><span class="atag" onclick="sort('GioiTinh')" > Giới tính </span></th>
                            <th><span class="atag" onclick="sort('Email')" > Email </span></th>
                            <th><span class="atag" onclick="sort('HocHam')" > Học Hàm </span></th>
                            <th><span class="atag" onclick="sort('HocVi')" > Học Vị </span></th>
                            <th> Sửa </th>
                            <th> Xóa </th>
                        <%--Should be sorted when click on table's header ? --%>
                        </tr>
                        <%if ((listLecturer != null) && !listLecturer.isEmpty()) {%>
                        <% for (int i = 0; i < listLecturer.size(); i++) {%>
                        <tr>
                            <td><INPUT type="checkbox" name="chk<%= i%>"/></td>
                            <td> <%= (i + 1)%> </td>
                            <td> <%= listLecturer.get(i).getId()%> </td> 
                            <td> <%= listLecturer.get(i).getFullName()%> </td>
                            <td> <%= listLecturer.get(i).getFacultyCode() %> </td>
                            <td> <%= listLecturer.get(i).getAddress() %> </td>
                            <td> <%= listLecturer.get(i).getBirthday()%> </td>
                            <td> <%= listLecturer.get(i).getGender()%> </td>
                            <td> <%= listLecturer.get(i).getEmail() %> </td>
                            <td> <%= listLecturer.get(i).getHocHam() %> </td>
                            <td> <%= listLecturer.get(i).getHocVi() %> </td>
                            <td><a href="../../ManageLecturerController?function=editlecturer&magv=<%= listLecturer.get(i).getId()%>">Sửa</a></td>
                            <td><span class="atag" onclick="deleteOneLecturer('<%= listLecturer.get(i).getId()%>')">Xóa</a></td>
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
    </body>

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
            }
        }
        
        function searchLecturer() {
            var key = document.getElementById("txt-search").value;
            var pagename = "../../ManageLecturerController?function=search"
                            + "&key=" + key;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            }
        }
        
        function deleteOneLecturer(id) {
            var pagename = "../../ManageLecturerController?function=delete-one&magv=" + id;
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
            }
        }
    
        function sort(sortBy) {
            var pagename = "../../ManageLecturerController?function=sort&by=" + sortBy;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            }
        }
    </script>
</html>
