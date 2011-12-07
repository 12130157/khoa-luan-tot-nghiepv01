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
                         padding-left: 10px;">
                        <input type = "text" placeholder = "Nhập thông tin tìm kiếm" />
                        <input type = "button" onclick = "" value = "Tìm">
                    </div>
                    <table id = "tablelistsubject" name = "tablelistsubject">
                        <tr>
                        <th><INPUT type = "checkbox" 
                                   name = "chkAll" 
                                   onclick = "selectAll('tablelistsubject')"/></th>
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
                        <td><a href = "../../ManageSubject?function=edit_subject&subject_code=<%= subjects.get(i).getId()%>">Sửa</a></td>
                        <td><a href = "../../ManageSubject?function=delete_single_subject&subject_code=<%= subjects.get(i).getId()%>">Xóa</a></td>
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
                <input type="hidden" value="<%= numpage%>">

                <hr/><hr/>
                <div id="title">
                    <u><h3>Thêm môn học</h3></u>
                </div>
                <hr/><hr/><br>
                <div id="addsubject">
                    <div style="padding-left: 20px; padding-bottom: 10px; padding-top: 15px;">
                        <input type="button" 
                               onclick="addRow('tableaddsubject')" 
                               value="Thêm dòng"
                               />
                        <input type="button" 
                               onclick="deleteRow('tableaddsubject')" 
                               value="Xóa mục đã chọn"
                               />
                        <input type="button" 
                               onclick="submitAddSubject('tableaddsubject')" 
                               value="Hoàn thành"
                               />
                    </div>
                    <div id ="sidebar">
                        <table id="tableaddsubject" name="tableaddsubject">
                            <tr>
                            <th><input type = "checkbox" 
                                       name = "chkAll" 
                                       onclick = "selectAll('tableaddsubject')"/></th>
                            <th> STT </th>
                            <th> Mã MH </th>
                            <th> Tên Môn học </th>
                            <th> Số TC </th>
                            <th> Số TCLT </th>
                            <th> Số TCTH </th>
                            </tr>
                        </table>
                    </div>

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
    <script  type = "text/javascript" >
        var currentpage = 1;
        var http = createRequestObject();
        numpage = document.getElementById("numpage").value;
        function firstPage(){
            currentpage = 1;
            sendRequest();
        }
        function prePage(){
            currentpage--;
            if(currentpage < 1) currentpage = 1;
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
                ajaxfunction("../../AccountController?action=Filter&curentPage="+currentpage);
            }
        }
        
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

            //Ma Mon hoc
            createNewInputCell(row, 'txtSubId', 2);
                
            //Ten mon hoc
            createNewInputCell(row, 'txtSubName', 3);
            //So Tin Chi
            createNewInputCell(row, 'txtNumTC', 4);
            
            //So TCLT
            createNewInputCell(row, 'txtNumTCLT', 5);
            
            //So TCTT
            createNewInputCell(row, 'txtNumTCTH', 6);

            //createNewSelectionCell(row, 'selectSex', 5, new Array("Nam", "Nữ"));
            
        }
    </script>
</html>