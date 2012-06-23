<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.FileInfo"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    List<FileInfo> files = (List<FileInfo>)session.getAttribute("files");
    String username = (String)session.getAttribute("username");
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
                <div class="range">
                    <h3><span id="btn-show-form-upload" class="atag">
                            Gửi bảng điểm
                    </span></h3>
                    <div id="upload-score">
                        <i><u>Giảng viên vui lòng nộp bảng điểm về cho PĐT theo đúng định dạng file</u></i>
                        <ul>
                            <li><i> (*) Chỉ chấp nhận file dạng .xls</i></li>
                            <li><i> (*) Tên file trùng với mã lớp</i></li>
                        </ul>
                        <br />
                        <div class="clear"></div>
                    
                        <div class="clear"></div>
                        <div class="clear-left">
                            <form id="upload-file" 
                                  action="../../FileUploadController?function=upload"
                                  method="post" name="uploadfile" enctype="multipart/form-data">
                                <u>Chọn File</u>
                                <div class="clear"> </div>
                                <input type="file" name="txtPath" id="txtPath" accept="application/xls" />
                                <div class="clear"> </div>
                                <br />
                                <input type="button" value="Tải lên" onclick="submitUploadFile()" />
                            </form>
                            <%-- Validate result --%>
                            <div id="validate-filename-result">
                            </div>
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
                    </div>
                </div>
                <div class="clear"></div>
                <input type="hidden" id="username-value" value="<%= username %>" />
                <%-- Show list trainclass of lecturer --%>
                <div class="range">
                        <h3><span id="btn-show-class-list" class="atag">
                            Các lớp <%= "<b> " + username + " </b>" %> đang dạy
                        </span></h3>
                        <div id="list-trainclass" style="display: none;">
                        </div>
                    </div>
                <%--Show file format--%>
                <div class="clear"></div>
                <div class="range">
                        <h3><span id="btn-show-file-format" class="atag">
                            File format
                        </span></h3>
                        <div id="file-format-view" class="short_sidebar" style="display: none;">
                            <img src="../../imgs/form_format/import_score_format_file.PNG"/>
                        </div>
                    </div>
                <div class="clear"></div>
                <br />
            </div><%--End Contents--%>
            <%--Footer--%>
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <SCRIPT language="javascript">
        var http = createRequestObject();
        var loadTrainClassOfLecturer = false;
        
        // Slide toggle form list trainclass
        $("#btn-show-class-list").click(function () {
            $('#list-trainclass').slideToggle(500);
            if (!loadTrainClassOfLecturer) {
                var lecturerId = document.getElementById("username-value").value;
                getTrainClass(lecturerId);
                loadTrainClassOfLecturer = true;
            }
        });
        // Slide toggle form file format
        $("#btn-show-file-format").click(function () {
            $('#file-format-view').slideToggle(500);
        });
        
         // Slide toggle form File upload
        $("#btn-show-form-upload").click(function () {
            $('#upload-score').slideToggle(500);
        });
        
        function getTrainClass(lecturerId) {
            var controller = "../../LecturerPrivateController?function=get-train-class"
                    + "&lecturer=" + lecturerId;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = getTrainClassHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function getTrainClassHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("list-trainclass");
                detail.innerHTML = http.responseText;
            }
        }
        
        function submitUploadFile() {
            // Validate file input
            var path = document.getElementById("txtPath").value;
            if (path.length == 0) {
                alert("Hãy chọn file trước khi submit.");
                return;
            }
            
            if (path.indexOf(".xls") < 0) {
                alert("Hiện tại hệ thống chỉ hỗ trợ file .xls");
                return;
            }
            
            var controller = "../../FileUploadController?function=validate-filename"
                            + "&txtPath=" + path;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = getValidateFileNameHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function getValidateFileNameHandler() {
            if(http.readyState == 4 && http.status == 200){
                var result = http.responseText;
                if (result.substring(0, 5) == "error") {
                    var msgDialog = document.getElementById("validate-filename-result");
                    msgDialog.innerHTML = result;
                    return;
                } else {
                    document.forms["upload-file"].submit();
                }
            }
        }
        
    </script>
</html>
