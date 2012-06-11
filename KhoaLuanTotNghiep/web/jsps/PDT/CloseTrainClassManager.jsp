<%-- 
    Document   : TrainClassManager
    Created on : 01-04-2012, 08:24:47
    Author     : LocNguyen
--%>

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

            #table-list-train-class{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #table-list-train-class th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #table-list-train-class td{
                text-align: center;
                background-color: #5F676D;
            }
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
                color: violet;
            }
            #createLabel{
                padding-right: 15px;
            }
        </style>
    </head>
    <%
        int numpage;
        try {
            numpage = (Integer) session.getAttribute("numpage");
        } catch (Exception ex) {
            numpage = 1;
        }
        
        String error = (String)session.getAttribute("error");
        session.removeAttribute("error");
        
        List<TrainClass> openedClazzs = (List<TrainClass>) session.getAttribute("train-clazzs");
        List<String> yearList=(List<String>) session.getAttribute("yearList");
        List<Faculty> facultyList = (List<Faculty>) session.getAttribute("facultyList");
                
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
                    <u><h3>Danh sách các lớp học đã đóng</h3></u>
                </div>
                <hr/><hr/><br>

                <%--Form add new Train subject--%>
                 <div id="error">
                        <%
                            if ((error != null) && !error.isEmpty()) { %>
                                <%= error %>
                            <%}
                        %>
                    </div>  
                     <b>
                        <a id= "a1" href="uit.edu.vn">Tìm kiếm</a> /
                        <a id="a2" href="uit.edu.vn">Lọc theo khoa</a> /
                        <a id="a3" href="uit.edu.vn">Lọc theo năm và học kỳ</a> 
                    </b>
                    <div id="inputvalue">
                        <input type = "text" id="searchValue" onKeyPress="keypressed()" placeholder = "Nhập thông tin tìm kiếm" />
                        <input type = "button" onclick = "search()" value = "Tìm">
                    </div>
                    <div id="faculty">
                        Khoa: <select id="facultyValue" name="facultyValue" onchange="reloadResultByFaculty()">
                            <option value="All">Tất cả </option>
                            <%for(int i=0; i<facultyList.size();i++){%>
                            <option value="<%=facultyList.get(i).getId()%>"><%=facultyList.get(i).getFacultyName()%></option>
                            <%}%>
                        </select>
                    </div>   
                    <div id="yearAndSemester">
                       Năm học: <select style="width:90px" name="year" id="year" onchange="reloadResultByYAS()">
                                    <option value="all">Tất cả</option>
                                    <% for (int i = 0; i < yearList.size(); i++) {%>
                                    <option value="<%=yearList.get(i)%>"><%=yearList.get(i)%></option>
                                    <%}%>
                                </select>
                       Học kỳ: <select style="width:70px" name="semester" id="semester" onchange="reloadResultByYAS()">
                                    <option value="0">Tất cả</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                       </select>  
                    </div><br>
                    <div id = "list-train-class">
                    <form id="trainclaslist">
                    <table id = "table-list-train-class" name = "table-list-train-class">
                        <tr>
                        <th> STT </th>
                        <th> Lớp học </th>
                        <th> Môn học </th>
                        <th> Giảng viên </th>
                        <th> Thứ </th>
                        <th> Phòng </th>
                        <th> Đăng ký </th>
                        <th> Học kỳ </th>
                        <th> Năm học </th>
                        <th>Mở</th>
                        </tr>
                        <%if ((openedClazzs != null) && (!openedClazzs.isEmpty())) {%>
                        <% for (int i = 0; i < openedClazzs.size(); i++) {%>
                        <tr>
                        <td> <%= (i + 1)%> </td>
                        <td><%= openedClazzs.get(i).getId().getClassCode()%></td>
                        <td> <%= openedClazzs.get(i).getSubjectName()%> </td>
                        <td> <%= openedClazzs.get(i).getLectturerName()%> </td>
                        <td> <%= openedClazzs.get(i).getStudyDate()%> </td>
                        <td> <%= openedClazzs.get(i).getClassRoom() %> </td>
                        <td> <%= openedClazzs.get(i).getNumOfStudentReg()+"/"+openedClazzs.get(i).getNumOfStudent()%> </td>
                        <td> <%= openedClazzs.get(i).getId().getSemester()%> </td>
                        <td> <%= openedClazzs.get(i).getId().getYear()%> </td>
                        <td><span class="atag" onclick="openClass('<%=openedClazzs.get(i).getId().getClassCode()%>',<%=openedClazzs.get(i).getId().getSemester()%>,'<%=openedClazzs.get(i).getId().getYear()%>')">Mở</span></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                    <div id="page">
                        <input type="button" value="|<<" onclick="firstPage()"/>- 
                        <input type="button" value="<<" onclick="prePage()"/>-
                        <input type="button" value=">>" onclick="nextPage()"/>-
                        <input type="button" value=">>|" onclick="endPage()"/>
                        <input type="hidden" value="<%= numpage %>" id = "numpage" />
                    </div>
                    </form>
                    <br/>
                </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/jquery-1.7.1.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var http = createRequestObject();
        var currentpage = 1;
        var http = createRequestObject();
        var numpage = document.getElementById("numpage").value;
        var type = "input";
        $(faculty).hide()
        $(yearAndSemester).hide()
    $(a1).click(function(event){
       event.preventDefault();
        $(inputvalue).show()
        $(faculty).hide()
        $(yearAndSemester).hide()
        type = "input";
       });
    $(a2).click(function(event){
       event.preventDefault();
        $(inputvalue).hide()
        $(faculty).show()
        $(yearAndSemester).hide()
        type = "faculty";
    });
     $(a3).click(function(event){
       event.preventDefault();
       $(inputvalue).hide()
       $(faculty).hide()
       $(yearAndSemester).show()
       type = "YAS";
     });
     function openClass(classCode, classSemester, classYear){
         if(type=="input"){
                value= document.getElementById("searchValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=OpenByInput&value="+value + "&curentPage="+ currentpage + "&classCode="+ classCode + "&classSemester="+ classSemester + "&classYear="+ classYear ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else if(type=="faculty"){
               value= document.getElementById("facultyValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=OpenByFaculty&value="+value + "&curentPage="+ currentpage + "&classCode="+ classCode + "&classSemester="+ classSemester + "&classYear="+ classYear  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else{
               year=document.getElementById("year").value;
               semester=document.getElementById("semester").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=OpenPageByYAS&year="+year+"&semester="+semester + "&curentPage="+ currentpage + "&classCode="+ classCode + "&classSemester="+ classSemester + "&classYear="+ classYear  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }
      } 
        function firstPage(){
            currentpage = 1;
            if(currentpage > numpage)
                currentpage = numpage;
            if(type=="input"){
                value= document.getElementById("searchValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByInput&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else if(type=="faculty"){
               value= document.getElementById("facultyValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByFaculty&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else{
               year=document.getElementById("year").value;
               semester=document.getElementById("semester").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByYAS&year="+year+"&semester="+semester + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }
        }
        function prePage(){
            currentpage-=1;
            if(currentpage < 1)
                currentpage = 1;
            if(type=="input"){
                value= document.getElementById("searchValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByInput&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else if(type=="faculty"){
               value= document.getElementById("facultyValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByFaculty&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else{
               year=document.getElementById("year").value;
               semester=document.getElementById("semester").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByYAS&year="+year+"&semester="+semester + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }
        }
        function nextPage(){
            currentpage ++;
            if(currentpage > numpage)
                currentpage = numpage;
            if(type=="input"){
                value= document.getElementById("searchValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByInput&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else if(type=="faculty"){
               value= document.getElementById("facultyValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByFaculty&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else{
               year=document.getElementById("year").value;
               semester=document.getElementById("semester").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByYAS&year="+year+"&semester="+semester + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }
        }
        function endPage(){
            currentpage = numpage;
            if(type=="input"){
                value= document.getElementById("searchValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByInput&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else if(type=="faculty"){
               value= document.getElementById("facultyValue").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByFaculty&value="+value + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }else{
               year=document.getElementById("year").value;
               semester=document.getElementById("semester").value;
               if(http){
                    http.open("GET", "../../ManagerCloseClassController?action=FilterPageByYAS&year="+year+"&semester="+semester + "&curentPage="+ currentpage  ,true);
                    http.onreadystatechange = searchResponeHandler;
                    http.send(null);
                 }
            }
       }
       function reloadResultByFaculty(){
           currentpage = 1;
           facultyCode = document.getElementById("facultyValue").value;
            if(http){
                http.open("GET", "../../ManagerCloseClassController?action=filterByFaculty&value="+facultyCode  ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
             }
       } 
       function reloadResultByYAS(){
             currentpage =1;
             year=document.getElementById("year").value;
             semester=document.getElementById("semester").value;
            SendRequest();
        }
        function SendRequest(){
              if(http){
                http.open("GET", "../../ManagerCloseClassController?action=filterByYAS&year="+year+"&semester="+semester  ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
             }
         }
       function searchResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("table-list-train-class");
                detail.innerHTML=http.responseText;
            }
        }
         function keypressed()
        { 
         if(event.keyCode=='13')
          {
           search();
          } 
         }
        function search(){
           currentpage = 1;
           value= document.getElementById("searchValue").value;
           if(http){
                http.open("GET", "../../ManagerCloseClassController?action=search&value="+value  ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
             }
        }
        
    </script>
</html>