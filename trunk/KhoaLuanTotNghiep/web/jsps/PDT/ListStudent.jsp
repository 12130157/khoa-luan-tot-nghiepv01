<%-- 
    Document   : ListStudent
    Created on : 16-11-2011, 22:06:24
    Author     : LocNguyen
--%>

<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">

<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.DAO.DAOFactory"%>
<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<uit.cnpm02.dkhp.model.Class> clazz = DAOFactory.getClassDao().findAll();
    List<Faculty> faculties = DAOFactory.getFacultyDao().findAll();
    
    String searchBy = "All";
    String searchValue = "All";
    
    String sortBy = "MSSV";
    String sort = "ASC";
    
    int currentPage = 0;
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
                <form id = "formsearch" name="formsearch" action="#" method="post">
                    <table>
                        <tr>
                            <td><input type="radio" name="radiooption" checked="true" onclick="selectAll()" ></td>
                            <td>All</td>
                        </tr>
                        <tr>
                            <td><input type="radio" name="radiooption" onclick="selectClass()"></td>
                            <td>
                                <select name="sClass" id="sClass">
                                    <%
                                    if ((clazz != null) && (clazz.size() > 0)) {
                                        for (int i = 0; i < clazz.size(); i++) {%>
                                        <option value="<%=clazz.get(i).getId()%>"><%=clazz.get(i).getId()%></option>
                                        <%}
                                    }%>
                                </select> Tìm theo lớp
                            </td>
                        </tr>
                        <tr>
                            <td><input type="radio" name="radiooption" onclick="selectCode()"></td>
                            <td>
                                <input type="text" name="txtcode" id="txtcode"> Tìm theo MSSV
                            </td>
                        </tr>
                        <tr>
                            <td><input type="radio" name="radiooption" onclick="selectName()"></td>
                            <td>
                                <input type="text" name="txtName" id="txtName"> Tìm theo tên
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2"><input type="button" onclick="search()" value="Tìm Kiếm"></td>
                        </tr>
                    </table>
                </form>
                <p align="right"><b><a href="../servStudentManager?action=create">Tiếp nhận sinh viên</a></b></p>
                <hr><hr>
                
                <form id="formdown" name="formdown" action="../DownloadFile?action=test" method="post">
                    Danh sách sinh viên:<br/>
                    <table id="tableliststudent" name="tableliststudent">
                        <tr>
                            <th> STT </th> <th> <a href=""> MSSV </a> </th><th> <a href=""> Họ Tên </a> </th><th> <a href=""> Lớp </a> </th><th>Ngày sinh</th><th>Giới tính</th><th>Loại</th><th>Sửa</th><th>Xóa</th><th>Cập nhật</th>
                            <%--Should be sorted when click on table's header--%>
                        </tr>
                        <%-- for (int i = 0; i < listStudent.size(); i++) { --%>
                        <tr>
                            <td> STT </td>
                            <td> MSSV </td> 
                            <td> Ho Ten </td>
                            <td> Lop </td>
                            <td> Ngay sinh </td>
                            <td> Gioi Tinh </td>
                            <td> Loai </td>
                            <td><a href="">Sửa</a></td>
                            <td><a href="">Xóa</a></td>
                            <td>Không</td>
                            <%-- } --%>
                        </tr>
                        <%--}--%>
                    </table>
                    <input style="position:absolute; left:650px;" type="button" value="|<<" onclick="">
                    <input style="position:absolute; left:680px;" type="button" value="<<" onclick="">
                    <input style="position:absolute; left:710px;" type="button" value=">>" onclick="">
                    <input style="position:absolute; left:740px;" type="button" value=">>|" onclick=""><br>
                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

</html>
