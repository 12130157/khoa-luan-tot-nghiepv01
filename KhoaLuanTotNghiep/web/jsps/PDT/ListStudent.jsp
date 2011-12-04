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
    Integer numStudent = (Integer) session.getAttribute("numStudent");
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
                            <input type="text" placeholder="Nhap ten SV"/>
                            <input type="button" onclick="" value="Tìm theo tên SV"/>
                        </td></tr>
                        <tr><td>
                            <input type="text" placeholder="Nhap ma lop"/>
                            <input type="button" onclick="" value="Tìm theo lớp"/>
                        </td></tr>
                        <tr><td>
                            <input type="text" placeholder="Nhap ma khoa"/>
                            <input type="button" onclick="" value="Tìm theo khoa"/>
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
                        <td><a href="../../ManageStudentController?function=delete&data=<%= listStudent.get(i).getId()%>">Xóa</a></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                    <input style="position:absolute; left:650px;" type="button" value="|<<" onclick="nprepage()">
                    <input style="position:absolute; left:680px;" type="button" value="<<" onclick="prepage()">
                    <input style="position:absolute; left:710px;" type="button" value=">>" onclick="nextpage()">
                    <input style="position:absolute; left:740px;" type="button" value=">>|" onclick="nnextpage()"><br>
                    <input type="hidden" value="<%=numStudent%>" id="numstu" />
                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/UtilTable.js"></script>
    <SCRIPT language="javascript">
        var typesearch = "All";
        var name = "All";
        var action = "search";
        var action1 = "studentlist";
        var start = 0;
        var end = document.getElementById("numstu").value;
        var http = createRequestObject();
        function search(){
            if(http){
                start = 0;
                if(typesearch == "name"){
                    name = document.formsearch.txtName.value;
                }else if(typesearch == "mssv") {
                    name = document.formsearch.txtcode.value;
                }else if(typesearch == "classname"){
                    name = document.formsearch.sClass.value;
                }
                ajaxfunction("../servStudentManager?action="+action+"&type="+typesearch+"&name="+name+"&start="+start);
            }
        }
        
        function nprepage(){
            search();
        }
        
        function prepage(){
            start = start - 5;
            if(start < 0) {
                start = 0;
            }
            if(typesearch == "name") {
                name = document.formsearch.txtName.value;
            } else if(typesearch == "mssv") {
                name = document.formsearch.txtcode.value;
            } else if(typesearch == "classname") {
                name = document.formsearch.sClass.value;
            }
            ajaxfunction("../servStudentManager?action="+action+"&type="+typesearch+"&name="+name+"&start="+start);
            
        }
        function nextpage(){
            start=start+5;
            if(start+5>end){
                start=end-5;
            }
            if(start<0){
                start=0;
            }
            if(typesearch=="name"){
                name=document.formsearch.txtName.value;
            }else if(typesearch == "mssv"){
                name=document.formsearch.txtcode.value;
            }else if(typesearch == "classname"){
                name = document.formsearch.sClass.value;
            }
            ajaxfunction("../servStudentManager?action="+action+"&type="+typesearch+"&name="+name+"&start="+start);
        }
        function nnextpage(){
            start=end-5;
            if(start<0){
                start=0;
            }
            if(typesearch=="name"){
                name=document.formsearch.txtName.value;
            }else if(typesearch == "mssv"){
                name=document.formsearch.txtcode.value;
            }else if(typesearch == "classname"){
                name = document.formsearch.sClass.value;
            }
            ajaxfunction("../servStudentManager?action="+action+"&type="+typesearch+"&name="+name+"&start="+start);
            
        }
        function selectCode(){
            typesearch = "mssv";
        }
        function selectName(){
            typesearch = "name";
        }
        function selectClass(){
            typesearch = "classname";
        }
        
        function load() {
            if(start < 0) {
                start = 0;
            }
            if(typesearch == "name") {
                name = document.formsearch.txtName.value;
            } else if(typesearch == "mssv") {
                name = document.formsearch.txtcode.value;
            } else if(typesearch == "classname") {
                name = document.formsearch.sClass.value;
            }
            document.forms["formdown"].action="../DownloadFile?action=studentlist&type="+typesearch+"&name="+name;
            document.forms["formdown"].submit();
        }
        
        function createRequestObject(){
            var req;
            if(window.XMLHttpRequest){
                req = new XMLHttpRequest();
            } else if(window.ActiveXObject){
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } else{
                alert('Your browser is not IE 5 or higher, or Firefox or Safari or Opera');
            }
            
            return req;
        }
 
        function ajaxfunction(pagename){
            if(http){
                http.open("GET", pagename ,true);
                http.onreadystatechange = handleResponse;
                http.send(null);
            }
        }

        function handleResponse(){
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("tableliststudent");
                detail.innerHTML = http.responseText;
                end = document.getElementById("numstuafter").value;
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
                var controller = '../../ManageStudentController?function=delete' + '&data=' + data;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = handleResponseDelete;
                    http.send(null);
                }
            }catch(e) {
                alert(e);
            }
        }
        function handleResponseDelete(){
            if(http.readyState == 4 && http.status == 200){
                location.reload(true);
                //var detail = document.getElementById("tableliststudent");
                //detail.innerHTML = http.responseText;
                //end = document.getElementById("numstuafter").value;
            }
        }
    </SCRIPT>
</html>
