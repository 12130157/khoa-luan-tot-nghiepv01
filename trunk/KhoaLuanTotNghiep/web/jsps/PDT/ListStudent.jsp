<%-- 
    Document   : ListStudent
    Created on : 16-11-2011, 22:06:24
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">

<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Student> listStudent = (List<Student>) session.getAttribute("liststudent");
    //List<String> listClass = (ArrayList<String>) session.getAttribute("listClass");
    Integer numpage = (Integer) session.getAttribute("numpage");
    List<uit.cnpm02.dkhp.model.Class> listClass = DAOFactory.getClassDao().findAll();
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý sinh viên</title>
        <style media="all" type="text/css">
            #tableliststudent{
                margin-left: 10px;
                margin-top: 20px;
                margin-bottom: 20px;
                width: 95%;
                border: 3px solid #73726E;
            }
            #tableliststudent-th{
                height: 32px;
                font-weight: bold;
                background-color: #175F6E;
                text-align: center;
            }
            #tableliststudent td{
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                padding: 2px 5px 2px 5px;
                text-align: center;
                width: auto;
            }
            #formsearch{
                margin-top: 10px;
                margin-left: 20px;
                padding: 5px 10px 5px 10px;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                border: 3px solid #73726E;
                width: 320px;
            }
            #red{
                margin-left: 32px;
                margin-top: 15px;
                background-color: #e4e4e3;
                width: 250px;
                height: 32px;
            }
            #red:hover {
                border: 2px solid #ff092d;
            }
            #sidebar {
                height:400px;
                overflow:auto;
            }
        </style>
    </head>
    <body onload="">
        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <p id="error">

                </p>

                <br/>

                <h1>Tìm kiếm sinh viên:</h1>
                <form>
                    <table>
                        <tr><td>
                            <input type="text" name="txtName" id="txtName" placeholder="Nhap ten SV"/>
                            <input type="button" onclick="searchByName()" value="Tìm theo tên SV"/>
                        </td></tr>
                        <tr><td>
                            <input type="text" name="txtClass" id="txtClass" placeholder="Nhap ma lop"/>
                            <input type="button" onclick="searchByClass()" value="Tìm theo lớp"/>
                        </td></tr>
                        <tr><td>
                            <input type="text" name="txtCourse" id="txtCourse" placeholder="Nhap ma khoa"/>
                            <input type="button" onclick="searchByCourse()" value="Tìm theo khoa"/>
                        </td></tr>
                    </table>
                </form>
                <p align="right"><b><a href="./ImportStudent.jsp">Tiếp nhận sinh viên</a></b></p>
                <hr><hr>

                <form id="formdown" name="formdown" action="../DownloadFile?action=test" method="post">
                    Danh sách sinh viên:<br/>
                    <input type="button" onclick="deleteStudent('tableliststudent')" value="Xóa mục đã chọn" />
                    <table id="tableliststudent" name="tableliststudent">
                        <tr id="tableliststudent-th">
                        <td><INPUT type="checkbox" name="chkAll" onclick="selectAll('tableliststudent')" /></td>
                        <td> STT </td>
                        <td> MSSV </td>
                        <td> Họ Tên </td>
                        <td> Lớp </td>
                        <td> Khoa </td>
                        <td> Ngày sinh </td>
                        <td> Giới tính </td>
                        <td> Loại </td>
                        <td> Sửa </td>
                        <td> Xóa </td>
                        <%--Should be sorted when click on table's header--%>
                        </tr>
                        <%if ((listStudent != null) && !listStudent.isEmpty()) {%>
                        <% for (int i = 0; i < listStudent.size(); i++) {%>
                        <tr>
                        <td><INPUT type="checkbox" name="chk<%= i%>"/></td>
                        <td> <%= (i + 1)%> </td>
                        <td> <%= listStudent.get(i).getId()%> </td> 
                        <td> <%= listStudent.get(i).getFullName()%> </td>
                        <td> <%= listStudent.get(i).getClassCode()%> </td>
                        <td> <%= listStudent.get(i).getFacultyCode()%> </td>
                        <td> <%= listStudent.get(i).getBirthday()%> </td>
                        <td> <%= listStudent.get(i).getGender()%> </td>
                        <td> <%= listStudent.get(i).getStudyType()%> </td>
                        <td><a href="../../ManageStudentController?function=editstudent&mssv=<%= listStudent.get(i).getId()%>">Sửa</a></td>
                        <td><a href="../../ManageStudentController?function=delete&ajax=true&data=<%= listStudent.get(i).getId()%>">Xóa</a></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                    <form id="testid">
                    </form>
                    <input style="position:absolute; left:650px;" type="button" value="|<<" onclick="firstPage()">
                    <input style="position:absolute; left:680px;" type="button" value="<<" onclick="prePage()">
                    <input style="position:absolute; left:710px;" type="button" value=">>" onclick="nextPage()">
                    <input style="position:absolute; left:740px;" type="button" value=">>|" onclick="endPage()"><br>
                    <input type="hidden" value="<%= numpage %>" id="numpage" />
                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/UtilTable.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var ajax = false;
        var currentpage = 1;
        var numpage = document.getElementById("numpage").value;
        var searchType = 'none';
        var searchValue = 'all';
        var http = createRequestObject();
        
        function firstPage(){
            ajax = true;
            currentpage = 1;
            submitSearch();
        }
        
        function prePage(){
            ajax = true;
            currentpage--;
            if(currentpage < 1) {
                currentpage = 1;
                return;
            }
            submitSearch();
        }
        
        function nextPage(){
            ajax = true;
            currentpage ++;
            if(currentpage > numpage) {
                currentpage = numpage;
                return;
            }
            submitSearch();
        }
        
        function endPage(){
            ajax = true;
            currentpage = numpage;
            submitSearch();
        }
        
        function searchHandler() {
            if(http.readyState == 4 && http.status == 200){
                location.reload(true);
            }
        }
        
        function searchByName(){
            searchType = 'name';
            searchValue = document.getElementById("txtName").value;
            submitSearch();
        }
        
        function searchByClass(){
            searchType = 'clazz';
            searchValue = document.getElementById("txtclass").value;
            submitSearch();
        }
        
        function searchByCourse(){
            searchType = "course";
            searchValue = document.getElementById("txtCourse").value;
            submitSearch();
        }
        
        function submitSearch() {
            var pagename = "../../ManageStudentController?function=liststudent&searchtype=" + searchType + "&searchvalue=" + searchValue + "&currentpage=" + currentpage + "&ajax=true";
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = handleResponseDelete;
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
                        if (selectOne == false) {
                            selectOne = true;
                        } else {
                            data += '-';
                        }
                        data += row.cells[2].innerHTML;
                    }
                }
                data = data.replace(/\s/g,'');
                var controller = '../../ManageStudentController?function=delete' + '&data=' + data + "&ajax=true";
                if(http){
                    alert('aaaaaaaa');
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
                var detail = document.getElementById("tableliststudent");
                detail.innerHTML = http.responseText;
            }
        }
    </script>
</html>
