<%-- 
    Document   : SubjectManager
    Created on : 09-11-2011, 23:08:36
    Author     : LocNguyen
--%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý tài khoản</title>
        <style media="all" type="text/css">

            #tablelistsubject, #tableaddsubject{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #tablelistsubject th, #tableaddsubject th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #tablelistsubject td, #tableaddsubject td{
                text-align: center;
                background-color: #5F676D;
            }
            #title{
                background-color: #2f4e3d;
                text-align: center;
                padding-top: 12px;
                padding-bottom: 10px;
            }
            #page{
                text-align: center;
            }
            #sidebar {
                height:250px;
                overflow:auto;
            }
            a {
                color: violet;
            }
        </style>
    </head>
    <%
        int numpage;
        try {
            numpage = (Integer) session.getAttribute("numpage_sub");
        } catch (Exception ex) {
            numpage = 1;
        }
        
        String error = (String)session.getAttribute("error");
        session.removeAttribute("error");

        List<Subject> subjects = (List<Subject>) session.getAttribute("list_subject");

    %>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <u><h3>Quản lý Môn học</h3></u>
                </div>
                <hr/><hr/><br>

                <div id = "listsubject">
                    <div style="padding-bottom: 10px;
                         padding-left: 10px;
                         ">
                        <input type = "text" placeholder = "Nhập thông tin tìm kiếm" />
                        <input type = "button" onclick = "" value = "Tìm">
                    </div>
                    <div style="padding-bottom: 5px;
                         padding-left: 10px;
                         ">
                        <input type = "button" onclick = "" value = "Xóa mục đã chọn">
                    </div>
                    <form method="post" action="../../ManageSubjectController?function=add_subject">
                        <div style="padding-bottom: 5px;
                             float: right; padding-right: 15px;
                             ">
                            <input 
                                style="background-color: #f40f0f;"
                                type = "submit" value = "Thêm môn học">
                        </div>
                    </form>
                    <div id="error">
                        <%
                            if ((error != null) && !error.isEmpty()) { %>
                                <%= error %>
                            <%}
                        %>
                    </div>
                    <table id = "tablelistsubject" name = "tablelistsubject">
                        <tr>
                        <th><INPUT type = "checkbox" 
                                   name = "chkAll" 
                                   onclick = "selectAll('tablelistsubject', 0)"/></th>
                        <th> STT </th>
                        <th> Mã MH </th>
                        <th> Tên Môn học </th>
                        <th> Số TC </th>
                        <th> Số TCLT </th>
                        <th> Số TCTH </th>
                        <th> Sửa </th>
                        <th> Xóa </th>
                        <%--Should be sorted when click on table's header--%>
                        </tr>
                        <%if ((subjects != null) && !subjects.isEmpty()) {%>
                        <% for (int i = 0; i < subjects.size(); i++) {%>
                        <tr>
                        <td><INPUT type="checkbox" name="chk<%= i%>"/></td>
                        <td> <%= (i + 1)%> </td>
                        <td> <%= subjects.get(i).getId()%> </td> 
                        <td> <%= subjects.get(i).getSubjectName()%> </td>
                        <td> <%= subjects.get(i).getnumTCLT()%> </td>
                        <td> <%= subjects.get(i).getnumTCLT()%> </td>
                        <td> <%= subjects.get(i).getnumTCTH()%> </td>
                        <td><a href = "../../ManageSubjectController?function=edit_subject&subject_code=<%= subjects.get(i).getId()%>">Sửa</a></td>
                        <td><a href = "../../ManageSubjectController?function=delete_single_subject&ajax=false&currentpage=1&subject_code=<%= subjects.get(i).getId()%>">Xóa</a></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                    <div id="page">
                        <input type="button" value="|<<" onclick="firstPage()"/>- 
                        <input type="button" value="<<" onclick="prePage()"/>-
                        <input type="button" value=">>" onclick="nextPage()"/>-
                        <input type="button" value=">>|" onclick="endPage()"/>
                        <input type="hidden" value="<%= numpage%>" id = "numpage" />
                    </div>
                    <br/>
                </div>

                <div id="editsubject">
                    <table id="tableeditsubject">
                    </table>
                </div>

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
        var currentpage = 1;
        var http = createRequestObject();
        var numpage = document.getElementById("numpage").value;
        function firstPage(){
            currentpage = 1;
            sendRequest();
        }
        function prePage(){
            currentpage--;
            if(currentpage < 1)
                currentpage = 1;
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
                submitSearchSubject("../../ManageSubjectController?function=list_subject&ajax=true&currentpage=" + currentpage);
            }
        }
        
        /*
         * Util functions for get data from a table
         * 
         * @Param tableId table's id
         */
        function getDataStringFromTable(tableID) {
            var datas = '';
            var selectOne = false;
            try {
                var table = document.getElementById(tableID);
                var rowCount = table.rows.length;
                for(var i = 1; i < rowCount; i++) {
                    var row = table.rows[i];
                    var chkbox = row.cells[0].childNodes[0];
                    if((null != chkbox) && (true == chkbox.checked)) {
                        if (validateSubjectValue(row) == false) {
                            alert('Vui lòng nh\u1eadp đầy thông tin cần thiết cho dòng ' + i);
                            return;
                        }
                        if (selectOne == false)
                            selectOne = true;
                        var elTableCells = row.getElementsByTagName('td');
                        var currentData = '';
                        currentData += elTableCells[2].childNodes[0].value + ','; //Ma Mon Hoc
                        currentData += elTableCells[3].childNodes[0].value + ','; //Ten Mon Hoc
                        currentData += elTableCells[4].childNodes[0].value + ','; //So TCLT
                        currentData += row.cells[5].childNodes[0].value; //So TCTH
                        
                        if (i < (rowCount - 1)) {
                            currentData += ';';
                        }
                        
                        datas += currentData;
                    }
                }
            }catch(e) {
                alert(e);
            }
            if (selectOne == false) {
                alert('Vui lòng ch\u1ecdn ít nhất một hàng.');
                return;
            }
            
            return datas;
        }
        
        /**
         * Validate the input data is correct.
         * 
         * @Param row row of table.
         */
        function validateSubjectValue(row) {
            var elTableCells = row. getElementsByTagName('td');
            if ((elTableCells[2].childNodes[0].value == '') ||
                (elTableCells[3].childNodes[0].value == '') ||
                (elTableCells[4].childNodes[0].value == '')) {
                return false;
            }
            
            return true;
        }
        
        function submitSearchSubject(pagename) {
            if(http){
                http.open("GET", pagename ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
            }
        }
        
        function searchResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tablelistsubject");
                detail.innerHTML=http.responseText;
            }
        }
    </script>
</html>