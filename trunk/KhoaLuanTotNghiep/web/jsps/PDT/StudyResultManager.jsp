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
        <title>Quản lý kết quả học tập</title>
        <style media="all" type="text/css">

            table {
                /*width: 100%;*/
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;
            }
            table td {
                text-align: center;
            }
            #report-range {
                float: left;
                padding-left: 10px;
                padding-right: 10px;
                padding-bottom: 25px;
                text-align: center;
            }
            #list-student {
                float: left;
                overflow:auto;
            }
            #student-detail {
                float: none;
                padding-left: 12px;
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
                    Quản lý kết quả học tập sinh viên
                </div>
                <%-- Search form --%>
                <div class="clear"></div>
                <div style="float: left;">
                    <div id="searchbox" action="#">
                        <input id="search" type="text" onKeyPress="keypressed()" placeholder="Nhập tên SV" />
                        <input type="button" id="submit" onclick="FindStudent()" value="Tìm kiếm" />
                    </div>
                </div>
                <div>
                    <div id="list-student">
                        <table class="general-table" id="list-studentFind" name="list-studentFind">
                           
                        </table>
                    </div>
                </div>
                <div class="clear"></div>
                <div class="range">
                    <h3><span id="btn-infor-manage" class="atag" >Hướng dẫn</span></h3>
                    <div id="info-manage">
                        <br/>
                        <p>
                            <b>Cho phép PĐT quản lý thông tin chi tiêt về kết quả học tập của từng Sinh Viên</b> <br />
                            <i>
                            <ul>
                                <li>- PĐT tìm SV cần xem bằng cách nhập gần đúng tên SV</li>
                                <li>- PĐT xem chi tết kết quả học tập của SV bằng cách click chuột vào tên SV tương ứng</li>
                            </ul>
                            </i>
                        </p>
                    </div>
                </div>
                <br>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <script  type = "text/javascript" >

        var http = createRequestObject();
        
        $("#btn-infor-manage").click(function () {
            $('#info-manage').slideToggle(500);
        });
        //
        // Search Student
        //
        function keypressed() { 
            if(event.keyCode=='13') {
                FindStudent();
            } 
        }
        
        function FindStudent(){
            var search = document.getElementById("search").value;
             if (http) {
                http.open("GET", "../../StudyResultManager?action=search_student&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
              }
            }
        
         function handleResponseFindStudent() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("list-studentFind");
                 detail.innerHTML=http.responseText;
                 
                 formatGeneralTable();
             }
         }
    </script>
    </body>
</html>
