<%-- 
    Document   : TrainingProgram
    Created on : 11-07-2012, 21:26:00
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Course"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.TrainProgram"%>
<%@page import="uit.cnpm02.dkhp.model.TrainProgram"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    
    List<TrainProgram> trainProgs = (List<TrainProgram>) session.getAttribute("train-programs");
    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
    List<Course> courses = (List<Course>) session.getAttribute("courses");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Chương trình đào tạo</title>
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
                    Quản lý Chương trình đào tạo
                </div>
                <%--List file--%>
                <br />
                <div id="list-existed-training-pro" class="range">
                    <%-- File from Lecturer --%>
                    <h3><span id="btn-list-existed-class" class="atag">Chương trình đào tạo</span></h3> 
                    <div id="tbl-existed-training-pro">
                        <%-- Existed Training program --%>
                        <div id="list-trainprog" style="float: left;">
                            <table class="general-table" style="width: 400px;">
                                <tr>
                                    <th>Mã CTĐT</th>
                                    <th>Khoa</th>
                                    <th>Khóa học</th>
                                    <th></th>
                                </tr>
                                <%
                                if ((trainProgs != null) && !trainProgs.isEmpty()) {
                                    for (TrainProgram tp : trainProgs) {%>
                                    <tr>
                                        <td><span class="atag" onclick="getTrainProgDetail('<%=tp.getId() %>')"> <%= tp.getId()  %></span></td>
                                        <td><%= tp.getFacultyCode() %></td>
                                        <td><%= tp.getCourseCode() %></td>
                                        <td>
                                            <% if(!tp.isIsStarted()) {%>
                                                <span class="atag" onclick="deleteTrainProg('<%=tp.getId()%>')"><img src="../../imgs/icon/delete.png" alt="delete" title="delete"/></span>
                                            <%} else {}%>
                                        </td>
                                    </tr>
                                <%  }
                                }
                                %>
                            </table>
                        </div>
                        <div id="train-class-detail" style="float: left; padding-left: 25px;">
                        </div>
                    </div>
                        
                    <div id="msg-response"></div>
                </div>

                <div class="clear"></div>
                <%-- Add a class --%>
                <div class="range">
                    <h3><span id="btn-create-new-training-pro" class="atag">Tạo mới CTĐT</span></h3> 
                    <div id="form-create-new-training-pro" style="display: block;">
                        <%-- New Training program creation --%>
                        <div id="form-new-train-prog" style="float: left;">
                            <table class="general-table" style="width: 320px;">
                                <tr>
                                    <td>Mã CTDT</td>
                                    <td>
                                        <input type="text" placeholder="CNPM01, CNPM02, ..." id="txt-trainprog-id" name="txt-trainprog-id" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>Chọn khoa</td>
                                    <td>
                                        <select id="txt-faculty" class="input-minwidth" onchange="changeFacultyUpdateLecturer()">
                                            <%
                                            if ((faculties != null) && !faculties.isEmpty()) {
                                                for (Faculty f : faculties) {
                                                    %>
                                                    <option value="<%=f.getId()%>"> <%= f.getFacultyName()%> </option>
                                                    <%
                                                }
                                            }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Khóa học</td>
                                    <td>
                                        <select id="txt-course" class="input-minwidth">
                                            <%
                                            if ((courses != null) && !courses.isEmpty()) {
                                                for (Course c : courses) {
                                                    String description = c.getYearIn() + " - " + c.getYearOut();
                                                    %>
                                                    <option value="<%=c.getId()%>"> <%= description %> </option>
                                                    <%
                                                }
                                            }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                            <div class="button-1">
                                <span class="atag" onclick="createNewTrainPro()" ><img src="../../imgs/check.png"/>Tạo mới</span>
                            </div>
                        </div>
                        <%-- Import data for training class --%>
                        <div id="form-new-train-prog-update" style="padding-left: 12px; margin-left: 25px; float: left; font-weight: bold; font-size: 12px;">
                        </div>
                        <div class="clear"> <br /> </div>
                        <%--
                        <div class="button-1">
                            <span class="atag" onclick="createNewTrainPro()" ><img src="../../imgs/check.png"/>Submit</span>
                        </div>
                        <div class="clear"></div>
                        --%>
                        <div id="message-handler"></div>
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
        <%--<script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>--%>
        <script  type = "text/javascript" >
            var http = createRequestObject();
            
            $("#btn-list-existed-class").click(function () {
                $('#tbl-existed-training-pro').slideToggle(500);
            });
            $("#btn-create-new-training-pro").click(function () {
                $('#form-create-new-training-pro').slideToggle(500);
            });
            
            // Delete
            // Neu ton tai trainprog detail --> show confirm dialog, NO --> do nothing, YES --> delete
            // Neu khong ton tai trainprog detail --> delete
            function deleteTrainProg(trainProgId) {
                var confirmDelete = confirm("Xác nhận xóa ?");
                if (confirmDelete == false){
                    alert("Delete canceled ...");
                    return;
                }
                
                var controller = "../../TrainingProgramControler?action=delete-train-prog"
                        + "&tpid=" + trainProgId;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = deleteTrainProgHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            // Exist trainprog detail --> exist_trainprog_detail
            function deleteTrainProgHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var responseValue = http.responseText;
                    if (responseValue.indexOf("exist_trainprog_detail") == 0) {
                        alert("Không cho phép xóa CTĐT đã tồn tại chi tiêt CTĐT");
                        return;
                    } else if (responseValue.indexOf("error") == 0) {
                        alert("Lỗi: " + responseValue.substring(5, responseValue.length - 1));
                        return;
                    } else {
                        var detail = document.getElementById("list-trainprog");
                        detail.innerHTML = responseValue;
                        
                        formatGeneralTable();
                    }
                }
            }
            
            // Create new
            function createNewTrainPro() {
                var id = document.getElementById("txt-trainprog-id").value;
                var facultyId = document.getElementById("txt-faculty").value;
                var courseId =  document.getElementById("txt-course").value;
                if (id==null || id.length <= 0) {
                    alert("Vui lòng nhập mã CTĐT");
                    return;
                }
                var controller = "../../TrainingProgramControler?action=create-new-train-prog"
                        + "&train-program-ID=" + id
                        + "&faculty-ID=" + facultyId
                        + "&course-ID=" + courseId;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = createNewTrainProHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function createNewTrainProHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("form-new-train-prog-update");
                    $(detail).fadeIn('slow');
                    detail.innerHTML = http.responseText;
                    setTimeOut('form-new-train-prog-update', AjaxConstants.SHORT_DELAY);
                }
            }
            
            function getTrainProgDetail(trainProgId) {
                var controller = "../../TrainingProgramControler?action=get-train-prog-detail"
                        + "&train-program-ID=" + trainProgId ;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = getTrainProgDetailHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            function getTrainProgDetailHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("train-class-detail");
                    detail.innerHTML = http.responseText;
                    formatGeneralTable();
                }
            }
            
            function updateTrainPro() {
                /*var controller = "../../TrainingProgramControler?action=pre-update-train-prog"
                        + "&train-program-ID=" + trainProgId ;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = null;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }*/
                document.forms["form-update"].submit();
            }
            
        </script>
    </body>
</html>