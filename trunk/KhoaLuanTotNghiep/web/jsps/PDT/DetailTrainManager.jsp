<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.Calendar"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết giảng dạy</title>
        <style media="all" type="text/css">
            #report-range {
                /*width: 100%;*/
                float: left;
                padding-left: 10px;
                padding-right: 10px;
                padding-bottom: 25px;
                text-align: center;
            }
            #list-Lecturer {
                float: left;
                max-height:350px;
                overflow:auto;
            }
            #Lecturer-detail {
                float: none;
                padding-left: 50px;
                border-width: 1px;
                border-color: #00ff00;
            }
            #report-range td {
                text-align: left;
            }
        </style>
    </head>

    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Quản lý chi tiết giảng dạy
                </div>
                <div style="float: left;">
                    <div id="searchbox" action="#">
                        <input id="search" type="text" onKeyPress="keypressed()" placeholder="Nhập tên GV" />
                        <input type="button" id="submit" onclick="FindLecturer()" value="Tìm kiếm" />
                    </div>
                </div>
                <div class="clear"></div>
                <div>
                    <div id="list-Lecturer" style="float: left;">
                        <br />
                        <table id ="list-LecturerFind" name = "list-LecturerFind" class="general-table">
                        </table>
                    </div>
                    
                    <div id="Lecturer-detail" style="float: left; margin-left: 15px;">
                    </div>
                </div>
                <div class="clear"></div>
                <div class="range">
                    <h3><span id="btn-infor-manage" class="atag" >Hướng dẫn</span></h3>
                    <div id="info-manage">
                        <br/>
                        <p>
                            <b>Cho phép PĐT quản lý thông tin thông tin giảng dạy của GV</b> <br />
                            <i>
                            <ul>
                                <li>- PĐT tìm GV cần xem bằng cách nhập gần đúng tên GV</li>
                                <li>- PĐT xem chi tết thông tin đăng ký học phần bằng cách chọn SV tương ứng trong danh sách SV tìm thấy</li>
                                <li>- Hệ thống hỗ trợ PĐT cập nhật môn học phụ trách của GV</li>
                            </ul>
                            </i>
                        </p>
                    </div>
                </div>
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <script  type = "text/javascript" >
        $("#btn-infor-manage").click(function () {
            $('#info-manage').slideToggle(500);
        });

        var http = createRequestObject();
        //
        // Search Student
        //
        function keypressed()
        { 
            if(event.keyCode=='13')
            {
                FindLecturer();
            } 
        }
        function FindLecturer(){
            var search = document.getElementById("search").value;
            if (http) {
                http.open("GET", "../../DetailTrainManager?action=search_lecturer&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
            }
        }
        
        function handleResponseFindStudent() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("list-LecturerFind");
                detail.innerHTML = http.responseText;
                formatGeneralTable();
            }
        }
         
        //
        // Get student Report
        //
        function getDetailLecturerReport(lecturerCode) {
            //var search = document.getElementById("search-student").value;
            if (http) {
                http.open("GET", "../../DetailTrainManager?action=lecturer-report&value="
                    + lecturerCode, true);
                http.onreadystatechange = handleResponseStudentReport;
                http.send(null);
            }
        }
        function handleResponseStudentReport() {
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("Lecturer-detail");
                detail.innerHTML = http.responseText;
                formatGeneralTable();
            }
        }
        function updateDetailTrain(){
           document.forms["form-update"].submit();
        }
     </script>
    </body>
</html>
