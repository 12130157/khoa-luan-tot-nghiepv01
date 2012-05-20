<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.FileInfo"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    List<FileInfo> files = (List<FileInfo>)session.getAttribute("files");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Quản lý File</title>
        <style media="all" type="text/css">
            /* CSS definition */
        </style>
    </head>
    <body>
        <%--Div Wrapper--%>
        <div id="wrapper">
            <%@include file="MenuGV.jsp"%>
            <%--Main Navigation--%>
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><%--End Navigation--%>
            <%--Main Contents--%>
            <div id="content">
                <br /><br />
                <div id="upload-score">
                    <i><u>Giảng viên vui lòng nộp bảng điểm về cho PĐT theo đúng định dạng file</u></i>
                    <ul>
                        <li><i> (*) Chỉ chấp nhận file dạng .xls</i></li>
                        <li><i> (*) Tên file trùng với mã lớp</i></li>
                    </ul>
                    <br />
                    <div class="clear"></div>
                    <%--
                    <div id="" class="clear-left">
                        Chọn lớp:
                        <select>
                            <option> Tobe retrieve data </option>
                        </select>
                    </div>
                    --%>
                    <div class="clear"></div>
                    <div class="clear-left">
                        <form id="upload-file" 
                              action="../../FileUploadController?function=upload"
                              method="post" name="uploadfile" enctype="multipart/form-data">
                            <u>Chọn File</u>
                            <table id="tblFromFile">
                                <tr><td><input type="file" name="txtPath" id="txtPath" accept="application/xls" /></td></tr>
                                <tr><td><input type="submit" value="Tải lên" /></td></tr>
                            </table>
                        </form>
                    </div>
                    <%-- Files uploaded --%>
                    <div class="clear-left" style="padding-left: 25px;">
                        <u>Files đã uploaded:</u>
                        <table class="general-table" style="width: 350px;">
                            <tr>
                                <th> STT </th>
                                <th> Tên file </th>
                            </tr>
                            <%
                            if ((files != null) && !files.isEmpty()) {
                                for (int i = 0; i < files.size(); i++) {
                            %>
                                <tr>
                                    <td> <%= (i+1)%> </td>
                                    <td> <%= files.get(i).getFileName() %> </td>
                                </tr>
                            <%
                                }
                            }
                            %>
                        </table>
                    </div>
                    <%--Show file format--%>
                    <div class="clear"></div>
                    <br /><br />
                    <div id="btn-show-file-format" class="clear-right">
                        <span onclick="showStuff('file-format-view', 'btn-show-file-format', 'File format', 'Ẩn')"
                              class="atag"> File format 
                        </span>
                    </div>
                    <div id="file-format-view" class="short_sidebar">
                        <img src="../../imgs/form_format/import_score_format_file.PNG"/>
                    </div>
                </div>
            </div><%--End Contents--%>
            <%--Footer--%>
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <SCRIPT language="javascript">
        window.onload = doAfterPageLoaded; 
        function doAfterPageLoaded() {
            try {
                hiddenValue = document.getElementById("btn-hide-import-form").value;
            } catch (err) {
                //
            }
            hideStuff('file-format-view', 'btn-show-file-format', 'File format', 'Ẩn');
            //showStuff('import-lecturer-from-file', 'btn-show-import-file', 'Thêm từ file', 'Ẩn');
        }
    </script>
</html>
