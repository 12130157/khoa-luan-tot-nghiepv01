<%-- 
    Document   : EditStudent
    Created on : 16-11-2011, 21:27:49
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@include file="MenuPDT.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    Student student = (Student) session.getAttribute("student");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý sinh viên</title>
        <style media="all" type="text/css">
            #tableliststudent{
                margin-left: 10px;
                margin-top: 20px;
                margin-bottom: 20px;
                width: 740px;
                border: 3px solid #73726E;
            }
            #tableliststudent th{
                height: 32px;
                font-weight: bold;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                text-align: center;
            }
            #tableliststudent td{
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                padding: 2px 5px 2px 5px;
                text-align: left;
            }
            #formsearch{
                margin-top: 10px;
                margin-left: 20px;
                padding: 5px 10px 5px 10px;
                background: url("../../imgs/opaque_10.png") repeat scroll 0 0 transparent;
                border: 3px solid #73726E;
                width: 320px;
            }
            #red{
                margin-left: 32px;
                margin-top: 15px;
                background-color: #e4e4e3;
                width: 250px;
                height: 32px;
            }
            #red:hover {
                border: 2px solid #ff092d;
            }
            #sidebar {
                height:400px;
                overflow:auto;
            }
        </style>
    </head>
    <body onload="">
        <!--Div Wrapper-->
        <div id="wrapper">
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div style="text-align: center;">
                    <u> <h3> Trang chỉnh sửa thông tin Sinh Viên </h3></u>
                </div>
                <p id="error">

                </p>

                <% if (student != null) { %>
               <table>
                   <tr> <td style="width: 150px">  MSSV  </td> <td style="width: 320px" > <%= student.getId() %> </td> </tr>
                   <tr> <td> Họ và tên </td> <td> <input type="text" value="<%= student.getFullName() %>"> </td> </tr>
                   <tr> <td> Ngày sinh </td> <td> <input type="text" value="<%= student.getBirthday() %>"> </td> </tr>
                   <tr> <td> Giới tính </td> <td> <input type="text" value="<%= student.getGender() %>"> </td> </tr>
                   <tr> <td> CMND </td> <td> <input type="text" value="<%= student.getIdentityNumber() %>"> </td> </tr>
                   <tr> <td> Quê quán </td> <td> <input type="text" value=" <%= student.getHomeAddr() %>"> </td> </tr>
                   <tr> <td> Địa chỉ thường trú </td> <td> <input type="text" value="<%= student.getAddress() %>"> </td> </tr>
                   <tr> <td> Số điện thoại </td> <td> <input type="text" value=" <%= student.getPhone() %>"> </td> </tr>
                   <tr> <td> Email </td> <td> <input type="text" value="<%= student.getEmail() %>"> </td> </tr>
                   <tr> <td> Mã lớp </td> <td> <input type="text" value="<%= student.getClassCode() %>"> </td> </tr>
                   <tr> <td> Mã khoa </td> <td> <input type="text" value="<%= student.getFacultyCode() %>"> </td> </tr>
                   <tr> <td> Mã khóa học </td> <td> <input type="text" value="<%= student.getCourseCode() %>"> </td> </tr>
                   <tr> <td> Tình trạng </td> <td> <input type="text" value="<%= student.getStatus() %>"> </td> </tr>
                   <tr> <td> Bậc học </td> <td><input type="text" value=" <%= student.getStudyLevel() %>"> </td> </tr>
                   <tr> <td> Ngày nhập học </td> <td> <input type="text" value="<%= student.getDateStart() %>"> </td> </tr>
                   <tr> <td> Loại </td> <td> <input type="text" value="<%= student.getStudyType() %>"> </td> </tr>
                   <tr> <td> Ghi chú </td> <td> <input type="text" value="<%= student.getNote() %>"> </td> </tr>
                   <tr style="margin-top: 25px;">
                       <td>  </td>
                       <td> 
                           <input type="button" onclick="" value="Hoàn thành">
                           <input type="button" onclick="" value="Hủy">
                       </td>
                   </tr>
               </table>
               <% } %>
                
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <SCRIPT language = "javascript">
        function createRequestObject(){
            var req;
            if(window.XMLHttpRequest){
                req = new XMLHttpRequest();
            } else if(window.ActiveXObject){
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } else{
                alert('Functions does not support you Brower');
            }
            return req;
        }
 
        function handleResponse(){
            if(http.readyState == 4 && http.status == 200){
                var detail = document.getElementById("error");
                detail.innerHTML = http.responseText;
            }
        }
        
    </SCRIPT>
</html>
