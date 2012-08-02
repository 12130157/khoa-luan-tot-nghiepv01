<%-- 
    Document   : ManageStudentClass
    Created on : 10-07-2012, 22:47:01
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);

    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
    List<Lecturer> lecturer = (List<Lecturer>) session.getAttribute("lecturers");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý khoa, bộ môn</title>
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
                    Trang quản khoa, bộ môn
                </div>
                <%--List file--%>
                <br />
                <div id="list-existed-class" class="range">
                    <%-- File from Lecturer --%>
                    <h3><span id="btn-list-existed-faculty" class="atag">Danh sách các khoa, bộ môn hiện tại</span></h3> 
                    <div id="tbl-existed-faculty">
                        <table class="general-table">
                            <tr>
                                <th>Mã khoa</th>
                                <th>Tên khoa</th>
                                <th>Trưởng khoa</th>
                                <th>Số SV</th>
                                <th>Sửa</th>
                                <th>Xóa</th>
                            </tr>
                            <%
                                if (faculties != null && !faculties.isEmpty()) {
                                    for (Faculty faculty : faculties) {
                            %>
                            <tr>
                                <td><%= faculty.getId()%></td>
                                <td><%= faculty.getFacultyName()%></td>
                                <td><%= faculty.getDeanName()%></td>
                                <td><%= faculty.getNumOfStudent()%></td>
                                <td>
                                    <span class="atag" onclick="loadDataToEdit('<%= faculty.getId()%>')">
                                        <a href="#edit-account-range"> <img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/> </a>
                                    </span>    
                                </td>
                                <td><span class="atag" onclick="deleteFaculty('<%=faculty.getId()%>')" > <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/> </span></td>
                            </tr>
                            <%}
                                }
                            %>
                        </table>
                    </div>
                    <div id="msg-response" class="msg-response"> </div>
                </div>

                <div class="clear"></div>
                <%-- Add a class --%>
                <div class="range">
                    <h3><span id="btn-create-new-faculty" class="atag">Tạo khoa mới</span></h3> 
                    <div id="form-create-new-faculty" style="display: none;">
                        <table class="general-table" style="width: 650px;">
                            <tr>
                                <td>Mã khoa</td>
                                <td><input type="text" placeholder="CNPM, KTMT, ..." id="txt-faculty-id" name="txt-faculty-id" /></td>
                            </tr>
                            <tr>
                                <td>Tên khoa</td>
                                <td><input type="text" placeholder="Công nghệ phần mềm, ..." id="txt-faculty-name" name="txt-faculty-name" /></td>
                            </tr>
                            <tr>
                                <td>Trưởng khoa</td>
                                <td>
                                    <select id="txt-dean" class="input-minwidth" >
                                        <%
                                            if ((lecturer != null) && !lecturer.isEmpty()) {
                                                for (Lecturer l : lecturer) {
                                        %>
                                        <option value="<%=l.getId()%>"> <%= l.getFullName()%> </option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <div class="clear"> <br /> </div>
                        <div class="button-1">
                            <span class="atag" onclick="createNewFaculty()" ><img src="../../imgs/check.png"/>Submit</span>
                        </div>
                        <div class="clear"></div>
                        <div id="message-handler" class="msg-response"></div>
                    </div>
                </div>
                <div class="range" id="edit-account-range">
                    <h3><span id="btn-to-edite-faculty" class="atag" >Cập nhật thông tin khoa</span></h3>
                    <div id="form-edit-faculty" style="display: none;">
                        <u>Cập nhật thông tin khoa và nhấn <b>Hoàn Thành</b></u>
                        <br/>
                        <div id="form-edit-faculty">
                            <br />
                            <ul>
                                <li>
                                    Để thay đổi thông tin khoa, vui lòng click vào biểu tượng <img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/> tương ứng vơi khoa.
                                </li>
                            </ul>
                        </div>
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
            $("#btn-to-edite-faculty").click(function () {
                $('#form-edit-faculty').slideToggle(500);
            });
            
            $("#btn-list-existed-faculty").click(function () {
                $('#tbl-existed-faculty').slideToggle(500);
            });
            
            $("#btn-create-new-faculty").click(function () {
                $('#form-create-new-faculty').slideToggle(500);
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

            function createNewFaculty() {
                //validate
                var id = document.getElementById("txt-faculty-id").value;
                var name = document.getElementById("txt-faculty-name").value;
                var dean = document.getElementById("txt-dean").value;
                
                if (id == null || id.length <=0
                    || name == null || name.length <=0) {
                    alert("Vui lòng nhập đầy đủ thông tin.");
                    return;
                }
                var controller = "../../FacultyManager?action=create-faculty"
                    + "&id=" + id
                    + "&name=" + name
                    + "&dean=" + dean;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = createNewFacultyHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
                
            }
            
            function createNewFacultyHandler() {
                //
                if(http.readyState == 4 && http.status == 200){
                    $('#message-handler').show('slow');
                    var detail = document.getElementById("message-handler");
                    detail.innerHTML = http.responseText;
                    setTimeOut("message-handler", AjaxConstants.SHORT_DELAY);
                }
            }

            function deleteFaculty(facultyID) {
                var controller = "../../FacultyManager?action=delete-faculty"
                    + "&facultyID=" + facultyID;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = deleteFacultyHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function deleteFacultyHandler() {
                if(http.readyState == 4 && http.status == 200){
                    $('#msg-response').show('slow');
                    var detail = document.getElementById("msg-response");
                    detail.innerHTML = http.responseText;
                    setTimeOut("msg-response", AjaxConstants.SHORT_DELAY);
                }
            }
            function loadDataToEdit(facultyID) {
                // Load data
                preDataForEditClass(facultyID);   
                // Open edit range
                $('#form-edit-faculty').slideDown(500);
            }
            function preDataForEditClass(facultyID) {
                var pagename = "../../FacultyManager?action=edit-faculty"
                    + "&facultyID=" + facultyID;
                if(http){
                    http.open("GET", pagename, true);
                    http.onreadystatechange = preDataForEditFacultytHandler;
                    http.send(null);
                } else {
                    alert("Error: Could not create http object.");
                }
            }
        
            function preDataForEditFacultytHandler() {
                if((http.readyState == 4) && (http.status == 200)){
                    var detail = document.getElementById("form-edit-faculty");
                    detail.innerHTML = http.responseText;
                }
            }
        
            function update() {
                var facultyID = document.getElementById("txtFacultyID_edit").value;
                var facultyName = document.getElementById("txtFacultyName_edit").value;
                var dean = document.getElementById("dean").value;
                var controller = "../../FacultyManager?action=update&facultyID=" 
                    + facultyID
                    + "&facultyName=" + facultyName
                    + "&deanCode=" + dean;
                if(http){
                    http.open("GET", controller ,true);
                    http.onreadystatechange = updateResponseHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            function updateResponseHandler() {
                if(http.readyState == 4 && http.status == 200){
                    $('#respone-edit-area').show('slow');
                    var detail = document.getElementById("respone-edit-area");
                    detail.innerHTML = http.responseText;
                    setTimeOut("respone-edit-area", AjaxConstants.SHORT_DELAY);
                }
            }
            // Update account --
        </script>
    </body>
</html>