<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.DetailTrain"%>
<%@page import="uit.cnpm02.dkhp.model.Lecturer"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page import="uit.cnpm02.dkhp.model.TrainProDetail"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    
    Lecturer lecturer=(Lecturer) session.getAttribute("lecturer");
    List<Subject> subjectList = (List<Subject>) session.getAttribute("subjectList");
    List<DetailTrain> lecsublist = (List<DetailTrain>) session.getAttribute("detainTrainList");
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cập nhật chi tiết giảng dạy</title>
        <style media="all" type="text/css">
            #subjectlist{
                float: left;
                width: 40%;
            } 
            #lecturerSub{
                float: right;
                 width: 75%;
            }
            #button{
                float: left;
                width: 25%;
            }
            #lecturerSub_Show{
               float: right;
                 width: 60%; 
            }
            #AddRemove{
                padding-left: 50px;
            }
            #message{
                text-align: center;
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
            <!--Main Contents-->
            <div id="content">
                <%-- Title --%>
                <div id="main-title">
                    Cập nhật chi tiết giảng dạy của giảng viên: <%= " " +lecturer.getFullName()  %>
                </div>
                <%--List file--%>
                <br />
                <%-- Form add subject to Current train program --%>
                <div class="range">
                    <h3><span id="btn-list-existed-class" class="atag">Cập nhật chi tiết giảng dạy</span></h3> 
                    <div id="content">
                        <div id="subjectlist">
                           <h3><span id="btn-list-existed-class" class="atag">Danh sách môn học của khoa: <%=lecturer.getFacultyCode()%></span></h3> 
                           <select style="width:390px" name="sublist" id="sublist" size="15" onchange="selectedIndexChangeInSubjectList()">
                              <%for(int i=0; i< subjectList.size();i++){%> 
                              <option value="<%=subjectList.get(i).getId()%>"><%=subjectList.get(i).getSubjectName()%></option>
                              <%}%>
                           </select>
                        </div>
                        <div id="lecturerSub_Show">
                            <div id="button">
                                <div id="AddRemove">
                               <br><br><br><br>
                               <input type="button" value="    >>    " onclick="AddSubjectToLecSubList()"/>
                               <br><br><br>
                               <input type="button" value="    <<    " onclick="RemoveSubjectToLecSubList()"/>
                               </div>
                            </div>
                           <div id="lecturerSub">
                            <h3><span id="btn-list-existed-class" class="atag">Danh sách môn học giảng viên đang phụ trách</span></h3>
                            <select style="width:430px" name="lecsublist" id="lecsublist" size="15" onchange="selectedIndexChangeInLecSubjectList()"> 
                               <%for(int i=0; i< lecsublist.size();i++){%> 
                              <option value="<%=lecsublist.get(i).getId().getSubjectCode()%>"><%=lecsublist.get(i).getSubjectName()%></option>
                              <%}%>
                           </select>
                        </div>
                        </div>
                    </div>
                </div>
            <br><br>
            </div><!--End Contents-->
            <br><br><br><br><br><br><br><br><br><br><br><br><br><br>            
            <br><br><br><br><br><br>
            <div id="message">
            </div>
            <div class="button-1">
                    <span class="atag" onclick="updateDetailTrain()" ><img src="../../imgs/check.png"/>Cập nhật</span>
                    <input type="hidden" value="<%=lecturer.getId()%>" id="lecCode" />
            </div>
            
            <br><br>
            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"> </script>
        <script src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            var http = createRequestObject();
             var subjectCodeAdd="";
             var subjectCodeRemove="";
             var lecturerCode=document.getElementById("lecCode").value; 
         function selectedIndexChangeInSubjectList(){
             subjectCodeAdd = document.getElementById("sublist").value; 
             subjectCodeRemove="";
         }
         function selectedIndexChangeInLecSubjectList(){
             subjectCodeRemove = document.getElementById("lecsublist").value; 
             subjectCodeAdd=""
         }
         function AddSubjectToLecSubList(){
             var action="AddDetailTrain";
             if(subjectCodeAdd.length == 0){
              alert("Chưa chọn môn học cần thêm");   
             }else{
                AddRemoveSubject(subjectCodeAdd, lecturerCode, action)
             }
         }
         function RemoveSubjectToLecSubList(){
             var action="RemoveDetailTrain";
             if(subjectCodeRemove.length ==0 ){
              alert("Chưa chọn môn học cần xóa");   
             }else{
                 AddRemoveSubject(subjectCodeRemove, lecturerCode, action)
             }
         }
         function AddRemoveSubject(subCode, lecCode, action){
              if (http) {
                http.open("GET", "../../DetailTrainManager?action="
                    + action + "&subCode=" + subCode + "&lecCode=" +lecCode, true);
                http.onreadystatechange = handleResponseAddRemoveReport;
                http.send(null);
            }
         }
          function handleResponseAddRemoveReport() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("lecsublist");
                detail.innerHTML=http.responseText;
            }
        }
        function updateDetailTrain(){
             if (http) {
                http.open("GET", "../../DetailTrainManager?action=update", true);
                http.onreadystatechange = handleResponseUpdateComplete;
                http.send(null);
            }
        }
        function handleResponseUpdateComplete() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("message");
                detail.innerHTML=http.responseText;
            }
        }
        </script>
    </body>
</html>