<%-- 
    Document   : ListStudent
    Created on : 16-11-2011, 22:06:24
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">

<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<Student> listStudent = (List<Student>) session.getAttribute("liststudent");
    Integer numpage = (Integer) session.getAttribute("numpage");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý sinh viên</title>
        <style media="all" type="text/css">
            #btn-delete-selected {
                float: left;
                padding-top: 8px;
                margin-left: 122px;
            }
            #btn-to-import-student {
                float: right;
                padding-top: 6px;
                margin-right: 20px;
            }
            
            
        </style>
    </head>
    <body onload="">
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <!--Main Navigation-->
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <%--------------Title--------------%>
                <div id="main-title">
                    Quản lý Sinh Viên
                </div>
                <br /><br />
                <p id="error">
                    <%-- NOT USED --%>
                </p>
                <br/>
                <div id="control-range">
                    <%--------------Search-------------%>
                    <div id="search-area" style="float: left;">
                        <%--SEARCH FORM--%>
                        <div id="searchbox">
                            <input id="search" type="text" onKeyPress="txtBoxSearchkeypressed()" placeholder="Search" />
                            <input type="button" id="submit" onclick="searchStudent()" value="Tìm kiếm" />
                        </div>
                    </div>
                    <%-- Button delete selected rows --%>
                    <div id="btn-delete-selected" class="clear-left">
                        <span class="atag" type="button" onclick="deleteStudent('tableliststudent')"><img src="../../imgs/icon/delete.png"/>Xóa mục đã chọn</span>
                    </div>
                    <%-- Link import student --%>
                    <div id="btn-to-import-student">
                        <a href="../../ManageStudentController?function=pre-import-student"><img src="../../imgs/icon/add.png"/>Tiếp nhận sinh viên</a>
                    </div>
                </div>
                <div class="clear"></div>
                <div id="list-students">
                    <%-- List student (Table)--%>
                    <%--Danh sách sinh viên:--%>
                    <table id="tableliststudent" name="tableliststudent" class="general-table">
                            <tr id="tableliststudent-th">
                            <th><INPUT type="checkbox" name="chkAll" onclick="selectAll('tableliststudent', 0)" /></th>
                            <th> STT </th>
                            <th> <span class="atag" onclick="sort('MSSV')" > MSSV </span></th>
                            <th> <span class="atag" onclick="sort('HoTen')" >  Họ Tên </span></th>
                            <th> <span class="atag" onclick="sort('MaLop')" >  Lớp </span></th>
                            <th> <span class="atag" onclick="sort('MaKhoa')" >  Khoa </span></th>
                            <th> <span class="atag" onclick="sort('NgaySinh')" > Ngày sinh </span></th>
                            <th> <span class="atag" onclick="sort('GioiTinh')" >  Giới tính </span></th>
                            <th> <span class="atag" onclick="sort('LoaiHinhHoc')" >  Loại </span></th>
                            <th> Sửa </th>
                            <th> Xóa </th>
                            <%--Should be sorted when click on table's header--%>
                            </tr>
                            <%if ((listStudent != null) && !listStudent.isEmpty()) {%>
                            <% for (int i = 0; i < listStudent.size(); i++) {%>
                            <tr id="tr_student<%= i%>">
                            <td>
                                <INPUT type="checkbox" value="aaaa" name="chk<%= i%>" onchange="highLightSelectRow(this, 'tr_student<%=i%>')" />
                            </td>
                            <td> <%= (i + 1)%> </td>
                            <td> <%= listStudent.get(i).getId()%> </td> 
                            <td> <%= listStudent.get(i).getFullName()%> </td>
                            <td> <%= listStudent.get(i).getClassCode()%> </td>
                            <td> <%= listStudent.get(i).getFacultyCode()%> </td>
                            <td> <%= listStudent.get(i).getBirthday()%> </td>
                            <td> <%= listStudent.get(i).getGender()%> </td>
                            <td> <%= listStudent.get(i).getStudyType()%> </td>
                            <td><a href="../../ManageStudentController?function=editstudent&mssv=<%= listStudent.get(i).getId()%>"><img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/></a></td>
                            <td><span class="atag" onclick="deleteOneStudent('<%=listStudent.get(i).getId()%>')" > <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/> </span></td>
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
                <br /><br />
                <%-- Thogn ke theo SV --%>
                <div class="range">
                    <h3><span id="btn-student-report" class="atag" >Tìm kiếm SV</span></h3>
                    <div id="form-student-report">
                        <div style="clear: both; margin-top: 10px;">
                            <table>
                                <tr>
                                    <td>
                                        <b>Tìm kiếm</b>
                                        <input id="search-student" type="text" placeholder="Nhập tên SV" value="" onKeyPress="keypressed()"/>
                                    </td>
                                    <td>
                                        <div class="button-1" style="padding: 2px !important;">
                                            <span class="atag" onclick="SendRequestFindStudent()" ><img src="../../imgs/check.png" />Tìm</span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    
                        <div id="list-student" style="float: left;">
                        </div>
                        <div id="student-detail" style="float: left; margin-top: -25px; padding-left: 16px;">
                        </div>
                    </div>
                <div class="clear"></div>
                <br /><br />
                </div>
            </div>
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div><!--End Wrapper-->
    

    <script src="../../javascripts/UtilTable.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var currentpage = 1;
        var numpage = document.getElementById("numpage").value;
        var searchType = 'none';
        var searchValue = 'all';
        var http = createRequestObject();
        
        $("#btn-student-report").click(function () {
            $('#form-student-report').slideToggle(500);
        });
        
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
            var pagename = "../../ManageStudentController?function=pagging"
                + "&currentpage=" + currentpage;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = searchHandler;
                http.send(null);
            }
        }
        
        function searchStudent() {
            var key = document.getElementById("search").value;
            var pagename = "../../ManageStudentController?function=search-students&key=" + key;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = handleResponseSearch;
                http.send(null);
            }
        }
        
        function handleResponseSearch(){
            if((http.readyState == 4) && (http.status == 200)){
                var detail = document.getElementById("list-students");
                detail.innerHTML = http.responseText;
            }
        }
        
        function sort(sortBy) {
            var pagename = "../../ManageStudentController?function=sort&by=" + sortBy;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = handleResponseSearch;
                http.send(null);
            }
        }
        
        function deleteOneStudent(mssv) {
            var pagename = "../../ManageStudentController?function=delete-one&mssv=" + mssv;
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = handleResponseDelete
                http.send(null);
            }
        }
        
        function deleteStudent(tableId) {
            try {
                var table = document.getElementById(tableId);
                var rowCount = table.rows.length;
                var data = '';
                var selectOne = false;
                
                for(var i = 1; i < rowCount; i++) {
                    var row = table.rows[i];
                    var chkbox = row.cells[0].childNodes[0];
                    if(null != chkbox && true == chkbox.checked) {
                        alert("Row 1 value: " + chkbox.value);
                        return;
                        if (selectOne == false) {
                            selectOne = true;
                        } else {
                            data += '-';
                        }
                        data += row.cells[2].innerHTML;
                    }
                }
                data = data.replace(/\s/g,'');
                var controller = '../../ManageStudentController?function=delete-multi' 
                    + '&data=' + data;
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
                
                var detail = document.getElementById("list-students");
                detail.innerHTML = http.responseText;
            }
        }
        
        
        function searchHandler() {
            if((http.readyState == 4) && (http.status == 200)){
                var detail = document.getElementById("list-students");
                detail.innerHTML = http.responseText;
            }
        }
        
        function txtBoxSearchkeypressed() {
            if(event.keyCode == '13') {
                searchStudent();
            }
        }
        
        //
        //
        // Search Student
        //
        function keypressed() { 
            if(event.keyCode=='13') {
                SendRequestFindStudent();
            }
        }
        
        function SendRequestFindStudent(){
            var search = document.getElementById("search-student").value;
            if (http) {
                http.open("GET", "../../ReportController?action=search_student&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
            }
        }
        
         function handleResponseFindStudent() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("list-student");
                 detail.innerHTML=http.responseText;
             }
         }
         
         //
         // Get student Report
         //
         function getDetailStudentReport(mssv) {
            //var search = document.getElementById("search-student").value;
             if (http) {
                http.open("GET", "../../ReportController?action=student-report&value="
                    + mssv, true);
                http.onreadystatechange = handleResponseStudentReport;
                http.send(null);
              }
         }
         
         function handleResponseStudentReport() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("student-detail");
                 detail.innerHTML=http.responseText;
             }
         }
         
         function sortTrainClass(by, type) {
             if (http) {
                http.open("GET", "../../ReportController?action=sort-student-report"
                    + "&by=" + by
                    + "&type=" + type, true);
                http.onreadystatechange = handleResponseStudentReport;
                http.send(null);
              }
         }
         
    </script>
    </body>
</html>
