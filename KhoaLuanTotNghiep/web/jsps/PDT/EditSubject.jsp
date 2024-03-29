<%-- 
    Document   : EditSubject
    Created on : 13-11-2011, 11:11:38
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý tài khoản</title>
        <style media="all" type="text/css">
            /** CSS definition **/
        </style>
    </head>
    <%
        // Validate Access role
        ClientValidate.validateAcess(AccountType.ADMIN, session, response);

        String subjectStr = "";
        String preSubStr = "";
        String error = "";
        List<String> subject = (List<String>) session.getAttribute("subjects");
        List<String> preSub = (List<String>) session.getAttribute("pre_subjects");
        Subject sub = (Subject) session.getAttribute("subject");
        error = (String) session.getAttribute("error");

        if (subject != null) {
            for (int i = 0; i < subject.size(); i++) {
                subjectStr += subject.get(i);
                if (i < (subject.size() - 1)) {
                    subjectStr += ";";
                }
            }
        }
        
        if (preSub != null) {
            for (int i = 0; i < preSub.size(); i++) {
                preSubStr += preSub.get(i);
                if (i < (preSub.size() - 1)) {
                    preSubStr += ";";
                }
            }
        }
    %>
    <body onload="initPreSubForEdit('table_mhtq')">
        <!--Div Wrapper-->
        <div id="wrapper">
            <%--Menu--%>
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Sửa môn học
                </div>
                <br /><br />
                <input type="hidden" id="subs" value="<%= subjectStr%>">
                <input type="hidden" id="pre_subs" value="<%= preSubStr%>">
                <form action="" method="post">
                    <b><u>Vui lòng cập nhật thông tin môn học và nhấn "Hoàn thành"</u></b>
                    <table id="table_mh" >
                        <tr>
                        <td> Mã MH </td>
                        <td> <input type="text" 
                                    id="txt_code" 
                                    readonly="readonly" 
                                    value="<%= sub.getId()%>"
                                    />
                                     </td>
                        <td id ="error_code"> </td>
                        </tr>
                        <tr>
                        <td> Tên MH </td>
                        <td> <input type="text"
                                    id="txt_name"
                                    value="<%= sub.getSubjectName()%>"
                                    /> </td>
                        <td id ="error_name"> </td>
                        </tr>
                        <tr>
                        <td> Số TCLT </td>
                        <td> <input type="text" 
                                    id="txt_tclt"
                                    value="<%= sub.getnumTCLT()%>"
                                    /> </td>
                        <td id ="error_tclt"> </td>
                        </tr>

                        <tr>
                        <td> Số TCTH </td>
                        <td> <input type="text" 
                                    id="txt_tcth"
                                    value="<%= sub.getnumTCTH()%>"
                                    /> </td>
                        <td id ="error_tcth"> </td>
                        </tr>

                        <tr>
                        <td> Môn học tiên quyểt </td>
                        <td>
                            <table id="table_mhtq"
                                   name="table_mhtq"
                                   >
                            </table>
                            <input 
                                type="button" 
                                onclick="addRow('table_mhtq')" 
                                value="+" 
                                />
                        </td>
                        </tr>
                    </table>
                    <i style="font-size:11px">Nếu chọn nhiều môn học tiên quyết trùng nhau, chỉ 1 môn được tính.</i>
                    <div class="button-1">
                        <span class="atag" onclick="submitEditSubject('../../ManageSubjectController?function=edit_subject&ajax=true')"> <img src="../../imgs/check.png"/>Update</span>
                    </div>
                    <div id="insert_from_table_error"
                         style="color: #cc0033;">

                        <% if ((error != null) && !error.isEmpty()) {%>
                        <%= error %>
                        <% } %>
                    </div>
                </form>
                <br /><br />
            </div><!--End Contents-->

            <br/> <br/>
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
            var isValidated = true;
            if ((id == null) || (id == '') 
                || (id.indexOf(" ", 0)) >= 0
                || (id.indexOf("-", 0)) >= 0) {
                document.getElementById('error_code').innerHTML = 'Mã MH không \u0111úng.';
                isValidated = false;
            } else {
                document.getElementById('error_code').innerHTML = '';
            }
    
            if ((name == null)
                || (name.length <= 0)
                || (name.indexOf("-", 0) >= 0)) {
                document.getElementById('error_name').innerHTML = 'Tên môn h\u1ecdc không đúng.';
                isValidated = false;
            } else {
                document.getElementById('error_name').innerHTML = '';
            }
    
            //Check tclt & tcth is number
            //
            // ...
            //
            return isValidated;
        }
        
        function submitEditSubject(pageName) {
            var validate = checkData();
            if (validate == false) {
                return;
            }
            
            var data = getDataStringFromTable();
            //if ((data == null) || (data.length < 1)) {
            //    alert("D\u1eef li?u nh?p không đúng.");
            //    return;
            //}
            
            var controller = pageName + '&data=' + data;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = hdlRespForAddSubFromTable;
                http.send(null);
            }
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
            //createNewButton(row, 'btn_disselect_' + rowCount, 1, 'B?');
        }
        
        /*
         * Util functions for get data from a table
         * 
         * @Param tableId table's id
         */
        function getDataStringFromTable() {
            var id = document.getElementById('txt_code').value;
            var name = document.getElementById('txt_name').value;
            var tclt = document.getElementById('txt_tclt').value;
            var tcth = document.getElementById('txt_tcth').value;
            var mark = '-';
                
            // Init subject information.
            var datas = id + mark
                + name + mark
                + tclt + mark
                + tcth;

            // Init PreSubject information
            try {
                var table = document.getElementById('table_mhtq');
                var rowCount = table.rows.length;
                for(var i = 0; i < rowCount; i++) {
                    var row = table.rows[i];
                    var chkbox = row.cells[1].childNodes[0];
                    if((null != chkbox) && (true == chkbox.checked)) {
                        datas += mark;
                        var elTableCells = row.getElementsByTagName('td');
                        var presub = elTableCells[0].childNodes[0].value;
                        datas += presub.split('-', 1)[0];
                    }
                }
            }catch(e) {
                alert(e);
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
        
        function initPreSubForEdit(tableID) {
            var valuesStr = document.getElementById('subs').value;
            var preSubStr = document.getElementById('pre_subs').value;//pre_subs
            var table = document.getElementById(tableID);
            var rowCount = table.rows.length;
            
            var preSubArray = preSubStr.split(";")
            for (var i = 0; i < preSubArray.length; i ++) {
                var row = table.insertRow(rowCount);
                row.backgroundColor = '#AA25FF';
                row. color = "#FFDD33";
                //Selection
                var values = valuesStr.split(";");
                createNewSelectionCell(row, 'selectSubject' + rowCount, 0, values, preSubArray[i]);
        
                var cellChkb = row.insertCell(1);
                var elementChkb = document.createElement("input");
                elementChkb.type = "checkbox";
                elementChkb.checked = true;
                cellChkb.appendChild(elementChkb);
            }
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
