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
    String error = (String) session.getAttribute("error");
    if ((error != null) && !error.isEmpty()) {
        session.removeAttribute("error");
    }
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý giảng viên</title>
        <style media="all" type="text/css">
            #tablelistlecturer{
                margin-left: 10px;
                margin-top: 20px;
                margin-bottom: 20px;
                width: 95%;
                border: 3px solid #73726E;
            }
            #tablelistlecturer-th{
                height: 32px;
                font-weight: bold;
                background-color: #175F6E;
                text-align: center;
            }
            #tablelistlecturer td{
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

                <h1>Tìm kiếm giảng viên:</h1>
                <form>
                    <table>
                        <tr>
                        <td>
                            <input type="text" name="txtName" id="txtName" placeholder="Nhap ten GV"/>
                            <input type="button" onclick="searchByName()" value="Tìm"/>
                        </td>
                        </tr>
                    </table>
                </form>
                <p align="right"><b><a href="./ImportLecturer.jsp">Tiếp nhận giảng viên</a></b></p>
                <hr><hr>

                <form id="formdown" name="formdown" action="#" method="post">
                    Danh sách giảng viên:<br/>
                    <div id="error">
                        <% if ((error != null) && (!error.isEmpty())) { %>
                            <%= error %>
                        <% } %>
                    </div>
                    <input type="button" onclick="deleteLecturer('tablelistlecturer')" value="Xóa mục đã chọn" />
                    <table id="tablelistlecturer" name="tablelistlecturer">
                        <tr id="tablelistlecturer-th">
                        <td><INPUT type="checkbox" name="chkAll" onclick="selectAll('tablelistlecturer', 0)" /></td>
                        <td> STT </td>
                        <td> Mã GV </td>
                        <td> Họ Tên </td>
                        <td> Khoa </td>
                        <td> Địa chỉ </td>
                        <td> Ngày sinh </td>
                        <td> Giới tính </td>
                        <td> Email </td>
                        <td> Học Hàm </td>
                        <td> Học Vị </td>
                        <td> Sửa </td>
                        <td> Xóa </td>
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
                        <td><a href="../../ManageLecturerController?function=editlecturer&mgv=<%= listLecturer.get(i).getId()%>">Sửa</a></td>
                        <td><a href="../../ManageLecturerController?function=delete&ajax=false&data=<%= listLecturer.get(i).getId()%>">Xóa</a></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
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
            if (currentpage == 1)
                return;
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
            if (currentpage == numpage)
                return;
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
        
        /*function searchByClass(){
            searchType = 'clazz';
            searchValue = document.getElementById("txtclass").value;
            submitSearch();
        }
        
        function searchByCourse(){
            searchType = "course";
            searchValue = document.getElementById("txtCourse").value;
            submitSearch();
        }*/
        
        function submitSearch() {
            var pagename = "../../ManageLecturerController?function=listlecturer&searchtype=" + searchType + "&searchvalue=" + searchValue + "&currentpage=" + currentpage + "&ajax=true";
            if(http){
                http.open("GET", pagename, true);
                http.onreadystatechange = handleResponseDelete;
                http.send(null);
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
                var controller = '../../ManageLecturerController?function=delete' + '&data=' + data + "&ajax=true";
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
                var detail = document.getElementById("tablelistlecturer");
                detail.innerHTML = http.responseText;
            }
        }
    </script>
</html>
