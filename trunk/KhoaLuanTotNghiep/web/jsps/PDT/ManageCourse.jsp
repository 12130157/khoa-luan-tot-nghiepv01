<%-- 
    Document   : ManageStudentClass
    Created on : 10-07-2012, 22:47:01
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<Course> courses = (List<Course>) session.getAttribute("courses");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý khóa học</title>
        <style media="all" type="text/css">
            
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%-- Title --%>
                <div id="main-title">
                    Trang quản khóa học
                </div>
                <%--List file--%>
                <br />
                <div id="list-existed-class" class="range">
                    <%-- File from Lecturer --%>
                    <h3><span id="btn-list-existed-course" class="atag">Danh sách các khóa học</span></h3> 
                    <div id="tbl-existed-course">
                        <table class="general-table">
                            <tr>
                                <th>Mã khóa</th>
                                <th>Năm bắt đầu</th>
                                <th>Năm dự kết thúc</th>
                                <th>Số sinh viên</th>
                                <th>Xóa</th>
                            </tr>
                            <%
                            if (courses != null && !courses.isEmpty()) {
                                for (uit.cnpm02.dkhp.model.Course course : courses) {
                            %>
                                <tr>
                                    <td><%= course.getId() %></td>
                                    <td><%= course.getYearIn() %></td>
                                    <td><%= course.getYearOut() %></td>
                                    <td><%= course.getNumOfStudent() %></td>
                                    <td><span class="atag" onclick="deleteStudentCourse('<%=course.getId() %>')" > <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/> </span></td>
                                </tr>
                            <%}}
                            %>
                        </table>
                    </div>
                        <div id="msg-response" class="msg-response"> </div>
                </div>

                <div class="clear"></div>
                <%-- Add a class --%>
                <div class="range">
                    <h3><span id="btn-create-new-course" class="atag">Tạo khóa mới</span></h3> 
                    <div id="form-create-new-course" style="display: none;">
                        <table class="general-table" style="width: 650px;">
                            <tr>
                                <td>Mã khóa</td>
                                <td><input type="text" placeholder="K1, K2, ..." id="txt-class-id" name="txt-class-id" /></td>
                            </tr>
                            <tr>
                                <td>Năm bắt đầu</td>
                                <td>
                                    <select id="txt-yearIn" name="txt-yearIn">
                                        <%for(int i = 2010; i<2030; i++){%>
                                        <option value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>Năm kết thúc</td>
                                <td>
                                    <select id="txt-yearOut" name="txt-yearOut">
                                        <%for(int i = 2010; i<2030; i++){%>
                                        <option value="<%=i%>"><%=i%></option>
                                        <%}%> 
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <div class="clear"> <br /> </div>
                        <div class="button-1">
                            <span class="atag" onclick="createNewCourse()" ><img src="../../imgs/check.png"/>Submit</span>
                        </div>
                        <div class="clear"></div>
                        <div id="message-handler" class="msg-response"></div>
                    </div>
                </div>
                 <br />
            </div><!--End Contents-->

            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"> </script>
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            var http = createRequestObject();
           $("#btn-list-existed-course").click(function () {
                $('#tbl-existed-course').slideToggle(500);
            });
            
            $("#btn-create-new-course").click(function () {
                $('#form-create-new-course').slideToggle(500);
            });
            
            function validate(evt) {
                var theEvent = evt || window.event;
                var key = theEvent.keyCode || theEvent.which;
                key = String.fromCharCode( key );
                var regex = /[0-9]|\./;
                if( !regex.test(key) ) {
                    theEvent.returnValue = false;
                    if(theEvent.preventDefault) theEvent.preventDefault();
                }
            }

            function createNewCourse() {
                //validate
                var id = document.getElementById("txt-class-id").value;
                var yearIn = document.getElementById("txt-yearIn").value;
                var yearOut = document.getElementById("txt-yearOut").value;
                if (id == null || id.length <=0){
                    alert("Vui lòng nhập mã khóa học (ví dụ: K1, K2...)");
                    return;
                }
                if(yearOut - yearIn <4){
                    alert("Nằm ra phải lớn hơn năm vào ít nhất 4 năm");
                    return ;
                }
               var controller = "../../CourseManager?action=create-course"
                        + "&id=" + id
                        + "&yearIn=" + yearIn
                        + "&yearOut=" + yearOut;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = createNewClassHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
                
            }
            
            function createNewClassHandler() {
                //
                if(http.readyState == 4 && http.status == 200){
                    $('#message-handler').show('slow');
                    var detail = document.getElementById("message-handler");
                    detail.innerHTML = http.responseText;
                    setTimeOut("message-handler", AjaxConstants.SHORT_DELAY);
                }
            }

           function deleteStudentCourse(courseID) {
                var controller = "../../CourseManager?action=delete-course"
                        + "&courseID=" + courseID;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = deleteCourseHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function deleteCourseHandler() {
                if(http.readyState == 4 && http.status == 200){
                    $('#msg-response').show('slow');
                    var detail = document.getElementById("msg-response");
                    detail.innerHTML = http.responseText;
                    setTimeOut("msg-response", AjaxConstants.SHORT_DELAY);
                }
            }
       </script>
    </body>
</html>