<%-- 
    Document   : ManageStudent
    Created on : 16-11-2011, 22:06:04
    Author     : LocNguyen
--%>

<%@page import="java.util.Date"%>
<%@include file="MenuPDT.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
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
                width: 740px;
                border: 3px solid #73726E;
            }
            #tableliststudent th{
                height: 32px;
                font-weight: bold;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                text-align: center;
            }
            #tableliststudent td{
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                padding: 2px 5px 2px 5px;
                text-align: left;
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
    <body onload="addRow('dataTable');">
        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->

                <INPUT type="button" value="Thêm hàng" onclick="addRow('dataTable')" />
                <INPUT type="button" value="Xóa mục đã chọn" onclick="deleteRow('dataTable')" />
                <INPUT type="submit" value="Hoàn thành" onclick="" />
                <div id="sidebar">
                    <table id="dataTable" width="450px" border="1">
                        <tr style="width: 800px">
                            <td style="width: 10px;"><INPUT type="checkbox" name="chkAll"/></td>
                            <td style="width: 10px; align: center"> STT </td>
                            <td width="15px" align="center"> MSSV </td>
                            <td align="center"> Họ Và Tên </td>
                            <td align="center"> Ngày Sinh </td>
                            <td align="center"> Giới Tính </td>
                            <td align="center"> Quê quán </td>
                            <td align="center"> Địa chỉ </td>
                            <td align="center"> Điện thoại </td>
                            <td align="center"> Email </td>
                            <td align="center"> Lớp </td>
                            <td align="center"> Khoa </td>
                            <td align="center"> Khóa học </td>
                            <td align="center"> Tình Trạng </td>
                            <td align="center"> Bậc học </td>
                            <td align="center"> Ngày nhập học </td>
                            <td align="center"> Loại hình học </td>
                            <td align="center"> Ghi chú</td>
                        </tr>
                    </table>

                </div> 

                <br />
                <hr /><hr />
                <form id="importFromFile" action="#" method="post" name="importFromFile" enctype="multipart/form-data">
                    <u>Thêm Sinh Viên Từ File</u><br/>

                    <table id="tblFromFile">
                        <tr>
                            <td><input type="file" name="txtPath" id="txtPath"></td>
                        </tr>
                        <tr>
                            <td><input type="button" onclick="" value="Hoàn thành."></td>
                        </tr>
                    </table>
                </form><br>

            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <SCRIPT language="javascript">
        function addRow(tableID) {
 
            var table = document.getElementById(tableID);
            var rowCount = table.rows.length;
            var row = table.insertRow(rowCount);
            row.backgroundColor = '#AA25FF';
            row. color = "#FFDD33";
                
            var cellChkb = row.insertCell(0);
            var elementChkb = document.createElement("input");
            elementChkb.type = "checkbox";
            cellChkb.appendChild(elementChkb);
 
            //STT
            var cellIndex = row.insertCell(1);
            cellIndex.innerHTML = rowCount;

            //MSSV
            createNewInputCell(row, 'txtMSSV', 2, '10px');
                
            //Họ Và Tên
            createNewInputCell(row, 'txtName', 3, '10px');
            
            //Ngày Sinh
            // ==> share be update to calenda
            createNewInputCell(row, 'txtBirthDay', 4, '10px');
            
            //Giới Tính
            var cellSex = row.insertCell(5);
            var elementSex = document.createElement("select");
            elementSex.name = "selectSex";
            elementSex.options[0] = new Option("Nam","nam");
            elementSex.options[1] = new Option("N\u1eef","nu");
            cellSex.appendChild(elementSex);
            
            //Quê quán
            createNewInputCell(row, 'txtHomeAddr', 6, '10px');
            
            //Địa chỉ
            createNewInputCell(row, 'txtAddr', 7, '10px');
            
            //Điện thoại
            createNewInputCell(row, 'txtPhone', 8);
            
            //Email
            createNewInputCell(row, 'txtEmail', 9);
            
            //Lớp
            // ==> share be update to select input
            createNewInputCell(row, 'selectCourse', 10);
            
            //Khoa
            // ==> share be update to select input
            createNewInputCell(row, 'selectFaculty', 11);
            
            //Khóa học
            // ==> share be update to select input
            createNewInputCell(row, 'selectCourse', 12);
            
            //Tình Trạng
            // ==> share be update to select input
            createNewInputCell(row, 'selectStatus', 13);
            
            //Bậc học
            // ==> share be update to select input
            createNewInputCell(row, 'selectLevel', 14);
            
            //Ngày nhập học
            // ==> share be update to calendar input
            createNewInputCell(row, 'txtEnterDay', 15);
            
            //Loại hình học
            // ==> share be update to select input
            createNewInputCell(row, 'selectStudyType', 16);
            
            //Ghi chú
            createNewInputCell(row, 'txtPhone', 17);
        }
        
        function createNewInputCell (row, name, index, width) {
            var cell = row.insertCell(index);
            var element = document.createElement("input");
            //if (width != null) {
            //    cell.width = width;
            //}
            cell.setAttribute("border", "2");
            element.style.width = 10;
            element.width = 5;
            element.name = name;
            element.type = "text";
            cell.appendChild(element);
        }
 
        function deleteRow(tableID) {
            try {
                var table = document.getElementById(tableID);
                var rowCount = table.rows.length;
                
                if (rowCount < 3) {
                    alert("Không thể xóa tất cả các dòng");
                    return;
                }

                for(var i=1; i<rowCount; i++) {
                    var row = table.rows[i];
                    row. cells[1].innerHTML = i;
                    var chkbox = row.cells[0].childNodes[0];
                    if(null != chkbox && true == chkbox.checked) {
                        table.deleteRow(i);
                        rowCount--;
                        i--;
                        
                    }
 
                }
            }catch(e) {
                alert(e);
            }
        }
 
    </SCRIPT>
</html>
