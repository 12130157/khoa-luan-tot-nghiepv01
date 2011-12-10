<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
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

            #table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #table th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #table td{
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
        String subjectStr = "";
        List<String> subject = (List<String>) session.getAttribute("subjects");
        
        if (subject != null) {
            for (int i = 0; i < subject.size(); i ++) {
                subjectStr += subject.get(i);
                if (i < (subject.size() - 1)) {
                    subjectStr += ";";
                }
            }
        }
        
                
    %>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->

                <hr/><hr/>
                <div id="title">
                    <u><h3>Thêm môn học</h3></u>
                </div>
                <hr/><hr/><br>

                <input type="hidden" id="subs" value="<%= subjectStr%>">
                <form action="" method="post">
                    <div style="padding-bottom: 20px;
                         margin-left: 40px;">
                        <input type="button" onclick="submit()" value="Hoàn thành" />
                        <input type="button" onclick="clearData()" value="Xóa" />
                    </div>
                    <table id="table_mh">
                        <tr>
                        <td> Mã MH </td>
                        <td> <input type="text" id="txt_code"/> </td>
                        <td id ="error_code"> </td>
                        </tr>
                        <tr>
                        <td> Tên MH </td>
                        <td> <input type="text" id="txt_name"/> </td>
                        <td id ="error_name"> </td>
                        </tr>
                        <tr>
                        <td> Số TCLT </td>
                        <td> <input type="text" id="txt_tclt"/> </td>
                        <td id ="error_tclt"> </td>
                        </tr>

                        <tr>
                        <td> Số TCTH </td>
                        <td> <input type="text" id="txt_tcth"/> </td>
                        <td id ="error_tcth"> </td>
                        </tr>

                        <tr>
                        <td> Môn học tiên quyết </td>
                        <td>
                            <table id="table_mhtq" name="table_mhtq">
                            </table>
                            <input 
                                type="button" onclick="addRow('table_mhtq')" value="+" 
                                />
                        </td>
                        </tr>
                    </table>

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
        var http = createRequestObject();
        
        /**
         *
         * Clear entered information
         * 
         **/
        function clearData() {
            try {
                document.getElementById('txt_code').value = '';
                document.getElementById('txt_name').value = '';
                document.getElementById('txt_tclt').value = '';
                document.getElementById('txt_tcth').value = '';
                
                document.getElementById('error_code').innerHTML = '';
                document.getElementById('error_name').innerHTML = '';
                document.getElementById('error_tclt').innerHTML = '';
                document.getElementById('error_tcth').innerHTML = '';
                
                var table_mhtq = document.getElementById('table_mhtq');
                var rowCount_mhtq = table_mhtq.rows.length;
                for(var j = rowCount_mhtq - 1; j >= 0; j--) {
                    table_mhtq.deleteRow(j);
                }
            }catch(e) {
                alert(e);
            }
        }
        
        function checkData() {
            var id = document.getElementById('txt_code').value;
            var name = document.getElementById('txt_name').value;
            var tclt = document.getElementById('txt_tclt').value;
            var tcth = document.getElementById('txt_tcth').value;

            var isValidated = false;
            if (id.indexOf(' ', 0) >= 0) {
                document.getElementById('error_code').innerHTML = 'Mã MH không \u0111úng.';
                isValidated = true;
            } else {
                document.getElementById('error_code').innerHTML = '';
            }
    
            if (name.length <= 0) {
                document.getElementById('error_name').innerHTML = 'Tên môn h\u1ecdc không đúng.';
                isValidated = true;
            } else {
                document.getElementById('error_name').innerHTML = '';
            }
    
            //Check tclt & tcth is number
            //
            // ...
            //
            return isValidated;
        }
        
        function submit() {
            var validate = checkData();
            if (validate == false) {
                return;
            }
            
            //var data = getDataStringFromTable(tableID);
            //if ((data == null) || (data.length < 1)) {
            //    alert("D\u1eef liệu nhập không đúng.");
            //    return;
            //}
            
            //var controller = pageName + '&data=' + data;
            //if(http){
            //    http.open("GET", controller ,true);
            //    http.onreadystatechange = hdlRespForAddSubFromTable;
            //    http.send(null);
                
            //}
        }
        
        /**
         * Add new row to table
         * @Param tableID table's id
         * @Param valuesStr list of subject
         *          for selection creation
         **/
        function addRow(tableID) {
            var valuesStr = document.getElementById('subs').value;
            var table = document.getElementById(tableID);
            var rowCount = table.rows.length;
            var row = table.insertRow(rowCount);
            row.backgroundColor = '#AA25FF';
            row. color = "#FFDD33";
                
            //Selection
            //createNewInputCell(row, 'txtSubId', 1);
            //var values = new Array('Mon hoc 1', 'Mon Hoc 2', 'Mon Hoc 3');
            var values = valuesStr.split(";");
            createNewSelectionCell(row, 'selectSubject' + rowCount, 0, values);
        
            var cellChkb = row.insertCell(1);
            var elementChkb = document.createElement("input");
            elementChkb.type = "checkbox";
            elementChkb.checked = true;
            cellChkb.appendChild(elementChkb);
            //createNewButton(row, 'btn_disselect_' + rowCount, 1, 'Bỏ');
        }
        
        /*
         * Util functions for get data from a table
         * 
         * @Param tableId table's id
         */
        function getDataStringFromTable() {
            document.getElementById('txt_code').value = '';
                document.getElementById('txt_name').value = '';
                document.getElementById('txt_tclt').value = '';
                document.getElementById('txt_tcth').value = '';
                
                document.getElementById('error_code').innerHTML = '';
                document.getElementById('error_name').innerHTML = '';
                document.getElementById('error_tclt').innerHTML = '';
                document.getElementById('error_tcth').innerHTML = '';
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
    
        /**
         * Respone when submit insert subject from a table
         */
        function responeHandler(){
            if(http.readyState == 4 && http.status == 200){
                //var detail=document.getElementById("insert_from_table_error");
                //detail.innerHTML=http.responseText;
            }
        }
        /**
         * Send request to submit insert subject(s)
         * from a table.
         * 
         * @Param pageName point to controller.
         * @Param tableID table's id.
         **/
        function addSubjectsFromTable(pageName, tableID) {
            var data = getDataStringFromTable(tableID);
            if ((data == null) || (data.length < 1)) {
                alert("D\u1eef liệu nhập không đúng.");
                return;
            }
            var controller = pageName + '&data=' + data;
            alert(http);
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = hdlRespForAddSubFromTable;
                http.send(null);
                
            }
        }
        
        /**
         * Respone when submit insert subject from a table
         */
        function hdlRespForAddSubFromTable(){
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("insert_from_table_error");
                detail.innerHTML=http.responseText;
            }
        }
    </script>
</html>