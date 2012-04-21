<%-- 
    Document   : TrainClassManager
    Created on : 01-04-2012, 08:24:47
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page import="java.util.List"%>
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
            numpage = (Integer) session.getAttribute("numpage");
        } catch (Exception ex) {
            numpage = 1;
        }
        
        String error = (String)session.getAttribute("error");
        session.removeAttribute("error");
        
        List<TrainClass> openedClazzs = (List<TrainClass>) session.getAttribute("train-clazzs");
                
    %>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <u><h3>Danh sách lớp học đã có</h3></u>
                </div>
                <hr/><hr/><br>

                <%--Form add new Train subject--%>
                <div id="">
                    <input type="button" value="Add" onclick="showAddTrainClassForm('id_add_train_class_form')"
                    <div id="id_add_train_class_form">
                          
                    </div>
                </div>
                
                <div id = "list-train-class">
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
                    
                    <div id="error">
                        <%
                            if ((error != null) && !error.isEmpty()) { %>
                                <%= error %>
                            <%}
                        %>
                    </div>
                    
                    <table id = "table-list-train-class" name = "table-list-train-class">
                        <tr>
                        <th><INPUT type = "checkbox" 
                                   name = "chkAll" 
                                   onclick = "selectAll('tablelistsubject', 0)"/></th>
                        <th> STT </th>
                        <th> Môn Học </th> <!-- Tên môn học-->
                        <th> Giảng viên </th>
                        <th> Số lượng SV </th>
                        <th> Đã ĐK </th>
                        <th> Ngày học </th>
                        <th> Ca học </th>
                        <th> Phòng học </th>
                        <th> Ngày thi </th>
                        <th> Sửa </th>
                        <th> Xóa </th>
                        <%--Should be sorted when click on table's header--%>
                        </tr>
                        <%if ((openedClazzs != null) && (!openedClazzs.isEmpty())) {%>
                        <% for (int i = 0; i < openedClazzs.size(); i++) {%>
                        <tr>
                        <td><INPUT type="checkbox" name="chk<%= i%>"/></td>
                        <td> <%= (i + 1)%> </td>
                        <td> <%= openedClazzs.get(i).getSubjectName()%> </td>
                        <td> <%= openedClazzs.get(i).getLectturerName()%> </td>
                        <td> <%= openedClazzs.get(i).getNumOfStudent()%> </td>
                        <td> <%= openedClazzs.get(i).getNumOfStudentReg() %> </td>
                        <td> <%= openedClazzs.get(i).getStudyDate() %> </td>
                        <td> <%= openedClazzs.get(i).getShift() %> </td>
                        <td> <%= openedClazzs.get(i).getTestRoom() %> </td>
                        <td> <%= openedClazzs.get(i).getTestDate() %> </td>

                        <td><a href = "../../ManageSubjectController?function=edit_subject&ajx=false&subject_code=?">Sửa</a></td>
                        <td><a href = "../../ManageSubjectController?function=delete_single_subject&ajax=false&currentpage=1&subject_code=?">Xóa</a></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                    <div id="page">
                        <input type="button" value="|<<" onclick="firstPage()"/>- 
                        <input type="button" value="<<" onclick="prePage()"/>-
                        <input type="button" value=">>" onclick="nextPage()"/>-
                        <input type="button" value=">>|" onclick="endPage()"/>
                        <input type="hidden" value="<%= numpage %>" id = "numpage" />
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