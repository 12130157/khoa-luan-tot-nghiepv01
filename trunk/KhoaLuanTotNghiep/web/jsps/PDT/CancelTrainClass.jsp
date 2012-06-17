<%-- 
    Document   : TrainClassManager
    Created on : 01-04-2012, 08:24:47
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý lớp học</title>
        <style media="all" type="text/css">

            #title{
                background-color: #2f4e3d;
                text-align: center;
                padding-top: 12px;
                padding-bottom: 10px;
            }
            #page{
                text-align: center;
            }
            #sidebar {
                height:250px;
                overflow:auto;
            }
            a {
                color: #0000ff;
            }
            #createLabel{
                padding-right: 15px;
            }
            #detailclass{
                text-align: left;
            }
            #detailclass th td{
                text-align: left;
            }
        </style>
    </head>
    <%
        TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
        List<TrainClass> sameClass = (List<TrainClass>) session.getAttribute("classList");
        List<Student> studentList = (List<Student>) session.getAttribute("studentList");
                
    %>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <u><h3>Hủy lớp học <%=trainClass.getId().getClassCode()%></h3></u>
                </div>
                <hr/><hr/><br>
                <u>Chi tiết lơp học:</u>
                <table id="detailclass">
                    <tr>
                        <th width="100px">Mã lớp:</th>
                        <td width="200px"><%=trainClass.getId().getClassCode()%></td>
                        <th width="100px">Môn học: </th>
                        <td width="200px"><%=trainClass.getSubjectName()%></td>
                    </tr>
                    <tr>
                        <th >Giảng viên:</th>
                        <td><%=trainClass.getLectturerName()%></td>
                        <th>Ngày học: </th>
                        <td>Thứ <%=trainClass.getStudyDate()%></td>
                    </tr>
                    <tr>
                        <th ></th>
                        <td></td>
                        <th></th>
                        <td><input type="button" value="  Xóa tự động  " onclick="autoCancelClass('<%=trainClass.getId().getClassCode()%>',<%=trainClass.getId().getSemester()%>,'<%=trainClass.getId().getYear()%>')" /></td>
                    </tr>
                </table>

                <%--Form add new Train subject--%>
                <div id = "list-train-class">
                    <form id="trainclaslist">
                    <table id = "table-list-train-class" name = "table-list-train-class" class="general-table">
                        <tr>
                        
                        </tr>
                        
                        
                    </table>
                    
                    </form>
                </div>
                   
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script  type = "text/javascript" >
        function autoCancelClass(classCode, classSemester, classYear){
         alert(classCode);
         alert(classSemester);
         alert(classYear);
      }  
        
    </script>
</html>