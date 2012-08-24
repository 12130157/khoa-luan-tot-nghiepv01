<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Class"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);
    
    Student student=(Student) session.getAttribute("student");
    Class classes=(Class) session.getAttribute("classes");
    Faculty faculty=(Faculty)session.getAttribute("faculty");
    Course course=(Course)session.getAttribute("course");
    String pass = (String) session.getAttribute("password");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thông tin cá nhân</title>
        <style media="all" type="text/css">
            <%-- CSS definition --%>
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">            
            <%@include file="MenuSV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Trang cá nhân <%=student.getFullName()%> - <%=student.getId()%>
                </div>
                <br /><br />
                <div class="range">
                    <%-- Sub title --%>
                    <h3><span id="btn-open-student-information" class="atag">
                        Thông tin cá nhân
                    </span></h3>
                    <%-- Main data --%>
                        <div id="tbl-student-information">
                        <%--
                            <a href="../../AccountController?action=changeinfo">Cập nhật thông tin</a>
                            <br />
                        --%>
                        <table class="general-table" style="width: 550px;">
                            <tr>
                                <td><i>Ngày sinh</i> </td>
                                <td><%=DateTimeUtil.format(student.getBirthday())%></td>
                            </tr>  
                            <tr>
                                <td><i>Giới tính</i> </td>
                                <td><%=student.getGender()%></td>
                            </tr>
                            <tr>
                                <td><i>Lớp</i> </td>
                                <td><%=classes.getClassName()%></td>
                            </tr>
                            <tr>
                                <td><i>Khoa</i> </td>
                                <td><%=faculty.getFacultyName()%></td>
                            </tr>
                            <tr>
                                <td><i>Khóa</i> </td>
                                <td><%=course.getId()%></td>
                            </tr>
                            <tr>
                                <td><i>Bậc học</i> </td>
                                <td><%=student.getStudyLevel()%></td>
                            </tr>
                            <tr>
                                <td><i>Loại hình học</i> </td>
                                <td><%=student.getStudyType()%></td>
                            </tr>
                            <tr>
                                <td><i>Ngày nhập học</i> </td>
                                <td><%=DateTimeUtil.format(student.getDateStart())%></td>
                            </tr>
                            <tr>
                                <td><i>CMND </i></td>
                                <td><%=student.getIdentityNumber()%></td>
                            </tr>
                            <tr>
                                <td><i>Quê quán</i> </td>
                                <td><%=student.getHomeAddr()%></td>
                            </tr>
                            <tr>
                                <td><i>Đại chỉ liên lạc</i> </td>
                                <td><%=student.getAddress()%></td>
                            </tr>
                            <tr>
                                <td><i>Điện thoại</i> </td>
                                <td><%=student.getPhone()%></td>
                            </tr>
                            <tr>
                                <td><i>Email</i> </td>
                                <td><%=student.getEmail()%></td>
                            </tr>
                            <tr>
                                <td><i>Tình trạng</i> </td>
                                <td><%=student.getStatus()%></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="clear"></div><br />
                <div class="range">
                    <%-- Sub title --%>
                    <h3><span id="btn-open-update-information" class="atag">
                            Cập nhật thông tin cá nhân
                    </span></h3>
                        <%-- Main data --%>
                    <div id="tbl-update-information" style="display: none;">
                        <form id="formdetail" name="formdetail" action="../../AccountController?action=update" method="post">
                            <table class="general-table">
                                <tr>
                                    <td><i>Ngày sinh</i> </td>
                                    <td>
                                        <input type="text" id="birthday" name="birthday" readonly="readonly" value="<%=student.getBirthday()%>">
                                        <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('birthday','YYMMMDD')" />
                                    </td>
                                </tr>  
                                <tr>
                                    <td><i>Giới tính</i> </td>
                                    <td>
                                        <select name="gender" style="width:100px">
                                            <%
                                            if(student.getGender().equalsIgnoreCase("Nam")){
                                            %>
                                            <option value="Nam">  Nam  </option>
                                            <option value="Nữ">  Nữ  </option>
                                            <%}else{%>
                                            <option value="Nữ">  Nữ  </option>
                                            <option value="Nam">  Nam  </option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><i>Ngày nhập học</i> </td>
                                    <td>
                                        <input type="text" id="startdate" name="startdate" readonly="readonly" value="<%=student.getDateStart()%>">
                                        <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('startdate','YYMMMDD')" />
                                    </td>
                                </tr>
                                <tr>
                                    <td><i>CMND </i></td>
                                    <td><input type="text" name="IdentityCard" id="IdentityCard" onkeypress="return checknumber(event)" value="<%=student.getIdentityNumber()%>"/></td>
                                </tr>
                                <tr>
                                    <td><i>Quê quán</i> </td>
                                    <td><input type="text" name="home" id="home" value="<%=student.getHomeAddr()%>" size="100"/></td>
                                </tr>
                                <tr>
                                    <td><i>Đại chỉ liên lạc</i> </td>
                                    <td><input type="text" name="address" id="address" value="<%=student.getAddress()%>" size="100"/></td>
                                </tr>
                                <tr>
                                    <td><i>Điện thoại</i> </td>
                                    <td><input type="text" name="phone" id="phone" onkeypress="return checknumber(event)" value="<%=student.getPhone()%>"/></td>
                                </tr>
                            </table>
                            <%--
                            <div id="button">
                                <input type="button" name="OK" id="OK" onclick="Change()" value="       OK       "/>
                                <input type="button" name="Can" id="Can" onclick="Cancel();" value="     Đóng     "/>
                            </div>
                            --%>    
                        </form>
                      <form id="reset" name="reset" action="../../AccountController?action=Info" method="post">
                      </form>
                        <div class="button-1" style="float:left; margin-left: 50px; height: 20px;">
                            <span class="atag" onclick="updateInfor()"><img src="../../imgs/check.png"/>OK</span>
                        </div>
                        <%--
                        <div class="button-1" style="float:left; margin-left: 12px; height: 20px;">
                            <span class="atag" onclick="cancelUpdateInfor()"><img src="../../imgs/check.png"/>Hủy</span>
                        </div>
                        --%>
                    </div>
                    <%-- Update personal information respond --%>
                    <div class="clear"></div> <br />
                    <div id="update-info-respond" class="msg-response" style="width: 30%;">
                    </div>
                </div>
                <div class="range">
                    <%-- Sub title --%>
                    <h3><span id="btn-open-update-password" class="atag">
                            Cập nhật mật khẩu truy cập hệ thống
                    </span></h3>
                        <%-- Main data --%>
                    <div id="tbl-update-password" style="display: none;">
                        <br />
                        <form id="changepast" name="changepast" action="../../AccountController?action=changePass" method="post">
                            <%
                            String message = (String) session.getAttribute("messageChanPass");
                                if (message != null) {
                                    session.removeAttribute("messageChanPass");
                            %>
                            <p id="message"> <%=message%></p>
                            <br />
                            <%}%>
                            <table id="past" name="past">
                                <tr>
                                    <td>Mật khẩu hiện tại:</td>
                                    <td><input type="password" name="oldPass" id="oldPass"/></td>
                                </tr>
                                <tr>
                                    <td>Mật khẩu mới:</td>
                                    <td><input type="password" name="newpass" id="newpass"/></td>
                                </tr>
                                <tr>
                                    <td>Xác nhận mật khẩu:</td>
                                    <td><input type="password" name="confirmPass" id="confirmPass"/></td>
                                </tr>
                                <%--
                                <tr>
                                    <td></td>
                                    <td>
                                        <input type="button" name="OK" id="OK" onclick="Change()" value="       OK       "/>
                                        <input type="button" name="Can" id="Can" onclick="Cancel();" value="     Đóng     "/>
                                    </td>
                                </tr>
                                --%>
                            </table>
                            <input type="text" id="curentpass" name="curentpass" value="<%=pass%>" hidden="true">
                        </form>
                        <br />
                        <div class="button-1" style="float:left; margin-left: 50px; height: 20px;">
                            <span class="atag" onclick="updatePwd()"><img src="../../imgs/check.png"/>OK</span>
                        </div>
                        <%--
                        <div class="button-1" style="float:left; margin-left: 12px; height: 20px;">
                            <span class="atag" onclick="cancelUpdatePwd()"><img src="../../imgs/check.png"/>Hủy</span>
                        </div>
                        --%>
                    </div>
                    <%-- Update password respond --%>
                    <div class="clear"></div> <br />
                    <div id="update-pwd-respond" class="msg-response" style="width: 30%;">
                    </div>
                </div>
                   
            </div><!--End Contents-->
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
        <script src="../../javascripts/AjaxUtil.js"></script>
        <%--<script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>--%>
        <script  type = "text/javascript" >
            var http = createRequestObject();
            
            $("#btn-open-student-information").click(function () {
                $('#tbl-student-information').slideToggle(500);
            });
            $("#btn-open-update-information").click(function () {
                $('#tbl-update-information').slideToggle(500);
            });
            $("#btn-open-update-password").click(function () {
                $('#tbl-update-password').slideToggle(500);
            });
            
            function updatePwd(){
                var pwd = document.getElementById("oldPass").value;
                var newPwd = document.getElementById("newpass").value;
                var newPwdConfirm = document.getElementById("confirmPass").value;

                if ((pwd == null) || (pwd.length == 0)
                    || (newPwd == null) || (newPwd.length == 0)
                    || (newPwdConfirm == null) || (newPwdConfirm.length == 0)) {
                    alert("Vui lòng nhập đầy đủ thông tin trước khi submit.");
                    return;
                }

                if (newPwd != newPwdConfirm) {
                    alert("Mật khẩu xác nhận không đúng.");
                    return;
                }

                var controller = "../../AccountController?action=changePass"
                        + "&old_pwd=" + pwd
                        + "&new_pwd=" + newPwd;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = updatePwdHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }

            }

            function updatePwdHandler() {
                if(http.readyState == 4 && http.status == 200){
                    $('#update-pwd-respond').show('slow');
                    var detail = document.getElementById("update-pwd-respond");
                    detail.innerHTML = http.responseText;

                    //$('#update-pwd-respond').fadeIn(10000).fadeOut('slow'); 
                }
            }

            function updateInfor() {
                var identityCard = document.getElementById("IdentityCard").value;
                var home = document.getElementById("home").value;
                var address = document.getElementById("address").value;
                var phone = document.getElementById("phone").value;
                if(identityCard.length == 0){
                    alert("Hãy nhập CMND");
                } else if(home.length==0){
                    alert("Hãy nhập đại chỉ thường trú");
                } else if(address.length==0){
                    alert("Hãy nhập địa chỉ liên lạc");
                } else if(phone.length==0){
                    alert("Hãy nhập điện thoại");
                }/* else{
                    document.forms["formdetail"].submit();
                }*/
                var controller = "../../AccountController?action=update"
                        + "&IdentityCard=" + identityCard
                        + "&home=" + home
                        + "&address=" + address
                        + "&phone=" + phone;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = updateInforHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }

            function updateInforHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("update-info-respond");
                    $('#update-info-respond').show('slow');
                    detail.innerHTML = http.responseText;

                    //$('#update-info-respond').fadeIn(10000).fadeOut('slow'); 
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