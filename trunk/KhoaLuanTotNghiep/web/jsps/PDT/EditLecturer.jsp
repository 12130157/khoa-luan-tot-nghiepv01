<%-- 
    Document   : EditLecturer
    Created on : 24-12-2011, 12:40:49
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.type.HocVi"%>
<%@page import="uit.cnpm02.dkhp.model.type.HocHam"%>
<%@page import="java.util.EnumSet"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    Lecturer lecturer = (Lecturer) session.getAttribute("lecturer");
    List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý giang viên</title>
        <style media="all" type="text/css">
            /*CSS OVERIDDEN*/
        </style>
    </head>
    <body onload="">
        <!--Div Wrapper-->
        <div id="wrapper">
            <%--Menu--%>
            <%@include file="MenuPDT.jsp"%>
            <!--Main Navigation-->
            <div id="mainNav">
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%--Page's title--%>
                <div id="title">
                    Trang chỉnh sửa thông tin Giảng Viên
                </div>
                <% if (lecturer != null) { %>
                <div id="lecturer-persional-info">
                <u>Sửa thông tin GV</u>
                    <table>
                       <tr>
                           <td>  Mã GV  </td>
                           <td>
                               <input type="text" id="txt-magv" readonly="readonly" onfocus="this.blur();" value="<%= lecturer.getId() %>" />
                           </td>
                       </tr>
                       <tr>
                           <td> Họ và tên </td>
                           <td> <input type="text" id="txt-fullname" value="<%= lecturer.getFullName() %>"> </td>
                       </tr>
                       <tr>
                           <td> Khoa </td>
                           <td>
                               <select id="txt-faculty" class="input-minwidth" onchange="changeFaculty()">
                                    <%
                                    if ((faculties != null) && !faculties.isEmpty()) {
                                        for (int i = 0; i < faculties.size(); i++) {
                                            Faculty f = faculties.get(i);
                                            %>
                                            <option value="<%=f.getId()%>"
                                                    <%
                                                    if (lecturer.getFacultyCode().equalsIgnoreCase(f.getId())) {
                                                    %>
                                                    selected="selected"
                                                    <%
                                                    }
                                                    %>
                                                    >
                                                <%= f.getFacultyName()%>
                                            </option>
                                            <%
                                        }
                                    }
                                    %>
                                </select>
                           </td>
                       </tr>
                       <tr>
                           <td> Ngày sinh </td>
                           <td>
                                <input type="text" id="txt-birthday" name="txt-birthday" value="<%= DateTimeUtil.format(lecturer.getBirthday()) %>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('txt-birthday','YYMMMDD')" />
                            </td>
                       </tr>
                       <tr>
                           <td> Quê quán </td>
                           <td> 
                                <textarea id="txt-home" class="input-text-area">
                                    <%= lecturer.getAddress() %>
                                </textarea> 
                            </td>
                       </tr>
                       <tr>
                           <td> CMND </td>
                           <td> <input type="text" id="txt-cmnd" value=" <%= lecturer.getIdentityCard() %>"> </td>
                       </tr>
                       <tr>
                           <td> Điện thoại </td>
                           <td> <input type="text" id="txt-phone" value="<%= lecturer.getPhone() %>"> </td>
                       </tr>
                       <tr>
                           <td> Email </td>
                           <td> <input type="text" id="txt-email" value=" <%= lecturer.getEmail() %>"> </td>
                       </tr>
                       <tr>
                           <td> Giới tính </td>
                           <td>
                                <select id="txt-gender" class="input-minwidth">
                                    <%
                                    String[] genders = new String[] {"Nam", "Nữ"};
                                    for (int i = 0; i < 2; i++) {
                                    %>
                                        <option value="<%= genders[i]%>"
                                                <%
                                                if (lecturer.getGender().equalsIgnoreCase(genders[i])) {
                                                %>
                                                selected="selected"
                                                <%
                                                }
                                                %>
                                        >
                                            <%= genders[i]%>
                                        </option>
                                    <%
                                    }
                                    %>
                                </select>
                            </td>
                       </tr>
                       <tr>
                           <td> Học Hàm </td>
                           <td>
                            <select id="txt-hocham" class="input-minwidth">
                                <%
                                // Retrieve Student status
                                for(HocHam st : EnumSet.allOf(HocHam.class)) {
                                %>
                                <option value="<%= st.value()%>" 
                                        <%
                                        if (lecturer.getHocHam().equalsIgnoreCase(st.description())) {
                                        %>
                                        selected="selected" 
                                        <%
                                        }
                                        %>>
                                        <%= st.description()%> 
                                </option>
                                <%}%>
                            </select>
                        </td>
                       </tr>
                       <tr>
                           <td> Học Vị </td>
                           <td> 
                               <select id="txt-hocvi" class="input-minwidth">
                                    <%
                                    // Retrieve Student status
                                    for(HocVi st : EnumSet.allOf(HocVi.class)) {
                                    %>
                                    <option value="<%= st.value()%>"
                                            <%
                                            if (lecturer.getHocVi().equalsIgnoreCase(st.description())) {
                                            %>
                                            selected="selected"
                                            <%
                                            }
                                            %>>
                                            <%= st.description()%>
                                    </option>
                                    <%}%>
                                </select>
                           </td>
                       </tr>
                       <tr>
                           <td> Ghi chú </td>
                           <td> <input type="text" id="txt-note" value="<%= lecturer.getNote() %>"> </td>
                       </tr>
                       
                   </table>
               </div>
               <div class="clear"></div>
               <div id="import-student-button">
                    <%--input type="button" value="Kiểm tra" onclick="checkAddOneStudent()" /--%>
                    <input type="button" value="Cập nhật" onclick="updateLecturer()" />
                </div>
                <div id="update-result">
                    
                </div>
               <% } %>
                
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/AjaxUtil.js"></script>
    <SCRIPT language = "javascript">
        var http = createRequestObject();
        
        function updateLecturer() {
            alert("Entered Update function...");
            if (!validateData()) {
                return;
            }
            var controller = "../../ManageLecturerController?function=update" 
                            + getInformationFromForm();
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
                var detail = document.getElementById("update-result");
                detail.innerHTML = http.responseText;
            }
        }
        
        function validateData() {
            var fullName = "";
            var cmnd = "";
            var address = "";
            try {
                fullName = document.getElementById("txt-fullname").value;
                birthDay = document.getElementById("txt-birthday").value;
                cmnd = document.getElementById("txt-cmnd").value;
                address = document.getElementById("txt-home").value;
            } catch(err) {
                alert("[EditLecturer][Validate] - An error occur:" + err);
                return;
            }
            var error = "";
            if ((fullName == null) || (fullName.length == 0))
                error += "Họ tên, ";
            if ((birthDay == null) || (birthDay.length == 0))
                error += "Ngày sinh, ";
            if ((cmnd == null) || (cmnd.length == 0))
                error += "CMND, ";
            if ((address == null) || (address.length == 0))
                error += "Quê quán, ";
            if (error.length > 0) {
                alert("Các trường sau không được để trống: " + error);
                return false;
            } else {
                // Validate something else...
                return true;
            }
        }
        
        function getInformationFromForm() {
            // Basic information
            var magv = null;
            var fullName = null;
            var birthDay = null;
            var gender = null;
            var cmnd = null;
            var phone = null;
            var email = null;
            var address = null;
            
            // External information from University
            var faculty = null;
            var hocham = null;
            var hocvi = null;
            var note = null;
            try {
                magv = document.getElementById("txt-magv").value;
                fullName = document.getElementById("txt-fullname").value;
                birthDay = document.getElementById("txt-birthday").value;
                cmnd = document.getElementById("txt-cmnd").value;
                address = document.getElementById("txt-home").value;
                gender = document.getElementById("txt-gender").value;
                phone = document.getElementById("txt-phone").value;
                email = document.getElementById("txt-email").value;
                faculty = document.getElementById("txt-faculty").value;
                hocham = document.getElementById("txt-hocham").value;
                hocvi = document.getElementById("txt-hocvi").value;
                note = document.getElementById("txt-note").value;
            } catch(err) {
                alert("[EditLecturer][Validate] - An error occur:" + err);
                return;
            }
            var data = "&magv=" + magv
                        + "&fullname=" + fullName
                        + "&birthDay=" + birthDay
                        + "&cmnd=" + cmnd
                        + "&gender=" + gender
                        + "&phone=" + phone
                        + "&email=" + email
                        + "&address=" + address
                        + "&faculty=" + faculty
                        + "&hocham=" + hocham
                        + "&hocvi=" + hocvi
                        + "&note=" + note;
            return data;
        }
        
    </SCRIPT>
</html>
