<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="uit.cnpm02.dkhp.model.RegistrationTime"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
 RegistrationTime registrationTime = (RegistrationTime) session.getAttribute("registrationTime");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết lớp học</title>
        <style media="all" type="text/css">
          #table_mh{
                padding-left: 100px;
                text-align: left;
                width: 500px;

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
        </style>
    </head>
    <body>
        <script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Thời gian đăng ký cho học kỳ <%=registrationTime.getId().getSemester()%> năm học <%=registrationTime.getId().getYear()%>
                </div>
                <br /> <br />
                <%-- Main content --%>
                <div id="information-form">
                    <u><b>Thời gian đăng kí</b></u>
                    <div id="form-time-register">
                        <table id="table_mh" class="general-table">
                            <tr>
                                <td>Ngày bắt đầu đăng ký: </td>   
                                <td><input type="text" id="start-date" readonly="readonly" value="<%=DateTimeUtil.format(registrationTime.getStartDate())%>" /></td>
                            </tr>
                            <tr>
                                <td>Ngày kết thúc đăng ký: </td>   
                                <td><input type="text" id="end-date" readonly="readonly" value="<%=DateTimeUtil.format(registrationTime.getEndDate())%>" /></td>
                            </tr>
                        </table>
                        <div class="button-1" style="float:left; margin-left: 20%;">
                            <span class="atag" onclick="preUpdate()"><img src="../../imgs/check.png"/>Thay đổi</span>
                        </div>
                        <%-- MEssage respond --%>
                        <div id="message"></div>
                    </div>
                </div>
                <div id="edit-time" style="display: none;">
                    <u><b>Thay đổi thời gian đăng kí</b></u>
                    <div id="org-information">
                        <table id="table_mh" class="general-table">
                            <tr>
                                <td>Ngày bắt đầu đăng ký: </td>   
                                <td> 
                                    <input type="text" id="startDate" name="startDate" readonly="readonly" value="<%=DateTimeUtil.format(registrationTime.getStartDate())%>" />
                                    <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('startDate','YYMMMDD')" />
                                </td>
                            </tr>
                            <tr>
                                <td>Ngày kết thúc đăng ký: </td>   
                                <td>
                                    <input type="text" id="endDate" name="endDate" readonly="readonly" value="<%=DateTimeUtil.format(registrationTime.getEndDate())%>" />
                                    <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('endDate','YYMMMDD')" />
                                </td>
                            </tr>
                        </table>
                        <div class="button-1" style="float:left; margin-left: 20%;">
                            <span class="atag" onclick="update()"><img src="../../imgs/check.png"/>Hoàn thành"</span>
                        </div>
                        <div class="button-1" style="float:left; margin-left: 15px;">
                            <span class="atag" onclick="cancel()"><img src="../../imgs/check.png"/>Hủy"</span>
                        </div>
                        <div id="message"></div>
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
        var http = createRequestObject();
        
        function preUpdate() {
            $('#information-form').slideToggle(500);
            $('#edit-time').slideToggle(500);
        }
        
        function update() {
            changeTimeRegister();
        }
        
        function cancel() {
            preUpdate();
        }
        
        function changeTimeRegister() {
            var newStart = document.getElementById("startDate").value;
            var newEnd = document.getElementById("endDate").value;
            if (http) {
                http.open("GET", "../../TimeController?action=update&new_start="
                    + newStart + "&new_end=" + newEnd, true);
                http.onreadystatechange = changeTimerRegisterHandler;
                http.send(null);
            }
        }
        
        function changeTimerRegisterHandler() {
            if(http.readyState == 4 && http.status == 200){
                var responeText = http.responseText;
                if (responeText.substring(0, 5) == "error") {
                    alert("Đã có lỗi xảy ra, vui lòng thử lại");
                }
                var startDate = document.getElementById("start-date");
                var endDate = document.getElementById("end-date");
                var values = responeText.split(";");
                startDate.value = values[0];
                endDate.value = values[1];
                
                $('#edit-time').slideToggle(500);
                $('#information-form').slideToggle(500);
                //detail.innerHTML = responeText;
            }
        }
    </script>
    </body>
</html>
