<%-- 
    Document   : PersionnalInformation
    Created on : 20-05-2012, 21:37:16
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.Constants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Lecturer lecturer = (Lecturer) session.getAttribute("lecturer");
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Trang cá nhân</title>
        <style media="all" type="text/css">
            #send-request-result {
                display: none;
            }
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
                
                <%-- Thong tin ca nhan --%>
                <div class="range">
                    <h3><span id="btn-persional-information" class="atag">
                        Thông tin cá nhân
                    </span></h3>
                    <div id="persional-information">
                        <%
                        if (lecturer != null) {
                        %>
                        <%--
                            MaGV            MaKhoa
                            HoTen           QueQuan
                            NgaySinh        CMND
                            DienThoai       HocHam
                            Email           HocVi
                        --%>

                            <table style="width: 600px;">
                                <tr class="odd-row">
                                    <td> Mã GV </td>
                                    <td>
                                        <input type="text" readonly="readonly" id="txt-lecturer-id" value="<%= lecturer.getId() %>" />
                                    </td>
                                    <td> Quên quán </td>
                                    <td> <%= lecturer.getAddress() %> </td>
                                </tr>
                                <tr class="even-row">
                                    <td> Họ và tên </td>
                                    <td> <%= lecturer.getFullName() %> </td>
                                    <td> Khoa </td>
                                    <td> <%= lecturer.getFacultyCode() %> </td>
                                </tr>
                                <tr class="odd-row">
                                    <td> Ngày sinh </td>
                                    <td> <%= sdf.format(lecturer.getBirthday()) %> </td>
                                    <td> CMND </td>
                                    <td> <%= lecturer.getIdentityCard() %> </td>
                                </tr>
                                <tr class="even-row">
                                    <td> ĐT liên lạc </td>
                                    <td> <%= lecturer.getPhone() %> </td>
                                    <td> Học Hàm </td>
                                    <td> <%= lecturer.getHocHam() %> </td>
                                </tr>
                                <tr class="odd-row">
                                    <td> Email </td>
                                    <td> <%= lecturer.getEmail() %> </td>
                                    <td> Học Vị </td>
                                    <td> <%= lecturer.getHocVi() %> </td>
                                </tr>
                            </table>
                        <%
                        }
                        %>
                    </div>
                </div>
                
                <%-- PHan cap nhat thong tin ca nhan --%>
                <br />
                <div class="range">
                    <h3><span id="btn-change-information" class="atag">
                        Cập nhật thông tin
                    </span></h3>
                    <div id="update-information" style="display: none;">
                        <%
                        if (lecturer != null) {
                        %>
                            <table style="width: 600px;">
                                <tr class="odd-row">
                                    <td> Họ và tên </td>
                                    <td> 
                                        <input type="text" id="txt-fullname"
                                               value="<%= lecturer.getFullName() %>" />
                                    </td>
                                    <td> Ngày sinh </td>
                                    <td> 
                                        <input type="text" id="txt-birthday" name="txt-birthday"
                                               value="<%= sdf.format(lecturer.getBirthday()) %> " />
                                        <img src="../../imgs/cal.gif" style="cursor: pointer;"
                                             onclick="javascript:NewCssCal('txt-birthday','YYMMMDD')" />
                                    </td>
                                </tr>
                                <tr class="even-row">
                                    <td> CMND </td>
                                    <td> 
                                        <input type="text" id="txt-cmnd"
                                               value="<%= lecturer.getIdentityCard() %>" />
                                    </td>
                                    <td> ĐT liên lạc </td>
                                    <td> 
                                        <input type="text" id="txt-phone"
                                           value="<%= lecturer.getPhone() %>" />
                                    </td>
                                </tr>
                                <tr class="odd-row">
                                    <td> Email </td>
                                    <td>
                                        <input type="text" id="txt-email"
                                           value="<%= lecturer.getEmail() %>" />
                                    </td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </table>
                            <br/>
                            <input type="button" onclick="update()" value="Cập nhật"/>
                        <%
                        }
                        %>
                    </div>
                </div>
                
                
                <%-- Cac lop hien dang day --%>
                <br />
                <div class="clear"></div>
                <div class="range">
                    <h3><span id="btn-show-class-list" class="atag">
                        Các lớp đang dạy
                    </span></h3>
                    <div id="list-trainclass" style="display: none;">
                    </div>
                </div>
                
                <%-- Thong bao nghi day --%>
                <br />
                <div class="range">
                    <h3><span id="btn-show-send-notify" class="atag">
                        Thông báo nghỉ dạy
                        </span></h3>
                    <div id="delay-schedule" style="display: none;">
                        <i>(*) GV nhập mã lớp, ngày, ca nghỉ và ngày dạy bù dự kiến.</i>
                        <table style="width: 600px;">
                            <tr class="odd-row">
                                <td> Lớp </td>
                                <td> 
                                    <input type="text" id="txt-class" />
                                </td>
                                <td> Ngày nghỉ </td>
                                <td> 
                                    <input type="text" id="txt-dayoff" name="txt-dayoff" />
                                    <img src="../../imgs/cal.gif" style="cursor: pointer;"
                                         onclick="javascript:NewCssCal('txt-dayoff','YYMMMDD')" />
                                </td>
                            </tr>
                            <tr class="even-row">
                                <td> Ca </td>
                                <td> 
                                    <select id="txt-shift" class="input-minwidth">
                                        <option value="Sang"> Sáng </option>
                                        <option value="Chieu"> Chiều </option>
                                    </select>
                                </td>
                                <td> Ngày dạy bù </td>
                                <td> 
                                    <input type="text" id="txt-dayon" name="txt-dayon" />
                                    <img src="../../imgs/cal.gif" style="cursor: pointer;"
                                         onclick="javascript:NewCssCal('txt-dayon','YYMMMDD')" />
                                </td>
                            </tr>
                        </table>
                        <div id="send-request-result">
                            <%-- Show result after send request... --%>
                        </div>
                        <input type="button" onclick="sendRequest()" value="Gửi yêu cầu"/>
                    </div>
                </div>
                <br /><br />
                
            </div><%--End Contents--%>
            <%--Footer--%>
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
    <script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script src="../../javascripts/jquery-1.7.1.js"></script>
    <SCRIPT language="javascript">
        var http = createRequestObject();
        var loadTrainClassOfLecturer = false;
        
        $("#btn-show-send-notify").click(function () {
            $('#delay-schedule').slideToggle(500);
        });
        
        $("#btn-show-class-list").click(function () {
            $('#list-trainclass').slideToggle(500);
            if (!loadTrainClassOfLecturer) {
                var lecturerId = document.getElementById("txt-lecturer-id").value;
                getTrainClass(lecturerId);
                loadTrainClassOfLecturer = true;
            }
        });
        
        $("#btn-change-information").click(function () {
            $('#update-information').slideToggle(500);
        });
        
        $("#btn-persional-information").click(function () {
            $('#persional-information').slideToggle(500);
        });
        
        ////////////////////////
        //     SEND REQUEST   //
        ////////////////////////
        function sendRequest() {
            var trainclass = document.getElementById("txt-class").value;
            var dayoff = document.getElementById("txt-dayoff").value;
            var dayon = document.getElementById("txt-dayon").value;
            var shift = document.getElementById("txt-shift").value;
            if ((trainclass.length == 0)
                || (dayoff.length == 0)
                || (dayon.length == 0)
                || (shift.length == 0)) {
                alert("Vui lòng nhập đầy đủ thông tin.");
                return;
            }
            
            var controller = "../../LecturerPrivateController?function=send-request"
                    + "&trainclass=" + trainclass
                    + "&dayoff=" + dayoff
                    + "&dayon=" + dayon
                    + "&shift=" + shift;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = sendRequestHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function sendRequestHandler() {
            if(http.readyState == 4 && http.status == 200){
                var pi = document.getElementById("send-request-result");
                pi.innerHTML = http.responseText;
                $('#send-request-result').fadeIn(1000);
               setTimeout(function(){
                   $('#send-request-result').fadeOut(2000);
                },3000);
            }
        }
        
        //////////////////
        //     UPDATE   //
        //////////////////
        function update() {
            var lecturerId = document.getElementById("txt-lecturer-id").value;
            var fullname = document.getElementById("txt-fullname").value;
            var cmnd = document.getElementById("txt-cmnd").value;
            var email = document.getElementById("txt-email").value;
            var birthday = document.getElementById("txt-birthday").value;
            var phone = document.getElementById("txt-phone").value;
            
            if ((fullname.length == 0)
                || (cmnd.length == 0)
                || (email.length == 0)
                || (birthday.length == 0)
                || (phone.length == 0)) {
                alert("Vui lòng nhập đầy đủ thông tin.");
                return;
            }
            
            var controller = "../../LecturerPrivateController?function=update"
                    + "&lecturer=" + lecturerId
                    + "&fullname=" + fullname
                    + "&cmnd=" + cmnd
                    + "&email=" + email
                    + "&birthday=" + birthday
                    + "&phone=" + phone;
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = updateHandler;
                http.send(null);
            } else {
                alert("Error: http object not found");
            }
        }
        
        function updateHandler() {
            if(http.readyState == 4 && http.status == 200){
                var result = http.responseText;
                if (result.substring(0, 5) == "error") {
                    alert(result);
                    return;
                } else {
                    var pi = document.getElementById("persional-information");
                    pi.innerHTML = result;
                }
            }

        }
        
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
        function checknumber(evt){
            var e = event || evt; // for trans-browser compatibility
            var charCode = e.which || e.keyCode;

            if (charCode > 31 && (charCode < 48 || charCode > 57))
                return false;

            return true;
        }
    </script>
    </body>
    
</html>
