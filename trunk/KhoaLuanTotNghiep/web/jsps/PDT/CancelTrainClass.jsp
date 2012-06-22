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
            #message{
                text-align: left;
                width: 100%;
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
                <div id="message">
                    
                </div><br><hr/><br>
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
                        <td><input type="button" value="  Hủy lớp  " onclick="autoCancelClass('<%=trainClass.getId().getClassCode()%>',<%=trainClass.getId().getSemester()%>,'<%=trainClass.getId().getYear()%>')" /></td>
                    </tr>
                </table>
               <br><hr>  
                <%--Form add new Train subject--%>
                <%if(studentList.size()>0 && sameClass.size()>0){%>
                <u>Danh sách sinh viên đăng  ký lớp học: <%=studentList.size()%></u>
                <div id = "list-train-class">
                    <form id="trainclaslist">
                    <table id ="table-list-train-class" name = "table-list-train-class" class="general-table">
                        <tr>
                            <th>STT</th>
                            <th>MSSV</th>
                            <th>Họ Tên</th>
                            <th>Lớp</th>
                            <th>Lớp cùng môn</th>
                            <th>Chuyển</th>
                        </tr>
                        <%for (int i = 0; i<studentList.size(); i++ ){%>
                        <tr>
                            <td><%=(i+1)%></td>
                            <td><%=studentList.get(i).getId()%></td>
                            <td><%=studentList.get(i).getFullName()%></td>
                            <td><%=studentList.get(i).getClassCode()%></td>
                            <td>
                                <select id="<%=i%>">
                                   <%for(int j =0; j<sameClass.size(); j++){%>
                                      <option value="<%=sameClass.get(j).getId().getClassCode()%>"><%=sameClass.get(j).getId().getClassCode()%></option>
                                   <%}%>
                                    
                               </select>
                            </td>
                            <td><input type="button" value="  Chuyển  " onclick="moveEachStudent('<%=studentList.get(i).getId()%>','<%=trainClass.getId().getClassCode()%>','<%=i%>', '<%=trainClass.getId().getSemester()%>','<%=trainClass.getId().getYear()%>')" /></td>
                            
                                
                       </tr>
                        <%}%>
                        
                    </table>
                    
                    </form>
                </div>
                   <%}%>
            </div><!--End Contents-->
             
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var http = createRequestObject();
        function autoCancelClass(classCode, classSemester, classYear){
         if(http){
                    http.open("GET", "../../ManageClassController?action=autoCancel&classCode="+ classCode + "&classSemester="+ classSemester + "&classYear="+ classYear  ,true);
                    http.onreadystatechange = cancelResponeHandler;
                    http.send(null);
         }
      }  
      function cancelResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("message");
                detail.innerHTML=http.responseText;
            }
        }
       
      function moveEachStudent(studenttCode, sourceClass, id, classSemester, classYear){
          var desClass = document.getElementById(id).value;
          if(http){
                    http.open("GET", "../../ManageClassController?action=moveStudent&studentCode="+ studenttCode + "&sourceClass="+ sourceClass + "&desClass="+ desClass + "&semester="+ classSemester + "&year="+ classYear  ,true);
                    http.onreadystatechange = moveResponeHandler;
                    http.send(null);
         }
       }
      function moveResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("table-list-train-class");
                detail.innerHTML=http.responseText;
            }
        }  
    </script>
</html>