<%-- 
    Document   : TrainClassManager
    Created on : 01-04-2012, 08:24:47
    Author     : LocNguyen
--%>

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
                    <u><h3>Danh sách các lớp học đang mở</h3></u>
                </div>
                <hr/><hr/><br>

                <%--Form add new Train subject--%>
                           
                <div id = "list-train-class">
                    <div style="padding-bottom: 10px;
                         padding-left: 10px;
                         ">
                        <input type = "text" id="searchValue" onKeyPress="keypressed()" placeholder = "Nhập thông tin tìm kiếm" />
                        <input type = "button" onclick = "search()" value = "Tìm">
                    </div>
                                      
                    <div id="error">
                        <%
                            if ((error != null) && !error.isEmpty()) { %>
                                <%= error %>
                            <%}
                        %>
                    </div><br>
                    <form id="trainclaslist">
                    <p id="filtername" align="left">
                        Năm học: <select style="width:90px" name="year" id="year" onchange="reloadResult()">
                                    <option value="all">Tất cả</option>
                                    <% for (int i = 0; i < yearList.size(); i++) {%>
                                    <option value="<%=yearList.get(i)%>"><%=yearList.get(i)%></option>
                                    <%}%>
                                </select>
                                Học kỳ: <select style="width:70px" name="semester" id="semester" onchange="reloadResult()">
                                    <option value="0">Tất cả</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                </select>
                    </p>
                    <p id="createLabel" align="right"><b><a href="../../ManageClassController?action=pre_create">Mở lớp học mới</a></b></p>
                    <table id = "table-list-train-class" name = "table-list-train-class">
                        <tr>
                        <th> STT </th>
                        <th> Lớp học </th>
                        <th> Môn học </th>
                        <th> Giảng viên </th>
                        <th> Thứ </th>
                        <th> Phòng </th>
                        <th> Đăng ký </th>
                        <th>Ngày thi</th>
                        <th>Hủy</th>
                        <th>Đóng</th>
                        </tr>
                        <%if ((openedClazzs != null) && (!openedClazzs.isEmpty())) {%>
                        <% for (int i = 0; i < openedClazzs.size(); i++) {%>
                        <tr>
                        <td> <%= (i + 1)%> </td>
                        <td> <a href="../../ManageClassController?action=detail&classID=<%= openedClazzs.get(i).getId().getClassCode()%>&year=<%= openedClazzs.get(i).getId().getYear()%>&semester=<%= openedClazzs.get(i).getId().getSemester()%>"><%= openedClazzs.get(i).getId().getClassCode() %> </a></td>
                        <td> <%= openedClazzs.get(i).getSubjectName()%> </td>
                        <td> <%= openedClazzs.get(i).getLectturerName()%> </td>
                        <td> <%= openedClazzs.get(i).getStudyDate()%> </td>
                        <td> <%= openedClazzs.get(i).getClassRoom() %> </td>
                        <td> <%= openedClazzs.get(i).getNumOfStudentReg()+"/"+openedClazzs.get(i).getNumOfStudent()%> </td>
                        <%if(openedClazzs.get(i).getTestDate() == null ){%>
                            <td>Chưa có</td>
                            <%}else {%>
                            <td><%=openedClazzs.get(i).getTestDate()%></td>
                            <%}%>
                            <td><a href="../../ManageClassController?action=cancel&classID=<%= openedClazzs.get(i).getId().getClassCode()%>">Hủy</a></td>
                            <td><a href="../../ManageClassController?action=close&classID=<%= openedClazzs.get(i).getId().getClassCode()%>">Đóng</a></td>
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

    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var http = createRequestObject();
        var currentpage = 1;
        var http = createRequestObject();
        var numpage = document.getElementById("numpage").value;
        var key = document.getElementById("searchValue").value;
       /* function firstPage(){
            currentpage = 1;
            sendRequest();
        }
        function prePage(){
            currentpage--;
            if(currentpage < 1)
                currentpage = 1;
            sendRequest();
        }
        function nextPage(){
            currentpage ++;
            if(currentpage > numpage)
                currentpage = numpage;
            sendRequest();
        }
        function endPage(){
            currentpage = numpage;
            sendRequest();
        }
        function sendRequest(){
            if(http){
                submitSearchSubject("../../ManageClassController?action=Filter&currentpage=" + currentpage + "&key=" + key);
            }
        }*/
        
       function reloadResult(){
           
             year=document.getElementById("year").value;
             semester=document.getElementById("semester").value;
            SendRequest();
        }
        function SendRequest(){
              if(http){
                http.open("GET", "../../ManageClassController?action=filter&year="+year+"&semester="+semester  ,true);
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
           value= document.getElementById("searchValue").value;
           searchByInputValue();
        }
        function searchByInputValue(){
             if(http){
                http.open("GET", "../../ManageClassController?action=search&value="+value  ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
             }
        }
    </script>
</html>