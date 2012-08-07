<%-- 
    Document   : TrainClassManager
    Created on : 01-04-2012, 08:24:47
    Author     : LocNguyen
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
    List<TrainClass> sameClass = (List<TrainClass>) session.getAttribute("classList");
    List<Student> studentList = (List<Student>) session.getAttribute("studentList");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý lớp học</title>
        <style media="all" type="text/css">
            #createLabel{
                padding-right: 15px;
            }
            #detailclass{
                width: 100%;
                text-align: left;
            }
            #detailclass th td{
                text-align: left;
            }
            #message{
                text-align: center;
                max-width: 500px;
             }
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
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Hủy lớp học <%=trainClass.getId().getClassCode()%>
                </div>
                <br/>
                <u>Chi tiết lớp học:</u>
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
                </table>
                <div class="button-1" style="padding: 2px !important; margin-top: 13px;">
                    <span class="atag" onclick="autoCancelClass('<%=trainClass.getId().getClassCode()%>',<%=trainClass.getId().getSemester()%>,'<%=trainClass.getId().getYear()%>')" ><img src="../../imgs/check.png" />Hủy lớp</span>
                </div>
                <br />
                <%-- Response message when cancel train class --%>
                <div id="message" class="msg-response" style="width: 500px;">
                </div>
               <br><hr>  
                <%--Form add new Train subject--%>
                <%if(studentList.size()>0 && sameClass.size()>0){%>
                <u>Danh sách sinh viên đăng  ký lớp học: <%=studentList.size()%> (SV)</u>
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
    
        <script src="../../javascripts/AjaxUtil.js"></script>
        <script  type = "text/javascript" >
            var http = createRequestObject();
            function autoCancelClass(classCode, classSemester, classYear){
                if(http){
                    http.open("GET", "../../ManageClassController?action=autoCancel&classCode=" + classCode
                        + "&classSemester="+ classSemester
                        + "&classYear="+ classYear, true);
                    http.onreadystatechange = cancelResponeHandler;
                    http.send(null);
                 }
            }  
            function cancelResponeHandler() {
                if(http.readyState == 4 && http.status == 200){
                    $('#message').show('slow');
                    var detail = document.getElementById("message");
                    detail.innerHTML = http.responseText;
                    setTimeOut("message", AjaxConstants.SHORT_DELAY);
                }
            }
    
            function moveEachStudent(studenttCode, sourceClass, id, classSemester, classYear){
                var desClass = document.getElementById(id).value;
                if(http){
                    http.open("GET", "../../ManageClassController?action=moveStudent&studentCode="+ studenttCode
                        + "&sourceClass="+ sourceClass
                        + "&desClass="+ desClass
                        + "&semester="+ classSemester
                        + "&year="+ classYear, true);
                    http.onreadystatechange = moveResponeHandler;
                    http.send(null);
                }
            }
            
            function moveResponeHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("table-list-train-class");
                    detail.innerHTML = http.responseText;
                }
            }
        </script>
    </body>
</html>